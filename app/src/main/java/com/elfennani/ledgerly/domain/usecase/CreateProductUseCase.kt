package com.elfennani.ledgerly.domain.usecase

import com.elfennani.ledgerly.domain.model.Product
import com.elfennani.ledgerly.domain.repository.ProductRepository
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend operator fun invoke(
        name: String,
        description: String?,
        type: String,
        defaultUnit: String?,
        pricePerUnit: Double?
    ) {
        val product = Product(
            name = name,
            description = description,
            type = type,
            defaultUnit = defaultUnit,
            pricePerUnit = pricePerUnit
        )
        productRepository.upsertProduct(product)
    }
}