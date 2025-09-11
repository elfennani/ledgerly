package com.elfennani.ledgerly.presentation.scene.transaction_form

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.model.Product
import com.elfennani.ledgerly.presentation.component.DatePickerModal
import com.elfennani.ledgerly.presentation.scene.transaction_form.component.SelectAccountModal
import com.elfennani.ledgerly.presentation.scene.transaction_form.component.SelectCategoryModal
import com.elfennani.ledgerly.presentation.scene.transaction_form.component.SelectProductModal
import com.elfennani.ledgerly.presentation.scene.transaction_form.component.SplitItemCard
import com.elfennani.ledgerly.presentation.scene.transaction_form.model.SplitItem
import com.elfennani.ledgerly.presentation.scene.transaction_form.model.TransactionFormTab
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.clickableTextField
import com.elfennani.ledgerly.presentation.utils.excludeFirstEmoji
import com.elfennani.ledgerly.presentation.utils.firstEmojiOrNull
import com.elfennani.ledgerly.presentation.utils.plus
import com.elfennani.ledgerly.presentation.utils.pretty
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun TransactionFormScreen(
    navController: NavController,
    viewModel: TransactionFormViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    TransactionFormScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = navController::popBackStack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionFormScreen(
    state: TransactionFormUiState = TransactionFormUiState(),
    onEvent: (TransactionFormEvent) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val productModalState = rememberModalBottomSheetState()
    val accountModalState = rememberModalBottomSheetState()
    val categoryModalState = rememberModalBottomSheetState()
    val pagerState = rememberPagerState(
        if (state.tab == TransactionFormTab.SPLITS) 0 else 1,
        pageCount = { 2 }
    )
    val isDarkTheme = isSystemInDarkTheme()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(
                it,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            onEvent(TransactionFormEvent.ClearError)
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            Toast.makeText(context, "Transaction saved", Toast.LENGTH_SHORT).show()
            onBack()
        }
    }

    if (state.isDateModalOpen) {
        DatePickerModal(
            initialSelectedDate = state.date,
            onDateSelected = {
                if (it != null) onEvent(
                    TransactionFormEvent.SetDate(
                        Instant.ofEpochMilli(
                            it
                        )
                    )
                )
            },
            onDismiss = { onEvent(TransactionFormEvent.DismissDateModal) }
        )
    }

    if (state.isAddProductModalOpen) {
        SelectProductModal(
            sheetState = productModalState,
            onDismissRequest = {
                scope.launch { productModalState.hide() }
                    .invokeOnCompletion { onEvent(TransactionFormEvent.DismissAddProductModal) }
            },
            onEvent = onEvent,
            products = state.products
        )
    }

    if (state.isSelectAccountModalOpen) {
        SelectAccountModal(
            sheetState = accountModalState,
            onDismissRequest = {
                scope.launch { accountModalState.hide() }
                    .invokeOnCompletion { onEvent(TransactionFormEvent.DismissSelectAccountModal) }
            },
            onClickAccount = { account -> onEvent(TransactionFormEvent.SetAccount(account)) },
            accounts = state.accounts
        )
    }

    if (state.isSelectCategoryModalOpen) {
        SelectCategoryModal(
            sheetState = categoryModalState,
            onDismissRequest = {
                scope.launch { categoryModalState.hide() }
                    .invokeOnCompletion { onEvent(TransactionFormEvent.DismissSelectCategoryModal) }
            },
            onEvent = onEvent,
            categories = state.categories
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record Transaction") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (pagerState.currentPage == 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(painterResource(R.drawable.arrow_left), null)
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        HorizontalPager(
            pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) {
            if (it == 0) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .consumeWindowInsets(innerPadding),
                            contentPadding = innerPadding + PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.splits) { split ->
                                SplitItemCard(
                                    split = split,
                                    open = split.id == state.openSplitId,
                                    onEvent = onEvent
                                )
                            }
                        }

                        ExtendedFloatingActionButton(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 24.dp)
                                .align(Alignment.BottomEnd),
                            onClick = { onEvent(TransactionFormEvent.OpenAddProductModal) },
                            icon = { Icon(painterResource(R.drawable.plus), null) },
                            text = { Text("Add Product") }
                        )
                    }

                    Column {
                        if (isDarkTheme)
                            HorizontalDivider()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(if (isDarkTheme) 0.dp else 8.dp)
                                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total", style = MaterialTheme.typography.labelLarge)
                                Text(
                                    "$${state.total.pretty}",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                                contentPadding = PaddingValues(vertical = 12.dp)
                            ) {
                                Text("Next")
                                Spacer(Modifier.width(8.dp))
                                Icon(
                                    painterResource(R.drawable.arrow_left),
                                    null,
                                    modifier = Modifier
                                        .rotate(180f)
                                        .size(18.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column {
                            Text("Amount", style = MaterialTheme.typography.bodyLarge)
                            Text(
                                "$${state.total.pretty}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.title,
                            onValueChange = { onEvent(TransactionFormEvent.SetTitle(it)) },
                            label = { Text("Title") }
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickableTextField(state.date) {
                                    onEvent(TransactionFormEvent.OpenDateModal)
                                },
                            value = state.date.readable()!!,
                            onValueChange = {},
                            label = { Text("Date") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.calendar_days),
                                    contentDescription = "Select date"
                                )
                            }
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickableTextField(Unit) {
                                    onEvent(TransactionFormEvent.OpenSelectAccountModal)
                                },
                            value = state.account?.name ?: "Select Account",
                            onValueChange = {},
                            label = { Text("Account") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.chevron_up),
                                    contentDescription = "Select date",
                                    modifier = Modifier.rotate(180f)
                                )
                            }
                        )

                        AnimatedContent(
                            modifier = Modifier.fillMaxWidth(),
                            targetState = state.category
                        ) { category ->
                            if (category == null) {
                                Row(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .fillMaxWidth()
                                        .clip(CircleShape)
                                        .clickable { onEvent(TransactionFormEvent.OpenSelectCategoryModal) }
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 12.dp)
                                        .padding(end = 4.dp)
                                        .height(64.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(vertical = 12.dp)
                                            .clip(CircleShape)
                                            .fillMaxHeight()
                                            .aspectRatio(1f)
                                            .background(Color.Gray.copy(alpha = 0.12f))
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.pencil_square),
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = "Select Category",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(CircleShape)
                                        .clickable { onEvent(TransactionFormEvent.OpenSelectCategoryModal) }
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(horizontal = 12.dp)
                                        .padding(end = 4.dp)
                                        .height(64.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(vertical = 12.dp)
                                            .clip(CircleShape)
                                            .fillMaxHeight()
                                            .aspectRatio(1f)
                                            .background(Color.Gray.copy(alpha = 0.12f))
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        val icon = category.name.firstEmojiOrNull()

                                        if (icon != null) {
                                            Text(
                                                text = icon,
                                                fontSize = 14.sp,
                                                lineHeight = 14.sp,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                            )
                                        } else {
                                            Icon(
                                                painter = painterResource(R.drawable.baseline_inventory_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(bottom = 4.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = category.name.excludeFirstEmoji(),
                                                style = MaterialTheme.typography.titleMedium,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }

                                    Icon(
                                        painter = painterResource(R.drawable.pencil_square),
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                            }
                        }
                    }

                    ExtendedFloatingActionButton(
                        onClick = { onEvent(TransactionFormEvent.Save) },
                        icon = { Icon(painterResource(R.drawable.check), null) },
                        text = { Text("Save") },
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomEnd)
                    )
                }
            }
        }
    }
}

