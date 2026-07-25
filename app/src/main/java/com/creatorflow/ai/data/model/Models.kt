package com.creatorflow.ai.data.model

import com.squareup.moshi.JsonClass
import java.util.Date

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String? = null,
    val photoUrl: String? = null,
    val phone: String? = null,
    val role: String = "user",
    val subscriptionTier: String = "free",
    val scriptsGeneratedThisMonth: Int = 0,
)

data class Script(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val platform: String = "youtube",
    val category: String = "Tutorial",
    val tone: String = "professional",
    val targetDuration: Int = 300,
    val tags: List<String> = emptyList(),
    val createdAt: Date = Date(),
)

data class Thumbnail(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val platform: String = "youtube",
    val style: String = "modern",
    val createdAt: Date = Date(),
)

data class ScheduledVideo(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val platform: String = "youtube",
    val scheduledTime: Date = Date(),
    val status: String = "pending",
    val tags: List<String> = emptyList(),
    val createdAt: Date = Date(),
)

data class CreatorFlowNotification(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val body: String = "",
    val type: String = "system",
    val isRead: Boolean = false,
    val createdAt: Date = Date(),
)

data class SubscriptionPlan(
    val id: String = "",
    val name: String = "",
    val tier: String = "free",
    val monthlyPrice: Double = 0.0,
    val yearlyPrice: Double = 0.0,
    val lifetimePrice: Double = 0.0,
    val features: List<String> = emptyList(),
)

data class PaymentTransaction(
    val id: String = "",
    val razorpayPaymentId: String = "",
    val razorpayOrderId: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val planName: String = "",
    val amount: Double = 0.0,
    val gstAmount: Double = 0.0,
    val totalAmount: Double = 0.0,
    val status: String = "created",
    val paymentMethod: String = "upi",
    val invoiceNumber: String = "",
    val createdAt: Date = Date(),
)

data class DashboardStats(
    val totalScripts: Int = 0,
    val totalThumbnails: Int = 0,
    val scriptsThisMonth: Int = 0,
    val thumbnailsThisMonth: Int = 0,
    val publishedVideos: Int = 0,
    val pendingVideos: Int = 0,
    val totalScheduled: Int = 0,
)

data class BankAccount(
    val id: String = "",
    val accountHolder: String = "",
    val bankName: String = "",
    val accountNumber: String = "",
    val ifscCode: String = "",
    val isVerified: Boolean = false,
)

data class GSTInvoice(
    val invoiceNumber: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val taxableAmount: Double = 0.0,
    val cgstAmount: Double = 0.0,
    val sgstAmount: Double = 0.0,
    val totalGst: Double = 0.0,
    val grandTotal: Double = 0.0,
    val planName: String = "",
    val invoiceDate: Date = Date(),
)

sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : Resource<Nothing>()
}