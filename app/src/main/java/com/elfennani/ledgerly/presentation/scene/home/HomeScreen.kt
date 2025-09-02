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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.domain.model.Group
import com.elfennani.ledgerly.presentation.component.AccountCard
import com.elfennani.ledgerly.presentation.component.GroupCard
import com.elfennani.ledgerly.presentation.scene.groups.GroupsRoute
import com.elfennani.ledgerly.presentation.scene.home.component.AccountDetailsModal
import com.elfennani.ledgerly.presentation.scene.home.component.CreateAccountModal
import com.elfennani.ledgerly.presentation.scene.home.component.CreateCategoryModal
import com.elfennani.ledgerly.presentation.scene.home.component.DeleteAccountDialog
import com.elfennani.ledgerly.presentation.scene.home.component.EditAccountModal
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.pretty
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
        onEvent = viewModel::onEvent,
        onNavigateToGroups = { navController.navigate(GroupsRoute) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeUiState,
    onEvent: (HomeEvent) -> Unit = {},
    onNavigateToGroups: () -> Unit = {},
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
        val createCategoryModalState = rememberModalBottomSheetState(true)
        val scope = rememberCoroutineScope()

        if (state.isCreateAccountModalVisible) {
            CreateAccountModal(
                formState = state.accountFormState,
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
                formState = state.accountFormState,
                sheetState = editAccountModalState,
                onEvent = onEvent,
                onDismissRequest = {
                    scope.launch { editAccountModalState.hide() }
                        .invokeOnCompletion { onEvent(HomeEvent.DismissEditAccountModal) }
                }
            )
        }

        if (state.createCategoryGroupId != null) {
            CreateCategoryModal(
                formState = state.categoryFormState,
                groups = state.groups,
                sheetState = createCategoryModalState,
                onEvent = onEvent,
                onDismissRequest = {
                    scope.launch { createCategoryModalState.hide() }
                        .invokeOnCompletion { onEvent(HomeEvent.DismissCreateCategoryModal) }
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
                            onClick = { onNavigateToGroups() },
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

                items(state.groups) {
                    GroupCard(
                        title = it.name,
                        collapsed = it.collapsed,
                        onToggleCollapse = { onEvent(HomeEvent.ToggleGroupCollapsed(it.id)) },
                        onAdd = { onEvent(HomeEvent.ShowCreateCategoryModal(it.id)) }
                    ) {
                        if (it.categories == null || it.categories.isEmpty()) {
                            Text(
                                "No categories in this group.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        } else {
                            for (category in it.categories) {
                                Column(
                                    modifier = Modifier
                                        .clickable {}
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.surfaceContainer,
                                            MaterialTheme.shapes.small
                                        )
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        val totalStyle =
                                            MaterialTheme.typography.labelSmall.toSpanStyle()
                                                .copy(color = Color.Gray)
                                        val currentBalanceStyle =
                                            MaterialTheme.typography.titleMedium.toSpanStyle()
                                                .copy(color = MaterialTheme.colorScheme.primary)
                                        val balance by remember(category.target) {
                                            derivedStateOf {
                                                buildAnnotatedString {
                                                    withStyle(currentBalanceStyle) {
                                                        append("$")
                                                        append(category.target?.pretty)
                                                    }
                                                    withStyle(style = totalStyle) {
                                                        append(
                                                            " / $${category.target?.pretty}"
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        Text(
                                            category.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.weight(1f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = balance,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }

                                    LinearProgressIndicator(
                                        progress = { 1f },
                                        modifier = Modifier.fillMaxWidth(),
                                        drawStopIndicator = {}
                                    )
                                }
                            }
                        }
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
                groups = listOf(
                    Group(id = 1, name = "Bills", index = 0)
                )
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