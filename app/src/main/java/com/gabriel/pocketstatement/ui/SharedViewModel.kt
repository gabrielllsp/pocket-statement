package com.gabriel.pocketstatement.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _capturedImageUri = MutableStateFlow<Uri?>(null)
    val capturedImageUri = _capturedImageUri.asStateFlow()

    fun setUri(uri: Uri) {
        _capturedImageUri.value = uri
    }

    fun clearUri() {
        _capturedImageUri.value = null
    }
}