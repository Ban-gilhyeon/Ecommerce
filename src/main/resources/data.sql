INSERT IGNORE INTO users (id, email, password, name, role, address)
VALUES
  (1, 'user1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '테스트유저1', 'SELLER', '서울시 테스트구 1 길'),
  (2, 'user2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '테스트유저2', 'SELLER', '서울시 테스트구 2 길'),
  (3, 'user3@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '테스트유저3', 'SELLER', '서울시 테스트구 3 길'),
  (4, 'user4@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '테스트유저4', 'BUYER', '서울시 테스트구 4 길'),
  (5, 'user5@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '테스트유저5', 'BUYER', '서울시 테스트구 5 길'),
  (6, 'user6@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '테스트유저6', 'BUYER', '서울시 테스트구 6 길'),
  (7, 'user7@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '테스트유저7', 'BUYER', '서울시 테스트구 7 길'),
  (8, 'user8@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '테스트유저8', 'BUYER', '서울시 테스트구 8 길'),
  (9, 'user9@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '테스트유저9', 'BUYER', '서울시 테스트구 9 길'),
  (10, 'user10@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '테스트유저10', 'BUYER', '서울시 테스트구 10 길');

INSERT IGNORE INTO brands (id, name, description, owner_id)
VALUES
  (1, '테스트브랜드1', '테스트 브랜드 설명 1', 1),
  (2, '테스트브랜드2', '테스트 브랜드 설명 2', 2),
  (3, '테스트브랜드3', '테스트 브랜드 설명 3', 3),
  (4, '테스트브랜드4', '테스트 브랜드 설명 4', 1),
  (5, '테스트브랜드5', '테스트 브랜드 설명 5', 2),
  (6, '테스트브랜드6', '테스트 브랜드 설명 6', 3),
  (7, '테스트브랜드7', '테스트 브랜드 설명 7', 1),
  (8, '테스트브랜드8', '테스트 브랜드 설명 8', 2),
  (9, '테스트브랜드9', '테스트 브랜드 설명 9', 3),
  (10, '테스트브랜드10', '테스트 브랜드 설명 10', 1);

INSERT IGNORE INTO products (id, brand_id, stock_id, name, price, category, gender)
VALUES
  (1, 1, 1, '테스트상품1', 11000, 'OUTER', 'MEN'),
  (2, 2, 2, '테스트상품2', 12000, 'SHIRTS', 'WOMEN'),
  (3, 3, 3, '테스트상품3', 13000, 'T_SHIRTS', 'UNISEX'),
  (4, 4, 4, '테스트상품4', 14000, 'SWEAT_AND_KNIT', 'MEN'),
  (5, 5, 5, '테스트상품5', 15000, 'PANTS', 'WOMEN'),
  (6, 6, 6, '테스트상품6', 16000, 'HALF_PANTS', 'UNISEX'),
  (7, 7, 7, '테스트상품7', 17000, 'HEAD_WEAR', 'MEN'),
  (8, 8, 8, '테스트상품8', 18000, 'ACCESSORY', 'WOMEN'),
  (9, 9, 9, '테스트상품9', 19000, 'BAG', 'UNISEX'),
  (10, 10, 10, '테스트상품10', 20000, 'SHOE', 'MEN');

INSERT IGNORE INTO product_stocks (id, product_id, size, color, quantity)
VALUES
  (1, 1, 'XS', 'BLACK', 21),
  (2, 2, 'S', 'WHITE', 22),
  (3, 3, 'M', 'GRAY', 23),
  (4, 4, 'L', 'NAVY', 24),
  (5, 5, 'XL', 'GREEN', 25),
  (6, 6, 'XXL', 'BLACK', 26),
  (7, 7, 'XS', 'WHITE', 27),
  (8, 8, 'S', 'GRAY', 28),
  (9, 9, 'M', 'NAVY', 29),
  (10, 10, 'L', 'GREEN', 30);

INSERT IGNORE INTO coupons (id, name, category, expiration_time, discount_type, discount_value)
VALUES
  (1, '테스트쿠폰1', 'OUTER', DATE_ADD(NOW(), INTERVAL 7 DAY), 'PERCENT', 6),
  (2, '테스트쿠폰2', 'SHIRTS', DATE_ADD(NOW(), INTERVAL 14 DAY), 'AMOUNT', 2000),
  (3, '테스트쿠폰3', 'T_SHIRTS', DATE_ADD(NOW(), INTERVAL 21 DAY), 'PERCENT', 8),
  (4, '테스트쿠폰4', 'SWEAT_AND_KNIT', DATE_ADD(NOW(), INTERVAL 28 DAY), 'AMOUNT', 4000),
  (5, '테스트쿠폰5', 'PANTS', DATE_ADD(NOW(), INTERVAL 35 DAY), 'PERCENT', 10),
  (6, '테스트쿠폰6', 'HALF_PANTS', DATE_ADD(NOW(), INTERVAL 42 DAY), 'AMOUNT', 6000),
  (7, '테스트쿠폰7', 'HEAD_WEAR', DATE_ADD(NOW(), INTERVAL 49 DAY), 'PERCENT', 12),
  (8, '테스트쿠폰8', 'ACCESSORY', DATE_ADD(NOW(), INTERVAL 56 DAY), 'AMOUNT', 8000),
  (9, '테스트쿠폰9', 'BAG', DATE_ADD(NOW(), INTERVAL 63 DAY), 'PERCENT', 14),
  (10, '테스트쿠폰10', 'SHOE', DATE_ADD(NOW(), INTERVAL 70 DAY), 'AMOUNT', 10000);

INSERT IGNORE INTO coupon_issues (id, user_id, coupon_id)
VALUES
  (1, 1, 1),
  (2, 2, 2),
  (3, 3, 3),
  (4, 4, 4),
  (5, 5, 5),
  (6, 6, 6),
  (7, 7, 7),
  (8, 8, 8),
  (9, 9, 9),
  (10, 10, 10);
