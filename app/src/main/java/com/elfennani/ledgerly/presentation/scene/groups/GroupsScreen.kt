package com.elfennani.ledgerly.presentation.scene.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Group
import com.elfennani.ledgerly.presentation.scene.groups.component.CreateGroupModal
import com.elfennani.ledgerly.presentation.scene.groups.component.EditGroupModal
import com.elfennani.ledgerly.presentation.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun GroupsScreen(
    navController: NavController,
    viewModel: GroupsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    GroupsScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupsScreen(
    state: GroupsUiState,
    onEvent: (GroupsEvent) -> Unit = { },
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Groups") }
            )
        }
    ) { innerPadding ->
        val createGroupModal = rememberModalBottomSheetState(true)
        val editGroupModal = rememberModalBottomSheetState(true)
        val scope = rememberCoroutineScope()

        if (state.isAddGroupDialogVisible) {
            CreateGroupModal(
                sheetState = createGroupModal,
                formState = state.groupFormState,
                onEvent = onEvent,
                onDismissRequest = {
                    scope.launch {
                        createGroupModal.hide()
                    }.invokeOnCompletion { onEvent(GroupsEvent.DismissAddGroupModal) }
                }
            )
        }

        if (state.editingGroupId != null) {
            EditGroupModal(
                sheetState = editGroupModal,
                formState = state.groupFormState,
                onEvent = onEvent,
                groupId = state.editingGroupId,
                onDismissRequest = {
                    scope.launch {
                        editGroupModal.hide()
                    }.invokeOnCompletion { onEvent(GroupsEvent.DismissEditGroupModal) }
                }
            )
        }

        if (state.isLoading) {
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (state.groups.isEmpty()) {
                    item {
                        Text(
                            text = "No groups available.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        )
                    }
                }
                items(state.groups) { group ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceContainer,
                                MaterialTheme.shapes.small
                            )
                            .padding(vertical = 8.dp)
                            .padding(start = 16.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.bars_2),
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            group.name,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        TextButton(
                            onClick = { onEvent(GroupsEvent.ShowEditGroupModal(group.id)) },
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Icon(painterResource(R.drawable.pencil_square), null)
                            Spacer(Modifier.width(6.dp))
                            Text("Edit")
                        }
                        IconButton(onClick = { onEvent(GroupsEvent.DeleteGroup(group.id)) }) {
                            Icon(
                                painter = painterResource(R.drawable.trash),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        onClick = { onEvent(GroupsEvent.ShowAddGroupModal) },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = null
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Add Group")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GroupsScreenPreview() {
    AppTheme {
        GroupsScreen(
            state = GroupsUiState(
                isLoading = false,
                groups = listOf(
                    Group(1, "Family"),
                    Group(2, "Friends"),
                    Group(3, "Work")
                )
            )
        )
    }
}