# Planner API

다중 시간 단위 계획 관리 REST API 애플리케이션입니다. 일간, 주간, 월간, 연간 계획을 체계적으로 관리할 수 있습니다.

## 기술 스택

| 구분 | 기술 |
|------|------|
| Framework | Spring Boot 4.0.1 |
| Language | Java 21 |
| ORM | Spring Data JPA + Hibernate |
| Database | H2 (In-memory) |
| API Docs | SpringDoc OpenAPI 2.7.0 |
| Validation | Jakarta Bean Validation |
| Build Tool | Gradle |

## 프로젝트 구조

```
src/main/java/com/planner/my/
├── PlannerApplication.java          # 애플리케이션 진입점
├── config/
│   └── OpenApiConfig.java           # Swagger/OpenAPI 설정
├── controller/                       # REST API 컨트롤러
│   ├── DailyController.java         # 일간 계획 API
│   ├── WeeklyController.java        # 주간 계획 API
│   ├── MonthlyController.java       # 월간 계획 API
│   └── YearlyController.java        # 연간 계획 API
├── service/                          # 비즈니스 로직 계층
│   ├── DailyService.java
│   ├── WeeklyService.java
│   ├── MonthlyService.java
│   └── YearlyService.java
├── repository/                       # 데이터 접근 계층
│   ├── DailyPlanRepository.java
│   ├── WeeklyPlanRepository.java
│   ├── MonthlyPlanRepository.java
│   └── YearlyPlanRepository.java
├── entity/                           # JPA 엔티티 및 Enum
│   ├── DailyPlan.java
│   ├── WeeklyPlan.java
│   ├── MonthlyPlan.java
│   ├── YearlyPlan.java
│   ├── PlanStatus.java              # 상태: NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED
│   └── Priority.java                # 우선순위: HIGH, MEDIUM, LOW
├── dto/                              # 데이터 전송 객체
│   ├── DailyPlanRequest.java
│   ├── DailyPlanResponse.java
│   ├── WeeklyPlanRequest.java
│   ├── WeeklyPlanResponse.java
│   ├── MonthlyPlanRequest.java
│   ├── MonthlyPlanResponse.java
│   ├── YearlyPlanRequest.java
│   ├── YearlyPlanResponse.java
│   └── StatusUpdateRequest.java
└── exception/
    └── GlobalExceptionHandler.java   # 전역 예외 처리
```

## 빌드 및 실행

### 요구 사항
- Java 21 이상
- Gradle 9.x

### 빌드
```bash
./gradlew clean build
```

### 실행
```bash
./gradlew bootRun
```

또는 빌드된 JAR 파일 실행:
```bash
java -jar build/libs/planner-0.0.1-SNAPSHOT.jar
```

### 접속 정보
- API 서버: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:plannerdb`
  - Username: `sa`
  - Password: (비어있음)

## API 엔드포인트

### 일간 계획 (Daily Plan)

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/daily` | 일간 계획 생성 |
| GET | `/api/daily` | 전체 일간 계획 조회 |
| GET | `/api/daily/{id}` | ID로 계획 조회 |
| GET | `/api/daily/date/{date}` | 특정 날짜 계획 조회 |
| GET | `/api/daily/date-range?startDate=&endDate=` | 기간별 계획 조회 |
| GET | `/api/daily/status/{status}` | 상태별 계획 조회 |
| GET | `/api/daily/priority/{priority}` | 우선순위별 계획 조회 |
| PUT | `/api/daily/{id}` | 계획 수정 |
| PATCH | `/api/daily/{id}/status` | 상태만 수정 |
| DELETE | `/api/daily/{id}` | 계획 삭제 |

### 주간 계획 (Weekly Plan)

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/weekly` | 주간 계획 생성 |
| GET | `/api/weekly` | 전체 주간 계획 조회 |
| GET | `/api/weekly/{id}` | ID로 계획 조회 |
| GET | `/api/weekly/week/{weekStartDate}` | 주 시작일로 계획 조회 |
| GET | `/api/weekly/date-range?startDate=&endDate=` | 기간별 계획 조회 |
| GET | `/api/weekly/status/{status}` | 상태별 계획 조회 |
| GET | `/api/weekly/priority/{priority}` | 우선순위별 계획 조회 |
| PUT | `/api/weekly/{id}` | 계획 수정 |
| PATCH | `/api/weekly/{id}/status` | 상태만 수정 |
| DELETE | `/api/weekly/{id}` | 계획 삭제 |

### 월간 계획 (Monthly Plan)

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/monthly` | 월간 계획 생성 |
| GET | `/api/monthly` | 전체 월간 계획 조회 |
| GET | `/api/monthly/{id}` | ID로 계획 조회 |
| GET | `/api/monthly/year/{year}/month/{month}` | 년월별 계획 조회 |
| GET | `/api/monthly/year/{year}` | 연도별 계획 조회 |
| GET | `/api/monthly/status/{status}` | 상태별 계획 조회 |
| GET | `/api/monthly/priority/{priority}` | 우선순위별 계획 조회 |
| PUT | `/api/monthly/{id}` | 계획 수정 |
| PATCH | `/api/monthly/{id}/status` | 상태만 수정 |
| DELETE | `/api/monthly/{id}` | 계획 삭제 |

