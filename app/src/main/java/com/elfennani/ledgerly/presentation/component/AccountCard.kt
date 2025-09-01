package com.elfennani.ledgerly.presentation.component

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.pretty

@SuppressLint("DefaultLocale")
@Composable
fun AccountCard(
    modifier: Modifier = Modifier,
    account: Account
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer, MaterialTheme.shapes.small)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
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

            Text(
                account.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = balance,
                style = MaterialTheme.typography.titleMedium
            )
        }

        LinearProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxWidth(),
            drawStopIndicator = {}
        )
    }
}

@Preview(
    name = "Light Mode",
)
@Composable
private fun AccountCardPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            AccountCard(
                account = Account(
                    id = 1,
                    name = "Checking Account with a very long name",
                    balance = 9876.00
                )
            )
        }
    }
}

@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AccountCardDarkPreview() {
    AccountCardPreview()
}