fun Instant.readable(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val formatted = LocalDateTime.ofInstant(this, ZoneId.systemDefault()).format(formatter)
    return formatted!!
}

fun Instant.readable(compact: Boolean = false): String {
    return if (compact) {
        readable("dd MMM")
    } else {
        readable("EE, dd/MM/yyyy")
    }
}

@Preview
@Composable
private fun TransactionFormScreenSplitsPreview() {
    AppTheme {
        TransactionFormScreen(
            state = readyToUseTransactionFormUiState
        )
    }
}

@Preview
@Composable
private fun TransactionFormScreenDetailsPreview() {
    AppTheme {
        TransactionFormScreen(
            state =
                readyToUseTransactionFormUiState.copy(
                    tab = TransactionFormTab.DETAILS,
                )
        )
    }
}

val readyToUseTransactionFormUiState = TransactionFormUiState(
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
    ),
    splits = listOf(
        SplitItem(
            id = 1,
            product = Product(
                id = 1,
                name = "Milk",
                description = "Whole milk",
                type = "Dairy",
                defaultUnit = "Liter",
                pricePerUnit = 1.50
            ),
            isByUnit = true,
            units = 2,
            total = 3.00,
            isNew = false
        ),
        SplitItem(
            id = 2,
            product = Product(
                id = 2,
                name = "Bread",
                description = "Wheat bread",
                type = "Bakery",
                defaultUnit = "Loaf",
                pricePerUnit = 2.25
            ),
            isByUnit = false,
            units = 1,
            total = 2.25,
            isNew = false,
            totalText = TextFieldValue("12.23")
        ),
        SplitItem(
            id = 3,
            product = Product(
                id = 3,
                name = "Eggs",
                description = "Dozen eggs",
                type = "Protein",
                defaultUnit = "Dozen",
                pricePerUnit = 3.00
            ),
            isByUnit = true,
            units = 1,
            total = 3.00,
            isNew = true
        )
    ),
    total = 8.25,
    title = TextFieldValue("Weekly Grocery Run"),
    category = Category(id = 101, groupId = 1, name = "Groceries"),
    date = Instant.now().minusSeconds(3600 * 24 * 3),
    account = Account(id = 201, name = "Checking Account", balance = 1250.75),
    openSplitId = 2, // 3 days ago
)