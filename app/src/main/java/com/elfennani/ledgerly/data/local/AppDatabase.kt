package com.elfennani.ledgerly.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.elfennani.ledgerly.data.local.dao.AccountDao
import com.elfennani.ledgerly.data.local.dao.CategoryDao
import com.elfennani.ledgerly.data.local.dao.GroupDao
import com.elfennani.ledgerly.data.local.entities.AccountEntity
import com.elfennani.ledgerly.data.local.entities.CategoryEntity
import com.elfennani.ledgerly.data.local.entities.GroupEntity

@Database(
    entities = [
        AccountEntity::class,
        GroupEntity::class,
        CategoryEntity::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun groupDao(): GroupDao
    abstract fun categoryDao(): CategoryDao
}