package com.elfennani.ledgerly.presentation.scene.category

import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.elfennani.ledgerly.domain.usecase.DeleteCategoryUseCase
import com.elfennani.ledgerly.domain.usecase.GetBudgetDataUseCase
import com.elfennani.ledgerly.domain.usecase.GetCategoryUseCase
import com.elfennani.ledgerly.domain.usecase.SetCategoryBudgetUseCase
import com.elfennani.ledgerly.domain.usecase.SetCategoryTargetUseCase
import com.elfennani.ledgerly.domain.usecase.UpdateCategoryNameUseCase
import com.elfennani.ledgerly.presentation.scene.category.model.CategoryValueType
import com.elfennani.ledgerly.presentation.scene.category.model.EditNameForm
import com.elfennani.ledgerly.presentation.scene.category.model.EditValueForm
import com.elfennani.ledgerly.presentation.utils.pretty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategory: GetCategoryUseCase,
    private val setCategoryBudget: SetCategoryBudgetUseCase,
    private val setCategoryTarget: SetCategoryTargetUseCase,
    private val updateCategoryName: UpdateCategoryNameUseCase,
    private val deleteCategory: DeleteCategoryUseCase,
    private val getBudgetData: GetBudgetDataUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<CategoryRoute>()

    private val _state = MutableStateFlow(CategoryUiState(isLoading = true))
    val state = _state.asStateFlow()

    val now: Calendar = Calendar.getInstance()
    val month = now.get(Calendar.MONTH)
    val year = now.get(Calendar.YEAR)

    init {
        viewModelScope.launch {
            combine(
                getCategory(route.categoryId),
                getBudgetData(month, year)
            ) { groupCategory, budgetData ->
                if (groupCategory != null)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            category = groupCategory.second,
                            group = groupCategory.first,
                            budgetData = budgetData
                        )
                    }
            }.collect {}
        }
    }

    fun onEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.ShowEditCategoryValueModal -> {
                _state.update { state ->
                    state.copy(
                        valueEditModalType = event.valueType,
                        valueEditForm = EditValueForm(
                            monthIndex = event.monthIndex,
                            year = event.year,
                            valueType = event.valueType,
                            initialValue = state.category?.budgets?.find { it.month == event.monthIndex && it.year == event.year }
                                ?.let {
                                    when (event.valueType) {
                                        CategoryValueType.BUDGET -> it.budget
                                        CategoryValueType.TARGET -> it.target
                                    }
                                } ?: 0.0,
                            value = state.category?.budgets?.find { it.month == event.monthIndex && it.year == event.year }
                                ?.let {
                                    when (event.valueType) {
                                        CategoryValueType.BUDGET -> it.budget
                                        CategoryValueType.TARGET -> it.target
                                    }
                                }.let {
                                    TextFieldValue(
                                        it?.pretty ?: "",
                                        selection = TextRange(index = it?.pretty?.length ?: 0)
                                    )
                                },
                            valueError = null,
                            isSubmitting = false,
                            isSuccess = false
                        ),
                    )
                }
            }

            is CategoryEvent.ShowEditCategoryNameModal -> {
                _state.update {
                    it.copy(
                        isNameEditModalVisible = true, nameEditForm = EditNameForm(
                            name = TextFieldValue(
                                text = _state.value.category?.name ?: "",
                                selection = TextRange(
                                    index = _state.value.category?.name?.length ?: 0
                                )
                            ),
                        )
                    )
                }
            }

            is CategoryEvent.DismissModal -> {
                _state.value = _state.value.copy(
                    valueEditModalType = null,
                    isNameEditModalVisible = false,
                    isDeleteConfirmationVisible = false,
                    valueEditForm = EditValueForm(),
                    nameEditForm = EditNameForm()
                )
            }

            is CategoryEvent.SetValue -> {
                _state.update {
                    if (event.value.text.toDoubleOrNull() == null) {
                        return@update it.copy(
                            valueEditForm = _state.value.valueEditForm.copy(
                                value = event.value,
                                valueError = "Invalid number"
                            )
                        )
                    }

                    it.copy(
                        valueEditForm = _state.value.valueEditForm.copy(
                            value = event.value,
                            valueError = null
                        )
                    )
                }
            }

            is CategoryEvent.OnNameChanged -> {
                _state.update {
                    it.copy(
                        nameEditForm = it.nameEditForm.copy(
                            name = event.name,
                            error = if (event.name.text.isBlank()) "Name cannot be empty" else null
                        )
                    )
                }
            }

            is CategoryEvent.DeleteCategory -> {
                _state.update {
                    it.copy(
                        isDeleteConfirmationVisible = true
                    )
                }
            }

            is CategoryEvent.ConfirmDeleteCategory -> {
                viewModelScope.launch {
                    _state.value.category?.let {
                        _state.update { state ->
                            state.copy(
                                isDeleteConfirmationVisible = false,
                                isLoading = true
                            )
                        }
                        Log.d("CategoryViewModel", "Deleting category: ${it.id}")
                        deleteCategory(it.id)
                        Log.d("CategoryViewModel", "Deleted category: ${it.id}")
                        _state.update { state ->
                            state.copy(
                                isDeleteSuccess = true
                            )
                        }
                    }
                }
            }

            is CategoryEvent.DismissDeleteCategoryDialog -> {
                _state.update {
                    it.copy(
                        isDeleteConfirmationVisible = false
                    )
                }
            }

            CategoryEvent.SubmitNameChange -> {
                val name = _state.value.nameEditForm.name.text

                _state.update {
                    it.copy(
                        nameEditForm = it.nameEditForm.copy(
                            isSubmitting = true,
                            isSuccess = false,
                            error = null
                        )
                    )
                }

                if (name.isBlank()) {
                    _state.update {
                        it.copy(
                            nameEditForm = it.nameEditForm.copy(
                                error = "Name cannot be empty"
                            )
                        )
                    }
                    return
                }

                viewModelScope.launch {
                    _state.value.category?.let {
                        updateCategoryName(it.id, name)
                        _state.value = _state.value.copy(
                            nameEditForm = _state.value.nameEditForm.copy(
                                isSubmitting = false,
                                isSuccess = true,
                                error = null
                            )
                        )
                    }
                }
            }

            CategoryEvent.SubmitValueChange -> {
                val value = _state.value.valueEditForm.value.text.toDoubleOrNull()
                _state.update {
                    it.copy(
                        valueEditForm = it.valueEditForm.copy(
                            isSubmitting = true,
                            isSuccess = false,
                            valueError = null
                        )
                    )
                }
                if (value == null) {
                    _state.update {
                        it.copy(
                            valueEditForm = _state.value.valueEditForm.copy(
                                valueError = "Invalid number",
                                isSubmitting = false
                            )
                        )
                    }
                    return
                } else if (value < 0) {
                    _state.update {
                        it.copy(
                            valueEditForm = _state.value.valueEditForm.copy(
                                valueError = "Value cannot be negative",
                                isSubmitting = false
                            )
                        )
                    }
                    return
                }

                val category = _state.value.category ?: return
                val valueType = _state.value.valueEditModalType ?: return

                viewModelScope.launch {
                    when (valueType) {
                        CategoryValueType.BUDGET -> {
                            Log.d("CategoryViewModel", "Setting budget: $value")
                            setCategoryBudget(
                                categoryId = category.id,
                                month = state.value.valueEditForm.monthIndex,
                                year = state.value.valueEditForm.year,
                                budget = value
                            )
                        }

                        CategoryValueType.TARGET -> {
                            Log.d("CategoryViewModel", "Setting target: $value")
                            setCategoryTarget(
                                categoryId = category.id,
                                month = state.value.valueEditForm.monthIndex,
                                year = state.value.valueEditForm.year,
                                target = value
                            )
                        }
                    }

                    Log.d("CategoryViewModel", "Set value successfully")

                    _state.update {
                        it.copy(
                            valueEditForm = it.valueEditForm.copy(
                                isSubmitting = false,
                                isSuccess = true
                            )
                        )
                    }
                }
            }
        }
    }
}