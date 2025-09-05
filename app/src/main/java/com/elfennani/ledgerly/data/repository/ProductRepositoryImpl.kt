package com.elfennani.ledgerly.data.repository

import com.elfennani.ledgerly.data.local.dao.ProductDao
import com.elfennani.ledgerly.data.mappers.toDomain
import com.elfennani.ledgerly.data.mappers.toEntity
import com.elfennani.ledgerly.domain.model.Product
import com.elfennani.ledgerly.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {
    override fun getProducts(): Flow<List<Product>> =
        productDao.getProducts().map { it.map { product -> product.toDomain() } }

    override fun getProductById(id: Int): Flow<Product?> =
        productDao.getProductById(id).map { it?.toDomain() }

    override suspend fun upsertProduct(product: Product) {
        productDao.upsertProduct(product.toEntity())
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProductById(product.id)
    }
}