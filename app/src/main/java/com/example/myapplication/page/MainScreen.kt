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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.example.myapplication.model.MeasurementType
import com.example.myapplication.model.displayName
import com.example.myapplication.utils.LogManager
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.myapplication.components.dialog.BigDialog
import com.example.myapplication.ui.theme.Stroke
import com.example.myapplication.ui.theme.b1
import androidx.compose.ui.draw.shadow
import com.example.myapplication.ui.theme.ShadowTokens


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
                .fillMaxWidth().fillMaxHeight()
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
	val title: String = type.displayName()

	val shape = RoundedCornerShape(24.dp)
	val bgPainter = when (type) {
		MeasurementType.BloodSugar -> painterResource(R.drawable.blood_sugar)
		MeasurementType.BloodPressure -> painterResource(R.drawable.blood_pressure)
		MeasurementType.Weight -> painterResource(R.drawable.weight)
	}

	var showTimingDialog by remember { mutableStateOf(false) }

	Button(
		onClick = {
			LogManager.userAction(MAIN_SCREEN_TAG, "$title 측정 버튼 클릭")
			if (type == MeasurementType.BloodSugar) {
				showTimingDialog = true
			} else {
				navigateToMeasurement(navController, type)
			}
		},
		modifier = modifier
			.fillMaxHeight()
			.shadow(ShadowTokens().default.elevation, shape = shape)
			.clip(shape)
			.paint(bgPainter, contentScale = ContentScale.Crop)
			.border(10.dp, Stroke.black20, shape),
		colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
		shape = shape,
		contentPadding = PaddingValues(0.dp)
	) {
		Text(
			text = "$title\n측정",
			style = MaterialTheme.typography.bodyLarge,
			color = CustomColor.white,
			textAlign = TextAlign.Center
		)
	}

        //혈당 측정시기 선택 다이얼로그
	if (showTimingDialog) {
		BigDialog(
			title = "언제 ${title}을 측정하시는 건가요?",
			onDismiss = { showTimingDialog = false },
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            SelectedMeasurementStore.save(type, true)
                            showTimingDialog = false
                            navigateToMeasurement(navController, type)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(96.dp)
                            .border(
                                width = 10.dp,
                                color = Stroke.black20,
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(RoundedCornerShape(30.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomColor.pink,
                            contentColor = CustomColor.white
                        ),
                        shape = RoundedCornerShape(30.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "식사전",
                            style = MaterialTheme.typography.titleLarge,
                            color = CustomColor.white
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            SelectedMeasurementStore.save(type, false)
                            showTimingDialog = false
                            navigateToMeasurement(navController, type)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(96.dp)
                            .border(
                                width = 10.dp,
                                color = Stroke.black20,
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clip(RoundedCornerShape(30.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomColor.purple,
                            contentColor = CustomColor.white
                        ),
                        shape = RoundedCornerShape(30.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "식사후",
                            style = MaterialTheme.typography.titleLarge,
                            color = CustomColor.white
                        )
                    }
                }
            }
		)
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