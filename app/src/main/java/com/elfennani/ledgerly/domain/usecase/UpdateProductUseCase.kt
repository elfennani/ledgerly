package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.model.Product
import com.elfennani.ledgerly.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend operator fun invoke(product: Product) {
        productRepository.upsertProduct(product)
    }
}