package com.elfennani.ledgerly.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elfennani.ledgerly.data.local.entities.CategoryBudgetEntity
import com.elfennani.ledgerly.data.local.entities.CategoryEntity

data class CategoryWithBudgets(
    @Embedded val category: CategoryEntity,

    @Relation(parentColumn = "id", entityColumn = "categoryId")
    val budgets: List<CategoryBudgetEntity>
)
