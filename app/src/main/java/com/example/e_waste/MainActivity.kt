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
import com.example.e_waste.presentation.ui.Screen.Auth.Register.RegisterScreen
import com.example.e_waste.presentation.ui.Screen.Main.MainContainerScreen
import com.example.e_waste.presentation.ui.theme.EwasteTheme
import com.example.e_waste.presentation.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

// AppDestinations sekarang hanya berisi rute-rute level atas.
// Rute untuk tab (Home, Tips, Profile) diatur di dalam MainContainerScreen.
object AppDestinations {
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    const val MAIN_ROUTE = "main_route" // Nama diganti untuk menghindari konflik
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
fun EWasteApp(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    // ViewModel ini diambil di level atas untuk di-pass ke MainContainerScreen
    // agar bisa menangani logout.
    val authViewModel: AuthViewModel = hiltViewModel()

    // NavHost adalah pengatur utama semua layar di aplikasi
    NavHost(
        navController = navController,
        startDestination = AppDestinations.LOGIN_ROUTE
    ) {
        // Rute untuk layar Login
        composable(AppDestinations.LOGIN_ROUTE) {
            LoginScreen(
                onLoginSuccess = {
                    // Jika login berhasil, navigasi ke layar utama (yang berisi bottom nav)
                    // dan hapus semua layar sebelumnya dari tumpukan (back stack).
                    navController.navigate(AppDestinations.MAIN_ROUTE) {
                        popUpTo(AppDestinations.LOGIN_ROUTE) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppDestinations.REGISTER_ROUTE)
                }
            )
        }

        // Rute untuk layar Register
        composable(AppDestinations.REGISTER_ROUTE) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() }, // Kembali ke layar sebelumnya (Login)
                onRegisterSuccess = {
                    // Tampilkan pesan dan arahkan kembali ke Login untuk masuk
                    Toast.makeText(context, "Registrasi berhasil! Silakan login.", Toast.LENGTH_LONG).show()
                    navController.navigate(AppDestinations.LOGIN_ROUTE) {
                        popUpTo(AppDestinations.LOGIN_ROUTE) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Rute untuk layar utama setelah login
        composable(AppDestinations.MAIN_ROUTE) {
            // Memanggil MainContainerScreen yang menjadi "rumah" bagi BottomNavBar
            // dan semua tab-nya.
            MainContainerScreen(
                mainNavController = navController,
                authViewModel = authViewModel
            )
        }
    }
}