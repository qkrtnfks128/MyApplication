package com.example.myapplication.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.components.AppBar
import com.example.myapplication.components.LeftButtonType
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.CustomColor
import com.example.myapplication.manager.AdminManager
import com.example.myapplication.manager.SelectedMeasurementStore
import com.example.myapplication.manager.SelectedOrgStore
import com.example.myapplication.manager.SelectedUserStore
import com.example.myapplication.navigation.LocalAppNavController
import com.example.myapplication.page.measurement.MeasurementType
import com.example.myapplication.utils.LogManager


// MainScreen은 메인 화면으로, 혈당, 혈압, 체중 측정 버튼을 포함합니다.
// 각 버튼은 클릭 시 유저로그인화면 또는 해당 측정 화면으로 이동합니다.

private const val MAIN_SCREEN_TAG: String = "MainScreen"
@Composable
fun MainScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        // verticalArrangement = Arrangement.Center
    ) {
        // AppBar
        AppBar(
            leftButtonType = if (SelectedUserStore.get() != null) LeftButtonType.LOGOUT else LeftButtonType.NONE,
            centerWidget = {
                if (SelectedUserStore.get() != null) {
                    Text(
                        text = "${SelectedUserStore.get()?.name} 어르신",
                        style = TextStyle(
                            fontSize = 40.sp,
                            lineHeight = 52.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard)),
                            fontWeight = FontWeight(600),
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                        ),
                    )

                }else{
                    
                Box(
                    modifier = Modifier
                        .clickable { /* 클릭 시 동작 추가 가능 */
                            LogManager.userAction(MAIN_SCREEN_TAG, "메인 앱바 중앙 텍스트 클릭")
                        // 기관 선택이 되어있는지 확인
                        navController.navigate(Screen.AdminOrgSelect.route)
                        }
                        .fillMaxWidth()
                ) {
                    Text(
                        text = SelectedOrgStore.getSelected()?.orgName
                            ?: "오늘의 건강을 확인해볼까요?",
                        style = TextStyle(
                            fontSize = 40.sp,
                            lineHeight = 52.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard)),
                            fontWeight = FontWeight(600),
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                }

            }
        )
        
        // 기존 3개 버튼 영역 교체
        val items = listOf(
            MeasurementType.BloodSugar,
            MeasurementType.BloodPressure,
            MeasurementType.Weight
        )

        Row(
            modifier = Modifier
                .fillMaxWidth().fillMaxHeight().background(CustomColor.red)
                .padding(start = 40.dp, top = 10.dp, end = 40.dp, bottom = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(21.dp)
        ) {
            items.forEach { type ->
                MeasurementButton(
                    type = type,
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// 헬퍼: 타입 저장 후 페이지 이동
private fun navigateToMeasurement(navController: NavController, type: MeasurementType) {
    SelectedMeasurementStore.save(type)
    val hasUser = SelectedUserStore.get() != null
    if (hasUser) {
        navController.navigate(Screen.Measurement.route + "/${type.name}")
    } else {
        navController.navigate(Screen.UserAuth.route)
    }
}

@Composable
fun MeasurementButton(
    type: MeasurementType,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val (title: String, backgroundColor: Color) = when (type) {
        MeasurementType.BloodSugar -> "혈당" to Color(0xFFE53E3E)
        MeasurementType.BloodPressure -> "혈압" to Color(0xFFED8936)
        MeasurementType.Weight -> "체중" to Color(0xFF38A169)
    }

    Button(
        onClick = {
            LogManager.userAction(MAIN_SCREEN_TAG, "$title 측정 버튼 클릭")
            navigateToMeasurement(navController, type)
        },
        modifier = modifier.fillMaxHeight(),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(text = title+"측정",style=MaterialTheme.typography.b1,color=CustomColor.white)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
	MyApplicationTheme {
		val ctx = LocalContext.current
	SelectedOrgStore.initialize(ctx)
		val nav = rememberNavController()
		CompositionLocalProvider(LocalAppNavController provides nav) {
			MainScreen(nav)
		}
	}
}