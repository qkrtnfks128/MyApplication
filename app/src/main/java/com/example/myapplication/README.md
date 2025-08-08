# MyApplication 패키지 구조

`com.example.myapplication`의 폴더와 역할을 간단히 정리합니다.

## 디렉터리

- controller/: 레포지토리 호출을 조율하고 UI에 상태를 노출합니다.
- manager/: 사용자·세션 등 앱 전역 상태와 오케스트레이션을 담당합니다.
- model/: 도메인 모델. 불변 권장, 프레임워크/직렬화 비의존.
- network/: Retrofit/OkHttp, 인터셉터, API, DTO. 서버 세부를 격리합니다.
- repository/: 데이터 접근. 인터페이스·구현, DTO↔모델 매핑, 캐싱/정책.
- navigation/: 네비게이션 그래프와 라우트 정의.
- page/: Compose 화면. 상태 렌더링과 상호작용 처리.
- ui/: 공용 UI 컴포넌트와 테마(Material 3 등).
- utils/: 공용 유틸리티(로깅 등). 전역 상태 없음.

## 핵심 파일

- MyApplication.kt: 앱 진입점. 전역 의존성·컴포넌트 초기화.

## 기본 원칙

- 도메인 모델은 `model/`, 전송 객체는 `network/dto/`. 매핑은 `repository/`에서 수행.
- UI 로직은 `page/`, 재사용 뷰는 `ui/`에 위치.
- 작은 단위, 명확한 네이밍, 합성 우선.

## 새 API 추가 플로우

- 요구 확정: 메서드·경로, 요청/응답, 인증 여부.
- API 인터페이스: `network/api/`에 `XxxApi` 추가.
- Repository 구현: Retrofit 호출 → DTO→모델 매핑 → 캐싱/에러 정책 반영.
- Controller 연동: 메서드 노출, 상태(StateFlow 등) 갱신.
- Manager 연계(선택): 토큰·세션 등 전역 관심사 처리.
- Page 반영: 이벤트에서 호출, 상태 구독·렌더링.
- 로깅: `utils/LogManager` 활용.
- 설정 점검: `NetworkConfig.BASE_URL`, 인증 헤더, 타임아웃.
- 테스트: 매퍼·레포지토리 단위 테스트, 화면 동작 검증.
