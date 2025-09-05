package com.elfennani.ledgerly.di

import com.elfennani.ledgerly.data.repository.AccountRepositoryImpl
import com.elfennani.ledgerly.data.repository.CategoryRepositoryImpl
import com.elfennani.ledgerly.data.repository.GroupRepositoryImpl
import com.elfennani.ledgerly.data.repository.ProductRepositoryImpl
import com.elfennani.ledgerly.domain.repository.AccountRepository
import com.elfennani.ledgerly.domain.repository.CategoryRepository
import com.elfennani.ledgerly.domain.repository.GroupRepository
import com.elfennani.ledgerly.domain.repository.ProductRepository
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

    @Binds
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository
}