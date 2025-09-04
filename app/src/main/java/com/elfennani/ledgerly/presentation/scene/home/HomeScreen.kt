package com.elfennani.ledgerly.presentation.scene.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.domain.model.BudgetData
import com.elfennani.ledgerly.domain.model.Group
import com.elfennani.ledgerly.presentation.component.AccountCard
import com.elfennani.ledgerly.presentation.component.GroupCard
import com.elfennani.ledgerly.presentation.scene.category.CategoryRoute
import com.elfennani.ledgerly.presentation.scene.groups.GroupsRoute
import com.elfennani.ledgerly.presentation.scene.home.component.AccountDetailsModal
import com.elfennani.ledgerly.presentation.scene.home.component.CreateAccountModal
import com.elfennani.ledgerly.presentation.scene.home.component.CreateCategoryModal
import com.elfennani.ledgerly.presentation.scene.home.component.DeleteAccountDialog
import com.elfennani.ledgerly.presentation.scene.home.component.EditAccountModal
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.pretty
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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
        onNavigateToGroups = { navController.navigate(GroupsRoute) },
        onNavigateToCategory = { categoryId -> navController.navigate(CategoryRoute(categoryId)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeUiState,
    onEvent: (HomeEvent) -> Unit = {},
    onNavigateToGroups: () -> Unit = {},
    onNavigateToCategory: (Int) -> Unit = {},
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
        val pagerState = rememberPagerState(
            pageCount = { state.accounts.size }
        )

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
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "Accounts",
                            style = MaterialTheme.typography.labelLarge
                        )

                        if (state.accounts.isNotEmpty())
                            TextButton(
                                onClick = { onEvent(HomeEvent.ShowCreateAccountModal) },
                                contentPadding = PaddingValues(horizontal = 12.dp),
                                modifier = Modifier.offset(x = 12.dp)
                            ) {
                                Icon(painterResource(R.drawable.plus_small), null)
                                Spacer(Modifier.width(4.dp))
                                Text("Add")
                            }
                    }
                }

                if (state.accounts.isEmpty()) {
                    item(
                        key = "no_accounts",
                        contentType = "no_accounts"
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
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
                } else {
                    item(
                        key = "accounts_pager",
                        contentType = "accounts_pager"
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier,
                            pageSpacing = 8.dp,
                            snapPosition = SnapPosition.Center,
                            contentPadding = PaddingValues(horizontal = 16.dp),
                        ) {
                            val account = state.accounts[it]

                            AccountCard(
                                account = account,
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.small)
                                    .fillMaxWidth()
                                    .clickable {
                                        onEvent(HomeEvent.ShowAccountDetailsModal(account.id))
                                    }
                            )
                        }
                    }
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "Categories",
                            style = MaterialTheme.typography.labelLarge
                        )

                        TextButton(
                            onClick = { onNavigateToGroups() },
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            modifier = Modifier.offset(x = 12.dp)
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

                if (state.budgetData.unused != 0.0) {
                    val percentage =
                        (state.budgetData.total - state.budgetData.unused) / state.budgetData.total
                    item {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    progress = { percentage.toFloat() },
                                    trackColor = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(56.dp),
                                )
                                Text(
                                    "${(percentage * 100).roundToInt()}%",
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.W600
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(0.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Ready to Assign",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = "$${state.budgetData.unused.pretty} Left to Budget",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                items(state.groups) {
                    GroupCard(
                        group = it,
                        onAdd = {
                            onEvent(HomeEvent.ShowCreateCategoryModal(it.id))
                        },
                        onToggleCollapse = {
                            onEvent(HomeEvent.ToggleGroupCollapsed(it.id))
                        },
                        onPressCategory = { category ->
                            onNavigateToCategory(category.id)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
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
                ),
                budgetData = BudgetData(
                    used = 157.00,
                    unused = 843.00,
                    total = 1000.00
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