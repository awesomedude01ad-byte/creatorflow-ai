package com.creatorflow.ai.ui.screens.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.creatorflow.ai.ui.components.GradientButton
import com.creatorflow.ai.ui.navigation.Screen
import com.creatorflow.ai.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class ProfileData(val name: String = "Cool Dude", val email: String = "support@creatorflowai.com", val bio: String = "Content creator & entrepreneur", val plan: String = "Free Plan", val scripts: Int = 12, val videos: Int = 8, val followers: Int = 0)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _profile = MutableStateFlow(ProfileData()); val profile: StateFlow<ProfileData> = _profile
    fun updateName(n: String) { _profile.value = _profile.value.copy(name = n) }
    fun updateBio(b: String) { _profile.value = _profile.value.copy(bio = b) }
}

@Composable
fun ProfileScreen(navController: NavController, vm: ProfileViewModel = hiltViewModel()) {
    val profile by vm.profile.collectAsState(); var editing by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(profile.name) }; var editBio by remember { mutableStateOf(profile.bio) }; val ctx = LocalContext.current
    Column(Modifier.fillMaxSize()) {
        SmallTopAppBar(title = { Text("Profile") }, actions = { TextButton(onClick = { editing = !editing; editName = profile.name; editBio = profile.bio }) { Text(if (editing) "Save" else "Edit") } }, colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.background))
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(Primary.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, null, tint = Primary, modifier = Modifier.size(56.dp)) }
            Spacer(Modifier.height(16.dp))
            if (editing) {
                OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
                Spacer(Modifier.height(8.dp)); OutlinedTextField(value = editBio, onValueChange = { editBio = it }, label = { Text("Bio") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), maxLines = 3)
                Spacer(Modifier.height(16.dp)); GradientButton("Save Changes", onClick = { vm.updateName(editName); vm.updateBio(editBio); editing = false })
            } else {
                Text(profile.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(profile.bio, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(24.dp)); Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                Stat(profile.scripts.toString(), "Scripts", Primary); Stat(profile.videos.toString(), "Videos", Secondary); Stat(profile.followers.toString(), "Followers", Success)
            }
            Spacer(Modifier.height(24.dp)); Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Column {
                ProfileItem(Icons.Default.Star, "Subscription", profile.plan) { navController.navigate(Screen.Subscription.route) }
                HorizontalDivider(); ProfileItem(Icons.Default.Security, "Privacy Policy", "") { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://creatorflow.ai/privacy"))) }
                HorizontalDivider(); ProfileItem(Icons.Default.Description, "Terms", "") { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://creatorflow.ai/terms"))) }
                HorizontalDivider(); ProfileItem(Icons.Default.Info, "App Version", "v1.0.0 .com.creatorflow.ai") {}
            } }
            Spacer(Modifier.height(16.dp)); TextButton(onClick = { navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } } }) { Text("Sign Out", color = Error) }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable private fun Stat(v: String, l: String, c: androidx.compose.ui.graphics.Color) = Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(v, style = MaterialTheme.typography.titleLarge, color = c, fontWeight = FontWeight.Bold); Text(l, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }

@Composable private fun ProfileItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, sub:String, onClick: () -> Unit) {
    ListItem(headlineContent = { Text(title) }, supportingContent = { if (sub.isNotEmpty()) Text(sub, color = MaterialTheme.colorScheme.onSurfaceVariant) }, leadingContent = { Icon(icon, null) }, modifier = Modifier.clickable { onClick() })
}