package com.elfennani.ledgerly.presentation.scene.groups.model

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

data class GroupFormState(
    val name: TextFieldValue = TextFieldValue(
        text = "",
        selection = TextRange(0)
    ),
    val nameError: String? = null,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false
)
