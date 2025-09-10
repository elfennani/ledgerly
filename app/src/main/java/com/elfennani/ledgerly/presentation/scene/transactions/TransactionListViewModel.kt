package com.elfennani.ledgerly.presentation.scene.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.ledgerly.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionListUiState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getTransactionsUseCase().collect { transactions ->
                _state.update { it.copy(transactions = transactions, isLoading = false) }
            }
        }
    }

}