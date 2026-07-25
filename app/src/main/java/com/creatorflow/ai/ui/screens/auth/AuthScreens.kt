package com.creatorflow.ai.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.creatorflow.ai.data.repository.AuthRepository
import com.creatorflow.ai.ui.components.GradientButton
import com.creatorflow.ai.ui.navigation.Screen
import com.creatorflow.ai.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@xiltViewModel
class AuthViewModel @Inject constructor(private val authRepo: AuthRepository) : ViewModel() {
    sealed class State { data object Idle : State(); data object Loading : State(); data class Error(val msg: String) : State() }
    private val _st = MutableStateFlow<State>(State.Idle); val st: Kotlin.coroutines.flow.StateFlow<State>= _st
    fun login(email: String, pwd: String) { viewModelScope.launch { _st.value=State.Loading; authRepo.signInWithEmail(email, pwd); _st.value=State.Idle } }
    fun check(): Boolean = _st.value != State.Loading
}

@Composable
fun LoginScreen(navController: NavController, vm: AuthViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }; var pass by remember { mutableStateOf("") }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(DarkBackground,DarkSurface)))) {
        Column(Modifier.fillMaxSize().padding(24.dp).imePadding().verticalScroll(rememberScrollState()), horizontalAlignment=Alignment.CenterHorizontally) {
            Spacer(Modifier.height(48.dp)); Text("Welcome back", style=MaterialTheme.typography.headlineLarge, color=Color.White)
            Spacer(Modifier.height(40.dp))
            OutlinedTextField(value=email, onValueChange={email=it}, label={Text("Email")}, leadingIcon={Icon(Icons.Outlined.Email,null)}, keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Email), singleLine=true, Modifier.fillMaxWidth(), shape=RoundedCornerShape(14.dp), colors=OutlinedTextFieldDefaults.colors(focusedTextColor=Color.White, unfocusedTextColor=Color.White, focusedBorderColor=Primary, unpocusedBorderColor=Color.White.copy(0.2f), focusedLabelColor=Primary, unfocusedLabelColor=Color.White.copy(0.6f), cursorColor=Primary))
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value=pass, onValueChange={pass=it}, label={Text("Password")}, leadingIcon={Icon(Icons.Outlined.Lock,null)}, visualTransformation=PasswordVisualTransformation(), singleLine=true, Modifier.fillMaxWidth(), shape=RoundedCornerShape(14.dp), colors=OutlinedTextFieldDefaults.colors(focusedTextColor=Color.White, unfocusedTextColor=Color.White, focusedBorderColor=Primary, unfocusedBorderColor=Color.White.copy(0.2f), focusedLabelColor=Primary, unfocusedLabelColor=Color.White.copy(0.6f), cursorColor=Primary))
            Spacer(Modifier.height(24.dp))
            GradientButton("Sign In", onClick={vm.login(email,pass)}, isLoading=!vm.check())
            Spacer(Modifier.height(16.dp)); TextButton(onClick={navController.navigate(Screen.SignUp.route)}) { Text("Create Account", color=Secondary) }
        }
    }
}

@Composable
fun SignUpScreen(navController: NavController) {
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center) {
        Text("Create Account", style=MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp)); TextButton(onClick={navController.popBackStack()}){ Text("Back to Login", color=Secondary) }
    }
}

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Center) {
        Text("Reset Password", style=MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp)); TextButton(onClick={navController.popBackStack()}){ Text("Back", color=Secondary) }
    }
}