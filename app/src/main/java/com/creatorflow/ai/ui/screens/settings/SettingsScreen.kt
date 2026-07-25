package com.creatorflow.ai.ui.screens.settings
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.creatorflow.ai.ui.navigation.Screen
import com.creatorflow.ai.ui.theme.*

@Composable
fun SettingsScreen(navController: NavController) {
    val ctx = LocalContext.current
    Scaffold(topBar={SmallTopAppBar(title={Text("Settings")},colors=TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background))}){p->
        Column(Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState())) {
            ListItem(headlineContent={Text("Profile")}, supportingContent={Text("Cool Dude")}, leadingContent={Icon(Icons.Default.AccountCircle,null,tint=Primary)})
            ListItem(headlineContent={Text("Dark Mode")}, leadingContent={Icon(Icons.Default.DarkMode,null)}, trailingContent={Switch(checked=true){}})
            ListItem(headlineContent={Text("Privacy Policy")}, leadingContent={Icon(Icons.Default.PrivacyTip,null)}, modifier=Modifier.clickable{ctx.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("https://creatorflow.ai/privacy")))})
            ListItem(headlineContent={Text("Terms of Service")}, leadingContent={Icon(Icons.Default.Description,null)}, modifier=Modifier.clickable{ctx.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse("https://creatorflow.ai/terms")))})
            ListItem(headlineContent={Text("Sign Out", color=Error)}, leadingContent={Icon(Icons.Default.Logout,null,tint=Error)}, modifier=Modifier.clickable{navController.navigate(Screen.Login.route){popUpTo(0){inclusive=true}}})
        }
    }
}