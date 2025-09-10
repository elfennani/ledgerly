package com.elfennani.ledgerly.presentation.scene.transactions

import com.elfennani.ledgerly.domain.model.Transaction

data class TransactionListUiState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList()
)
