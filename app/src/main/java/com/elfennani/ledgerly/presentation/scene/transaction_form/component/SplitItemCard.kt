package com.elfennani.ledgerly.presentation.scene.transaction_form.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Product
import com.elfennani.ledgerly.presentation.scene.transaction_form.TransactionFormEvent
import com.elfennani.ledgerly.presentation.scene.transaction_form.model.SplitItem
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.pretty
import java.util.Locale

@Composable
fun SplitItemCard(
    modifier: Modifier = Modifier,
    split: SplitItem,
    open: Boolean = false,
    onEvent: (TransactionFormEvent) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable(!open) { onEvent(TransactionFormEvent.EditSplit(split.id)) }
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(split.product.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = when {
                        split.product.pricePerUnit != null -> "${
                            split.product.type.capitalize(
                                Locale.getDefault()
                            )
                        } • $${split.product.pricePerUnit.pretty} per ${split.product.defaultUnit ?: "unit"}"

                        else -> split.product.type.capitalize(Locale.getDefault())
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "$${split.total.pretty}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    painterResource(R.drawable.pencil_square),
                    null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        AnimatedVisibility(open) {
            Spacer(Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (split.product.pricePerUnit != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = split.isByUnit,
                            onClick = { onEvent(TransactionFormEvent.SetIsByUnit(split.id, true)) },
                            label = {
                                Text(
                                    "By Units",
                                    textAlign = TextAlign.Center,
                                )
                            },
                            leadingIcon = if (split.isByUnit) {
                                {
                                    Icon(
                                        painter = painterResource(R.drawable.check),
                                        contentDescription = "Done icon",
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else {
                                null
                            },
                        )

                        FilterChip(
                            selected = !split.isByUnit,
                            onClick = {
                                onEvent(
                                    TransactionFormEvent.SetIsByUnit(
                                        split.id,
                                        false
                                    )
                                )
                            },
                            label = {
                                Text(
                                    "By Total",
                                    textAlign = TextAlign.Center,
                                )
                            },
                            leadingIcon = if (!split.isByUnit) {
                                {
                                    Icon(
                                        painter = painterResource(R.drawable.check),
                                        contentDescription = "Done icon",
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else {
                                null
                            },
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledIconButton(
                        modifier = Modifier.offset(x = (-4).dp),
                        onClick = { onEvent(TransactionFormEvent.DecrementQuantity(split.id)) },
                        colors = IconButtonDefaults.filledIconButtonColors().copy(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text("-")
                    }
                    AnimatedContent(split.units, modifier = Modifier.weight(1f)) { units ->
                        Text(
                            "${split.units} ${split.product.defaultUnit ?: ""}",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    FilledIconButton(
                        modifier = Modifier.offset(x = (4).dp),
                        onClick = { onEvent(TransactionFormEvent.IncrementQuantity(split.id)) },
                        colors = IconButtonDefaults.filledIconButtonColors().copy(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text("+")
                    }
                }

                if (split.isByUnit) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total", style = MaterialTheme.typography.labelLarge)
                        AnimatedContent(split.units) { units ->
                            Text(
                                "$${split.product.pricePerUnit!!.pretty} * ${units} = $${(split.product.pricePerUnit * units).pretty}",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                } else {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = split.totalText,
                        onValueChange = { onEvent(TransactionFormEvent.SetTotal(split.id, it)) },
                        label = { Text("Total") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal
                        ),
                        placeholder = { Text("0.00") },
                        trailingIcon = {
                            Icon(painterResource(R.drawable.currency_dollar), null)
                        }
                    )
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onEvent(TransactionFormEvent.ConfirmSplit(split.id)) }
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

@Preview
@Composable
private fun SplitItemCardPreview(open: Boolean = false, isByUnit: Boolean = true) {
    AppTheme {
        SplitItemCard(
            open = open,
            split = SplitItem(
                id = 1,
                product = Product(
                    id = 1,
                    name = "Milk",
                    description = "Whole milk",
                    type = "Dairy",
                    defaultUnit = "Liter",
                    pricePerUnit = 1.50
                ),
                isByUnit = isByUnit,
                units = 2,
                total = 3.00,
                isNew = false
            )
        )
    }
}

@Preview
@Composable
private fun SplitItemCardOpenPreview() {
    SplitItemCardPreview(true)
}

@Preview
@Composable
private fun SplitItemCardOpenByTotalPreview() {
    SplitItemCardPreview(true, isByUnit = false)
}