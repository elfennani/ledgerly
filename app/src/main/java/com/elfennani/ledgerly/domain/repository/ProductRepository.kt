package com.elfennani.ledgerly.domain.repository

import com.elfennani.ledgerly.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getProductById(id: Int): Flow<Product?>
    suspend fun upsertProduct(product: Product)
    suspend fun deleteProduct(product: Product)
}