package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.AccountRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(accountId: Int) {
        accountRepository.deleteAccountById(accountId)
    }
}