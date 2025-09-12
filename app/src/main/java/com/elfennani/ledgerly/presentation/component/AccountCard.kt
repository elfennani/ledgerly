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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.domain.model.Transaction
import com.elfennani.ledgerly.presentation.scene.transaction_form.readable
import com.elfennani.ledgerly.presentation.scene.transactions.PreviewTransactions
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
            .background(MaterialTheme.colorScheme.surfaceContainer, MaterialTheme.shapes.large)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.wallet),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    account.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    "Balance",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 11.sp
                )
                Text(
                    text = "$${account.balance.pretty}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFeatureSettings = "tnum"
                    ),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            account.transactions.forEach { transaction ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        transaction.date.readable("dd / MM"),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFeatureSettings = "tnum"
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        transaction.title,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                    )
                    if (transaction is Transaction.Outflow)
                        Text(
                            "- $${transaction.amount.pretty}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFeatureSettings = "tnum"
                            ),
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.End
                        )
                    else {
                        Text(
                            "+ $${transaction.amount.pretty}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFeatureSettings = "tnum"
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.End
                        )
                    }
                }

            }
            if (account.transactions.size < 3)
                for (i in 0 until (3 - account.transactions.size))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.alpha(0.5f)
                    ) {
                        Text(
                            "‒‒ / ‒‒",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFeatureSettings = "ss01,tnum"
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            "--",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            "$ --",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFeatureSettings = "tnum"
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.End
                        )
                    }
        }
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
                    name = "Checking Account",
                    balance = 9876.00,
                    transactions = PreviewTransactions.transactionList.filter { it.account.id == 201 }
                        .subList(0, 2)
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