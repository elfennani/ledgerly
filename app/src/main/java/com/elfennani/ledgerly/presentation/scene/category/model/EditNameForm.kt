package com.elfennani.ledgerly.presentation.scene.category.model

import androidx.compose.ui.text.input.TextFieldValue

data class EditNameForm(
    val name: TextFieldValue = TextFieldValue(""),
    val error: String? = null,

    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
)
