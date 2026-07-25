package com.creatorflow.ai.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.creatorflow.ai.ui.navigation.Screen
import com.creatorflow.ai.ui.screens.splash.SplashScreen
import com.creatorflow.ai.ui.screens.auth.*
import com.creatorflow.ai.ui.screens.dashboard.DashboardScreen
import com.creatorflow.ai.ui.screens.youtube.YouTubeScreen
import com.creatorflow.ai.ui.screens.instagram.InstagramScreen
import com.creatorflow.ai.ui.screens.aiscript.AIScriptScreen
import com.creatorflow.ai.ui.screens.thumbnail.AIThumbnailScreen
import com.creatorflow.ai.ui.screens.scheduler.SchedulerScreen
import com.creatorflow.ai.ui.screens.notifications.NotificationsScreen
import com.creatorflow.ai.ui.screens.subscription.SubscriptionScreen
import com.creatorflow.ai.ui.screens.settings.SettingsScreen
import com.creatorflow.ai.ui.screens.admin.*
import com.creatorflow.ai.ui.screens.onboarding.OnboardingScreen
import com.creatorflow.ai.ui.screens.aichat.AIChatScreen
import com.creatorflow.ai.ui.screens.youtubetools.YouTubeToolsScreen
import com.creatorflow.ai.ui.screens.igtools.InstagramToolsScreen
import com.creatorflow.ai.ui.screens.profile.ProfileScreen

val bottomNavItems = listOf(
    Screen.Dashboard, Screen.YouTube, Screen.Instagram,
    Screen.AIChat, Screen.Profile
)

@Composable
fun CreatorFlowApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CreatorFlowBottomBar(navController, currentRoute)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) { SplashScreen(navController) }
            composable(Screen.Login.route) { LoginScreen(navController) }
            composable(Screen.SignUp.route) { SignUpScreen(navController) }
            composable(Screen.ForgotPassword.route) { ForgotPasswordScreen(navController) }
            composable(Screen.Dashboard.route) { DashboardScreen(navController) }
            composable(Screen.YouTube.route) { YouTubeScreen(navController) }
            composable(Screen.Instagram.route) { InstagramScreen(navController) }
            composable(Screen.AIScript.route) { AIScriptScreen(navController) }
            composable(Screen.AIThumbnail.route) { AIThumbnailScreen(navController) }
            composable(Screen.Scheduler.route) { SchedulerScreen(navController) }
            composable(Screen.Notifications.route) { NotificationsScreen(navController) }
            composable(Screen.Subscription.route) { SubscriptionScreen(navController) }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
            composable(Screen.AdminPanel.route) { AdminPanelScreen(navController) }
            composable(Screen.AdminConfig.route) { AdminConfigScreen(navController) }
            composable(Screen.AdminBank.route) { AdminBankScreen(navController) }
            composable(Screen.PaymentHistory.route) { PaymentHistoryScreen(navController) }
            composable(Screen.Withdrawals.route) { WithdrawalScreen(navController) }
            composable(Screen.InvoiceView.route) { InvoiceViewScreen(navController) }
            composable(Screen.Onboarding.route) { OnboardingScreen(navController) }
            composable(Screen.AIChat.route) { AIDhatScreen(navController) }
            composable(Screen.YouTubeTools.route) { YouTubeToolsScreen(navController) }
            composable(Screen.InstagramTools.route) { InstagramToolsScreen(navController) }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
        }
    }
}

@Composable
fun CreatorFlowBottomBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        bottomNavItems.forEachIndexed { index, screen ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                icon = {
                    if (index == 3) {
                        Icon(
                            imageVector = screen.selectedIcon!!,
                            contentDescription = screen.title,
                            tint = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Icon(
                            imageVector = if (selected) screen.selectedIcon!! else screen.icon!!,
                            contentDescription = screen.title
                        )
                    }
                },
                label = { Text(screen.title, style = MaterialTheme.typography.labelSmall) },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}