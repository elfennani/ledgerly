package com.elfennani.ledgerly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.elfennani.ledgerly.data.local.entities.TransactionEntity
import com.elfennani.ledgerly.data.local.entities.TransactionSplitEntity
import com.elfennani.ledgerly.data.local.relations.TransactionWithSplits
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Transaction
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getTransactionsFlow(): Flow<List<TransactionWithSplits>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionWithSplits?

    @Upsert
    suspend fun upsertTransaction(transaction: TransactionEntity): Long

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Int)

    @Query("DELETE FROM transaction_splits WHERE transactionId = :transactionId")
    suspend fun deleteSplitsByTransactionId(transactionId: Int)

    @Transaction
    suspend fun deleteTransactionWithSplits(id: Int) {
        deleteSplitsByTransactionId(id)
        deleteTransactionById(id)
    }

    @Insert
    suspend fun insertSplits(splits: List<TransactionSplitEntity>)

    @Transaction
    suspend fun upsertTransactionWithSplits(
        transaction: TransactionEntity,
        splits: List<TransactionSplitEntity>
    ): Long {
        val transactionId = upsertTransaction(transaction).toInt()
        deleteSplitsByTransactionId(transactionId)
        val splitsWithTransactionId = splits.map { it.copy(transactionId = transactionId) }
        insertSplits(splitsWithTransactionId)
        return transactionId.toLong()
    }
}