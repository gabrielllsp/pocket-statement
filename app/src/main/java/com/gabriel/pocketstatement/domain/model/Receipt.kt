package com.gabriel.pocketstatement.domain.model

import java.time.LocalDate

data class Receipt(
    val id: Long = 0,
    val storeName: String,
    val totalAmount: Double,
    val transactionDate: LocalDate,
    val items: List<ReceiptItem>,
    val category: String,
    val imageUrl: String? = null // Optional path to the original image
)
