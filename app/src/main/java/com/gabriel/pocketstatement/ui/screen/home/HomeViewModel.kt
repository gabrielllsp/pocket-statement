package com.gabriel.pocketstatement.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.domain.usecase.ReceiptUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val receiptUseCases: ReceiptUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        getReceipts()
    }

    private fun getReceipts() {
        receiptUseCases.getReceiptsUseCase()
            .onEach { receipts ->
                _state.value = state.value.copy(
                    receipts = receipts
                )
            }
            .launchIn(viewModelScope) // launchIn is a concise way to collect a flow in a scope
    }

    fun onDeleteReceipt(receipt: Receipt) {
        viewModelScope.launch {
            receiptUseCases.deleteReceiptUseCase(receipt)
        }
    }
}


data class HomeState(
    val receipts: List<Receipt> = emptyList(),
    val isLoading: Boolean = false
)