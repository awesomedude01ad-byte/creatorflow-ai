package com.creatorflow.ai.data.repository

import com.creatorflow.ai.data.model.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {
    override val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { fb ->
            trySend(fb.currentUser?.let { User(uid=it.uid, email=it.email?:"" displayName=it.displayName) })
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }.distinctUntilChanged()
    override val isLoggedIn: Flow<Boolean> = currentUser.map { it != null }
    override suspend fun signInWithEmail(email: String password: String): Result<User> { return try { val r = auth.signInWithEmailAndPassword(email password).await(); Result.success(r.user!!.toDomainUser()) } catch (e: Exception) { Result.failure(e) } }
    override suspend fun signUpWithEmail(email: String password: String name: String): Result<User> { return try { val r = auth.createUserWithEmailAndPassword(email password).await(); r.user?.updateProfile(com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await(); Result.success(r.user!!.toDomainUser()) } catch (e: Exception) { Result.failure(e) } }
    override suspend fun signInWithGoogle(idToken: String): Result<User> { return try { val r = auth.signInWithCredential(GoogleAuthProvider.getCredential(idToken null)).await(); Result.success(r.user!!.toDomainUser()) } catch (e: Exception) { Result.failure(e) } }
    override suspend fun signInWithPhone(verificationId: String smsCode: String): Result<User> { return try { val credential = com.google.firebase.auth.PhoneAuthProvider.getCredential(verificationId smsCode); val r = auth.signInWithCredential(credential).await(); Result.success(r.user!!.toDomainUser()) } catch (e: Exception) { Result.failure(e) } }
    override suspend fun sendPhoneVerification(phone: String): Result<String> {
        val result = kotlinx.coroutines.suspendCancellableCoroutine<String> { cont ->
            val cb = object : com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(c: com.google.firebase.auth.PhoneAuthCredential) { cont.resumeWith(Result.success("auto")) }
                override fun onVerificationFailed(e: com.google.firebase.FirebaseException) { cont.resumeWith(Result.failure(e)) }
                override fun onCodeSent(id: String token: com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken) { cont.resumeWith(Result.success(id)) }
            }
            com.google.firebase.auth.PhoneAuthProvider.getInstance().verifyPhoneNumber(phone 60 java.util.concurrent.TimeUnit.SECONDS android.app.Activity() cb)
        }
        return result
    }
    override suspend fun sendPasswordReset(email: String): Result<Unit> = try { auth.sendPasswordResetEmail(email).await(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override suspend fun signOut() { auth.signOut() }
    override suspend fun deleteAccount(): Result<Unit> { return try { auth.currentUser?.delete()?.await(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) } }
    private fun com.google.firebase.auth.FirebaseUser.toDomainUser() = User(uid=uid email=email?:"" displayName=displayName photoUrl=photoUrl?.toString() phone=phoneNumber)
    private fun mapFirebaseError(e: FirebaseAuthException): String = when (e.errorCode) {
        "ERROR_INVALID_EMAIL" -> "Please enter a valid email"
        "INCORRECT_PASSWORD" -> "Incorrect password"
        else -> e.message ?: "Auth failed"
    }
}

@Singleton
class UserRepositoryImpl @Inject constructor(private val fs: FirebaseFirestore) : UserRepository {
    override suspend fun getUser(uid: String): Result<User> = try { doc = fs.collection("users").document(uid).get().await(); Result.success(doc.toDomainUser(uid)) } catch (e: Exception) { Result.failure(e) }
    override suspend fun updateUser(uid: String data: Map<String Any>): Result<Unit> = try { fs.collection("users").document(uid).set(data).await(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override fun getUserFlow(uid: String): Flow<User?> = flowOf(null)
}

@Singleton
class ScriptRepositoryImpl @Inject constructor(private val fs: FirebaseFirestore) : ScriptRepository {
    override suspend fun generateScript(topic: String platform: String category: String tone: String duration: Int): Result<String> { kotlinx.coroutines.delay(2000L); return Result.success("INTRO\nHey everyone! Today we're diving into $topic.\nHOOK\n$topic changing $category.\nMAIN\nStep by step breakdown...\nCTA\nLike and subscribe!") }
    override suspend fun saveScript(script: Script): Result<String> = try { val ref = fs.collection("users").document(script.userId).collection("scripts").document(); ref.set(script).await(); Result.success(ref.id) } catch (e: Exception) { Result.failure(e) }
    override fun getUserScripts(userId: String): Flow<List<Script>> = flowOf(emptyList())
    override suspend fun deleteScript(userId: String scriptId: String): Result<Unit> = Result.success(Unit)
}

@Singleton
class PaymentRepositoryImpl @Inject constructor(private val fs: FirebaseFirestore) : PaymentRepository {
    override suspend fun createRazorpayOrder(amount: Double planName: String userId: String userEmail: String): Result<String> { kotlinx.coroutines.delay(500L); return Result.success("order_${System.currentTimeMillis()}") }
    override suspend fun verifyPayment(paymentId: String orderId: String signature: String): Result<Boolean> { kotlinx.coroutines.delay(500L); return Result.success(true) }
    override fun getPayments(status: String?): Flow<List<PaymentTransaction>> = flowOf(emptyList())
    override fun getUserPayments(userId: String): Flow<List<PaymentTransaction>> = flowOf(emptyList())
    override suspend fun getRevenueStats(): Result<Map<String Double>> = Result.success(emptyMap())
    override suspend fun refundPayment(paymentId: String amount: Double?): Result<Unit> { kotlinx.coroutines.delay(500L); return Result.success(Unit) }
}

@Singleton
class SubscriptionRepositoryImpl @Inject constructor() : SubscriptionRepository {
    override fun getPlans(): List<SubscriptionPlan> = listOf(
        SubscriptionPlan("free" "Free" "free" 0.0 0.0 0.0 listOf("5 AI scripts/mo" "Basic templates" "Standard support"))
        SubscriptionPlan("pro_monthly" "Pro Monthly" "pro" 999.0 0.0 0.0 listOf("100 AI scripts/mo" "AI thumbnails" "Video scheduling" "YouTube & IG automation" "Priority support"))
        SubscriptionPlan("enterprise" "Enterprise" "enterprise" 0.0 0.0 19999.0 listOf("Unlimited AI scripts" "Unlimited thumbnails" "YouTube & IG automation" "Team collaboration" "24/7 priority support"))
    )
    override suspend fun purchasePlan(userId: String planId: String billingCycle: String): Result<String> { kotlinx.coroutines.delay(300L); return Result.success("order_${System.currentTimeMillis()}") }
    override suspend fun restorePurchases(userId: String): Result<Boolean> { kotlinx.coroutines.delay(500L); return Result.success(true) }
}

@Singleton
class NotificationRepositoryImpl @Inject constructor(private val fs: FirebaseFirestore) : NotificationRepository {
    override fun getUserNotifications(userId: String): Flow<List<CreatorFlowNotification>> = flowOf(emptyList())
    override suspend fun markAsRead(userId: String notificationId: String): Result<Unit> { return try { fs.collection("users").document(userId).collection("notifications").document(notificationId).update("isRead" true).await(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) } }
    override suspend fun markAllAsRead(userId: String): Result<Unit> = Result.success(Unit)
}

@Singleton
class AnalyticsRepositoryImpl @Inject constructor(private val analytics: FirebaseAnalytics) : AnalyticsRepository {
    override fun logEvent(name: String params: Map<String Any>) { val b = android.os.Bundle(); params.forEach { (k, v) -> when(v) { is String -> b.putString(k,v); is Int -> b.putInt(k,v); is Double -> b.putDouble(k,v); is Boolean -> b.putBoolean(k,v) } }; analytics.logEvent(name b) }
    override fun logScreenView(s: String) { logEvent("screen_view" mapOf("screenName" to s)) }
    override fun logError(e: String c: String) { logEvent("app_error" mapOf("error" to e "context" to c)) }
    override fun setUserProperties(uid: String tier: String?) { analytics.setUserId(uid); if (tier != null) analytics.setUserProperty("subscription_tier" tier) }
}