package com.elfennani.ledgerly.presentation.scene.category.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.BudgetData
import com.elfennani.ledgerly.presentation.component.BudgetStatus
import com.elfennani.ledgerly.presentation.scene.category.CategoryEvent
import com.elfennani.ledgerly.presentation.scene.category.model.CategoryValueType
import com.elfennani.ledgerly.presentation.scene.category.model.EditValueForm
import com.elfennani.ledgerly.presentation.utils.pretty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditValueModal(
    modifier: Modifier = Modifier,
    formState: EditValueForm,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    budgetData: BudgetData = BudgetData(),
    onEvent: (CategoryEvent) -> Unit = {},
    onDismissRequest: () -> Unit = { }
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(formState.isSuccess) {
        if (formState.isSuccess) {
            onDismissRequest()
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "Edit ${formState.valueType!!.name.lowercase()}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            if (formState.valueType == CategoryValueType.BUDGET) {
                BudgetStatus(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(budgetData.unused > 0.0) {
                            onEvent(
                                CategoryEvent.SetValue(
                                    TextFieldValue(
                                        (budgetData.unused + formState.initialValue).pretty,
                                        selection = TextRange(
                                            0,
                                            (budgetData.unused + formState.initialValue).pretty.length
                                        )
                                    )
                                )
                            )
                            focusRequester.requestFocus()
                        },
                    budgetData = budgetData
                )
                Spacer(Modifier.height(8.dp))
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = formState.value,
                onValueChange = {
                    onEvent(CategoryEvent.SetValue(it))
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal,
                ),
                trailingIcon = {
                    Icon(painterResource(R.drawable.currency_dollar), null)
                },
                label = { Text(formState.valueType.name) },
                placeholder = { Text("e.g. 100.00") },
                shape = MaterialTheme.shapes.small,
                singleLine = true,
                isError = !formState.valueError.isNullOrEmpty(),
                supportingText = {
                    if (!formState.valueError.isNullOrEmpty())
                        Text(formState.valueError)
                }
            )

            Button(
                onClick = { onEvent(CategoryEvent.SubmitValueChange) },
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = !formState.isSubmitting
            ) {
                if (formState.isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
                Text("Confirm", style = MaterialTheme.typography.labelLarge, fontSize = 16.sp)
            }
        }
    }
}