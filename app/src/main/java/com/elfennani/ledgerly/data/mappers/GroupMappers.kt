package com.elfennani.ledgerly.data.mappers

import com.elfennani.ledgerly.data.local.entities.GroupEntity
import com.elfennani.ledgerly.data.local.relations.GroupWithCategories
import com.elfennani.ledgerly.domain.model.Group

fun GroupEntity.toDomain() = Group(
    id = id,
    name = name,
    collapsed = collapsed,
    index = index
)

fun Group.toEntity() = GroupEntity(
    id = id,
    name = name,
    collapsed = collapsed,
    index = index
)

fun GroupWithCategories.toDomain() = Group(
    id = group.id,
    name = group.name,
    collapsed = group.collapsed,
    categories = categories.map { it.toDomain() },
    index = group.index
)