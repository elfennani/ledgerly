package com.elfennani.ledgerly.presentation.scene.transactions

import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.model.Product
import com.elfennani.ledgerly.domain.model.Transaction
import com.elfennani.ledgerly.domain.model.TransactionSplit
import java.time.Instant
import java.time.temporal.ChronoUnit

object PreviewTransactions {

    // --- Reusable Sample Data ---

    private val milk = Product(id = 1, name = "Milk", type = "Dairy", pricePerUnit = 1.50)
    private val bread = Product(id = 2, name = "Bread", type = "Bakery", pricePerUnit = 2.25)
    private val eggs = Product(id = 3, name = "Eggs", type = "Protein", pricePerUnit = 3.00)
    private val steak = Product(id = 4, name = "Ribeye Steak", type = "Meat", pricePerUnit = 15.00)
    private val wine = Product(id = 5, name = "Red Wine", type = "Beverage", pricePerUnit = 12.50)
    private val movieTicket =
        Product(id = 6, name = "Movie Ticket", type = "Entertainment", pricePerUnit = 14.00)
    private val popcorn =
        Product(id = 7, name = "Large Popcorn", type = "Snack", pricePerUnit = 8.50)

    private val groceriesCategory = Category(id = 101, groupId = 1, name = "Groceries")
    private val diningCategory = Category(id = 102, groupId = 1, name = "Dining Out")
    private val entertainmentCategory = Category(id = 103, groupId = 2, name = "Entertainment")

    private val checkingAccount = Account(id = 201, name = "Main Checking", balance = 1542.78)
    private val creditCard = Account(
        id = 202,
        name = "Visa Rewards",
        balance = -450.21,
        description = "Primary credit card"
    )


    // --- The Generated List ---

    val transactionList: List<Transaction> = listOf(
        Transaction.Outflow(
            id = 1,
            amount = 10.50,
            date = Instant.now().minus(2, ChronoUnit.DAYS),
            title = "Weekly Groceries",
            description = "Essentials for the week",
            account = checkingAccount,
            category = groceriesCategory,
            splits = listOf(
                TransactionSplit(
                    transactionId = 1,
                    productId = milk.id,
                    units = 2,
                    totalPrice = 3.00,
                    product = milk
                ),
                TransactionSplit(
                    transactionId = 1,
                    productId = bread.id,
                    units = 1,
                    totalPrice = 2.25,
                    product = bread
                ),
                TransactionSplit(
                    transactionId = 1,
                    productId = eggs.id,
                    units = 1,
                    totalPrice = 3.00,
                    product = eggs
                )
            )
        ),
        Transaction.Outflow(
            id = 2,
            amount = 42.50,
            date = Instant.now().minus(5, ChronoUnit.DAYS),
            title = "Date Night Dinner",
            description = "Steakhouse downtown",
            account = creditCard,
            category = diningCategory,
            splits = listOf(
                TransactionSplit(
                    transactionId = 2,
                    productId = steak.id,
                    units = 2,
                    totalPrice = 30.00,
                    product = steak
                ),
                TransactionSplit(
                    transactionId = 2,
                    productId = wine.id,
                    units = 1,
                    totalPrice = 12.50,
                    product = wine
                )
            )
        ),
        Transaction.Inflow(
            id = 5,
            amount = 2250.00,
            date = Instant.now().minus(7, ChronoUnit.DAYS),
            title = "Paycheck",
            description = "Bi-weekly salary",
            account = checkingAccount
        ),
        Transaction.Outflow(
            id = 3,
            amount = 36.50,
            date = Instant.now().minus(1, ChronoUnit.DAYS),
            title = "Cinema",
            description = null,
            account = creditCard,
            category = entertainmentCategory,
            splits = listOf(
                TransactionSplit(
                    transactionId = 3,
                    productId = movieTicket.id,
                    units = 2,
                    totalPrice = 28.00,
                    product = movieTicket
                ),
                TransactionSplit(
                    transactionId = 3,
                    productId = popcorn.id,
                    units = 1,
                    totalPrice = 8.50,
                    product = popcorn
                )
            )
        ),
        Transaction.Outflow(
            id = 4,
            amount = 4.50,
            date = Instant.now(),
            title = "Morning Bread",
            description = "Quick trip to the bakery",
            account = checkingAccount,
            category = groceriesCategory,
            splits = listOf(
                TransactionSplit(
                    transactionId = 4,
                    productId = bread.id,
                    units = 2,
                    totalPrice = 4.50,
                    product = bread
                )
            )
        ),
        Transaction.Inflow(
            id = 6,
            amount = 14.00,
            date = Instant.now().minus(4, ChronoUnit.HOURS),
            title = "Movie Ticket Refund",
            description = "Refund for canceled show",
            account = creditCard
        )
    ).sortedByDescending { it.date }
}