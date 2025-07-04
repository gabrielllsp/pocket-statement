package com.gabriel.pocketstatement.domain.usecase

import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow

class GetReceiptsUseCase(
    private val repository: ReceiptRepository
) {
    operator fun invoke(): Flow<List<Receipt>> {
        return repository.getAllReceipts()
    }
}