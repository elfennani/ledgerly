package com.elfennani.ledgerly.presentation.scene.groups

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
import com.elfennani.ledgerly.presentation.theme.AppTheme

@Composable
fun GroupsScreen(
    navController: NavController,
    viewModel: GroupsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    GroupsScreen(
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupsScreen(
    state: GroupsUiState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Groups") }
            )
        }
    ) { innerPadding ->
        Text(
            text = "Welcome to the Groups Screen",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview
@Composable
private fun GroupsScreenPreview() {
    AppTheme {
        GroupsScreen(state = GroupsUiState())
    }
}