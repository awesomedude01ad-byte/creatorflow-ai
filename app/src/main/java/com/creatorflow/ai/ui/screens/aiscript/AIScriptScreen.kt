package com.creatorflow.ai.ui.screens.aiscript
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.creatorflow.ai.ui.components.GradientButton
import com.creatorflow.ai.ui.theme.*

@Composable
fun AIScriptScreen(navController: NavController) {
    var topic by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize()) {
        SmallTopAppBar(title={Text("AI Script")}, colors=TopAppBarDefaults.smallTopAppBarColors(containerColor=MaterialTheme.colorScheme.background))
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            OutlinedTextField(value=topic, onValueChange={topic=it}, label={Text("Video Topic")}, Modifier.fillMaxWidth(), shape=RoundedCornerShape(14.dp))
            Spacer(Modifier.height(24.dp))
            GradientButton("Generate Script",onClick={})
            Spacer(Modifier.height(24.dp))
            Card(Modifier.fillMaxWidth(), shape=RoundedCornerShape(16.dp)){
                Text("AI-generated script appears here", Modifier.padding(16.dp), style=MaterialTheme.typography.bodyMedium)
            }
        }
    }
}