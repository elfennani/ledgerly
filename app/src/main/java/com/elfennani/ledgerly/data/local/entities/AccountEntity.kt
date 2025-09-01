package com.elfennani.ledgerly.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val balance: Double,

    @ColumnInfo(defaultValue = "NULL")
    val description: String?
)
