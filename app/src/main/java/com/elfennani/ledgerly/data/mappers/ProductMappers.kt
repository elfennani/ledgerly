package com.elfennani.ledgerly.data.mappers

import com.elfennani.ledgerly.data.local.entities.ProductEntity
import com.elfennani.ledgerly.domain.model.Product

fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        name = this.name,
        description = this.description,
        type = this.type,
        defaultUnit = this.defaultUnit,
        pricePerUnit = this.pricePerUnit
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        type = this.type,
        defaultUnit = this.defaultUnit,
        pricePerUnit = this.pricePerUnit
    )
}