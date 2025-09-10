package com.elfennani.ledgerly.presentation.scene.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.model.Product
import com.elfennani.ledgerly.domain.model.Transaction
import com.elfennani.ledgerly.domain.model.TransactionSplit
import com.elfennani.ledgerly.presentation.component.TransactionCard
import com.elfennani.ledgerly.presentation.scene.transaction_form.TransactionFormRoute
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.plus
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun TransactionListScreen(
    navController: NavController,
    viewModel: TransactionListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    TransactionListScreen(
        state = state,
        onNavigateToTransactionForm = {
            navController.navigate(TransactionFormRoute)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionListScreen(
    state: TransactionListUiState = TransactionListUiState(),
    onNavigateToTransactionForm: () -> Unit = { }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transactions") }
            )
        },
    ) { innerPadding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (state.transactions.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("No transactions yet")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(innerPadding),
                    contentPadding = innerPadding + PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            val actions = listOf(
                                Triple(R.drawable.arrow_path, "Transfer") {},
                                Triple(R.drawable.arrow_up_circle, "Top-up") {},
                                Triple(R.drawable.document_text, "Record") {
                                    onNavigateToTransactionForm()
                                }
                            )

                            actions.forEach { (icon, label, onClick) ->
                                val interactionSource = remember { MutableInteractionSource() }

                                Column(
                                    modifier = Modifier
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) { onClick() },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(MaterialTheme.shapes.medium)
                                            .indication(interactionSource, indication = ripple())
                                            .background(MaterialTheme.colorScheme.secondaryContainer)
                                            .padding(16.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(icon),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                    Text(
                                        label,
                                        style = MaterialTheme.typography.labelLarge,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            "Last Transactions",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                        )
                    }
                    items(state.transactions) { transaction ->
                        TransactionCard(transaction = transaction)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TransactionListScreenPreview() {
    AppTheme {
        TransactionListScreen(
            state = TransactionListUiState(
                isLoading = false,
                transactions = PreviewTransactions.transactionList
            )
        )
    }
}

object PreviewTransactions {

    // --- Reusable Sample Data ---

    // Products
    private val milk = Product(id = 1, name = "Milk", type = "Dairy", pricePerUnit = 1.50)
    private val bread = Product(id = 2, name = "Bread", type = "Bakery", pricePerUnit = 2.25)
    private val eggs = Product(id = 3, name = "Eggs", type = "Protein", pricePerUnit = 3.00)
    private val steak = Product(id = 4, name = "Ribeye Steak", type = "Meat", pricePerUnit = 15.00)
    private val wine = Product(id = 5, name = "Red Wine", type = "Beverage", pricePerUnit = 12.50)
    private val movieTicket =
        Product(id = 6, name = "Movie Ticket", type = "Entertainment", pricePerUnit = 14.00)
    private val popcorn =
        Product(id = 7, name = "Large Popcorn", type = "Snack", pricePerUnit = 8.50)

    // Categories
    private val groceriesCategory = Category(id = 101, groupId = 1, name = "Groceries")
    private val diningCategory = Category(id = 102, groupId = 1, name = "Dining Out")
    private val entertainmentCategory = Category(id = 103, groupId = 2, name = "Entertainment")

    // Accounts
    private val checkingAccount = Account(id = 201, name = "Main Checking", balance = 1542.78)
    private val creditCard = Account(
        id = 202,
        name = "Visa Rewards",
        balance = -450.21,
        description = "Primary credit card"
    )
    private val savingsAccount = Account(id = 203, name = "High-Yield Savings", balance = 12800.00)


    // --- The Generated List ---

    val transactionList = listOf(
        Transaction(
            id = 1,
            amount = 10.50,
            date = Instant.now().minus(2, ChronoUnit.DAYS),
            title = "Weekly Groceries",
            description = "Essentials for the week",
            category = groceriesCategory,
            account = checkingAccount, // <-- Added Account
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
        Transaction(
            id = 2,
            amount = 42.50,
            date = Instant.now().minus(5, ChronoUnit.DAYS),
            title = "Date Night Dinner",
            description = "Steakhouse downtown",
            category = diningCategory,
            account = creditCard, // <-- Added Account
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
        Transaction(
            id = 3,
            amount = 36.50,
            date = Instant.now().minus(1, ChronoUnit.DAYS),
            title = "Cinema",
            description = null,
            category = entertainmentCategory,
            account = creditCard, // <-- Added Account
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
        Transaction(
            id = 4,
            amount = 4.50,
            date = Instant.now(),
            title = "Morning Bread",
            description = "Quick trip to the bakery",
            category = groceriesCategory,
            account = checkingAccount, // <-- Added Account
            splits = listOf(
                TransactionSplit(
                    transactionId = 4,
                    productId = bread.id,
                    units = 2,
                    totalPrice = 4.50,
                    product = bread
                )
            )
        )
    )
}