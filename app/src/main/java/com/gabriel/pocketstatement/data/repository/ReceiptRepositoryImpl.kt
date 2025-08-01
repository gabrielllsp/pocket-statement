package com.gabriel.pocketstatement.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.gabriel.pocketstatement.data.local.ReceiptDao
import com.gabriel.pocketstatement.data.mapper.toDomain
import com.gabriel.pocketstatement.data.mapper.toEntity
import com.gabriel.pocketstatement.domain.model.Receipt
import com.gabriel.pocketstatement.domain.model.SpendingByCategory
import com.gabriel.pocketstatement.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReceiptRepositoryImpl(
    private val dao: ReceiptDao
) : ReceiptRepository {

    override suspend fun saveReceipt(receipt: Receipt) {
        dao.insertReceipt(receipt.toEntity())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAllReceipts(): Flow<List<Receipt>> {
        return dao.getAllReceipts()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getReceiptById(id: Long): Flow<Receipt?> {
        return dao.getReceiptById(id)
            .map { entity ->
                entity?.toDomain()
            }
    }

    override suspend fun deleteReceipt(receipt: Receipt) {
        dao.deleteReceipt(receipt.toEntity())
    }

    override fun getSpendingByCategory(): Flow<List<SpendingByCategory>> {
        return dao.getSpendingByCategory()
    }
}