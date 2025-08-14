package com.example.myapplication.page.measurement

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.manager.SelectedUserStore
import com.example.myapplication.navigation.LocalAppNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.model.MeasurementType
import com.example.myapplication.model.displayName
import com.example.myapplication.ui.theme.h1
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.measurement.MeasurementViewModel
import com.example.myapplication.viewmodel.measurement.MeasurementStage
import androidx.compose.ui.res.painterResource
import com.example.myapplication.R
import com.example.myapplication.components.AppBar
import com.example.myapplication.components.LeftButtonType
import com.example.myapplication.ui.theme.CustomColor
import com.example.myapplication.ui.theme.Stroke
import com.example.myapplication.ui.theme.b2
import com.example.myapplication.ui.theme.b3
import com.example.myapplication.ui.theme.b4

@Composable
fun MeasurementScreen(
    navController: NavController,
    type: MeasurementType,
    vm: MeasurementViewModel = viewModel()
) {
    val userName: String = SelectedUserStore.get()?.name ?: "사용자"
    val stage by vm.stage.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 공통 AppBar 사용
        AppBar(
            leftButtonType = LeftButtonType.HOME,
            centerWidget = {
                Text(
                    text = "$userName 어르신",
                    style = MaterialTheme.typography.h1
                )
            }
        )


        when (stage) {
            MeasurementStage.Waiting,    MeasurementStage.Measuring -> {
                ProcessCard(
                    type = type,
                    stage = stage
                )
            }
            MeasurementStage.Completed -> {
                CompletedStateCard()
            }
            MeasurementStage.Error -> {
                ErrorStateCard(onRetry = { vm.triggerRetry() })
            }
        }
    }
}

@Composable
private fun ProcessCard(
    type: MeasurementType,
    stage: MeasurementStage
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp,top =20.dp, end = 40.dp, bottom = 50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 왼쪽: 텍스트와 로딩 영역
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. 메인 텍스트
            val mainText = when (stage) {
                MeasurementStage.Waiting -> "${type.displayName()}측정을\n시작해보세요!"
                MeasurementStage.Measuring -> "${type.displayName()}을\n측정중입니다."
                else -> ""
            }
            Text(
                text = mainText,
                style = MaterialTheme.typography.b2,
            )



            // 2. 로딩 영역
            when (stage) {
                MeasurementStage.Waiting -> {
                    // 대기중: 원형 로딩 + "측정대기중" 텍스트
                    Spacer(modifier = Modifier.height(35.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(34.dp),
                            color = CustomColor.blue,
                            strokeWidth = 6.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "측정대기중",
                            style = MaterialTheme.typography.b4,
                            color = CustomColor.blue
                        )
                    }
                }
                MeasurementStage.Measuring -> {
                    // 측정중: 점 3개 애니메이션
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(3) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(8.dp),
                                color = CustomColor.blue,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
                else -> { /* 처리하지 않음 */ }
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        // 오른쪽: 측정 타입별 이미지
        val imageResId = when (type) {
            MeasurementType.BloodSugar -> R.drawable.measurement_sugar
            MeasurementType.BloodPressure -> R.drawable.measurement_pressure
            MeasurementType.Weight -> R.drawable.measurement_weight
        }

        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "${type.displayName()} 측정 기기 이미지",
        )
    }
}

@Composable
private fun CompletedStateCard() {
    // 3초뒤 결과 페이지 이동(페이지 스택 전부 삭제)
    LaunchedEffect(Unit) {
        // delay(3000)
        // navController.navigate("result")
    }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           Image(
                painter = painterResource(id = R.drawable.success),
                contentDescription = "완료 이미지",
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "측정이 완료되었어요!",
                style = MaterialTheme.typography.b3,
                textAlign = TextAlign.Center
            )
        }

}

@Composable
private fun ErrorStateCard(onRetry: () -> Unit) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           Image(
                painter = painterResource(id = R.drawable.caution),
                contentDescription = "에러 이미지",
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "측정이 중단되었어요",
                style = MaterialTheme.typography.b3,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "다시 한 번 천천히 측정해볼까요?",
                    style = MaterialTheme.typography.b4,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onRetry,
                modifier = Modifier
                    .width(329.dp)
                    .height(118.dp),
                border = BorderStroke(6.dp, Stroke.black20),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomColor.blue,
                ),

                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    text = "다시시도",
                    color = CustomColor.white,
                    style = MaterialTheme.typography.b4
                )
            }
        }

}

@Preview(showBackground = true)
@Composable
private fun MeasurementScreenPreview_BloodSugar() {
    val nav = rememberNavController()
    CompositionLocalProvider(LocalAppNavController provides nav) {
        MeasurementScreen(nav, MeasurementType.BloodSugar)
    }
}

@Preview(showBackground = true)
@Composable
private fun MeasurementScreenPreview_BloodPressure() {
    val nav = rememberNavController()
    CompositionLocalProvider(LocalAppNavController provides nav) {
        MeasurementScreen(nav, MeasurementType.BloodPressure)
    }
}

@Preview(showBackground = true)
@Composable
private fun MeasurementScreenPreview_Weight() {
    val nav = rememberNavController()
    CompositionLocalProvider(LocalAppNavController provides nav) {
        MeasurementScreen(nav, MeasurementType.Weight)
    }
}
