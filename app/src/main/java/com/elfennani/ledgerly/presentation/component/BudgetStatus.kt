package com.elfennani.ledgerly.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfennani.ledgerly.domain.model.BudgetData
import com.elfennani.ledgerly.presentation.utils.pretty
import kotlin.math.roundToInt

@Composable
fun BudgetStatus(
    modifier: Modifier = Modifier,
    budgetData: BudgetData
) {
    if (budgetData.unused < 0.0) {
        val overAmount = -budgetData.unused
        val total = if (budgetData.total != 0.0) budgetData.total else 1.0
        val percentOver = (overAmount / total).coerceAtMost(1.0)


        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { percentOver.toFloat() },
                    trackColor = MaterialTheme.colorScheme.errorContainer,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(56.dp),
                )
                Text(
                    "${(percentOver * 100).roundToInt()}%",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Over Budget",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "$${overAmount.pretty} over budget — reduce assigned amounts",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    } else if (budgetData.unused != 0.0) {
        val percentage =
            (budgetData.total - budgetData.unused) / budgetData.total

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { percentage.toFloat() },
                    trackColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(56.dp),
                )
                Text(
                    "${(percentage * 100).roundToInt()}%",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W600
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Ready to Assign",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "$${budgetData.unused.pretty} Left to Budget",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}