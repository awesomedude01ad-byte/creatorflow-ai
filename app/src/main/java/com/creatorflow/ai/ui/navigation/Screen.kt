package com.creatorflow.ai.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String = "",
    val icon: ImageVector? = null,
    val selectedIcon: ImageVector? = null,
) {
    data object Splash : Screen("splash")
    data object Login : Screen("login", "Sign In")
    data object SignUp : Screen("signup", "Create Account")
    data object ForgotPassword : Screen("forgot_password", "Reset Password")
    data object Dashboard : Screen("dashboard", "Dashboard", icon = Icons.Outlined.Dashboard, selectedIcon = Icons.Filled.Dashboard)
    data object YouTube : Screen("youtube", "YouTube", icon = Icons.Outlined.PlayCircle, selectedIcon = Icons.Filled.PlayCircle)
    data object Instagram : Screen("instagram", "Instagram", icon = Icons.Outlined.Camera, selectedIcon = Icons.Filled.Camera)
    data object AIScript : Screen("ai_script", "AI Script", icon = Icons.Outlined.AutoAwesome, selectedIcon = Icons.Filled.AutoAwesome)
    data object AIThumbnail : Screen("ai_thumbnail", "Thumbnail")
    data object Scheduler : Screen("scheduler", "Schedule", icon = Icons.Outlined.CalendarMonth, selectedIcon = Icons.Filled.CalendarMonth)
    data object Notifications : Screen("notifications", "Alerts", icon = Icons.Outlined.Notifications, selectedIcon = Icons.Filled.Notifications)
    data object Subscription : Screen("subscription", "Plans")
    data object Settings : Screen("settings", "Settings")
    data object AdminPanel : Screen("admin", "Admin Panel")
    data object AdminConfig : Screen("admin/config", "Configuration")
    data object AdminBank : Screen("admin/bank", "Bank Settings")
    data object PaymentHistory : Screen("admin/payments", "Payment History")
    data object Withdrawals : Screen("admin/withdrawals", "Settlements")
    data object InvoiceView : Screen("subscription/invoices", "GST Invoices")
}