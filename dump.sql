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
-- Table structure for table `appointment_status`
--

DROP TABLE IF EXISTS `appointment_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `appointment_id` int DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `appointment_id` (`appointment_id`),
  KEY `update_by` (`update_by`),
  CONSTRAINT `appointment_status_ibfk_1` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
  CONSTRAINT `appointment_status_ibfk_2` FOREIGN KEY (`update_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment_status`
--

LOCK TABLES `appointment_status` WRITE;
/*!40000 ALTER TABLE `appointment_status` DISABLE KEYS */;
INSERT INTO `appointment_status` VALUES (1,1,'Chß╗¥ x├íc nhß║¡n',NULL,3,'2025-05-16 09:29:47'),(2,1,'X├íc nhß║¡n','',4,'2025-05-16 09:41:30'),(8,6,'Chß╗¥ x├íc nhß║¡n',NULL,3,'2025-05-19 00:17:16'),(13,7,'Chß╗¥ x├íc nhß║¡n',NULL,3,'2025-05-25 02:11:33'),(14,6,'Hß╗ºy','Bß║¡n ─æß╗Öt xuß║Ñt',3,'2025-05-25 02:12:04'),(15,8,'Chß╗¥ x├íc nhß║¡n',NULL,3,'2025-08-22 11:11:38'),(16,9,'Chß╗¥ x├íc nhß║¡n',NULL,9,'2025-08-24 04:00:07'),(17,10,'Chß╗¥ x├íc nhß║¡n',NULL,3,'2025-08-25 06:57:26'),(18,7,'Hß╗ºy','Bß║¡n ─æß╗Öt xuß║Ñt',3,'2025-08-25 06:58:13');
/*!40000 ALTER TABLE `appointment_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `doctor_schedule_id` int DEFAULT NULL,
  `present_time` datetime DEFAULT NULL,
  `appointment_time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `patient_id` (`patient_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `doctor_schedule_id` (`doctor_schedule_id`),
  CONSTRAINT `appointments_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `appointments_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `appointments_ibfk_3` FOREIGN KEY (`doctor_schedule_id`) REFERENCES `doctor_schedules` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES (1,1,1,2,'2025-05-16 09:29:47','09:00 - 09:15'),(6,1,1,152,'2025-05-19 00:17:16',' '),(7,1,2,253,'2025-05-25 02:11:33',' '),(8,1,3,316,'2025-08-22 11:11:38','09:00 - 09:15'),(9,3,3,315,'2025-08-24 04:00:07',' '),(10,1,1,306,'2025-08-25 06:57:26',' ');
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `beds`
--

