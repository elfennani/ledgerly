package com.elfennani.ledgerly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elfennani.ledgerly.data.local.dao.AccountDao
import com.elfennani.ledgerly.data.local.entities.AccountEntity

@Database(
    entities = [
        AccountEntity::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}