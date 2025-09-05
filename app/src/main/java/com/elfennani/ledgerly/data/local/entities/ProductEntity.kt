package com.elfennani.ledgerly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String? = null,
    val type: String,
    val defaultUnit: String?,
    val pricePerUnit: Double = 0.0,
)
