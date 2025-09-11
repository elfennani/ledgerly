package com.elfennani.ledgerly.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val date: Long,
    val title: String,
    val description: String?,
    val categoryId: Int?,
    val accountId: Int,
    @ColumnInfo(defaultValue = "0")
    val isTopUp: Boolean = false
)
