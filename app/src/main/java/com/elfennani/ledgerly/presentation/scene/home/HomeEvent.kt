package com.elfennani.ledgerly.presentation.scene.home

import androidx.compose.ui.text.input.TextFieldValue

sealed class HomeEvent {
    // Modal Events
    data object ShowCreateAccountModal : HomeEvent()
    data object DismissCreateAccountModal : HomeEvent()
    data class ShowAccountDetailsModal(val accountId: Int) : HomeEvent()
    data object DismissAccountDetailsModal : HomeEvent()
    data class ShowEditAccountModal(val accountId: Int) : HomeEvent()
    data object DismissEditAccountModal : HomeEvent()
    data class ShowCreateCategoryModal(val groupId: Int) : HomeEvent()
    data object DismissCreateCategoryModal : HomeEvent()

    // Form Events
    data class OnAccountNameChange(val name: String) : HomeEvent()
    data class OnInitialBalanceChange(val balance: String) : HomeEvent()
    data class OnAccountDescriptionChange(val description: String) : HomeEvent()
    data object SubmitCreateAccount : HomeEvent()
    data object SubmitEditAccount : HomeEvent()
    data class OnCategoryNameChange(val name: TextFieldValue) : HomeEvent()
    data class OnCategoryTargetChange(val target: TextFieldValue) : HomeEvent()
    data object SubmitCreateCategory : HomeEvent()


    // Account Events
    data class DeleteAccount(val accountId: Int) : HomeEvent()
    data class ConfirmDeleteAccount(val accountId: Int) : HomeEvent()
    data object CancelDeleteAccount : HomeEvent()

    // Group Events
    data class ToggleGroupCollapsed(val groupId: Int) : HomeEvent()
}