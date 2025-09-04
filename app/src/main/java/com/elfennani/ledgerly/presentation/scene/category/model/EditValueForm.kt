package com.elfennani.ledgerly.presentation.scene.category.model

import androidx.compose.ui.text.input.TextFieldValue

data class EditValueForm(
    val initialValue: Double = 0.0,
    val value: TextFieldValue = TextFieldValue(""),
    val monthIndex: Int = 0,
    val year: Int = 0,
    val valueType: CategoryValueType? = null,
    val valueError: String? = null,

    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
)
