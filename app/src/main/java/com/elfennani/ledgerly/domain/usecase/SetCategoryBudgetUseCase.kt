package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.model.CategoryBudget
import com.elfennani.ledgerly.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SetCategoryBudgetUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(categoryId: Int, month: Int, year: Int, budget: Double) {
        val existingBudgets = categoryRepository.getCategoryBudgetsFlow()
            .first()

        val existingBudget = existingBudgets.find {
            it.categoryId == categoryId && it.month == month && it.year == year
        }

        val updatedBudget = existingBudget?.copy(budget = budget)
            ?: CategoryBudget(
                categoryId = categoryId,
                month = month,
                year = year,
                budget = budget,
                target = null
            )

        categoryRepository.upsertCategoryBudget(updatedBudget)
    }
}