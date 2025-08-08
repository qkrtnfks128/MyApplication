package com.example.myapplication.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.components.AppBar
import com.example.myapplication.components.LeftButtonType
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.manager.UserManager
import com.example.myapplication.utils.LogManager



// MainScreen은 메인 화면으로, 혈당, 혈압, 체중 측정 버튼을 포함합니다.
// 각 버튼은 클릭 시 로그인화면 또는 해당 측정 화면으로 이동합니다.
private const val MAIN_SCREEN_TAG: String = "MainScreen"
@Composable
fun MainScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // AppBar
        AppBar(
            leftButtonType = LeftButtonType.NONE,
            centerWidget = {
                Box(
                    modifier = Modifier
                        .clickable { /* 클릭 시 동작 추가 가능 */
                            LogManager.userAction(MAIN_SCREEN_TAG, "메인 앱바 중앙 텍스트 클릭")
                            // 기관 선택이 되어있는지 확인
                            //
                        }
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "오늘의 건강을 확인해볼까요?",
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
        )
        
        // 3개의 측정 버튼을 가로로 배치
        Row(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(start = 40.dp, top = 10.dp, end = 40.dp, bottom = 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 혈당 측정 버튼 (빨간색)
            MeasurementButton(
                title = "혈당",
                subtitle = "측정",
                backgroundColor = Color(0xFFE53E3E), // 빨간색
                onClick = {
                    LogManager.userAction(MAIN_SCREEN_TAG, "혈당 측정 버튼 클릭")
                    LogManager.navigation(MAIN_SCREEN_TAG, "MainScreen", "BloodSugarHistory")
                    navController.navigate(Screen.BloodSugarHistory.route)
                },
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(21.dp))
            
            // 혈압 측정 버튼 (주황색)
            MeasurementButton(
                title = "혈압",
                subtitle = "측정",
                backgroundColor = Color(0xFFED8936), // 주황색
                onClick = { /* 혈압 측정 로직 */ 
                    LogManager.userAction(MAIN_SCREEN_TAG, "혈압 측정 버튼 클릭")
                    if (!com.example.myapplication.manager.UserManager.isLoggedIn()) {
                        LogManager.navigation(MAIN_SCREEN_TAG, "MainScreen", "Login")
                        navController.navigate(Screen.Login.route)
                        return@MeasurementButton
                    }},
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(21.dp))
            
            // 체중 측정 버튼 (초록색)
            MeasurementButton(
                title = "체중",
                subtitle = "측정",
                backgroundColor = Color(0xFF38A169), // 초록색
                onClick = { /* 체중 측정 로직 */ 
                    LogManager.userAction(MAIN_SCREEN_TAG, "체중 측정 버튼 클릭")
                   },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MeasurementButton(
    title: String,
    subtitle: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 80.sp,
                lineHeight = 104.sp,
                fontFamily = FontFamily(Font(R.font.pretendard)),
                fontWeight = FontWeight(600),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 80.sp,
                lineHeight = 104.sp,
                fontFamily = FontFamily(Font(R.font.pretendard)),
                fontWeight = FontWeight(600),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyApplicationTheme {
        MainScreen(rememberNavController())
    }
} 