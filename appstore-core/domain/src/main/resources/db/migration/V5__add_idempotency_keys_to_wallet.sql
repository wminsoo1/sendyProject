-- Wallet 테이블에 chargeIdempotencyKey와 paymentIdempotencyKey 컬럼 추가, 각 컬럼은 고유한 멱등키로 설정
ALTER TABLE wallet
    ADD COLUMN charge_idempotency_key VARCHAR(255) UNIQUE,
    ADD COLUMN payment_idempotency_key VARCHAR(255) UNIQUE;
