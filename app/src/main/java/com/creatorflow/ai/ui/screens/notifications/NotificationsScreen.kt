package com.creatorflow.ai.ui.screens.notifications
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.creatorflow.ai.ui.theme.*

@Composable
fun NotificationsScreen(navController: NavController) {
    val items = remember { listOf("Payment Successful!", "Video Published", "Scheduled Reminder") }
    Column(Modifier.fillMaxSize()) {
        SmallTopAppBar(title={Text("Notifications")}, colors=TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background))
        LazyColumn(Modifier.fillMaxSize()){ release items(items){t-> ListItem(headlineContent={Text(t)}, supportingContent={Text("Recent")}), HorizontalDivider()) } }
    }
}