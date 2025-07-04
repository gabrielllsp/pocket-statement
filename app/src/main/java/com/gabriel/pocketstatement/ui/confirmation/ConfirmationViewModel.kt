package com.gabriel.pocketstatement.ui.confirmation

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.pocketstatement.domain.usecase.TextRecognitionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ConfirmationViewModel @Inject constructor(
    private val textRecognitionUseCase: TextRecognitionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ConfirmationState())
    val state = _state.asStateFlow()

    fun onAnalyzeClick(bitmap: Bitmap?) {
        if (bitmap == null) {
            _state.value = _state.value.copy(error = "No image found.")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, extractedText = null, error = null)
            try {
                val text = textRecognitionUseCase(bitmap)
                _state.value = _state.value.copy(isLoading = false, extractedText = text)
                // TODO: Step 10 - Send 'text' to Gemini API
                Log.d("ConfirmationViewModel", "Extracted Text: $text")
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
                Log.e("ConfirmationViewModel", "Error recognizing text", e)
            }
        }
    }
}

data class ConfirmationState(
    val isLoading: Boolean = false,
    val extractedText: String? = null,
    val error: String? = null
)