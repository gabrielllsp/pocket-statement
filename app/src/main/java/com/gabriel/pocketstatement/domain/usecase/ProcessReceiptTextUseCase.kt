package com.gabriel.pocketstatement.domain.usecase

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.gabriel.pocketstatement.data.remote.GeminiApiService
import com.gabriel.pocketstatement.domain.model.Receipt
import javax.inject.Inject

class ProcessReceiptTextUseCase @Inject constructor(
    private val geminiApiService: GeminiApiService
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(rawText: String): Result<Receipt> {
        return try {
            val generativeModel = geminiApiService.getGenerativeModel()
            val prompt = geminiApiService.buildPrompt(rawText)
            val response = generativeModel.generateContent(prompt)

            val jsonResponse = response.text

            Log.d("GeminiResponse", "Resposta Bruta do Gemini: $jsonResponse")
            if (jsonResponse == null) {
                Result.failure(Exception("Gemini returned an empty response."))
            } else {
                val receipt = geminiApiService.parseReceiptFromJson(jsonResponse)
                if (receipt != null) {
                    Result.success(receipt)
                } else {
                    Result.failure(Exception("Failed to parse JSON response from Gemini."))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}