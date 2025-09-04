package com.elfennani.ledgerly.presentation.scene.category.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.elfennani.ledgerly.R
import com.elfennani.ledgerly.domain.model.Category
import com.elfennani.ledgerly.presentation.scene.category.CategoryEvent

@Composable
fun DeleteCategoryDialog(
    category: Category,
    onEvent: (CategoryEvent) -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(painterResource(R.drawable.trash), null)
        },
        title = {
            Text(text = "Delete category '${category.name}'", textAlign = TextAlign.Center)
        },
        text = {
            Text(text = "Are you sure you want to delete this category? This action cannot be undone.")
        },
        onDismissRequest = {
            onEvent(CategoryEvent.DismissDeleteCategoryDialog)
        },
        confirmButton = {
            Button(
                onClick = {
                    onEvent(CategoryEvent.ConfirmDeleteCategory)
                },
                colors = ButtonDefaults.textButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onEvent(CategoryEvent.DismissDeleteCategoryDialog)
                }
            ) {
                Text("Dismiss")
            }
        }
    )

}