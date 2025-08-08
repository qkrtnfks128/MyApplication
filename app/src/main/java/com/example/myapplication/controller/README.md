# Controller Layer

## 역할 (Role)

Controller 계층은 비즈니스 로직을 담당하는 중간 계층입니다. Repository와 UI 계층 사이에서 데이터를 가공하고 상태를 관리합니다.

## 주요 특징 (Key Features)

### 1. 비즈니스 로직 처리

- Repository에서 받은 데이터를 가공
- UI에 필요한 형태로 데이터 변환
- 상태 관리 (StateFlow, LiveData 등)

### 2. 의존성 주입

- Repository를 생성자로 주입받음
- 테스트 시 Mock 객체로 쉽게 교체 가능

### 3. 에러 처리

- Repository에서 발생한 에러를 적절히 처리
- UI에 전달할 수 있는 형태로 변환

## 네이밍 규칙 (Naming Convention)

- 클래스명: `[기능명]Controller` (예: `AuthController`, `UserController`)
- 메서드명: 동사로 시작 (예: `login()`, `logout()`, `getCurrentUser()`)

## Clean Architecture 원칙

- Repository 계층에만 의존
- UI 계층에 직접 의존하지 않음
- 단일 책임 원칙 준수
