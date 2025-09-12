package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.model.HomeOverview
import com.elfennani.ledgerly.domain.repository.AccountRepository
import com.elfennani.ledgerly.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetHomeOverviewUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val getGroupsUseCase: GetGroupsUseCase,
    private val categoryRepository: CategoryRepository,
    private val getBudgetDataUseCase: GetBudgetDataUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
) {
    operator fun invoke(monthIndex: Int, year: Int) =
        combine(
            accountRepository.accountListFlow,
            getGroupsUseCase(),
            categoryRepository.getCategoryBudgetsFlow(),
            getBudgetDataUseCase(monthIndex, year),
            getTransactionsUseCase()
        ) { accounts, groups, budgets, budgetData, transactions ->
            HomeOverview(
                accounts = accounts.map { account ->
                    account.copy(
                        transactions = transactions
                            .filter { it.account.id == account.id }
                            .sortedByDescending { it.date.toEpochMilli() }
                    )
                },
                budgetData = budgetData,
                groups = groups.sortedBy { it.index }.map { group ->
                    group.copy(
                        categories = group.categories?.map { category ->
                            category.copy(
                                budgets = budgets.filter { it.categoryId == category.id }
                            )
                        }
                    )
                }
            )
        }
}