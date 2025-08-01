package com.gabriel.pocketstatement.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.pocketstatement.domain.model.SpendingByCategory
import com.gabriel.pocketstatement.domain.usecase.ReceiptUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val useCases: ReceiptUseCases
) : ViewModel() {

    val spendingState: StateFlow<List<SpendingByCategory>> =
        useCases.getSpendingByCategoryUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(5000L),
                initialValue = emptyList()
            )
}