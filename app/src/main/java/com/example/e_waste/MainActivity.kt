package com.example.e_waste

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.e_waste.presentation.ui.Screen.Auth.Login.LoginScreen
import com.example.e_waste.presentation.ui.Screen.Home.MainScreen
import com.example.e_waste.presentation.ui.Screen.User.Profile.ProfileScreen
import com.example.e_waste.presentation.ui.Screen.Auth.Register.RegisterScreen
import com.example.e_waste.presentation.ui.theme.EwasteTheme
import com.example.e_waste.presentation.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

object AppDestinations {
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    // FORGOT_PASSWORD, OTP_VERIFICATION, RESET_PASSWORD dihapus
    const val MAIN_ROUTE = "main"
    const val PROFILE_ROUTE = "profile"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EwasteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EWasteApp()
                }
            }
        }
    }
}

@Composable
fun EWasteApp(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = AppDestinations.LOGIN_ROUTE) {
        composable(AppDestinations.LOGIN_ROUTE) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestinations.MAIN_ROUTE) {
                        popUpTo(AppDestinations.LOGIN_ROUTE) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(AppDestinations.REGISTER_ROUTE) }
                // onNavigateToForgotPassword dihapus
            )
        }
        composable(AppDestinations.REGISTER_ROUTE) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    Toast.makeText(context, "Registration successful! Please login.", Toast.LENGTH_LONG).show()
                    navController.navigate(AppDestinations.LOGIN_ROUTE) {
                        popUpTo(AppDestinations.LOGIN_ROUTE) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        // composable untuk FORGOT_PASSWORD, OTP_VERIFICATION, dan RESET_PASSWORD dihapus

        composable(AppDestinations.MAIN_ROUTE) {
            MainScreen(
                onNavigateToProfile = { navController.navigate(AppDestinations.PROFILE_ROUTE) },
                onLogout = {
                    authViewModel.logout() // Panggil fungsi logout di ViewModel
                    navController.navigate(AppDestinations.LOGIN_ROUTE) {
                        popUpTo(AppDestinations.MAIN_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(AppDestinations.PROFILE_ROUTE) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
                // onNavigateToChangePassword dihapus
            )
        }
    }
}