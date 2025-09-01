package com.elfennani.ledgerly.di

import android.content.Context
import androidx.room.Room
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
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "ledgerly_db_new")
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideAccountDao(database: AppDatabase) = database.accountDao()

    @Provides
    @Singleton
    fun provideGroupDao(database: AppDatabase) = database.groupDao()
}