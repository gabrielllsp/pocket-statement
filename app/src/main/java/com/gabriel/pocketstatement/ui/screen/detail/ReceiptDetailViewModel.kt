package com.gabriel.pocketstatement.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.domain.usecase.ReceiptUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ReceiptDetailViewModel @Inject constructor(
    private val useCases: ReceiptUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val receiptId: Long = checkNotNull(savedStateHandle["receiptId"])
    val receiptState: StateFlow<Receipt?> = useCases.getReceiptByIdUseCase(receiptId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )
}