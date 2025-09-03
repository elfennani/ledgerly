package com.elfennani.ledgerly.presentation.scene.category

import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.model.Group
import com.elfennani.ledgerly.presentation.scene.category.model.CategoryValueType
import com.elfennani.ledgerly.presentation.scene.category.model.EditNameForm
import com.elfennani.ledgerly.presentation.scene.category.model.EditValueForm

data class CategoryUiState(
    val isLoading: Boolean = false,
    val category: Category? = null,
    val group: Group? = null,

    val valueEditForm: EditValueForm = EditValueForm(),
    val valueEditModalType: CategoryValueType? = null,

    val nameEditForm: EditNameForm = EditNameForm(),
    val isNameEditModalVisible: Boolean = false,

    val isDeleteConfirmationVisible: Boolean = false,
)
