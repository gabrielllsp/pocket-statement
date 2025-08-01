package com.gabriel.pocketstatement.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gabriel.pocketstatement.ui.SharedViewModel
import com.gabriel.pocketstatement.ui.screen.camera.CameraScreen
import com.gabriel.pocketstatement.ui.screen.confirmation.ConfirmationScreen
import com.gabriel.pocketstatement.ui.screen.dashboard.DashboardScreen
import com.gabriel.pocketstatement.ui.screen.detail.ReceiptDetailScreen
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
                },
                onReceiptClick = { receiptId ->
                    navController.navigate(Screen.ReceiptDetail.createRoute(receiptId))
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route)
                }
            )
        }

        composable(route = Screen.Camera.route) {
            CameraScreen(
                onNavigateBack = { navController.popBackStack() },
                onPhotoCaptured = { uri ->
                    sharedViewModel.setUri(uri)
                    navController.navigate(Screen.Confirmation.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(route = Screen.Confirmation.route) {
            ConfirmationScreen(
                sharedViewModel = sharedViewModel,
                onNavigateBack = { navController.popBackStack() },
                onSaveComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.ReceiptDetail.route,
            arguments = listOf(
                navArgument("receiptId") {
                    type = NavType.LongType
                }
            )
        ) {
            ReceiptDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}