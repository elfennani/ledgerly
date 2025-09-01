package com.elfennani.ledgerly.presentation.scene.home.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.elfennani.ledgerly.presentation.scene.home.HomeEvent
import com.elfennani.ledgerly.presentation.scene.home.model.AccountFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountModal(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    formState: AccountFormState,
    onEvent: (HomeEvent) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    CreateAccountModal(
        modifier = modifier,
        title = "Edit Account",
        sheetState = sheetState,
        formState = formState,
        onEvent = {
            when (it) {
                is HomeEvent.SubmitCreateAccount -> onEvent(HomeEvent.SubmitEditAccount)
                else -> onEvent(it)
            }
        },
        onDismissRequest = onDismissRequest
    )
}