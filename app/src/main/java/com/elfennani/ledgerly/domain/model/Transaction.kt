package com.elfennani.ledgerly.domain.model

import java.time.Instant

data class Transaction(
    val id: String,
    val amount: Double,
    val date: Instant,
    val title: String,
    val description: String?,
    val category: String,
    val splits: List<TransactionSplit>
)
