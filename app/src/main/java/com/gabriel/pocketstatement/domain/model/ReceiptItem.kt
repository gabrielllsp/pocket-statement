package com.gabriel.pocketstatement.domain.model

data class ReceiptItem(
    val description: String,
    val quantity: Double,
    val price: Double
)