package com.gabriel.pocketstatement.data.mapper

import com.gabriel.pocketstatement.data.local.ReceiptEntity
import com.gabriel.pocketstatement.domain.model.Receipt

fun ReceiptEntity.toDomain(): Receipt {
    return Receipt(
        id = this.id,
        storeName = this.storeName,
        totalAmount = this.totalAmount,
        transactionDate = this.transactionDate,
        items = this.items, // The list is already converted by Room's TypeConverter
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
        items = this.items,
        category = this.category,
        imageUrl = this.imageUrl
    )
}