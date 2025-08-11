package com.example.myapplication.page.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.components.AppBar
import com.example.myapplication.components.LeftButtonType
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun PhoneAuthScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar(
            leftButtonType = LeftButtonType.BACK,
            centerWidget = { Text(text = "사용자 인증", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)) }
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "휴대전화 뒷자리 4자리를 입력해 주세요.", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        val digits = remember { mutableStateListOf("", "", "", "") }
        val nextIndex: Int = digits.indexOfFirst { it.isEmpty() }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            digits.forEachIndexed { index: Int, value: String ->
                val isActive: Boolean = nextIndex != -1 && index == nextIndex
                val borderColor: Color = if (isActive) Color(0xFF1E88E5) else Color(0xFFB0BEC5)
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 130.dp)
                        .border(4.dp, borderColor, RoundedCornerShape(16.dp))
                        .background(Color(0xFFF2F3F5), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = value, style = MaterialTheme.typography.headlineLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val keyHeight: androidx.compose.ui.unit.Dp = 88.dp
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2F3F5), RoundedCornerShape(24.dp))
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.align(Alignment.Center)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().height(keyHeight)
                ) {
                    listOf("1", "2", "3", "4", "5").forEach { label: String ->
                        KeyButton(label = label, height = keyHeight, modifier = Modifier.weight(1f)) {
                            val idx: Int = digits.indexOfFirst { it.isEmpty() }
                            if (idx in 0..3) digits[idx] = label
                        }
                    }
                    ActionButton(text = "지우기", color = Color(0xFFE0E0E0), textColor = Color.Black, height = keyHeight, modifier = Modifier.weight(1f)) {
                        val idx: Int = digits.indexOfLast { it.isNotEmpty() }
                        if (idx >= 0) digits[idx] = ""
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().height(keyHeight)
                ) {
                    listOf("6", "7", "8", "9", "0").forEach { label: String ->
                        KeyButton(label = label, height = keyHeight, modifier = Modifier.weight(1f)) {
                            val idx: Int = digits.indexOfFirst { it.isEmpty() }
                            if (idx in 0..3) digits[idx] = label
                        }
                    }
                    ActionButton(text = "입력완료", color = Color(0xFF1976D2), textColor = Color.White, height = keyHeight, modifier = Modifier.weight(1f)) {
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyButton(label: String, height: androidx.compose.ui.unit.Dp, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(height).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = label, fontSize = 56.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ActionButton(text: String, color: Color, textColor: Color = Color.Black, height: androidx.compose.ui.unit.Dp, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.height(height).clickable { onClick() },
        color = color,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = text, color = textColor, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhoneAuthScreenPreview() {
    MyApplicationTheme { PhoneAuthScreen(rememberNavController()) }
}


