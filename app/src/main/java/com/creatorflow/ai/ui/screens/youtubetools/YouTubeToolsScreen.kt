package com.creatorflow.ai.ui.screens.youtubetools

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
class YouTubeToolsViewModel @Inject constructor() : ViewModel() {
    private val _result = MutableStateFlow<Map<String, String>>(emptyMap()); val result: StateFlow<Map<String, String>> = _result
    private val _loading = MutableStateFlow(false); val loading: StateFlow<Boolean> = _loading
    fun generate(tool: String, topic: String) {
        _loading.value = true; viewModelScope.launch { delay(1500L)
            _result.value = when (tool) {
                "title" -> mapOf("result" to "✞ $topic: The Ultimate Guide for 2026\n✍ How I Mastered $topic in 30 Days\n💏 $topic Secrets Nobody Tells You\n✅ $topic for Beginners: Step-by-Step")
                "description" -> mapOf("result" to "Discover everything about $topic! In this video, we cover tips, strategies, and insider secrets. Subscribe for more content like this.\n\n#${topic.replace(" ","")} #Tutorial #CreatorFlow")
                "tags" -> mapOf("result" to "${topic.replace(" ","")}, ${topic.replace(" ","")}Tutorial, howto${topic.replace(" ","")}, creatorflow, contentcreator, viral, trending 2026, ai, automation, growth")
                "thumbnail" -> mapOf("result" to "📦 Thumbnail Ideas:\n1. Split-screen: Before/After with bold text overlay\n2. Close-up face reaction + big emoji + text\n3. Gradient bg + centered icon + title in large font\n4. Comparison chart with arrows\n5. Teaser screenshot with 🔥 overlay")
                "script" -> mapOf("result" to "[INTRO - 0:00]\nHey everyone! Today we're talking about \"$topic\" — and it's going to blow your mind.\n\n[HOOK - 0:30]\nHere's why $topic matters RIGHT NOW.\n\n[BODY - 2:00]\nStep 1: ... Step 2: ... Step 3: ...\n\n[CTA - 0:30]\nLike, subscribe, and hit the bell. Let's grow together!")
                else -> mapOf("result" to "Select a tool above to generate content.")
            }; _loading.value = false
        }
    }
}

@Composable
fun YouTubeToolsScreen(navController: NavController, vm: YouTubeToolsViewModel = hiltViewModel()) {
    val tools = listOf("title" to "Title Generator", "description" to "Description Generator", "tags" to "Tags Generator", "thumbnail" to "Thumbnail Ideas", "script" to "Script Generator")
    var topic by remember { mutableStateOf("") }; var activeTool by remember { mutableStateOf("title") }
    val result by vm.result.collectAsState(); val loading by vm.loading.collectAsState()
    Column(Modifier.fillMaxSize()) {
        SmallTopAppBar(title = { Text("YouTube Tools") }, colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.background))
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            OutlinedTextField(value = topic, onValueChange = { topic = it }, label = { Text("Your Topic or Keyword") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)); Spacer(Modifier.height(16.dp))
            Text("Select Tool", style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(8.dp))
            tools.forEach { (key, label) -> FilterChip(selected = activeTool == key, onClick = { activeTool = key; if (topic.isNotBlank()) vm.generate(key, topic) }, label = { Text(label) }, modifier = Modifier.padding(end = 8.dp, bottom = 4.dp)) }
            Spacer(Modifier.height(16.dp)); GradientButton("Generate ${tools.find { it.first == activeTool }?.second ?= ""}", onClick = { vm.generate(activeTool, topic) }, isLoading = loading)
            Spacer(Modifier.height(16.dp)); if (result.isNotEmpty()) { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Text(result["result"] ?: "", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium) } }
        }
    }
}