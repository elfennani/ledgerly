package com.elfennani.ledgerly.data.local.entities

import androidx.room.Entity

@Entity(tableName = "transaction_splits", primaryKeys = ["transactionId", "productId"])
data class TransactionSplitEntity(
    val transactionId: Int,
    val productId: Int,
    val units: Int,
    val totalPrice: Double
)
