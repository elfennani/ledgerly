package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(
        accountId: Int,
        name: String,
        initialBalance: Double,
        description: String?
    ) {
        accountRepository.updateAccount(
            Account(
                id = accountId,
                name = name,
                balance = initialBalance,
                description = description
            )
        )
    }
}