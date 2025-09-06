package com.elfennani.ledgerly.presentation.scene.product_form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.elfennani.ledgerly.domain.usecase.CreateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createProduct: CreateProductUseCase
) : ViewModel() {
    private val route = savedStateHandle.toRoute<ProductFormRoute>()

    private val _state = MutableStateFlow(ProductFormUiState(isLoading = route.productId != null))
    val state = _state.asStateFlow()

    fun onEvent(event: ProductFormEvent) {
        when (event) {
            is ProductFormEvent.DescriptionChanged -> {
                _state.update {
                    it.copy(
                        description = event.description,
                        descriptionError = null
                    )
                }
            }

            is ProductFormEvent.NameChanged -> {
                _state.update {
                    it.copy(
                        name = event.name,
                        nameError = null
                    )
                }
            }

            ProductFormEvent.Submit -> {
                viewModelScope.launch {
                    if (isErrored()) return@launch

                    _state.update { it.copy(isSubmitting = true) }

                    val currentState = state.value
                    val result = createProduct(
                        name = currentState.name.text,
                        description = currentState.description.text.takeIf {
                            it.isNotBlank()
                        },
                        pricePerUnit = currentState.unitPrice.text.takeIf {
                            it.isNotBlank()
                        }?.toDoubleOrNull(),
                        defaultUnit = currentState.unit.text.takeIf {
                            it.isNotBlank()
                        },
                        type = currentState.type.text.lowercase()
                    )

                    _state.update {
                        it.copy(
                            isSuccess = true,
                            isSubmitting = false
                        )
                    }
                }
            }

            is ProductFormEvent.TypeChanged -> {
                _state.update {
                    it.copy(
                        type = event.type,
                        typeError = null
                    )
                }
            }

            is ProductFormEvent.UnitChanged -> {
                _state.update {
                    it.copy(
                        unit = event.unit,
                        unitError = null
                    )
                }
            }

            is ProductFormEvent.UnitPriceChanged -> {
                _state.update {
                    it.copy(
                        unitPrice = event.unitPrice,
                        unitPriceError = null
                    )
                }
            }

            ProductFormEvent.ToggleIsPriceFixed -> {
                _state.update {
                    it.copy(isPriceFixed = !it.isPriceFixed)
                }
            }
        }
    }

    private fun isErrored(): Boolean {
        _state.update { currentState ->
            currentState.copy(
                nameError = if (currentState.name.text.isBlank()) "Name cannot be empty" else null,
                unitPriceError = when {
                    currentState.unitPrice.text.toDoubleOrNull() == null -> "Unit Price must be a valid number"
                    currentState.unitPrice.text.toDouble() < 0.0 -> "Unit Price cannot be negative"
                    else -> null
                },
                typeError = if (currentState.type.text.isBlank()) "Type cannot be empty" else null,
            )
        }

        return state.value.run {
            !nameError.isNullOrEmpty() ||
                    !unitPriceError.isNullOrEmpty() ||
                    !typeError.isNullOrEmpty()
        }
    }
}