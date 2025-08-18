package com.example.myapplication.page.measurement

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
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
import com.example.myapplication.model.BloodSugarData
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.CustomColor
import com.example.myapplication.ui.theme.Stroke
import com.example.myapplication.ui.theme.b1
import com.example.myapplication.ui.theme.b2
import com.example.myapplication.ui.theme.b3
import com.example.myapplication.ui.theme.b4
import com.example.myapplication.ui.theme.b5
import kotlinx.coroutines.delay
import com.example.myapplication.viewmodel.measurement.MeasurementViewModelFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.ui.components.CustomToast
import com.example.myapplication.viewmodel.measurement.MeasurementEvent
import kotlinx.coroutines.flow.collectLatest
import com.example.myapplication.ui.components.Chip
import getStatusColor
import getStatusText
import com.example.myapplication.model.BloodPressureData
import com.example.myapplication.model.WeightData
import displayName
import getDescriptionText

@Composable
fun MeasurementScreen(
    navController: NavController,
    type: MeasurementType,
    vm: MeasurementViewModel = viewModel(
        factory = MeasurementViewModelFactory(type)
    )
) {
    val context = LocalContext.current
    val userName: String = SelectedUserStore.get()?.name ?: "사용자"
    val stage by vm.stage.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            MeasurementStage.Waiting, MeasurementStage.Measuring -> {
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
            MeasurementStage.ShowingResult -> {
                when (type) {
                    MeasurementType.BloodSugar -> {
                        val bloodSugarData by vm.bloodSugarData.collectAsState()
                        bloodSugarData?.let {
                            BloodSugarResultCard(bloodSugarData = it, navController = navController)
                        }
                    }
                    MeasurementType.BloodPressure -> {
                        val bloodPressureData by vm.bloodPressureData.collectAsState()
                        bloodPressureData?.let {
                            BloodPressureResultCard(bloodPressureData = it, navController = navController)
                        }
                    }
                    MeasurementType.Weight -> {
                        val weightData by vm.weightData.collectAsState()
                        weightData?.let {
                            WeightResultCard(weightData = it, navController = navController)
                        }
                    }
                }
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
                    CircularProgressIndicator(
                        modifier = Modifier.size(34.dp),
                        color = CustomColor.blue,
                        strokeWidth = 6.dp
                    )
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
    // 측정 완료 화면 (3초 후 자동으로 결과 화면으로 전환)
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

// 혈당 결과 화면
@Composable
private fun BloodSugarResultCard(
    bloodSugarData: BloodSugarData,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize().padding(horizontal = 40.dp)
    ) {

        // 측정 결과 카드
        Card(
            modifier = Modifier
                .fillMaxWidth().border(6.dp, CustomColor.gray06, RoundedCornerShape(10.dp)),
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 측정값
                    Text(
                        text = "${bloodSugarData.mealFlag!!.displayName()}",
                        style = MaterialTheme.typography.b4
                    )
                    // 측정값
                    Text(
                        text = "${bloodSugarData.glucoseResult}",
                        style = MaterialTheme.typography.b1
                    )


                    Text(
                        text = "mg/dL",
                        style = MaterialTheme.typography.b5
                    )

                    // 상태 배지
                    bloodSugarData.judgment?.let { judgment ->
                        Chip(text = judgment.getStatusText(), color = judgment.getStatusColor())
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // 설명 텍스트
                Text(
                    text = bloodSugarData.judgment?.getDescriptionText(bloodSugarData.mealFlag!!) ?: "",
                    style = MaterialTheme.typography.b4,
                )
            }
        }

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

// 혈압 결과 화면
@Composable
private fun BloodPressureResultCard(
    bloodPressureData: BloodPressureData,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize().padding(horizontal = 40.dp)
    ) {

        // 측정 결과 카드
        Card(
            modifier = Modifier
                .fillMaxWidth().border(6.dp, CustomColor.gray06, RoundedCornerShape(10.dp)),
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 측정값
                    Text(
                        text = "${bloodPressureData.systolic}/${bloodPressureData.diastolic}",
                        style = MaterialTheme.typography.b1
                    )

                    Text(
                        text = "mmHg",
                        style = MaterialTheme.typography.b5
                    )
                    // 상태 배지
                    bloodPressureData.judgment?.let { judgment ->
                        Chip(text = "정상", color = CustomColor.blue)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // 설명 텍스트
                Text(
                    text = bloodPressureData.judgment?.getDescriptionText() ?: "",
                    style = MaterialTheme.typography.b4,
                )
            }
        }

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

// 체중 결과 화면
@Composable
private fun WeightResultCard(
    weightData: WeightData,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize().padding(horizontal = 40.dp)
    ) {

        // 측정 결과 카드
        Card(
            modifier = Modifier
                .fillMaxWidth().border(6.dp, CustomColor.gray06, RoundedCornerShape(10.dp)),
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 측정값
                    Text(
                        text = "${weightData.scale}",
                        style = MaterialTheme.typography.b1
                    )

                    Text(
                        text = "kg",
                        style = MaterialTheme.typography.b5
                    )
                    // 상태 배지
                    weightData.judgment?.let { judgment ->
                        Chip(text = "정상", color = CustomColor.blue)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // 설명 텍스트
                Text(
                    text = weightData.judgment?.getDescriptionText() ?: "",
                    style = MaterialTheme.typography.b4,
                )
            }
        }

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
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "History List",
                tint = CustomColor.blue,
                modifier = Modifier.size(42.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
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


@Preview(showBackground = true)
@Composable
private fun MeasurementScreenPreview_BloodSugar_Waiting() {
    val nav = rememberNavController()
    CompositionLocalProvider(LocalAppNavController provides nav) {
        MeasurementScreen(nav, MeasurementType.BloodSugar, )
    }
}

@Preview(showBackground = true)
@Composable
private fun MeasurementScreenPreview_BloodSugar_Result() {
    val nav = rememberNavController()
    val mockViewModel: MeasurementViewModelFactory = MeasurementViewModelFactory(MeasurementType.BloodSugar)
    // 상태를 결과 화면으로 설정

    CompositionLocalProvider(LocalAppNavController provides nav) {
        MeasurementScreen(nav, MeasurementType.BloodSugar, )
    }
}

@Preview(showBackground = true)
@Composable
private fun MeasurementScreenPreview_BloodPressure() {
    val nav = rememberNavController()
    val mockViewModel = MeasurementViewModelFactory(MeasurementType.BloodPressure)
    CompositionLocalProvider(LocalAppNavController provides nav) {
        MeasurementScreen(nav, MeasurementType.BloodPressure, )
    }
}

@Preview(showBackground = true)
@Composable
private fun MeasurementScreenPreview_Weight() {
    val nav = rememberNavController()
    val mockViewModel = MeasurementViewModelFactory(MeasurementType.Weight)
    CompositionLocalProvider(LocalAppNavController provides nav) {
        MeasurementScreen(nav, MeasurementType.Weight, )
    }
}
