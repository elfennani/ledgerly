package com.elfennani.ledgerly.data.mappers

import com.elfennani.ledgerly.data.local.entities.TransactionEntity
import com.elfennani.ledgerly.data.local.entities.TransactionSplitEntity
import com.elfennani.ledgerly.data.local.relations.SplitWithProduct
import com.elfennani.ledgerly.data.local.relations.TransactionWithSplits
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.model.Product
import com.elfennani.ledgerly.domain.model.Transaction
import com.elfennani.ledgerly.domain.model.TransactionSplit
import java.time.Instant

fun TransactionEntity.toDomain(
    splits: List<TransactionSplit>,
    category: Category?,
    account: Account
) = if (isTopUp) Transaction.Inflow(
    id = id,
    date = Instant.ofEpochMilli(date),
    description = description,
    amount = amount,
    title = title,
    account = account
) else Transaction.Outflow(
    id = id,
    date = Instant.ofEpochMilli(date),
    description = description,
    amount = amount,
    title = title,
    splits = splits,
    category = category!!,
    account = account
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    date = date.toEpochMilli(),
    description = description,
    amount = amount,
    title = title,
    categoryId = if (this is Transaction.Outflow) this.category.id else null,
    accountId = account.id,
    isTopUp = this is Transaction.Inflow
)

fun TransactionSplitEntity.toDomain(product: Product) = TransactionSplit(
    transactionId = transactionId,
    productId = productId,
    units = units,
    totalPrice = totalPrice,
    product = product,
)

fun TransactionSplit.toEntity() = TransactionSplitEntity(
    transactionId = transactionId,
    productId = productId,
    units = units,
    totalPrice = totalPrice
)

fun SplitWithProduct.toDomain() = this.split.toDomain(product.toDomain())
fun TransactionWithSplits.toDomain() = this.transaction.toDomain(
    splits = this.splits.map { it.toDomain() },
    category = this.category?.toDomain(),
    account = this.account.toDomain()
)