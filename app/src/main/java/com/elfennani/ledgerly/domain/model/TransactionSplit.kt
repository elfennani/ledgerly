package com.elfennani.ledgerly.domain.model

data class TransactionSplit(
    val transactionId: Int,
    val productId: Int,
    val units: Int,
    val totalPrice: Double,
    val product: Product
)