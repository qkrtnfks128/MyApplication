package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


object YC_Color {
    val green: Color = Color(0xFF15A93C)
    val blue: Color = Color(0xFF0052A4)
}

object CustomColor {
    val white: Color = Color(0xFFFFFFFF)
    val red: Color = Color(0xFFFF2215)
    val orange: Color = Color(0xFFE66700)
    val orange_ligt: Color = Color(0xFFFF7B00) // 디자인 명칭 유지
    val yellow: Color = Color(0xFFFFCC00)
    val green: Color = Color(0xFF00A356)
    val teal: Color = Color(0xFF5AC8FA)
    val blue: Color = Color(0xFF007AFF)
    val indigo: Color = Color(0xFF2A28A4)
    val purple: Color = Color(0xFF932BC7)
    val pink: Color = Color(0xFFE02A4D)
    val black: Color = Color(0xFF000000)
    val gray01: Color = Color(0xFF7C7C7C)
    val gray02: Color = Color(0xFFAEAEB2)
    val gray03: Color = Color(0xFFC7C7CC)
    val gray04: Color = Color(0xFFD1D1D6)
    val gray05: Color = Color(0xFFE5E5EA)
    val gray06: Color = Color(0xFFF2F2F7)
    val bg: Color = Color(0xFFF9FAFC)
}
object Stroke {
    val black20: Color = Color(0x33000000) // 20% alpha
}

data class ShadowToken(
    val color: Color,
    val elevation: Dp,      
)

data class ShadowTokens(
    val default: ShadowToken = ShadowToken(Color(0xFF000000), 16.dp), // #000000/25%
    val header: ShadowToken = ShadowToken(Color(0x26000000), 12.dp),  // #000000/15%
)