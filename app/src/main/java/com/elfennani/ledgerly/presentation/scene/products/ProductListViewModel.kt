package com.elfennani.ledgerly.presentation.scene.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.ledgerly.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductListUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getProducts().collect { products ->
                _state.update { it.copy(products = products, isLoading = false) }
            }
        }
    }
}