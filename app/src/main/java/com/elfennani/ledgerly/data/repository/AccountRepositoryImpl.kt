package com.elfennani.ledgerly.data.repository

import com.elfennani.ledgerly.data.local.dao.AccountDao
import com.elfennani.ledgerly.data.local.entities.AccountEntity
import com.elfennani.ledgerly.data.mappers.toDomain
import com.elfennani.ledgerly.data.mappers.toEntity
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {
    override val accountListFlow: Flow<List<Account>>
        get() = accountDao.getAllAccounts().map { it.map { account -> account.toDomain() } }

    override suspend fun getAccountById(id: Int): Flow<Account?> =
        accountDao.getAccountById(id).map { it?.toDomain() }

    override suspend fun insertAccount(
        name: String,
        initialBalance: Double,
        description: String?
    ): Long {
        return accountDao.insertAccount(
            AccountEntity(
                name = name,
                balance = initialBalance,
                description = description
            )
        )
    }

    override suspend fun updateAccount(account: Account) {
        accountDao.updateAccount(account.toEntity())
    }

    override suspend fun deleteAccount(account: Account) {
        accountDao.deleteAccount(account.toEntity())
    }

    override suspend fun deleteAccountById(id: Int) {
        accountDao.deleteAccountById(id)
    }

}