package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateCategoryNameUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(categoryId: Int, newName: String) {
        val category = categoryRepository.getCategoryByIdFlow(categoryId).first()
        val updatedCategory = category?.copy(name = newName)
        if (updatedCategory != null)
            categoryRepository.upsertCategory(updatedCategory)
    }
}