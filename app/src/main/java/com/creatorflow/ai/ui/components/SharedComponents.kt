package com.creatorflow.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.creatorflow.ai.ui.theme.*

@Composable
fun GradientButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, isLoading: Boolean = false, enabled: Boolean = true, icon: ImageVector? = null) {
    Button(onClick = onClick, modifier = modifier.fillMaxWidth().height(52.dp), enabled = enabled && !isLoading, shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
        if (isLoading) { CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp) }
        else { if (icon != null) { Icon(icon, null, tint=Color.White, modifier=Modifier.size(20.dp)); Spacer(Modifier.width(8.dp)) }; Text(text, style=MaterialTheme.typography.labelLarge, color=Color.White) }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, color: Color, onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    Card(onClick=onClick, modifier=modifier, shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp)) {
            Row(horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically) {
                Box(Modifier.clip(RoundedCornerShape(10.dp)).background(color.copy(alpha=0.15f)).padding(8.dp)) { Icon(icon, null, tint=color, modifier=Modifier.size(20.dp)) }
                Icon(Icons.Default.ChevronRight, null, tint=MaterialTheme.colorScheme.onSurface.copy(alpha=0.3f), modifier=Modifier.size(16.dp))
            }
            Spacer(Modifier.weight(1f)); Text(value, style=MaterialTheme.typography.headlineMedium, color=MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(4.dp)); Text(title, style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun FeatureCard(icon: ImageVector, title: String, description: String, color: Color = Primary, onClick: () -> Unit = {}, modifier: Modifier = Modifier) {
    Card(onClick=onClick, modifier=modifier, shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface)) {
        Row(Modifier.padding(16.dp), verticalAlignment=Alignment.CenterVertically) {
            Box(Modifier.clip(RoundedCornerShape(12.dp)).background(color.copy(alpha=0.15f)).padding(10.dp)) { Icon(icon, null, tint=color, modifier=Modifier.size(24.dp)) }
            Spacer(Modifier.width(16.dp)); Column(Modifier.weight(1f)) { Text(title, style=MaterialTheme.typography.titleMedium); Text(description, style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onSurfaceVariant) }
            Icon(Icons.Default.ChevronRight, null, tint=MaterialTheme.colorScheme.onSurface.copy(alpha=0.3f))
        }
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String? = null, modifier: Modifier = Modifier, action: (@Composable () -> Unit)? = null) {
    Row(modifier.fillMaxWidth().padding(horizontal=16.dp, vertical=12.dp), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically) {
        Column { Text(title, style=MaterialTheme.typography.titleLarge); if (subtitle != null) Text(subtitle, style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onSurfaceVariant) }
        action?.invoke()
    }
}

@Composable
fun EmptyState(icon: ImageVector = Icons.Default.Inbox, title: String, subtitle: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
    Column(Modifier.fillMaxSize().padding(32.dp), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center) {
        Box(Modifier.clip(CircleShape).background(Primary.copy(alpha=0.1f)).padding(20.dp)) { Icon(icon, null, tint=Primary, modifier=Modifier.size(48.dp)) }
        Spacer(Modifier.height(24.dp)); Text(title, style=MaterialTheme.typography.titleLarge, textAlign=TextAlign.Center)
        Spacer(Modifier.height(8.dp)); Text(subtitle, style=MaterialTheme.typography.bodyMedium, color=MaterialTheme.colorScheme.onSurface.copy(alpha=0.6f), textAlign=TextAlign.Center)
        if (actionLabel != null && onAction != null) { Spacer(Modifier.height(24.dp)); GradientButton(text=actionLabel, onClick=onAction, modifier=Modifier.widthIn(max=250.dp)) }
    }
}