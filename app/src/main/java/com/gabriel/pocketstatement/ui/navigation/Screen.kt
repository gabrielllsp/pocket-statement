package com.gabriel.pocketstatement.ui.navigation

import com.gabriel.pocketstatement.ui.screen.dashboard.DashboardScreen

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Camera : Screen("camera_screen")
    object Confirmation : Screen("confirmation_screen")
    object Dashboard : Screen("dashboard_screen") // Rota para o Dashboard

    object ReceiptDetail : Screen("receipt_detail_screen/{receiptId}") {
        fun createRoute(receiptId: Long) = "receipt_detail_screen/$receiptId"
    }
}