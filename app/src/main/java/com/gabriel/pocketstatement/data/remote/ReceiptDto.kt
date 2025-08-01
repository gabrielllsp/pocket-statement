package com.gabriel.pocketstatement.data.remote

import com.gabriel.pocketstatement.domain.model.ReceiptItem

data class ReceiptDto(
    val storeName: String,
    val totalAmount: Double,
    val transactionDate: String?,
    val category: String,
    val items: List<ReceiptItem>
)