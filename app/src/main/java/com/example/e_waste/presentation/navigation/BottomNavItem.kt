package com.example.e_waste.presentation.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Tip : BottomNavItem("tip", Icons.Default.Lightbulb, "Tips")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profil")
}