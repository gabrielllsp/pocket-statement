package com.gabriel.pocketstatement.domain.usecase

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TextRecognitionUseCase @Inject constructor() {

    suspend operator fun invoke(bitmap: Bitmap): String {
        // Wrap the callback API into a suspend function
        return suspendCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // On success, resume the coroutine with the result text
                    continuation.resume(visionText.text)
                }
                .addOnFailureListener { e ->
                    // On failure, resume the coroutine with an exception
                    continuation.resumeWithException(e)
                }
        }
    }
}