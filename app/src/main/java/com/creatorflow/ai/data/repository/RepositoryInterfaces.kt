package com.creatorflow.ai.data.repository

import com.creatorflow.ai.data.model.*
import kotlinx.coroutines.flow.Flow

// ════════════════════════════════════════════════════════════Т��&W�6�F�'���FW&f6W2�6�V�&6��FV7GW&R&�V�F'�����)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y)Y

interface AuthRepository {
    val currentUser: Flow<User?>
    val isLoggedIn: Flow<Boolean>
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun signUpWithEmail(email: String, password: String, name: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signInWithPhone(verificationId: String, smsCode: String): Result<User>
    suspend fun sendPhoneVerification(phone: String): Result<String>
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun signOut()
    suspend fun deleteAccount(): Result<Unit>
}

interface UserRepository {
    suspend fun getUser(uid: String): Result<User>
    suspend fun updateUser(uid: String, data: Map<String, Any>): Result<Unit>
    fun getUserFlow(uid: String): Flow<User?>
}

interface ScriptRepository {
    suspend fun generateScript(topic: String, platform: String, category: String, tone: String, duration: Int): Result<String>
    suspend fun saveScript(script: Script): Result<String>
    fun getUserScripts(userId: String): Flow<List<Script>>
    suspend fun deleteScript(userId: String, scriptId: String): Result<Unit>
}

interface PaymentRepository {
    suspend fun createRazorpayOrder(amount: Double, planName: String, userId: String, userEmail: String): Result<String>
    suspend fun verifyPayment(paymentId: String, orderId: String, signature: String): Result<Boolean>
    fun getPayments(status: String? = null): Flow<List<PaymentTransaction?>
    fun getUserPayments(userId: String): Flow<List<PaymentTransaction>>
    suspend fun getRevenueStats(): Result<Map<String, Double>>
    suspend fun refundPayment(paymentId: String, amount: Double?): Result<Unit>
}

interface SubscriptionRepository {
    fun getPlans(): List<SubscriptionPlan>
    suspend fun purchasePlan(userId: String, planId: String, billingCycle: String): Result<String>
    suspend fun restorePurchases(userId: String): Result<Boolean>
}

interface NotificationRepository {
    fun getUserNotifications(userId: String): Flow<List<CreatorFlowNotification>>
    suspend fun markAsRead(userId: String, notificationId: String): Result<Unit>
    suspend fun markAllAsRead(userId: String): Result<Unit>
}

interface AnalyticsRepository {
    fun logEvent(name: String, params: Map<String, Any> = emptyMap())
    fun logScreenView(screenName: String)
    fun logError(error: String, context: String = "unknown")
    fun setUserProperties(userId: String, tier: String? = null)
}
