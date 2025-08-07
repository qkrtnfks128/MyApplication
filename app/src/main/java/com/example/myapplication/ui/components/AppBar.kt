package com.example.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import androidx.compose.ui.tooling.preview.Preview


enum class LeftButtonType {
    NONE, HOME, BACK, LOGOUT
}

@Preview(showBackground = true)
@Composable
fun AppBar(
    leftButtonType: LeftButtonType = LeftButtonType.NONE,
    centerWidget: @Composable (() -> Unit)? = null,
    onLeftButtonClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 40.dp, top = 24.dp, end = 40.dp, bottom = 24.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            
            contentAlignment = Alignment.Center
        ) {
            // 가운데 위젯 - 항상 중앙에 위치
            centerWidget?.invoke()
            
            // 왼쪽 버튼 섹션 - 왼쪽에 고정
            if (leftButtonType != LeftButtonType.NONE) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .clickable { onLeftButtonClick?.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    when (leftButtonType) {
                        LeftButtonType.HOME -> {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "홈",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        LeftButtonType.BACK -> {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "뒤로가기",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        LeftButtonType.LOGOUT -> {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "로그아웃",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        LeftButtonType.NONE -> {
                            // 이 경우는 실행되지 않음
                        }
                    }
                }
            }
            
            // 우측 종료 버튼 섹션 - 오른쪽에 고정
            Button(
                onClick = { /* 종료 버튼 클릭 시 동작 구현 */ },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .shadow(elevation = 12.dp, spotColor = Color(0x26000000), ambientColor = Color(0x26000000))
                    .background(color = Color.White, shape = RoundedCornerShape(size = 100.dp))
                    .padding(start = 20.dp, top = 20.dp, end = 30.dp, bottom = 20.dp),
                shape = RoundedCornerShape(size = 100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                contentPadding = PaddingValues(0.dp),
                elevation = null
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = "종료",
                        tint = Color.Red,
                        modifier = Modifier.size(42.dp)
                    )
                    Text(
                        text = "종료",
                        style = TextStyle(
                          fontSize = 40.sp,
                          lineHeight = 52.sp,
                          fontFamily = FontFamily(Font(R.font.pretendard)),
                          fontWeight = FontWeight(600),
                          color = Color.Black,
                          textAlign = TextAlign.Center,
                        )
                    )
                }
            }
        }
    }
}