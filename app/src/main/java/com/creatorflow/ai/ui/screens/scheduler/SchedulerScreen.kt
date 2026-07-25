package com.creatorflow.ai.ui.screens.scheduler
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.creatorflow.ai.ui.components.SectionHeader
import com.creatorflow.ai.ui.theme.*

private data class ScheduleEvent(val title: String, val platform: String, val time: String, val status: String)

@Composable
fun SchedulerScreen(navController: NavController) {
    val events = remember { listOf(
        ScheduleEvent("Product Review", "youtube", "Jul 25, 14:00", "Pending"),
        ScheduleEvent("Tutorial Upload", "youtube", "Jul 26, 10:00", "Pending"),
        ScheduleEvent("Reel Post", "instagram", "Jul 27, 18:00", "Pending"),
    ) }
    Column(Modifier.fillMaxSize()) {
        SmallTopAppBar(title={Text("Schedule")}, colors=TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background))
        LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement=Arrangement.spacedBy(8.dp)) {
            item{SectionHeader("Upcoming Posts")}
            items(events){ev->
                Card(shape=RoundedCornerShape(16.dp)){
                    Row(Modifier.padding(16.dp), verticalAlignment=Alignment.CenterVertically){
                        Icon(if(ev.platform=="youtube")Icons.Default.PlayCircle else Icons.Default.Camera,null,tint=if(ev.platform=="youtube")Color(0xFFFF0000) else Color(0xFF833AB4),Modifier.size(28.dp))
                        Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(ev.title,style=MaterialTheme.typography.titleMedium);Text(ev.time,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)}
                        Surface(shape=RoundedCornerShape(20.dp),color=Warning.copy(0.15f)){Text(ev.status,Modifier.padding(horizontal=10.dp,vertical=4.dp),style=MaterialTheme.typography.labelSmall,color=Warning)}
                    }
                }
            }
        }
    }
}