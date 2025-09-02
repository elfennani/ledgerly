package com.elfennani.ledgerly.presentation.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Account
import com.elfennani.ledgerly.presentation.theme.AppTheme

@Composable
fun GroupCard(
    modifier: Modifier = Modifier,
    title: String,
    collapsed: Boolean = false,
    onToggleCollapse: () -> Unit = {},
    onAdd: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val rotation by animateFloatAsState(targetValue = if (!collapsed) 180f else 0f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainer, MaterialTheme.shapes.large),
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleCollapse() }
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    onClick = { onAdd() },
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.plus_small),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Add")
                }
                IconButton(
                    onClick = { onToggleCollapse() },
                    modifier = Modifier
                        .size(32.dp)
                        .rotate(rotation)
                ) {
                    Icon(painterResource(R.drawable.chevron_up), null)
                }
            }
            AnimatedVisibility(visible = collapsed, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth()) {
                    content()
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
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            GroupCard(title = "🧾 Bills") {
                AccountCard(
                    account = Account(
                        id = 1,
                        name = "🛒 Groceries",
                        balance = 1234.56
                    ),
                    modifier = Modifier.clickable {}
                )
                AccountCard(
                    account = Account(
                        id = 1,
                        name = "🌎 Internet",
                        balance = 55.56
                    ),
                    modifier = Modifier.clickable {}
                )
                AccountCard(
                    account = Account(
                        id = 1,
                        name = "🎶 Music",
                        balance = 1435.56
                    ),
                    modifier = Modifier.clickable {}
                )
            }
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