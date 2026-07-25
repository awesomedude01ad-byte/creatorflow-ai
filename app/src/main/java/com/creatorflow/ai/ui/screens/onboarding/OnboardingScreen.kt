package com.creatorflow.ai.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.creatorflow.ai.ui.components.GradientButton
import com.creatorflow.ai.ui.navigation.Screen
import com.creatorflow.ai.ui.theme.*

data class OnboardingPage(val icon: androidx.compose.ui.graphics.vector.ImageVector, val title: String, val description: String, val color: Color)

private val pages = listOf( OnboardingPage(Icons.Default.AutoAwesome, "AI-Powered Content", "Generate scripts, thumbnails, captions, and hashtags with cutting-edge AI.", Primary), OnboardingPage(Icons.Default.Schedule, "Schedule & Automate", "Auto-publish to YouTube and Instagram. Save hours every week.", Secondary), OnboardingPage(Icons.Default.TrendingUp, "Grow Faster", "Analyze performance, track growth, and scale your creator business.", Success) )

@Composable
fun OnboardingScreen(navController: NavController) {
    var currentPage by remember { mutableIntStateOf(0) }
    val maxPages = pages.size
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(Modifier.fillMaxSize().padding(horizontal = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(48.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                if (currentPage < maxPages - 1) { TextButton(onClick = { navController.navigate(Screen.Login.route) { popUpTo(Screen.Onboarding.route) { inclusive = true } } }) { Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant) } }
            }
            Spacer(Modifier.weight(0.5f))
            AnimatedContent(targetState = currentPage, transitionSpec = { fadeIn() + slideInHorizontally { it } togetherWith fadeOut() + slideOutHorizontally { -it } }, label = "onboarding") { page ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(140.dp).clip(CircleShape).background(Brush.horizontalGradient(listOf(Primary, Secondary)).copy(alpha = 0.15f)), contentAlignment = Alignment.Center) { Icon(pages[page].icon, null, tint = pages[page].color, modifier = Modifier.size(64.dp)) }
                    Spacer(Modifier.height(40.dp)); Text(pages[page].title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center); Spacer(Modifier.height(16.dp)); Text(pages[page].description, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), textAlign = TextAlign.Center)
                }
            }
            Spacer(Modifier.weight(1f)); Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { repeat(maxPages){i -> Box(modifier = Modifier.size(if (i == currentPage) 24.dp else 8.dp, 8.dp).clip(RoundedCornerShape(4.dp)).background(if (i == currentPage) Primary else Primary.copy(alpha = 0.3f))) } }; Spacer(Modifier.height(32.dp)); GradientButton(if (currentPage < maxPages - 1) "Next" else "Get Started", onClick = { if (currentPage < maxPages - 1) currentPage++ else navController.navigate(Screen.Login.route) { popUpTo(Screen.Onboarding.route) { inclusive = true } } })
            Spacer(Modifier.height(48.dp))
        }
    }
}