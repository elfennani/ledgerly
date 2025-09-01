package com.elfennani.ledgerly.presentation.scene.home

import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.presentation.scene.home.model.AccountFormState

data class HomeUiState(
    val isLoading: Boolean = false,
    val accounts: List<Account> = emptyList(),
    val isCreateAccountModalVisible: Boolean = false,
    val formState: AccountFormState = AccountFormState()
)
