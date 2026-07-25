package com.creatorflow.ai.ui.screens.igtools
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.creatorflow.ai.ui.components.GradientButton
import com.creatorflow.ai.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstagramToolsViewModel @Inject constructor() : ViewModel() {
    private val _result = MutableStateFlow<Map<String, String>>(emptyMap()); val result: StateFlow<Map<String, String>> = _result
    private val _loading = MutableStateFlow(false); val loading: StateFlow<Boolean> = _loading
    fun generate(tool: String, topic: String) {
        _loading.value = true; viewModelScope.launch { delay(1200L)
            _result.value = when (tool) {
                "caption" -> mapOf("result" to "✨ $topic\n\nThis hit different today! 🔥\nDrop a �h️ if you agree!\n\nFollow @creatorflow for more →\n#instagram #contentcreator")
                "hashtags" -> mapOf("result" to "${topic.replace(" ","")} ${topic.replace(" ","")}Content instagram viral reelsindia explorepage trend creatorflow contentcreator instareels photooftheday instagood marketing socialmedia")
                "reel" -> mapOf("result" to "🎬 Reel Idea: \"$topic in 60 Seconds\"\n\nShot 1: Hook text overlay + dramatic music\nShot 2-4: Quick cuts showing key points\nShot 5: Trend transition effect\nShot 6: CTA + follow button\n␵ Details: Trending audio, 15-30s duration")
                else -> mapOf("result" to "Select a tool to get started.")
            }; _loading.value = false
        }
    }
}

@Composable
fun InstagramToolsScreen(navController: NavController, vm: InstagramToolsViewModel = hiltViewModel()) {
    var topic by remember { mutableStateOf("") }; var active by remember { mutableStateOf("caption") }
    val result by vm.result.collectAsState(); val loading by vm.loading.collectAsState()
    val tools = listOf("caption" to "Caption Generator", "hashtags" to "Hashtag Generator", "reel" to "Reel Idea Generator")
    Column(Modifier.fillMaxSize()) {
        SmallTopAppBar(title = { Text("Instagram Tools") }, colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.background))
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            OutlinedTextField(value = topic, onValueChange = { topic = it }, label = { Text("Your topic, niche, or keyword") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)); Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth()) { tools.forEach { (k, v) -> FilterChip(selected = active == k, onClick = { active = k }, label = { Text(v) }, modifier = Modifier.padding(end = 8.dp, bottom = 4.dp)) } }
            Spacer(Modifier.height(16.dp)); GradientButton("Generate ${tools.find { it.first == active }?.second ?= ""}", onClick = { vm.generate(active, topic) }, isLoading = loading)
            Spacer(Modifier.height(16.dp)); if (result.isNotEmpty()) { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Text(result["result"] ?: "", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}