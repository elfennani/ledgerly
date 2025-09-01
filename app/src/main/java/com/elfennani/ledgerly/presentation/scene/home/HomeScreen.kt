package com.elfennani.ledgerly.presentation.scene.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.presentation.component.AccountCard
import com.elfennani.ledgerly.presentation.component.GroupCard
import com.elfennani.ledgerly.presentation.scene.home.component.AccountDetailsModal
import com.elfennani.ledgerly.presentation.scene.home.component.CreateAccountModal
import com.elfennani.ledgerly.presentation.scene.home.component.DeleteAccountDialog
import com.elfennani.ledgerly.presentation.scene.home.component.EditAccountModal
import com.elfennani.ledgerly.presentation.theme.AppTheme
import kotlinx.coroutines.launch

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    HomeScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeUiState,
    onEvent: (HomeEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") }
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Top)
    ) { innerPadding ->
        val createAccountModalState = rememberModalBottomSheetState(true)
        val accountDetailsModalState = rememberModalBottomSheetState(true)
        val editAccountModalState = rememberModalBottomSheetState(true)
        val scope = rememberCoroutineScope()

        if (state.isCreateAccountModalVisible) {
            CreateAccountModal(
                formState = state.formState,
                sheetState = createAccountModalState,
                onEvent = onEvent,
                onDismissRequest = {
                    scope.launch { createAccountModalState.hide() }
                        .invokeOnCompletion { onEvent(HomeEvent.DismissCreateAccountModal) }
                }
            )
        }

        if (state.selectedAccount != null) {
            AccountDetailsModal(
                sheetState = accountDetailsModalState,
                account = state.accounts.first { it.id == state.selectedAccount },
                onDismissRequest = {
                    scope.launch { accountDetailsModalState.hide() }
                        .invokeOnCompletion { onEvent(HomeEvent.DismissAccountDetailsModal) }
                },
                onEvent = onEvent
            )
        }

        if (state.editingAccount != null) {
            EditAccountModal(
                formState = state.formState,
                sheetState = editAccountModalState,
                onEvent = onEvent,
                onDismissRequest = {
                    scope.launch { editAccountModalState.hide() }
                        .invokeOnCompletion { onEvent(HomeEvent.DismissEditAccountModal) }
                }
            )
        }

        if (state.toDeleteAccount != null) {
            val account by remember {
                derivedStateOf { state.accounts.first { it.id == state.toDeleteAccount } }
            }
            DeleteAccountDialog(account, onEvent)
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Accounts",
                            style = MaterialTheme.typography.labelLarge
                        )

                        if (state.accounts.isNotEmpty())
                            TextButton(
                                onClick = { onEvent(HomeEvent.ShowCreateAccountModal) },
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) {
                                Icon(painterResource(R.drawable.plus_small), null)
                                Spacer(Modifier.width(4.dp))
                                Text("Add")
                            }
                    }
                }

                if (state.accounts.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    MaterialTheme.shapes.medium
                                )
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "No accounts found.",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { onEvent(HomeEvent.ShowCreateAccountModal) }) {
                                Icon(painterResource(R.drawable.plus_small), null)
                                Spacer(Modifier.width(4.dp))
                                Text("Create Account")
                            }
                        }
                    }
                }

                items(state.accounts) { account ->
                    AccountCard(
                        account = account,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .clickable {
                                onEvent(HomeEvent.ShowAccountDetailsModal(account.id))
                            }
                    )
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            "Groups",
                            style = MaterialTheme.typography.labelLarge
                        )

                        TextButton(
                            onClick = { onEvent(HomeEvent.ShowCreateAccountModal) },
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.adjustments_horizontal),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Manage")
                        }
                    }
                }


                item {
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
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            state = HomeUiState(
                accounts = listOf(
                    Account(id = 1, name = "Checking", balance = 1234.56),
                    Account(id = 2, name = "Savings", balance = 9876.54),
                    Account(id = 3, name = "Credit Card", balance = 123.45)
                ),
            )
        )
    }
}

@Preview
@Composable
private fun HomeScreenNoAccountPreview() {
    AppTheme {
        HomeScreen(state = HomeUiState(accounts = emptyList()))
    }
}

@Preview
@Composable
private fun HomeScreenLoadingPreview() {
    AppTheme {
        HomeScreen(state = HomeUiState(isLoading = true))
    }
}