package com.elfennani.ledgerly.data.local.entities

import androidx.room.Entity

@Entity(tableName = "category_budgets", primaryKeys = ["categoryId", "month", "year"])
data class CategoryBudgetEntity(
    val categoryId: Int,
    val month: Int,
    val year: Int,
    val budget: Double?,
    val target: Double?,
    val lastUpdated: Long = System.currentTimeMillis(),
)
