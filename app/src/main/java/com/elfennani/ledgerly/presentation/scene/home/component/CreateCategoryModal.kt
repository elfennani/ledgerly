package com.elfennani.ledgerly.presentation.scene.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfennani.ledgerly.domain.model.Group
import com.elfennani.ledgerly.presentation.scene.home.HomeEvent
import com.elfennani.ledgerly.presentation.scene.home.model.CategoryFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCategoryModal(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    formState: CategoryFormState,
    groups: List<Group>,
    onEvent: (HomeEvent) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    val nameFocusRequester = remember { FocusRequester() }
    val group = groups.firstOrNull { it.id == formState.groupId }

    LaunchedEffect(Unit) {
        nameFocusRequester.requestFocus()
    }

    LaunchedEffect(formState.isSuccess) {
        if (formState.isSuccess)
            onEvent(HomeEvent.DismissCreateCategoryModal)
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
                "Create Category for ${group?.name}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(nameFocusRequester),
                value = formState.name,
                onValueChange = {
                    onEvent(HomeEvent.OnCategoryNameChange(it))
                },
                placeholder = { Text("e.g. Music") },
                label = { Text("Nickname") },
                shape = MaterialTheme.shapes.small,
                singleLine = true,
                isError = !formState.nameError.isNullOrEmpty(),
                supportingText = {
                    if (!formState.nameError.isNullOrEmpty())
                        Text(formState.nameError)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Words
                ),
            )

            Button(
                onClick = { onEvent(HomeEvent.SubmitCreateCategory) },
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