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
            trySend(fb.currentUser?.let { User(uid=it.uid, email=it.email?:"", displayName=it.displayName) })
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }.distinctUntilChanged()
    override val isLoggedIn: Flow<Boolean> = currentUser.map { it != null }
    override suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(User(uid=result.user!!.uid, email=result.user!!.email?:""))
        } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun signUpWithEmail(email: String, password: String, name: String): Result<User> {
        return try { val r = auth.createUserWithEmailAndPassword(email, password).await(); Result.success(User(uid=r.user!!.uid)) } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try { val r = auth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null)).await(); Result.success(User(uid=r.user!!.uid)) } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun sendPasswordReset(email: String): Result<Unit> = try { auth.sendPasswordResetEmail(email).await(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override suspend fun signOut() { auth.signOut() }
}

@Singleton
class UserRepositoryImpl @Inject constructor(private val fs: FirebaseFirestore) : UserRepository {
    override suspend fun getUser(uid: String): Result<User> = try { Result.success(User(uid=uid)) } catch (e: Exception) { Result.failure(e) }
    override suspend fun updateUser(uid: String, data: Map<String, Any>): Result<Unit> = try { fs.collection("users").document(uid).set(data).await(); Result.success(Unit) } catch (e: Exception) { Result.failure(e) }
    override fun getUserFlow(uid: String): Flow<User?> = flowOf(null)
}

@Singleton
class ScriptRepositoryImpl @Inject constructor(private val fs: FirebaseFirestore) : ScriptRepository {
    override suspend fun generateScript(topic: String, platform: String, category: String, tone: String, duration: Int): Result<String> {
        kotlinx.coroutines.delay(2000L)
        return Result.success("[INTRO]\nHey everyone! Today we're diving deep into \"$topic\".\n\n[HOOK]\n$topic is completely changing the game in $category.\n\n[MAIN CONTENT]\nLet's break down $topic step by step...\n\n[CALL TO ACTION]\nLike, subscribe, and comment your thoughts!")
    }
    override suspend fun saveScript(script: Script): Result<String> = try { val ref = fs.collection("users").document(script.userId).collection("scripts").document(); Result.success(ref.id) } catch (e: Exception) { Result.failure(e) }
    override fun getUserScripts(userId: String): Flow<List<Script>> = flowOf(emptyList())
    override suspend fun deleteScript(userId: String, scriptId: String): Result<Unit> = Result.success(Unit)
}

@Singleton
class PaymentRepositoryImpl @Inject constructor(private val fs: FirebaseFirestore) : PaymentRepository {
    override suspend fun createRazorpayOrder(amount: Double, planName: String, userId: String, userEmail: String): Result<String> {
        kotlinx.coroutines.delay(500L); Result.success("order_${System.currentTimeMillis()}")
    }
    override fun getPayments(status: String?): Flow<List<PaymentTransaction>> = flowOf(emptyList())
    override suspend fun getRevenueStats(): Result<Map<String, Double>> = Result.success(emptyMap())
}

@Singleton
class SubscriptionRepositoryImpl @Inject constructor() : SubscriptionRepository {
    override fun getPlans(): List<SubscriptionPlan> = listOf(
        SubscriptionPlan("free", "Free", "free", 0.0, 0.0, 0.0, listOf("5 AI scripts/mo", "Basic templates", "Standard support")),
        SubscriptionPlan("pro_monthly", "Pro Monthly", "pro", 999.0, 0.0, 0.0, listOf("100 AI scripts/mo", "AI thumbnails", "Video scheduling", "YouTube & IG automation", "Priority support")),
        SubscriptionPlan("enterprise", "Enterprise", "enterprise", 0.0, 0.0, 19999.0, listOf("Unlimited AI scripts", "Unlimited thumbnails", "YouTube & IG automation", "Team collaboration", "24/7 priority support"))
    )
    override suspend fun purchasePlan(userId: String, planId: String, billingCycle: String): Result<String> = Result.success("order_${System.currentTimeMillis()}")
}

@Singleton
class NotificationRepositoryImpl @Inject constructor(private val fs: FirebaseFirestore) : NotificationRepository {
    override fun getUserNotifications(userId: String): Flow<List<CreatorFlowNotification>> = flowOf(emptyList())
    override suspend fun markAsRead(userId: String, notificationId: String): Result<Unit> = Result.success(Unit)
}

@Singleton
class AnalyticsRepositoryImpl @Inject constructor(private val analytics: FirebaseAnalytics) : AnalyticsRepository {
    override fun logEvent(name: String, params: Map<String, Any>) { val b = android.os.Bundle(); params.forEach { (k, v) -> when(v) { is String -> b.putString(k, v); is Int -> b.putInt(k, v); is Double -> b.putDouble(k, v); is Boolean -> b.putBoolean(k, v) } }; analytics.logEvent(name, b) }
    override fun logScreenView(screenName: String) { logEvent("screen_view", mapOf("screen_name" to screenName)) }
}