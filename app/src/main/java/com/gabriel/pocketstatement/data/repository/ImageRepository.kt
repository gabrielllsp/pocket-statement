package com.gabriel.pocketstatement.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val cacheDir = context.cacheDir

    fun saveImageToCache(bitmap: Bitmap): Uri? {
        val fileName = "captured_image_${UUID.randomUUID()}.jpg"
        val imageFile = File(cacheDir, fileName)

        return try {
            FileOutputStream(imageFile).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            }
            Uri.fromFile(imageFile)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getImageFromCache(uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun deleteImageFromCache(uri: Uri) {
        uri.path?.let { File(it).delete() }
    }
}