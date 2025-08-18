package com.example.myapplication.page.history

import WeightStatus
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.components.AppBar
import com.example.myapplication.components.LeftButtonType
import com.example.myapplication.manager.SelectedUserStore
import com.example.myapplication.ui.components.list.HistoryRow
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.R
import com.example.myapplication.model.WeightData
import com.example.myapplication.navigation.LocalAppNavController
import com.example.myapplication.ui.components.Chip
import com.example.myapplication.ui.components.CustomToast
import com.example.myapplication.ui.theme.b1
import com.example.myapplication.ui.theme.b4
import com.example.myapplication.ui.theme.b5
import com.example.myapplication.viewmodel.history.WeightHistoryViewModel
import getStatusColor
import getStatusText

// WeightHistoryScreen은 체중 기록 내역 화면으로, 체중 기록 내역을 표시합니다.
// 체중 기록 내역은 시간순으로 정렬되며, 각 기록은 체중, BMI, 체지방률, 근육량 정보를 포함합니다.

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun WeightHistoryScreen(
    navController: NavController,
    viewModel: WeightHistoryViewModel = viewModel()
) {
    // 현재 선택된 사용자 정보 가져오기
      val userName: String = SelectedUserStore.get()?.name ?: "사용자"
    val context = LocalContext.current

    // ViewModel에서 상태 수집
    val historyItems by viewModel.historyItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()




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
                        text = "$userName",
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

        // 체중 기록 리스트
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(historyItems.list) { weightData ->
                HistoryRow(
                    timeString = "${weightData.date}${weightData.time}",
                    rightWidget = {
                        WeightInfoWidget(weightData)
                    }
                )
            }

            // 하단 로딩 표시
            if (isLoading) {
                item {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun WeightInfoWidget(weightData: WeightData) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // 체중 정보
        Text(
            text = "${String.format("%.1f", weightData.scale)}",
            style = MaterialTheme.typography.b1
        )
        Text(
            text = "kg",
            style = MaterialTheme.typography.b5
        )
        // 상태 라벨
        Chip(text = weightData.judgment!!.getStatusText(), color = weightData.judgment.getStatusColor())
    }
}

@Preview(showBackground = true)
@Composable
fun WeightHistoryScreenPreview() {
    MyApplicationTheme {
        val nav = rememberNavController()
        // 미리보기용 ViewModel 생성
        val viewModel : WeightHistoryViewModel = viewModel()

        CompositionLocalProvider(LocalAppNavController provides nav) {
            WeightHistoryScreen(nav, viewModel)
        }
    }
}
