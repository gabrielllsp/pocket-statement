package com.gabriel.pocketstatement.ui.screen.home

import com.gabriel.pocketstatement.domain.model.Receipt

data class HomeState(
    val receipts: List<Receipt> = emptyList(),
    val isLoading: Boolean = true
)
