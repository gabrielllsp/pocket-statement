package com.gabriel.pocketstatement.domain.usecase

import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.domain.repository.ReceiptRepository

class SaveReceiptUseCase(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(receipt: Receipt) {
        // Future logic can be added here (e.g., validation)
        if (receipt.storeName.isBlank()) {
            // throw IllegalArgumentException("Store name cannot be empty.")
        }
        repository.saveReceipt(receipt)
    }
}