package com.gabriel.pocketstatement.data.remote

import android.os.Build
import androidx.annotation.RequiresApi
import com.gabriel.pocketstatement.BuildConfig
import com.gabriel.pocketstatement.data.mapper.toDomain
import com.gabriel.pocketstatement.domain.model.Receipt
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiApiService @Inject constructor(private val gson: Gson) {

    fun getGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }

    fun buildPrompt(rawText: String): String {
        return """
            Você é um assistente especializado em processar texto de recibos de compra.
            Analise o texto a seguir, que foi extraído de um recibo usando OCR, e estruture-o em um formato JSON.

            Texto do Recibo:
            "$rawText"

            Siga estas regras estritamente:
            1.  O JSON de saída DEVE ter a seguinte estrutura: { "storeName": "string", "totalAmount": "double", "transactionDate": "string (YYYY-MM-DD)", "category": "string", "items": [{ "description": "string", "quantity": "double", "price": "double" }] }
            2.  iPara 'storeName', dentifique o nome do estabelecimento. Se for impossível, use "Estabelecimento Desconhecido".
            3.  Para 'totalAmount', encontre o valor total da compra. Use o maior valor numérico se houver ambiguidade. Deve ser um número (double).
            4.  Para 'transactionDate', encontre a data da transação. Formate-a como YYYY-MM-DD. Se NÃO houver data no texto, o valor deve ser null.
            5.  Para 'items', liste todos os produtos com descrição, quantidade e preço unitário. Se a quantidade não for explícita, assuma 1.0. Se não conseguir extrair itens, retorne uma lista vazia [].
            6.  Para 'category', categorize o estabelecimento (ex: "Supermercado", "Restaurante", "Transporte", "Combustível", "Vestuário", "Farmácia", "Outros").
            7.  Sua resposta deve conter APENAS o objeto JSON, sem nenhum texto adicional, markdown, ou explicações.
        """.trimIndent()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseReceiptFromJson(jsonString: String): Receipt? {
        return try {

            val startIndex = jsonString.indexOfFirst { it == '{' }
            val endIndex = jsonString.indexOfLast { it == '}' }

            if (startIndex == -1 || endIndex == -1) {
                return null
            }

            val cleanJson = jsonString.substring(startIndex, endIndex + 1)

            gson.fromJson(cleanJson, ReceiptDto::class.java)?.toDomain()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}