package com.elfennani.ledgerly.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Transaction
import com.elfennani.ledgerly.presentation.scene.transaction_form.readable
import com.elfennani.ledgerly.presentation.scene.transactions.PreviewTransactions
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.pretty

@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    initialOpened: Boolean = false
) {
    val isTopUp =
        transaction is Transaction.Inflow
    var opened by rememberSaveable { mutableStateOf(initialOpened) }

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .clickable { opened = !opened }
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        if (isTopUp) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.errorContainer,
                        CircleShape
                    )
                    .padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (isTopUp) R.drawable.arrow_up_right else R.drawable.arrow_down_right
                    ),
                    contentDescription = null,
                    tint = if (isTopUp) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                var isTextOverflowing by remember { mutableStateOf(false) }
                AnimatedContent(
                    opened && isTextOverflowing,
                    transitionSpec = {
                        expandVertically(
                            expandFrom = Alignment.Top,
                            animationSpec = tween(220, delayMillis = 90)
                        ).togetherWith(
                            shrinkVertically(
                                shrinkTowards = Alignment.Top,
                                animationSpec = tween(
                                    220,
                                    delayMillis = 90
                                )
                            )
                        )
                    }
                ) { opened ->
                    Text(
                        transaction.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = if (opened) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = {
                            if (!isTextOverflowing && it.didOverflowHeight)
                                isTextOverflowing = true
                        }
                    )
                }
                Text(
                    text = when (transaction) {
                        is Transaction.Inflow -> "@${transaction.account.name}"
                        is Transaction.Outflow -> transaction.category.name
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(fontFeatureSettings = "calt"),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.7f
                    )
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    "${if (isTopUp) "+" else "-"}$${transaction.amount.pretty}",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isTopUp) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Text(
                    text = transaction.date.readable(compact = true)!!,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.7f
                    )
                )
            }
        }


        AnimatedVisibility(opened, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(start = 76.dp, end = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val color = DividerDefaults.color
                Canvas(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                ) {
                    drawLine(
                        color = color,
                        strokeWidth = 1.dp.toPx(),
                        start = Offset(0f, 1.dp.toPx() / 2),
                        end = Offset(size.width, 1.dp.toPx() / 2),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(10f, 10f),
                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Account",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            transaction.account.name,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Date",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            transaction.date.readable("EE, dd/MM"),
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                if (transaction is Transaction.Outflow) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Items Bought",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        transaction.splits.forEach { split ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    split.units.toString().padStart(2, '0'),
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontFeatureSettings = "tnum"
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                                Text(
                                    split.product.name, modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    "$${split.totalPrice.pretty}",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontFeatureSettings = "tnum"
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TransactionCardPreview() {
    AppTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            TransactionCard(
                transaction = PreviewTransactions.transactionList.find { it is Transaction.Outflow }!!,
                initialOpened = true
            )
        }
    }
}

@Preview
@Composable
private fun TransactionCardInFlowPreview() {
    AppTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            TransactionCard(
                transaction = (PreviewTransactions.transactionList.find { it is Transaction.Inflow }!! as Transaction.Inflow).copy(
                    title = "Morning Bread aenfie oimfoe oemfoem omfeomf"
                ),
                initialOpened = true
            )
        }
    }
}