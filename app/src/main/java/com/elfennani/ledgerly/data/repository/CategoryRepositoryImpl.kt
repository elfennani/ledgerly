package com.elfennani.ledgerly.data.repository

import com.elfennani.ledgerly.data.local.dao.CategoryDao
import com.elfennani.ledgerly.data.mappers.toDomain
import com.elfennani.ledgerly.data.mappers.toEntity
import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.model.CategoryBudget
import com.elfennani.ledgerly.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun getCategoriesFlow(): Flow<List<Category>> {
        return categoryDao.getAllCategoriesFlow().map { it.map { entity -> entity.toDomain() } }
    }

    override fun getCategoryByIdFlow(categoryId: Int): Flow<Category?> {
        return categoryDao.getCategoryByIdFlow(categoryId).map { it?.toDomain() }
    }

    override suspend fun getCategoriesByGroupId(groupId: Int) =
        categoryDao.getCategoriesByGroupId(groupId).map { it.toDomain() }

    override suspend fun upsertCategory(category: Category) =
        categoryDao.upsertCategory(category.toEntity())

    override suspend fun deleteCategory(category: Category) =
        categoryDao.deleteCategory(category.toEntity())

    override suspend fun deleteCategoryById(categoryId: Int) =
        categoryDao.deleteCategoryById(categoryId)

    override fun getCategoryBudgetsFlow(): Flow<List<CategoryBudget>> {
        return categoryDao.getAllCategoryBudgets()
            .map { it.map { categoryBudgetEntity -> categoryBudgetEntity.toDomain() } }
    }

    override suspend fun upsertCategoryBudget(categoryBudget: CategoryBudget): Long {
        return categoryDao.upsertCategoryBudget(categoryBudget.toEntity())
    }
}