package com.elfennani.ledgerly.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.ledgerly.data.local.entities.CategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE groupId = :groupId")
    suspend fun getCategoriesByGroupId(groupId: Int): List<CategoryEntity>

    @Upsert
    suspend fun upsertCategory(category: CategoryEntity): Long

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Int)
}