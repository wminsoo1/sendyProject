-- Driver 테이블에 balance 컬럼 추가, 기본값은 0.00
ALTER TABLE driver
    ADD COLUMN balance DECIMAL(19, 2) DEFAULT 0.00;

-- Driver 테이블에 imageUrl 컬럼 추가, 기본값은 NULL
ALTER TABLE driver
    ADD COLUMN image_url VARCHAR(255) NULL;