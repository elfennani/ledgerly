package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.AccountRepository
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(
        name: String,
        initialBalance: Double,
        description: String?
    ): Long {
        return accountRepository.insertAccount(name, initialBalance, description)
    }
}