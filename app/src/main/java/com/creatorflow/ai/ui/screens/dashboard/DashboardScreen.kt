package com.creatorflow.ai.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.creatorflow.ai.ui.components.*
import com.creatorflow.ai.ui.navigation.Screen
import com.creatorflow.ai.ui.theme.*

@Composable
fun DashboardScreen(navController: NavController) {
    Column(Modifier.fillMaxSize()) {
        SmallTopAppBar(
            title={Column{Text("Good Morning,", style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onSurfaceVariant);Text("Cool Dude", style=MaterialTheme.typography.titleLarge, fontWeight=FontWeight.Bold)}},
            colors=TopAppBarDefaults.smallTopAppBarColors(containerColor=MaterialTheme.colorScheme.background)
        )
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=16.dp)) {
            Card(onClick={navController.navigate(Screen.Subscription.route)}, shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(containerColor=Color.Transparent)) {
                Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(Primary,Secondary))).padding(16.dp)) {
                    Row(verticalAlignment=Alignment.CenterVertically) {
                        Icon(Icons.Default.Stars, null, tint=Color.White, modifier=Modifier.size(28.dp)); Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) {
                            Text("Upgrade to Pro", style=MaterialTheme.typography.titleMedium, color=Color.White, fontWeight=FontWeight.Bold)
                            Text("Unlock unlimited AI scripts & features", style=MaterialTheme.typography.bodySmall, color=Color.White.copy(alpha=0.85f))
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
                StatCard("Scripts", "12", Icons.Default.Article, Primary, modifier=Modifier.weight(1f), onClick={navController.navigate(Screen.AIScript.route)})
                StatCard("Thumbnails", "5", Icons.Default.Image, Secondary, modifier=Modifier.weight(1f), onClick={navController.navigate(Screen.AIThumbnail.route)})
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
                StatCard("Published", "8", Icons.Default.PlayCircle, Success, modifier=Modifier.weight(1f), onClick={navController.navigate(Screen.YouTube.route)})
                StatCard("Scheduled", "3", Icons.Default.Schedule, Warning, modifier=Modifier.weight(1f), onClick={navController.navigate(Screen.Scheduler.route)})
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}