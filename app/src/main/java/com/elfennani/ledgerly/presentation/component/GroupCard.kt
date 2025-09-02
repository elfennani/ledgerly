package com.elfennani.ledgerly.presentation.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.domain.model.Group
import com.elfennani.ledgerly.presentation.theme.AppTheme
import com.elfennani.ledgerly.presentation.utils.pretty

fun String.startsWithEmoji(): Boolean {
    if (isEmpty()) return false
    val codePoint = this.codePointAt(0)
    val type = Character.getType(codePoint)
    return type == Character.OTHER_SYMBOL.toInt()
}

@Composable
fun GroupCard(
    modifier: Modifier = Modifier,
    group: Group,
    onToggleCollapse: () -> Unit = {},
    onAdd: () -> Unit = {},
) {
    val rotation by animateFloatAsState(targetValue = if (group.collapsed) 180f else 0f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleCollapse() }
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 12.dp)
                    .padding(end = 4.dp)
                    .height(64.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .clip(CircleShape)
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = group.name.substring(0, 2).takeIf { group.name.startsWithEmoji() }

                    if (icon != null) {
                        Text(
                            text = icon,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.baseline_inventory_24),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = group.name.let {
                            if (it.startsWithEmoji()) {
                                it.drop(2).trim()
                            } else {
                                it
                            }
                        },
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = "${group.categories?.size ?: 0} Categories",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
                FilledIconButton(
                    onClick = { onToggleCollapse() },
                    modifier = Modifier
                        .size(32.dp)
                        .rotate(rotation)
                ) {
                    Icon(painterResource(R.drawable.chevron_up), null)
                }
            }

            AnimatedVisibility(visible = !group.collapsed, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth()) {
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.background
                    )
                    group.categories?.forEach { category ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 12.dp)
                                .padding(end = 4.dp)
                                .height(64.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 12.dp)
                                    .clip(CircleShape)
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                                    .background(Color.Gray.copy(alpha = 0.12f))
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                val icon = category.name.substring(0, 2)
                                    .takeIf { category.name.startsWithEmoji() }

                                if (icon != null) {
                                    Text(
                                        text = icon,
                                        fontSize = 14.sp,
                                        lineHeight = 14.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_inventory_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(bottom = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = category.name.let {
                                            if (it.startsWithEmoji()) {
                                                it.drop(2).trim()
                                            } else {
                                                it
                                            }
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "$0 / $${category.target?.pretty ?: "0.00"}",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                                LinearProgressIndicator(
                                    progress = { 0.2f },
                                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    drawStopIndicator = {}
                                )
                            }

                        }

                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.background
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAdd() }
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 12.dp)
                            .padding(end = 4.dp)
                            .height(64.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .clip(CircleShape)
                                .fillMaxHeight()
                                .aspectRatio(1f)
                                .background(Color.Gray.copy(alpha = 0.12f))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.plus),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = if ((group.categories?.size
                                    ?: 0) == 0
                            ) "Add category" else "Add another category",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GroupCardPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            GroupCard(
                group = Group(
                    id = 1,
                    name = "💡 Monthly Bills",
                    collapsed = false,
                    index = 0,
                    categories = listOf(
                        Category(
                            id = 1,
                            name = "🛒 Groceries",
                            groupId = 1,
                            target = 800.0,
                        ),
                        Category(
                            id = 2,
                            name = "🌎 Internet",
                            groupId = 1,
                            target = 60.0,
                        ),
                        Category(
                            id = 3,
                            name = "Music",
                            groupId = 1,
                            target = 15.0,
                        ),
                    )
                ),
            )
        }
    }
}

@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun GroupCardDarkPreview() {
    GroupCardPreview()
}