package com.elfennani.ledgerly.domain.model

data class Category(
    val id: Int = 0,
    val groupId: Int,
    val name: String,
    val budgets: List<CategoryBudget> = emptyList()
)
