package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.repository.CategoryRepository
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(groupId: Int, name: String) {
        categoryRepository.upsertCategory(
            Category(
                id = 0,
                name = name,
                groupId = groupId
            )
        )
    }
}