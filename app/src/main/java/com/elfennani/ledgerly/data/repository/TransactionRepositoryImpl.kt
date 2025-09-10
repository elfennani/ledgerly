package com.elfennani.ledgerly.data.repository

import com.elfennani.ledgerly.data.local.dao.TransactionDao
import com.elfennani.ledgerly.data.mappers.toDomain
import com.elfennani.ledgerly.data.mappers.toEntity
import com.elfennani.ledgerly.domain.model.Transaction
import com.elfennani.ledgerly.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun getTransactions(): Flow<List<Transaction>> {
        return transactionDao.getTransactionsFlow()
            .map { it.map { transaction -> transaction.toDomain() } }
    }

    override suspend fun getTransactionById(id: Int): Transaction? {
        return transactionDao.getTransactionById(id)?.toDomain()
    }

    override suspend fun addTransaction(transaction: Transaction) {
        transactionDao.upsertTransactionWithSplits(
            transaction = transaction.toEntity(),
            splits = transaction.splits.map { it.toEntity() }
        )
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.upsertTransactionWithSplits(
            transaction = transaction.toEntity(),
            splits = transaction.splits.map { it.toEntity() }
        )
    }

    override suspend fun deleteTransaction(id: Int) {
        transactionDao.deleteTransactionWithSplits(id)
    }
}