package com.elfennani.ledgerly.presentation.scene.top_up_form

import androidx.compose.ui.text.input.TextFieldValue
import com.elfennani.ledgerly.domain.model.Account
import java.time.Instant

data class TopUpFormUiState(
    val isLoading: Boolean = false,
    val total: TextFieldValue = TextFieldValue(""),
    val title: TextFieldValue = TextFieldValue(""),
    val date: Instant = Instant.now(),
    val account: Account? = null,
    val error: String? = null,
    val isDateModalOpen: Boolean = false,
    val isSelectAccountModalOpen: Boolean = false,
    val accounts: List<Account> = emptyList(),
    val isSaving: Boolean = false,
    val isSuccess: Boolean = false
)
