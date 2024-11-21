-- member 테이블: 회원 정보를 저장
CREATE TABLE member (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        provide_id BIGINT DEFAULT NULL,
                        email VARCHAR(255) DEFAULT NULL,
                        member_name VARCHAR(255) NOT NULL,
                        password VARCHAR(255) DEFAULT NULL,
                        provider ENUM('KAKAO') DEFAULT NULL,
                        roles ENUM('ADMIN', 'DRIVER', 'USER') DEFAULT NULL,
                        created_at DATETIME(6) DEFAULT NULL,
                        updated_at DATETIME(6) DEFAULT NULL,
                        PRIMARY KEY (id),
                        UNIQUE KEY UK_member_name (member_name),
                        UNIQUE KEY UK_member_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- driver 테이블: 운전자 정보를 저장
CREATE TABLE driver (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        driver_name VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        roles ENUM('ADMIN', 'DRIVER', 'USER') DEFAULT NULL,
                        vehicle_type ENUM('CARGO', 'FREEZER', 'LIFT', 'REFRIGERATED', 'TOP_CAR', 'WATERPROOF_COVER', 'WING_BODY') DEFAULT NULL,
                        vehicle_weight ENUM('DAMAS', 'FIVE_TON', 'ONE_TON', 'RAMBO', 'THREE_TON') DEFAULT NULL,
                        created_at DATETIME(6) DEFAULT NULL,
                        updated_at DATETIME(6) DEFAULT NULL,
                        PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- wallet 테이블: 사용자 지갑 정보를 저장
CREATE TABLE wallet (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        member_id BIGINT NOT NULL,
                        balance DECIMAL(38, 2) DEFAULT NULL,
                        card_company ENUM('HANA', 'IBK', 'KB', 'LOTTE', 'NH', 'SAMSUNG', 'SHINHAN', 'WOORI') DEFAULT NULL,
                        created_at DATETIME(6) DEFAULT NULL,
                        updated_at DATETIME(6) DEFAULT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- delivery 테이블: 배달 정보를 저장
CREATE TABLE delivery (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          member_id BIGINT NOT NULL,
                          driver_id BIGINT DEFAULT NULL,
                          delivery_fee DECIMAL(38, 2) NOT NULL,
                          drop_latitude DOUBLE DEFAULT NULL,
                          drop_longitude DOUBLE DEFAULT NULL,
                          pick_up_latitude DOUBLE DEFAULT NULL,
                          pick_up_longitude DOUBLE DEFAULT NULL,
                          created_at DATETIME(6) DEFAULT NULL,
                          updated_at DATETIME(6) DEFAULT NULL,
                          delivery_date DATETIME(6) NOT NULL,
                          delivery_options VARCHAR(255) DEFAULT NULL,
                          drop_address VARCHAR(255) DEFAULT NULL,
                          pick_up_address VARCHAR(255) DEFAULT NULL,
                          reservation_number VARCHAR(255) NOT NULL,
                          delivery_personal_category ENUM('PERSONAL_GENERAL_CARGO', 'PERSONAL_MINI_CARGO', 'PERSONAL_MOVING_CARGO') DEFAULT NULL,
                          delivery_status ENUM('ASSIGNMENT_COMPLETED', 'ASSIGNMENT_PENDING', 'CANCELED', 'DELIVERING', 'DELIVERY_COMPLETED', 'PAYMENT_PENDING') DEFAULT NULL,
                          delivery_type ENUM('BUSINESS', 'PERSONAL') DEFAULT NULL,
                          vehicle_type ENUM('CARGO', 'FREEZER', 'LIFT', 'REFRIGERATED', 'TOP_CAR', 'WATERPROOF_COVER', 'WING_BODY') DEFAULT NULL,
                          vehicle_weight ENUM('DAMAS', 'FIVE_TON', 'ONE_TON', 'RAMBO', 'THREE_TON') DEFAULT NULL,
                          PRIMARY KEY (id),
                          UNIQUE KEY UK_reservation_number (reservation_number),
                          KEY idx_member_delivery_status (member_id, delivery_status),
                          FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
                          FOREIGN KEY (driver_id) REFERENCES driver(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- stop_over 테이블: 배달 경유지 정보를 저장
CREATE TABLE stop_over (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           delivery_id BIGINT DEFAULT NULL,
                           drop_latitude DOUBLE DEFAULT NULL,
                           drop_longitude DOUBLE DEFAULT NULL,
                           pick_up_latitude DOUBLE DEFAULT NULL,
                           pick_up_longitude DOUBLE DEFAULT NULL,
                           drop_address VARCHAR(255) DEFAULT NULL,
                           pick_up_address VARCHAR(255) DEFAULT NULL,
                           PRIMARY KEY (id),
                           KEY FK_delivery (delivery_id),
                           CONSTRAINT FK_delivery FOREIGN KEY (delivery_id) REFERENCES delivery(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
