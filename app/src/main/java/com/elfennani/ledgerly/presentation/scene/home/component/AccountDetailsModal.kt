package com.elfennani.ledgerly.presentation.scene.home.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.presentation.scene.home.HomeEvent
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.pretty

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsModal(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(false),
    onDismissRequest: () -> Unit = {},
    onEvent: (HomeEvent) -> Unit = {},
    account: Account,
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                account.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            if (!account.description.isNullOrEmpty()) {
                Text("Description", style = MaterialTheme.typography.labelMedium)
                Text(
                    account.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            Column(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                val totalStyle =
                    MaterialTheme.typography.labelSmall.toSpanStyle().copy(color = Color.Gray)
                val currentBalanceStyle = MaterialTheme.typography.titleMedium.toSpanStyle()
                    .copy(color = MaterialTheme.colorScheme.primary)
                val balance by remember(account.balance) {
                    derivedStateOf {
                        buildAnnotatedString {
                            withStyle(currentBalanceStyle) {
                                append("$")
                                append(account.balance.pretty)
                            }
                            withStyle(style = totalStyle) {
                                append(
                                    " / $${account.balance.pretty}"
                                )
                            }
                        }
                    }
                }

                LinearProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp),
                    drawStopIndicator = {},
                )

                Text(
                    text = balance,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            Row {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onDismissRequest()
                        onEvent(HomeEvent.DeleteAccount(account.id))
                    },
                    colors = ButtonDefaults
                        .buttonColors()
                        .copy(containerColor = MaterialTheme.colorScheme.errorContainer),
                    contentPadding = PaddingValues(vertical = 18.dp)
                ) {
                    Icon(
                        painterResource(R.drawable.trash),
                        null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Delete",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onDismissRequest()
                        onEvent(HomeEvent.ShowEditAccountModal(account.id))
                    },
                    contentPadding = PaddingValues(vertical = 18.dp)
                ) {
                    Icon(
                        painterResource(R.drawable.pencil_square),
                        null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Edit")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AccountDetailsModalPrev() {
    AppTheme {
        AccountDetailsModal(
            account = Account(
                id = 1,
                name = "Checking",
                balance = 1234.56,
                description = "Main checking account"
            )
        )
    }
}