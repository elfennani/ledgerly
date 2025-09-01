package com.elfennani.ledgerly.domain.model

data class Account(
    val id: Int,
    val name: String,
    val balance: Double,
    val description: String? = null
)
