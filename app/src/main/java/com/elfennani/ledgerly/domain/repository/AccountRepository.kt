package com.elfennani.ledgerly.domain.repository

import com.elfennani.ledgerly.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    val accountListFlow: Flow<List<Account>>

    suspend fun getAccountById(id: Int): Flow<Account?>
    suspend fun insertAccount(name: String, initialBalance: Double, description: String?): Long
    suspend fun updateAccount(account: Account)
    suspend fun deleteAccount(account: Account)
    suspend fun deleteAccountById(id: Int)
}