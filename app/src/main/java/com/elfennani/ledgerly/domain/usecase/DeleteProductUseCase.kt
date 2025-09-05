package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.ProductRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend operator fun invoke(id: Int) {
        val product = productRepository.getProductById(id).first()
        if (product != null)
            productRepository.deleteProduct(product)
    }
}