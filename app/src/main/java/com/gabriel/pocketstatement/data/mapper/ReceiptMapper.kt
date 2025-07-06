package com.gabriel.pocketstatement.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.gabriel.pocketstatement.data.local.ReceiptEntity
import com.gabriel.pocketstatement.data.remote.ReceiptDto
import com.gabriel.pocketstatement.domain.model.Receipt
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun ReceiptEntity.toDomain(): Receipt {
    return Receipt(
        id = this.id,
        storeName = this.storeName,
        totalAmount = this.totalAmount,
        transactionDate = this.transactionDate,
        creationDate = this.creationDate,
        items = this.items,
        category = this.category,
        imageUrl = this.imageUrl
    )
}

fun Receipt.toEntity(): ReceiptEntity {
    return ReceiptEntity(
        id = this.id,
        storeName = this.storeName,
        totalAmount = this.totalAmount,
        transactionDate = this.transactionDate,
        creationDate = this.creationDate,
        items = this.items,
        category = this.category,
        imageUrl = this.imageUrl
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun ReceiptDto.toDomain(): Receipt {
    return Receipt(
        storeName = this.storeName,
        totalAmount = this.totalAmount,
        transactionDate = this.transactionDate?.let { LocalDate.parse(it) }, // Converte para LocalDate
        items = this.items,
        category = this.category
    )
}