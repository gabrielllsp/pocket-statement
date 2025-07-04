package com.gabriel.pocketstatement.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _capturedBitmap = MutableStateFlow<Bitmap?>(null)
    val capturedBitmap = _capturedBitmap.asStateFlow()

    fun setBitmap(bitmap: Bitmap) {
        _capturedBitmap.value = bitmap
    }

    fun clearBitmap() {
        _capturedBitmap.value = null
    }
}