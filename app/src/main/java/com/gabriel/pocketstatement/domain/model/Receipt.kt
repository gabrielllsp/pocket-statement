package com.gabriel.pocketstatement.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class Receipt @RequiresApi(Build.VERSION_CODES.O) constructor(
    val id: Long = 0,
    val storeName: String,
    val totalAmount: Double,
    val transactionDate: LocalDate?,
    val creationDate: LocalDate = LocalDate.now(),
    val items: List<ReceiptItem>,
    val category: String,
    val imageUrl: String? = null
)