package com.example.qrcodeapp

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ModalDialog(
    title: String,
    text: String,
    confirmText:String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit

) {
    AlertDialog(
        title = {
            Text(text = title)
        },
        text = {
            Text(text = text)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Позже")
            }
        }
    )
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DialogReminderPreview() {
    ModalDialog("title", "text", "confirm", {}, {})
}