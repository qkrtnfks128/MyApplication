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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import com.example.myapplication.ui.theme.CustomColor
import com.example.myapplication.ui.theme.Stroke
import com.example.myapplication.ui.theme.b3
import com.example.myapplication.ui.theme.h1
import com.example.myapplication.R
import com.example.myapplication.ui.theme.b4
import com.example.myapplication.navigation.LocalAppNavController


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
            centerWidget = { Text(text = "사용자 인증", style = MaterialTheme.typography.h1) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (state.items.size) {
            0 -> EmptyResultSection()
            1 -> SingleConfirmSection(
                item = state.items.first(),
            )
            else -> MultiResultSection(items = state.items,)
        }
    }
}


// 일치번호 없음
@Composable
private fun EmptyResultSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 94.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 이미지 영역 플레이스홀더
        Image(
            painter = painterResource(id = R.drawable.x),
            contentDescription = "일치번호 없음",
        )
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "입력하신 번호와 일치하는 분이 없어요.\n다시 한번 확인해 주세요!",
            style = MaterialTheme.typography.b4
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            // OutlinedButton(
            //     border = BorderStroke(6.dp, CustomColor.blue),
            //     onClick = { onNo() },
            //     modifier = Modifier
            //         .weight(1f)
            //         .height(118.dp),
            //     shape = RoundedCornerShape(30.dp)
            // ) {
            //     Text(text = "아니오", color = CustomColor.blue, style = MaterialTheme.typography.b4)
            // }

            val nav  =  LocalAppNavController.current
            OutlinedButton(
                border = BorderStroke(6.dp, Stroke.black20),
                onClick = {
                    nav.popBackStack()
                 },
                modifier = Modifier
                    .weight(1f)
                    .height(118.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CustomColor.blue)
            ) {
                Text(text = "다시 입력하기", color = CustomColor.white, style = MaterialTheme.typography.b4)
            }
        }
    }
}


// 한 사용자 결과 표시
@Composable
private fun SingleConfirmSection(item: UserListItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 94.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 이미지 영역 플레이스홀더
        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = "환영 이미지",
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "${item.name} 어르신이 맞으신가요?",
            style = MaterialTheme.typography.b3
        )

        Spacer(modifier = Modifier.height(20.dp))

        val nav  =  LocalAppNavController.current
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            OutlinedButton(
                border = BorderStroke(6.dp, CustomColor.blue),
                onClick = { nav.popBackStack() },
                modifier = Modifier
                    .weight(1f)
                    .height(118.dp),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(text = "아니오", color = CustomColor.blue, style = MaterialTheme.typography.b4)
            }


            val vm: UserResultViewModel = viewModel()
            OutlinedButton(
                border = BorderStroke(6.dp, Stroke.black20),
                onClick = {
                    val type = vm.saveUserDataAndGetMeasurementType(item)
                    if (type != null) {
                        nav.navigate(
                            Screen.Measurement.route + "/" + type.name
                        ){
                          popUpTo(nav.graph.id) { inclusive = true }
    launchSingleTop = true
    restoreState = false
                        }
                    } else {
                        nav.navigate(Screen.Main.route){
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                 },
                modifier = Modifier
                    .weight(1f)
                    .height(118.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CustomColor.blue)
            ) {
                Text(text = "네", color = CustomColor.white, style = MaterialTheme.typography.b4)
            }
        }
    }
}


// 여러 사용자 결과 표시
@Composable
private fun MultiResultSection(items: List<UserListItem>,) {
    val vm: UserResultViewModel = viewModel()
    val nav  =  LocalAppNavController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "본인을 선택해 주세요",
            style = MaterialTheme.typography.b4,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(19.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            items(items) { item ->
                UserRow(item = item,) {
                    val type = vm.saveUserDataAndGetMeasurementType(item)
                    if (type != null) {
                        nav.navigate(
                            Screen.Measurement.route + "/" + type.name
                        ){
                          popUpTo(nav.graph.id) { inclusive = true }
    launchSingleTop = true
    restoreState = false
                        }
                    } else {
                        nav.navigate(Screen.Main.route){
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                 }

            }
        }
    }
}

@Composable
private fun UserRow(item: UserListItem,  onClick: () -> Unit) {

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
                .border(width = 3.dp, color = CustomColor.blue, shape = RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = item.name, style = MaterialTheme.typography.b3)

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


