package com.elfennani.ledgerly.presentation.scene.product_form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.presentation.component.DefaultLoadingScreen
import com.elfennani.ledgerly.presentation.theme.AppTheme

@Composable
fun ProductFormScreen(
    navController: NavController,
    viewModel: ProductFormViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    ProductFormScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = navController::popBackStack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun ProductFormScreen(
    state: ProductFormUiState,
    onEvent: (ProductFormEvent) -> Unit = {},
    onBack: () -> Unit = { }
) {
    val nameFocus = remember { FocusRequester() }
    val unitPriceFocus = remember { FocusRequester() }
    val unitFocus = remember { FocusRequester() }
    val descriptionFocus = remember { FocusRequester() }
    val typeFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        nameFocus.requestFocus()
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isUpdatingMode) "Updating Product Details" else "New Product") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(R.drawable.arrow_left), null)
                    }
                },
            )
        },
        floatingActionButton = {
            if (!state.isLoading)
                ExtendedFloatingActionButton(
                    onClick = {
                        if (!state.isSubmitting)
                            onEvent(ProductFormEvent.Submit)
                    },
                    modifier = Modifier
                        .imePadding()
                ) {
                    Icon(painterResource(R.drawable.check), null)
                    Text(
                        " Save",
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
        },
    ) { innerPadding ->
        if (state.isLoading) {
            DefaultLoadingScreen(contentPadding = innerPadding)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .padding(16.dp)
                    .padding(bottom = 64.dp)
                    .imePadding()
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(nameFocus),
                    value = state.name,
                    onValueChange = {
                        onEvent(ProductFormEvent.NameChanged(it))
                    },
                    placeholder = { Text("e.g. Coca-Cola") },
                    label = { Text("Name") },
                    shape = MaterialTheme.shapes.small,
                    singleLine = true,
                    isError = !state.nameError.isNullOrEmpty(),
                    supportingText = {
                        if (!state.nameError.isNullOrEmpty())
                            Text(state.nameError)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { typeFocus.requestFocus() }
                    )
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(typeFocus),
                    value = state.type,
                    onValueChange = {
                        onEvent(ProductFormEvent.TypeChanged(it))
                    },
                    placeholder = { Text("e.g. Groceries") },
                    label = { Text("Type (optional)") },
                    shape = MaterialTheme.shapes.small,
                    singleLine = true,
                    isError = !state.typeError.isNullOrEmpty(),
                    supportingText = {
                        if (!state.typeError.isNullOrEmpty())
                            Text(state.typeError)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { unitPriceFocus.requestFocus() }
                    )
                )

                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Switch(
                        modifier = Modifier
                            .focusRequester(typeFocus),
                        checked = state.isPriceFixed,
                        onCheckedChange = {
                            onEvent(ProductFormEvent.ToggleIsPriceFixed)
                        }
                    )

                    Text("Is Price Fixed", style = MaterialTheme.typography.bodyLarge)
                }

                AnimatedVisibility(state.isPriceFixed) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(unitPriceFocus),
                            value = state.unitPrice,
                            onValueChange = {
                                onEvent(ProductFormEvent.UnitPriceChanged(it))
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { unitFocus.requestFocus() }
                            ),
                            trailingIcon = {
                                Icon(painterResource(R.drawable.currency_dollar), null)
                            },
                            label = { Text("Unit Price") },
                            placeholder = { Text("0.00") },
                            shape = MaterialTheme.shapes.small,
                            singleLine = true,
                            isError = !state.unitPriceError.isNullOrEmpty(),
                            supportingText = {
                                if (!state.unitPriceError.isNullOrEmpty())
                                    Text(state.unitPriceError)
                            }
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(unitFocus),
                            value = state.unit,
                            onValueChange = {
                                onEvent(ProductFormEvent.UnitChanged(it))
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { descriptionFocus.requestFocus() }
                            ),
                            label = { Text("Unit (optional)") },
                            placeholder = { Text("item") },
                            shape = MaterialTheme.shapes.small,
                            singleLine = true,
                            isError = !state.unitError.isNullOrEmpty(),
                            supportingText = {
                                if (!state.unitError.isNullOrEmpty())
                                    Text(state.unitError)
                            }
                        )
                    }
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp)
                        .focusRequester(descriptionFocus),
                    value = state.description,
                    onValueChange = {
                        onEvent(ProductFormEvent.DescriptionChanged(it))
                    },
                    label = { Text("Description") },
                    placeholder = { Text("e.g. A 12-pack of soda") },
                    shape = MaterialTheme.shapes.small,
                    isError = !state.descriptionError.isNullOrEmpty(),
                    supportingText = {
                        if (!state.descriptionError.isNullOrEmpty())
                            Text(state.descriptionError)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProductFormScreenPreview() {
    AppTheme {
        ProductFormScreen(state = ProductFormUiState())
    }
}