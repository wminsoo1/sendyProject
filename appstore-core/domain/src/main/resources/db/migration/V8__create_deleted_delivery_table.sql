-- deleted_delivery 테이블 생성

CREATE TABLE `deleted_delivery` (
                                    `delivery_fee` DECIMAL(38,2) NOT NULL COMMENT '배달 요금',
                                    `drop_latitude` DOUBLE DEFAULT NULL COMMENT '도착지 위도',
                                    `drop_longitude` DOUBLE DEFAULT NULL COMMENT '도착지 경도',
                                    `pick_up_latitude` DOUBLE DEFAULT NULL COMMENT '픽업지 위도',
                                    `pick_up_longitude` DOUBLE DEFAULT NULL COMMENT '픽업지 경도',
                                    `created_at` DATETIME(6) DEFAULT NULL COMMENT '레코드 생성 시간',
                                    `delivery_date` DATETIME(6) NOT NULL COMMENT '배달 삭제 날짜',
                                    `driver_id` BIGINT DEFAULT NULL COMMENT '배달을 수행한 운전자의 ID',
                                    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '테이블의 기본 키',
                                    `member_id` BIGINT NOT NULL COMMENT '배달을 요청한 회원의 ID',
                                    `updated_at` DATETIME(6) DEFAULT NULL COMMENT '레코드 수정 시간',
                                    `created_by` VARCHAR(255) DEFAULT NULL COMMENT '레코드를 생성한 사용자',
                                    `delivery_options` VARCHAR(255) DEFAULT NULL COMMENT '추가 배달 옵션',
                                    `drop_address` VARCHAR(255) DEFAULT NULL COMMENT '도착지 주소',
                                    `idempotency_key` VARCHAR(255) DEFAULT NULL COMMENT '요청의 멱등성을 보장하기 위한 고유 키',
                                    `modified_by` VARCHAR(255) DEFAULT NULL COMMENT '레코드를 마지막으로 수정한 사용자 또는 시스템',
                                    `pick_up_address` VARCHAR(255) DEFAULT NULL COMMENT '픽업지 주소',
                                    `reservation_number` VARCHAR(255) NOT NULL COMMENT '배달의 고유 예약 번호',
                                    `delivery_personal_category` ENUM('PERSONAL_GENERAL_CARGO','PERSONAL_MINI_CARGO','PERSONAL_MOVING_CARGO') DEFAULT NULL COMMENT '개인 배달 카테고리',
                                    `delivery_status` ENUM('ASSIGNMENT_COMPLETED','ASSIGNMENT_PENDING','CANCELED','DELIVERING','DELIVERY_COMPLETED','PAYMENT_PENDING') DEFAULT NULL COMMENT '배달의 현재 상태',
                                    `delivery_type` ENUM('BUSINESS','PERSONAL') DEFAULT NULL COMMENT '배달 유형: 사업용 또는 개인용',
                                    `vehicle_type` ENUM('CARGO','FREEZER','LIFT','REFRIGERATED','TOP_CAR','WATERPROOF_COVER','WING_BODY') DEFAULT NULL COMMENT '배달에 사용된 차량 유형',
                                    `vehicle_weight` ENUM('DAMAS','FIVE_TON','ONE_TON','RAMBO','THREE_TON') DEFAULT NULL COMMENT '배달에 사용된 차량의 적재 중량',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `UK_reservation_number_deleted` (`reservation_number`),
                                    UNIQUE KEY `UK_idempotency_key_deleted` (`idempotency_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
