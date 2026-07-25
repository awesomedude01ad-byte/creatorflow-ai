package com.creatorflow.ai.ui.screens.thumbnail
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.creatorflow.ai.ui.components.GradientButton
import com.creatorflow.ai.ui.theme.*

@Composable
fun AIThumbnailScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize()) {
        SmallTopAppBar(title={Text("AI Thumbnail")}, colors=TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background))
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            OutlinedTextField(value=title, onValueChange={title=it}, label={Text("Video Title")}, Modifier.fillMaxWidth(), shape=RoundedCornerShape(14.dp))
            Spacer(Modifier.height(24.dp))
            GradientButton("Generate Thumbnail",onClick={}, icon=Icons.Default.AutoAwesome)
        }
    }
}