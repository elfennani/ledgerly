package com.elfennani.ledgerly.presentation.scene.product_form

import androidx.compose.ui.text.input.TextFieldValue

data class ProductFormUiState(
    val isLoading: Boolean = false,
    val isUpdatingMode: Boolean = false,

    val name: TextFieldValue = TextFieldValue(""),
    val description: TextFieldValue = TextFieldValue(""),
    val unitPrice: TextFieldValue = TextFieldValue(""),
    val unit: TextFieldValue = TextFieldValue(""),
    val type: TextFieldValue = TextFieldValue(""),

    val nameError: String? = null,
    val descriptionError: String? = null,
    val unitPriceError: String? = null,
    val unitError: String? = null,
    val typeError: String? = null,

    val isSuccess: Boolean = false,
    val isSubmitting: Boolean = false,
)
