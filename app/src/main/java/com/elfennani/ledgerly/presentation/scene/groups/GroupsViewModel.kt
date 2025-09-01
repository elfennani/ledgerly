package com.elfennani.ledgerly.presentation.scene.groups

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GroupsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(GroupsUiState())
    val state = _state.asStateFlow()

}