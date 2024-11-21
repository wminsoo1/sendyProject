-- delivery 테이블: 생성자와 수정자를 기록하기 위한 컬럼 추가
ALTER TABLE delivery ADD COLUMN created_by VARCHAR(255);
ALTER TABLE delivery ADD COLUMN modified_by VARCHAR(255);

-- driver 테이블: 생성자와 수정자를 기록하기 위한 컬럼 추가
ALTER TABLE driver ADD COLUMN created_by VARCHAR(255);
ALTER TABLE driver ADD COLUMN modified_by VARCHAR(255);

-- member 테이블: 생성자와 수정자를 기록하기 위한 컬럼 추가
ALTER TABLE member ADD COLUMN created_by VARCHAR(255);
ALTER TABLE member ADD COLUMN modified_by VARCHAR(255);

-- wallet 테이블: 생성자와 수정자를 기록하기 위한 컬럼 추가
ALTER TABLE wallet ADD COLUMN created_by VARCHAR(255);
ALTER TABLE wallet ADD COLUMN modified_by VARCHAR(255);

-- stop_over 테이블: 생성자와 수정자를 기록하기 위한 컬럼 추가
ALTER TABLE stop_over ADD COLUMN created_by VARCHAR(255);
ALTER TABLE stop_over ADD COLUMN modified_by VARCHAR(255);
