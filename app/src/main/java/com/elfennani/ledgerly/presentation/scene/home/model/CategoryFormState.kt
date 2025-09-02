package com.elfennani.ledgerly.presentation.scene.home.model

import androidx.compose.ui.text.input.TextFieldValue

data class CategoryFormState(
    // Fields
    val name: TextFieldValue = TextFieldValue(""),
    val target: TextFieldValue = TextFieldValue(""),
    val groupId: Int? = null,

    // Error
    val nameError: String? = null,
    val targetError: String? = null,
    val groupError: String? = null,

    // Status
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
)