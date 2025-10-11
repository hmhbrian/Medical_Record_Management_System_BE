-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: clinicbooking
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `clinicbooking`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `clinicbooking` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `clinicbooking`;

--
-- Dumping data for table `appointment_status`
--

LOCK TABLES `appointment_status` WRITE;
/*!40000 ALTER TABLE `appointment_status` DISABLE KEYS */;
INSERT INTO `appointment_status` VALUES (1,1,1,NULL,3,'2025-05-16 09:29:47'),(2,1,2,'',4,'2025-05-16 09:41:30'),(8,6,1,NULL,3,'2025-05-19 00:17:16'),(13,7,1,NULL,3,'2025-05-25 02:11:33'),(14,6,4,'Bận đột xuất',3,'2025-05-25 02:12:04'),(15,8,1,NULL,3,'2025-08-22 11:11:38'),(16,9,1,NULL,9,'2025-08-24 04:00:07'),(17,10,1,NULL,3,'2025-08-25 06:57:26'),(18,7,4,'Bận đột xuất',3,'2025-08-25 06:58:13');
/*!40000 ALTER TABLE `appointment_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES (1,1,1,2,'2025-05-16 09:29:47',3,'LH1'),(6,1,1,152,'2025-05-19 00:17:16',15,'LH6'),(7,1,2,253,'2025-05-25 02:11:33',28,'LH7'),(8,1,3,316,'2025-08-22 11:11:38',37,'LH8'),(9,3,3,315,'2025-08-24 04:00:07',45,'LH9'),(10,1,1,306,'2025-08-25 06:57:26',61,'LH10');
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `beds`
--

LOCK TABLES `beds` WRITE;
/*!40000 ALTER TABLE `beds` DISABLE KEYS */;
INSERT INTO `beds` VALUES (1,17,'G0001','2025-10-06 16:50:34',110000,1);
/*!40000 ALTER TABLE `beds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cashier`
--

LOCK TABLES `cashier` WRITE;
/*!40000 ALTER TABLE `cashier` DISABLE KEYS */;
/*!40000 ALTER TABLE `cashier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'Khoa Nội','Chuyên điều trị các bệnh nội khoa','027412345602',1,1,'2024-10-01 08:44:25.422000'),(2,'Khoa Ngoại','Chuyên phẫu thuật và ngoại khoa','027425341602',1,4,'2024-09-02 08:44:25.422000'),(3,'Khoa Nhi','Chăm sóc và điều trị cho trẻ em','027652441602',1,NULL,'2024-09-15 08:44:25.422000'),(5,'Khoa Sản','Chăm sóc sức khỏe sinh sản và phụ nữ mang thai','027652321602',1,3,'2024-09-15 08:44:25.422000');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `doctor_schedules`
--

LOCK TABLES `doctor_schedules` WRITE;
/*!40000 ALTER TABLE `doctor_schedules` DISABLE KEYS */;
INSERT INTO `doctor_schedules` VALUES (1,1,2,3,'2025-05-10','ACTIVE',15,0),(2,1,1,3,'2025-05-12','ACTIVE',15,1),(52,2,2,2,'2025-05-13','ACTIVE',15,0),(53,2,1,1,'2025-05-14','ACTIVE',15,0),(102,1,2,3,'2025-05-20','ACTIVE',15,0),(152,1,3,1,'2025-05-20','ACTIVE',10,0),(153,1,1,1,'2025-05-21','ACTIVE',14,0),(154,1,2,3,'2025-05-22','ACTIVE',14,0),(155,1,3,3,'2025-05-22','ACTIVE',10,0),(156,1,1,3,'2025-05-23','ACTIVE',12,0),(157,1,2,3,'2025-05-23','ACTIVE',12,0),(158,1,2,3,'2025-05-24','ACTIVE',12,0),(202,4,1,3,'2025-05-29','ACTIVE',15,0),(252,2,1,2,'2025-05-26','ACTIVE',15,0),(253,2,1,2,'2025-05-27','ACTIVE',15,0),(254,2,2,2,'2025-05-27','ACTIVE',10,0),(255,2,2,6,'2025-05-28','ACTIVE',15,0),(256,2,3,6,'2025-05-28','ACTIVE',8,0),(257,2,1,2,'2025-05-30','ACTIVE',15,0),(258,2,2,2,'2025-05-30','ACTIVE',15,0),(259,2,1,2,'2025-06-01','ACTIVE',15,0),(260,2,2,6,'2025-06-02','ACTIVE',15,0),(261,2,3,6,'2025-06-02','ACTIVE',8,0),(262,1,1,1,'2025-05-27','ACTIVE',15,0),(263,1,1,1,'2025-05-28','ACTIVE',15,0),(264,1,2,1,'2025-05-28','ACTIVE',15,0),(265,1,2,7,'2025-05-29','ACTIVE',15,0),(266,1,3,7,'2025-05-29','ACTIVE',8,0),(302,1,2,3,'2025-08-24','ACTIVE',20,0),(303,1,1,3,'2025-08-24','ACTIVE',20,0),(304,1,2,3,'2025-08-25','ACTIVE',20,0),(305,1,3,3,'2025-08-25','ACTIVE',20,0),(306,1,1,2,'2025-08-27','ACTIVE',20,0),(307,1,2,2,'2025-08-27','ACTIVE',20,0),(308,2,2,5,'2025-08-24','ACTIVE',20,0),(309,2,3,5,'2025-08-24','ACTIVE',20,0),(310,2,2,5,'2025-08-25','ACTIVE',20,0),(311,2,1,5,'2025-08-26','ACTIVE',20,0),(312,2,2,5,'2025-08-26','ACTIVE',20,0),(313,3,1,7,'2025-08-24','ACTIVE',20,0),(314,3,2,7,'2025-08-24','ACTIVE',20,0),(315,3,2,7,'2025-08-25','ACTIVE',20,0),(316,3,3,7,'2025-08-25','ACTIVE',20,0);
/*!40000 ALTER TABLE `doctor_schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `doctor_schedules_seq`
--

LOCK TABLES `doctor_schedules_seq` WRITE;
/*!40000 ALTER TABLE `doctor_schedules_seq` DISABLE KEYS */;
INSERT INTO `doctor_schedules_seq` VALUES (1);
/*!40000 ALTER TABLE `doctor_schedules_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES (1,1,'DOC1',1,9,'Chứng chỉ Nội khoa','Bộ Y tế','2015-08-20'),(2,2,'DOC2',1,10,'Chứng chỉ Nội khoa','Bộ Y tế','2015-08-20'),(3,3,'DOC3',7,10,'Chứng chỉ Nội khoa','Bộ Y tế','2020-08-20'),(4,4,'DOC4',8,11,'Chứng chỉ Nội khoa','Bộ Y tế','2020-08-20'),(5,6,'DOC5',1,9,'Chứng chỉ Nội khoa','Bộ Y tế','2015-08-20');
/*!40000 ALTER TABLE `doctors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `drugtype`
--

LOCK TABLES `drugtype` WRITE;
/*!40000 ALTER TABLE `drugtype` DISABLE KEYS */;
INSERT INTO `drugtype` VALUES (6,'Chống dị ứng'),(3,'Giảm đau'),(1,'Kháng sinh'),(4,'Tiểu đường'),(7,'Tiêu hóa'),(2,'Tim mạch'),(5,'Vitamin');
/*!40000 ALTER TABLE `drugtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `feedbacks`
--

LOCK TABLES `feedbacks` WRITE;
/*!40000 ALTER TABLE `feedbacks` DISABLE KEYS */;
INSERT INTO `feedbacks` VALUES (1,1,5,'Bác sĩ tận tâm và nhiệt tình!',NULL),(2,1,5,'Y tá tận tâm và nhiệt tình!','2025-05-17 10:59:01'),(3,1,4,'Cơ sở vật chất ổn','2025-05-17 15:20:46'),(4,1,4,'Dịch vụ tốt','2025-05-19 00:18:43'),(5,4,5,'Cơ sở vật chất hiện đại','2025-05-24 04:15:34'),(6,1,4,'Cơ sở vật chất tốt','2025-05-25 02:12:36'),(7,1,5,'Dịch vụ tốt','2025-08-25 06:59:09');
/*!40000 ALTER TABLE `feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `imagingstaff`
--

LOCK TABLES `imagingstaff` WRITE;
/*!40000 ALTER TABLE `imagingstaff` DISABLE KEYS */;
/*!40000 ALTER TABLE `imagingstaff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `imagingtests`
--

LOCK TABLES `imagingtests` WRITE;
/*!40000 ALTER TABLE `imagingtests` DISABLE KEYS */;
/*!40000 ALTER TABLE `imagingtests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `imagingtypes`
--

LOCK TABLES `imagingtypes` WRITE;
/*!40000 ALTER TABLE `imagingtypes` DISABLE KEYS */;
INSERT INTO `imagingtypes` VALUES (1,'X-quang ngực thẳng',300000,'Chẩn đoán các bệnh lý phổi và tim mạch',1,'IMG1',1),(2,'Siêu âm ổ bụng',350000,'Đánh giá các cơ quan trong ổ bụng',1,'IMG2',1),(3,'CT Scanner vùng bụng',1200000,'Chẩn đoán chi tiết trước phẫu thuật ngoại khoa',2,'IMG3',1),(4,'Siêu âm tim',400000,'Đánh giá bệnh lý tim mạch ở trẻ em',3,'IMG4',1),(5,'Siêu âm thai',300000,'Theo dõi sự phát triển của thai nhi',5,'IMG5',1),(6,'Siêu âm sản phụ khoa',350000,'Đánh giá sức khỏe sinh sản',5,'IMG6',1);
/*!40000 ALTER TABLE `imagingtypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `inpatient_records`
--

LOCK TABLES `inpatient_records` WRITE;
/*!40000 ALTER TABLE `inpatient_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `inpatient_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `labstaffs`
--

LOCK TABLES `labstaffs` WRITE;
/*!40000 ALTER TABLE `labstaffs` DISABLE KEYS */;
INSERT INTO `labstaffs` VALUES (1,'LABS1',8,3,NULL);
/*!40000 ALTER TABLE `labstaffs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `labtests`
--

LOCK TABLES `labtests` WRITE;
/*!40000 ALTER TABLE `labtests` DISABLE KEYS */;
/*!40000 ALTER TABLE `labtests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `medical_records`
--

LOCK TABLES `medical_records` WRITE;
/*!40000 ALTER TABLE `medical_records` DISABLE KEYS */;
INSERT INTO `medical_records` VALUES (1,1,1,'2025-05-12',1,'Thiếu máu nhẹ',1,'Đau đầu, chóng mặt'),(2,1,1,'2025-05-24',1,'Loét dạ dày',1,'Đau bụng'),(3,1,3,'2025-08-22',1,'Loét dạ dày',8,'Đau bụng');
/*!40000 ALTER TABLE `medical_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `medicalexamination`
--

LOCK TABLES `medicalexamination` WRITE;
/*!40000 ALTER TABLE `medicalexamination` DISABLE KEYS */;
INSERT INTO `medicalexamination` VALUES (1,'Khám nội tổng quát',200000,'Khám và tư vấn sức khỏe chung',1,'EXA1',1),(2,'Khám ngoại tổng quát',250000,'Khám, chẩn đoán và tư vấn ngoại khoa',2,'EXA2',1),(3,'Khám nhi tổng quát',180000,'Khám sức khỏe trẻ em',3,'EXA3',1),(4,'Khám sản phụ khoa',220000,'Khám sức khỏe sinh sản và phụ nữ mang thai',5,'EXA4',1);
/*!40000 ALTER TABLE `medicalexamination` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `medicines`
--

LOCK TABLES `medicines` WRITE;
/*!40000 ALTER TABLE `medicines` DISABLE KEYS */;
INSERT INTO `medicines` VALUES (1,'Paracetamol 500 mg','viên','2025-07-24',2000,100,2000,'500 mg','Traphaco',0,'2023-07-24',3,'Paracetamol','Viên nén'),(2,'Amoxicillin 500 mg','viên','2025-08-24',1500,100,1500,'500 mg','DHG Pharma',0,'2023-08-24',1,'Amoxicillin','Viên nén'),(3,'Ascorbic acid 500mg','viên','2025-08-01',1000,50,1000,'500 mg','Traphaco',0,'2023-08-01',5,'Ascorbic acid','Viên nén'),(4,'Ibuprofen 400mg','viên','2025-11-20',2500,100,2500,'400 mg','Sanofi',0,'2023-11-20',3,'Ibuprofen','Viên nén'),(5,'Acetylsalicylic acid 81mg','viên','2025-05-30',1800,100,1800,'81 mg','Bayer',0,'2023-05-30',2,'Acetylsalicylic acid','Viên nén'),(6,'Metformin hydrochloride 500mg','viên','2025-12-01',3000,100,3000,'500 mg','US Pharma',0,'2023-12-01',4,'Metformin hydrochloride','Viên nén'),(7,'Azithromycin 500mg','viên','0202-10-05',5000,100,5000,'500 mg','Pfizer',0,'2022-10-05',1,'Azithromycin','Viên nén'),(8,'Loratadine 10mg','viên','2026-02-18',1200,50,1200,'10 mg','Stada',0,'2026-02-18',6,'Loratadine','Viên nén'),(9,'Cefixime 200mg','viên','2025-09-15',3500,100,3500,'200 mg','Domesco',0,'2023-09-15',1,'Cefixime','Viên nén'),(10,'Omeprazole 20mg','viên','2026-03-01',2200,100,2200,'20 mg','Mekophar',0,'2024-03-01',7,'Omeprazole','Viên nang');
/*!40000 ALTER TABLE `medicines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `nurses`
--

LOCK TABLES `nurses` WRITE;
/*!40000 ALTER TABLE `nurses` DISABLE KEYS */;
INSERT INTO `nurses` VALUES (1,'NUR1',7,2);
/*!40000 ALTER TABLE `nurses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (1,3,'PAT1','Tiền sử cao huyết áp','BH123456789'),(3,9,'PAT2','Bệnh tim bẩm sinh','SV1234567'),(4,10,'PAT4','',''),(5,12,'PAT5','Phổi yếu','SV1526425888'),(6,13,'PAT6','',''),(7,15,'PAT7','',''),(8,16,'PAT8','',''),(9,17,'PAT9','','');
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `paymentdetails`
--

LOCK TABLES `paymentdetails` WRITE;
/*!40000 ALTER TABLE `paymentdetails` DISABLE KEYS */;
/*!40000 ALTER TABLE `paymentdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `pharmacystaff`
--

LOCK TABLES `pharmacystaff` WRITE;
/*!40000 ALTER TABLE `pharmacystaff` DISABLE KEYS */;
INSERT INTO `pharmacystaff` VALUES (1,'PHA1',5,3);
/*!40000 ALTER TABLE `pharmacystaff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `prescriptiondetails`
--

LOCK TABLES `prescriptiondetails` WRITE;
/*!40000 ALTER TABLE `prescriptiondetails` DISABLE KEYS */;
INSERT INTO `prescriptiondetails` VALUES (1,1,1,4,'2 lần/ngày','Sau bữa ăn'),(2,1,5,2,'1 lần/ngày','Trước khi ngủ'),(3,2,5,9,'3 lần/ngày','Sau bữa ăn'),(4,2,8,6,'2 lần/ngày','Trước khi ăn'),(5,2,10,3,'1 lần/ngày','Sau khi ăn bữa trưa');
/*!40000 ALTER TABLE `prescriptiondetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES (1,1,NULL,1,1,'NEW','2025-05-24'),(2,2,NULL,1,1,'NEW','2025-05-24');
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `receptionist`
--

LOCK TABLES `receptionist` WRITE;
/*!40000 ALTER TABLE `receptionist` DISABLE KEYS */;
/*!40000 ALTER TABLE `receptionist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `resultexamination`
--

LOCK TABLES `resultexamination` WRITE;
/*!40000 ALTER TABLE `resultexamination` DISABLE KEYS */;
/*!40000 ALTER TABLE `resultexamination` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,1,1,'Phòng A1',1),(2,1,1,'Phòng A2',1),(3,2,1,'Phòng B1',1),(4,2,1,'Phòng B2',1),(5,2,8,'Phòng xét nghiệm 1',1),(6,1,5,'Phòng A3',1),(7,1,5,'Phòng A4',1),(8,1,5,'Phòng A5',1),(9,1,5,'Phòng A6',1),(10,2,6,'Phòng B3',1),(11,2,6,'Phòng B4',1),(12,2,6,'Phòng B5',1),(13,2,6,'Phòng B6',1),(14,2,6,'Phòng B7',1),(15,3,6,'Phòng C1',1),(16,3,6,'Phòng C2',1),(17,2,2,'Phòng A22',1);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `roomtypes`
--

LOCK TABLES `roomtypes` WRITE;
/*!40000 ALTER TABLE `roomtypes` DISABLE KEYS */;
INSERT INTO `roomtypes` VALUES (4,'Phòng ICU'),(1,'Phòng khám'),(6,'Phòng khám ngoại'),(5,'Phòng khám nội'),(7,'Phòng siêu âm'),(2,'Phòng thường'),(3,'Phòng VIP'),(8,'Phòng xét nghiệm');
/*!40000 ALTER TABLE `roomtypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `schedule_slot`
--

LOCK TABLES `schedule_slot` WRITE;
/*!40000 ALTER TABLE `schedule_slot` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_slot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `schedule_slots`
--

LOCK TABLES `schedule_slots` WRITE;
/*!40000 ALTER TABLE `schedule_slots` DISABLE KEYS */;
INSERT INTO `schedule_slots` VALUES (1,2,'08:00:00','08:20:00',0),(2,2,'08:20:00','08:40:00',0),(3,2,'08:40:00','09:00:00',1),(4,2,'09:00:00','09:20:00',0),(5,2,'09:20:00','09:40:00',0),(6,2,'09:40:00','10:00:00',0),(7,2,'10:00:00','10:20:00',0),(8,2,'10:20:00','10:40:00',0),(9,2,'10:40:00','11:00:00',0),(10,2,'11:00:00','11:20:00',0),(11,2,'11:20:00','11:40:00',0),(12,2,'11:40:00','12:00:00',0),(13,152,'18:00:00','18:20:00',0),(14,152,'18:20:00','18:40:00',0),(15,152,'18:40:00','19:00:00',1),(16,152,'19:00:00','19:20:00',0),(17,152,'19:20:00','19:40:00',0),(18,152,'19:40:00','20:00:00',0),(19,152,'20:00:00','20:20:00',0),(20,152,'20:20:00','20:40:00',0),(21,152,'20:40:00','21:00:00',0),(22,253,'08:00:00','08:20:00',0),(23,253,'08:20:00','08:40:00',0),(24,253,'08:40:00','09:00:00',0),(25,253,'09:00:00','09:20:00',0),(26,253,'09:20:00','09:40:00',0),(27,253,'09:40:00','10:00:00',0),(28,253,'10:00:00','10:20:00',1),(29,253,'10:20:00','10:40:00',0),(30,253,'10:40:00','11:00:00',0),(31,253,'11:00:00','11:20:00',0),(32,253,'11:20:00','11:40:00',0),(33,253,'11:40:00','12:00:00',0),(34,316,'18:00:00','18:20:00',0),(35,316,'18:20:00','18:40:00',0),(36,316,'18:40:00','19:00:00',0),(37,316,'19:00:00','19:20:00',1),(38,316,'19:20:00','19:40:00',0),(39,316,'19:40:00','20:00:00',0),(40,316,'20:00:00','20:20:00',0),(41,316,'20:20:00','20:40:00',0),(42,316,'20:40:00','21:00:00',0),(43,315,'13:00:00','13:20:00',0),(44,315,'13:20:00','13:40:00',0),(45,315,'13:40:00','14:00:00',1),(46,315,'14:00:00','14:20:00',0),(47,315,'14:20:00','14:40:00',0),(48,315,'14:40:00','15:00:00',0),(49,315,'15:00:00','15:20:00',0),(50,315,'15:20:00','15:40:00',0),(51,315,'15:40:00','16:00:00',0),(52,315,'16:00:00','16:20:00',0),(53,315,'16:20:00','16:40:00',0),(54,315,'16:40:00','17:00:00',0),(55,306,'08:00:00','08:20:00',0),(56,306,'08:20:00','08:40:00',0),(57,306,'08:40:00','09:00:00',0),(58,306,'09:00:00','09:20:00',0),(59,306,'09:20:00','09:40:00',0),(60,306,'09:40:00','10:00:00',0),(61,306,'10:00:00','10:20:00',1),(62,306,'10:20:00','10:40:00',0),(63,306,'10:40:00','11:00:00',0),(64,306,'11:00:00','11:20:00',0),(65,306,'11:20:00','11:40:00',0),(66,306,'11:40:00','12:00:00',0);
/*!40000 ALTER TABLE `schedule_slots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `shift_type`
--

LOCK TABLES `shift_type` WRITE;
/*!40000 ALTER TABLE `shift_type` DISABLE KEYS */;
INSERT INTO `shift_type` VALUES (1,'Sáng','08:00:00','12:00:00'),(2,'Chiều','13:00:00','17:00:00'),(3,'Tối','18:00:00','21:00:00');
/*!40000 ALTER TABLE `shift_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `specialty`
--

LOCK TABLES `specialty` WRITE;
/*!40000 ALTER TABLE `specialty` DISABLE KEYS */;
INSERT INTO `specialty` VALUES (1,1,'Tim mạch','Điều trị các bệnh về tim','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//tim_mach.jpg'),(2,1,'Hô hấp','Chuyên điều trị các bệnh liên quan đến phổi','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//ho_hap.jpg'),(3,2,'Ngoại tổng quát','Phẫu thuật cơ bản','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//Ngoai_tong_quat.jpg'),(4,3,'Nhi hô hấp','Chăm sóc bệnh hô hấp cho trẻ','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//tre_em.png'),(6,2,'Chấn thương chỉnh hình','Điều trị các chấn thương xương khớp','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//chan_thuong_chinh_hinh.jpg'),(7,5,'Thai sản','','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//thai_san.jpg'),(8,2,'Răng hàm mặt','','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//rang_ham_mat.jpg'),(9,2,'Da liễu','','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//Da_lieu.jpg');
/*!40000 ALTER TABLE `specialty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (1,1,4,1),(2,1,5,1),(3,1,7,5),(4,1,8,2),(5,5,11,1),(6,1,14,1),(7,2,18,2),(8,3,19,1);
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `staff_position`
--

LOCK TABLES `staff_position` WRITE;
/*!40000 ALTER TABLE `staff_position` DISABLE KEYS */;
INSERT INTO `staff_position` VALUES (6,'Cashier'),(1,'Doctor'),(3,'Lab Technician'),(2,'NURSE'),(7,'Patient Receptionist'),(5,'Pharmacist'),(4,'Radiology Technician');
/*!40000 ALTER TABLE `staff_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `staff_schedules`
--

LOCK TABLES `staff_schedules` WRITE;
/*!40000 ALTER TABLE `staff_schedules` DISABLE KEYS */;
/*!40000 ALTER TABLE `staff_schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `testtypes`
--

LOCK TABLES `testtypes` WRITE;
/*!40000 ALTER TABLE `testtypes` DISABLE KEYS */;
INSERT INTO `testtypes` VALUES (1,'Xét nghiệm máu tổng quát',200000,'Kiểm tra chỉ số huyết học',1,'TEST1',1),(2,'Xét nghiệm sinh hóa',200000,'Đánh giá chức năng gan, thận',1,'TEST2',1),(3,'Xét nghiệm đông máu',180000,'Phục vụ chuẩn bị phẫu thuật ngoại khoa',2,'TEST3',1),(4,'Xét nghiệm nước tiểu',120000,'Đánh giá chức năng thận và tiết niệu',3,'TEST4',1),(5,'Xét nghiệm nội tiết',250000,'Theo dõi sức khỏe thai kỳ và nội tiết tố',5,'TEST5',1);
/*!40000 ALTER TABLE `testtypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Huỳnh Minh Hoàng','hoanghm4869@gmail.com','0337023824','2003-11-12',0,'TBH TDM','$2a$10$b6E2VmRKJ8pB25uky/vWN.1XnlYvFSOEwK3jtxPchw5nqbLmqQoPC',0,'2025-04-10 00:00:00',NULL),(3,'Nguyễn Văn A','nguyenvanA@example.com','0123456789','1995-05-20',0,'123 Đường Lê Lợi, Tdm, TP HCM','$2a$10$XDLLf84jVbgGduWVA4N/.e2/ZqEVVhZe5d0ljbaxetYsLS.Az55I6',1,'2025-04-10 04:48:35',NULL),(4,'Trần Văn C','tranvanc@hospital.com','0323456789','1985-06-20',0,'123 Lê Lợi, TP.HCM','$2a$10$XiutYothZ4rma/P.J5c2leYOLb/hTj6V677Re5gy5hSWeB1yBe71y',2,'2025-04-10 08:28:32','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1747710399077.png'),(5,'Nguyễn Minh Thuận','Thuannm@example.com','0223456789','1985-06-20',0,'123 Nguyễn Trãi, TP.HCM','$2a$10$o/BAwrm8rlbRUtVKepG8iuhlFxvVMfn6LVhLyaaiLtmdvUAIkGLbC',2,'2025-04-16 08:06:58','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//bs_nam.png'),(7,'Nguyễn Hồng Yến','Yennh@example.com','0423456789','1995-06-20',1,'123 Lý Thái Tổ, TP.HCM','$2a$10$ZRb1sOPqiTR1NgDn7l9ryuU7JbBOT6QnX2.ai.skFTmg/VMVFNu5u',2,'2025-05-18 06:40:23','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//bs_nu.jpg'),(8,'Trần Thu Phương','Phuongtt20@example.com','0523456789','1994-08-20',1,'123 Phan Châu Trinh, TP.HCM','$2a$10$qFTu3C44lJEAz4.AEo/qIOnwihykzMMB5mYTqv0nKcnFePOhvBtxK',2,'2025-05-18 07:36:34','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//bs_nu.jpg'),(9,'Nguyễn Việt Trung','Truntnv@gmail.com','0336013824','2003-06-25',0,'Phú Hòa','$2a$10$kvsD41NR8tMs6ZFfth6wI.ksdSKpySwLVYW2Y/CT/0KoryGjrzxCq',1,'2025-05-19 00:13:09',''),(10,'Nguyễn Thanh Nam','Namnt@gmail.com','0723456789','2002-05-23',0,'Củ Chi','$2a$10$WYEA1.VFcVZYAR1tAqY8vO/Sr/C1Y3bVp6TDVwyKAbQIfPcTuBCQm',1,'2025-05-23 17:17:39',''),(11,'Hồ Ngọc Châu','Chaunh@gmail.com','0823456789','2000-06-24',1,'TP HCM','$2a$10$XDLLf84jVbgGduWVA4N/.e2/ZqEVVhZe5d0ljbaxetYsLS.Az55I6',2,'2025-05-24 16:47:19',NULL),(12,'Nguyễn Ngọc Hà','hann@gmail.com','038023564','2003-11-25',1,'phường Thủ Dầu Một','$2a$10$jtuaGLERzhkK9ScR63RZPO1VLcFgTratjYAmWhWZXz1.PHKX2p3XK',1,'2025-08-21 09:18:07',NULL),(13,'Nguyễn Thanh Thảo','thaont@gmail.com','0320156487','2002-05-14',1,'phường Chánh Hiệp','$2a$10$Ru49ve58Uu1O0CXhf0.Lqu48tpwLWf32xZVJ32FS9VL91up0IulK6',1,'2025-08-21 16:05:53','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1755825874773.jpg'),(14,'Trần Văn C','Thuann1m@example.com','0323456789','1985-06-20',0,'123 Lê Lợi, TP.HCM','$2a$10$iwcOFEhX3qKwdYfG/kQ6QuPxQUokKlxOENmeSJ/VYgu4918xbGLhq',2,'2025-08-22 01:23:35','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1755825850325.jpg'),(15,'Nguyễn Thanh Hằng','hangnt@gmail.com','0320156487','2002-05-14',1,'phường Chánh Hiệp','$2a$10$v4mss5YDekozGY9xkvRQIe/FdEjz8uFNx1U9u84TzjQkASAvkUdpO',1,'2025-08-22 01:26:01',NULL),(16,'Nguyễn Thanh Thu','nguyenvan123@example.com','0320156487','2002-05-14',0,'phường Chánh Hiệp','$2a$10$.cnH7Hx/jTUaCll79EDPk.q5zlRJ1ntCUmGtV9sQSULT8ybVYnCH6',1,'2025-08-22 01:26:52',NULL),(17,'Nguyễn Thanh Thuỷ','thuynt@gmail.com','0320156487','2002-05-14',1,'phường Chánh Hiệp','$2a$10$4iEmWwD6Ju6U/vamg5MbMOjWERSQ2PHCPA67uCq42eqQlKDxYlrtm',1,'2025-08-22 13:10:33',''),(18,'Nguyễn Thị Ngân','ngannt@gmail.com','0337033824','2000-09-28',0,'Chánh Hiệp','$2a$10$aOyLpSBFMRaj1n8MRk2.EOma7w1gsf/gIjKB4aMFkRd4Te./hMaAm',2,'2025-09-28 17:36:54','string'),(19,'Nguyễn Tiến Duy','duynt@gmail.com','0337043824','1999-10-28',1,'Phú Lợi','$2a$10$ixigocA1JDl.IF/Nu7vb4eh6.JAdWX7eZWGfX/J6C.8oDt5YPGtRO',2,'2025-09-28 17:39:05','');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `vitalsigns`
--

LOCK TABLES `vitalsigns` WRITE;
/*!40000 ALTER TABLE `vitalsigns` DISABLE KEYS */;
/*!40000 ALTER TABLE `vitalsigns` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-12  0:07:55
