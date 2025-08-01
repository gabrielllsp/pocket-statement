package com.gabriel.pocketstatement.ui.screen.confirmation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.domain.usecase.ReceiptUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class ConfirmationViewModel @Inject constructor(
    private val useCases: ReceiptUseCases,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(ConfirmationState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAnalyzeClick(uri: Uri?) {
        if (uri == null) {
            _state.value = _state.value.copy(error = "Imagem não encontrada.")
            return
        }

        val bitmap = uri.toBitmap(context)
        if (bitmap == null) {
            _state.value = _state.value.copy(error = "Não foi possível carregar a imagem.")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                extractedText = null,
                error = null,
                processedReceipt = null
            )
            try {
                val rawText = useCases.textRecognitionUseCase(bitmap)
                _state.value = _state.value.copy(extractedText = rawText)

                useCases.processReceiptTextUseCase(rawText)
                    .onSuccess { receipt ->
                        val finalReceipt = if (receipt.transactionDate == null) {
                            receipt.copy(transactionDate = LocalDate.now())
                        } else {
                            receipt
                        }
                        _state.value =
                            _state.value.copy(isLoading = false, processedReceipt = finalReceipt)
                    }
                    .onFailure { error ->
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Falha na IA: ${error.message}"
                        )
                    }
            } catch (e: Exception) {
                _state.value =
                    _state.value.copy(isLoading = false, error = "Erro na análise: ${e.message}")
            }
        }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            val receiptToSave = state.value.processedReceipt
            if (receiptToSave == null) {
                _state.value = _state.value.copy(error = "Nenhum recibo para salvar.")
                return@launch
            }
            useCases.saveReceiptUseCase(receiptToSave)
            _eventFlow.emit(UiEvent.SaveSuccess)
        }
    }

    private fun Uri.toBitmap(context: Context): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT < 28) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, this)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, this)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    sealed class UiEvent {
        object SaveSuccess : UiEvent()
    }
}

data class ConfirmationState(
    val isLoading: Boolean = false,
    val extractedText: String? = null,
    val processedReceipt: Receipt? = null,
    val error: String? = null
)
