package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val productRepository: ProductRepository) {
    operator fun invoke() = productRepository.getProducts()
}