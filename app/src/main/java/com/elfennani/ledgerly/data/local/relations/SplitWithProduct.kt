package com.elfennani.ledgerly.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elfennani.ledgerly.data.local.entities.ProductEntity
import com.elfennani.ledgerly.data.local.entities.TransactionSplitEntity

data class SplitWithProduct(
    @Embedded val split: TransactionSplitEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: ProductEntity
)
