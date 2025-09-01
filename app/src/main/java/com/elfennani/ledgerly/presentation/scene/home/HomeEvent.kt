package com.elfennani.ledgerly.presentation.scene.home

sealed class HomeEvent {
    // Modal Events
    data object ShowCreateAccountModal : HomeEvent()
    data object DismissCreateAccountModal : HomeEvent()
    data class ShowAccountDetailsModal(val accountId: Int) : HomeEvent()
    data object DismissAccountDetailsModal : HomeEvent()
    data class ShowEditAccountModal(val accountId: Int) : HomeEvent()
    data object DismissEditAccountModal : HomeEvent()

    // Form Events
    data class OnAccountNameChange(val name: String) : HomeEvent()
    data class OnInitialBalanceChange(val balance: String) : HomeEvent()
    data class OnAccountDescriptionChange(val description: String) : HomeEvent()
    data object SubmitCreateAccount : HomeEvent()
    data object SubmitEditAccount : HomeEvent()

    // Account Events
    data class DeleteAccount(val accountId: Int) : HomeEvent()
    data class ConfirmDeleteAccount(val accountId: Int) : HomeEvent()
    data object CancelDeleteAccount : HomeEvent()
}