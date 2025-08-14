package com.example.myapplication.page.error

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.components.AppBar
import com.example.myapplication.components.LeftButtonType
import com.example.myapplication.navigation.LocalAppNavController
import com.example.myapplication.ui.theme.CustomColor
import com.example.myapplication.ui.theme.Stroke
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun ErrorScreen(
    navController: NavController,
    errorMessage: String = "네트워크 오류가 발생했습니다.",
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar(
            leftButtonType = LeftButtonType.BACK,
        )

        Spacer(modifier = Modifier.height(80.dp))

        // 에러 아이콘
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "에러",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 에러 메시지
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))


        // 뒤로가기
        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .width(200.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomColor.bg,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, Stroke.black20)
        ) {
            Text("뒤로가기")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    MyApplicationTheme {
        // Preview용 CompositionLocal 제공
        CompositionLocalProvider(LocalAppNavController provides rememberNavController()) {
            ErrorScreen(
                navController = rememberNavController(),
                errorMessage = "서버 연결에 실패했습니다.",
            )
        }
    }
}
