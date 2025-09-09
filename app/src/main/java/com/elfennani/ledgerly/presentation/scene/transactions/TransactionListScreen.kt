package com.elfennani.ledgerly.presentation.scene.transactions

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.presentation.scene.transaction_form.TransactionFormRoute
import com.elfennani.ledgerly.presentation.theme.AppTheme

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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToTransactionForm,
                icon = { Icon(painterResource(R.drawable.plus), null) },
                text = { Text("Record") }
            )
        }
    ) { innerPadding ->
        Text(
            text = "Welcome to the Transactions Screen",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview
@Composable
private fun TransactionListScreenPreview() {
    AppTheme {
        TransactionListScreen(state = TransactionListUiState())
    }
}