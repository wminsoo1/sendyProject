-- delivery 테이블에 deleted 컬럼 추가, 기본값은 'F'
ALTER TABLE delivery
    ADD COLUMN deleted VARCHAR(1) DEFAULT 'F';

-- stopover 테이블에 deleted 컬럼 추가, 기본값은 'F'
ALTER TABLE stop_over
    ADD COLUMN deleted VARCHAR(1) DEFAULT 'F';