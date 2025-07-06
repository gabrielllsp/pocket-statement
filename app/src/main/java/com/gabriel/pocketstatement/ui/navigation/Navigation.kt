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
import com.gabriel.pocketstatement.ui.screen.confirmation.ConfirmationScreen
import com.gabriel.pocketstatement.ui.screen.camera.CameraScreen
import com.gabriel.pocketstatement.ui.screen.dashboard.DashboardScreen
import com.gabriel.pocketstatement.ui.screen.detail.ReceiptDetailScreen
import com.gabriel.pocketstatement.ui.screen.home.HomeScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Rota para a Tela Inicial (Home)
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToCamera = {
                    navController.navigate(Screen.Camera.route)
                },
                onReceiptClick = { receiptId ->
                    navController.navigate(Screen.ReceiptDetail.createRoute(receiptId))
                },
                // Conectando o novo botão de ação do Dashboard
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route)
                }
            )
        }

        // Rota para a Tela da Câmera
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

        // Rota para a Tela de Confirmação
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

        // Rota para a Tela de Detalhes do Recibo (com argumento)
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

        // Rota para a nova Tela do Dashboard
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}