# Login Page

## 역할 (Role)

Login 페이지는 사용자 인증을 담당하는 UI 계층입니다. 로그인 화면과 관련된 모든 Compose 컴포넌트를 포함합니다.

## 구조 (Structure)

```
page/login/
├── LoginScreen.kt             # 로그인 화면 컴포넌트
└── README.md                 # 이 파일
```

## 주요 특징 (Key Features)

### 1. UI 컴포넌트

- Jetpack Compose를 사용한 현대적인 UI
- Material 3 디자인 시스템 적용
- 반응형 레이아웃 지원

### 2. 상태 관리

- UserManager를 통한 전역 상태 접근
- StateFlow를 사용한 실시간 상태 관찰
- 로딩 상태 및 에러 상태 처리

### 3. 사용자 상호작용

- 로그인 폼 입력 처리
- 유효성 검사
- 로그인/로그아웃 기능

## 사용 예시 (Usage Example)

```kotlin
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val currentUser = UserManager.observeUser()?.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 로그인 폼
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") }
        )

        Button(
            onClick = {
                scope.launch {
                    UserManager.login(email, password)
                }
            }
        ) {
            Text("로그인")
        }

        // 현재 사용자 정보 표시
        currentUser?.value?.let { user ->
            Text("안녕하세요, ${user.name}님!")
        }
    }
}
```

## 네이밍 규칙 (Naming Convention)

- 파일명: `[기능명]Screen.kt` (예: `LoginScreen.kt`, `RegisterScreen.kt`)
- 컴포넌트명: `[기능명]Screen` (예: `LoginScreen`, `RegisterScreen`)
- Preview 함수: `[기능명]Preview` (예: `LoginScreenPreview`)

## 컴포넌트 구조

### 1. 메인 화면 컴포넌트

```kotlin
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
)
```

### 2. 하위 컴포넌트

```kotlin
@Composable
private fun LoginForm(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean
)

@Composable
private fun UserInfoCard(
    user: User,
    onLogoutClick: () -> Unit
)
```

### 3. Preview 함수

```kotlin
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MyApplicationTheme {
        LoginScreen()
    }
}
```

## 상태 관리 패턴

### 1. 로컬 상태 (Local State)

```kotlin
var email by remember { mutableStateOf("") }
var password by remember { mutableStateOf("") }
var isLoading by remember { mutableStateOf(false) }
var errorMessage by remember { mutableStateOf("") }
```

### 2. 전역 상태 관찰 (Global State Observation)

```kotlin
val currentUser = UserManager.observeUser()?.collectAsStateWithLifecycle()
```

### 3. 이벤트 처리 (Event Handling)

```kotlin
val scope = rememberCoroutineScope()

Button(
    onClick = {
        scope.launch {
            isLoading = true
            val result = UserManager.login(email, password)
            result.fold(
                onSuccess = { user ->
                    onLoginSuccess()
                },
                onFailure = { exception ->
                    errorMessage = exception.message ?: "로그인 실패"
                }
            )
            isLoading = false
        }
    }
) {
    Text("로그인")
}
```

## UI 컴포넌트 사용법

### 1. 입력 필드

```kotlin
OutlinedTextField(
    value = email,
    onValueChange = { email = it },
    label = { Text("이메일") },
    modifier = Modifier.fillMaxWidth(),
    keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Email
    )
)
```

### 2. 비밀번호 필드

```kotlin
OutlinedTextField(
    value = password,
    onValueChange = { password = it },
    label = { Text("비밀번호") },
    visualTransformation = PasswordVisualTransformation(),
    modifier = Modifier.fillMaxWidth()
)
```

### 3. 로딩 상태

```kotlin
Button(
    onClick = { /* 로그인 로직 */ },
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
```

## 에러 처리

### 1. 에러 메시지 표시

```kotlin
if (errorMessage.isNotEmpty()) {
    Text(
        text = errorMessage,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall
    )
}
```

### 2. 유효성 검사

```kotlin
fun validateEmail(email: String): Boolean {
    return email.contains("@") && email.contains(".")
}

fun validatePassword(password: String): Boolean {
    return password.length >= 6
}
```

## Clean Architecture 원칙

- UI 로직과 비즈니스 로직 분리
- UserManager를 통한 전역 상태 접근
- 단일 책임 원칙 준수
- 테스트 가능한 컴포넌트 설계
