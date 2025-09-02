package com.elfennani.ledgerly.presentation.scene.groups.component

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
import com.elfennani.ledgerly.presentation.scene.groups.GroupsEvent
import com.elfennani.ledgerly.presentation.scene.groups.model.GroupFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGroupModal(
    modifier: Modifier = Modifier,
    groupId: Int,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    formState: GroupFormState = GroupFormState(),
    onEvent: (GroupsEvent) -> Unit = {},
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
                "Create Group",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = formState.name,
                onValueChange = { onEvent(GroupsEvent.OnGroupNameChanged(it)) },
                placeholder = { Text("e.g. Bills") },
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
                onClick = { onEvent(GroupsEvent.SubmitEditGroupForm(groupId)) },
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