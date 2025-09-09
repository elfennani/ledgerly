package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.model.Transaction
import com.elfennani.ledgerly.domain.repository.TransactionRepository
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.addTransaction(transaction)
    }
}