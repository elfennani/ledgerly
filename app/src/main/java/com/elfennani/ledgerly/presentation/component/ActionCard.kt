package com.elfennani.ledgerly.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.presentation.theme.AppTheme

@Composable
fun ActionCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    trailing: @Composable () -> Unit = {},
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(enabled = enabled) {
                onClick()
            }
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.shapes.large
            )
            .alpha(if (enabled) 1f else 0.5f)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
            icon()
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.primary,
                LocalTextStyle provides MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.W600,
                    fontSize = 18.sp
                )
            ) {
                title()
            }

            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface,
                LocalTextStyle provides MaterialTheme.typography.bodySmall
            ) {
                description()
            }
        }

        trailing()
    }
}

@Preview
@Composable
private fun ActionCardPreview(enabled: Boolean = true) {
    AppTheme {
        ActionCard(
            modifier = Modifier.fillMaxWidth(),
            icon = {
                Icon(
                    painterResource(R.drawable.flag),
                    null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = { Text("Set Target") },
            description = { Text("Define how much you want to spend in this category to track you progress.") },
            enabled = enabled
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ActionCardDarkPreview() {
    ActionCardPreview()
}

@Preview
@Composable
private fun ActionCardDisabledPreview() {
    ActionCardPreview(false)
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ActionCardDisabledDarkPreview() {
    ActionCardPreview(false)
}