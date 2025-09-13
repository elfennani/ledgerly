package com.elfennani.ledgerly.domain.model

data class CategoryBudget(
    val categoryId: Int,
    val month: Int,
    val year: Int,
    val target: Double?,
    val budget: Double?,
    val spent: Double? = null
)
