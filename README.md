# Test CRUD Application

시험 및 문항을 관리하는 Spring Boot MSA 애플리케이션입니다.

## 📋 목차
- [개요](#개요)
- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [API 명세](#api-명세)
- [데이터베이스 스키마](#데이터베이스-스키마)
- [설치 및 실행](#설치-및-실행)
- [개발 가이드](#개발-가이드)

## 🎯 개요

이 애플리케이션은 교육 환경에서 시험과 문항을 체계적으로 관리할 수 있는 RESTful API를 제공합니다. 세션 기반으로 시험을 구성하고, 객관식과 단답식 문항을 지원하며, 시험의 준비 상태를 관리할 수 있습니다.

## ✨ 주요 기능

### 🧪 시험 관리
- **CRUD 작업**: 시험 생성, 조회, 수정, 삭제
- **세션별 관리**: `session_id`를 통한 시험 그룹화
- **난이도 필터링**: EASY, MEDIUM, HARD 등 난이도별 분류
- **준비 상태 관리**: `isReady` 플래그로 시험 공개/비공개 제어
- **생성자 추적**: `created_by`로 시험 작성자 식별

### 📝 문항 관리
- **다양한 유형**: 객관식(MCQ), 단답식(SHORT) 지원
- **순서 관리**: 문항의 위치 순서 조정
- **점수 설정**: 각 문항별 배점 설정
- **선택지 저장**: 객관식 문항의 선택지를 JSON 형태로 저장
- **계층적 구조**: 시험 → 문항의 1:N 관계

## 🛠 기술 스택

- **Backend Framework**: Spring Boot 3.x
- **ORM**: Spring Data JPA + Hibernate
- **Database**: PostgreSQL
- **Build Tool**: Gradle
- **Language**: Java 17+
- **Utilities**: Lombok, SLF4J
- **Validation**: Jakarta Validation

## 📁 프로젝트 구조

```
src/main/java/com/kt/damim/testcrud/
├── controller/          # REST API 컨트롤러
│   ├── ExamController.java
│   └── QuestionController.java
├── service/            # 비즈니스 로직
│   ├── ExamService.java
│   └── QuestionService.java
├── entity/             # JPA 엔티티
│   ├── Exam.java
│   └── Question.java
├── repo/               # 데이터 접근 계층
│   ├── ExamRepository.java
│   └── QuestionRepository.java
├── dto/                # 데이터 전송 객체
│   ├── CreateExamRequest.java
│   ├── UpdateExamRequest.java
│   ├── ExamResponse.java
│   └── QuestionResponse.java
└── exception/          # 예외 처리
    ├── ExamNotFoundException.java
    ├── QuestionNotFoundException.java
    └── GlobalExceptionHandler.java
```

## 🔌 API 명세

### 시험 관리 API

#### 1. 시험 생성
```http
POST /api/exams
Content-Type: application/json

{
  "sessionId": 11111111,
  "name": "네트워크 기초 퀴즈",
  "difficulty": "EASY",
  "isReady": false,
  "createdBy": 22222222
}
```

#### 2. 시험 목록 조회
```http
# 특정 세션의 모든 시험 조회
GET /api/exams?sessionId=11111111

# 특정 세션 + 난이도별 시험 조회
GET /api/exams?sessionId=11111111&difficulty=EASY
```

#### 3. 시험 상세 조회
```http
GET /api/exams/{examId}
```

#### 4. 시험 수정
```http
PATCH /api/exams/{examId}
Content-Type: application/json

{
  "name": "수정된 시험명",
  "difficulty": "MEDIUM",
  "isReady": true
}
```

#### 5. 시험 준비 상태 변경
```http
PATCH /api/exams/{examId}/ready?isReady=true
```

#### 6. 시험 삭제
```http
DELETE /api/exams/{examId}
```

### 문항 관리 API

#### 1. 문항 추가
```http
POST /api/exams/{examId}/questions
Content-Type: application/json

{
  "qtype": "MCQ",
  "body": "OSI 4계층의 명칭은?",
  "choices": "[\"Application\",\"Transport\",\"Network\",\"Data Link\"]",
  "answerKey": "Transport",
  "points": 5,
  "position": 1
}
```

#### 2. 문항 목록 조회
```http
GET /api/exams/{examId}/questions
```

#### 3. 문항 수정
```http
PATCH /api/questions/{questionId}
Content-Type: application/json

{
  "body": "수정된 문제 본문",
  "points": 10
}
```

#### 4. 문항 순서 변경
```http
PUT /api/exams/{examId}/questions/reorder
Content-Type: application/json

{
  "1": 3,
  "2": 1,
  "3": 2
}
```

#### 5. 문항 삭제
```http
DELETE /api/questions/{questionId}
```

## 🗄 데이터베이스 스키마

### exams 테이블
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| `exam_id` | BIGSERIAL | PRIMARY KEY | 시험 ID (자동 증가) |
| `session_id` | BIGINT | NOT NULL | 세션 ID |
| `name` | VARCHAR(255) | NOT NULL | 시험명 |
| `difficulty` | VARCHAR(255) | - | 난이도 (EASY, MEDIUM, HARD) |
| `is_ready` | BOOLEAN | NOT NULL, DEFAULT FALSE | 준비 상태 |
| `created_by` | BIGINT | - | 작성자 ID |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성 시간 |

### questions 테이블
| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| `question_id` | BIGSERIAL | PRIMARY KEY | 문항 ID (자동 증가) |
| `exam_id` | BIGINT | NOT NULL, FOREIGN KEY | 시험 ID (exams.exam_id 참조) |
| `qtype` | VARCHAR(255) | NOT NULL, CHECK | 문제 유형 (MCQ, SHORT) |
| `body` | TEXT | NOT NULL | 문제 본문 |
| `choices` | JSONB | - | 선택지 (객관식만) |
| `answer_key` | VARCHAR(255) | - | 정답 |
| `points` | NUMERIC(19,2) | NOT NULL, DEFAULT 0 | 배점 |
| `position` | INTEGER | NOT NULL | 문항 순서 |

### 인덱스
- `idx_exam_session_diff`: `exams(session_id, difficulty)` - 세션별 난이도 조회 최적화
- `idx_question_exam_position`: `questions(exam_id, position)` - 문항 순서 조회 최적화

## 🚀 설치 및 실행

### 1. 사전 요구사항
- Java 17 이상
- PostgreSQL 12 이상
- Gradle 7.x 이상

### 2. 데이터베이스 설정
```bash
# PostgreSQL 데이터베이스 생성
createdb testcrud

# 스키마 및 샘플 데이터 생성
psql -d testcrud -f test.sql
```

### 3. 애플리케이션 실행
```bash
# 프로젝트 루트 디렉토리에서
./gradlew bootRun

# 또는 JAR 파일로 실행
./gradlew build
java -jar build/libs/test-crud-0.0.1-SNAPSHOT.jar
```

### 4. API 테스트
```bash
# test.http 파일을 사용하여 API 테스트
# IntelliJ IDEA, VS Code 등에서 HTTP Client 확장 사용
```

## 💻 개발 가이드

### 새로운 기능 추가 시
1. **엔티티 수정**: `entity/` 패키지의 해당 클래스 수정
2. **리포지토리 추가**: `repo/` 패키지에 쿼리 메서드 추가
3. **서비스 로직**: `service/` 패키지에 비즈니스 로직 구현
4. **컨트롤러**: `controller/` 패키지에 API 엔드포인트 추가
5. **DTO 생성**: `dto/` 패키지에 요청/응답 객체 정의

### 예외 처리
- `ExamNotFoundException`: 시험을 찾을 수 없을 때
- `QuestionNotFoundException`: 문항을 찾을 수 없을 때
- `GlobalExceptionHandler`: 전역 예외 처리 및 에러 응답 포맷팅

### 로깅
- SLF4J를 사용한 구조화된 로깅
- 각 API 요청에 대한 상세 로그 기록
- 에러 상황에 대한 적절한 로그 레벨 설정

## 🔧 환경 설정

### application.properties 예시
```properties
# 데이터베이스 설정
spring.datasource.url=jdbc:postgresql://localhost:5432/testcrud
spring.datasource.username=postgres
spring.datasource.password=password

# JPA 설정
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# 로깅 설정
logging.level.com.kt.damim.testcrud=DEBUG
```

## 📊 성능 고려사항

- **인덱스 최적화**: 자주 조회되는 컬럼 조합에 인덱스 적용
- **지연 로딩**: `@ManyToOne(fetch = FetchType.LAZY)`로 N+1 문제 방지
- **배치 처리**: 문항 삭제 시 CASCADE 옵션으로 효율적인 삭제
- **페이징**: 대용량 데이터 조회 시 페이징 적용 고려

## 🤝 기여 가이드

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 📞 문의

프로젝트에 대한 문의사항이 있으시면 이슈를 생성해 주세요.

---

**참고**: 이 애플리케이션은 MSA 환경에서 세션 기반 시험 관리 서비스로 설계되었습니다. 외부 시스템과의 연동을 위해 적절한 API Gateway와 인증/인가 시스템을 구성하는 것을 권장합니다.
