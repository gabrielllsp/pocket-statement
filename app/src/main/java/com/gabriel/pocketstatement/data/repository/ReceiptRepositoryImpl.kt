package com.gabriel.pocketstatement.data.repository

import com.gabriel.pocketstatement.data.local.ReceiptDao
import com.gabriel.pocketstatement.data.mapper.toDomain
import com.gabriel.pocketstatement.data.mapper.toEntity
import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReceiptRepositoryImpl(
    private val dao: ReceiptDao
) : ReceiptRepository {

    override suspend fun saveReceipt(receipt: Receipt) {
        // Map the domain model to a database entity before saving
        dao.insertReceipt(receipt.toEntity())
    }

    override fun getAllReceipts(): Flow<List<Receipt>> {
        return dao.getAllReceipts()
            .map { entities ->
                // Map the list of entities to a list of domain models
                entities.map { it.toDomain() }
            }
    }

    override fun getReceiptById(id: Long): Flow<Receipt?> {
        return dao.getReceiptById(id)
            .map { entity ->
                // Map the entity to a domain model, handling the nullable case
                entity?.toDomain()
            }
    }

    override suspend fun deleteReceipt(receipt: Receipt) {
        dao.deleteReceipt(receipt.toEntity())
    }
}