package com.gabriel.pocketstatement.domain.usecase

import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.domain.repository.ReceiptRepository

class DeleteReceiptUseCase(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(receipt: Receipt) {

        repository.deleteReceipt(receipt)
    }
}