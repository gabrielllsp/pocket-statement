package com.gabriel.pocketstatement.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Camera : Screen("camera_screen")
    object Confirmation : Screen("confirmation_screen") // Add this
}