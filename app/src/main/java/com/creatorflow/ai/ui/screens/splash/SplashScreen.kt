package com.creatorflow.ai.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.creatorflow.ai.ui.navigation.Screen
import com.creatorflow.ai.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var start by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(if(start)1f else 0.7f, tween(800), label="scale")
    LaunchedEffect(Unit) { start=true; delay(2500L); navController.navigate(Screen.Dashboard.route){popUpTo(Screen.Splash.route){inclusive=true}} }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(DarkBackground, DarkSurface))), contentAlignment=Alignment.Center) {
        Column(Modifier.scale(scale.value), horizontalAlignment=Alignment.CenterHorizontally) {
            Box(Modifier.size(100.dp).clip(RoundedCornerShape(28.dp)).background(Brush.horizontalGradient(listOf(Primary,Secondary))), contentAlignment=Alignment.Center) { Icon(Icons.Default.AutoAwesome, null, tint=Color.White, modifier=Modifier.size(48.dp)) }
            Spacer(Modifier.height(32.dp)); Text("CreatorFlow AI", style=MaterialTheme.typography.displayMedium, color=Color.White)
            Spacer(Modifier.height(8.dp)); Text("Create. Automate. Grow.", style=MaterialTheme.typography.bodyMedium, color=Color.White.copy(0.7f))
            Spacer(Modifier.height(48.dp)); CircularProgressIndicator(color=Primary, modifier=Modifier.size(28.dp), strokeWidth=2.dp)
        }
    }
}