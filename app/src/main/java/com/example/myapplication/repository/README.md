# Repository Layer

## 역할 (Role)

Repository 계층은 데이터 접근을 담당하는 계층입니다. 로컬 데이터베이스, 네트워크 API, SharedPreferences 등 다양한 데이터 소스에 대한 추상화를 제공합니다.

## 주요 특징 (Key Features)

### 1. 데이터 소스 추상화

- 로컬 데이터베이스 (Room, SQLite)
- 네트워크 API (Retrofit, OkHttp)
- 로컬 저장소 (SharedPreferences, DataStore)
- 캐시 (Memory Cache, Disk Cache)

### 2. 인터페이스 기반 설계

- 구현체와 인터페이스 분리
- 테스트 시 Mock 객체로 쉽게 교체 가능
- 의존성 역전 원칙 준수

### 3. 에러 처리

- 네트워크 에러, 데이터베이스 에러 등 처리
- Result 타입을 사용한 안전한 에러 처리

## 네이밍 규칙 (Naming Convention)

- 인터페이스명: `[기능명]Repository` (예: `AuthRepository`, `UserRepository`)
- 구현체명: `[기능명]RepositoryImpl` (예: `AuthRepositoryImpl`)
- 메서드명: 동사로 시작 (예: `login()`, `getUser()`, `saveUser()`)

## Clean Architecture 원칙

- 외부 데이터 소스에 대한 추상화 제공
- 비즈니스 로직과 데이터 접근 로직 분리
- 단일 책임 원칙 준수