DROP TABLE IF EXISTS `beds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beds` (
  `id` int NOT NULL AUTO_INCREMENT,
  `room_id` int DEFAULT NULL,
  `bed_number` varchar(20) DEFAULT NULL,
  `status` enum('available','occupied') DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `bed_fee` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `room_id` (`room_id`),
  CONSTRAINT `beds_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beds`
--

LOCK TABLES `beds` WRITE;
/*!40000 ALTER TABLE `beds` DISABLE KEYS */;
/*!40000 ALTER TABLE `beds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cashier`
--

DROP TABLE IF EXISTS `cashier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cashier` (
  `id` int NOT NULL AUTO_INCREMENT,
  `casScode` varchar(20) DEFAULT NULL,
  `staff_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_cashier_staff_id` (`staff_id`),
  CONSTRAINT `cashier_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cashier`
--

LOCK TABLES `cashier` WRITE;
/*!40000 ALTER TABLE `cashier` DISABLE KEYS */;
/*!40000 ALTER TABLE `cashier` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_cashier` BEFORE INSERT ON `cashier` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(20);
    SET new_code = CONCAT('CAS',  IFNULL((SELECT MAX(id) FROM cashier), 0) + 1);
    SET NEW.casScode = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `head_doctor_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'Khoa Nß╗Öi','Chuy├¬n ─æiß╗üu trß╗ï c├íc bß╗çnh nß╗Öi khoa',NULL),(2,'Khoa Ngoß║íi','Chuy├¬n phß║½u thuß║¡t v├á ngoß║íi khoa',NULL),(3,'Khoa Nhi','Ch─âm s├│c v├á ─æiß╗üu trß╗ï cho trß║╗ em',NULL),(5,'Khoa Sß║ún','Ch─âm s├│c sß╗⌐c khß╗Åe sinh sß║ún v├á phß╗Ñ nß╗» mang thai',NULL);
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_schedules`
--

DROP TABLE IF EXISTS `doctor_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_schedules` (
  `id` int NOT NULL AUTO_INCREMENT,
  `doctor_id` int DEFAULT NULL,
  `shift_type_id` int DEFAULT NULL,
  `room_id` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `max_patients` int DEFAULT NULL,
  `booked_patients` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `shift_type_id` (`shift_type_id`),
  KEY `room_id` (`room_id`),
  CONSTRAINT `doctor_schedules_ibfk_1` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `doctor_schedules_ibfk_2` FOREIGN KEY (`shift_type_id`) REFERENCES `shift_type` (`id`),
  CONSTRAINT `doctor_schedules_ibfk_3` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=317 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_schedules`
--

LOCK TABLES `doctor_schedules` WRITE;
/*!40000 ALTER TABLE `doctor_schedules` DISABLE KEYS */;
INSERT INTO `doctor_schedules` VALUES (1,1,2,3,'2025-05-10','ACTIVE',15,0),(2,1,1,3,'2025-05-12','ACTIVE',15,1),(52,2,2,2,'2025-05-13','ACTIVE',15,0),(53,2,1,1,'2025-05-14','ACTIVE',15,0),(102,1,2,3,'2025-05-20','ACTIVE',15,0),(152,1,3,1,'2025-05-20','ACTIVE',10,0),(153,1,1,1,'2025-05-21','ACTIVE',14,0),(154,1,2,3,'2025-05-22','ACTIVE',14,0),(155,1,3,3,'2025-05-22','ACTIVE',10,0),(156,1,1,3,'2025-05-23','ACTIVE',12,0),(157,1,2,3,'2025-05-23','ACTIVE',12,0),(158,1,2,3,'2025-05-24','ACTIVE',12,0),(202,4,1,3,'2025-05-29','ACTIVE',15,0),(252,2,1,2,'2025-05-26','ACTIVE',15,0),(253,2,1,2,'2025-05-27','ACTIVE',15,0),(254,2,2,2,'2025-05-27','ACTIVE',10,0),(255,2,2,6,'2025-05-28','ACTIVE',15,0),(256,2,3,6,'2025-05-28','ACTIVE',8,0),(257,2,1,2,'2025-05-30','ACTIVE',15,0),(258,2,2,2,'2025-05-30','ACTIVE',15,0),(259,2,1,2,'2025-06-01','ACTIVE',15,0),(260,2,2,6,'2025-06-02','ACTIVE',15,0),(261,2,3,6,'2025-06-02','ACTIVE',8,0),(262,1,1,1,'2025-05-27','ACTIVE',15,0),(263,1,1,1,'2025-05-28','ACTIVE',15,0),(264,1,2,1,'2025-05-28','ACTIVE',15,0),(265,1,2,7,'2025-05-29','ACTIVE',15,0),(266,1,3,7,'2025-05-29','ACTIVE',8,0),(302,1,2,3,'2025-08-24','ACTIVE',20,0),(303,1,1,3,'2025-08-24','ACTIVE',20,0),(304,1,2,3,'2025-08-25','ACTIVE',20,0),(305,1,3,3,'2025-08-25','ACTIVE',20,0),(306,1,1,2,'2025-08-27','ACTIVE',20,0),(307,1,2,2,'2025-08-27','ACTIVE',20,0),(308,2,2,5,'2025-08-24','ACTIVE',20,0),(309,2,3,5,'2025-08-24','ACTIVE',20,0),(310,2,2,5,'2025-08-25','ACTIVE',20,0),(311,2,1,5,'2025-08-26','ACTIVE',20,0),(312,2,2,5,'2025-08-26','ACTIVE',20,0),(313,3,1,7,'2025-08-24','ACTIVE',20,0),(314,3,2,7,'2025-08-24','ACTIVE',20,0),(315,3,2,7,'2025-08-25','ACTIVE',20,0),(316,3,3,7,'2025-08-25','ACTIVE',20,0);
/*!40000 ALTER TABLE `doctor_schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_schedules_seq`
--

DROP TABLE IF EXISTS `doctor_schedules_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_schedules_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_schedules_seq`
--

LOCK TABLES `doctor_schedules_seq` WRITE;
/*!40000 ALTER TABLE `doctor_schedules_seq` DISABLE KEYS */;
INSERT INTO `doctor_schedules_seq` VALUES (401);
/*!40000 ALTER TABLE `doctor_schedules_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctors`
--

DROP TABLE IF EXISTS `doctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `staff_id` int DEFAULT NULL,
  `doctorcode` varchar(255) DEFAULT NULL,
  `specialty_id` int DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  `certification_name` varchar(255) DEFAULT NULL,
  `issued_by` varchar(255) DEFAULT NULL,
  `issue_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_doctor_staff_id` (`staff_id`),
  KEY `idx_doctor_specialty` (`specialty_id`),
  CONSTRAINT `doctors_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`),
  CONSTRAINT `doctors_ibfk_2` FOREIGN KEY (`specialty_id`) REFERENCES `specialty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES (1,1,'DOC1',1,9,'Chß╗⌐ng chß╗ë Nß╗Öi khoa','Bß╗Ö Y tß║┐','2015-08-20'),(2,2,'DOC2',1,10,'Chß╗⌐ng chß╗ë Nß╗Öi khoa','Bß╗Ö Y tß║┐','2015-08-20'),(3,3,'DOC3',7,10,'Chß╗⌐ng chß╗ë Nß╗Öi khoa','Bß╗Ö Y tß║┐','2020-08-20'),(4,4,'DOC4',8,11,'Chß╗⌐ng chß╗ë Nß╗Öi khoa','Bß╗Ö Y tß║┐','2020-08-20'),(5,6,'DOC5',1,9,'Chß╗⌐ng chß╗ë Nß╗Öi khoa','Bß╗Ö Y tß║┐','2015-08-20');
/*!40000 ALTER TABLE `doctors` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_doctors` BEFORE INSERT ON `doctors` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(20);
    SET new_code = CONCAT('DOC', IFNULL((SELECT MAX(id) FROM doctors), 0) + 1);
    SET NEW.doctorcode = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `feedbacks`
--

DROP TABLE IF EXISTS `feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedbacks` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `patient_id` (`patient_id`),
  CONSTRAINT `feedbacks_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `feedbacks_chk_1` CHECK (((`rating` >= 1) and (`rating` <= 5)))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedbacks`
--

LOCK TABLES `feedbacks` WRITE;
/*!40000 ALTER TABLE `feedbacks` DISABLE KEYS */;
INSERT INTO `feedbacks` VALUES (1,1,5,'B├íc s─⌐ tß║¡n t├óm v├á nhiß╗çt t├¼nh!',NULL),(2,1,5,'Y t├í tß║¡n t├óm v├á nhiß╗çt t├¼nh!','2025-05-17 10:59:01'),(3,1,4,'C╞í sß╗ƒ vß║¡t chß║Ñt ß╗òn','2025-05-17 15:20:46'),(4,1,4,'Dß╗ïch vß╗Ñ tß╗æt','2025-05-19 00:18:43'),(5,4,5,'C╞í sß╗ƒ vß║¡t chß║Ñt hiß╗çn ─æß║íi','2025-05-24 04:15:34'),(6,1,4,'C╞í sß╗ƒ vß║¡t chß║Ñt tß╗æt','2025-05-25 02:12:36'),(7,1,5,'Dß╗ïch vß╗Ñ tß╗æt','2025-08-25 06:59:09');
/*!40000 ALTER TABLE `feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `imagingstaff`
--

DROP TABLE IF EXISTS `imagingstaff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imagingstaff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `imgScode` varchar(20) DEFAULT NULL,
  `staff_id` int DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_imaging_staff_id` (`staff_id`),
  CONSTRAINT `imagingstaff_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imagingstaff`
--

LOCK TABLES `imagingstaff` WRITE;
/*!40000 ALTER TABLE `imagingstaff` DISABLE KEYS */;
/*!40000 ALTER TABLE `imagingstaff` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_imagingstaff` BEFORE INSERT ON `imagingstaff` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(20);
    SET new_code = CONCAT('IMGS', IFNULL((SELECT MAX(id) FROM cashier), 0) + 1);
    SET NEW.imgScode = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `imagingtests`
--

DROP TABLE IF EXISTS `imagingtests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imagingtests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `record_id` int DEFAULT NULL,
  `inpatient_record_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `imagingStaff_id` int DEFAULT NULL,
  `imageType_id` int DEFAULT NULL,
  `requestDate` date DEFAULT NULL,
  `resultDate` date DEFAULT NULL,
  `image_url` text,
  `result` text,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `inpatient_record_id` (`inpatient_record_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `imagingStaff_id` (`imagingStaff_id`),
  KEY `imageType_id` (`imageType_id`),
  CONSTRAINT `imagingtests_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `imagingtests_ibfk_2` FOREIGN KEY (`inpatient_record_id`) REFERENCES `inpatient_records` (`id`),
  CONSTRAINT `imagingtests_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `imagingtests_ibfk_4` FOREIGN KEY (`imagingStaff_id`) REFERENCES `imagingstaff` (`id`),
  CONSTRAINT `imagingtests_ibfk_5` FOREIGN KEY (`imageType_id`) REFERENCES `imagingtypes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imagingtests`
--

LOCK TABLES `imagingtests` WRITE;
/*!40000 ALTER TABLE `imagingtests` DISABLE KEYS */;
/*!40000 ALTER TABLE `imagingtests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `imagingtypes`
--

DROP TABLE IF EXISTS `imagingtypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imagingtypes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `imagingName` varchar(100) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imagingtypes`
--

LOCK TABLES `imagingtypes` WRITE;
/*!40000 ALTER TABLE `imagingtypes` DISABLE KEYS */;
/*!40000 ALTER TABLE `imagingtypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inpatient_records`
--

DROP TABLE IF EXISTS `inpatient_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inpatient_records` (
  `id` int NOT NULL AUTO_INCREMENT,
  `record_id` int DEFAULT NULL,
  `admissionDate` datetime DEFAULT NULL,
  `dischargeDate` datetime DEFAULT NULL,
  `bed_id` int DEFAULT NULL,
  `treatmentPlan` text,
  `status` enum('Admitted','Discharged') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `bed_id` (`bed_id`),
  CONSTRAINT `inpatient_records_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `inpatient_records_ibfk_2` FOREIGN KEY (`bed_id`) REFERENCES `beds` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inpatient_records`
--

LOCK TABLES `inpatient_records` WRITE;
/*!40000 ALTER TABLE `inpatient_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `inpatient_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `labstaffs`
--

DROP TABLE IF EXISTS `labstaffs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `labstaffs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `labScode` varchar(20) DEFAULT NULL,
  `staff_id` int DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  `lab_scode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_lab_staff_id` (`staff_id`),
  CONSTRAINT `labstaffs_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `labstaffs`
--

LOCK TABLES `labstaffs` WRITE;
/*!40000 ALTER TABLE `labstaffs` DISABLE KEYS */;
INSERT INTO `labstaffs` VALUES (1,'LABS1',8,3,NULL);
/*!40000 ALTER TABLE `labstaffs` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_labstaffs` BEFORE INSERT ON `labstaffs` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(15);
    SET new_code = CONCAT('LABS',  IFNULL((SELECT MAX(id) FROM labstaffs), 0) + 1);
    SET NEW.labScode = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `labtests`
--

DROP TABLE IF EXISTS `labtests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `labtests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `record_id` int DEFAULT NULL,
  `inpatient_record_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `labStaff_id` int DEFAULT NULL,
  `testType_id` int DEFAULT NULL,
  `requestDate` date DEFAULT NULL,
  `resultDate` date DEFAULT NULL,
  `result` text,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `inpatient_record_id` (`inpatient_record_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `labStaff_id` (`labStaff_id`),
  KEY `testType_id` (`testType_id`),
  CONSTRAINT `labtests_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `labtests_ibfk_2` FOREIGN KEY (`inpatient_record_id`) REFERENCES `inpatient_records` (`id`),
  CONSTRAINT `labtests_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `labtests_ibfk_4` FOREIGN KEY (`labStaff_id`) REFERENCES `labstaffs` (`id`),
  CONSTRAINT `labtests_ibfk_5` FOREIGN KEY (`testType_id`) REFERENCES `testtypes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `labtests`
--

LOCK TABLES `labtests` WRITE;
/*!40000 ALTER TABLE `labtests` DISABLE KEYS */;
/*!40000 ALTER TABLE `labtests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medical_records`
--

DROP TABLE IF EXISTS `medical_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medical_records` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `visit_date` date DEFAULT NULL,
  `visit_number` int DEFAULT NULL,
  `diagnosis` varchar(255) DEFAULT NULL,
  `appointment_id` int DEFAULT NULL,
  `initial_symptoms` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `patient_id` (`patient_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `FK_mr_ap` (`appointment_id`),
  CONSTRAINT `FK_mr_ap` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
  CONSTRAINT `medical_records_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `medical_records_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_records`
--

LOCK TABLES `medical_records` WRITE;
/*!40000 ALTER TABLE `medical_records` DISABLE KEYS */;
INSERT INTO `medical_records` VALUES (1,1,1,'2025-05-12',1,'Thiß║┐u m├íu nhß║╣',1,'─Éau ─æß║ºu, ch├│ng mß║╖t'),(2,1,1,'2025-05-24',1,'Lo├⌐t dß║í d├áy',1,'─Éau bß╗Ñng'),(3,1,3,'2025-08-22',1,'Lo├⌐t dß║í d├áy',8,'─Éau bß╗Ñng');
/*!40000 ALTER TABLE `medical_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicines`
--

DROP TABLE IF EXISTS `medicines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicines` (
  `id` int NOT NULL AUTO_INCREMENT,
  `medicine_name` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `stock_quantity` int DEFAULT NULL,
  `expiration_date` date DEFAULT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicines`
--

LOCK TABLES `medicines` WRITE;
/*!40000 ALTER TABLE `medicines` DISABLE KEYS */;
INSERT INTO `medicines` VALUES (1,'Paracetamol','vi├¬n',100,'2025-07-24',2000),(2,'Amoxicilin','vi├¬n',50,'2025-08-24',1500),(3,'Vitamin C','vi├¬n',100,'2025-08-01',1000),(4,'Ibuprofen','vi├¬n',8,'2025-11-20',2500),(5,'Aspirin','vi├¬n',20,'2025-05-30',1800),(6,'Metformin','vi├¬n',5,'2025-12-01',3000),(7,'Azithromycin','vi├¬n',3,'0202-10-05',5000),(8,'Loratadine','vi├¬n',40,'2026-02-18',1200),(9,'Cefixime','vi├¬n',7,'2025-09-15',3500),(10,'Omeprazole','vi├¬n',60,'2026-03-01',2200);
/*!40000 ALTER TABLE `medicines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `receiver_id` int DEFAULT NULL,
  `message` text,
  `is_read` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `receiver_id` (`receiver_id`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nurses`
--

DROP TABLE IF EXISTS `nurses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nurses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nursecode` varchar(255) DEFAULT NULL,
  `staff_id` int DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_nurse_staff_id` (`staff_id`),
  CONSTRAINT `nurses_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nurses`
--

LOCK TABLES `nurses` WRITE;
/*!40000 ALTER TABLE `nurses` DISABLE KEYS */;
INSERT INTO `nurses` VALUES (1,'NUR1',7,2);
/*!40000 ALTER TABLE `nurses` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_nurses` BEFORE INSERT ON `nurses` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(20);
    SET new_code = CONCAT('NUR',  IFNULL((SELECT MAX(id) FROM nurses), 0) + 1);
    SET NEW.nursecode = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `patientcode` varchar(255) DEFAULT NULL,
  `medical_history` varchar(255) DEFAULT NULL,
  `insurance_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `patients_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (1,3,'PAT1','Tiß╗ün sß╗¡ cao huyß║┐t ├íp','BH123456789'),(3,9,'PAT2','Bß╗çnh tim bß║⌐m sinh','SV1234567'),(4,10,'PAT4','',''),(5,12,'PAT5','Phß╗òi yß║┐u','SV1526425888'),(6,13,'PAT6','',''),(7,15,'PAT7','',''),(8,16,'PAT8','',''),(9,17,'PAT9','','');
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_patients` BEFORE INSERT ON `patients` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(20);
    SET new_code = CONCAT('PAT', IFNULL((SELECT MAX(id) FROM patients), 0) + 1);
    SET NEW.patientcode = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `paymentdetails`
--

DROP TABLE IF EXISTS `paymentdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paymentdetails` (
  `id` int NOT NULL AUTO_INCREMENT,
  `payment_id` int DEFAULT NULL,
  `ServiceType` enum('Examination','LabTest','ImagingTest','Medicine','Bed','Other') DEFAULT NULL,
  `service_id` int DEFAULT NULL,
  `description` text,
  `amount` decimal(10,2) DEFAULT NULL,
  `InsuranceCoveredAmount` decimal(10,2) DEFAULT NULL,
  `PatientPaidAmount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `payment_id` (`payment_id`),
  CONSTRAINT `paymentdetails_ibfk_1` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentdetails`
--

LOCK TABLES `paymentdetails` WRITE;
/*!40000 ALTER TABLE `paymentdetails` DISABLE KEYS */;
/*!40000 ALTER TABLE `paymentdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `record_id` int DEFAULT NULL,
  `cashier_id` int DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `paymentDate` date DEFAULT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `insuranceCoverage` decimal(10,2) DEFAULT NULL,
  `patientPayment` decimal(10,2) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `cashier_id` (`cashier_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`cashier_id`) REFERENCES `cashier` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pharmacystaff`
--

DROP TABLE IF EXISTS `pharmacystaff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pharmacystaff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `phaScode` varchar(20) DEFAULT NULL,
  `staff_id` int DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_pharmact_staff_id` (`staff_id`),
  CONSTRAINT `pharmacystaff_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pharmacystaff`
--

LOCK TABLES `pharmacystaff` WRITE;
/*!40000 ALTER TABLE `pharmacystaff` DISABLE KEYS */;
INSERT INTO `pharmacystaff` VALUES (1,'PHA1',5,3);
/*!40000 ALTER TABLE `pharmacystaff` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_pharmacystaff` BEFORE INSERT ON `pharmacystaff` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(20);
    SET new_code = CONCAT('PHA',  IFNULL((SELECT MAX(id) FROM pharmacystaff), 0) + 1);
    SET NEW.phaScode = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `prescriptiondetails`
--

DROP TABLE IF EXISTS `prescriptiondetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescriptiondetails` (
  `id` int NOT NULL AUTO_INCREMENT,
  `prescription_id` int DEFAULT NULL,
  `medicine_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `dosage` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `prescription_id` (`prescription_id`),
  KEY `medicine_id` (`medicine_id`),
  CONSTRAINT `prescriptiondetails_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`id`),
  CONSTRAINT `prescriptiondetails_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptiondetails`
--

LOCK TABLES `prescriptiondetails` WRITE;
/*!40000 ALTER TABLE `prescriptiondetails` DISABLE KEYS */;
INSERT INTO `prescriptiondetails` VALUES (1,1,1,4,'2 lß║ºn/ng├áy','Sau bß╗»a ─ân'),(2,1,5,2,'1 lß║ºn/ng├áy','Tr╞░ß╗¢c khi ngß╗º'),(3,2,5,9,'3 lß║ºn/ng├áy','Sau bß╗»a ─ân'),(4,2,8,6,'2 lß║ºn/ng├áy','Tr╞░ß╗¢c khi ─ân'),(5,2,10,3,'1 lß║ºn/ng├áy','Sau khi ─ân bß╗»a tr╞░a');
/*!40000 ALTER TABLE `prescriptiondetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescriptions`
--

DROP TABLE IF EXISTS `prescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescriptions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `record_id` int DEFAULT NULL,
  `inpatient_record_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `pharmacist_id` int DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `prescription_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `inpatient_record_id` (`inpatient_record_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `pharmacist_id` (`pharmacist_id`),
  CONSTRAINT `prescriptions_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `prescriptions_ibfk_2` FOREIGN KEY (`inpatient_record_id`) REFERENCES `inpatient_records` (`id`),
  CONSTRAINT `prescriptions_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `prescriptions_ibfk_4` FOREIGN KEY (`pharmacist_id`) REFERENCES `pharmacystaff` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES (1,1,NULL,1,1,'NEW','2025-05-24'),(2,2,NULL,1,1,'NEW','2025-05-24');
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receptionist`
--

DROP TABLE IF EXISTS `receptionist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receptionist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `PatRecCode` varchar(20) DEFAULT NULL,
  `staff_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_receptionist_staff_id` (`staff_id`),
  CONSTRAINT `receptionist_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receptionist`
--

LOCK TABLES `receptionist` WRITE;
/*!40000 ALTER TABLE `receptionist` DISABLE KEYS */;
/*!40000 ALTER TABLE `receptionist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `department_id` int DEFAULT NULL,
  `room_type_id` int NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`),
  KEY `room_type_id` (`room_type_id`),
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `room_ibfk_2` FOREIGN KEY (`room_type_id`) REFERENCES `roomtypes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,1,1,'Ph├▓ng A1'),(2,1,1,'Ph├▓ng A2'),(3,2,1,'Ph├▓ng B1'),(4,2,1,'Ph├▓ng B2'),(5,2,8,'Ph├▓ng x├⌐t nghiß╗çm 1'),(6,1,5,'Ph├▓ng A3'),(7,1,5,'Ph├▓ng A4'),(8,1,5,'Ph├▓ng A5'),(9,1,5,'Ph├▓ng A6'),(10,2,6,'Ph├▓ng B3'),(11,2,6,'Ph├▓ng B4'),(12,2,6,'Ph├▓ng B5'),(13,2,6,'Ph├▓ng B6'),(14,2,6,'Ph├▓ng B7'),(15,3,6,'Ph├▓ng C1'),(16,3,6,'Ph├▓ng C2');
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roomtypes`
--

DROP TABLE IF EXISTS `roomtypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roomtypes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roomtypes`
--

LOCK TABLES `roomtypes` WRITE;
/*!40000 ALTER TABLE `roomtypes` DISABLE KEYS */;
INSERT INTO `roomtypes` VALUES (4,'Ph├▓ng ICU'),(1,'Ph├▓ng kh├ím'),(6,'Ph├▓ng kh├ím ngoß║íi'),(5,'Ph├▓ng kh├ím nß╗Öi'),(7,'Ph├▓ng si├¬u ├óm'),(2,'Ph├▓ng th╞░ß╗¥ng'),(3,'Ph├▓ng VIP'),(8,'Ph├▓ng x├⌐t nghiß╗çm');
/*!40000 ALTER TABLE `roomtypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shift_type`
--

DROP TABLE IF EXISTS `shift_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shift_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name_type` varchar(255) NOT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shift_type`
--

LOCK TABLES `shift_type` WRITE;
/*!40000 ALTER TABLE `shift_type` DISABLE KEYS */;
INSERT INTO `shift_type` VALUES (1,'S├íng','08:00:00','12:00:00'),(2,'Chiß╗üu','13:00:00','17:00:00'),(3,'Tß╗æi','18:00:00','21:00:00');
/*!40000 ALTER TABLE `shift_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specialty`
--

DROP TABLE IF EXISTS `specialty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specialty` (
  `id` int NOT NULL AUTO_INCREMENT,
  `department_id` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`),
  CONSTRAINT `specialty_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialty`
--

LOCK TABLES `specialty` WRITE;
/*!40000 ALTER TABLE `specialty` DISABLE KEYS */;
INSERT INTO `specialty` VALUES (1,1,'Tim mß║ích','─Éiß╗üu trß╗ï c├íc bß╗çnh vß╗ü tim','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//tim_mach.jpg'),(2,1,'H├┤ hß║Ñp','Chuy├¬n ─æiß╗üu trß╗ï c├íc bß╗çnh li├¬n quan ─æß║┐n phß╗òi','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//ho_hap.jpg'),(3,2,'Ngoß║íi tß╗òng qu├ít','Phß║½u thuß║¡t c╞í bß║ún','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//Ngoai_tong_quat.jpg'),(4,3,'Nhi h├┤ hß║Ñp','Ch─âm s├│c bß╗çnh h├┤ hß║Ñp cho trß║╗','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//tre_em.png'),(6,2,'Chß║Ñn th╞░╞íng chß╗ënh h├¼nh','─Éiß╗üu trß╗ï c├íc chß║Ñn th╞░╞íng x╞░╞íng khß╗¢p','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//chan_thuong_chinh_hinh.jpg'),(7,5,'Thai sß║ún','','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//thai_san.jpg'),(8,2,'R─âng h├ám mß║╖t','','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//rang_ham_mat.jpg'),(9,2,'Da liß╗àu','','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//Da_lieu.jpg');
/*!40000 ALTER TABLE `specialty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `position_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `department_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_staff_userid` (`user_id`),
  KEY `idx_staff_departmentid` (`department_id`),
  KEY `idx_staff_positionid` (`position_id`),
  CONSTRAINT `staff_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `staff_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `staff_ibfk_3` FOREIGN KEY (`position_id`) REFERENCES `staff_position` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (1,1,4,1),(2,1,5,1),(3,1,7,5),(4,1,8,2),(5,5,11,1),(6,1,14,1),(7,2,18,2),(8,3,19,1);
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff_position`
--

DROP TABLE IF EXISTS `staff_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff_position` (
  `id` int NOT NULL AUTO_INCREMENT,
  `position` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `position` (`position`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff_position`
--

LOCK TABLES `staff_position` WRITE;
/*!40000 ALTER TABLE `staff_position` DISABLE KEYS */;
INSERT INTO `staff_position` VALUES (6,'Cashier'),(1,'Doctor'),(3,'Lab Technician'),(2,'NURSE'),(7,'Patient Receptionist'),(5,'Pharmacist'),(4,'Radiology Technician');
/*!40000 ALTER TABLE `staff_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff_schedules`
--

DROP TABLE IF EXISTS `staff_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff_schedules` (
  `id` int NOT NULL AUTO_INCREMENT,
  `staff_id` int DEFAULT NULL,
  `shift_type_id` int DEFAULT NULL,
  `room_id` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `staff_id` (`staff_id`),
  KEY `shift_type_id` (`shift_type_id`),
  KEY `room_id` (`room_id`),
  CONSTRAINT `staff_schedules_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`),
  CONSTRAINT `staff_schedules_ibfk_2` FOREIGN KEY (`shift_type_id`) REFERENCES `shift_type` (`id`),
  CONSTRAINT `staff_schedules_ibfk_3` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff_schedules`
--

LOCK TABLES `staff_schedules` WRITE;
/*!40000 ALTER TABLE `staff_schedules` DISABLE KEYS */;
/*!40000 ALTER TABLE `staff_schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `testtypes`
--

DROP TABLE IF EXISTS `testtypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `testtypes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `testName` varchar(100) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `testtypes`
--

LOCK TABLES `testtypes` WRITE;
/*!40000 ALTER TABLE `testtypes` DISABLE KEYS */;
/*!40000 ALTER TABLE `testtypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fullname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `gender` int NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `pass` varchar(255) DEFAULT NULL,
  `role` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `avartar_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_fullname` (`fullname`),
  KEY `idx_user_email` (`email`),
  KEY `idx_user_phone` (`phone_number`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Huß╗│nh Minh Ho├áng','hoanghm4869@gmail.com','0337023824','2003-11-12',0,'TBH TDM','$2a$10$b6E2VmRKJ8pB25uky/vWN.1XnlYvFSOEwK3jtxPchw5nqbLmqQoPC',0,'2025-04-10 00:00:00',NULL),(3,'Nguyß╗àn V─ân A','nguyenvanA@example.com','0123456789','1995-05-20',0,'123 ─É╞░ß╗¥ng L├¬ Lß╗úi, Tdm, TP HCM','$2a$10$XDLLf84jVbgGduWVA4N/.e2/ZqEVVhZe5d0ljbaxetYsLS.Az55I6',1,'2025-04-10 04:48:35',NULL),(4,'Trß║ºn V─ân C','tranvanc@hospital.com','0323456789','1985-06-20',0,'123 L├¬ Lß╗úi, TP.HCM','$2a$10$XiutYothZ4rma/P.J5c2leYOLb/hTj6V677Re5gy5hSWeB1yBe71y',2,'2025-04-10 08:28:32','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1747710399077.png'),(5,'Nguyß╗àn Minh Thuß║¡n','Thuannm@example.com','0223456789','1985-06-20',0,'123 Nguyß╗àn Tr├úi, TP.HCM','$2a$10$o/BAwrm8rlbRUtVKepG8iuhlFxvVMfn6LVhLyaaiLtmdvUAIkGLbC',2,'2025-04-16 08:06:58','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//bs_nam.png'),(7,'Nguyß╗àn Hß╗ông Yß║┐n','Yennh@example.com','0423456789','1995-06-20',1,'123 L├╜ Th├íi Tß╗ò, TP.HCM','$2a$10$ZRb1sOPqiTR1NgDn7l9ryuU7JbBOT6QnX2.ai.skFTmg/VMVFNu5u',2,'2025-05-18 06:40:23','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//bs_nu.jpg'),(8,'Trß║ºn Thu Ph╞░╞íng','Phuongtt20@example.com','0523456789','1994-08-20',1,'123 Phan Ch├óu Trinh, TP.HCM','$2a$10$qFTu3C44lJEAz4.AEo/qIOnwihykzMMB5mYTqv0nKcnFePOhvBtxK',2,'2025-05-18 07:36:34','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//bs_nu.jpg'),(9,'Nguyß╗àn Viß╗çt Trung','Truntnv@gmail.com','0336013824','2003-06-25',0,'Ph├║ H├▓a','$2a$10$kvsD41NR8tMs6ZFfth6wI.ksdSKpySwLVYW2Y/CT/0KoryGjrzxCq',1,'2025-05-19 00:13:09',''),(10,'Nguyß╗àn Thanh Nam','Namnt@gmail.com','0723456789','2002-05-23',0,'Cß╗º Chi','$2a$10$WYEA1.VFcVZYAR1tAqY8vO/Sr/C1Y3bVp6TDVwyKAbQIfPcTuBCQm',1,'2025-05-23 17:17:39',''),(11,'Hß╗ô Ngß╗ìc Ch├óu','Chaunh@gmail.com','0823456789','2000-06-24',1,'TP HCM','$2a$10$XDLLf84jVbgGduWVA4N/.e2/ZqEVVhZe5d0ljbaxetYsLS.Az55I6',2,'2025-05-24 16:47:19',NULL),(12,'Nguyß╗àn Ngß╗ìc H├á','hann@gmail.com','038023564','2003-11-25',1,'ph╞░ß╗¥ng Thß╗º Dß║ºu Mß╗Öt','$2a$10$jtuaGLERzhkK9ScR63RZPO1VLcFgTratjYAmWhWZXz1.PHKX2p3XK',1,'2025-08-21 09:18:07',NULL),(13,'Nguyß╗àn Thanh Thß║úo','thaont@gmail.com','0320156487','2002-05-14',1,'ph╞░ß╗¥ng Ch├ính Hiß╗çp','$2a$10$Ru49ve58Uu1O0CXhf0.Lqu48tpwLWf32xZVJ32FS9VL91up0IulK6',1,'2025-08-21 16:05:53','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1755825874773.jpg'),(14,'Trß║ºn V─ân C','Thuann1m@example.com','0323456789','1985-06-20',0,'123 L├¬ Lß╗úi, TP.HCM','$2a$10$iwcOFEhX3qKwdYfG/kQ6QuPxQUokKlxOENmeSJ/VYgu4918xbGLhq',2,'2025-08-22 01:23:35','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1755825850325.jpg'),(15,'Nguyß╗àn Thanh Hß║▒ng','hangnt@gmail.com','0320156487','2002-05-14',1,'ph╞░ß╗¥ng Ch├ính Hiß╗çp','$2a$10$v4mss5YDekozGY9xkvRQIe/FdEjz8uFNx1U9u84TzjQkASAvkUdpO',1,'2025-08-22 01:26:01',NULL),(16,'Nguyß╗àn Thanh Thu','nguyenvan123@example.com','0320156487','2002-05-14',0,'ph╞░ß╗¥ng Ch├ính Hiß╗çp','$2a$10$.cnH7Hx/jTUaCll79EDPk.q5zlRJ1ntCUmGtV9sQSULT8ybVYnCH6',1,'2025-08-22 01:26:52',NULL),(17,'Nguyß╗àn Thanh Thuß╗╖','thuynt@gmail.com','0320156487','2002-05-14',1,'ph╞░ß╗¥ng Ch├ính Hiß╗çp','$2a$10$4iEmWwD6Ju6U/vamg5MbMOjWERSQ2PHCPA67uCq42eqQlKDxYlrtm',1,'2025-08-22 13:10:33',''),(18,'Nguyß╗àn Thß╗ï Ng├ón','ngannt@gmail.com','0337033824','2000-09-28',0,'Ch├ính Hiß╗çp','$2a$10$aOyLpSBFMRaj1n8MRk2.EOma7w1gsf/gIjKB4aMFkRd4Te./hMaAm',2,'2025-09-28 17:36:54','string'),(19,'Nguyß╗àn Tiß║┐n Duy','duynt@gmail.com','0337043824','1999-10-28',1,'Ph├║ Lß╗úi','$2a$10$ixigocA1JDl.IF/Nu7vb4eh6.JAdWX7eZWGfX/J6C.8oDt5YPGtRO',2,'2025-09-28 17:39:05','');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_staff_unified`
--

DROP TABLE IF EXISTS `v_staff_unified`;
/*!50001 DROP VIEW IF EXISTS `v_staff_unified`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_staff_unified` AS SELECT 
 1 AS `id`,
 1 AS `roleType`,
 1 AS `code`,
 1 AS `departmentId`,
 1 AS `department`,
 1 AS `positionId`,
 1 AS `position`,
 1 AS `specialtyId`,
 1 AS `specialty`,
 1 AS `experienceYears`,
 1 AS `fullname`,
 1 AS `email`,
 1 AS `phoneNumber`,
 1 AS `dateOfBirth`,
 1 AS `gender`,
 1 AS `address`,
 1 AS `avatar_url`,
 1 AS `staffId`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `vitalsigns`
--

DROP TABLE IF EXISTS `vitalsigns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vitalsigns` (
  `id` int NOT NULL AUTO_INCREMENT,
  `inpatient_record_id` int DEFAULT NULL,
  `nurse_id` int DEFAULT NULL,
  `blood_pressure` varchar(20) DEFAULT NULL,
  `pulse` int DEFAULT NULL,
  `temperature` decimal(4,2) DEFAULT NULL,
  `notes` text,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `inpatient_record_id` (`inpatient_record_id`),
  KEY `nurse_id` (`nurse_id`),
  CONSTRAINT `vitalsigns_ibfk_1` FOREIGN KEY (`inpatient_record_id`) REFERENCES `inpatient_records` (`id`),
  CONSTRAINT `vitalsigns_ibfk_2` FOREIGN KEY (`nurse_id`) REFERENCES `nurses` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vitalsigns`
--

LOCK TABLES `vitalsigns` WRITE;
/*!40000 ALTER TABLE `vitalsigns` DISABLE KEYS */;
/*!40000 ALTER TABLE `vitalsigns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `clinicbooking`
--

USE `clinicbooking`;

--
-- Final view structure for view `v_staff_unified`
--

/*!50001 DROP VIEW IF EXISTS `v_staff_unified`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_staff_unified` AS select `d`.`id` AS `id`,'DOCTOR' AS `roleType`,`d`.`doctorcode` AS `code`,`s`.`department_id` AS `departmentId`,`dept`.`name` AS `department`,`s`.`position_id` AS `positionId`,`pos`.`position` AS `position`,`d`.`specialty_id` AS `specialtyId`,`sp`.`name` AS `specialty`,`d`.`experience_years` AS `experienceYears`,`u`.`fullname` AS `fullname`,`u`.`email` AS `email`,`u`.`phone_number` AS `phoneNumber`,`u`.`date_of_birth` AS `dateOfBirth`,`u`.`gender` AS `gender`,`u`.`address` AS `address`,`u`.`avartar_url` AS `avatar_url`,`s`.`id` AS `staffId` from (((((`doctors` `d` join `staff` `s` on((`d`.`staff_id` = `s`.`id`))) join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `department` `dept` on((`s`.`department_id` = `dept`.`id`))) left join `staff_position` `pos` on((`s`.`position_id` = `pos`.`id`))) left join `specialty` `sp` on((`d`.`specialty_id` = `sp`.`id`))) union all select `n`.`id` AS `id`,'NURSE' AS `roleType`,`n`.`nursecode` AS `code`,`s`.`department_id` AS `departmentId`,`dept`.`name` AS `department`,`s`.`position_id` AS `positionId`,`pos`.`position` AS `position`,NULL AS `specialtyId`,NULL AS `specialty`,`n`.`experience_years` AS `experienceYears`,`u`.`fullname` AS `fullname`,`u`.`email` AS `email`,`u`.`phone_number` AS `phoneNumber`,`u`.`date_of_birth` AS `dateOfBirth`,`u`.`gender` AS `gender`,`u`.`address` AS `address`,`u`.`avartar_url` AS `avatar_url`,`s`.`id` AS `staffId` from ((((`nurses` `n` join `staff` `s` on((`n`.`staff_id` = `s`.`id`))) join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `department` `dept` on((`s`.`department_id` = `dept`.`id`))) left join `staff_position` `pos` on((`s`.`position_id` = `pos`.`id`))) union all select `l`.`id` AS `id`,'LAB' AS `roleType`,`l`.`labScode` AS `code`,`s`.`department_id` AS `departmentId`,`dept`.`name` AS `department`,`s`.`position_id` AS `positionId`,`pos`.`position` AS `position`,NULL AS `specialtyId`,NULL AS `specialty`,`l`.`experience_years` AS `experienceYears`,`u`.`fullname` AS `fullname`,`u`.`email` AS `email`,`u`.`phone_number` AS `phoneNumber`,`u`.`date_of_birth` AS `dateOfBirth`,`u`.`gender` AS `gender`,`u`.`address` AS `address`,`u`.`avartar_url` AS `avatar_url`,`s`.`id` AS `staffId` from ((((`labstaffs` `l` join `staff` `s` on((`l`.`staff_id` = `s`.`id`))) join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `department` `dept` on((`s`.`department_id` = `dept`.`id`))) left join `staff_position` `pos` on((`s`.`position_id` = `pos`.`id`))) union all select `i`.`id` AS `id`,'IMAGING' AS `roleType`,`i`.`imgScode` AS `code`,`s`.`department_id` AS `departmentId`,`dept`.`name` AS `department`,`s`.`position_id` AS `positionId`,`pos`.`position` AS `position`,NULL AS `specialtyId`,NULL AS `specialty`,`i`.`experience_years` AS `experienceYears`,`u`.`fullname` AS `fullname`,`u`.`email` AS `email`,`u`.`phone_number` AS `phoneNumber`,`u`.`date_of_birth` AS `dateOfBirth`,`u`.`gender` AS `gender`,`u`.`address` AS `address`,`u`.`avartar_url` AS `avatar_url`,`s`.`id` AS `staffId` from ((((`imagingstaff` `i` join `staff` `s` on((`i`.`staff_id` = `s`.`id`))) join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `department` `dept` on((`s`.`department_id` = `dept`.`id`))) left join `staff_position` `pos` on((`s`.`position_id` = `pos`.`id`))) union all select `p`.`id` AS `id`,'PHARMACY' AS `roleType`,`p`.`phaScode` AS `code`,`s`.`department_id` AS `departmentId`,`dept`.`name` AS `department`,`s`.`position_id` AS `positionId`,`pos`.`position` AS `position`,NULL AS `specialtyId`,NULL AS `specialty`,`p`.`experience_years` AS `experienceYears`,`u`.`fullname` AS `fullname`,`u`.`email` AS `email`,`u`.`phone_number` AS `phoneNumber`,`u`.`date_of_birth` AS `dateOfBirth`,`u`.`gender` AS `gender`,`u`.`address` AS `address`,`u`.`avartar_url` AS `avatar_url`,`s`.`id` AS `staffId` from ((((`pharmacystaff` `p` join `staff` `s` on((`p`.`staff_id` = `s`.`id`))) join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `department` `dept` on((`s`.`department_id` = `dept`.`id`))) left join `staff_position` `pos` on((`s`.`position_id` = `pos`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-29 22:46:52
