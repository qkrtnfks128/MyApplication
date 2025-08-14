package com.example.myapplication.page.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.manager.AdminManager
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.CustomColor
import com.example.myapplication.ui.theme.Stroke
import com.example.myapplication.utils.LogManager
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.filled.Error
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.myapplication.navigation.LocalAppNavController
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.components.CustomToast
import com.example.myapplication.ui.theme.b1
import com.example.myapplication.ui.theme.b3
import com.example.myapplication.ui.theme.b4
import com.example.myapplication.ui.theme.b5

/**
 * AdminLoginScreen은 관리자 로그인을 위한 Compose 화면입니다.
 * - 이메일(또는 전화번호)와 비밀번호를 입력받아 로그인 시도를 합니다.
 * - 로그인 성공 시 기관 선택 화면으로 이동합니다.
 * - 로그인 실패 시 에러 메시지를 표시합니다.
 * - 비밀번호 입력란의 가시성 토글 기능을 제공합니다.
 * - 로딩 중에는 버튼이 비활성화되고, 진행 인디케이터가 표시됩니다.
 * - AdminManager를 통해 로그인 처리를 하며, 로그 기록(LogManager)도 남깁니다.
 * - Compose Preview를 통해 디자인 미리보기가 가능합니다.
 * - 별도의 뷰모델은 없습니다.
 */


private const val ADMIN_LOGIN_SCREEN_TAG: String = "AdminLoginScreen"

@Composable
fun AdminLoginScreen(
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val currentSession = AdminManager.observeAdminSession()?.collectAsStateWithLifecycle()
    val context = LocalContext.current
     // 포커스 관리자 추가
     val focusManager = LocalFocusManager.current

    // 로그인 함수를 별도로 분리
    val performLogin: () -> Unit = {
        scope.launch {
            isLoading = true

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
                CustomToast.show(context, "존재하지 않는 계정정보입니다.")
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "관리자 로그인",
            style = MaterialTheme.typography.b5,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 입력 폼 영역
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, Stroke.black20),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // 전화번호 입력 필드
                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "전화번호",
                    leadingIcon = Icons.Default.Phone,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    onImeAction = { action ->
                        when (action) {
                            ImeAction.Next -> {
                                // 다음 필드(비밀번호)로 포커스 이동
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                            else -> {}
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 비밀번호 입력 필드
                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "비밀번호",
                    leadingIcon = Icons.Default.Lock,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    onTrailingIconClick = { passwordVisible = !passwordVisible },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    onImeAction = { action ->
                        when (action) {
                            ImeAction.Done -> {
                                if (email.isNotEmpty() && password.isNotEmpty()) {
                                    performLogin()
                                }
                            }
                            else -> {}
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 로그인 버튼
                Button(
                    onClick = performLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomColor.blue,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "로그인",
                            style = MaterialTheme.typography.b4
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onImeAction: ((ImeAction) -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.b5) },
        textStyle = MaterialTheme.typography.b5,
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
        },
        trailingIcon = trailingIcon?.let { icon ->
            {
                IconButton(onClick = { onTrailingIconClick?.invoke() }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (icon == Icons.Default.Visibility) "비밀번호 보기" else "비밀번호 숨기기",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        },
        visualTransformation = visualTransformation,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onNext = { onImeAction?.invoke(ImeAction.Next) },
            onDone = { onImeAction?.invoke(ImeAction.Done) },
            onGo = { onImeAction?.invoke(ImeAction.Go) },
            onSearch = { onImeAction?.invoke(ImeAction.Search) },
            onSend = { onImeAction?.invoke(ImeAction.Send) }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CustomColor.blue,
            unfocusedBorderColor = Stroke.black20,
            focusedLabelColor = CustomColor.blue,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
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
