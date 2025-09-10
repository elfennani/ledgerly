package com.elfennani.ledgerly.domain.model

import java.time.Instant

data class Transaction(
    val id: Int = 0,
    val amount: Double,
    val date: Instant,
    val title: String,
    val description: String?,
    val category: Category,
    val splits: List<TransactionSplit>
)
