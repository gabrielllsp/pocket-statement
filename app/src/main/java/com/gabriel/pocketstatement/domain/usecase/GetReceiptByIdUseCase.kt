package com.gabriel.pocketstatement.domain.usecase

import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow

class GetReceiptByIdUseCase(
    private val repository: ReceiptRepository
) {
    operator fun invoke(id: Long): Flow<Receipt?> {
        return repository.getReceiptById(id)
    }
}