package com.elfennani.ledgerly.presentation.scene.groups

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.ledgerly.domain.usecase.CreateGroupUseCase
import com.elfennani.ledgerly.domain.usecase.DeleteGroupUseCase
import com.elfennani.ledgerly.domain.usecase.GetGroupsUseCase
import com.elfennani.ledgerly.domain.usecase.MoveGroupUseCase
import com.elfennani.ledgerly.domain.usecase.UpdateGroupUseCase
import com.elfennani.ledgerly.presentation.scene.groups.model.GroupFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor(
    getGroups: GetGroupsUseCase,
    private val updateGroupUseCase: UpdateGroupUseCase,
    private val deleteGroupUseCase: DeleteGroupUseCase,
    private val createGroupUseCase: CreateGroupUseCase,
    private val moveGroupUseCase: MoveGroupUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GroupsUiState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getGroups().collect { groups ->
                _state.value = GroupsUiState(
                    groups = groups,
                    isLoading = false
                )
            }
        }
    }

    fun onEvent(event: GroupsEvent) {
        when (event) {
            GroupsEvent.DismissAddGroupModal -> {
                _state.update {
                    it.copy(
                        isAddGroupDialogVisible = false,
                        groupFormState = GroupFormState()
                    )
                }
            }

            GroupsEvent.DismissEditGroupModal -> {
                _state.update {
                    it.copy(
                        editingGroupId = null,
                        groupFormState = GroupFormState()
                    )
                }
            }

            is GroupsEvent.OnGroupNameChanged -> {
                _state.update { currentState ->
                    currentState.copy(
                        groupFormState = currentState.groupFormState.copy(
                            name = event.name
                        )
                    )
                }
            }

            is GroupsEvent.ShowEditGroupModal -> {
                val group = _state.value.groups.find { it.id == event.groupId }
                if (group != null) {
                    _state.update { currentState ->
                        currentState.copy(
                            editingGroupId = event.groupId,
                            isAddGroupDialogVisible = false,
                            groupFormState = GroupFormState(
                                name = TextFieldValue(
                                    text = group.name,
                                    selection = TextRange(group.name.length)
                                )
                            )
                        )
                    }
                }
            }

            GroupsEvent.SubmitAddGroupForm -> {
                viewModelScope.launch {
                    _state.update { it.copy(groupFormState = it.groupFormState.copy(isSubmitting = true)) }
                    val currentState = _state.value
                    val name = currentState.groupFormState.name.text.trim()

                    if (name.isEmpty()) {
                        _state.update {
                            it.copy(
                                groupFormState = it.groupFormState.copy(
                                    isSubmitting = false,
                                    nameError = "Name cannot be empty"
                                )
                            )
                        }
                        return@launch
                    }

                    createGroupUseCase(name)
                    _state.update {
                        it.copy(
                            groupFormState = GroupFormState(isSuccess = true, isSubmitting = false),
                        )
                    }
                }
            }

            is GroupsEvent.SubmitEditGroupForm -> {
                viewModelScope.launch {
                    _state.update { it.copy(groupFormState = it.groupFormState.copy(isSubmitting = true)) }
                    val currentState = _state.value
                    val name = currentState.groupFormState.name.text.trim()

                    if (name.isEmpty()) {
                        _state.update {
                            it.copy(
                                groupFormState = it.groupFormState.copy(
                                    isSubmitting = false,
                                    nameError = "Name cannot be empty"
                                )
                            )
                        }
                        return@launch
                    }

                    updateGroupUseCase(event.groupId, name)
                    _state.update {
                        it.copy(
                            groupFormState = GroupFormState(
                                isSuccess = true,
                                isSubmitting = false
                            ),
                        )
                    }
                }
            }

            GroupsEvent.ShowAddGroupModal -> {
                _state.update {
                    it.copy(
                        isAddGroupDialogVisible = true,
                        editingGroupId = null,
                        groupFormState = GroupFormState()
                    )
                }
            }

            is GroupsEvent.DeleteGroup -> {
                viewModelScope.launch {
                    deleteGroupUseCase(event.groupId)
                }
            }

            is GroupsEvent.MoveGroup -> {
                viewModelScope.launch {
                    moveGroupUseCase(event.fromIndex, event.toIndex)
                }
            }
        }
    }
}