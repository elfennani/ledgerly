package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.model.HomeOverview
import com.elfennani.ledgerly.domain.repository.AccountRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetHomeOverviewUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val getGroupsUseCase: GetGroupsUseCase
) {
    operator fun invoke() =
        combine(accountRepository.accountListFlow, getGroupsUseCase()) { accounts, groups ->
        HomeOverview(
            accounts = accounts,
            groups = groups.sortedBy { it.index }
        )
    }
}