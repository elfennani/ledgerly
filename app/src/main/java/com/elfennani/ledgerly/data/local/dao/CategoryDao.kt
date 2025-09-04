package com.elfennani.ledgerly.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.elfennani.ledgerly.data.local.entities.CategoryBudgetEntity
import com.elfennani.ledgerly.data.local.entities.CategoryEntity
import com.elfennani.ledgerly.data.local.relations.CategoryWithBudgets
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    @Transaction
    fun getAllCategoriesFlow(): Flow<List<CategoryWithBudgets>>

    @Query("SELECT * FROM categories WHERE groupId = :groupId")
    suspend fun getCategoriesByGroupId(groupId: Int): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    @Transaction
    fun getCategoryByIdFlow(categoryId: Int): Flow<CategoryWithBudgets?>

    @Upsert
    suspend fun upsertCategory(category: CategoryEntity): Long

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Int)

    @Query("DELETE FROM category_budgets WHERE categoryId = :categoryId")
    suspend fun deleteCategoryBudgetsByCategoryId(categoryId: Int)

    @Transaction
    suspend fun deleteCategoryAndItsBudgets(categoryId: Int) {
        deleteCategoryBudgetsByCategoryId(categoryId)
        deleteCategoryById(categoryId)
    }

    @Query("SELECT * FROM category_budgets")
    fun getAllCategoryBudgets(): Flow<List<CategoryBudgetEntity>>

    @Upsert
    suspend fun upsertCategoryBudget(categoryBudget: CategoryBudgetEntity): Long
}