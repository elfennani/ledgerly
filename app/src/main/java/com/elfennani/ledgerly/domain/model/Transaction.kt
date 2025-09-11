package com.elfennani.ledgerly.domain.model

import java.time.Instant

//
//data class Transaction(
//    val id: Int = 0,
//    val amount: Double,
//    val date: Instant,
//    val title: String,
//    val description: String?,
//    val category: Category,
//    val splits: List<TransactionSplit>,
//    val account: Account,
//)

sealed class Transaction {
    abstract val id: Int
    abstract val amount: Double
    abstract val date: Instant
    abstract val title: String
    abstract val description: String?
    abstract val account: Account

    data class Outflow(
        override val id: Int = 0,
        override val amount: Double,
        override val date: Instant,
        override val title: String,
        override val description: String?,
        override val account: Account,
        val category: Category,
        val splits: List<TransactionSplit>,
    ) : Transaction()

    data class Inflow(
        override val id: Int = 0,
        override val amount: Double,
        override val date: Instant,
        override val title: String,
        override val description: String?,
        override val account: Account,
    ) : Transaction()
}