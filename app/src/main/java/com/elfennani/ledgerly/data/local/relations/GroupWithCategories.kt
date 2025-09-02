package com.elfennani.ledgerly.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.elfennani.ledgerly.data.local.entities.CategoryEntity
import com.elfennani.ledgerly.data.local.entities.GroupEntity

data class GroupWithCategories(
    @Embedded val group: GroupEntity,

    @Relation(parentColumn = "id", entityColumn = "groupId")
    val categories: List<CategoryEntity>
)
