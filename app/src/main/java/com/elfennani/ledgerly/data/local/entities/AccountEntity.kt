package com.elfennani.ledgerly.data.local.entities

import androidx.room.Entity

@Entity(tableName = "accounts")
data class AccountEntity(
    val id: String,
    val name: String,
    val balance: Double
)
