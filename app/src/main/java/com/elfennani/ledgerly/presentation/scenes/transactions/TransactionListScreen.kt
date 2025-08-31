package com.elfennani.ledgerly.presentation.scenes.transactions

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun TransactionListScreen(
    navController: NavController,
    viewModel: TransactionListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    TransactionListScreen(
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionListScreen(
    state: TransactionListUiState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TransactionList") }
            )
        }
    ) { innerPadding ->
        Text(
            text = "Welcome to the TransactionList Screen",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview
@Composable
private fun TransactionListScreenPreview() {
    TransactionListScreen(state = TransactionListUiState())
}