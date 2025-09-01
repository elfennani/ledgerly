package com.elfennani.ledgerly.presentation.scene.groups

import com.elfennani.ledgerly.domain.model.Group
import com.elfennani.ledgerly.presentation.scene.groups.model.GroupFormState

data class GroupsUiState(
    val isLoading: Boolean = false,
    val groups: List<Group> = emptyList(),
    val editingGroupId: Int? = null,
    val isAddGroupDialogVisible: Boolean = false,
    val groupFormState: GroupFormState = GroupFormState()
)
