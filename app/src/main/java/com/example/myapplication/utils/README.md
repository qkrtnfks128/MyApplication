# Utils

앱에서 공통으로 사용되는 유틸리티 클래스들을 포함합니다.

## LogManager

앱 전체에서 사용할 공통화된 로그 시스템입니다.

### 사용법

```kotlin
import com.example.myapplication.utils.LogManager

class MainScreen {
    companion object {
        private const val TAG = "MainScreen"
    }

    fun someFunction() {
        // 디버그 로그 (릴리즈 빌드에서는 출력되지 않음)
        LogManager.debug(TAG, "디버그 메시지")

        // 정보 로그
        LogManager.info(TAG, "정보 메시지")

        // 사용자 액션 로그
        LogManager.userAction(TAG, "혈당 측정 버튼 클릭")

        // 네비게이션 로그 - 자동
        LogManager.navigation(TAG, "MainScreen", "BloodSugarHistory")

        // 인증 로그
        LogManager.auth(TAG, "로그인", true)

        // 에러 로그
        LogManager.error(TAG, "에러 발생", exception)
    }
}
```

### 로그 레벨

- **debug**: 디버그 정보 (릴리즈 빌드에서는 출력되지 않음)
- **info**: 일반 정보
- **warning**: 경고
- **error**: 에러
- **userAction**: 사용자 액션 (버튼 클릭 등)
- **navigation**: 화면 이동
- **auth**: 인증 관련

### 태그 규칙

- 클래스명을 태그로 사용 (예: "MainScreen", "LoginScreen")
- 태그는 companion object에 상수로 정의
- 로그 필터링을 위해 일관된 태그 사용
