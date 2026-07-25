package com.creatorflow.ai.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.creatorflow.ai.ui.components.*
import com.creatorflow.ai.ui.navigation.Screen
import com.creatorflow.ai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(nav: NavController) {
    var t by remember { mutableIntStateOf(0) }
    Scaffold(topBar={SmallTopAppBar(title={Text("Admin Panel")},colors=TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background))}){p->
        Column(Modifier.fillMaxSize().padding(p)){
            TabRow(selectedTabIndex=t){
                listOf("Overview","Users","Payments","Settlements","Content","Analytics").forEachIndexed{i,l->Tab(selected=t==i,onClick={t=i},text={Text(l)})}
            }
            when(t){0->OvNab(navd);1->EmptyState(Icons.Default.People,"User Management","Coming soon");2->EmptyState(Icons.Default.Payments,"Payments","Coming soon");3->EmptyState(Icons.Default.AccountBalance,"Settlements","Coming soon");4->EmptyState(Icons.Default.Article,"Content","Coming soon");5->EmptyState(Icons.Default.Analytics,"Analytics","Coming soon")}
        }
    }
}
@Composable fun OvNab(n:NavController)=Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp)){
    Row(Modifier.fillMaxWidth(),Arrangement.spacedBy(12.dp)){ StatCard("Users","1,247",Icons.Default.People,Primary,Modifier.weight(1f)); StatCard("Revenue","₹8.4L",Icons.Default.CurrencyRupee,Accent,Modifier.weight(1f)) }
    Spacer(Modifier.height(24.dp))
SectionHeader("Configuration")
    Card(Modifier.fillMaxWidth()){ Column{ ListItem(headlineContent={Text("App Name")},supportingContent={Text("CreatorFlow AI")},leadingContent={Icon(Icons.Default.Badge,null)});HorizontalDivider();ListItem(headlineContent={Text("Owner")},supportingContent={Text("Cool Dude")},leadingContent={Icon(Icons.Default.Person,null)});HorizontalDivider();ListItem(headlineContent={Text("Support Email")},supportingContent={Text("support@creatorflowai.com")},leadingContent={Icon(Icons.Default.Email,null)})}}
}

@Composable fun AdminConfigScreen(n:NavController)=Scaffold(topBar={SmallTopAppBar(title={Text("Configuration")},navigationIcon={IconButton(onClick={n.popBackStack()}){Icon(Icons.Default.ArrowBack,null)}},colors=TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background))}){EmptyState(Icons.Default.Tune,"Configuration","Coming soon")}
@Composable fun AdminBankScreen(n:NavController)=Scaffold(topBar={SmallTopAppBar(title={Text("Bank Settings")},navigationIcon={IconButton(onClick={n.popBackStack()}){Icon(Icons.Default.ArrowBack,null)}},colors=TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background))}){EmptyState(Icons.Default.AccountBalance,"Bank Settings","Coming soon")}
@Composable fun PaymentHistoryScreen(n:NavController)=Scaffold(topBar={SmallTopAppBar(title={Text("Payment History")},navigationIcon={IconButton(onClick={n.popBackStack()}){Icon(Icons.Default.ArrowBack,null)}},colors=TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background))}){EmptyState(Icons.Default.ReceiptLong,"Payment History","Coming soon")}
@Composable fun WithdrawalScreen(n:NavController)=Scaffold(topBar={SmallTopAppBar(title={Text("Settlements")},navigationIcon={IconButton(onClick={n.popBackStack()}){Icon(Icons.Default.ArrowBack,null)}},colors=TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background))}){EmptyState(Icons.Default.AccountBalance,"Settlements","Coming soon")}
@Composable fun InvoiceViewScreen(n:NavController)=Scaffold(topBar={SmallTopAppBar(title={Text("GST Invoices")},navigationIcon={IconButton(onClick={n.popBackStack()}){Icon(Icons.Default.ArrowBack,null)}},colors=TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background))}){EmptyState(Icons.Default.ReceiptLong,"GST Invoices","Coming soon")}