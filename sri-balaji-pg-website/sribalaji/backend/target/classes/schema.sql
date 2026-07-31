-- Reference schema. With spring.jpa.hibernate.ddl-auto=update, Hibernate will create/update
-- these tables automatically from the entity classes on first run — this file is for reference
-- and for teams who prefer to manage schema migrations by hand (recommended: switch to Flyway
-- or Liquibase before going to production).

CREATE DATABASE IF NOT EXISTS sribalaji_pg;
USE sribalaji_pg;

CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  mobile_number VARCHAR(15) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role ENUM('ADMIN','MANAGER','TENANT') NOT NULL,
  full_name VARCHAR(120),
  email VARCHAR(120),
  active BOOLEAN DEFAULT TRUE
);

CREATE TABLE rooms (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  room_number VARCHAR(20) UNIQUE NOT NULL,
  sharing_type INT,
  ac_type VARCHAR(20),
  base_rent DOUBLE
);

CREATE TABLE beds (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  bed_label VARCHAR(20),
  room_id BIGINT,
  status ENUM('OCCUPIED','VACANT','CLEANING','MAINTENANCE') DEFAULT 'VACANT',
  tenant_id BIGINT,
  FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE TABLE tenants (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  full_name VARCHAR(120),
  phone_number VARCHAR(15),
  email VARCHAR(120),
  room_id BIGINT,
  bed_id BIGINT,
  joining_date DATE,
  monthly_rent DOUBLE,
  deposit_amount DOUBLE,
  active BOOLEAN DEFAULT TRUE,
  aadhaar_url VARCHAR(500),
  pan_url VARCHAR(500),
  photo_url VARCHAR(500),
  agreement_url VARCHAR(500),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (room_id) REFERENCES rooms(id),
  FOREIGN KEY (bed_id) REFERENCES beds(id)
);

ALTER TABLE beds ADD FOREIGN KEY (tenant_id) REFERENCES tenants(id);

CREATE TABLE payments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  tenant_id BIGINT,
  billing_month VARCHAR(7),
  amount DOUBLE,
  status ENUM('PENDING','PAID','FAILED','OVERDUE') DEFAULT 'PENDING',
  payment_method VARCHAR(30),
  razorpay_order_id VARCHAR(100),
  razorpay_payment_id VARCHAR(100),
  due_date DATE,
  paid_date DATE,
  invoice_url VARCHAR(500),
  receipt_url VARCHAR(500),
  FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

CREATE TABLE complaints (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  tenant_id BIGINT,
  category VARCHAR(50),
  description TEXT,
  status ENUM('OPEN','IN_PROGRESS','RESOLVED') DEFAULT 'OPEN',
  assigned_to VARCHAR(120),
  raised_at DATETIME,
  resolved_at DATETIME,
  FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

CREATE TABLE notices (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200),
  category VARCHAR(50),
  body TEXT,
  posted_at DATETIME
);
