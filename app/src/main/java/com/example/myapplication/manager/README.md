# Manager Layer

## 역할 (Role)

Manager 계층은 앱 전체에서 전역적으로 사용되는 상태와 기능을 관리하는 계층입니다. 싱글톤 패턴을 사용하여 앱 전체에서 일관된 접근을 제공합니다.

## 구조 (Structure)

```
manager/
├── UserManager.kt             # 전역 유저 상태 관리
└── README.md                 # 이 파일
```

## 주요 특징 (Key Features)

### 1. 전역 상태 관리

- 앱 전체에서 공유되는 상태 관리
- 싱글톤 패턴으로 단일 인스턴스 보장
- 앱 생명주기 동안 상태 유지

### 2. 의존성 주입 포인트

- Controller나 Repository를 주입받아 초기화
- 앱 시작 시 한 번만 초기화되어 성능 최적화

### 3. 편의 메서드 제공

- 복잡한 로직을 단순한 인터페이스로 제공
- 앱 전체에서 일관된 API 제공

## 사용 예시 (Usage Example)

```kotlin
object UserManager {
    private var authController: AuthController? = null

    fun initialize(authController: AuthController) {
        this.authController = authController
    }

    suspend fun login(email: String, password: String): Result<User> {
        return authController?.login(email, password)
            ?: Result.failure(IllegalStateException("Not initialized"))
    }

    fun isLoggedIn(): Boolean {
        return authController?.isLoggedIn() ?: false
    }
}
```

## 네이밍 규칙 (Naming Convention)

- 클래스명: `[기능명]Manager` (예: `UserManager`, `ApiManager`, `SettingsManager`)
- 메서드명: 동사로 시작 (예: `initialize()`, `getCurrentUser()`, `isLoggedIn()`)

## Manager 종류별 예시

### 1. UserManager (유저 관리)

```kotlin
object UserManager {
    fun login(email: String, password: String): Result<User>
    fun logout(): Result<Unit>
    fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
}
```

### 2. ApiManager (API 관리)

```kotlin
object ApiManager {
    fun initialize(apiService: ApiService)
    fun getApiService(): ApiService?
    fun setAuthToken(token: String)
    fun clearAuthToken()
}
```

### 3. SettingsManager (설정 관리)

```kotlin
object SettingsManager {
    fun getString(key: String): String?
    fun setString(key: String, value: String)
    fun getBoolean(key: String): Boolean
    fun setBoolean(key: String, value: Boolean)
}
```

### 4. DatabaseManager (데이터베이스 관리)

```kotlin
object DatabaseManager {
    fun initialize(database: AppDatabase)
    fun getUserDao(): UserDao?
    fun getBloodSugarDao(): BloodSugarDao?
    fun clearDatabase()
}
```

## 초기화 패턴

### 1. 앱 시작 시 초기화

```kotlin
// MyApplication.kt
private fun initializeDependencies() {
    val authRepository = AuthRepositoryImpl()
    val authController = AuthController(authRepository)
    UserManager.initialize(authController)  // Manager 초기화
}
```

### 2. 사용 시점

```kotlin
// 어디서든 사용 가능
val isLoggedIn = UserManager.isLoggedIn()
val currentUser = UserManager.getCurrentUser()
```

## 장점 (Advantages)

1. **전역 접근성**: 앱 전체에서 일관된 접근
2. **상태 공유**: 여러 화면에서 동일한 상태 공유
3. **단순한 API**: 복잡한 로직을 단순한 인터페이스로 제공
4. **메모리 효율성**: 싱글톤으로 메모리 사용량 최적화

## 주의사항 (Precautions)

1. **초기화 순서**: 앱 시작 시 반드시 초기화 필요
2. **메모리 관리**: 앱 생명주기 동안 메모리에 유지
3. **테스트 복잡성**: 전역 상태로 인한 테스트 복잡성 증가
4. **순환 의존성**: Manager 간 순환 참조 주의

## Clean Architecture 원칙

- 전역 상태 관리의 단일 책임
- 의존성 역전 원칙 준수
- 테스트 가능한 설계
