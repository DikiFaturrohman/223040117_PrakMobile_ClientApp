package com.example.e_waste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.e_waste.presentation.ui.Screen.Auth.ForgotPassword.ForgotPasswordScreen
import com.example.e_waste.presentation.ui.Screen.Auth.Login.LoginScreen
import com.example.e_waste.presentation.ui.Screen.Home.MainScreen
import com.example.e_waste.presentation.ui.Screen.Auth.Otp.OtpVerificationScreen
import com.example.e_waste.presentation.ui.Screen.User.Profile.ProfileScreen
import com.example.e_waste.presentation.ui.Screen.Auth.Register.RegisterScreen
import com.example.e_waste.presentation.ui.Screen.Auth.ResetPassword.ResetPasswordScreen
import com.example.e_waste.presentation.ui.theme.EwasteTheme
import com.example.e_waste.presentation.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

object AppDestinations {
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    const val FORGOT_PASSWORD_ROUTE = "forgot_password"
    const val OTP_VERIFICATION_ROUTE = "otp_verification" // email, isForRegistration
    const val RESET_PASSWORD_ROUTE = "reset_password" // email
    const val MAIN_ROUTE = "main"
    const val PROFILE_ROUTE = "profile"
    // Add other destinations here
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
    // AuthViewModel can be hoisted here if needed for initial auth state check,
    // or directly used in LoginScreen if it's always the startDestination for non-logged-in users.
    // val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = AppDestinations.LOGIN_ROUTE) {
        composable(AppDestinations.LOGIN_ROUTE) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestinations.MAIN_ROUTE) {
                        popUpTo(AppDestinations.LOGIN_ROUTE) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(AppDestinations.REGISTER_ROUTE) },
                onNavigateToForgotPassword = { navController.navigate(AppDestinations.FORGOT_PASSWORD_ROUTE) }
            )
        }
        composable(AppDestinations.REGISTER_ROUTE) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() }, // Go back to login
                onRegisterSuccessNavigateToOtp = { email ->
                    navController.navigate("${AppDestinations.OTP_VERIFICATION_ROUTE}/$email/true") // true for registration
                }
            )
        }
        composable(AppDestinations.FORGOT_PASSWORD_ROUTE) {
            ForgotPasswordScreen(
                onResetEmailSentNavigateToOtp = { email ->
                    navController.navigate("${AppDestinations.OTP_VERIFICATION_ROUTE}/$email/false") // false for password reset
                }
            )
        }
        composable(
            route = "${AppDestinations.OTP_VERIFICATION_ROUTE}/{email}/{isForRegistration}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("isForRegistration") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val isForRegistration = backStackEntry.arguments?.getBoolean("isForRegistration") ?: false
            OtpVerificationScreen(
                email = email,
                isForRegistration = isForRegistration,
                onVerificationSuccessForRegistration = { // After successful OTP for registration
                    navController.navigate(AppDestinations.LOGIN_ROUTE) { // Navigate to login after registration OTP success
                        popUpTo(AppDestinations.LOGIN_ROUTE) { inclusive = true } // Clear backstack up to login
                        launchSingleTop = true
                    }
                },
                onVerificationSuccessForPasswordReset = { verifiedEmail ->
                    navController.navigate("${AppDestinations.RESET_PASSWORD_ROUTE}/$verifiedEmail") {
                        popUpTo("${AppDestinations.OTP_VERIFICATION_ROUTE}/$email/$isForRegistration") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "${AppDestinations.RESET_PASSWORD_ROUTE}/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ResetPasswordScreen(
                email = email,
                onPasswordResetSuccess = {
                    navController.navigate(AppDestinations.LOGIN_ROUTE) {
                        popUpTo(AppDestinations.LOGIN_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(AppDestinations.MAIN_ROUTE) {
            MainScreen(
                onNavigateToProfile = { navController.navigate(AppDestinations.PROFILE_ROUTE) },
                onLogout = {
                    navController.navigate(AppDestinations.LOGIN_ROUTE) {
                        popUpTo(AppDestinations.MAIN_ROUTE) { inclusive = true }
                    }
                    // Potentially clear auth state / token here via AuthViewModel
                }
            )
        }
        composable(AppDestinations.PROFILE_ROUTE) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChangePassword = {
                    // Navigate to a dedicated change password screen
                    // For now, let's assume it's part of forgot password flow for simplicity,
                    // or you'd create a new screen for it.
                    // Example: navController.navigate("change_password_ внутри_app")
                    // For simplicity, let's navigate to forgot password which uses OTP.
                    // A better approach would be a separate screen that asks for current password.
                    navController.navigate(AppDestinations.FORGOT_PASSWORD_ROUTE)
                }
                // authViewModel can be passed if needed, or hiltViewModel used internally
            )
        }
    }
}