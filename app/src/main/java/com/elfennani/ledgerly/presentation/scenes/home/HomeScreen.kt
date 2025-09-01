package com.elfennani.ledgerly.presentation.scenes.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.presentation.component.AccountCard
import com.elfennani.ledgerly.presentation.component.GroupCard
import com.elfennani.ledgerly.presentation.scenes.products.ProductListRoute
import com.elfennani.ledgerly.presentation.theme.AppTheme

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    HomeScreen(
        state = state,
        navigateToProduct = { navController.navigate(ProductListRoute) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeUiState,
    navigateToProduct: () -> Unit = { }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") }
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Top)
    ) { innerPadding ->
        Log.d(TAG, "HomeScreen: $innerPadding")
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Accounts",
                style = MaterialTheme.typography.labelLarge
            )
            AccountCard(
                account = Account(
                    id = 1,
                    name = "Checking Account",
                    balance = 1234.56
                ),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable {}
            )

            AccountCard(
                account = Account(
                    id = 2,
                    name = "Savings Account",
                    balance = 7890.12
                ),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable {}
            )

            AccountCard(
                account = Account(
                    id = 3,
                    name = "Credit Card",
                    balance = 345.0
                ),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable {}
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Categories",
                style = MaterialTheme.typography.labelLarge
            )

            GroupCard(title = "🧾 Bills") {
                AccountCard(
                    account = Account(
                        id = 1,
                        name = "🛒 Groceries",
                        balance = 1234.56
                    ),
                    modifier = Modifier.clickable {}
                )
                AccountCard(
                    account = Account(
                        id = 1,
                        name = "🌎 Internet",
                        balance = 55.56
                    ),
                    modifier = Modifier.clickable {}
                )
                AccountCard(
                    account = Account(
                        id = 1,
                        name = "🎶 Music",
                        balance = 1435.56
                    ),
                    modifier = Modifier.clickable {}
                )
            }

            GroupCard(title = "🏦 Investments", defaultCollapsed = false) {
                AccountCard(
                    account = Account(
                        id = 1,
                        name = "📈 Stocks",
                        balance = 1234.56
                    ),
                    modifier = Modifier.clickable {}
                )
                AccountCard(
                    account = Account(
                        id = 1,
                        name = "🏠 Real Estate",
                        balance = 55.56
                    ),
                    modifier = Modifier.clickable {}
                )
            }

        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(state = HomeUiState())
    }
}