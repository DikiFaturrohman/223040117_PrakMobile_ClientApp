package com.example.e_waste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.e_waste.ui.ForgotPasswordScreen
import com.example.e_waste.ui.LoginScreen
import com.example.e_waste.ui.MainScreen
import com.example.e_waste.ui.OtpVerificationScreen
import com.example.e_waste.ui.ProfileScreen
import com.example.e_waste.ui.RegisterScreen
import com.example.e_waste.ui.ResetPasswordScreen
import com.example.e_waste.ui.theme.EwasteTheme
import dagger.hilt.android.AndroidEntryPoint


// MainActivity.kt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EwasteTheme {
                EWasteApp()
            }
        }
    }
}

@Composable
fun EWasteApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToForgotPassword = { navController.navigate("forgot_password") },
                onLoginSuccess = { navController.navigate("main") }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onRegisterSuccess = { navController.navigate("otp_verification/$it") }
            )
        }
        composable("otp_verification/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            OtpVerificationScreen(
                email = email,
                onVerificationSuccess = { navController.navigate("login") }
            )
        }
        composable("forgot_password") {
            ForgotPasswordScreen(
                onResetEmailSent = { email -> navController.navigate("reset_password/$email") }
            )
        }
        composable("reset_password/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ResetPasswordScreen(
                email = email,
                onPasswordResetSuccess = { navController.navigate("login") }
            )
        }
        composable("main") {
            MainScreen(
                onNavigateToProfile = { navController.navigate("profile") },
                onLogout = { navController.navigate("login") }
            )
        }
        composable("profile") {
            ProfileScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}