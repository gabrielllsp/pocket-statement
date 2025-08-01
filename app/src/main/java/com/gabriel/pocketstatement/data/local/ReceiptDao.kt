package com.gabriel.pocketstatement.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gabriel.pocketstatement.domain.model.SpendingByCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(receipt: ReceiptEntity)

    @Query("SELECT * FROM receipts ORDER BY transactionDate DESC")
    fun getAllReceipts(): Flow<List<ReceiptEntity>>

    @Query("SELECT * FROM receipts WHERE id = :id")
    fun getReceiptById(id: Long): Flow<ReceiptEntity?>

    @Delete
    suspend fun deleteReceipt(receipt: ReceiptEntity)

    @Query("SELECT category, SUM(totalAmount) as totalAmount FROM receipts GROUP BY category")
    fun getSpendingByCategory(): Flow<List<SpendingByCategory>>
}