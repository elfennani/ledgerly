package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {
    operator fun invoke() = transactionRepository.getTransactions()
}