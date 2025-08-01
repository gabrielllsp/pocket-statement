package com.gabriel.pocketstatement.domain.repository

import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.domain.model.SpendingByCategory
import kotlinx.coroutines.flow.Flow

interface ReceiptRepository {


    suspend fun saveReceipt(receipt: Receipt)

    fun getAllReceipts(): Flow<List<Receipt>>

    fun getReceiptById(id: Long): Flow<Receipt?>

    suspend fun deleteReceipt(receipt: Receipt)

    fun getSpendingByCategory(): Flow<List<SpendingByCategory>>
}