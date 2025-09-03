package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SetCategoryTargetUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(categoryId: Int, month: Int, year: Int, target: Double) {
        val existingBudgets = categoryRepository.getCategoryBudgetsFlow()
            .first()

        val existingBudget = existingBudgets.find {
            it.categoryId == categoryId && it.month == month && it.year == year
        }

        val updatedBudget = existingBudget?.copy(target = target)
            ?: com.elfennani.ledgerly.domain.model.CategoryBudget(
                categoryId = categoryId,
                month = month,
                year = year,
                budget = null,
                target = target
            )

        categoryRepository.upsertCategoryBudget(updatedBudget)
    }
}