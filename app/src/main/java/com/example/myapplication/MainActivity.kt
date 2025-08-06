package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.SolidButton
import com.example.myapplication.components.dialog.BigDialog
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "BigDialog 예제",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        SolidButton(
            text = "다이얼로그 열기",
            onClick = { showDialog = true }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        SolidButton(
            text = "정보 다이얼로그",
            onClick = { showInfoDialog = true },
            backgroundColor = MaterialTheme.colorScheme.secondary
        )
    }
    
    // 기본 다이얼로그
    if (showDialog) {
        BigDialog(
            title = "확인 다이얼로그",
            content = {
                Text(
                    text = "이것은 BigDialog 컴포넌트의 예제입니다. 타이틀과 내용을 파라미터로 받아서 유연하게 사용할 수 있습니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            onDismiss = { showDialog = false },
            onConfirm = {
                // 확인 버튼 클릭 시 처리
                println("확인 버튼 클릭됨")
            },
            onCancel = {
                // 취소 버튼 클릭 시 처리
                println("취소 버튼 클릭됨")
            }
        )
    }
    
    // 정보 다이얼로그 (취소 버튼 없음)
    if (showInfoDialog) {
        BigDialog(
            title = "앱 정보",
            content = {
                Column {
                    Text(
                        text = "앱 버전: 1.0.0",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "개발자: MyApplication",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "이 앱은 Jetpack Compose로 개발되었습니다.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            onDismiss = { showInfoDialog = false },
            onConfirm = {
                println("정보 다이얼로그 확인")
            },
            showCancelButton = false,
            confirmText = "알겠습니다"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyApplicationTheme {
        MainScreen()
    }
}