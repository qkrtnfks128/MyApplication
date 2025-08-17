package com.example.myapplication.page.measurement


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.AppBar
import com.example.myapplication.components.LeftButtonType
import com.example.myapplication.model.BloodSugarData
import com.example.myapplication.model.MealType
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.components.Chip
import com.example.myapplication.ui.theme.CustomColor
import com.example.myapplication.ui.theme.Stroke
import com.example.myapplication.ui.theme.b1
import com.example.myapplication.ui.theme.h1
import com.example.myapplication.ui.theme.b4
import com.example.myapplication.ui.theme.b5
import getStatusColor
import getStatusText

import kotlinx.serialization.json.Json

@Composable
fun BloodSugarResultScreen(
    navController: NavController,
) {
    // LocalBackStackEntry를 사용하여 현재 백스택 엔트리 가져오기
    val backStackEntry = navController.currentBackStackEntry

    // SavedStateHandle에서 JSON 문자열 가져오기
    val jsonData :String= backStackEntry!!.savedStateHandle.get<String>("blood_sugar_data")!!

    // JSON을 BloodSugarData로 역직렬화
    val bloodSugarData: BloodSugarData = Json.decodeFromString<BloodSugarData>(jsonData)

    Column(
        modifier = Modifier
            .fillMaxSize().padding(horizontal = 40.dp)
    ) {
        // AppBar
        AppBar(
            leftButtonType = LeftButtonType.BACK,
            centerWidget = {
                Text(
                    text = "김영희 어르신",
                    style = MaterialTheme.typography.h1
                )
            },
        )

        // 측정 결과 카드
        ResultCard(bloodSugarData = bloodSugarData)

        Spacer(modifier = Modifier.height(23.dp))

        // 하단 버튼들
        BottomButtons(
            onViewHistory = {
                navController.navigate(Screen.BloodSugarHistory.route)
            },
            onMeasureOther = {
                navController.navigate(Screen.Main.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                    launchSingleTop = true
                    restoreState = false
                }
            }
        )
    }
}

@Composable
private fun ResultCard(bloodSugarData: BloodSugarData) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 46.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 측정값과 상태
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // 측정값
                Text(
                    text = "${bloodSugarData.glucoseResult}",
                    style = MaterialTheme.typography.b1
                )

                Spacer(modifier = Modifier.width(10.dp))


                    Text(
                        text = "mg/dL",
                        style = MaterialTheme.typography.b5
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    // 상태 배지
                   Chip(text = bloodSugarData.judgment!!.getStatusText(), color = bloodSugarData.judgment.getStatusColor())
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 설명 텍스트
            Text(
                text = getDescriptionText(bloodSugarData),
                style = MaterialTheme.typography.b4,
            )
        }
    }
}



@Composable
private fun BottomButtons(
    onViewHistory: () -> Unit,
    onMeasureOther: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(36.dp)
    ) {
        OutlinedButton(
            border = BorderStroke(6.dp, CustomColor.blue),
            onClick = { onViewHistory() },
            modifier = Modifier
                .weight(1f)
                .height(118.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(text = "이력보기", color = CustomColor.blue, style = MaterialTheme.typography.b4)
        }


        OutlinedButton(
            border = BorderStroke(6.dp, Stroke.black20),
            onClick = {
                onMeasureOther()
             },
            modifier = Modifier
                .weight(1f)
                .height(118.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CustomColor.blue)
        ) {
            Text(text = "다른 항목 측정", color = CustomColor.white, style = MaterialTheme.typography.b4)
        }
    }

}

private fun getDescriptionText(bloodSugarData: BloodSugarData): String {
    val mealTypeText = when (bloodSugarData.mealFlag) {
        MealType.BEFORE_MEAL -> "식사 전"
        MealType.AFTER_MEAL -> "식사 후"
        else -> ""
    }

    val statusText = when (bloodSugarData.judgment) {
        BloodSugarStatus.HIGH -> "높은 편이에요"
        BloodSugarStatus.LOW -> "낮은 편이에요"
        BloodSugarStatus.NORMAL -> "정상 범위에요"
        else -> "측정 결과입니다"
    }

    return "\"$mealTypeText 혈당 기준으로 보면 $statusText.\""
}

