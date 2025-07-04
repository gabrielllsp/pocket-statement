package com.gabriel.pocketstatement.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gabriel.pocketstatement.ui.SharedViewModel
import com.gabriel.pocketstatement.ui.confirmation.ConfirmationScreen
import com.gabriel.pocketstatement.ui.screen.camera.CameraScreen
import com.gabriel.pocketstatement.ui.screen.home.HomeScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToCamera = {
                    navController.navigate(Screen.Camera.route)
                }
            )
        }
        composable(route = Screen.Camera.route) {
            CameraScreen(
                onNavigateBack = { navController.popBackStack() },
                onPhotoCaptured = { bitmap ->
                    // Set the bitmap in the shared ViewModel
                    sharedViewModel.setBitmap(bitmap)
                    // Navigate to the new confirmation screen
                    navController.navigate(Screen.Confirmation.route) {
                        // Pop up to home screen to clear camera from back stack
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }
        composable(route = Screen.Confirmation.route) {
            ConfirmationScreen(
                sharedViewModel = sharedViewModel, // Pass the ViewModel
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
