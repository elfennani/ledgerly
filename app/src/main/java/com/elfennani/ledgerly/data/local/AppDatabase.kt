package com.elfennani.ledgerly.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.elfennani.ledgerly.data.local.dao.AccountDao
import com.elfennani.ledgerly.data.local.dao.CategoryDao
import com.elfennani.ledgerly.data.local.dao.GroupDao
import com.elfennani.ledgerly.data.local.entities.AccountEntity
import com.elfennani.ledgerly.data.local.entities.CategoryBudgetEntity
import com.elfennani.ledgerly.data.local.entities.CategoryEntity
import com.elfennani.ledgerly.data.local.entities.GroupEntity

@Database(
    entities = [
        AccountEntity::class,
        GroupEntity::class,
        CategoryEntity::class,
        CategoryBudgetEntity::class
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = AppDatabase.DeletedTargetMigration::class)
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun groupDao(): GroupDao
    abstract fun categoryDao(): CategoryDao

    @DeleteColumn(tableName = "categories", columnName = "target")
    class DeletedTargetMigration : AutoMigrationSpec
}