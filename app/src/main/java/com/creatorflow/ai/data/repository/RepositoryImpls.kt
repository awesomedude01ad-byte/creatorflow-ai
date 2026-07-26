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

// ═══════════════════════════════════════════════════════════
// Repository implementations
// ═══════════════════════════════════════════════════════════

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val fbUser = firebaseAuth.currentUser
            trySend(fbUser?.let { fb ->
                User(
                    uid = fb.uid,
                    email = fb.email ?: "",
                    displayName = fb.displayName,
                    photoUrl = fb.photoUrl?.toString(),
                    phone = fb.phoneNumber,
                )
            })
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }.distinctUntilChanged()

    override val isLoggedIn: Flow<Boolean> = currentUser.map { it != null }

    override suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!.toDomainUser())
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String, name: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            Result.success(result.user!!.toDomainUser())
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            Result.success(result.user!!.toDomainUser())
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    override suspend fun signOut() { auth.signOut() }

    override suspend fun signInWithPhone(verificationId: String, smsCode: String): Result<User> {
        return try {
            val credential = com.google.firebase.auth.PhoneAuthProvider.getCredential(verificationId, smsCode)
            val result = auth.signInWithCredential(credential).await()
            Result.success(result.user!!.toDomainUser())
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun sendPhoneVerification(phone: String): Result<String> {
        val result = kotlinx.coroutines.suspendCancellableCoroutine<String> { cont ->
            val callbacks = object : com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) { cont.resumeWith(Result.success("auto-verified")) }
                override fun onVerificationFailed(e: com.google.firebase.FirebaseException) { cont.resumeWith(Result.failure(e)) }
                override fun onCodeSent(id: String, token: com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken) { cont.resumeWith(Result.success(id)) }
            }
            com.google.firebase.auth.PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, java.util.concurrent.TimeUnit.SECONDS, android.app.Activity(), callbacks)
        }
        return result
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            auth.currentUser?.delete()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun com.google.firebase.auth.FirebaseUser.toDomainUser() = User(
        uid = uid, email = email ?: "", displayName = displayName,
        photoUrl = photoUrl?.toString(), phone = phoneNumber,
    )

    private fun mapFirebaseError(e: FirebaseAuthException): String = when (e.errorCode) {
        "ERROR_INVALID_EMAIL" -> "Please enter a valid email"
        "ERROR_WRONG_PASSWORD" -> "Incorrect password"
        "ERROR_USER_NOT_FOUND" -> "No account found with this email"
        "ERROR_EMAIL_ALREADY_IN_USE" -> "Email already registered"
        "ERROR_WEAK_PASSWORD" -> "Password should be at least 6 characters"
        "ERROR_USER_DISABLED" -> "This account has been disabled"
        "ERROR_TOO_MANY_REQUESTS" -> "Too many attempts. Try again later"
        "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Check your connection"
        else -> e.message ?: "Authentication failed"
    }
}

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getUser(uid: String): Result<User> {
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            Result.success(doc.toDomainUser(uid))
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun updateUser(uid: String, data: Map<String, Any>): Result<Unit> {
        return try {
            firestore.collection("users").document(uid).update(data).await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    override fun getUserFlow(uid: String): Flow<User?> = callbackFlow {
        val listener = firestore.collection("users").document(uid).addSnapshotListener { snap, e ->
            if (e != null) { close(e); return@addSnapshotListener }
            trySend(snap?.toDomainUser(uid))
        }
        awaitClose { listener.remove() }
    }

    private fun com.google.firebase.firestore.DocumentSnapshot?.toDomainUser(uid: String): User {
        val d = this?.data ?: emptyMap()
        return User(
            uid = uid,
            email = d["email"] as? String ?: "",
            displayName = d["displayName"] as? String,
            role = d["role"] as? String ?: "user",
            subscriptionTier = d["subscriptionTier"] as? String ?: "free",
            scriptsGeneratedThisMonth = (d["scriptsGeneratedThisMonth"] as? Long)?.toInt() ?: 0,
        )
    }
}

@Singleton
class ScriptRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ScriptRepository {

    override suspend fun generateScript(topic: String, platform: String, category: String, tone: String, duration: Int): Result<String> {
        // In production, call your AI API backend (OpenAI, Claude, etc.)
        kotlinx.coroutines.delay(2000L)
        val script = buildString {
            appendLine("[INTRO - 0:00-0:30]")
            appendLine("Hey everyone! Today we're diving deep into \"$topic\".")
            appendLine()
            appendLine("[HOOK - 0:30-1:00]")
            appendLine("$topic is completely changing the game in $category. Here's what you need to know.")
            appendLine()
            appendLine("[MAIN CONTENT - 1:00-${(duration / 60) - 1}:00]")
            appendLine("Let's break down $topic step by step...")
            appendLine()
            appendLine("[CALL TO ACTION]")
            appendLine("Like, subscribe, and comment your thoughts on $topic!")
        }
        return Result.success(script)
    }

    override suspend fun saveScript(script: Script): Result<String> {
        return try {
            val ref = firestore.collection("users").document(script.userId).collection("scripts").document()
            ref.set(script).await()
            Result.success(ref.id)
        } catch (e: Exception) { Result.failure(e) }
    }

    override fun getUserScripts(userId: String): Flow<List<Script>> = callbackFlow {
        val listener = firestore.collection("users").document(userId).collection("scripts")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snap, e ->
                if (e != null) { close(e); return@addSnapshotListener }
                trySend(snap?.documents?.mapNotNull { it.toScript() } ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    override suspend fun deleteScript(userId: String, scriptId: String): Result<Unit> {
        return try {
            firestore.collection("users").document(userId).collection("scripts").document(scriptId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toScript(): Script = Script(
        id = id,
        userId = getString("userId") ?: "",
        title = getString("title") ?: "",
        content = getString("content") ?: "",
        platform = getString("platform") ?: "youtube",
        category = getString("category") ?: "Tutorial",
    )
}

@Singleton
class PaymentRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PaymentRepository {

    override suspend fun createRazorpayOrder(amount: Double, planName: String, userId: String, userEmail: String): Result<String> {
        // In production: POST to your backend API → backend calls Razorpay API
        kotlinx.coroutines.delay(500L)
        return Result.success("order_${System.currentTimeMillis()}")
    }

    override suspend fun verifyPayment(paymentId: String, orderId: String, signature: String): Result<Boolean> {
        kotlinx.coroutines.delay(500L)
        return Result.success(true)
    }

    override fun getPayments(status: String?): Flow<List<PaymentTransaction>> = callbackFlow {
        var query: com.google.firebase.firestore.Query = firestore.collection("payments").orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
        if (status != null) query = query.whereEqualTo("status", status)
        val listener = query.addSnapshotListener { snap, e ->
            if (e != null) { close(e); return@addSnapshotListener }
            trySend(snap?.documents?.mapNotNull { it.toPayment() } ?: emptyList())
        }
        awaitClose { listener.remove() }
    }

    override fun getUserPayments(userId: String): Flow<List<PaymentTransaction>> = callbackFlow {
        val listener = firestore.collection("payments")
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snap, e ->
                if (e != null) { close(e); return@addSnapshotListener }
                trySend(snap?.documents?.mapNotNull { it.toPayment() } ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getRevenueStats(): Result<Map<String, Double>> {
        return try {
            val snap = firestore.collection("payments").whereEqualTo("status", "captured").get().await()
            var rev = 0.0; var gst = 0.0; var refunded = 0.0
            for (doc in snap.documents) {
                rev += (doc.getDouble("amount") ?: 0.0)
                gst += (doc.getDouble("gstAmount") ?: 0.0)
                refunded += (doc.getDouble("refundAmount") ?: 0.0)
            }
            Result.success(mapOf("totalRevenue" to rev, "totalGst" to gst, "totalRefunded" to refunded, "netRevenue" to (rev - refunded)))
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun refundPayment(paymentId: String, amount: Double?): Result<Unit> {
        kotlinx.coroutines.delay(500L)
        return Result.success(Unit)
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toPayment(): PaymentTransaction = PaymentTransaction(
        id = id,
        razorpayPaymentId = getString("razorpayPaymentId") ?: "",
        userId = getString("userId") ?: "",
        userEmail = getString("userEmail") ?: "",
        planName = getString("planName") ?: "",
        amount = getDouble("amount") ?: 0.0,
        totalAmount = getDouble("totalAmount") ?: 0.0,
        status = getString("status") ?: "created",
        invoiceNumber = getString("invoiceNumber") ?: "",
        paymentMethod = getString("paymentMethod") ?: "upi",
    )
}

@Singleton
class SubscriptionRepositoryImpl @Inject constructor() : SubscriptionRepository {
    override fun getPlans(): List<SubscriptionPlan> = listOf(
        SubscriptionPlan("free", "Free", "free", 0.0, 0.0, 0.0, listOf("5 AI scripts/mo", "Basic templates", "Standard support")),
        SubscriptionPlan("pro_monthly", "Pro Monthly", "pro", 999.0, 0.0, 0.0, listOf("100 AI scripts/mo", "AI thumbnails", "Video scheduling", "YouTube & IG automation", "Priority support")),
        SubscriptionPlan("pro_yearly", "Pro Yearly", "pro", 0.0, 7999.0, 0.0, listOf("Everything in Pro", "2 months free", "GST invoice")),
        SubscriptionPlan("enterprise", "Enterprise", "enterprise", 0.0, 0.0, 19999.0, listOf("Unlimited AI scripts", "Unlimited thumbnails", "YouTube & IG automation", "Team collaboration", "White-label", "24/7 priority support")),
    )

    override suspend fun purchasePlan(userId: String, planId: String, billingCycle: String): Result<String> {
        kotlinx.coroutines.delay(300L)
        return Result.success("order_${System.currentTimeMillis()}")
    }

    override suspend fun restorePurchases(userId: String): Result<Boolean> {
        kotlinx.coroutines.delay(500L)
        return Result.success(true)
    }
}

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : NotificationRepository {

    override fun getUserNotifications(userId: String): Flow<List<CreatorFlowNotification>> = callbackFlow {
        val listener = firestore.collection("users").document(userId).collection("notifications")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snap, e ->
                if (e != null) { close(e); return@addSnapshotListener }
                trySend(snap?.documents?.mapNotNull { it.toNotification() } ?: emptyList())
            }
        awaitClose { listener.remove() }
    }

    override suspend fun markAsRead(userId: String, notificationId: String): Result<Unit> {
        return try {
            firestore.collection("users").document(userId).collection("notifications").document(notificationId).update("isRead", true).await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun markAllAsRead(userId: String): Result<Unit> {
        return try {
            val snap = firestore.collection("users").document(userId).collection("notifications").whereEqualTo("isRead", false).get().await()
            val batch = firestore.batch()
            for (doc in snap.documents) batch.update(doc.reference, "isRead", true)
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toNotification() = CreatorFlowNotification(
        id = id,
        userId = getString("userId") ?: "",
        title = getString("title") ?: "",
        body = getString("body") ?: "",
        type = getString("type") ?: "system",
        isRead = getBoolean("isRead") ?: false,
    )
}

@Singleton
class AnalyticsRepositoryImpl @Inject constructor(
    private val analytics: FirebaseAnalytics
) : AnalyticsRepository {

    override fun logEvent(name: String, params: Map<String, Any>) {
        val bundle = android.os.Bundle()
        params.forEach { (k, v) ->
            when (v) {
                is String -> bundle.putString(k, v)
                is Int -> bundle.putInt(k, v)
                is Long -> bundle.putLong(k, v)
                is Double -> bundle.putDouble(k, v)
                is Boolean -> bundle.putBoolean(k, v)
            }
        }
        analytics.logEvent(name, bundle)
    }

    override fun logScreenView(screenName: String) {
        logEvent("screen_view", mapOf("screen_name" to screenName))
    }

    override fun logError(error: String, context: String) {
        logEvent("app_error", mapOf("error" to error, "context" to context))
    }

    override fun setUserProperties(userId: String, tier: String?) {
        analytics.setUserId(userId)
        if (tier != null) analytics.setUserProperty("subscription_tier", tier)
    }
}
