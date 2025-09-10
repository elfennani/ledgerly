package com.elfennani.ledgerly.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.elfennani.ledgerly.data.local.dao.AccountDao
import com.elfennani.ledgerly.data.local.dao.CategoryDao
import com.elfennani.ledgerly.data.local.dao.GroupDao
import com.elfennani.ledgerly.data.local.dao.ProductDao
import com.elfennani.ledgerly.data.local.dao.TransactionDao
import com.elfennani.ledgerly.data.local.entities.AccountEntity
import com.elfennani.ledgerly.data.local.entities.CategoryBudgetEntity
import com.elfennani.ledgerly.data.local.entities.CategoryEntity
import com.elfennani.ledgerly.data.local.entities.GroupEntity
import com.elfennani.ledgerly.data.local.entities.ProductEntity
import com.elfennani.ledgerly.data.local.entities.TransactionEntity
import com.elfennani.ledgerly.data.local.entities.TransactionSplitEntity

@Database(
    entities = [
        AccountEntity::class,
        GroupEntity::class,
        CategoryEntity::class,
        CategoryBudgetEntity::class,
        ProductEntity::class,
        TransactionEntity::class,
        TransactionSplitEntity::class
    ],
    version = 8,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = AppDatabase.DeletedTargetMigration::class),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7)
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun groupDao(): GroupDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao

    @DeleteColumn(tableName = "categories", columnName = "target")
    class DeletedTargetMigration : AutoMigrationSpec
}