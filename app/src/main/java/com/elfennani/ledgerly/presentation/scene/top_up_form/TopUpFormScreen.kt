package com.elfennani.ledgerly.presentation.scene.top_up_form

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.presentation.component.DatePickerModal
import com.elfennani.ledgerly.presentation.scene.transaction_form.component.SelectAccountModal
import com.elfennani.ledgerly.presentation.scene.transaction_form.readable
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.clickableTextField
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun TopUpFormScreen(
    navController: NavController,
    viewModel: TopUpFormViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    TopUpFormScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = navController::popBackStack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopUpFormScreen(
    state: TopUpFormUiState,
    onEvent: (TopUpFormEvent) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val accountModalState = rememberModalBottomSheetState()
    val amountFocus = remember { FocusRequester() }
    val titleFocus = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(
                it,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            onEvent(TopUpFormEvent.ClearError)
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            Toast.makeText(context, "Transaction saved", Toast.LENGTH_SHORT).show()
            onBack()
        }
    }

    LaunchedEffect(Unit) {
        amountFocus.requestFocus()
    }

    if (state.isDateModalOpen) {
        DatePickerModal(
            initialSelectedDate = state.date,
            onDateSelected = {
                if (it != null)
                    onEvent(TopUpFormEvent.OnDateChange(Instant.ofEpochMilli(it)))
            },
            onDismiss = { onEvent(TopUpFormEvent.OnCloseDateModal) }
        )
    }

    if (state.isSelectAccountModalOpen) {
        SelectAccountModal(
            sheetState = accountModalState,
            onDismissRequest = {
                scope.launch { accountModalState.hide() }
                    .invokeOnCompletion { onEvent(TopUpFormEvent.OnCloseSelectAccountModal) }
            },
            onClickAccount = { account -> onEvent(TopUpFormEvent.OnSelectAccount(account)) },
            accounts = state.accounts
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Top Up") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(R.drawable.arrow_left), null)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onEvent(TopUpFormEvent.OnSubmit) },
                text = { Text("Save") },
                icon = { Icon(painterResource(R.drawable.check), null) }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Total Amount", style = MaterialTheme.typography.titleSmall)
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(amountFocus),
                    value = state.total,
                    onValueChange = { onEvent(TopUpFormEvent.OnTotalChange(it)) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displayMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                    ),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { titleFocus.requestFocus() }),
                    decorationBox = {
                        Row {
                            Text(
                                "$ ",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                modifier = Modifier.padding(top = 3.dp)
                            )
                            Box(
                                modifier = Modifier.weight(1f)
                            ) {
                                it()
                                if (state.total.text.isEmpty()) {
                                    Text(
                                        "0.00",
                                        style = MaterialTheme.typography.displayMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                                }
                            }
                        }
                    }
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(titleFocus),
                value = state.title,
                onValueChange = { onEvent(TopUpFormEvent.OnTitleChange(it)) },
                label = { Text("Title") }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableTextField(state.date) {
                        onEvent(TopUpFormEvent.OnOpenDateModal)
                    },
                value = state.date.readable(),
                onValueChange = { },
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
                        onEvent(TopUpFormEvent.OnOpenSelectAccountModal)
                    },
                value = state.account?.name ?: "Select Account",
                onValueChange = {},
                label = { Text("Account") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.chevron_up),
                        contentDescription = "Select account",
                        modifier = Modifier.rotate(180f)
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun TopUpFormScreenPreview() {
    AppTheme {
        TopUpFormScreen(
            state = TopUpFormUiState(
                total = TextFieldValue("")
            )
        )
    }
}