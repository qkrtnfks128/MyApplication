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
import com.example.myapplication.ui.components.CustomToast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.example.myapplication.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.example.myapplication.repository.WonderfulRepositoryFactory
import kotlinx.coroutines.flow.collectLatest
import com.example.myapplication.viewmodel.login.PhoneAuthViewModel
import com.example.myapplication.viewmodel.login.PhoneAuthEvent

@Composable
fun PhoneAuthScreen(navController: NavController,vm: PhoneAuthViewModel = viewModel()) {
    val context = LocalContext.current
    val ui by vm.uiState.collectAsState()

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

        // 입력 번호 표시 (ViewModel 상태 사용)
        val digits = ui.digits
        val activeIndex = ui.activeIndex
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            digits.forEachIndexed { index: Int, value: String ->
                val isActive: Boolean = index == activeIndex
                val borderColor: Color = if (isActive) CustomColor.blue else CustomColor.gray04
                Box(
                    modifier = Modifier
                        .size(width = 110.dp, height = 130.dp)
                        .border(5.dp, borderColor, RoundedCornerShape(10.dp))
                        .background(CustomColor.gray06, RoundedCornerShape(10.dp))
                        .clickable { /* 커서 이동은 append/delete 로직에서 자동 진행 */ },
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
        val repo = remember { WonderfulRepositoryFactory.create() }
        Box(
            modifier = Modifier
                .fillMaxWidth().fillMaxHeight()
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
                            vm.appendDigit(label)
                        }
                    }
                        ActionButton(
                            color = CustomColor.white,
                            content = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource( R.drawable.ic_backspace),
                                    contentDescription = "지우기 아이콘",
                                    tint = CustomColor.gray01
                                )

                                Text(text = "지우기", style = MaterialTheme.typography.b5,color = CustomColor.gray01)
                                }
                            },
                        ) {
                            vm.deleteDigit()
                        }
                    }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("6", "7", "8", "9", "0").forEach { label: String ->
                        KeyButton(label = label, modifier = Modifier.weight(1f)) {
                            vm.appendDigit(label)
                        }
                    }
                    ActionButton(
                        content = {
                                Text(text = "입력완료", style = MaterialTheme.typography.b5,color = CustomColor.white)
                        },
                        color = if (ui.isLoading) CustomColor.gray01 else CustomColor.blue,
                    ) {

                        vm.submit()
                    }
                }
            }
        }
    }

    // 이벤트 수집: 성공 시 내비게이션, 실패 시 토스트
    LaunchedEffect(Unit) {
        vm.events.collectLatest { ev ->
            when (ev) {
                is PhoneAuthEvent.Success -> {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        Screen.UserResult.KEY_RESULT,
                        ev.result
                    )
                    navController.navigate(Screen.UserResult.route)
                }
                is PhoneAuthEvent.Error -> {
                    CustomToast.show(context, ev.message)
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
private fun ActionButton(
    content: @Composable () -> Unit,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(98.dp)
            .width(194.dp)
            .clickable { onClick() },
        color = color,
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
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


