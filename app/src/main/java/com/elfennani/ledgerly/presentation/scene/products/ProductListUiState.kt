package com.elfennani.ledgerly.presentation.scene.products

import com.elfennani.ledgerly.domain.model.Product

data class ProductListUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList()
)
