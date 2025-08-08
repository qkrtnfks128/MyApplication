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
import com.example.myapplication.manager.UserManager
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utils.LogManager
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

private const val LOGIN_SCREEN_TAG: String = "LoginScreen"
@Composable
fun LoginScreen(
    navController: NavController
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val scope = rememberCoroutineScope()
    val currentUser = UserManager.observeUser()?.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "로그인",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
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
                    
                    val result = UserManager.login(email, password)
                    result.fold(
                        onSuccess = { user ->
                          LogManager.auth(LOGIN_SCREEN_TAG, "로그인", true)
                          
                           Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { exception ->
                            LogManager.auth(LOGIN_SCREEN_TAG, "로그인", false)
                            errorMessage = "로그인 실패: ${exception.message}"
                        }
                    )
                    isLoading = false
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
        
        // 현재 로그인된 사용자 정보 표시
        currentUser?.value?.let { user ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "현재 로그인된 사용자",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text("이름: ${user.name}")
                    Text("이메일: ${user.email}")
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                UserManager.logout()
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
fun LoginScreenPreview() {
    MyApplicationTheme {
        LoginScreen(rememberNavController())
    }
}
