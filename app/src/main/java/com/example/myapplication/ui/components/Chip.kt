package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.CustomColor
import com.example.myapplication.ui.theme.b4
import com.example.myapplication.ui.theme.b5

/**
 * Chip 컴포넌트
 * @param text 표시할 텍스트
 * @param color 배경색
 * @param modifier Modifier
 */
@Composable
fun Chip(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(100.dp),
        color = color
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.b5.copy(
                color = CustomColor.white,
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChipPreview() {
    androidx.compose.material3.MaterialTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            Chip(
                text = "높음",
                color = Color.Red
            )
            Chip(
                text = "정상",
                color = Color.Green
            )
            Chip(
                text = "낮음",
                color = Color.Blue
            )
        }
    }
}
