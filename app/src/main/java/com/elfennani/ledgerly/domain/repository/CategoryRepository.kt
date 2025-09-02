package com.elfennani.ledgerly.domain.repository

import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.model.CategoryBudget
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategoriesByGroupId(groupId: Int): List<Category>
    suspend fun upsertCategory(category: Category): Long
    suspend fun deleteCategory(category: Category)
    suspend fun deleteCategoryById(categoryId: Int)
    fun getCategoryBudgetsFlow(): Flow<List<CategoryBudget>>
}