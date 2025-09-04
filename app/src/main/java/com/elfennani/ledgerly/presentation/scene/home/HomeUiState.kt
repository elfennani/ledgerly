package com.elfennani.ledgerly.presentation.scene.home

import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.domain.model.BudgetData
import com.elfennani.ledgerly.domain.model.Group
import com.elfennani.ledgerly.presentation.scene.home.model.AccountFormState
import com.elfennani.ledgerly.presentation.scene.home.model.CategoryFormState

data class HomeUiState(
    val isLoading: Boolean = false,
    val accounts: List<Account> = emptyList(),
    val groups: List<Group> = emptyList(),
    val selectedAccount: Int? = null,
    val toDeleteAccount: Int? = null,
    val editingAccount: Int? = null,
    val isCreateAccountModalVisible: Boolean = false,
    val createCategoryGroupId: Int? = null,
    val accountFormState: AccountFormState = AccountFormState(),
    val categoryFormState: CategoryFormState = CategoryFormState(),
    val budgetData: BudgetData = BudgetData()
)
