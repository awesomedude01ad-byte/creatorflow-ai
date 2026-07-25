package com.creatorflow.ai.ui.screens.aichat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.creatorflow.ai.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(val text: String, val isUser: Boolean, val id: String = java.util.UUID.randomUUID().toString())

@HiltViewModel
class AIChatViewModel @Inject constructor() : ViewModel() {
    private val _msgs = MutableStateFlow(listOf(ChatMessage("Hi! I'm your AI assistant. Ask me anything about content creation!", isUser = false))); val messages: StateFlow<List<ChatMessage>> = _msgs
    private val _isTyping = MutableStateFlow(false); val isTyping: StateFlow<Boolean> = _isTyping
    fun sendMessage(text: String) {
        if (text.isBlank()) return; _msgs.value = _msgs.value + ChatMessage(text, isUser = true); _isTyping.value = true
        viewModelScope.launch {
            delay(1500L); val reply = when { text.contains("script", true) -> "For a great YouTube script: start with a hook, deliver value in the body, and end with a CTA. Want me to generate one?"
                text.contains("thumbnail", true) -> "High-CTR thumbnails use bold text, contrasting colors, and expressive faces. Try our AI Thumbnail tool!"
                text.contains("hashtag", true) -> "For Instagram growth: use 5-10 relevant hashtags. I can generate them for you!"
                else -> "That's a great question! Try our AI tools for instant scripts, thumbnails, captions, and more. What specific content do you need help with today?" }
            _msgs.value = _msgs.value + ChatMessage(reply, isUser = false); _isTyping.value = false
        }
    }
}

@Composable
fun AIChatScreen(navController: NavController, vm: AIChatViewModel = hiltViewModel()) {
    val messages by vm.messages.collectAsState(); val isTyping by vm.isTyping.collectAsState(); var input by remember { mutableStateOf("") }; val listState = rememberLazyListState()
    LaunchedEffect(messages.size) { if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1) }
    Column(Modifier.fillMaxSize()) {
        SmallTopAppBar(title = { Text("AI Chat Assistant") }, colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.background))
        LazyColumn(state = listState, modifier = Modifier.weight(1f).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(messages, key = { it.id }) { msg ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start) {
                    if (!msg.isUser) { Icon(Icons.Default.AutoAwesome, null, tint = Primary, modifier = Modifier.size(24.dp).padding(top = 8.dp)); Spacer(Modifier.width(8.dp)) }
                    Surface(shape = RoundedCornerShape(16.dp, 16.dp, if (msg.isUser) 4.dp else 16.dp, if (msg.isUser) 16.dp else 4.dp), color = if (msg.isUser) Primary else MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.widthIn(max = 300.dp)) { Text(msg.text, modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium, color = if (msg.isUser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant) }
                    if (msg.isUser) { Spacer(Modifier.width(8.dp)); Icon(Icons.Default.Person, null, tint = Primary, modifier = Modifier.size(24.dp).padding(top = 8.dp)) }
                }
            }
            if (isTyping) { item { Row { Icon(Icons.Default.AutoAwesome, null, tint = Primary, modifier = Modifier.size(24.dp)); Spacer(Modifier.width(8.dp)); Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceVariant) { Text("Typing...", modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium) } } } }
            item { Spacer(Modifier.height(8.dp)) }
        }
        Surface(tonalElevation = 2.dp) { Row(Modifier.padding(8.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { OutlinedTextField(value = input, onValueChange = { input = it }, placeholder = { Text("Ask about content creation...") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(24.dp), singleLine = true); Spacer(Modifier.width(8.dp)); FilledIconButton(onClick = { vm.sendMessage(input); input = "" }, enabled = input.isNotBlank(), modifier = Modifier.size(48.dp), shape = RoundedCornerShape(16.dp), colors = IconButtonDefaults.filledIconButtonColors(containerColor = Primary)) { Icon(Icons.Default.Send, "Send", tint = Color.White) } } }
    }
}