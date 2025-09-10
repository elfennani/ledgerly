package com.elfennani.ledgerly.data.mappers

import com.elfennani.ledgerly.data.local.entities.AccountEntity
import com.elfennani.ledgerly.domain.model.Account

fun AccountEntity.toDomain() = Account(
    id = id,
    name = name,
    balance = balance,
    description = description,
)

fun Account.toEntity() = AccountEntity(
    id = id,
    name = name,
    balance = balance,
    description = description
)