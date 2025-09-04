package com.elfennani.ledgerly.presentation.scene.category

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.model.CategoryBudget
import com.elfennani.ledgerly.domain.model.Group
import com.elfennani.ledgerly.presentation.component.ActionCard
import com.elfennani.ledgerly.presentation.scene.category.component.DeleteCategoryDialog
import com.elfennani.ledgerly.presentation.scene.category.component.EditCategoryModal
import com.elfennani.ledgerly.presentation.scene.category.component.EditValueModal
import com.elfennani.ledgerly.presentation.scene.category.model.CategoryValueType
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.excludeFirstEmoji
import com.elfennani.ledgerly.presentation.utils.firstEmojiOrNull
import com.elfennani.ledgerly.presentation.utils.pretty
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun CategoryScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    CategoryScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = navController::popBackStack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryScreen(
    state: CategoryUiState,
    onEvent: (CategoryEvent) -> Unit = {},
    onBack: () -> Unit = {},
) {
    val nameEditSheet = rememberModalBottomSheetState(true)
    val valueEditSheet = rememberModalBottomSheetState(true)
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.isDeleteSuccess) {
        Log.d("CategoryScreen", "isDeleteSuccess: ${state.isDeleteSuccess}")
        if (state.isDeleteSuccess) {
            onBack()
        }
    }

    if (state.isNameEditModalVisible) {
        EditCategoryModal(
            formState = state.nameEditForm,
            sheetState = nameEditSheet,
            onEvent = onEvent,
            onDismissRequest = {
                scope.launch {
                    keyboardController?.hide()
                    nameEditSheet.hide()
                }.invokeOnCompletion {
                    if (!nameEditSheet.isVisible) {
                        onEvent(CategoryEvent.DismissModal)
                    }
                }
            }
        )
    }

    if (state.valueEditModalType != null) {
        EditValueModal(
            formState = state.valueEditForm,
            sheetState = valueEditSheet,
            onEvent = onEvent,
            budgetData = state.budgetData,
            onDismissRequest = {
                scope.launch {
                    keyboardController?.hide()
                    valueEditSheet.hide()
                }.invokeOnCompletion {
                    if (!valueEditSheet.isVisible) {
                        onEvent(CategoryEvent.DismissModal)
                    }
                }
            }
        )
    }

    if (state.isDeleteConfirmationVisible) {
        DeleteCategoryDialog(state.category!!) {
            onEvent(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(painterResource(R.drawable.arrow_left), null)
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(CategoryEvent.DeleteCategory) }) {
                        Icon(
                            painterResource(R.drawable.trash),
                            null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val now = remember { Calendar.getInstance() }
        val month = now.get(Calendar.MONTH)
        val year = now.get(Calendar.YEAR)
        val budgetForMonth by remember(month, year, state.category) {
            derivedStateOf {
                state.category!!.budgets.firstOrNull { it.month == month && it.year == year }
            }
        }
        val primary = MaterialTheme.colorScheme.tertiary

        if (state.isLoading || state.category == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(0.2f))
                            .padding(8.dp)
                            .zIndex(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        val icon = state.category.name.firstEmojiOrNull()

                        if (icon != null) {
                            Text(
                                text = icon,
                                fontSize = 24.sp,
                                lineHeight = 24.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.baseline_inventory_24),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = state.category.name.excludeFirstEmoji(),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier,
                            lineHeight = 28.sp
                        )
                        Text(
                            state.group!!.name,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }


                    IconButton(onClick = {
                        onEvent(CategoryEvent.ShowEditCategoryNameModal)
                    }) {
                        Icon(painterResource(R.drawable.pencil_square), null)
                    }
                }

                Spacer(Modifier)

                Column(
                    modifier = Modifier,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Spending",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp
                        )
                        Text(
                            "Sep 2025",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    LinearProgressIndicator(
                        progress = {
                            if (budgetForMonth?.target == null)
                                0f
                            else {
                                12f / budgetForMonth!!.target!!.toFloat()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        drawStopIndicator = {
                            if (budgetForMonth?.target != null && budgetForMonth?.budget != null && budgetForMonth!!.budget!! < budgetForMonth!!.target!!) {
                                val adjustedStopSize = 4.dp.toPx()
                                val stopOffset =
                                    (size.height - adjustedStopSize) / 2 // Offset from end
                                val percentage =
                                    budgetForMonth!!.budget!!.toFloat() / budgetForMonth!!.target!!.toFloat()
                                drawCircle(
                                    color = primary,
                                    radius = adjustedStopSize / 2f,
                                    center =
                                        Offset(
                                            x = (size.width * percentage) - (adjustedStopSize / 2f) - stopOffset,
                                            y = size.height / 2f
                                        )
                                )
                            }
                        }
                    )

                    Text(
                        when {
                            budgetForMonth?.target == null -> "No Target Set"
                            budgetForMonth?.budget == null -> "Spent $${12.00.pretty} of $${budgetForMonth!!.target!!.pretty}"
                            else -> "Remaining $${(budgetForMonth!!.budget!! - 12).pretty} of $${budgetForMonth!!.budget!!.pretty} budget • $${12.00.pretty} of $${budgetForMonth!!.target!!.pretty} target"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        textAlign = TextAlign.End
                    )
                }

                Spacer(Modifier)

                if (budgetForMonth?.target == null) {
                    ActionCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onEvent(
                                CategoryEvent.ShowEditCategoryValueModal(
                                    CategoryValueType.TARGET,
                                    month,
                                    year
                                )
                            )
                        },
                        icon = { Icon(painterResource(R.drawable.flag), null) },
                        title = { Text("Set Target") },
                        description = {
                            Text("Define how much you want to spend in this category to track you progress.")
                        }
                    )
                } else {
                    ActionCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onEvent(
                                CategoryEvent.ShowEditCategoryValueModal(
                                    CategoryValueType.TARGET,
                                    month,
                                    year
                                )
                            )
                        },
                        icon = { Icon(painterResource(R.drawable.flag), null) },
                        trailing = {
                            Icon(
                                painterResource(R.drawable.pencil_square),
                                null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.75f)
                            )
                        },
                        title = {
                            Text(
                                "Current Target",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        description = {
                            Text(
                                text = "$${budgetForMonth!!.target!!.pretty}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    )
                }

                if (budgetForMonth?.budget == null) {
                    ActionCard(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = budgetForMonth?.target != null,
                        onClick = {
                            onEvent(
                                CategoryEvent.ShowEditCategoryValueModal(
                                    CategoryValueType.BUDGET,
                                    month,
                                    year
                                )
                            )
                        },
                        icon = { Icon(painterResource(R.drawable.banknotes_filled), null) },
                        title = { Text("Set Budget") },
                        description = {
                            Text("Allocate the money you have now to this category so you can control you spending.")
                        }
                    )
                } else {
                    ActionCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onEvent(
                                CategoryEvent.ShowEditCategoryValueModal(
                                    CategoryValueType.BUDGET,
                                    month,
                                    year
                                )
                            )
                        },
                        icon = { Icon(painterResource(R.drawable.banknotes_filled), null) },
                        trailing = {
                            Icon(
                                painterResource(R.drawable.pencil_square),
                                null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                            )
                        },
                        title = {
                            Text(
                                "Current Budget",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        description = {
                            Text(
                                text = "$${budgetForMonth!!.budget!!.pretty}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    )

                }
            }
        }
    }
}

@Preview
@Composable
private fun CategoryScreenPreview(budgets: List<CategoryBudget> = emptyList()) {
    AppTheme {
        CategoryScreen(
            state =
                CategoryUiState(
                    isLoading = false,
                    category = Category(
                        id = 1,
                        name = "🧾 Sample Category",
                        groupId = 1,
                        budgets = budgets
                    ),
                    group = Group(
                        id = 1,
                        name = "Sample Group",
                        index = 0
                    )
                )
        )
    }
}

@Preview
@Composable
private fun CategoryScreenWithBudgetPreview() {
    CategoryScreenPreview(
        listOf(
            CategoryBudget(
                categoryId = 1,
                month = 8,
                year = 2025,
                target = 100.00,
                budget = 80.00
            )
        )
    )
}