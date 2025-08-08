# Model Layer

## 역할 (Role)

Model 계층은 앱에서 사용되는 데이터 구조를 정의하는 계층입니다. 비즈니스 로직과 데이터 전송에 사용되는 객체들을 포함합니다.

## 구조 (Structure)

```
model/
├── User.kt                    # 유저 정보 데이터 모델
├── BloodSugarData.kt          # 혈당 데이터 모델
└── README.md                 # 이 파일
```

## 주요 특징 (Key Features)

### 1. 데이터 구조 정의

- 앱에서 사용되는 모든 데이터 타입 정의
- 불변성(Immutability) 원칙 준수
- 데이터 클래스 사용으로 간결한 코드

### 2. 비즈니스 로직 분리

- 순수한 데이터 구조만 포함
- 비즈니스 로직은 Controller나 UseCase에서 처리
- 데이터 검증 로직 포함 가능

### 3. 계층 간 데이터 전송

- Repository ↔ Controller 간 데이터 전송
- Controller ↔ UI 간 데이터 전송
- API 응답 데이터 매핑

## 사용 예시 (Usage Example)

```kotlin
data class User(
    val id: String,
    val email: String,
    val name: String,
    val isLoggedIn: Boolean = false,
    val lastLoginTime: Long = 0L
) {
    // 데이터 검증 로직
    fun isValid(): Boolean {
        return id.isNotEmpty() && email.isNotEmpty() && name.isNotEmpty()
    }

    // 데이터 변환 로직
    fun toDisplayName(): String {
        return if (name.isNotEmpty()) name else email
    }
}
```

## 네이밍 규칙 (Naming Convention)

- 클래스명: 명사로 시작 (예: `User`, `BloodSugarData`, `ApiResponse`)
- 프로퍼티명: camelCase (예: `userId`, `email`, `lastLoginTime`)
- 메서드명: 동사로 시작 (예: `isValid()`, `toDisplayName()`)

## Model 종류별 예시

### 1. 도메인 모델 (Domain Model)

```kotlin
data class User(
    val id: String,
    val email: String,
    val name: String,
    val profileImage: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
```

### 2. API 응답 모델 (API Response Model)

```kotlin
data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?,
    val errorCode: String?
)

data class LoginResponse(
    val user: User,
    val token: String,
    val expiresAt: Long
)
```

### 3. UI 상태 모델 (UI State Model)

```kotlin
data class LoginState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)
```

### 4. 데이터베이스 모델 (Database Model)

```kotlin
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val name: String,
    val createdAt: Long
)
```

## 데이터 변환 패턴

### 1. 확장 함수를 사용한 변환

```kotlin
// User.kt
fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        name = name,
        createdAt = createdAt
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        email = email,
        name = name,
        createdAt = createdAt
    )
}
```

### 2. 매핑 함수를 사용한 변환

```kotlin
data class BloodSugarData(
    val id: String,
    val value: Int,
    val timestamp: Long,
    val type: String
) {
    fun toDisplayValue(): String {
        return "$value mg/dL"
    }

    fun isHigh(): Boolean {
        return value > 140
    }

    fun isLow(): Boolean {
        return value < 70
    }
}
```

## 검증 로직 (Validation Logic)

```kotlin
data class User(
    val id: String,
    val email: String,
    val name: String
) {
    fun isValid(): Boolean {
        return id.isNotEmpty() &&
               email.contains("@") &&
               name.isNotEmpty()
    }

    fun getValidationErrors(): List<String> {
        val errors = mutableListOf<String>()

        if (id.isEmpty()) errors.add("ID는 필수입니다")
        if (!email.contains("@")) errors.add("올바른 이메일 형식이 아닙니다")
        if (name.isEmpty()) errors.add("이름은 필수입니다")

        return errors
    }
}
```

## Clean Architecture 원칙

- 순수한 데이터 구조 정의
- 외부 의존성 없음
- 비즈니스 로직과 분리
- 테스트 용이성 보장
