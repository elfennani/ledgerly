package com.elfennani.ledgerly.presentation.scene.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.presentation.scene.home.HomeEvent
import com.elfennani.ledgerly.presentation.scene.home.model.AccountFormState
import com.elfennani.ledgerly.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountModal(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    formState: AccountFormState,
    onEvent: (HomeEvent) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    val nameFocus = remember { FocusRequester() }
    val balanceFocus = remember { FocusRequester() }
    val descriptionFocus = remember { FocusRequester() }

    LaunchedEffect(formState.isSuccess) {
        if (formState.isSuccess) {
            onDismissRequest()
        }
    }

    LaunchedEffect(Unit) {
        nameFocus.requestFocus()
    }

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "Create Account",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(nameFocus),
                value = formState.name,
                onValueChange = {
                    onEvent(HomeEvent.OnAccountNameChange(it))
                },
                placeholder = { Text("e.g. My Savings") },
                label = { Text("Nickname") },
                shape = MaterialTheme.shapes.small,
                singleLine = true,
                isError = !formState.nameError.isNullOrEmpty(),
                supportingText = {
                    if (!formState.nameError.isNullOrEmpty())
                        Text(formState.nameError)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { balanceFocus.requestFocus() }
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(balanceFocus),
                value = formState.initialBalance,
                onValueChange = {
                    onEvent(HomeEvent.OnInitialBalanceChange(it))
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { descriptionFocus.requestFocus() }
                ),
                trailingIcon = {
                    Icon(painterResource(R.drawable.currency_dollar), null)
                },
                label = { Text("Initial Balance") },
                placeholder = { Text("0.00") },
                shape = MaterialTheme.shapes.small,
                singleLine = true,
                isError = !formState.initialBalanceError.isNullOrEmpty(),
                supportingText = {
                    if (!formState.initialBalanceError.isNullOrEmpty())
                        Text(formState.initialBalanceError)
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .focusRequester(descriptionFocus),
                value = formState.description,
                onValueChange = {
                    onEvent(HomeEvent.OnAccountDescriptionChange(it))
                },
                label = { Text("Description") },
                placeholder = { Text("e.g. My personal savings account") },
                shape = MaterialTheme.shapes.small,
                isError = !formState.descriptionError.isNullOrEmpty(),
                supportingText = {
                    if (!formState.descriptionError.isNullOrEmpty())
                        Text(formState.descriptionError)
                }
            )

            Button(
                onClick = {
                    onEvent(HomeEvent.SubmitCreateAccount)
                },
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CreateAccountModalPreview() {
    AppTheme {
        CreateAccountModal(
            formState = AccountFormState(),
            sheetState = rememberModalBottomSheetState(true),
            onDismissRequest = { }
        )
    }
}