package com.elfennani.ledgerly.presentation.scene.home.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.presentation.scene.home.HomeEvent

@Composable
fun DeleteAccountDialog(
    account: Account,
    onEvent: (HomeEvent) -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(painterResource(R.drawable.trash), null)
        },
        title = {
            Text(text = "Delete Account '${account.name}'", textAlign = TextAlign.Center)
        },
        text = {
            Text(text = "Are you sure you want to delete this account? This action cannot be undone.")
        },
        onDismissRequest = {
            onEvent(HomeEvent.CancelDeleteAccount)
        },
        confirmButton = {
            Button(
                onClick = {
                    onEvent(HomeEvent.ConfirmDeleteAccount(account.id))
                },
                colors = ButtonDefaults.textButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onEvent(HomeEvent.CancelDeleteAccount)
                }
            ) {
                Text("Dismiss")
            }
        }
    )

}