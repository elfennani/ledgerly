package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.model.HomeOverview
import com.elfennani.ledgerly.domain.repository.AccountRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHomeOverviewUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke() = accountRepository.accountListFlow.map {
        HomeOverview(
            accounts = it
        )
    }
}