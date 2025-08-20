-- test.sql
-- 시드/리셋용. 로컬 Postgres에서 실행:  psql -d testcrud -f test.sql

-- 1) 기존 테이블 삭제 (순서 중요: 자식 테이블 먼저)
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS exams CASCADE;

-- 2) 시험 테이블 생성
CREATE TABLE exams (
    exam_id BIGSERIAL PRIMARY KEY,
    class_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    difficulty VARCHAR(255),
    created_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- 3) 문항 테이블 생성 (외래키 참조)
CREATE TABLE questions (
    question_id BIGSERIAL PRIMARY KEY,
    exam_id BIGINT NOT NULL REFERENCES exams(exam_id) ON DELETE CASCADE,
    qtype VARCHAR(255) NOT NULL CHECK (qtype IN ('MCQ', 'SHORT')),
    body TEXT NOT NULL,
    choices JSONB,
    answer_key VARCHAR(255),
    points NUMERIC(19,2) NOT NULL DEFAULT 0,
    position INTEGER NOT NULL
);

-- 4) 인덱스 생성 (조회 최적화)
CREATE INDEX idx_exam_class_diff ON exams(class_id, difficulty);
CREATE INDEX idx_question_exam_position ON questions(exam_id, position);

-- 5) 공용 Long ID
-- 클래스와 작성자 ID는 외부 MSA(클래스/계정 서비스)에서 온다고 가정
-- 여기서는 고정 Long 값을 사용
-- classId:  11111111
-- createdBy: 22222222

-- 6) 시험 샘플 2개
INSERT INTO exams (exam_id, class_id, name, difficulty, created_by, created_at)
VALUES
    (1, 11111111, '네트워크 기초 퀴즈', 'EASY', 22222222, NOW()),
    (2, 11111111, '네트워크 중간고사', 'MEDIUM', 22222222, NOW());

-- 7) ID 1 시험의 문항 3개 (position 오름차순)
INSERT INTO questions
(question_id, exam_id, qtype, body, choices, answer_key, points, position)
VALUES
    (1, 1, 'MCQ', 'OSI 4계층의 명칭은?', '["Application","Transport","Network","Data Link"]', 'Transport', 5.00, 1),
    (2, 1, 'SHORT', 'TCP 연결 수립 시 사용하는 handshake 단계 수?', NULL, '3', 5.00, 2),
    (3, 1, 'MCQ', '다음 중 전송계층 프로토콜은?', '["IP","TCP","ARP","ICMP"]', 'TCP', 5.00, 3);