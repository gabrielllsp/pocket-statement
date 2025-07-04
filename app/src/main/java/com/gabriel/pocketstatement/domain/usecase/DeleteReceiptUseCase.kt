package com.gabriel.pocketstatement.domain.usecase

import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.domain.repository.ReceiptRepository

class DeleteReceiptUseCase(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(receipt: Receipt) {
        // In a real-world app, you might add business logic here.
        // For example: "Can this type of receipt be deleted?"
        // Or: "Log this deletion event to an analytics service."
        // For now, it's a direct call to the repository.
        repository.deleteReceipt(receipt)
    }
}