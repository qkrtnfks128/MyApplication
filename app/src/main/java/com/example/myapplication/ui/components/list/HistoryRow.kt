package com.example.myapplication.ui.components.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryRow(
    timeString: String,
    rightWidget: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 왼쪽: 포매팅된 시간 텍스트
        Text(
            text = formatTimeString(timeString),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
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
        val outputFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
        
        val date = inputFormat.parse(timeString)
        date?.let { outputFormat.format(it) } ?: timeString
    } catch (e: Exception) {
        // 파싱 실패 시 원본 문자열 반환
        timeString
    }
}
