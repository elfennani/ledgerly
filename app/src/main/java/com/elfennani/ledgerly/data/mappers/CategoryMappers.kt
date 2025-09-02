package com.elfennani.ledgerly.data.mappers

import com.elfennani.ledgerly.data.local.entities.CategoryEntity
import com.elfennani.ledgerly.domain.model.Category

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = this.id,
        groupId = this.groupId,
        name = this.name,
        target = this.target
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        groupId = this.groupId,
        name = this.name,
        target = this.target
    )
}