package com.example.myapplication.page.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.manager.AdminManager
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utils.LogManager
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.navigation.LocalAppNavController
import com.example.myapplication.navigation.Screen

private const val ADMIN_LOGIN_SCREEN_TAG: String = "AdminLoginScreen"
@Composable
fun AdminLoginScreen(
    navController: NavController
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val currentSession = AdminManager.observeAdminSession()?.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "관리자 로그인",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("전화번호") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = ""

                    try {
                        val session = AdminManager.adminLogin(email, password)
                        LogManager.auth(ADMIN_LOGIN_SCREEN_TAG, "관리자 로그인", true)

                        Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.AdminOrgSelect.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        LogManager.auth(ADMIN_LOGIN_SCREEN_TAG, "관리자 로그인", false)
                        errorMessage = "로그인 실패: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("로그인")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 현재 로그인된 관리자 정보 표시
        currentSession?.value?.let { session ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "현재 로그인된 관리자",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text("사용자 ID: ${session.userUuid}")
                    Text("상태 코드: ${session.statusCode}")
                    Text("기관 수: ${session.adminOrgs.size}")

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                AdminManager.logout()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("로그아웃")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminLoginScreenPreview() {
    MyApplicationTheme {
        val nav = rememberNavController()
        CompositionLocalProvider(LocalAppNavController provides nav) {
            AdminLoginScreen(nav)
        }
    }
}
