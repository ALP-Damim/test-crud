# Test CRUD Application

시험 및 문항을 관리하는 Spring Boot 애플리케이션입니다.

## 주요 기능

### 시험 관리
- 시험 생성, 조회, 수정, 삭제
- 클래스별 시험 목록 조회
- 난이도별 시험 필터링
- **시험 준비 상태 관리** (`isReady`)

### 문항 관리
- 객관식/단답식 문항 생성, 조회, 수정, 삭제
- 문항 순서 관리
- 점수 설정
- **선택지 JSON 문자열 저장** (`choices`)

## 새로운 기능: 시험 준비 상태

### `isReady` 필드
- `true`: 시험이 준비 완료 상태 (학생에게 표시 가능)
- `false`: 시험이 준비 중 상태 (학생에게 표시되지 않음)

### API 엔드포인트

#### 시험 준비 상태 변경
```http
PATCH /api/exams/{examId}/ready?isReady=true
PATCH /api/exams/{examId}/ready?isReady=false
```

#### 시험 생성 시 준비 상태 설정
```http
POST /api/exams
{
  "sessionId": 11111111,
  "name": "시험명",
  "difficulty": "EASY",
  "isReady": false,
  "createdBy": 22222222
}
```

#### 시험 수정 시 준비 상태 변경
```http
PATCH /api/exams/{examId}
{
  "name": "수정된 시험명",
  "difficulty": "MEDIUM",
  "isReady": true
}
```

#### 문항 생성 (객관식)
```http
POST /api/exams/{examId}/questions
{
  "qtype": "MCQ",
  "body": "문제 본문",
  "choices": "[\"선택지1\", \"선택지2\", \"선택지3\", \"선택지4\"]",
  "answerKey": "선택지1",
  "points": 5,
  "position": 1
}
```

## 데이터베이스 스키마

### exams 테이블
- `exam_id`: 시험 ID (Primary Key)
- `session_id`: 세션 ID
- `name`: 시험명
- `difficulty`: 난이도
- `is_ready`: 준비 상태 (기본값: false)
- `created_by`: 작성자 ID
- `created_at`: 생성 시간

### questions 테이블
- `question_id`: 문항 ID (Primary Key)
- `exam_id`: 시험 ID (Foreign Key)
- `qtype`: 문제 유형 (MCQ, SHORT)
- `body`: 문제 본문
- `choices`: 선택지 (JSON 문자열, 객관식만)
- `answer_key`: 정답
- `points`: 배점
- `position`: 문항 순서

## 실행 방법

1. PostgreSQL 데이터베이스 설정
2. `test.sql` 실행하여 테이블 생성
3. Spring Boot 애플리케이션 실행
4. `test.http` 파일로 API 테스트

## 기술 스택

- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Gradle
- Lombok
