package com.creatorflow.ai.ui.screens.youtube

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.creatorflow.ai.ui.components.*
import com.creatorflow.ai.ui.navigation.Screen
import com.creatorflow.ai.ui.theme.*

@Composable
fun YouTubeScreen(navController: NavController) {
    Column(Modifier.fillMaxSize()) {
        SmallTopAppBar(title={Text("YouTube")}, colors=TopAppBarDefaults.smallTopAppBarColors(containerColor=MaterialTheme.colorScheme.background))
        LazyColumn(Modifier.fillMaxSize().padding(horizontal=16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
            item { Card(shape=RoundedCornerShape(20.dp),colors=CardDefaults.cardColors(containerColor=Color.Transparent)){
                Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Brush.horizontalGradient(listOf(Color(0xFFFF0000),Color(0xFFCC0000)))).padding(20.dp)) {
                    Column {
                        Row(verticalAlignment=Alignment.CenterVertically) { Icon(Icons.Default.PlayCircle,null,tint=Color.White,Modifier.size(40.dp)); Spacer(Modifier.width(12.dp)); Column{Text("CreatorFlow Demo",style=MaterialTheme.typography.titleLarge,color=Color.White);Text("Connected",style=MaterialTheme.typography.bodySmall,color=Color.White.copy(0.7f))}}
                        Spacer(Modifier.height(16.dp); Row(Modifier.fillMaxWidth(),Arrangement.SpaceAround){ ChannelStat("Subscribers","12.4K"); ChannelStat("Videos","87"); ChannelStat("Views","1.2M") }
                    }
                }
            } }
            item {SectionHeader("Videos")}
            items((1..5).toList()){i->Card{Row(Modifier.padding(12.dp)){AsyncImage("https://picsum.photos/120/68?random=$i",null,ContentScale.Crop,Modifier.size(100.dp,60.dp).clip(RoundedCornerShape(8.dp)));Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text("Video Title $i",style=MaterialTheme.typography.titleMedium)}} }}
            item{Spacer(Modifier.height(32.dp))}
        }
    }
}

@Composable fun ChannelStat(l:String,v:String){ Column(horizontalAlignment=Alignment.CenterHorizontally){Text(v,style=MaterialTheme.typography.titleLarge,color=Color.White);Text(l,style=MaterialTheme.typography.bodySmall,color=Color.White.copy(0.7f))}}