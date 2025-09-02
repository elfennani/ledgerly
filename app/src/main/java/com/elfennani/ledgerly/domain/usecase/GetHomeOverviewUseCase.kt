package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.model.HomeOverview
import com.elfennani.ledgerly.domain.repository.AccountRepository
import com.elfennani.ledgerly.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetHomeOverviewUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val getGroupsUseCase: GetGroupsUseCase,
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke() =
        combine(
            accountRepository.accountListFlow,
            getGroupsUseCase(),
            categoryRepository.getCategoryBudgetsFlow()
        ) { accounts, groups, budgets ->
            HomeOverview(
                accounts = accounts,
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