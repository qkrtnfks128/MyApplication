package com.example.myapplication.page.history

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
import androidx.compose.foundation.verticalScroll
import androidx.navigation.NavController
import com.example.myapplication.model.BloodSugarData
import com.example.myapplication.model.BloodSugarStatus
import com.example.myapplication.model.MealType
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
                    timeString = bloodSugarData.measurementTime,
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
            text = "${getMealTypeText(bloodSugarData.mealType)} ${bloodSugarData.bloodSugarLevel} mg/dL",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 상태 라벨
        StatusLabel(bloodSugarData.status)
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
            id = "2",
            measurementTime = "20240805102000", // 08월 05일 오전 10:20
            bloodSugarLevel = 180,
            mealType = MealType.AFTER_MEAL,
            status = BloodSugarStatus.NORMAL
        ),
        BloodSugarData(
            id = "1",
            measurementTime = "20240806091100", // 08월 06일 오전 9:11
            bloodSugarLevel = 235,
            mealType = MealType.BEFORE_MEAL,
            status = BloodSugarStatus.HIGH
        ),
        BloodSugarData(
            id = "2",
            measurementTime = "20240805102000", // 08월 05일 오전 10:20
            bloodSugarLevel = 180,
            mealType = MealType.AFTER_MEAL,
            status = BloodSugarStatus.NORMAL
        ),
        BloodSugarData(
            id = "1",
            measurementTime = "20240806091100", // 08월 06일 오전 9:11
            bloodSugarLevel = 235,
            mealType = MealType.BEFORE_MEAL,
            status = BloodSugarStatus.HIGH
        ),
        BloodSugarData(
            id = "2",
            measurementTime = "20240805102000", // 08월 05일 오전 10:20
            bloodSugarLevel = 180,
            mealType = MealType.AFTER_MEAL,
            status = BloodSugarStatus.NORMAL
        ),
        BloodSugarData(
            id = "1",
            measurementTime = "20240806091100", // 08월 06일 오전 9:11
            bloodSugarLevel = 235,
            mealType = MealType.BEFORE_MEAL,
            status = BloodSugarStatus.HIGH
        ),
        BloodSugarData(
            id = "2",
            measurementTime = "20240805102000", // 08월 05일 오전 10:20
            bloodSugarLevel = 180,
            mealType = MealType.AFTER_MEAL,
            status = BloodSugarStatus.NORMAL
        ),
        BloodSugarData(
            id = "1",
            measurementTime = "20240806091100", // 08월 06일 오전 9:11
            bloodSugarLevel = 235,
            mealType = MealType.BEFORE_MEAL,
            status = BloodSugarStatus.HIGH
        ),
        BloodSugarData(
            id = "2",
            measurementTime = "20240805102000", // 08월 05일 오전 10:20
            bloodSugarLevel = 180,
            mealType = MealType.AFTER_MEAL,
            status = BloodSugarStatus.NORMAL
        ),
        BloodSugarData(
            id = "1",
            measurementTime = "20240806091100", // 08월 06일 오전 9:11
            bloodSugarLevel = 235,
            mealType = MealType.BEFORE_MEAL,
            status = BloodSugarStatus.HIGH
        ),
        BloodSugarData(
            id = "2",
            measurementTime = "20240805102000", // 08월 05일 오전 10:20
            bloodSugarLevel = 180,
            mealType = MealType.AFTER_MEAL,
            status = BloodSugarStatus.NORMAL
        ),
        BloodSugarData(
            id = "1",
            measurementTime = "20240806091100", // 08월 06일 오전 9:11
            bloodSugarLevel = 235,
            mealType = MealType.BEFORE_MEAL,
            status = BloodSugarStatus.HIGH
        ),
        BloodSugarData(
            id = "2",
            measurementTime = "20240805102000", // 08월 05일 오전 10:20
            bloodSugarLevel = 180,
            mealType = MealType.AFTER_MEAL,
            status = BloodSugarStatus.NORMAL
        ),
        BloodSugarData(
            id = "1",
            measurementTime = "20240806091100", // 08월 06일 오전 9:11
            bloodSugarLevel = 235,
            mealType = MealType.BEFORE_MEAL,
            status = BloodSugarStatus.HIGH
        ),
        BloodSugarData(
            id = "3",
            measurementTime = "20240804174000", // 08월 04일 오후 5:40
            bloodSugarLevel = 75,
            mealType = MealType.AFTER_MEAL,
            status = BloodSugarStatus.LOW
        )
    )
}

@Preview(showBackground = true)
@Composable
fun BloodSugarHistoryScreenPreview() {
    MyApplicationTheme {
        BloodSugarHistoryScreen(rememberNavController())
    }
} 