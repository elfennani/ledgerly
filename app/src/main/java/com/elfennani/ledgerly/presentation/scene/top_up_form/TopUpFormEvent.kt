package com.elfennani.ledgerly.presentation.scene.top_up_form

import androidx.compose.ui.text.input.TextFieldValue
import com.elfennani.ledgerly.domain.model.Account
import java.time.Instant

sealed class TopUpFormEvent {
    data class OnTotalChange(val total: TextFieldValue) : TopUpFormEvent()
    data class OnTitleChange(val title: TextFieldValue) : TopUpFormEvent()
    data class OnDateChange(val date: Instant) : TopUpFormEvent()
    data class OnSelectAccount(val account: Account) : TopUpFormEvent()
    object OnOpenDateModal : TopUpFormEvent()
    object OnCloseDateModal : TopUpFormEvent()
    object OnOpenSelectAccountModal : TopUpFormEvent()
    object OnCloseSelectAccountModal : TopUpFormEvent()
    object OnSubmit : TopUpFormEvent()

    object ClearError : TopUpFormEvent()
}