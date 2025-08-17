package com.example.myapplication.page.history


import BloodSugarStatus
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import com.example.myapplication.components.AppBar
import com.example.myapplication.components.LeftButtonType
import com.example.myapplication.ui.components.list.HistoryRow
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.R
import com.example.myapplication.model.BloodSugarData
import com.example.myapplication.model.MealType
import com.example.myapplication.navigation.LocalAppNavController


// BloodSugarHistoryScreen은 혈당 기록 내역 화면으로, 혈당 기록 내역을 표시합니다.
// 혈당 기록 내역은 시간순으로 정렬되며, 각 기록은 혈당 수치와 식전/식후 정보를 포함합니다.

@Composable
fun BloodSugarHistoryScreen(navController: NavController) {
    val testData = createTestData()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // AppBar
        AppBar(
            leftButtonType = LeftButtonType.BACK,
            centerWidget = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                        Text(
                            text = "김영희",

                            // Headline/H2_B
                            style = TextStyle(
                              fontSize = 50.sp,
                              lineHeight = 65.sp,
                              fontFamily = FontFamily(Font(R.font.pretendard)),
                              fontWeight = FontWeight(700),
                              color = Color.Black,
                              textAlign = TextAlign.Center,
                            )
                          )

                    Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "어르신",
                            style = TextStyle(
                              fontSize = 50.sp,
                              lineHeight = 65.sp,
                              fontFamily = FontFamily(Font(R.font.pretendard)),
                              fontWeight = FontWeight(600),
                              color = Color.Black,
                              textAlign = TextAlign.Center,
                            )
                          )

                }
            },

        )
        val state = rememberScrollState()
        // 혈당 기록 리스트
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            items(testData) { bloodSugarData ->
                HistoryRow(
                    timeString = bloodSugarData.time,
                    rightWidget = {
                        BloodSugarInfoWidget(bloodSugarData)
                    }
                )
            }
        }
    }
}

@Composable
fun BloodSugarInfoWidget(bloodSugarData: BloodSugarData) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 혈당 수치와 식전/식후 정보
        Text(
            text = "${getMealTypeText(bloodSugarData.mealFlag as MealType)} mg/dL",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 상태 라벨
        StatusLabel(bloodSugarData.judgment as BloodSugarStatus)
    }
}

@Composable
fun StatusLabel(status: BloodSugarStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        BloodSugarStatus.HIGH -> Triple(Color(0xFFFF6B6B), Color.White, "높음")
        BloodSugarStatus.NORMAL -> Triple(Color(0xFF4CAF50), Color.White, "보통")
        BloodSugarStatus.LOW -> Triple(Color(0xFFFF9800), Color.White, "낮음")

    }


    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

private fun getMealTypeText(mealType: MealType): String {
    return when (mealType) {
        MealType.BEFORE_MEAL -> "식전"
        MealType.AFTER_MEAL -> "식후"
    }
}

private fun createTestData(): List<BloodSugarData> {
    return listOf(
        BloodSugarData(
        date = "20240805",
        time = "102000",
        glucoseResult = 180,
        temperature = 36.5f,
            mealFlag = MealType.AFTER_MEAL,
        judgment = BloodSugarStatus.NORMAL as BloodSugarStatus
    ),
    BloodSugarData(
        date = "20240806",
        time = "091100",
        glucoseResult = 235,
        temperature = 36.8f,
        mealFlag = MealType.BEFORE_MEAL,
        judgment = BloodSugarStatus.HIGH as BloodSugarStatus
    ),
    BloodSugarData(
        date = "20240804",
        time = "174000",
        glucoseResult = 75,
        temperature = 36.2f,
        mealFlag = MealType.AFTER_MEAL,
        judgment = BloodSugarStatus.LOW as BloodSugarStatus
    ),
    BloodSugarData(
        date = "20240803",
        time = "120000",
        glucoseResult = 120,
        temperature = 36.6f,
        mealFlag = MealType.BEFORE_MEAL,
        judgment = BloodSugarStatus.NORMAL as BloodSugarStatus
    ),
    BloodSugarData(
        date = "20240802",
        time = "183000",
        glucoseResult = 200,
        temperature = 37.1f,
        mealFlag = MealType.AFTER_MEAL,
        judgment = BloodSugarStatus.HIGH as BloodSugarStatus
    ),
    BloodSugarData(
        date = "20240801",
        time = "080000",
        glucoseResult = 95,
        temperature = 36.4f,
        mealFlag = MealType.BEFORE_MEAL,
        judgment = BloodSugarStatus.NORMAL as BloodSugarStatus
    ),
    BloodSugarData(
        date = "20240731",
        time = "193000",
        glucoseResult = 160,
        temperature = 36.9f,
        mealFlag = MealType.AFTER_MEAL,
        judgment = BloodSugarStatus.NORMAL as BloodSugarStatus
    ),
    BloodSugarData(
        date = "20240730",
        time = "073000",
        glucoseResult = 110,
        temperature = 36.3f,
        mealFlag = MealType.BEFORE_MEAL,
        judgment = BloodSugarStatus.NORMAL as BloodSugarStatus
    ),
    BloodSugarData(
        date = "20240729",
        time = "140000",
        glucoseResult = 280,
        temperature = 37.2f,
        mealFlag = MealType.AFTER_MEAL,
        judgment = BloodSugarStatus.HIGH as BloodSugarStatus
    ),
    BloodSugarData(
        date = "20240728",
        time = "090000",
        glucoseResult = 85,
        temperature = 36.1f,
        mealFlag = MealType.BEFORE_MEAL,
        judgment = BloodSugarStatus.NORMAL as BloodSugarStatus
    )
    )
}

@Preview(showBackground = true)
@Composable
fun BloodSugarHistoryScreenPreview() {
    MyApplicationTheme {
        val nav = rememberNavController()
        CompositionLocalProvider(LocalAppNavController provides nav) {
            BloodSugarHistoryScreen(nav)
        }
    }
}
