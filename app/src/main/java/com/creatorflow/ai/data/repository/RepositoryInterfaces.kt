package com.creatorflow.ai.data.repository

import com.creatorflow.ai.data.model.*
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    val isLoggedIn: Flow<Boolean>
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun signUpWithEmail(email: String, password: String, name: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun signOut()
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
    fun getPayments(status: String? = null): Flow<List<PaymentTransaction>>
    suspend fun getRevenueStats(): Result<Map<String, Double>>
}

interface SubscriptionRepository {
    fun getPlans(): List<SubscriptionPlan>
    suspend fun purchasePlan(userId: String, planId: String, billingCycle: String): Result<String>
}

interface NotificationRepository {
    fun getUserNotifications(userId: String): Flow<List<CreatorFlowNotification>>
    suspend fun markAsRead(userId: String, notificationId: String): Result<Unit>
}

interface AnalyticsRepository {
    fun logEvent(name: String, params: Map<String, Any> = emptyMap())
    fun logScreenView(screenName: String)
}