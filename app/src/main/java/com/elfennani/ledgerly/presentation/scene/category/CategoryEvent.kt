package com.elfennani.ledgerly.presentation.scene.category

import androidx.compose.ui.text.input.TextFieldValue
import com.elfennani.ledgerly.presentation.scene.category.model.CategoryValueType

sealed class CategoryEvent {
    data class ShowEditCategoryValueModal(
        val valueType: CategoryValueType,
        val monthIndex: Int,
        val year: Int
    ) : CategoryEvent()

    data object ShowEditCategoryNameModal : CategoryEvent()
    object DismissModal : CategoryEvent()

    data class SetValue(val value: TextFieldValue) : CategoryEvent()
    data class OnNameChanged(val name: TextFieldValue) : CategoryEvent()

    data object DeleteCategory : CategoryEvent()
    data object ConfirmDeleteCategory : CategoryEvent()
    data object SubmitValueChange : CategoryEvent()
    data object SubmitNameChange : CategoryEvent()
}