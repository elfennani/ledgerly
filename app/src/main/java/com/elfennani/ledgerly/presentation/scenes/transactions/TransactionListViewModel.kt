package com.elfennani.ledgerly.presentation.scenes.transactions

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(TransactionListUiState())
    val state = _state.asStateFlow()

}