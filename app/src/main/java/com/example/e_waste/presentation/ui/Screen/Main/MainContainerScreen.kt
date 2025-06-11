package com.example.e_waste.presentation.ui.Screen.Main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e_waste.AppDestinations
import com.example.e_waste.presentation.navigation.BottomNavItem
import com.example.e_waste.presentation.ui.Screen.Home.HomeScreen
import com.example.e_waste.presentation.ui.Screen.Tips.TipsScreen
import com.example.e_waste.presentation.ui.Screen.User.Profile.ProfileScreen
import com.example.e_waste.presentation.ui.viewmodels.AuthViewModel
import com.example.e_waste.presentation.ui.viewmodels.EWasteViewModel
import com.example.e_waste.presentation.ui.Screen.JenisSampahKecil.JenisSampahKecilScreen
import com.example.e_waste.presentation.ui.Screen.JenisSampahBesar.JenisSampahBesarScreen
import com.example.e_waste.presentation.ui.Screen.KategoriSampah.KategoriSampahScreen

@Composable
fun MainContainerScreen(
    mainNavController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val bottomNavController = rememberNavController()
    val eWasteViewModel: EWasteViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            AppBottomNavigation(navController = bottomNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    authViewModel = authViewModel,
                    onLogout = {
                        authViewModel.logout()
                        mainNavController.navigate(AppDestinations.LOGIN_ROUTE) {
                            popUpTo(AppDestinations.MAIN_ROUTE) { inclusive = true }
                        }
                    },
                    onNavigateToCategorySelection = {
                        bottomNavController.navigate(AppDestinations.CATEGORY_SELECTION_ROUTE)
                    },
                    eWasteViewModel = eWasteViewModel
                )
            }
            composable(BottomNavItem.Tip.route) {
                TipsScreen()
            }
            composable(BottomNavItem.Profile.route) {
                // Perbaikan di sini: Hanya sertakan parameter yang diterima oleh ProfileScreen yang baru
                ProfileScreen(
                    onNavigateBack = { bottomNavController.popBackStack() }, // Untuk tombol back
                    onLogout = { // Implementasi logout di sini
                        authViewModel.logout()
                        mainNavController.navigate(AppDestinations.LOGIN_ROUTE) {
                            popUpTo(AppDestinations.MAIN_ROUTE) { inclusive = true }
                        }
                    }
                )
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    onNavigateBack = { bottomNavController.popBackStack() },
                    onLogout = {
                        // Menggunakan authViewModel dan mainNavController secara eksplisit
                        authViewModel.logout()
                        mainNavController.navigate(AppDestinations.LOGIN_ROUTE) {
                            popUpTo(AppDestinations.MAIN_ROUTE) { inclusive = true }
                        }
                    }
                )
            }
            composable(AppDestinations.CATEGORY_SELECTION_ROUTE) {
                KategoriSampahScreen(
                    onNavigateBack = {
                        eWasteViewModel.setCategoryFilter(null)
                        bottomNavController.popBackStack()
                    },
                    onNavigateToJenisSampahKecil = { bottomNavController.navigate(AppDestinations.JENIS_SAMPAH_KECIL_ROUTE) },
                    onNavigateToJenisSampahBesar = { bottomNavController.navigate(AppDestinations.JENIS_SAMPAH_BESAR_ROUTE) },
                    eWasteViewModel = eWasteViewModel
                )
            }
            composable(AppDestinations.JENIS_SAMPAH_KECIL_ROUTE) {
                JenisSampahKecilScreen(
                    onNavigateBack = { bottomNavController.popBackStack() }
                )
            }
            composable(AppDestinations.JENIS_SAMPAH_BESAR_ROUTE) {
                JenisSampahBesarScreen(
                    onNavigateBack = { bottomNavController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun AppBottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Tip,
        BottomNavItem.Profile
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}