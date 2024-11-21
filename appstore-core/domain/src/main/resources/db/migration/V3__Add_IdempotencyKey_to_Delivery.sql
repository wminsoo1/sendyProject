-- 'delivery' 테이블에 'idempotency_key' 컬럼 추가
ALTER TABLE delivery ADD COLUMN idempotency_key VARCHAR(255);

-- 'delivery' 테이블의 'idempotency_key' 컬럼에 고유 인덱스 생성
CREATE UNIQUE INDEX idx_delivery_idempotency_key ON delivery(idempotency_key);