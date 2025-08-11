package com.example.myapplication.page.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.myapplication.components.AppBar
import com.example.myapplication.components.LeftButtonType
import com.example.myapplication.model.UserListItem
import com.example.myapplication.model.UserListResult
import com.example.myapplication.navigation.LocalAppNavController
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.login.UserResultUiState
import com.example.myapplication.viewmodel.login.UserResultViewModel


@Composable
fun UserResultScreen(
    navController: NavController,
    result: UserListResult,
) {
    val context = LocalContext.current
    val vm: UserResultViewModel = viewModel()
    val state = remember(result) { UserResultUiState(items = result.items) }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar(
            leftButtonType = LeftButtonType.BACK,
            centerWidget = { Text(text = "사용자 인증", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (state.items.size) {
            0 -> EmptyResultSection()
            1 -> SingleConfirmSection(
                item = state.items.first(),
                onNo = { navController.popBackStack() },
                onYes = {
                    val type = vm.saveUserDataAndGetMeasurementType(state.items.first())
                    if (type != null) {
                        navController.navigate(
                            Screen.Measurement.route + "/" + type.name
                        ){
                          popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
    launchSingleTop = true
    restoreState = false
                        }
                    } else {
                        Toast.makeText(context, "측정 항목이 선택되지 않았습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            else -> MultiResultSection(items = state.items, onSelect = { 
                val type = vm.saveUserDataAndGetMeasurementType(it)
                if (type != null) {
                    navController.navigate(
                        Screen.Measurement.route + "/" + type.name
                    ){
                          popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
    launchSingleTop = true
    restoreState = false
                    }
                } else {
                    Toast.makeText(context, "측정 항목이 선택되지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

@Composable
private fun EmptyResultSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "일치하는 사용자가 없습니다.", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "휴대전화 번호를 다시 확인해 주세요.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}

@Composable
private fun SingleConfirmSection(item: UserListItem, onNo: () -> Unit, onYes: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 이미지 영역 플레이스홀더
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFEFF3F8)
        ) {}

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "${item.name} 어르신이 맞으신가요?",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            OutlinedButton(
                onClick = { onNo() },
                modifier = Modifier
                    .weight(1f)
                    .height(96.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = "아니오", color = Color(0xFF1976D2), style = MaterialTheme.typography.headlineMedium)
            }

            Button(
                onClick = { onYes() },
                modifier = Modifier
                    .weight(1f)
                    .height(96.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text(text = "네", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}

@Composable
private fun MultiResultSection(items: List<UserListItem>, onSelect: (UserListItem) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "같은 번호로 등록된 분이 여러 명 있어요.\n본인을 선택해 주세요",
            style = MaterialTheme.typography.headlineSmall,
            lineHeight = 36.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                UserRow(item = item, isHighlighted = false) { onSelect(item) }
            }
        }
    }
}

@Composable
private fun UserRow(item: UserListItem, isHighlighted: Boolean, onClick: () -> Unit) {
    val borderColor: Color = if (isHighlighted) Color(0xFF1E88E5) else Color(0xFF90CAF9)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(width = 3.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = item.name, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = item.registrationDate, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserResultScreenPreviewEmpty() {
    val nav = rememberNavController()
    val result = UserListResult(statusCode = 0, items = emptyList())
    CompositionLocalProvider(LocalAppNavController provides nav) {
        UserResultScreen(navController = nav, result = result)
    }
}

@Preview(showBackground = true)
@Composable
private fun UserResultScreenPreviewSingle() {
    val nav = rememberNavController()
    val result = UserListResult(
        statusCode = 0,
        items = listOf(
            UserListItem(
                name = "김영희",
                cvid = "CVID-001",
                imageUrl = "",
                parentTel = "01012345678",
                registrationDate = "1954년 01월 18일"
            )
        )
    )
    CompositionLocalProvider(LocalAppNavController provides nav) {
        UserResultScreen(navController = nav, result = result)
    }
}

@Preview(showBackground = true)
@Composable
private fun UserResultScreenPreviewMulti() {
    val nav = rememberNavController()
    val result = UserListResult(
        statusCode = 0,
        items = listOf(
            UserListItem("김영희", "CVID-001", "", "01011112222", "1954년 01월 18일"),
            UserListItem("박철수", "CVID-002", "", "01033334444", "1960년 03월 01일")
        )
    )
    CompositionLocalProvider(LocalAppNavController provides nav) {
        UserResultScreen(navController = nav, result = result)
    }
}


