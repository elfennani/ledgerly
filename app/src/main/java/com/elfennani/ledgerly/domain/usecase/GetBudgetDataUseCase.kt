package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.model.BudgetData
import com.elfennani.ledgerly.domain.repository.AccountRepository
import com.elfennani.ledgerly.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetBudgetDataUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
) {
    operator fun invoke(
        monthIndex: Int,
        year: Int
    ): Flow<BudgetData> {
        return combine(
            accountRepository.accountListFlow,
            categoryRepository.getCategoriesFlow()
        ) { accounts, categories ->
            val totalBudget = accounts.fold(0.00) { acc, account -> acc + account.balance }
            val usedBudget = categories.fold(0.00) { acc, category ->
                acc + (category.budgets.find { it.month == monthIndex && it.year == year }?.budget
                    ?: 0.00)
            }

            BudgetData(
                total = totalBudget,
                used = usedBudget,
                unused = totalBudget - usedBudget
            )
        }
    }
}