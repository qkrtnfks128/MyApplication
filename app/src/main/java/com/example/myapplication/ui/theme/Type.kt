package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

private val Pretendard = FontFamily(Font(R.font.pretendard))

//Text("본문", style = MaterialTheme.typography.b1)

// Material3 기본 타입 설정(필요 시 값 조정)
val Typography = Typography(
    displayLarge = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 45.sp, lineHeight = 58.5.sp),
    displayMedium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Bold, fontSize = 50.sp, lineHeight = 65.sp),
    displaySmall = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 40.sp, lineHeight = 52.sp),
    bodyLarge = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Bold, fontSize = 80.sp, lineHeight = 120.sp),
    bodyMedium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 72.sp, lineHeight = 100.8.sp),
    bodySmall = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Bold, fontSize = 56.sp, lineHeight = 84.sp),
    titleLarge = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 45.sp, lineHeight = 58.5.sp),
    titleMedium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 42.sp, lineHeight = 54.6.sp)
)

// 커스텀 네이밍 확장 프로퍼티(디자인 명칭과 매핑)
val androidx.compose.material3.Typography.h1: TextStyle
    get() = displayLarge.copy(textAlign = TextAlign.Center)

val androidx.compose.material3.Typography.h2: TextStyle
    get() = displayMedium.copy(textAlign = TextAlign.Center)

val androidx.compose.material3.Typography.h3: TextStyle
    get() = displaySmall.copy(textAlign = TextAlign.Center)

val androidx.compose.material3.Typography.b1: TextStyle
    get() = bodyLarge.copy(textAlign = TextAlign.Center)

val androidx.compose.material3.Typography.b2: TextStyle
    get() = bodyMedium.copy(textAlign = TextAlign.Center)

val androidx.compose.material3.Typography.b3: TextStyle
    get() = bodySmall.copy(textAlign = TextAlign.Center)

val androidx.compose.material3.Typography.b4: TextStyle
    get() = titleLarge.copy(textAlign = TextAlign.Center)

val androidx.compose.material3.Typography.b5: TextStyle
    get() = titleMedium.copy(textAlign = TextAlign.Center)