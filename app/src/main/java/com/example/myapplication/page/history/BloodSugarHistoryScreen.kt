package com.example.myapplication.page.history


import BloodSugarStatus
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
import com.example.myapplication.model.BloodSugarData
import com.example.myapplication.navigation.LocalAppNavController
import com.example.myapplication.ui.components.Chip
import com.example.myapplication.ui.components.CustomToast
import com.example.myapplication.ui.theme.b1
import com.example.myapplication.ui.theme.b4
import com.example.myapplication.ui.theme.b5
import com.example.myapplication.viewmodel.history.BloodSugarHistoryViewModel
import displayName
import getStatusColor
import getStatusText


// BloodSugarHistoryScreen은 혈당 기록 내역 화면으로, 혈당 기록 내역을 표시합니다.
// 혈당 기록 내역은 시간순으로 정렬되며, 각 기록은 혈당 수치와 식전/식후 정보를 포함합니다.

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BloodSugarHistoryScreen(
    navController: NavController,
    viewModel: BloodSugarHistoryViewModel = viewModel()
) {
    // 현재 선택된 사용자 정보 가져오기
    val selectedUser = SelectedUserStore.get()
    val context = LocalContext.current


    // 당겨서 새로고침 상태
    val isRefreshing = remember { mutableStateOf(false) }
//    val pullRefreshState = rememberPullRefreshState(
//        refreshing = isRefreshing.value,
//        onRefresh = {
//            isRefreshing.value = true
//            viewModel.refresh()
//            isRefreshing.value = false
//        }
//    )

    // LazyList 상태 - 스크롤 위치 감지용
    val listState = rememberLazyListState()


    // 스크롤 감지하여 페이징 처리
    LaunchedEffect(listState) {
        // 스크롤이 끝에 도달하면 다음 페이지 로드
//        if (!pagingState.isLoading && !pagingState.isLastPage &&
//            listState.layoutInfo.visibleItemsInfo.isNotEmpty() &&
//            listState.layoutInfo.visibleItemsInfo.last().index >= historyItems.size - 3) {
//            viewModel.loadNextPage()
//        }
    }



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

        // 혈당 기록 리스트


            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(viewModel.historyItems.value.list) { bloodSugarData ->
                    HistoryRow(
                        timeString = "${bloodSugarData.date}${bloodSugarData.time}",
                        rightWidget = {
                            BloodSugarInfoWidget(bloodSugarData)
                        }
                    )
                }

                // 하단 로딩 표시
                if (viewModel.isLoading.value) {
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
fun BloodSugarInfoWidget(bloodSugarData: BloodSugarData) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // 혈당 수치와 식전/식후 정보
        Text(
            text = "${bloodSugarData.mealFlag!!.displayName()}",
            style = MaterialTheme.typography.b4
        )
        Text(
            text = "${bloodSugarData.glucoseResult}",
            style = MaterialTheme.typography.b1
        )
        Text(
            text = "mg/dL",
            style = MaterialTheme.typography.b5
        )


        // 상태 라벨
        Chip(text = bloodSugarData.judgment!!.getStatusText(), color = bloodSugarData.judgment.getStatusColor())
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



// 테스트 데이터는 ViewModel로 이동

@Preview(showBackground = true)
@Composable
fun BloodSugarHistoryScreenPreview() {
    MyApplicationTheme {
        val nav = rememberNavController()
        // 미리보기용 ViewModel 생성
        val viewModel : BloodSugarHistoryViewModel = viewModel()

        CompositionLocalProvider(LocalAppNavController provides nav) {
            BloodSugarHistoryScreen(nav, viewModel)
        }
    }
}
