package com.elfennani.ledgerly.di

import com.elfennani.ledgerly.data.repository.AccountRepositoryImpl
import com.elfennani.ledgerly.data.repository.GroupRepositoryImpl
import com.elfennani.ledgerly.domain.repository.AccountRepository
import com.elfennani.ledgerly.domain.repository.GroupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAccountRepository(
        accountRepositoryImpl: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    abstract fun bindGroupRepository(
        groupRepositoryImpl: GroupRepositoryImpl
    ): GroupRepository
}