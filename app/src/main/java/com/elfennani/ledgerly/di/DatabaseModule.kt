package com.elfennani.ledgerly.di

import android.content.Context
import androidx.core.database.getIntOrNull
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.elfennani.ledgerly.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            var firstAccountId: Int? = null
            database.query("SELECT * FROM accounts LIMIT 1").use { cursor ->
                if (cursor.moveToFirst()) {
                    firstAccountId = cursor.getIntOrNull(cursor.getColumnIndexOrThrow("id"))
                }
            }
            if (firstAccountId != null)
                database.execSQL("ALTER TABLE transactions ADD COLUMN accountId INTEGER NOT NULL DEFAULT $firstAccountId")
            else {
                database.execSQL("DELETE FROM transactions")
                database.execSQL("ALTER TABLE transactions ADD COLUMN accountId INTEGER NOT NULL")
            }
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "ledgerly_db_new")
            .fallbackToDestructiveMigration(false)
            .addMigrations(MIGRATION_7_8)
            .build()
    }

    @Provides
    @Singleton
    fun provideAccountDao(database: AppDatabase) = database.accountDao()

    @Provides
    @Singleton
    fun provideGroupDao(database: AppDatabase) = database.groupDao()

    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase) = database.categoryDao()

    @Provides
    @Singleton
    fun provideProductDao(database: AppDatabase) = database.productDao()

    @Provides
    @Singleton
    fun provideTransactionDao(database: AppDatabase) = database.transactionDao()
}