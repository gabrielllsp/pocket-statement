package com.gabriel.pocketstatement.ui

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _capturedImageUri = MutableStateFlow<Uri?>(null) // Mude o nome e o tipo
    val capturedImageUri = _capturedImageUri.asStateFlow()

    fun setUri(uri: Uri) { // Mude o nome e o tipo
        _capturedImageUri.value = uri
    }

    fun clearUri() { // Mude o nome
        _capturedImageUri.value = null
    }
}