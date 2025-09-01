package com.elfennani.ledgerly.presentation.scene.home

sealed class HomeEvent {
    // Modal Events
    data object ShowCreateAccountModal : HomeEvent()
    data object DismissCreateAccountModal : HomeEvent()
    data class ShowAccountDetailsModal(val accountId: Int) : HomeEvent()
    data object DismissAccountDetailsModal : HomeEvent()

    // Form Events
    data class OnAccountNameChange(val name: String) : HomeEvent()
    data class OnInitialBalanceChange(val balance: String) : HomeEvent()
    data class OnAccountDescriptionChange(val description: String) : HomeEvent()
    data object SubmitCreateAccount : HomeEvent()
}