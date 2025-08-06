package com.example.myapplication.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun BigDialog(
    title: String,
    content: @Composable () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    confirmText: String = "확인",
    cancelText: String = "취소",
    showCancelButton: Boolean = true,
    showConfirmButton: Boolean = true,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                content()
            }
        },
        confirmButton = {
            if (showConfirmButton) {
                Button(
                    onClick = {
                        onConfirm?.invoke()
                        onDismiss()
                    }
                ) {
                    Text(text = confirmText)
                }
            }
        },
        dismissButton = {
            if (showCancelButton) {
                TextButton(
                    onClick = {
                        onCancel?.invoke()
                        onDismiss()
                    }
                ) {
                    Text(text = cancelText)
                }
            }
        }
    )
}
