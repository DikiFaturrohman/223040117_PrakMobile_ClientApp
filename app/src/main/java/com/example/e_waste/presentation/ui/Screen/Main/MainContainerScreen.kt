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

@Composable
fun MainContainerScreen(
    mainNavController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val bottomNavController = rememberNavController()

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
                    }
                )
            }
            composable(BottomNavItem.Tip.route) {
                // PERBAIKAN DI SINI: Panggil tanpa parameter onNavigateBack
                TipsScreen()
            }
            composable(BottomNavItem.Profile.route) {
                // PERBAIKAN DI SINI: Panggil tanpa parameter onNavigateBack
                ProfileScreen()
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