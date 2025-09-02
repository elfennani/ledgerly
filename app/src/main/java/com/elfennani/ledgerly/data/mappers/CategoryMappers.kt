package com.elfennani.ledgerly.data.mappers

import com.elfennani.ledgerly.data.local.entities.CategoryBudgetEntity
import com.elfennani.ledgerly.data.local.entities.CategoryEntity
import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.model.CategoryBudget

fun CategoryEntity.toDomain(budgets: List<CategoryBudget> = emptyList()): Category {
    return Category(
        id = this.id,
        groupId = this.groupId,
        name = this.name,
        budgets = budgets
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        groupId = this.groupId,
        name = this.name,
    )
}

fun CategoryBudgetEntity.toDomain(): CategoryBudget {
    return CategoryBudget(
        categoryId = this.categoryId,
        month = this.month,
        year = this.year,
        target = this.target,
        budget = this.budget
    )
}