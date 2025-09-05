package com.elfennani.ledgerly.presentation.scene.product_form

import androidx.compose.ui.text.input.TextFieldValue

sealed class ProductFormEvent {
    data class NameChanged(val name: TextFieldValue) : ProductFormEvent()
    data class DescriptionChanged(val description: TextFieldValue) : ProductFormEvent()
    data class UnitPriceChanged(val unitPrice: TextFieldValue) : ProductFormEvent()
    data class UnitChanged(val unit: TextFieldValue) : ProductFormEvent()
    data class TypeChanged(val type: TextFieldValue) : ProductFormEvent()

    object Submit : ProductFormEvent()
}