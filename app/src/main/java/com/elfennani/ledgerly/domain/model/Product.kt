package com.elfennani.ledgerly.domain.model

data class Product(
    val id: Int = 0,
    val name: String,
    val description: String? = "",
    val type: String,
    val defaultUnit: String?,
    val pricePerUnit: Double? = 0.0,
)
