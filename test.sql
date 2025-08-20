-- test.sql
-- 시드/리셋용. 로컬 Postgres에서 실행:  psql -d testcrud -f test.sql
--
-- -- 0) 확장 (필요 시)
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
--
-- -- 1) 깨끗하게
-- TRUNCATE TABLE submission_answers CASCADE; -- 존재하지 않으면 무시
-- TRUNCATE TABLE submissions CASCADE;        -- 존재하지 않으면 무시
TRUNCATE TABLE questions CASCADE;
TRUNCATE TABLE exams CASCADE;

-- 2) 공용 UUID
-- 클래스와 작성자 UUID는 외부 MSA(클래스/계정 서비스)에서 온다고 가정
-- 여기서는 고정 UUID를 사용
-- classId:  11111111-1111-1111-1111-111111111111
-- createdBy:22222222-2222-2222-2222-222222222222

-- 3) 시험 샘플 2개



-- 시험 테이블
-- CREATE TABLE exams (
--                        id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--                        class_id    UUID NOT NULL,
--                        name        TEXT NOT NULL,
--                        difficulty  TEXT NOT NULL,
--                        created_by  UUID,
--                        created_at  TIMESTAMPTZ DEFAULT now()
-- );
--
-- -- 문항 테이블
-- CREATE TABLE questions (
--                            id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--                            exam_id          UUID NOT NULL REFERENCES exams(id) ON DELETE CASCADE,
--                            qtype            TEXT NOT NULL CHECK (qtype IN ('MCQ', 'SHORT')),
--                            body             TEXT NOT NULL,
--                            choices_json     JSON,
--                            answer_key       TEXT NOT NULL,
--                            points           NUMERIC(6,2),
--                            position         INT NOT NULL,
--                            case_insensitive BOOLEAN NOT NULL DEFAULT TRUE,
--                            created_at       TIMESTAMPTZ DEFAULT now()
-- );
--
-- -- 인덱스 (조회 최적화)
-- CREATE INDEX idx_exam_class_diff ON exams(class_id, difficulty);
-- CREATE INDEX idx_question_exam_position ON questions(exam_id, position);

INSERT INTO exams (id, class_id, name, difficulty, created_by, created_at)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', '네트워크 기초 퀴즈', 'EASY',  '22222222-2222-2222-2222-222222222222', now()),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111', '네트워크 중간고사', 'MEDIUM','22222222-2222-2222-2222-222222222222', now());

-- 4) aaaaaaaa… 시험의 문항 3개 (position 오름차순)
INSERT INTO questions
(id, exam_id, qtype, body, choices, answer_key, points, position, case_insensitive, created_at)
VALUES
    ('c1111111-1111-1111-1111-111111111111',
     'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
     'MCQ',
     'OSI 4계층의 명칭은?',
     '["Application","Transport","Network","Data Link"]',
     'Transport',
     5.00, 1, true, now()),

    ('c2222222-2222-2222-2222-222222222222',
     'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
     'SHORT',
     'TCP 연결 수립 시 사용하는 handshake 단계 수?',
     NULL,
     '3', 5.00, 2, true, now()),

    ('c3333333-3333-3333-3333-333333333333',
     'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
     'MCQ',
     '다음 중 전송계층 프로토콜은?',
     '["IP","TCP","ARP","ICMP"]',
     'TCP', 5.00, 3, true, now());


-- 마이그레이션
ALTER TABLE questions
    ALTER COLUMN choices TYPE TEXT;
