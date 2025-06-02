package com.southapps.core.designSystem.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.southapps.core.UIAction

@Composable
fun Dialog(
    action: UIAction.ShowDialog,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
            action.cancelButton.second.invoke()
        },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                action.confirmButton.second.invoke()
            }) {
                Text(action.confirmButton.first)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
                action.cancelButton.second.invoke()
            }) {
                Text(action.cancelButton.first)
            }
        },
        title = { Text(action.title) },
        text = { Text(action.message) }
    )
}