### 연간 계획 (Yearly Plan)

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/yearly` | 연간 계획 생성 |
| GET | `/api/yearly` | 전체 연간 계획 조회 |
| GET | `/api/yearly/{id}` | ID로 계획 조회 |
| GET | `/api/yearly/year/{year}` | 연도별 계획 조회 |
| GET | `/api/yearly/status/{status}` | 상태별 계획 조회 |
| GET | `/api/yearly/priority/{priority}` | 우선순위별 계획 조회 |
| PUT | `/api/yearly/{id}` | 계획 수정 |
| PATCH | `/api/yearly/{id}/status` | 상태만 수정 |
| DELETE | `/api/yearly/{id}` | 계획 삭제 |

## 데이터 모델

### 공통 필드

모든 계획 엔티티는 다음 공통 필드를 포함합니다:

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | 기본 키 (자동 생성) |
| title | String | 계획 제목 (필수) |
| description | String | 상세 설명 (선택) |
| priority | Priority | 우선순위 (HIGH, MEDIUM, LOW) |
| status | PlanStatus | 상태 (NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED) |
| createdAt | LocalDateTime | 생성 시간 |
| updatedAt | LocalDateTime | 수정 시간 |

### 엔티티별 추가 필드

| 엔티티 | 추가 필드 | 설명 |
|--------|----------|------|
| DailyPlan | planDate (LocalDate) | 계획 날짜 |
| WeeklyPlan | weekStartDate, weekEndDate (LocalDate) | 주 시작일/종료일 |
| MonthlyPlan | year (Integer), month (Integer) | 연도, 월 (1-12) |
| YearlyPlan | year (Integer) | 연도 |

### Enum 값

**PlanStatus (계획 상태)**
- `NOT_STARTED`: 시작 전
- `IN_PROGRESS`: 진행 중
- `COMPLETED`: 완료
- `FAILED`: 실패

**Priority (우선순위)**
- `HIGH`: 높음
- `MEDIUM`: 보통
- `LOW`: 낮음

## API 사용 예시

### 일간 계획 생성

```bash
curl -X POST http://localhost:8080/api/daily \
  -H "Content-Type: application/json" \
  -d '{
    "title": "프로젝트 회의",
    "description": "스프린트 계획 회의 참석",
    "planDate": "2026-01-05",
    "priority": "HIGH",
    "status": "NOT_STARTED"
  }'
```

### 월간 계획 조회

```bash
curl http://localhost:8080/api/monthly/year/2026/month/1
```

### 상태 업데이트

```bash
curl -X PATCH http://localhost:8080/api/daily/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "COMPLETED"
  }'
```

## 에러 응답 형식

```json
{
  "timestamp": "2026-01-04T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Daily plan not found: 123",
  "fieldErrors": null
}
```

### HTTP 상태 코드

| 상태 코드 | 설명 |
|----------|------|
| 200 | 성공 |
| 201 | 생성 성공 |
| 400 | 잘못된 요청 (유효성 검사 실패) |
| 404 | 리소스를 찾을 수 없음 |
| 500 | 서버 내부 오류 |

## 아키텍처

### 계층 구조

```
Controller (REST API)
    ↓
Service (비즈니스 로직 + 트랜잭션)
    ↓
Repository (데이터 접근)
    ↓
Entity (JPA 엔티티)
```

### 설계 패턴

- **Layered Architecture**: Controller → Service → Repository → Entity
- **DTO Pattern**: 요청/응답 객체 분리
- **Repository Pattern**: Spring Data JPA 기반 데이터 접근 추상화
- **Global Exception Handler**: 일관된 에러 응답 처리
- **Builder Pattern**: Lombok @Builder를 활용한 객체 생성

## 테스트

```bash
./gradlew test
```