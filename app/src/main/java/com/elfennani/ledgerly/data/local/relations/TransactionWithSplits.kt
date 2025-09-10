package com.elfennani.ledgerly.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elfennani.ledgerly.data.local.entities.CategoryEntity
import com.elfennani.ledgerly.data.local.entities.TransactionEntity
import com.elfennani.ledgerly.data.local.entities.TransactionSplitEntity

data class TransactionWithSplits(
    @Embedded val transaction: TransactionEntity,

    @Relation(
        entity = TransactionSplitEntity::class,
        parentColumn = "id",
        entityColumn = "transactionId"
    )
    val splits: List<SplitWithProduct>,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity
)
