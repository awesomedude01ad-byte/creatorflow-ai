package com.creatorflow.ai.ui.screens.instagram
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.creatorflow.ai.ui.theme.*

@Composable
fun InstagramScreen(navController: NavController) {
    LazyColumn(Modifier.fillMaxSize().padding(horizontal=16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item { SmallTopAppBar(title={Text("Instagram")},colors=TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background))
        }
        item { Card(shape=RoundedCornerShape(20.dp),colors=CardDefaults.cardColors(Color.Transparent)){
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Brush.horizontalGradient(listOf(Color(0xFF833AB4),Color(0xFFF1D1D5)))).padding(20.dp)){
                Column{Row(verticalAlignment=Alignment.CenterVertically){Icon(Icons.Default.Camera,null,tint=Color.White,Modifier.size(40.dp));Spacer(Modifier.width(12.dp));Text("Instagram Dashboard",style=MaterialTheme.typography.titleLarge,color=Color.White)}
                Spacer(Modifier.height(16.dp)); Row(Modifier.fillMaxWidth(),Arrangement.SpaceAround){InstaStat("Posts","156");InstaStat("Followers","8.5K");InstaStat("Following","342")}}
            }}
        }
        item {SectionHeader("Posts")}
        item { LazyVerticalGrid(GridCells.Fixed(3),Arrangement.spacedBy(8.dp),Arrangement.spacedBy(8.dp)){items(9){i->AsyncImage("https://picsum.photos/200/200?random=ig$i",null,ContentScale.Crop,Modifier.aspectRatio(1f).clip(RoundedCornerShape(12.dp)))}} }
        item{Spacer(Modifier.height(32.dp))}
    }
}

@Composable fun InstaStat(l:String,v:String){ Column(horizontalAlignment=Alignment.CenterHorizontally){Text(v,style=MaterialTheme.typography.titleLarge,color=Color.White);Text(l,style=MaterialTheme.typography.bodySmall,color=Color.White.copy(0.7f))}}