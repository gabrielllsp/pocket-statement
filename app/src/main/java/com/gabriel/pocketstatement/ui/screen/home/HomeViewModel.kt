package com.gabriel.pocketstatement.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.domain.usecase.ReceiptUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getReceipts()
    }

    private fun getReceipts() {
        receiptUseCases.getReceiptsUseCase()
            .onEach { receipts ->
                _state.value = state.value.copy(
                    receipts = receipts,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    fun onDeleteReceipt(receipt: Receipt) {
        viewModelScope.launch {
            receiptUseCases.deleteReceiptUseCase(receipt)
            _eventFlow.emit(UiEvent.ShowUndoSnackbar(receipt))
        }
    }

    fun onUndoDelete(receipt: Receipt) {
        viewModelScope.launch {
            receiptUseCases.saveReceiptUseCase(receipt)
        }
    }

    sealed class UiEvent {
        data class ShowUndoSnackbar(val receipt: Receipt) : UiEvent()
    }
}