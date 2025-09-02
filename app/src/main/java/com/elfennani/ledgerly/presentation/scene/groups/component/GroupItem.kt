package com.elfennani.ledgerly.presentation.scene.groups.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Group

@Composable
fun GroupItem(
    modifier: Modifier = Modifier,
    group: Group,
    onEditClick: () -> Unit = { },
    onDeleteClick: () -> Unit = { },
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.shapes.small
            )
            .padding(vertical = 8.dp)
            .padding(start = 16.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.bars_2),
            contentDescription = null
        )
        Spacer(Modifier.width(8.dp))
        Text(
            group.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        TextButton(
            onClick = { onEditClick() },
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Icon(painterResource(R.drawable.pencil_square), null)
            Spacer(Modifier.width(6.dp))
            Text("Edit")
        }
        IconButton(onClick = { onDeleteClick() }) {
            Icon(
                painter = painterResource(R.drawable.trash),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }

}