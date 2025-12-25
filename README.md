# Medical Record Management System - Backend API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

Hệ thống backend quản lý hồ sơ bệnh án và đặt lịch khám bệnh, được xây dựng bằng Spring Boot với kiến trúc RESTful API.

## 📋 Mục lục

- [Tổng quan](#-tổng-quan)
- [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
- [Yêu cầu hệ thống](#-yêu-cầu-hệ-thống)
- [Cài đặt](#-cài-đặt)
- [Cấu hình](#-cấu-hình)
- [Chạy ứng dụng](#-chạy-ứng-dụng)
- [API Documentation](#-api-documentation)
- [Cấu trúc dự án](#-cấu-trúc-dự-án)
- [Docker Deployment](#-docker-deployment)
- [Features](#-features)

## 🎯 Tổng quan

**Medical Record Management System Backend** là hệ thống API phục vụ cho ứng dụng quản lý phòng khám, bao gồm:

- 🏥 Quản lý lịch hẹn khám bệnh
- 📋 Quản lý hồ sơ bệnh án điện tử
- 👨‍⚕️ Quản lý nhân viên y tế và lịch làm việc
- 👤 Quản lý bệnh nhân
- 🔔 Thông báo đẩy qua Firebase Cloud Messaging
- 💳 Tích hợp thanh toán SEPAY
- 🤖 Tích hợp Gemini AI để hỗ trợ bệnh nhân
- 📄 Xuất hóa đơn PDF

## 🛠 Công nghệ sử dụng

### Core Framework

- **Spring Boot 3.3.0** - Framework chính
- **Spring Data JPA** - ORM và truy vấn database
- **Spring Security** - Xác thực và phân quyền
- **Spring Web** - RESTful API
- **Spring WebFlux** - Reactive programming

### Database & Storage

- **MySQL 8.0** - Cơ sở dữ liệu chính
- **Supabase Storage** - Lưu trữ file và hình ảnh

### Authentication & Security

- **JWT (JSON Web Tokens)** - Token-based authentication
- **Spring Security** - Security framework

### Documentation & Monitoring

- **SpringDoc OpenAPI 2.5.0** - API documentation (Swagger UI)
- **Spring Boot Actuator** - Health checks và monitoring

### PDF & Reporting

- **JasperReports 7.0.3** - Tạo và xuất PDF reports
- **Eclipse JDT Compiler** - Compiler cho JasperReports

### Third-party Services

- **Firebase Admin SDK 9.3.0** - Push notifications
- **Gemini AI API** - AI integration
- **SEPAY** - Payment gateway integration

### Development Tools

- **Lombok** - Giảm boilerplate code
- **MapStruct 1.5.5** - Object mapping
- **Spring Boot DevTools** - Hot reload
- **Maven** - Build tool

## 💻 Yêu cầu hệ thống

- **Java**: JDK 17 hoặc cao hơn
- **Maven**: 3.6+ (hoặc sử dụng Maven Wrapper đã có sẵn)
- **MySQL**: 8.0+
- **Docker** (tùy chọn): Để chạy với Docker
- **Hệ điều hành**: Windows, macOS, hoặc Linux

## 📦 Cài đặt

### 1. Clone repository

```bash
git clone https://github.com/hmhbrian/Medical_Record_Management_System_BE.git
cd ClinicBooking
```

### 2. Cài đặt dependencies

Sử dụng Maven Wrapper (khuyến nghị):

```bash
# Windows
./mvnw.cmd clean install

# macOS/Linux
./mvnw clean install
```

Hoặc sử dụng Maven đã cài đặt:

```bash
mvn clean install
```

## ⚙️ Cấu hình

### 1. Tạo file `.env`

Tạo file `.env` trong thư mục root với nội dung sau:

```properties
# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/clinicbooking?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USER=your_db_username
DB_PASS=your_db_password

# JWT Configuration
JWT_SECRET=your_jwt_secret_key_at_least_256_bits

# Security Basic Auth (For initial setup)
SEC_NAME=admin
SEC_PASS=your_admin_password

# Supabase Configuration
SUPABASE_URL=https://your-project.supabase.co
SUPABASE_SERVICE_KEY=your_supabase_service_key
SUPABASE_STORAGE_BUCKETNAME=hospital

# Gemini AI Configuration
GEMINI_API_KEY=your_gemini_api_key

# SEPAY Configuration
SEPAY_API_KEY=your_sepay_api_key
SEPAY_ACCOUNT_NUMBER=your_account_number
SEPAY_ACCOUNT_NAME=your_account_name
SEPAY_BANK_CODE=your_bank_code
SEPAY_BANK_NAME=your_bank_name
```

### 2. Cấu hình Firebase

Đặt file `firebase-service-account.json` vào thư mục `src/main/resources/`

## 🚀 Chạy ứng dụng

### Development Mode

```bash
# Windows
./mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

### Production Build

```bash
# Build JAR file
./mvnw clean package -DskipTests

# Run JAR file
java -jar target/ClinicBooking-0.0.1-SNAPSHOT.jar
```

Ứng dụng sẽ chạy tại: `http://localhost:8080`

## 📚 API Documentation

Sau khi chạy ứng dụng, truy cập Swagger UI tại:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON spec:

```
http://localhost:8080/v3/api-docs
```

### Health Check

```
http://localhost:8080/actuator/health
```

## 📁 Cấu trúc dự án

```
ClinicBooking/
├── src/
│   ├── main/
│   │   ├── java/com/example/clinicbooking/
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── service/              # Business Logic
│   │   │   ├── repository/           # Data Access Layer (JPA)
│   │   │   ├── entity/               # JPA Entities
│   │   │   ├── DTO/                  # Data Transfer Objects
│   │   │   ├── Mapper/               # Object Mappers (MapStruct)
│   │   │   ├── config/               # Configuration Classes
│   │   │   ├── security/             # Security Configuration
│   │   │   ├── exceptions/           # Custom Exceptions
│   │   │   └── Utils/                # Utility Classes
│   │   └── resources/
│   │       ├── application.properties
│   │       └── firebase-service-account.json
│   └── test/                         # Unit & Integration Tests
├── mysql-init/                       # MySQL initialization scripts
├── .env                              # Environment variables
├── Dockerfile                        # Docker configuration
├── docker-compose.yml                # Docker Compose setup
├── pom.xml                           # Maven dependencies
└── README.md
```

### Kiến trúc Layer

```
┌─────────────────────────────────────┐
│     Controller Layer (REST API)     │
├─────────────────────────────────────┤
│      Service Layer (Business)       │
├─────────────────────────────────────┤
│   Repository Layer (Data Access)    │
├─────────────────────────────────────┤
│      Entity Layer (Database)        │
└─────────────────────────────────────┘
```

## 🐳 Docker Deployment

### Sử dụng Docker Compose (Khuyến nghị)

Docker Compose sẽ tự động khởi động cả MySQL database và ứng dụng backend:

```bash
# Build và chạy
docker-compose up -d

# Xem logs
docker-compose logs -f

# Dừng services
docker-compose down

# Dừng và xóa volumes
docker-compose down -v
```

Services được expose:

- **API Backend**: `http://localhost:8080`
- **MySQL Database**: `localhost:3307` (mapped từ container port 3306)

### Build Docker Image riêng

```bash
# Build image
docker build -t clinic-booking:latest .

# Run container
docker run -p 8080:8080 \
  --env-file .env \
  clinic-booking:latest
```

## ✨ Features

### 1. 🔐 Authentication & Authorization

- JWT-based authentication
- Role-based access control (RBAC)
- Secure password encryption

### 2. 📅 Appointment Management

- Đặt lịch hẹn online
- Walk-in appointments
- Xác nhận lịch hẹn qua thông báo đẩy
- Quản lý lịch tái khám

### 3. 📋 Medical Records

- Tạo và quản lý hồ sơ bệnh án điện tử
- Lưu trữ kết quả khám, chẩn đoán (ICD-10)
- Tạo và quản lý các chỉ định y tế
- Kê đơn thuốc điện tử
- Lịch sử khám bệnh của bệnh nhân

### 4. 👨‍⚕️ Doctor Management

- Quản lý thông tin bác sĩ
- Lịch làm việc của bác sĩ
- Quản lý ca khám và time slots

### 5. 💊 Medication & Prescription

- Danh mục thuốc
- Kê đơn thuốc
- Lịch sử đơn thuốc

### 6. 🔔 Notifications

- Push notifications qua Firebase FCM
- Thông báo xác nhận lịch hẹn
- Thông báo tái khám

### 7. 💳 Payment Integration

- Tích hợp cổng thanh toán SEPAY
- Theo dõi trạng thái thanh toán
- Lịch sử giao dịch

### 8. 📄 Reports

- Xuất hóa đơn PDF
- Báo cáo thống kê

### 9. 🤖 AI Integration

- Tích hợp Gemini AI
- Hỗ trợ phân tích và gợi ý

## 🔒 Security

- **JWT Authentication**: Mỗi request yêu cầu token hợp lệ
- **Password Encryption**: BCrypt hashing
- **CORS Configuration**: Cấu hình CORS cho web và mobile app
- **SQL Injection Prevention**: JPA Parameterized Queries
- **Environment Variables**: Sensitive data trong `.env`

**Note**: Đảm bảo cấu hình tất cả biến môi trường trong file `.env` trước khi chạy ứng dụng lần đầu tiên.
