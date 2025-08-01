package com.gabriel.pocketstatement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gabriel.pocketstatement.domain.model.ReceiptItem
import java.time.LocalDate

@Entity(tableName = "receipts")
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val storeName: String,
    val totalAmount: Double,
    val transactionDate: LocalDate?,
    val creationDate: LocalDate,
    val items: List<ReceiptItem>,
    val category: String,
    val imageUrl: String? = null
)