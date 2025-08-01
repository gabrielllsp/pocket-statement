package com.gabriel.pocketstatement.ui.screen.camera

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.gabriel.pocketstatement.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : ViewModel() {

    fun savePhotoToCache(bitmap: Bitmap): Uri? {
        return imageRepository.saveImageToCache(bitmap)
    }
}