package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(categoryId: Int) {
        categoryRepository.deleteCategoryById(categoryId)
    }
}