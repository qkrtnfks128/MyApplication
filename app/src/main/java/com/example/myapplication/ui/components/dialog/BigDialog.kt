package com.example.myapplication.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.ui.theme.b3

@Composable
fun BigDialog(
    title: String,
    content: @Composable () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val config = LocalConfiguration.current
    val maxWidth = 0.9f * config.screenWidthDp.dp
    val maxHeight = 0.9f * config.screenHeightDp.dp

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
            .widthIn(max = maxWidth)
            .heightIn(max = maxHeight),
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        ),
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.b3,
            )
        },
        text = { content() },
        confirmButton = { /* ...필요시 버튼 넣기... */ }
    )
}
