package com.elfennani.ledgerly.presentation.scene.transaction_form.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elfennani.ledgerly.domain.model.Product
import com.elfennani.ledgerly.presentation.scene.transaction_form.TransactionFormEvent
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.pretty
import java.util.Locale

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectProductModal(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(false),
    onDismissRequest: () -> Unit = {},
    onEvent: (TransactionFormEvent) -> Unit = {},
    products: List<Product> = emptyList()
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "Select Product",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .clickable {
                                onDismissRequest()
                                onEvent(TransactionFormEvent.AddProduct(product))
                            }
                            .background(
                                MaterialTheme.colorScheme.surfaceContainer,
                                MaterialTheme.shapes.medium
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        Text(product.name, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = when {
                                product.pricePerUnit != null -> "${product.type.capitalize(Locale.getDefault())} • $${product.pricePerUnit.pretty} per ${product.defaultUnit ?: "unit"}"
                                else -> product.type.capitalize(Locale.getDefault())
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AccountDetailsModalPrev() {
    AppTheme {
        SelectProductModal(
            products = listOf(
                Product(
                    id = 1,
                    name = "Milk",
                    description = "Whole milk",
                    type = "Dairy",
                    defaultUnit = "Liter",
                    pricePerUnit = 1.50
                ),
                Product(
                    id = 2,
                    name = "Bread",
                    description = "Wheat bread",
                    type = "Bakery",
                    defaultUnit = "Loaf",
                    pricePerUnit = 2.25
                ),
                Product(
                    id = 3,
                    name = "Eggs",
                    description = "Dozen eggs",
                    type = "Protein",
                    defaultUnit = "Dozen",
                    pricePerUnit = 3.00
                ),
                Product(
                    id = 4,
                    name = "Apples",
                    description = "Granny Smith",
                    type = "Fruit",
                    defaultUnit = "Kg",
                    pricePerUnit = 2.00
                )
            )
        )
    }
}