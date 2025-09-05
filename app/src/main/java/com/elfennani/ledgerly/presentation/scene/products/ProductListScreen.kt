package com.elfennani.ledgerly.presentation.scene.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Product
import com.elfennani.ledgerly.presentation.component.DefaultLoadingScreen
import com.elfennani.ledgerly.presentation.scene.product_form.ProductFormRoute
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.plus
import com.elfennani.ledgerly.presentation.utils.pretty
import java.util.Locale

@Composable
fun ProductListScreen(
    navController: NavController,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    ProductListScreen(
        state = state,
        onNavigateToNewProduct = {
            navController.navigate(ProductFormRoute())
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductListScreen(
    state: ProductListUiState,
    onNavigateToNewProduct: () -> Unit = { },
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Products") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToNewProduct() }) {
                Icon(painterResource(R.drawable.plus), null)
            }
        }
    ) { innerPadding ->
        if (state.isLoading) {
            DefaultLoadingScreen(contentPadding = innerPadding)
        } else {
            LazyColumn(
                modifier = Modifier.consumeWindowInsets(innerPadding),
                contentPadding = innerPadding + PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.products) { product ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceContainer,
                                MaterialTheme.shapes.medium
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        Text(product.name, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "${product.type.capitalize(Locale.getDefault())} • $${product.pricePerUnit.pretty} per ${product.defaultUnit ?: "unit"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductListScreenPreview() {
    AppTheme {
        ProductListScreen(
            state = ProductListUiState(
                products = listOf(
                    Product(
                        id = 1,
                        name = "Apple",
                        description = "Fresh red apples",
                        pricePerUnit = 0.5,
                        defaultUnit = "piece",
                        type = "food"
                    ),
                    Product(
                        id = 2,
                        name = "Banana",
                        description = "Ripe yellow bananas",
                        pricePerUnit = 0.3,
                        defaultUnit = "piece",
                        type = "food"
                    ),
                    Product(
                        id = 3,
                        name = "Shampoo",
                        description = "Hair care shampoo",
                        pricePerUnit = 5.0,
                        defaultUnit = "bottle",
                        type = "personal care"
                    )
                )
            )
        )
    }
}