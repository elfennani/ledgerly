package com.elfennani.ledgerly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elfennani.ledgerly.data.local.dao.AccountDao
import com.elfennani.ledgerly.data.local.dao.GroupDao
import com.elfennani.ledgerly.data.local.entities.AccountEntity
import com.elfennani.ledgerly.data.local.entities.GroupEntity

@Database(
    entities = [
        AccountEntity::class,
        GroupEntity::class
    ],
    version = 1,
    autoMigrations = [
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun groupDao(): GroupDao
}