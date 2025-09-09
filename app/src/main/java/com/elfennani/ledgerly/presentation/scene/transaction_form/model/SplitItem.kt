package com.elfennani.ledgerly.presentation.scene.transaction_form.model

import androidx.compose.ui.text.input.TextFieldValue
import com.elfennani.ledgerly.domain.model.Product

data class SplitItem(
    val id: Int,
    val product: Product,
    val isByUnit: Boolean,
    val units: Int,
    val total: Double,
    val isNew: Boolean = true,
    val totalText: TextFieldValue = TextFieldValue("")
)
