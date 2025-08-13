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
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.manager.SelectedOrgStore
import com.example.myapplication.navigation.LocalAppNavController
import com.example.myapplication.navigation.Screen
import com.example.myapplication.repository.SmartCareRepositoryFactory
import com.example.myapplication.network.NetworkConfig
import com.example.myapplication.ui.theme.b4
import com.example.myapplication.ui.theme.h1
import com.example.myapplication.ui.theme.CustomColor
import com.example.myapplication.ui.theme.Stroke
import com.example.myapplication.ui.theme.b1
import com.example.myapplication.ui.theme.b5
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign

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
            centerWidget = { Text(text = "사용자 인증", style = MaterialTheme.typography.h1) }
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "휴대전화 뒷자리 4자리를 입력해 주세요.", style = MaterialTheme.typography.b4)

        Spacer(modifier = Modifier.height(9.dp))

        // 입력 번호 표시
        val digits = remember { mutableStateListOf("", "", "", "") }
        var activeIndex by remember { mutableStateOf(digits.indexOfFirst { it.isEmpty() }.coerceAtLeast(0)) }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            digits.forEachIndexed { index: Int, value: String ->
                val isActive: Boolean = index == activeIndex
                val borderColor: Color = if (isActive) CustomColor.blue else CustomColor.gray04
                Box(
                    modifier = Modifier
                        .size(width = 110.dp, height = 130.dp)
                        .border(5.dp, borderColor, RoundedCornerShape(10.dp))
                        .background(CustomColor.gray06, RoundedCornerShape(10.dp))
                        .clickable { activeIndex = index },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = value,
                        style = TextStyle(
                            fontSize = 90.sp,
                            lineHeight = 117.sp,
                            fontWeight = FontWeight(600),
                            color = CustomColor.black,
                            textAlign = TextAlign.Center
                        )
                    )
                    if (isActive && value.isEmpty()) {
                        val transition = rememberInfiniteTransition(label = "cursor")
                        val alpha: Float by transition.animateFloat(
                            initialValue = 1f,
                            targetValue = 0f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(durationMillis = 650),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "cursorAlpha"
                        )
                        Box(
                            modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 12.dp)
                                .height(90.dp)
                                .width(6.dp)
                                .background(CustomColor.black)
                                .alpha(alpha)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(9.dp))

        // 숫자패드 및 액션버튼
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val repo = remember { SmartCareRepositoryFactory.create() }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.gray04, )
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.align(Alignment.Center)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("1", "2", "3", "4", "5").forEach { label: String ->
                        KeyButton(label = label, modifier = Modifier.weight(1f)) {
                            digits[activeIndex] = label
                            val next = activeIndex+1
                            activeIndex = if (next != 4) next else 3
                        }
                    }
                    ActionButton(text = "지우기", color = CustomColor.white, textColor = CustomColor.gray01, ) {
                        if (digits[activeIndex].isEmpty()) {
                            if (activeIndex > 0) {
                                digits[activeIndex] = ""
                                activeIndex -= 1
                            }
                        } else {
                            digits[activeIndex] = ""
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("6", "7", "8", "9", "0").forEach { label: String ->
                        KeyButton(label = label, modifier = Modifier.weight(1f)) {
                            if (activeIndex in 0..3 ) {
                                digits[activeIndex] = label
                            val next = activeIndex+1
                            activeIndex = if (next != 4) next else 3
                            }
                        }
                    }
                    ActionButton(
                        text = "입력완료",
                        color = CustomColor.blue,
                        textColor = CustomColor.white,
                    ) {
                        val phoneNumber: String = digits.joinToString("")
                        if (phoneNumber.length != 4) {
                            Toast.makeText(context, "4자리를 입력해주세요.", Toast.LENGTH_SHORT).show()
                            return@ActionButton
                        }
                        scope.launch {
                            val centerUuid: String = SelectedOrgStore.getSelected()?.orgUuid ?: ""
                            val result = repo.getUserListUsingPhoneNumber(
                                customerCode = NetworkConfig.CUSTOMER_CODE,
                                centerUuid = centerUuid,
                                number = phoneNumber
                            )
                            result.fold(
                                onSuccess = { r ->
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        Screen.UserResult.KEY_RESULT,
                                        r
                                    )
                                    navController.navigate(Screen.UserResult.route)
                                },
                                onFailure = { e ->
                                    Toast.makeText(context, e.message ?: "요청을 실패했습니다.", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(98.dp).fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = label, style = MaterialTheme.typography.b1)
        }
    }
}

@Composable
private fun ActionButton(text: String, color: Color, textColor: Color = CustomColor.black, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.height(98.dp).width(194.dp).clickable { onClick() },
        color = color,
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = text, color = textColor, style = MaterialTheme.typography.b5)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhoneAuthScreenPreview() {
    val nav = rememberNavController()
    CompositionLocalProvider(LocalAppNavController provides nav) {
        PhoneAuthScreen(nav)
    }
}


