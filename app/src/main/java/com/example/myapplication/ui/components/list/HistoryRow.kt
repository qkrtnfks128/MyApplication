package com.example.myapplication.ui.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.CustomColor
import com.example.myapplication.ui.theme.b4
import java.text.SimpleDateFormat
import java.util.*


import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme


@Composable
fun HistoryRow(
    timeString: String,
    rightWidget: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = CustomColor.gray01,
                    start = androidx.compose.ui.geometry.Offset(0f, size.height),
                    end = androidx.compose.ui.geometry.Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .padding( vertical = 30.dp,horizontal = 40.dp),

        verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // 왼쪽: 포매팅된 시간 텍스트
        Text(
            text = formatTimeString(timeString),
            style = MaterialTheme.typography.b4
        )

        // 오른쪽: 위젯
        rightWidget()
    }
}

/**
 * yyyymmddhhmmss 형식의 시간 문자열을 읽기 쉬운 형태로 포매팅
 */
private fun formatTimeString(timeString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM월 dd일 a hh:mm", Locale.getDefault())

        val date = inputFormat.parse(timeString)
        date?.let { outputFormat.format(it) } ?: timeString
    } catch (e: Exception) {
        // 파싱 실패 시 원본 문자열 반환
        timeString
    }
}


@Preview(showBackground = true)
@Composable
fun HistoryRowPreview() {
    MyApplicationTheme {
        HistoryRow(
            timeString = "20240601123045",
            rightWidget = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "식전", style = MaterialTheme.typography.b4)
                    Text(
                        text = "정상",
                        color = CustomColor.green,
                        style = MaterialTheme.typography.b4,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        )
    }
}
