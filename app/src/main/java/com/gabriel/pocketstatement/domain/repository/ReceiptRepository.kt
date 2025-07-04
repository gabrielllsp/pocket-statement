package com.gabriel.pocketstatement.domain.repository

import com.gabriel.pocketstatement.domain.model.Receipt
import kotlinx.coroutines.flow.Flow

interface ReceiptRepository {

    /**
     * Inserts or updates a receipt.
     */
    suspend fun saveReceipt(receipt: Receipt)

    /**
     * Retrieves all receipts from the data source, ordered by date.
     * @return A Flow that emits a list of receipts whenever the data changes.
     */
    fun getAllReceipts(): Flow<List<Receipt>>

    /**
     * Retrieves a single receipt by its ID.
     * @return A Flow that emits the receipt or null if not found.
     */
    fun getReceiptById(id: Long): Flow<Receipt?>

    /**
     * Deletes a receipt.
     */
    suspend fun deleteReceipt(receipt: Receipt)
}