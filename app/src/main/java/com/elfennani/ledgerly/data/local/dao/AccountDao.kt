package com.elfennani.ledgerly.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.elfennani.ledgerly.data.local.entities.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    fun getAccountById(id: Int): Flow<AccountEntity?>

    @Insert
    suspend fun insertAccount(account: AccountEntity): Long

    @Upsert
    suspend fun updateAccount(account: AccountEntity)

    @Delete
    suspend fun deleteAccount(account: AccountEntity)

    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun deleteAccountById(id: Int)
}