package com.gabriel.pocketstatement.domain.usecase

import com.gabriel.pocketstatement.domain.model.SpendingByCategory
import com.gabriel.pocketstatement.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSpendingByCategoryUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    operator fun invoke(): Flow<List<SpendingByCategory>> {
        return repository.getSpendingByCategory()
    }
}