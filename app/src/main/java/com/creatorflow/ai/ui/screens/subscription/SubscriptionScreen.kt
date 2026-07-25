package com.creatorflow.ai.ui.screens.subscription
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.creatorflow.ai.ui.components.GradientButton
import com.creatorflow.ai.ui.navigation.Screen
import com.creatorflow.ai.ui.theme.*
import java.util.Locale

@Composable
fun SubscriptionScreen(navController: NavController) {
    var billing by remember { mutableStateOf("yearly") }
    Scaffold(topBar={SmallTopAppBar(title={Text("Upgrade Plan")},navigationIcon={IconButton(onClick={navController.popBackStack()}){Icon(Icons.Default.ArrowBack,null)}},colors=TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background))}){p->
        Column(Modifier.fillMaxSize().padding(p).verticalScroll(rememberScrollState()).padding(16.dp), horizontalAlignment=Alignment.CenterHorizontally) {
            Icon(Icons.Default.Stars,null,tint=Primary,Modifier.size(56.dp))
            Spacer(Modifier.height(16.dp)); Text("Unlock Your Full Potential",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(32.dp))
            PlanCard("Pro", if(billing=="monthly")"₹999" else ₹7,999", if(billing=="monthly") "/mo" else "/yr", "+18% GST", listOf("100 AI scripts/mo", "AI thumbnails", "Video scheduling", "YouTube & IG automation"), isPopular=true, onSelect={})
            Spacer(Modifier.height(16.dp)); PlanCard("Enterprise", "₹19,999", "lifetime", "+18% GST", listOf("Unlimited AI scripts", "Unlimited thumbnails", "Team collaboration"), isPopular=false, onSelect={})
        }
    }
}

@Composable fun PlanCard(n:String,p:String,per:String,gst:String,f:List<String>,isPopular:Boolean,onSelect:()->Unit){
    Surface(shape=RoundedCornerShape(20.dp),color=if(isPopular)Color.Transparent else MaterialTheme.colorScheme.surface, Modifier.fillMaxWidth()){
        if(isPopular){Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Brush.horizontalGradient(listOf(Primary,Secondary))).padding(24.dp)){
                Text("POPULAR",style=MaterialTheme.typography.labelSmall,color=Color.White.copy(0.8f))
                Text("$p $per",style=MaterialTheme.typography.displaySmall,color=Color.White,fontWeight=FontWeight.Bold)
                f.forEach{_|->Row{Icon(Icons.Default.CheckCircle,null,tint=Color.White,Modifier.size(18.dp));Spacer(Modifier.width(8.dp));Text(_,color=Color.White.copy(0.9f), style=MaterialTheme.typography.bodyMedium)};Spacer(Modifier.height(8.dp))}
                Spacer(Modifier.height(16.dp)); Button(onClick=onSelect,Modifier.fillMaxWidth().height(48.dp),shape=RoundedCornerShape(14.dp),colors=ButtonDefaults.buttonColors(Color.White,Primary)){Text("Pay with Razorpay",fontWeight=FontWeight.Bold)}
            }} else {
                Column(Modifier.padding(24.dp)){Text(n,style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold);Text("$p $per",style=MaterialTheme.typography.displaySmall,fontWeight=FontWeight.Bold)
                f.forEach{_|->Row{Icon(Icons.Default.CheckCircle,null,tint=Success,Modifier.size(18.dp));Spacer(Modifier.width(8.dp));Text(_, style=MaterialTheme.typography.bodyMedium)};Spacer(Modifier.height(8.dp))}
                Spacer(Modifier.height(16.dp)); GradientButton("Pay with Razorpay",onClick=onSelect)
            }
        }
    }
}