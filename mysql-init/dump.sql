-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: clinicbooking
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appointment_status`
--

DROP TABLE IF EXISTS `appointment_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `appointment_id` int DEFAULT NULL,
  `status` int DEFAULT '1',
  `reason` varchar(255) DEFAULT NULL,
  `update_by` int DEFAULT NULL,
  `update_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `appointment_id` (`appointment_id`),
  KEY `update_by` (`update_by`),
  CONSTRAINT `appointment_status_ibfk_1` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
  CONSTRAINT `appointment_status_ibfk_2` FOREIGN KEY (`update_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment_status`
--

LOCK TABLES `appointment_status` WRITE;
/*!40000 ALTER TABLE `appointment_status` DISABLE KEYS */;
INSERT INTO `appointment_status` VALUES (1,1,1,NULL,3,'2025-05-16 09:29:47'),(2,1,2,'',4,'2025-05-16 09:41:30'),(8,6,1,NULL,3,'2025-05-19 00:17:16'),(13,7,1,NULL,3,'2025-05-25 02:11:33'),(14,6,6,'Bận đột xuất',3,'2025-05-25 02:12:04'),(15,8,1,NULL,3,'2025-08-22 11:11:38'),(16,9,1,NULL,9,'2025-08-24 04:00:07'),(17,10,1,NULL,3,'2025-08-25 06:57:26'),(18,7,6,'Bận đột xuất',3,'2025-08-25 06:58:13'),(22,12,1,'Đau răng hàm',10,'2025-10-16 03:05:55'),(23,12,6,'Bận đột xuất',10,'2025-10-16 03:11:37'),(24,13,1,'Đau răng hàm',10,'2025-10-16 08:15:39'),(25,14,1,'Đau răng hàm',3,'2025-10-19 02:23:07'),(27,14,3,NULL,22,'2025-10-23 08:24:22'),(28,14,4,NULL,8,'2025-10-23 08:26:27'),(29,13,2,NULL,8,'2025-10-31 01:59:07'),(30,13,3,NULL,NULL,'2025-10-31 02:01:37'),(31,15,1,'Khát và Uống nhiều, Đi tiểu nhiều',10,'2025-11-01 03:47:13'),(32,15,2,NULL,28,'2025-11-01 03:52:13'),(33,15,3,NULL,NULL,'2025-11-01 03:56:42'),(34,15,4,NULL,28,'2025-11-01 03:58:57'),(35,16,1,'khát, uống nước nhiều. Mệt mỏi trong người',13,'2025-11-05 08:57:34'),(36,16,2,NULL,28,'2025-11-05 08:59:10'),(37,16,3,NULL,NULL,'2025-11-05 09:00:04'),(38,16,4,NULL,28,'2025-11-05 09:00:48'),(39,17,1,'Nhức răng hàm',3,'2025-11-10 09:42:31'),(50,17,2,NULL,8,'2025-11-11 06:55:15'),(56,17,3,NULL,22,'2025-11-12 03:59:21'),(57,18,3,'đau họng',22,'2025-11-14 04:06:36'),(58,19,3,'đau rát cổ, có đàm',22,'2025-11-14 04:35:07'),(59,20,3,'nghi ngờ gãy xương tay',22,'2025-11-15 09:02:09'),(60,20,4,NULL,36,'2025-11-15 15:39:10'),(61,21,1,'Khám bệnh',17,'2025-11-18 07:36:16'),(67,21,2,NULL,33,'2025-11-18 15:26:33'),(68,21,3,NULL,22,'2025-11-19 04:11:07'),(69,22,3,'ho có đàm, gắt cổ',22,'2025-11-19 04:17:46'),(70,23,3,'Khát và Uống nhiều, Đi tiểu nhiều',22,'2025-11-20 00:55:54'),(71,23,4,NULL,28,'2025-11-20 01:27:39'),(72,24,3,'Ho khan, có đàm',22,'2025-11-21 03:53:19'),(73,24,4,NULL,33,'2025-11-21 03:55:42'),(74,25,1,'Ho nhiều, rát cổ họng',38,'2025-11-21 09:11:24'),(77,25,2,NULL,35,'2025-11-21 09:23:19'),(78,25,3,NULL,22,'2025-11-21 09:26:29'),(79,26,3,'Ho nhiều, có đàm, gắt cổ',22,'2025-11-22 02:43:16'),(80,27,1,'hô hấp khó chịu',16,'2025-11-29 04:50:49'),(83,28,1,'Cảm, đau đầu nhiều',38,'2025-12-03 10:04:34'),(84,29,1,'Đau đầu, sốt nhẹ',17,'2025-12-03 14:34:27'),(85,29,2,NULL,33,'2025-12-03 16:28:32'),(86,21,4,NULL,33,'2025-12-03 16:41:23'),(87,15,5,NULL,29,'2025-12-03 17:04:55'),(88,30,3,'cảm, ho, đau đầu',22,'2025-12-04 05:27:01');
/*!40000 ALTER TABLE `appointment_status` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_appt_status_au_cancel` AFTER UPDATE ON `appointment_status` FOR EACH ROW BEGIN
  -- Khai báo biến: PHẢI ở ngay đầu khối BEGIN..END
  DECLARE v_slot_id INT;
  DECLARE v_ds_id   INT;
  DECLARE v_is_booked TINYINT;
  DECLARE v_prev_canceled INT DEFAULT 0;

  IF (OLD.status <> 4 AND NEW.status = 4) THEN
    /* Đã có bản ghi Hủy khác cho appointment này chưa? */
    SELECT COUNT(*) INTO v_prev_canceled
    FROM appointment_status
    WHERE appointment_id = NEW.appointment_id
      AND status = 4
      AND id <> NEW.id;

    IF v_prev_canceled = 0 THEN
      /* Lấy slot & schedule từ appointments */
      SELECT schedule_slot_id, doctor_schedule_id
        INTO v_slot_id, v_ds_id
      FROM appointments
      WHERE id = NEW.appointment_id;

      IF v_slot_id IS NULL OR v_ds_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
          SET MESSAGE_TEXT = 'Appointment not found or missing slot/schedule.';
      END IF;

      /* Nếu slot đang giữ thì mở slot và giảm bộ đếm */
      SELECT is_booked INTO v_is_booked
      FROM schedule_slots
      WHERE id = v_slot_id;

      IF v_is_booked = 1 THEN
        UPDATE schedule_slots
          SET is_booked = 0
        WHERE id = v_slot_id;

        UPDATE doctor_schedules
          SET booked_patients = GREATEST(booked_patients - 1, 0)
        WHERE id = v_ds_id;
      END IF;
    END IF;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

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
  `schedule_slot_id` int DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `visit_date_time` datetime DEFAULT NULL,
  `visit_type` varchar(255) DEFAULT NULL,
  `visit_number` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `patient_id` (`patient_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `doctor_schedule_id` (`doctor_schedule_id`),
  KEY `fk_a_slot_schedule` (`schedule_slot_id`),
  CONSTRAINT `appointments_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `appointments_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `appointments_ibfk_3` FOREIGN KEY (`doctor_schedule_id`) REFERENCES `doctor_schedules` (`id`),
  CONSTRAINT `fk_a_slot_schedule` FOREIGN KEY (`schedule_slot_id`) REFERENCES `schedule_slots` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES (1,1,1,2,'2025-05-16 09:29:47',3,'LH1','2025-05-12 09:29:47','scheduled',1),(6,1,1,152,'2025-05-19 00:17:16',15,'LH6',NULL,'scheduled',NULL),(7,1,2,253,'2025-05-25 02:11:33',28,'LH7',NULL,'scheduled',NULL),(8,1,3,316,'2025-08-22 11:11:38',37,'LH8','2025-08-25 09:29:47','scheduled',1),(9,3,3,315,'2025-08-24 04:00:07',45,'LH9',NULL,'scheduled',NULL),(10,1,1,306,'2025-08-25 06:57:26',61,'LH10',NULL,'scheduled',NULL),(12,4,4,3,'2025-10-16 03:05:55',67,'LH11',NULL,'scheduled',NULL),(13,4,4,3,'2025-10-16 08:15:39',67,'LH13','2025-10-17 13:29:47','scheduled',1),(14,1,4,319,'2025-10-19 02:23:06',104,'LH14','2025-10-20 09:29:47','scheduled',1),(15,4,6,326,'2025-11-01 03:47:13',185,'LH15','2025-11-03 08:29:47','scheduled',1),(16,6,6,327,'2025-11-05 08:57:34',1,'LH16','2025-11-06 14:29:47','scheduled',1),(17,1,4,328,'2025-11-10 09:42:31',208,'LH17','2025-11-12 03:59:21','scheduled',2),(18,5,11,352,NULL,NULL,'LH18','2025-11-14 04:06:36','walk-in',1),(19,3,11,352,NULL,NULL,'LH19','2025-11-14 04:35:07','walk-in',2),(20,9,13,354,'2025-11-15 09:02:09',NULL,'LH20','2025-11-15 09:02:09','walk-in',1),(21,9,10,358,'2025-11-18 07:36:16',556,'LH21','2025-11-19 04:11:06','scheduled',1),(22,8,10,358,'2025-11-19 04:17:46',NULL,'LH22','2025-11-19 04:17:46','walk-in',2),(23,7,6,359,'2025-11-20 00:55:54',NULL,'LH23','2025-11-20 00:55:54','walk-in',1),(24,5,10,376,'2025-11-21 03:53:19',NULL,'LH24','2025-11-21 03:53:19','walk-in',1),(25,10,12,379,'2025-11-21 09:11:24',813,'LH25','2025-11-21 09:26:29','scheduled',2),(26,11,11,372,'2025-11-22 02:43:16',NULL,'LH26','2025-11-22 02:43:16','walk-in',1),(27,8,11,380,'2025-11-29 04:50:49',815,'LH27',NULL,'scheduled',NULL),(28,10,10,383,'2025-12-03 10:04:34',850,'LH28',NULL,'scheduled',NULL),(29,9,10,383,'2025-12-03 14:34:27',852,'LH29',NULL,'scheduled',NULL),(30,5,10,384,'2025-12-04 05:27:01',NULL,'LH30','2025-12-04 05:27:01','walk-in',1);
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_appointments` BEFORE INSERT ON `appointments` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(255);
    SET new_code = CONCAT('LH', IFNULL((SELECT MAX(id) FROM appointments), 0) + 1);
    SET NEW.code = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_appt_ai_book_slot` AFTER INSERT ON `appointments` FOR EACH ROW BEGIN
  UPDATE schedule_slots
    SET is_booked = 1
  WHERE id = NEW.schedule_slot_id;

  UPDATE doctor_schedules
    SET booked_patients = booked_patients + 1
  WHERE id = NEW.doctor_schedule_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `beds`
--

DROP TABLE IF EXISTS `beds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beds` (
  `id` int NOT NULL AUTO_INCREMENT,
  `room_id` int DEFAULT NULL,
  `bed_number` varchar(255) NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `bed_fee` double DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `room_id` (`room_id`),
  CONSTRAINT `beds_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beds`
--

LOCK TABLES `beds` WRITE;
/*!40000 ALTER TABLE `beds` DISABLE KEYS */;
INSERT INTO `beds` VALUES (1,17,'G0001','2025-10-06 16:50:34',110000,1);
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
  `staff_id` int DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  `cas_scode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_cashier_staff_id` (`staff_id`),
  CONSTRAINT `cashier_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cashier`
--

LOCK TABLES `cashier` WRITE;
/*!40000 ALTER TABLE `cashier` DISABLE KEYS */;
INSERT INTO `cashier` VALUES (1,18,5,'CAS1');
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
    SET NEW.cas_scode = new_code;
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
  `contact` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `head_doctor_id` int DEFAULT NULL,
  `establishment_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_hd_d` (`head_doctor_id`),
  CONSTRAINT `FK_hd_d` FOREIGN KEY (`head_doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'Khoa Nội','Chuyên điều trị các bệnh nội khoa','027412345602',1,1,'2024-10-01 08:44:25.422000'),(2,'Khoa Ngoại','Chuyên phẫu thuật và ngoại khoa','027425341602',1,4,'2024-09-02 08:44:25.422000'),(3,'Khoa Nhi','Chăm sóc và điều trị cho trẻ em','027652441602',1,NULL,'2024-09-15 08:44:25.422000'),(5,'Khoa Sản','Chăm sóc sức khỏe sinh sản và phụ nữ mang thai','027652321602',1,3,'2024-09-15 08:44:25.422000'),(8,'Khoa Xét Nghiệm',NULL,'027651241503',1,NULL,'2024-09-15 08:45:25.422000'),(9,'Khoa Chẩn Đoán Hình Ảnh',NULL,'027656351503',1,NULL,'2024-09-15 08:46:25.422000'),(10,'Phòng Lễ Tân',NULL,'027655847503',1,NULL,'2024-09-15 08:47:25.422000'),(11,'Phòng Tài Chính',NULL,'027651358403',1,NULL,'2024-09-15 08:48:25.422000'),(12,'Khoa Dược',NULL,'027632541503',1,NULL,'2024-09-15 08:49:25.422000');
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
) ENGINE=InnoDB AUTO_INCREMENT=431 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_schedules`
--

LOCK TABLES `doctor_schedules` WRITE;
/*!40000 ALTER TABLE `doctor_schedules` DISABLE KEYS */;
INSERT INTO `doctor_schedules` VALUES (1,1,2,3,'2025-05-10','ACTIVE',15,0),(2,1,1,3,'2025-05-12','ACTIVE',15,1),(3,4,2,17,'2025-10-17','ACTIVE',15,1),(52,2,2,2,'2025-05-13','ACTIVE',15,0),(53,2,1,1,'2025-05-14','ACTIVE',15,0),(102,1,2,3,'2025-05-20','ACTIVE',15,0),(152,1,3,1,'2025-05-20','ACTIVE',10,0),(153,1,1,1,'2025-05-21','ACTIVE',14,0),(154,1,2,3,'2025-05-22','ACTIVE',14,0),(155,1,3,3,'2025-05-22','ACTIVE',10,0),(156,1,1,3,'2025-05-23','ACTIVE',12,0),(157,1,2,3,'2025-05-23','ACTIVE',12,0),(158,1,2,3,'2025-05-24','ACTIVE',12,0),(202,4,1,3,'2025-05-29','ACTIVE',15,0),(252,2,1,2,'2025-05-26','ACTIVE',15,0),(253,2,1,2,'2025-05-27','ACTIVE',15,0),(254,2,2,2,'2025-05-27','ACTIVE',10,0),(255,2,2,6,'2025-05-28','ACTIVE',15,0),(256,2,3,6,'2025-05-28','ACTIVE',8,0),(257,2,1,2,'2025-05-30','ACTIVE',15,0),(258,2,2,2,'2025-05-30','ACTIVE',15,0),(259,2,1,2,'2025-06-01','ACTIVE',15,0),(260,2,2,6,'2025-06-02','ACTIVE',15,0),(261,2,3,6,'2025-06-02','ACTIVE',8,0),(262,1,1,1,'2025-05-27','ACTIVE',15,0),(263,1,1,1,'2025-05-28','ACTIVE',15,0),(264,1,2,1,'2025-05-28','ACTIVE',15,0),(265,1,2,7,'2025-05-29','ACTIVE',15,0),(266,1,3,7,'2025-05-29','ACTIVE',8,0),(302,1,2,3,'2025-08-24','ACTIVE',20,0),(303,1,1,3,'2025-08-24','ACTIVE',20,0),(304,1,2,3,'2025-08-25','ACTIVE',20,0),(305,1,3,3,'2025-08-25','ACTIVE',20,0),(306,1,1,2,'2025-08-27','ACTIVE',20,0),(307,1,2,2,'2025-08-27','ACTIVE',20,0),(308,2,2,5,'2025-08-24','ACTIVE',20,0),(309,2,3,5,'2025-08-24','ACTIVE',20,0),(310,2,2,5,'2025-08-25','ACTIVE',20,0),(311,2,1,5,'2025-08-26','ACTIVE',20,0),(312,2,2,5,'2025-08-26','ACTIVE',20,0),(313,3,1,7,'2025-08-24','ACTIVE',20,0),(314,3,2,7,'2025-08-24','ACTIVE',20,0),(315,3,2,7,'2025-08-25','ACTIVE',20,0),(316,3,3,7,'2025-08-25','ACTIVE',20,0),(317,4,1,17,'2025-10-18','ACTIVE',15,0),(318,4,1,3,'2025-10-21','ACTIVE',15,0),(319,4,1,3,'2025-10-20','ACTIVE',15,1),(320,4,2,3,'2025-10-20','ACTIVE',15,0),(321,4,2,3,'2025-10-21','ACTIVE',15,0),(322,4,2,4,'2025-10-22','ACTIVE',15,0),(323,4,3,4,'2025-10-22','ACTIVE',15,0),(324,2,1,7,'2025-10-30','ACTIVE',15,0),(326,6,1,8,'2025-11-03','ACTIVE',15,1),(327,6,2,8,'2025-11-06','ACTIVE',15,1),(328,4,1,1,'2025-11-12','ACTIVE',15,1),(329,4,2,1,'2025-11-12','ACTIVE',15,0),(330,4,1,1,'2025-11-13','ACTIVE',15,0),(331,4,2,1,'2025-11-13','ACTIVE',15,0),(332,4,1,1,'2025-11-14','ACTIVE',15,0),(333,4,1,1,'2025-11-15','ACTIVE',15,0),(334,4,2,1,'2025-11-15','ACTIVE',15,0),(335,7,2,2,'2025-11-13','ACTIVE',15,0),(336,7,3,2,'2025-11-13','ACTIVE',15,0),(337,7,1,2,'2025-11-14','ACTIVE',15,0),(338,7,2,2,'2025-11-14','ACTIVE',15,0),(339,7,1,2,'2025-11-15','ACTIVE',15,0),(340,7,2,2,'2025-11-15','ACTIVE',15,0),(341,7,1,2,'2025-11-16','ACTIVE',15,0),(342,8,2,6,'2025-11-13','ACTIVE',15,0),(343,8,1,6,'2025-11-14','ACTIVE',15,0),(344,8,2,6,'2025-11-14','ACTIVE',15,0),(345,8,2,6,'2025-11-15','ACTIVE',15,0),(346,8,3,6,'2025-11-15','ACTIVE',15,0),(347,9,1,7,'2025-11-14','ACTIVE',15,0),(348,9,2,7,'2025-11-14','ACTIVE',15,0),(349,9,2,7,'2025-11-15','ACTIVE',15,0),(351,11,2,8,'2025-11-13','ACTIVE',15,0),(352,11,1,8,'2025-11-14','ACTIVE',15,2),(353,11,2,8,'2025-11-14','ACTIVE',15,0),(354,13,2,10,'2025-11-15','ACTIVE',15,1),(355,13,3,10,'2025-11-15','ACTIVE',15,0),(356,10,1,6,'2025-11-18','ACTIVE',15,0),(357,10,2,6,'2025-11-18','ACTIVE',15,0),(358,10,1,8,'2025-11-19','ACTIVE',15,2),(359,6,1,1,'2025-11-20','ACTIVE',15,1),(360,6,2,1,'2025-11-20','ACTIVE',15,0),(361,6,1,2,'2025-11-21','ACTIVE',15,0),(362,6,1,1,'2025-11-22','ACTIVE',15,0),(363,6,2,1,'2025-11-22','ACTIVE',15,0),(364,14,2,2,'2025-11-20','ACTIVE',15,0),(365,14,1,6,'2025-11-21','ACTIVE',15,0),(366,14,2,6,'2025-11-21','ACTIVE',15,0),(367,14,2,6,'2025-11-22','ACTIVE',15,0),(368,14,3,6,'2025-11-22','ACTIVE',15,0),(369,11,1,3,'2025-11-20','ACTIVE',15,0),(370,11,2,3,'2025-11-20','ACTIVE',15,0),(371,11,2,3,'2025-11-21','ACTIVE',15,0),(372,11,1,3,'2025-11-22','ACTIVE',15,1),(373,11,2,3,'2025-11-22','ACTIVE',15,0),(374,10,2,4,'2025-11-20','ACTIVE',15,0),(375,10,3,4,'2025-11-20','ACTIVE',15,0),(376,10,1,4,'2025-11-21','ACTIVE',15,1),(377,12,1,10,'2025-11-20','ACTIVE',15,0),(378,12,1,10,'2025-11-21','ACTIVE',15,0),(379,12,2,10,'2025-11-21','ACTIVE',15,1),(380,11,1,1,'2025-12-01','ACTIVE',15,1),(381,11,1,1,'2025-12-02','ACTIVE',15,0),(382,11,2,1,'2025-12-02','ACTIVE',15,0),(383,10,1,10,'2025-12-04','ACTIVE',15,2),(384,10,2,10,'2025-12-04','ACTIVE',15,1),(385,10,1,10,'2025-12-05','ACTIVE',15,0),(386,10,1,10,'2025-12-06','ACTIVE',15,0),(387,10,2,10,'2025-12-06','ACTIVE',15,0),(388,14,1,10,'2025-12-02','ACTIVE',15,0),(389,14,2,10,'2025-12-02','ACTIVE',15,0),(390,14,2,10,'2025-12-05','ACTIVE',15,0),(391,3,1,9,'2025-12-04','ACTIVE',15,0),(392,3,2,9,'2025-12-04','ACTIVE',15,0),(393,3,1,9,'2025-12-05','ACTIVE',15,0),(394,3,2,9,'2025-12-05','ACTIVE',15,0),(395,8,1,8,'2025-12-02','ACTIVE',15,0),(396,8,2,8,'2025-12-02','ACTIVE',15,0),(397,8,1,8,'2025-12-04','ACTIVE',15,0),(398,8,2,8,'2025-12-04','ACTIVE',15,0),(399,8,2,8,'2025-12-05','ACTIVE',15,0),(400,8,1,8,'2025-12-06','ACTIVE',15,0),(401,11,1,2,'2025-12-04','ACTIVE',15,0),(402,11,2,2,'2025-12-04','ACTIVE',15,0),(403,11,1,2,'2025-12-05','ACTIVE',15,0),(404,11,2,2,'2025-12-05','ACTIVE',15,0),(405,12,1,3,'2025-12-02','ACTIVE',15,0),(406,12,2,3,'2025-12-02','ACTIVE',15,0),(407,12,1,3,'2025-12-03','ACTIVE',15,0),(408,12,1,3,'2025-12-04','ACTIVE',15,0),(409,12,2,3,'2025-12-04','ACTIVE',15,0),(410,12,1,3,'2025-12-05','ACTIVE',15,0),(411,13,1,4,'2025-12-02','ACTIVE',15,0),(412,13,2,4,'2025-12-02','ACTIVE',15,0),(413,13,2,4,'2025-12-03','ACTIVE',15,0),(414,13,1,4,'2025-12-04','ACTIVE',15,0),(415,13,2,4,'2025-12-04','ACTIVE',15,0),(416,13,1,4,'2025-12-05','ACTIVE',15,0),(417,13,2,4,'2025-12-05','ACTIVE',15,0),(418,6,1,6,'2025-12-04','ACTIVE',15,0),(419,6,2,6,'2025-12-04','ACTIVE',15,0),(420,6,1,6,'2025-12-05','ACTIVE',15,0),(421,6,2,6,'2025-12-05','ACTIVE',15,0),(422,6,1,6,'2025-12-06','ACTIVE',15,0),(423,6,2,6,'2025-12-06','ACTIVE',15,0),(424,9,1,7,'2025-12-02','ACTIVE',15,0),(425,9,1,7,'2025-12-03','ACTIVE',15,0),(426,9,2,6,'2025-12-03','ACTIVE',15,0),(427,9,1,7,'2025-12-04','ACTIVE',15,0),(428,9,2,7,'2025-12-04','ACTIVE',15,0),(429,9,2,7,'2025-12-05','ACTIVE',15,0),(430,9,3,7,'2025-12-05','ACTIVE',15,0);
/*!40000 ALTER TABLE `doctor_schedules` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_ds_ai_generate_slots` AFTER INSERT ON `doctor_schedules` FOR EACH ROW BEGIN
  -- Tự sinh slot 30 phút cho lịch vừa tạo
  CALL generate_slots_for_schedule(NEW.id, 20);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_ds_au_regen_slots` AFTER UPDATE ON `doctor_schedules` FOR EACH ROW BEGIN
  DECLARE v_changed TINYINT DEFAULT 0;

  IF NOT (NEW.shift_type_id <=> OLD.shift_type_id) 
     OR NOT (NEW.date <=> OLD.date)
     OR NOT (NEW.max_patients <=> OLD.max_patients) THEN
    SET v_changed = 1;
  END IF;

  IF v_changed = 1 THEN
    -- Sinh lại slot 30 phút (đổi 30 tuỳ ý: 15/20/…)
    CALL generate_slots_for_schedule(NEW.id, 20);

    -- Reset bộ đếm (an toàn vì đã chặn khi có appointment)
    UPDATE doctor_schedules
    SET booked_patients = 0
    WHERE id = NEW.id;
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_ds_bd_guard_delete` BEFORE DELETE ON `doctor_schedules` FOR EACH ROW BEGIN
  DECLARE v_appt_cnt INT;

  SELECT COUNT(*) INTO v_appt_cnt
  FROM appointments
  WHERE doctor_schedule_id = OLD.id;

  IF v_appt_cnt > 0 THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Không thể xoá doctor_schedule vì vẫn còn appointment đang tham chiếu.';
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES (1,1,'DOC1',1,9,'Chứng chỉ Nội khoa','Bộ Y tế','2015-08-20'),(2,2,'DOC2',1,10,'Chứng chỉ Nội khoa','Bộ Y tế','2015-08-20'),(3,3,'DOC3',7,10,'Chứng chỉ Nội khoa','Bộ Y tế','2020-08-20'),(4,4,'DOC4',8,11,'Chứng chỉ Nội khoa','Bộ Y tế','2020-08-20'),(5,6,'DOC5',1,9,'Chứng chỉ Nội khoa','Bộ Y tế','2015-08-20'),(6,17,'DOC6',10,7,'Chứng chỉ Nội khoa','Bộ Y tế','2018-10-12'),(7,19,'DOC7',1,14,'Chứng Chỉ Tim Mạch Chuyên Sâu','Bệnh Viện Bạch Mai','2010-06-05'),(8,20,'DOC8',9,9,'Chứng Chỉ Siêu Âm Tim','Hội Tim Mạch Việt Nam','2012-05-01'),(9,21,'DOC9',1,12,'Chứng Chỉ Can Thiệp Mạch Vành','Đại Học Y Dược TP.HCM','2010-05-15'),(10,22,'DOC10',2,10,'Chứng Chỉ Nội Soi Phế Quản','Bệnh Viện Phổi Trung Ương','2010-05-15'),(11,23,'DOC11',2,10,'Chứng Chỉ Hô Hấp Cấp Cứu','Bộ Y Tế','2010-05-20'),(12,24,'DOC12',2,11,'Chứng Chỉ Điều Trị Bệnh Phổi Tắc Nghẽn Mãn Tính','Trường Đại Học Y Hà Nội','2009-06-12'),(13,25,'DOC13',6,9,'Thạc sĩ, CKII Chấn Thương Chỉnh Hình','BV Chấn Thương Chỉnh Hình','2010-06-14'),(14,26,'DOC14',10,9,'Chứng chỉ Nội khoa','Hội Tim Mạch Việt Nam','2010-06-16');
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
-- Table structure for table `drugtype`
--

DROP TABLE IF EXISTS `drugtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `drugtype` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drugtype`
--

LOCK TABLES `drugtype` WRITE;
/*!40000 ALTER TABLE `drugtype` DISABLE KEYS */;
INSERT INTO `drugtype` VALUES (6,'Chống dị ứng'),(3,'Giảm đau'),(1,'Kháng sinh'),(4,'Tiểu đường'),(7,'Tiêu hóa'),(2,'Tim mạch'),(5,'Vitamin');
/*!40000 ALTER TABLE `drugtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fcm_tokens`
--

DROP TABLE IF EXISTS `fcm_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fcm_tokens` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `device_type` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `fcm_tokens_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fcm_tokens`
--

LOCK TABLES `fcm_tokens` WRITE;
/*!40000 ALTER TABLE `fcm_tokens` DISABLE KEYS */;
INSERT INTO `fcm_tokens` VALUES (1,3,'fQB1Ub0qTCmtylEj_DgLLQ:APA91bHYh3xEtRu16QYaV8H-4qkP8ldNFPWCItW5VbdiBIP8Rxi9ojHK_uOy_n4TfaxBkdSmUIXTdzCGdaJSuq7jXRk_qSOrG67fCXTgjXnSackNCBw3bGc','android','2025-11-09 15:57:42',1),(2,3,'ekwSvng3Sb-FrKWHmw4Ck-:APA91bH3iHsicHrKfbfTsRemq_UUsBh1OvRqXSLLjVf5o7GHbJ_8YsuH5DX7A5iY7XC4W66VZjc_JQesrcBD_jmkf-l8S-KBtesSUW3OyX83wdinUc25mGk','android','2025-11-10 03:09:12',1),(3,10,'ekwSvng3Sb-FrKWHmw4Ck-:APA91bH3iHsicHrKfbfTsRemq_UUsBh1OvRqXSLLjVf5o7GHbJ_8YsuH5DX7A5iY7XC4W66VZjc_JQesrcBD_jmkf-l8S-KBtesSUW3OyX83wdinUc25mGk','android','2025-11-18 03:41:27',1),(4,9,'ekwSvng3Sb-FrKWHmw4Ck-:APA91bH3iHsicHrKfbfTsRemq_UUsBh1OvRqXSLLjVf5o7GHbJ_8YsuH5DX7A5iY7XC4W66VZjc_JQesrcBD_jmkf-l8S-KBtesSUW3OyX83wdinUc25mGk','android','2025-11-18 05:16:13',1),(6,17,'ekwSvng3Sb-FrKWHmw4Ck-:APA91bH3iHsicHrKfbfTsRemq_UUsBh1OvRqXSLLjVf5o7GHbJ_8YsuH5DX7A5iY7XC4W66VZjc_JQesrcBD_jmkf-l8S-KBtesSUW3OyX83wdinUc25mGk','android','2025-11-18 06:03:50',0),(7,17,'ekwSvng3Sb-FrKWHmw4Ck-:APA91bH3iHsicHrKfbfTsRemq_UUsBh1OvRqXSLLjVf5o7GHbJ_8YsuH5DX7A5iY7XC4W66VZjc_JQesrcBD_jmkf-l8S-KBtesSUW3OyX83wdinUc25mGk','android','2025-11-18 17:15:31',0),(8,17,'fvAKHi3VRLe78g5zlbGYa3:APA91bF-yLvTksN-sneV666STN-gBqAu-TTWfK9JhXCGwpbjs6Ij0VKUsPXZfpAugb461WeEQxpsdSV2caTElOC1RYszBLzOxlM4d5a3rqmb9BhMwp9_JB4','android','2025-11-19 06:15:13',1),(9,9,'fvAKHi3VRLe78g5zlbGYa3:APA91bF-yLvTksN-sneV666STN-gBqAu-TTWfK9JhXCGwpbjs6Ij0VKUsPXZfpAugb461WeEQxpsdSV2caTElOC1RYszBLzOxlM4d5a3rqmb9BhMwp9_JB4','android','2025-11-21 09:06:04',1),(10,38,'fvAKHi3VRLe78g5zlbGYa3:APA91bF-yLvTksN-sneV666STN-gBqAu-TTWfK9JhXCGwpbjs6Ij0VKUsPXZfpAugb461WeEQxpsdSV2caTElOC1RYszBLzOxlM4d5a3rqmb9BhMwp9_JB4','android','2025-11-21 09:10:23',1),(11,10,'fvAKHi3VRLe78g5zlbGYa3:APA91bF-yLvTksN-sneV666STN-gBqAu-TTWfK9JhXCGwpbjs6Ij0VKUsPXZfpAugb461WeEQxpsdSV2caTElOC1RYszBLzOxlM4d5a3rqmb9BhMwp9_JB4','android','2025-12-03 08:21:02',1);
/*!40000 ALTER TABLE `fcm_tokens` ENABLE KEYS */;
UNLOCK TABLES;

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
INSERT INTO `feedbacks` VALUES (1,1,5,'Bác sĩ tận tâm và nhiệt tình!',NULL),(2,1,5,'Y tá tận tâm và nhiệt tình!','2025-05-17 10:59:01'),(3,1,4,'Cơ sở vật chất ổn','2025-05-17 15:20:46'),(4,1,4,'Dịch vụ tốt','2025-05-19 00:18:43'),(5,4,5,'Cơ sở vật chất hiện đại','2025-05-24 04:15:34'),(6,1,4,'Cơ sở vật chất tốt','2025-05-25 02:12:36'),(7,1,5,'Dịch vụ tốt','2025-08-25 06:59:09');
/*!40000 ALTER TABLE `feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `icd10_catalog`
--

DROP TABLE IF EXISTS `icd10_catalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `icd10_catalog` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `name_vn` varchar(255) NOT NULL,
  `name_en` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Danh mục chuẩn mã ICD-10';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `icd10_catalog`
--

LOCK TABLES `icd10_catalog` WRITE;
/*!40000 ALTER TABLE `icd10_catalog` DISABLE KEYS */;
INSERT INTO `icd10_catalog` VALUES (1,'A09','Viêm dạ dày-ruột và viêm đại tràng do nhiễm trùng và không đặc hiệu','Infectious and unspecified gastroenteritis and colitis','I: A00-B99'),(2,'A00.9','Bệnh tả, không đặc hiệu','Cholera, unspecified','I: A00-B99'),(3,'B18.1','Viêm gan vi rút B mạn tính không có delta tác nhân','Chronic viral hepatitis B without delta-agent','I: A00-B99'),(4,'B24','Bệnh do vi rút gây suy giảm miễn dịch ở người (HIV) không đặc hiệu','Unspecified human immunodeficiency virus [HIV] disease','I: A00-B99'),(5,'B35.1','Nấm móng tay/chân','Tinea unguium','I: A00-B99'),(6,'B02.9','Bệnh Zona, không biến chứng','Zoster without complication','I: A00-B99'),(7,'C34.9','U ác tính của phế quản hoặc phổi, không đặc hiệu','Malignant neoplasm of bronchus or lung, unspecified','II: C00-D48'),(8,'C50.9','U ác tính của vú, không đặc hiệu','Malignant neoplasm of breast, unspecified','II: C00-D48'),(9,'D12.6','U lành tính của đại tràng, không đặc hiệu','Benign neoplasm of colon, unspecified','II: C00-D48'),(10,'D50.9','Thiếu máu do thiếu sắt, không đặc hiệu','Iron deficiency anaemia, unspecified','III: D50-D89'),(11,'D64.9','Thiếu máu, không đặc hiệu','Anaemia, unspecified','III: D50-D89'),(12,'D80.9','Suy giảm miễn dịch, không đặc hiệu','Immunodeficiency, unspecified','III: D50-D89'),(13,'E11.9','Đái tháo đường không phụ thuộc insulin, không biến chứng','Non-insulin-dependent diabetes mellitus without complications','IV: E00-E90'),(14,'E78.5','Tăng lipid máu, không đặc hiệu','Hyperlipidaemia, unspecified','IV: E00-E90'),(15,'E66.9','Béo phì, không đặc hiệu','Obesity, unspecified','IV: E00-E90'),(16,'E05.9','Nhiễm độc tuyến giáp, không đặc hiệu','Thyrotoxicosis, unspecified','IV: E00-E90'),(17,'F32.9','Giai đoạn trầm cảm, không đặc hiệu','Depressive episode, unspecified','V: F00-F99'),(18,'F41.9','Rối loạn lo âu, không đặc hiệu','Anxiety disorder, unspecified','V: F00-F99'),(19,'F10.2','Rối loạn tâm thần và hành vi do sử dụng rượu, phụ thuộc','Mental and behavioural disorders due to use of alcohol, dependence syndrome','V: F00-F99'),(20,'G43.9','Đau nửa đầu, không đặc hiệu','Migraine, unspecified','VI: G00-G99'),(21,'G20','Bệnh Parkinson','Parkinson\'s disease','VI: G00-G99'),(22,'G44.2','Đau đầu kiểu căng thẳng','Tension-type headache','VI: G00-G99'),(23,'G81.9','Liệt nửa người, không đặc hiệu','Hemiplegia, unspecified','VI: G00-G99'),(24,'I10','Tăng huyết áp vô căn (nguyên phát)','Essential (primary) hypertension','IX: I00-I99'),(25,'I25.1','Bệnh tim thiếu máu cục bộ mạn tính','Atherosclerotic heart disease','IX: I00-I99'),(26,'I64','Đột quỵ, không phân loại là xuất huyết hoặc nhồi máu','Stroke, not specified as haemorrhage or infarction','IX: I00-I99'),(27,'I50.0','Suy tim sung huyết','Congest heart failure','IX: I00-I99'),(28,'I83.9','Giãn tĩnh mạch chi dưới không có loét hoặc viêm','Varicose veins of lower extremities without ulcer or inflammation','IX: I00-I99'),(29,'J45.9','Hen phế quản, không đặc hiệu','Asthma, unspecified','X: J00-J99'),(30,'J44.9','Bệnh phổi tắc nghẽn mạn tính (COPD), không đặc hiệu','Chronic obstructive pulmonary disease, unspecified','X: J00-J99'),(31,'J02.9','Viêm họng cấp, không đặc hiệu','Acute pharyngitis, unspecified','X: J00-J99'),(32,'J18.9','Viêm phổi, không đặc hiệu','Pneumonia, unspecified','X: J00-J99'),(33,'K29.7','Viêm dạ dày, không đặc hiệu','Gastritis, unspecified','XI: K00-K93'),(34,'K35.9','Viêm ruột thừa cấp, không đặc hiệu','Acute appendicitis, unspecified','XI: K00-K93'),(35,'K74.6','Xơ gan, không đặc hiệu','Other and unspecified cirrhosis of liver','XI: K00-K93'),(36,'M54.5','Đau thắt lưng dưới','Low back pain','XIII: M00-M99'),(37,'M17.9','Thoái hóa khớp gối, không đặc hiệu','Osteoarthritis of knee, unspecified','XIII: M00-M99'),(38,'M79.1','Đau cơ','Myalgia','XIII: M00-M99'),(39,'M25.5','Đau khớp, không đặc hiệu','Pain in joint, unspecified','XIII: M00-M99'),(40,'M81.9','Loãng xương, không đặc hiệu','Osteoporosis, unspecified','XIII: M00-M99'),(41,'N18.9','Suy thận mạn, không đặc hiệu','Chronic kidney disease, unspecified','XIV: N00-N99'),(42,'N39.0','Nhiễm trùng đường tiết niệu, vị trí không đặc hiệu','Urinary tract infection, site unspecified','XIV: N00-N99'),(43,'Z00.0','Khám sức khỏe tổng quát','General medical examination','XXI: Z00-Z99'),(44,'K02.9','Sâu răng, không đặc hiệu','Dental caries, unspecified','XI: K00-K93'),(45,'K04.0','Viêm tủy răng','Pulpitis','XI: K00-K93'),(46,'K04.4','Viêm nha chu cấp tính','Acute apical periodontitis of pulpal origin','XI: K00-K93'),(47,'K05.1','Viêm lợi mạn tính','Chronic gingivitis','XI: K00-K93'),(48,'K05.3','Viêm nha chu mạn tính','Chronic periodontitis','XI: K00-K93'),(49,'K08.1','Mất răng do tai nạn, nhổ răng hoặc do bệnh quanh răng','Loss of teeth due to accident, extraction or local periodontal disease','XI: K00-K93'),(50,'K12.0','Viêm lợi/miệng áp tơ tái diễn','Recurrent oral aphthae','XI: K00-K93'),(51,'K00.0','Thiếu răng (Mất răng bẩm sinh)','Anodontia','XI: K00-K93'),(52,'K07.3','Bất thường vị trí răng, không đặc hiệu','Anomalies of tooth position, unspecified','XI: K00-K93'),(53,'K06.9','Rối loạn lợi và xương ổ răng mất răng, không đặc hiệu','Disorder of gingiva and edentulous alveolar ridge, unspecified','XI: K00-K93'),(54,'K04.7','Áp xe quanh chóp không có lỗ rò','Periapical abscess without sinus','XI: K00-K93'),(55,'K01.1','Răng kẹt hoặc răng mọc ngầm','Impacted teeth','XI: K00-K93'),(56,'K03.6','Mòn răng (Bào mòn răng)','Attrition and abrasion of teeth','XI: K00-K93'),(57,'K11.2','Viêm tuyến nước bọt','Sialadenitis','XI: K00-K93'),(58,'K13.7','Các tổn thương và bệnh lý khác của niêm mạc miệng, không đặc hiệu','Other and unspecified lesions of oral mucosa','XI: K00-K93');
/*!40000 ALTER TABLE `icd10_catalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `icd_specialty`
--

DROP TABLE IF EXISTS `icd_specialty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `icd_specialty` (
  `id` int NOT NULL AUTO_INCREMENT,
  `icd_prefix` varchar(255) DEFAULT NULL,
  `specialty_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `specialty_id` (`specialty_id`),
  CONSTRAINT `icd_specialty_ibfk_1` FOREIGN KEY (`specialty_id`) REFERENCES `specialty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `icd_specialty`
--

LOCK TABLES `icd_specialty` WRITE;
/*!40000 ALTER TABLE `icd_specialty` DISABLE KEYS */;
INSERT INTO `icd_specialty` VALUES (1,'A',15),(2,'B',9),(3,'B',15),(4,'C',11),(5,'D',11),(6,'D',12),(7,'E',10),(8,'F',14),(9,'G',13),(10,'I',1),(11,'I',13),(12,'I',3),(13,'J',2),(14,'K',15),(15,'K',8),(16,'K',3),(17,'M',6),(18,'N',16),(19,'Z',3);
/*!40000 ALTER TABLE `icd_specialty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `imaging_result_files`
--

DROP TABLE IF EXISTS `imaging_result_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imaging_result_files` (
  `id` int NOT NULL AUTO_INCREMENT,
  `imaging_test_id` int DEFAULT NULL,
  `file_url` varchar(255) NOT NULL,
  `file_type` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL COMMENT 'Mô tả hình ảnh Ví dụ: "Ảnh X-quang thẳng", "Lát cắt T1")',
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `imaging_test_id` (`imaging_test_id`),
  CONSTRAINT `imaging_result_files_ibfk_1` FOREIGN KEY (`imaging_test_id`) REFERENCES `imaging_tests` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Danh sách kết quả của 1 chẩn đoán';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imaging_result_files`
--

LOCK TABLES `imaging_result_files` WRITE;
/*!40000 ALTER TABLE `imaging_result_files` DISABLE KEYS */;
INSERT INTO `imaging_result_files` VALUES (3,3,'https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/imaging-results/MR14_ImagingTest_3_1.jpg','jpg','cẳng tay','MR14_ImagingTest_3_1.jpg','2025-11-18 05:14:56'),(4,3,'https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/imaging-results/MR14_ImagingTest_3_2.jpg','jpg','bàn tay','MR14_ImagingTest_3_2.jpg','2025-11-18 05:14:56');
/*!40000 ALTER TABLE `imaging_result_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `imaging_staff`
--

DROP TABLE IF EXISTS `imaging_staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imaging_staff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `experience_years` int DEFAULT NULL,
  `img_scode` varchar(255) DEFAULT NULL,
  `staff_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKe2rg732qdwscgydn78nuvco2q` (`staff_id`),
  CONSTRAINT `FKknjmdqkkoxj0op524vopd0u8w` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imaging_staff`
--

LOCK TABLES `imaging_staff` WRITE;
/*!40000 ALTER TABLE `imaging_staff` DISABLE KEYS */;
INSERT INTO `imaging_staff` VALUES (2,3,'IMGS1',10);
/*!40000 ALTER TABLE `imaging_staff` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_imagingstaff` BEFORE INSERT ON `imaging_staff` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(20);
    SET new_code = CONCAT('IMGS', IFNULL((SELECT MAX(id) FROM imaging_staff), 0) + 1);
    SET NEW.img_scode = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `imaging_tests`
--

DROP TABLE IF EXISTS `imaging_tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imaging_tests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `record_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `imaging_staff_id` int DEFAULT NULL,
  `image_type_id` int DEFAULT NULL,
  `requested_date` datetime DEFAULT NULL,
  `result_date` datetime DEFAULT NULL,
  `result` text,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `imageType_id` (`image_type_id`),
  KEY `imagingtests_ibfk_6` (`imaging_staff_id`),
  CONSTRAINT `imaging_tests_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `imaging_tests_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `imaging_tests_ibfk_5` FOREIGN KEY (`image_type_id`) REFERENCES `imagingtypes` (`id`),
  CONSTRAINT `imaging_tests_ibfk_6` FOREIGN KEY (`imaging_staff_id`) REFERENCES `imaging_staff` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imaging_tests`
--

LOCK TABLES `imaging_tests` WRITE;
/*!40000 ALTER TABLE `imaging_tests` DISABLE KEYS */;
INSERT INTO `imaging_tests` VALUES (2,4,4,2,7,'2025-10-27 09:54:16',NULL,NULL,'IN_PROGRESS'),(3,17,13,2,8,'2025-11-15 08:39:27','2025-11-15 09:00:00','Nứt xương cẳng tay','COMPLETED');
/*!40000 ALTER TABLE `imaging_tests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `imagingtypes`
--

DROP TABLE IF EXISTS `imagingtypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imagingtypes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `imaging_name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `department_id` int DEFAULT NULL,
  `imaging_code` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_it_d` (`department_id`),
  CONSTRAINT `FK_it_d` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imagingtypes`
--

LOCK TABLES `imagingtypes` WRITE;
/*!40000 ALTER TABLE `imagingtypes` DISABLE KEYS */;
INSERT INTO `imagingtypes` VALUES (1,'X-quang ngực thẳng',300000,'Chẩn đoán các bệnh lý phổi và tim mạch',1,'IMG1',1),(2,'Siêu âm ổ bụng',350000,'Đánh giá các cơ quan trong ổ bụng',1,'IMG2',1),(3,'CT Scanner vùng bụng',1200000,'Chẩn đoán chi tiết trước phẫu thuật ngoại khoa',2,'IMG3',1),(4,'Siêu âm tim',400000,'Đánh giá bệnh lý tim mạch ở trẻ em',3,'IMG4',1),(5,'Siêu âm thai',300000,'Theo dõi sự phát triển của thai nhi',5,'IMG5',1),(6,'Siêu âm sản phụ khoa',350000,'Đánh giá sức khỏe sinh sản',5,'IMG6',1),(7,'X-quang răng',200000,'Đánh giá bệnh lý trong răng',5,'IMG7',1),(8,'Chụp X-quang',300000,'',2,'IMG8',1);
/*!40000 ALTER TABLE `imagingtypes` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_imagingtypes` BEFORE INSERT ON `imagingtypes` FOR EACH ROW BEGIN
    DECLARE max_id INT DEFAULT 0;
    DECLARE new_code VARCHAR(20);

    -- Lấy id lớn nhất hiện có
    SELECT IFNULL(MAX(id), 0) INTO max_id FROM imagingtypes;

    -- Sinh mã mới
    SET new_code = CONCAT('IMG', max_id + 1);

    -- Gán vào cột imagingCode
    SET NEW.imaging_code = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

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
  `status` varchar(255) DEFAULT NULL,
  `admission_date` datetime(6) DEFAULT NULL,
  `discharge_date` datetime(6) DEFAULT NULL,
  `treatment_plan` varchar(255) DEFAULT NULL,
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
-- Table structure for table `invoice_sequence`
--

DROP TABLE IF EXISTS `invoice_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_sequence` (
  `id` int NOT NULL AUTO_INCREMENT,
  `year` varchar(255) DEFAULT NULL,
  `serial` varchar(255) DEFAULT NULL,
  `currentNumber` int DEFAULT NULL,
  `current_number` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_sequence`
--

LOCK TABLES `invoice_sequence` WRITE;
/*!40000 ALTER TABLE `invoice_sequence` DISABLE KEYS */;
INSERT INTO `invoice_sequence` VALUES (1,'2025','AA/20E',NULL,4);
/*!40000 ALTER TABLE `invoice_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `keyword_icd_hint`
--

DROP TABLE IF EXISTS `keyword_icd_hint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `keyword_icd_hint` (
  `id` int NOT NULL AUTO_INCREMENT,
  `keyword` varchar(255) DEFAULT NULL,
  `icd_prefix_hint` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `keyword_icd_hint`
--

LOCK TABLES `keyword_icd_hint` WRITE;
/*!40000 ALTER TABLE `keyword_icd_hint` DISABLE KEYS */;
INSERT INTO `keyword_icd_hint` VALUES (1,'tiêu chảy','A'),(2,'sốt','A'),(3,'nấm','B'),(4,'zona','B'),(5,'viêm gan','B'),(6,'u','C'),(7,'ung thư','C'),(8,'thiếu máu','D'),(9,'suy giảm miễn dịch','D'),(10,'đái tháo đường','E'),(11,'tuyến giáp','E'),(12,'béo phì','E'),(13,'trầm cảm','F'),(14,'lo âu','F'),(15,'rối loạn tâm thần','F'),(16,'đau đầu','G'),(17,'đau nửa đầu','G'),(18,'liệt','G'),(19,'tê','G'),(20,'parkinson','G'),(21,'tăng huyết áp','I'),(22,'bệnh tim','I'),(23,'suy tim','I'),(24,'đột quỵ','I'),(25,'giãn tĩnh mạch','I'),(26,'ho','J'),(27,'khó thở','J'),(28,'viêm phổi','J'),(29,'hen','J'),(30,'họng','J'),(31,'dạ dày','K'),(32,'gan','K'),(33,'xơ gan','K'),(34,'ruột thừa','K'),(35,'răng','K'),(36,'miệng','K'),(37,'đau lưng','M'),(38,'khớp','M'),(39,'xương','M'),(40,'thoái hóa','M'),(41,'thận','N'),(42,'tiết niệu','N'),(43,'khám tổng quát','Z');
/*!40000 ALTER TABLE `keyword_icd_hint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_parameters`
--

DROP TABLE IF EXISTS `lab_parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_parameters` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `min_reference` varchar(255) DEFAULT NULL,
  `max_reference` varchar(255) DEFAULT NULL,
  `test_type_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `test_type_id` (`test_type_id`),
  CONSTRAINT `lab_parameters_ibfk_1` FOREIGN KEY (`test_type_id`) REFERENCES `testtypes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_parameters`
--

LOCK TABLES `lab_parameters` WRITE;
/*!40000 ALTER TABLE `lab_parameters` DISABLE KEYS */;
INSERT INTO `lab_parameters` VALUES (1,'WBC (Bạch cầu)','10^9/L','4.0','10.0',1),(2,'RBC (Hồng cầu)','10^12/L','3.8','5.8',1),(3,'HGB (Hemoglobin)','g/L','120','160',1),(4,'HCT (Hematocrit)','%','37','54',1),(5,'PLT (Tiểu cầu)','10^9/L','150','450',1),(6,'MCV','fL','80','100',1),(7,'MCH','pg','27','32',1),(8,'RDW-CV','%','11.5','14.5',1),(9,'Glucose','mmol/L','3.9','6.4',2),(10,'ALT (SGPT)','U/L','0','40',2),(11,'AST (SGOT)','U/L','0','40',2),(12,'Creatinine','umol/L','60','110',2),(13,'Cholesterol','mmol/L','0','5.2',2),(14,'Urea','mmol/L','2.5','7.5',2),(15,'Acid Uric','umol/L','140','420',2),(16,'Total Protein','g/L','60','80',2),(17,'Albumin','g/L','35','50',2),(18,'Bilirubin T.T','umol/L','0','21',2),(19,'Triglycerides','mmol/L','0','1.7',2),(20,'HDL-C','mmol/L','0.9','3.0',2),(21,'LDL-C','mmol/L','0','3.37',2),(22,'CRP (Định lượng)','mg/L','0','5',2),(23,'PT (Prothrombin Time)','giây','11.0','13.5',3),(24,'INR','ratio','0.8','1.2',3),(25,'APTT','giây','25.0','35.0',3),(26,'Fibrinogen','g/L','2.0','4.0',3),(27,'pH','','5.0','8.0',4),(28,'Protein niệu','g/L','0.0','0.15',4),(29,'Bạch cầu niệu','tb/HPF','0','5',4),(30,'LEU (Bạch cầu)','Định tính','Âm tính','Âm tính',4),(31,'NIT (Nitrite)','Định tính','Âm tính','Âm tính',4),(32,'BIL (Bilirubin)','Định tính','Âm tính','Âm tính',4),(33,'KET (Ketone)','Định tính','Âm tính','Âm tính',4),(34,'TSH (Tuyến giáp)','uIU/mL','0.27','4.2',5),(35,'T3','nmol/L','1.0','2.8',5),(36,'FT4','pmol/L','10.0','25.0',5),(37,'HCG (Định lượng)','mIU/mL','0','5',5);
/*!40000 ALTER TABLE `lab_parameters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_staff`
--

DROP TABLE IF EXISTS `lab_staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_staff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `staff_id` int DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  `lab_scode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_lab_staff_id` (`staff_id`),
  CONSTRAINT `lab_staff_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_staff`
--

LOCK TABLES `lab_staff` WRITE;
/*!40000 ALTER TABLE `lab_staff` DISABLE KEYS */;
INSERT INTO `lab_staff` VALUES (1,8,3,'LABS1'),(2,12,2,'LABS2'),(3,13,3,'LABS3'),(4,14,2,'LABS4'),(5,15,3,'LABS5');
/*!40000 ALTER TABLE `lab_staff` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_labstaffs` BEFORE INSERT ON `lab_staff` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(15);
    SET new_code = CONCAT('LABS',  IFNULL((SELECT MAX(id) FROM lab_staff), 0) + 1);
    SET NEW.lab_scode = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `lab_test_details`
--

DROP TABLE IF EXISTS `lab_test_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_test_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `lab_test_id` int NOT NULL,
  `test_parameter_id` int NOT NULL,
  `result_value` varchar(255) DEFAULT NULL,
  `parameter_name` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `min_reference_range` varchar(255) DEFAULT NULL,
  `max_reference_range` varchar(255) DEFAULT NULL,
  `is_abnormal` tinyint(1) DEFAULT '0',
  `notes` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `lab_test_id` (`lab_test_id`),
  KEY `test_parameter_id` (`test_parameter_id`),
  CONSTRAINT `lab_test_details_ibfk_1` FOREIGN KEY (`lab_test_id`) REFERENCES `lab_tests` (`id`),
  CONSTRAINT `lab_test_details_ibfk_2` FOREIGN KEY (`test_parameter_id`) REFERENCES `lab_parameters` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_test_details`
--

LOCK TABLES `lab_test_details` WRITE;
/*!40000 ALTER TABLE `lab_test_details` DISABLE KEYS */;
INSERT INTO `lab_test_details` VALUES (15,3,9,'8.5','Glucose','mmol/L','3.9','6.4',1,'Tăng cao, nghi ngờ Đái tháo đường.'),(16,3,10,'55','ALT (SGPT)','U/L','0','40',1,'Men gan tăng nhẹ.'),(17,3,13,'6.1','Cholesterol','mmol/L','0','5.2',1,'Tăng cao.'),(18,3,19,'2.5','Triglycerides','mmol/L','0','1.7',1,'Tăng cao.'),(19,3,21,'4.2','LDL-C','mmol/L','0','3.37',1,'Tăng cao.'),(20,3,22,'8','CRP (Định lượng)','mg/L','0','5',1,'Tăng nhẹ, có thể do viêm nhiễm.'),(21,3,11,'30','AST (SGOT)','U/L','0','40',0,NULL),(22,3,12,'88','Creatinine','umol/L','60','110',0,NULL),(23,3,14,'5.5','Urea','mmol/L','2.5','7.5',0,NULL),(24,3,15,'350','Acid Uric','umol/L','140','420',0,NULL),(25,3,16,'72','Total Protein','g/L','60','80',0,NULL),(26,3,17,'40','Albumin','g/L','35','50',0,NULL),(27,3,18,'15','Bilirubin T.T','umol/L','0','21',0,NULL),(28,3,20,'1.1','HDL-C','mmol/L','0.9','3.0',0,NULL),(29,4,27,'8.5','pH','','5.0','8.0',1,'pH tăng nhẹ so với ngưỡng tham chiếu (8.0)'),(30,4,28,'0.18','Protein niệu','g/L','0.0','0.15',1,'Hơi cao so với ngưỡng tối đa (0.15 g/L)'),(31,4,29,'3','Bạch cầu niệu','tb/HPF','0','5',0,''),(32,4,30,'Âm tính','LEU (Bạch cầu)','Định tính','Âm tính','Âm tính',0,''),(33,4,31,'Âm tính','NIT (Nitrite)','Định tính','Âm tính','Âm tính',0,''),(34,4,32,'Âm tính','BIL (Bilirubin)','Định tính','Âm tính','Âm tính',0,''),(35,4,33,'Âm tính','KET (Ketone)','Định tính','Âm tính','Âm tính',0,''),(36,5,27,'6.3','pH','','5.0','8.0',0,'Bình thường'),(37,5,28,'0.16','Protein niệu','g/L','0.0','0.15',1,'Hơi cao'),(38,5,29,'4','Bạch cầu niệu','tb/HPF','0','5',0,''),(39,5,30,'Âm tính','LEU (Bạch cầu)','Định tính','Âm tính','Âm tính',0,''),(40,5,31,'Âm tính','NIT (Nitrite)','Định tính','Âm tính','Âm tính',0,''),(41,5,32,'Âm tính','BIL (Bilirubin)','Định tính','Âm tính','Âm tính',0,''),(42,5,33,'Âm tính','KET (Ketone)','Định tính','Âm tính','Âm tính',0,''),(43,7,1,'','WBC (Bạch cầu)','10^9/L','4.0','10.0',0,''),(44,7,2,'','RBC (Hồng cầu)','10^12/L','3.8','5.8',0,''),(45,7,3,'','HGB (Hemoglobin)','g/L','120','160',0,''),(46,7,4,'','HCT (Hematocrit)','%','37','54',0,''),(47,7,5,'','PLT (Tiểu cầu)','10^9/L','150','450',0,''),(48,7,6,'','MCV','fL','80','100',0,''),(49,7,7,'','MCH','pg','27','32',0,''),(50,7,8,'','RDW-CV','%','11.5','14.5',0,'');
/*!40000 ALTER TABLE `lab_test_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab_tests`
--

DROP TABLE IF EXISTS `lab_tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_tests` (
  `id` int NOT NULL AUTO_INCREMENT,
  `record_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `lab_staff_id` int DEFAULT NULL,
  `test_type_id` int DEFAULT NULL,
  `requested_date` datetime DEFAULT NULL,
  `result_date` datetime DEFAULT NULL,
  `result` text,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `labStaff_id` (`lab_staff_id`),
  KEY `testType_id` (`test_type_id`),
  CONSTRAINT `lab_tests_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `lab_tests_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `lab_tests_ibfk_4` FOREIGN KEY (`lab_staff_id`) REFERENCES `lab_staff` (`id`),
  CONSTRAINT `lab_tests_ibfk_5` FOREIGN KEY (`test_type_id`) REFERENCES `testtypes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_tests`
--

LOCK TABLES `lab_tests` WRITE;
/*!40000 ALTER TABLE `lab_tests` DISABLE KEYS */;
INSERT INTO `lab_tests` VALUES (3,6,6,2,2,'2025-11-03 04:39:09','2025-11-03 05:00:09','Có nhiều dấu hiệu bất thường','COMPLETED'),(4,6,6,5,4,'2025-11-03 04:39:09','2025-11-03 05:20:09','Xét nghiệm nước tiểu cho thấy pH tăng nhẹ (8.5) và Protein niệu ở mức ranh giới, các chỉ số khác âm tính.','COMPLETED'),(5,7,6,5,4,'2025-11-05 09:04:07',NULL,'Xét nghiệm nước tiểu cho thấy Protein niệu ở mức ranh giới, các chỉ số khác âm tính.','IN_PROGRESS'),(6,22,6,NULL,8,'2025-11-20 01:28:20',NULL,NULL,'PAID'),(7,23,10,2,1,'2025-11-21 03:56:27',NULL,NULL,'IN_PROGRESS');
/*!40000 ALTER TABLE `lab_tests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medical_record_icd10`
--

DROP TABLE IF EXISTS `medical_record_icd10`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medical_record_icd10` (
  `id` int NOT NULL AUTO_INCREMENT,
  `record_id` int NOT NULL COMMENT 'ID của Hồ sơ bệnh án (MedicalRecord)',
  `icd10_catalog_id` int NOT NULL COMMENT 'ID của Mã ICD-10 trong danh mục',
  `is_principal` tinyint(1) DEFAULT '0' COMMENT 'TRUE nếu là chẩn đoán chính, FALSE nếu là chẩn đoán phụ',
  `diagnosis_order` int DEFAULT '0' COMMENT 'Thứ tự ưu tiên của chẩn đoán (nếu là phụ)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_icd10` (`record_id`,`icd10_catalog_id`),
  KEY `icd10_catalog_id` (`icd10_catalog_id`),
  CONSTRAINT `medical_record_icd10_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`) ON DELETE CASCADE,
  CONSTRAINT `medical_record_icd10_ibfk_2` FOREIGN KEY (`icd10_catalog_id`) REFERENCES `icd10_catalog` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Liên kết chẩn đoán ICD-10 với hồ sơ bệnh án';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_record_icd10`
--

LOCK TABLES `medical_record_icd10` WRITE;
/*!40000 ALTER TABLE `medical_record_icd10` DISABLE KEYS */;
INSERT INTO `medical_record_icd10` VALUES (5,4,44,1,0),(6,4,45,0,1),(10,6,13,1,0),(13,24,31,1,0);
/*!40000 ALTER TABLE `medical_record_icd10` ENABLE KEYS */;
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
  `diagnosis` varchar(255) DEFAULT NULL,
  `appointment_id` int DEFAULT NULL,
  `initial_symptoms` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `patient_id` (`patient_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `FK_mr_ap` (`appointment_id`),
  CONSTRAINT `FK_mr_ap` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
  CONSTRAINT `medical_records_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `medical_records_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_records`
--

LOCK TABLES `medical_records` WRITE;
/*!40000 ALTER TABLE `medical_records` DISABLE KEYS */;
INSERT INTO `medical_records` VALUES (1,1,1,'Thiếu máu nhẹ',1,'Đau đầu, chóng mặt',NULL,'PENDING_RESULTS','MR1'),(2,1,1,'Loét dạ dày',1,'Đau bụng',NULL,'WAITING','MR2'),(3,1,3,'Loét dạ dày',8,'Đau bụng',NULL,'WAITING','MR3'),(4,1,4,'đau răng do sâu',14,'Đau răng hàm','Đau răng do sâu','PENDING_APPROVAL','MR4'),(5,4,4,NULL,13,'Đau răng',NULL,'WAITING','MR5'),(6,4,6,'Nghi ngờ Đái tháo đường',15,'Khát và Uống nhiều, Đi tiểu nhiều','Bệnh nhân có tứ chứng kinh điển (đa khát, đa niệu), sụt 5kg/tháng. Đề nghị xét nghiệm Glucose máu lúc đói và HbA1c khẩn.','COMPLETED','MR6'),(7,6,6,NULL,16,'khát, uống nước nhiều. Mệt mỏi trong người',NULL,'PENDING_RESULTS','MR7'),(13,1,4,NULL,17,'Nhức răng hàm',NULL,'WAITING','MR8'),(17,9,13,NULL,20,'nghi ngờ gãy xương tay',NULL,'PENDING_APPROVAL','MR14'),(20,9,10,NULL,21,'Khám bệnh',NULL,'IN_PROGRESS','MR18'),(22,7,6,NULL,23,'Khát và Uống nhiều, Đi tiểu nhiều',NULL,'PENDING_RESULTS','MR21'),(23,5,10,NULL,24,'Ho khan, có đàm',NULL,'PENDING_RESULTS','MR23'),(24,10,12,'sưng amidan, viêm họng cấp',25,'Ho nhiều, rát cổ họng','sưng amidan','COMPLETED','MR24');
/*!40000 ALTER TABLE `medical_records` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_medical_records` BEFORE INSERT ON `medical_records` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(20);
    SET new_code = CONCAT('MR',  IFNULL((SELECT MAX(id) FROM medical_records), 0) + 1);
    SET NEW.code = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `medicalexamination`
--

DROP TABLE IF EXISTS `medicalexamination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicalexamination` (
  `id` int NOT NULL AUTO_INCREMENT,
  `examination_name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `department_id` int DEFAULT NULL,
  `examination_code` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`),
  CONSTRAINT `medicalexamination_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicalexamination`
--

LOCK TABLES `medicalexamination` WRITE;
/*!40000 ALTER TABLE `medicalexamination` DISABLE KEYS */;
INSERT INTO `medicalexamination` VALUES (1,'Khám nội tổng quát',200000,'Khám và tư vấn sức khỏe chung',1,'EXA1',1),(2,'Khám ngoại tổng quát',250000,'Khám, chẩn đoán và tư vấn ngoại khoa',2,'EXA2',1),(3,'Khám nhi tổng quát',180000,'Khám sức khỏe trẻ em',3,'EXA3',1),(4,'Khám sản phụ khoa',220000,'Khám sức khỏe sinh sản và phụ nữ mang thai',5,'EXA4',1);
/*!40000 ALTER TABLE `medicalexamination` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_medicalexamination` BEFORE INSERT ON `medicalexamination` FOR EACH ROW BEGIN
    DECLARE max_id INT DEFAULT 0;
    DECLARE new_code VARCHAR(20);

    -- Lấy id lớn nhất hiện có
    SELECT IFNULL(MAX(id), 0) INTO max_id FROM medicalexamination;

    -- Sinh mã mới
    SET new_code = CONCAT('EXA', max_id + 1);

    -- Gán vào cột imagingCode
    SET NEW.examination_code = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

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
  `expiration_date` date DEFAULT NULL,
  `price` double DEFAULT NULL,
  `minimum_quantity` double DEFAULT NULL,
  `current_quantity` double DEFAULT NULL,
  `concentration` varchar(255) DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `production_date` date DEFAULT NULL,
  `drugtype_id` int DEFAULT NULL,
  `active_ingredient` varchar(255) DEFAULT NULL,
  `dosage_form` varchar(255) DEFAULT NULL,
  `route_of_administration` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `reserved_quantity` double DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_m_dt` (`drugtype_id`),
  CONSTRAINT `FK_m_dt` FOREIGN KEY (`drugtype_id`) REFERENCES `drugtype` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicines`
--

LOCK TABLES `medicines` WRITE;
/*!40000 ALTER TABLE `medicines` DISABLE KEYS */;
INSERT INTO `medicines` VALUES (1,'Paracetamol 500 mg','viên','2026-07-24',2000,100,1964,'500 mg','Traphaco',0,'2025-07-24',3,'Paracetamol','Viên nén',NULL,0),(2,'Amoxicillin 500 mg','viên','2026-08-24',1500,100,1439,'500 mg','DHG Pharma',0,'2025-08-24',1,'Amoxicillin','Viên nén',NULL,0),(3,'Ascorbic acid 500mg','viên','2026-08-01',1000,50,1000,'500 mg','Traphaco',0,'2025-08-01',5,'Ascorbic acid','Viên nén',NULL,30),(4,'Ibuprofen 400mg','viên','2026-11-20',2500,100,2458,'400 mg','Sanofi',0,'2025-11-20',3,'Ibuprofen','Viên nén',NULL,8),(5,'Acetylsalicylic acid 81mg','viên','2026-05-30',1800,100,1800,'81 mg','Bayer',0,'2025-05-30',2,'Acetylsalicylic acid','Viên nén',NULL,30),(6,'Metformin hydrochloride 500mg','viên','2025-12-08',3000,100,3000,'500 mg','US Pharma',0,'2023-12-01',4,'Metformin hydrochloride','Viên nén',NULL,50),(7,'Azithromycin 500mg','viên','2026-10-05',5000,100,4986,'500 mg','Pfizer',0,'2025-10-05',1,'Azithromycin','Viên nén',NULL,6),(8,'Loratadine 10mg','viên','2026-02-18',1200,50,1172,'10 mg','Stada',0,'2026-02-18',6,'Loratadine','Viên nén',NULL,0),(9,'Cefixime 200mg','viên','2025-09-15',3500,100,3500,'200 mg','Domesco',0,'2023-09-15',1,'Cefixime','Viên nén',NULL,30),(10,'Omeprazole 20mg','viên','2026-03-01',2200,100,2200,'20 mg','Mekophar',0,'2024-03-01',7,'Omeprazole','Viên nang',NULL,30);
/*!40000 ALTER TABLE `medicines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messagelogs`
--

DROP TABLE IF EXISTS `messagelogs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messagelogs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `message_id` int NOT NULL,
  `token_id` int NOT NULL,
  `status` varchar(255) NOT NULL,
  `error_message` varchar(255) DEFAULT NULL,
  `sent_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `received_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `message_id` (`message_id`),
  KEY `token_id` (`token_id`),
  CONSTRAINT `messagelogs_ibfk_1` FOREIGN KEY (`message_id`) REFERENCES `messages` (`id`) ON DELETE CASCADE,
  CONSTRAINT `messagelogs_ibfk_2` FOREIGN KEY (`token_id`) REFERENCES `fcm_tokens` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messagelogs`
--

LOCK TABLES `messagelogs` WRITE;
/*!40000 ALTER TABLE `messagelogs` DISABLE KEYS */;
INSERT INTO `messagelogs` VALUES (6,17,6,'SUCCESS',NULL,'2025-11-18 15:26:34',NULL),(8,19,10,'SUCCESS',NULL,'2025-11-21 09:23:19',NULL),(9,22,6,'FAILED','UNREGISTERED: Requested entity was not found.','2025-12-03 16:28:33',NULL),(10,22,7,'FAILED','UNREGISTERED: Requested entity was not found.','2025-12-03 16:28:33',NULL),(11,22,8,'SUCCESS',NULL,'2025-12-03 16:28:33',NULL);
/*!40000 ALTER TABLE `messagelogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `body` text NOT NULL,
  `data` text NOT NULL,
  `send_type` varchar(255) NOT NULL,
  `send_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (2,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Trần Thu Phương đã chấp nhận lịch hẹn.','{\"id\":\"17\",\"type\":\"APPOINTMENT_CONFIRMED\"}','Token','Trần Thu Phương','2025-11-10 09:48:19'),(3,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Trần Thu Phương đã chấp nhận lịch hẹn.','{\"id\":\"17\",\"type\":\"APPOINTMENT_CONFIRMED\"}','Token','Trần Thu Phương','2025-11-10 16:56:10'),(4,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Trần Thu Phương đã chấp nhận lịch hẹn.','{\"id\":\"17\",\"type\":\"APPOINTMENT_CONFIRMED\"}','Token','Trần Thu Phương','2025-11-10 17:21:24'),(5,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Trần Thu Phương đã chấp nhận lịch hẹn.','{\"id\":\"17\",\"type\":\"APPOINTMENT_CONFIRMED\"}','Token','Trần Thu Phương','2025-11-11 02:26:26'),(6,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Trần Thu Phương đã chấp nhận lịch hẹn.','{\"id\":\"17\",\"type\":\"APPOINTMENT_CONFIRMED\"}','Token','Trần Thu Phương','2025-11-11 02:43:31'),(7,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Trần Thu Phương đã chấp nhận lịch hẹn.','{\"id\":\"17\",\"type\":\"APPOINTMENT_CONFIRMED\"}','Token','Trần Thu Phương','2025-11-11 03:00:50'),(9,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Trần Thu Phương đã chấp nhận lịch hẹn.','{\"id\":\"17\",\"type\":\"APPOINTMENT_CONFIRMED\"}','Token','Trần Thu Phương','2025-11-11 03:15:49'),(10,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Trần Thu Phương đã chấp nhận lịch hẹn.','{\"id\":\"17\",\"type\":\"APPOINTMENT_CONFIRMED\"}','Token','Trần Thu Phương','2025-11-11 04:05:17'),(11,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Trần Thu Phương đã chấp nhận lịch hẹn.','{\"id\":\"17\",\"type\":\"APPOINTMENT_CONFIRMED\"}','Token','Trần Thu Phương','2025-11-11 06:55:15'),(12,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Phạm Yến Nhi đã chấp nhận lịch hẹn.','{\"id\":\"21\",\"type\":\"APPOINTMENT_DETAIL\"}','Token','Phạm Yến Nhi','2025-11-18 08:24:40'),(17,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Phạm Yến Nhi đã chấp nhận lịch hẹn.','{\"id\":\"21\",\"type\":\"APPOINTMENT_DETAIL\"}','Token','Phạm Yến Nhi','2025-11-18 15:26:33'),(19,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Đặng Văn Sĩ đã chấp nhận lịch hẹn.','{\"id\":\"25\",\"type\":\"APPOINTMENT_DETAIL\"}','Token','Đặng Văn Sĩ','2025-11-21 09:23:19'),(20,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Hoàng Thị Diễm Ngân đã chấp nhận lịch hẹn.','{\"id\":\"27\",\"type\":\"APPOINTMENT_DETAIL\"}','Token','Hoàng Thị Diễm Ngân','2025-11-29 04:53:44'),(21,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Hoàng Thị Diễm Ngân đã chấp nhận lịch hẹn.','{\"id\":\"27\",\"type\":\"APPOINTMENT_DETAIL\"}','Token','Hoàng Thị Diễm Ngân','2025-11-29 05:07:26'),(22,'Lịch hẹn của bạn đã được xác nhận!','Bác sĩ Phạm Yến Nhi đã chấp nhận lịch hẹn.','{\"id\":\"29\",\"type\":\"APPOINTMENT_DETAIL\"}','Token','Phạm Yến Nhi','2025-12-03 16:28:33');
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `receiver_id` int NOT NULL,
  `message_id` int NOT NULL,
  `is_read` tinyint(1) DEFAULT '0',
  `sent_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `notification_ibfk_2_idx` (`message_id`),
  KEY `notifications_ibfk_1` (`receiver_id`),
  CONSTRAINT `notification_ibfk_2` FOREIGN KEY (`message_id`) REFERENCES `messages` (`id`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (2,17,17,1,'2025-11-18 15:26:33.503956'),(4,38,19,1,'2025-11-21 09:23:18.586256'),(5,17,22,0,'2025-12-03 16:28:32.646011');
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
  `nurse_code` varchar(255) DEFAULT NULL,
  `staff_id` int DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  `nursecode` varchar(255) DEFAULT NULL,
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
INSERT INTO `nurses` VALUES (1,'NUR1',7,2,NULL);
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
    SET NEW.nurse_code = new_code;
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
  `patient_code` varchar(255) DEFAULT NULL,
  `medical_history` varchar(255) DEFAULT NULL,
  `insurance_number` varchar(255) DEFAULT NULL,
  `insurance_rate` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `patients_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (1,3,'PAT1','Tiền sử cao huyết áp','BH123456789',0.7),(3,9,'PAT2','Bệnh tim bẩm sinh','SV1234567',0.8),(4,10,'PAT4','','',0),(5,12,'PAT5','Phổi yếu','SV1526425888',0.8),(6,13,'PAT6','','',0),(7,15,'PAT7','','',0),(8,16,'PAT8','','',0),(9,17,'PAT9','','',0),(10,38,'PAT10','Không có','SV 0010234567',0),(11,39,'PAT11',NULL,'SV 0100234506',0.4);
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
    SET NEW.patient_code = new_code;
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
  `service_type` varchar(50) DEFAULT NULL,
  `service_id` int DEFAULT NULL,
  `description` text,
  `amount` decimal(38,2) DEFAULT NULL,
  `insurance_covered_amount` decimal(38,2) DEFAULT NULL,
  `patient_paid_amount` decimal(38,2) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `payment_id` (`payment_id`),
  CONSTRAINT `paymentdetails_ibfk_1` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentdetails`
--

LOCK TABLES `paymentdetails` WRITE;
/*!40000 ALTER TABLE `paymentdetails` DISABLE KEYS */;
INSERT INTO `paymentdetails` VALUES (4,2,'IMAGING_TEST',2,'X-quang răng',200000.00,140000.00,60000.00,'2025-10-27 09:54:16'),(5,3,'PRESCRIPTION',4,'Đơn thuốc ngày 2025-10-30T16:23:15.094107',34000.00,23800.00,10200.00,'2025-10-30 15:51:22'),(6,3,'EXAMINATION',4,'Khám ngoại tổng quát',250000.00,175000.00,75000.00,'2025-10-30 15:51:22'),(7,4,'LAB_TEST',3,'Xét nghiệm sinh hóa',200000.00,0.00,200000.00,'2025-11-01 04:39:09'),(8,4,'LAB_TEST',4,'Xét nghiệm Hóa sinh/Tế bào nước tiểu',120000.00,0.00,120000.00,'2025-11-01 04:39:09'),(9,5,'LAB_TEST',5,'Xét nghiệm Hóa sinh/Tế bào nước tiểu',120000.00,0.00,120000.00,'2025-11-05 09:04:07'),(10,6,'IMAGING_TEST',3,'Chụp X-quang',300000.00,0.00,300000.00,'2025-11-15 15:39:27'),(11,7,'LAB_TEST',6,'Xét nghiệm Ký sinh trùng',200000.00,0.00,200000.00,'2025-11-20 01:28:20'),(12,8,'LAB_TEST',7,'Xét nghiệm Huyết học (Máu tổng quát)',200000.00,160000.00,40000.00,'2025-11-21 03:56:27'),(13,9,'PRESCRIPTION',6,'Đơn thuốc ngày 2025-11-21T16:52:15.129144',109900.00,0.00,109900.00,'2025-11-21 10:29:14'),(14,10,'PRESCRIPTION',5,'Đơn thuốc ngày 2025-11-09T15:11:13.914280',118300.00,0.00,118300.00,'2025-12-03 16:57:24'),(15,10,'EXAMINATION',5,'Khám nội tổng quát',200000.00,0.00,200000.00,'2025-12-03 16:57:24');
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
  `total` decimal(38,2) DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `insurance_coverage` decimal(38,2) DEFAULT NULL,
  `patient_payment` decimal(38,2) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `actual_paid_amount` decimal(38,2) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `invoice_number` varchar(255) DEFAULT NULL,
  `invoice_serial` varchar(255) DEFAULT NULL,
  `is_invoice_issued` bit(1) DEFAULT NULL,
  `payment_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `cashier_id` (`cashier_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`cashier_id`) REFERENCES `cashier` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (2,4,1,450000.00,'2025-11-02 17:07:06','CASH',315000.00,135000.00,'PAID','2025-10-27 09:35:54',135000.00,NULL,'AA/20E-2025-000001','AA/20E',_binary '','PAY2'),(3,4,1,284000.00,'2025-11-03 09:32:08','CASH',198800.00,85200.00,'PAID','2025-10-30 15:51:22',100000.00,'Hoàn lại: 14800.00','AA/20E-2025-000002','AA/20E',_binary '','PAY3'),(4,6,1,320000.00,'2025-11-01 04:50:09','CASH',0.00,320000.00,'PAID','2025-11-01 04:39:09',NULL,NULL,NULL,NULL,NULL,'PAY4'),(5,7,1,120000.00,'2025-11-19 04:50:03','CASH',0.00,120000.00,'PAID','2025-11-05 09:04:07',120000.00,NULL,NULL,NULL,NULL,'PAY5'),(6,17,1,300000.00,'2025-11-15 15:41:39','CASH',0.00,300000.00,'PAID','2025-11-15 15:39:27',300000.00,NULL,'AA/20E-2025-000003','AA/20E',_binary '','PAY6'),(7,22,1,200000.00,'2025-11-20 01:29:00','CASH',0.00,200000.00,'PAID','2025-11-20 01:28:20',200000.00,NULL,NULL,NULL,NULL,'PAY7'),(8,23,1,200000.00,'2025-11-21 03:57:37','CASH',160000.00,40000.00,'PAID','2025-11-21 03:56:27',40000.00,NULL,'AA/20E-2025-000004','AA/20E',_binary '','PAY8'),(9,24,1,109900.00,'2025-11-21 10:33:20','CASH',0.00,109900.00,'PAID','2025-11-21 10:29:14',110000.00,'Hoàn lại: 100.00',NULL,NULL,NULL,'PAY9'),(10,6,1,318300.00,'2025-12-03 17:04:55','CASH',0.00,318300.00,'PAID','2025-12-03 16:57:24',320000.00,'Hoàn lại: 1700.00',NULL,NULL,NULL,'PAY10');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_payment` BEFORE INSERT ON `payments` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(20);
    SET new_code = CONCAT('PAY',  IFNULL((SELECT MAX(id) FROM payments), 0) + 1);
    SET NEW.payment_code = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `pharmacy_staff`
--

DROP TABLE IF EXISTS `pharmacy_staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pharmacy_staff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pha_scode` varchar(255) DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  `staff_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKntva7w46rxffkbqnhgfeu54gc` (`staff_id`),
  CONSTRAINT `FK1fq7vmyodls1ygd8lrt35n4cg` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pharmacy_staff`
--

LOCK TABLES `pharmacy_staff` WRITE;
/*!40000 ALTER TABLE `pharmacy_staff` DISABLE KEYS */;
INSERT INTO `pharmacy_staff` VALUES (1,'PHA1',3,5);
/*!40000 ALTER TABLE `pharmacy_staff` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_pharmacystaff` BEFORE INSERT ON `pharmacy_staff` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(20);
    SET new_code = CONCAT('PHA',  IFNULL((SELECT MAX(id) FROM pharmacy_staff), 0) + 1);
    SET NEW.pha_scode = new_code;
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
  `is_substitutable` tinyint(1) DEFAULT NULL,
  `daily_quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `prescription_id` (`prescription_id`),
  KEY `medicine_id` (`medicine_id`),
  CONSTRAINT `prescriptiondetails_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`id`),
  CONSTRAINT `prescriptiondetails_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptiondetails`
--

LOCK TABLES `prescriptiondetails` WRITE;
/*!40000 ALTER TABLE `prescriptiondetails` DISABLE KEYS */;
INSERT INTO `prescriptiondetails` VALUES (12,3,1,6,'Uống 1 viên x 2 lần/ngày','',1,2),(13,3,2,9,'Uống 1 viên x 3 lần/ngày','',0,3),(14,4,1,8,'Uống 1 viên x 2 lần/ngày','',1,2),(15,4,2,12,'Uống 1 viên x 3 lần/ngày','',0,3),(41,6,1,14,'1 viên x 2 lần','Uống sau bữa sáng, chiều',0,2),(42,6,4,21,'1 viên x 3 lần','Uống sau ăn',0,3),(43,6,2,14,'1 viên x 2 lần','Uống sau bữa sáng, chiều',0,2),(44,6,8,7,'1 viên x 1 lần','Uống trước ngủ',0,1),(63,5,2,21,'1 viên x 3 lần','Uống sau ăn',0,3),(64,5,7,14,'1 viên x 2 lần','Uống sau ăn sáng và tối',0,2),(65,5,8,14,'1 viên x 2 lần','Uống sau ăn sáng và tối',0,2);
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
  `doctor_id` int DEFAULT NULL,
  `pharmacist_id` int DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `prescription_date` datetime(6) NOT NULL,
  `total_days` int DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `dispensed_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `prescriptions_ibfk_4_idx` (`pharmacist_id`),
  CONSTRAINT `prescriptions_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `prescriptions_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `prescriptions_ibfk_4` FOREIGN KEY (`pharmacist_id`) REFERENCES `pharmacy_staff` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES (3,4,4,NULL,'CANCELED','2025-10-30 09:23:15.094107',3,'PRE3',NULL),(4,4,4,1,'COMPLETED','2025-10-31 04:49:57.983889',4,'PRE4','2025-11-24 05:16:39.190111'),(5,6,6,1,'COMPLETED','2025-11-09 08:11:13.914280',7,'PRE5','2025-12-03 17:12:24.631129'),(6,24,12,1,'COMPLETED','2025-11-21 09:52:15.129144',7,'PRE6','2025-11-27 04:08:07.014618');
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_prescriptions` BEFORE INSERT ON `prescriptions` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(20);
    SET new_code = CONCAT('PRE',  IFNULL((SELECT MAX(id) FROM prescriptions), 0) + 1);
    SET NEW.code = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `receptionist`
--

DROP TABLE IF EXISTS `receptionist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receptionist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `staff_id` int DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  `receptionist_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_receptionist_staff_id` (`staff_id`),
  CONSTRAINT `receptionist_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receptionist`
--

LOCK TABLES `receptionist` WRITE;
/*!40000 ALTER TABLE `receptionist` DISABLE KEYS */;
INSERT INTO `receptionist` VALUES (1,11,3,'PHA1');
/*!40000 ALTER TABLE `receptionist` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_receptionist` BEFORE INSERT ON `receptionist` FOR EACH ROW BEGIN
    DECLARE new_code VARCHAR(20);
    SET new_code = CONCAT('PHA',  IFNULL((SELECT MAX(id) FROM receptionist), 0) + 1);
    SET NEW.receptionist_code = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `resultexamination`
--

DROP TABLE IF EXISTS `resultexamination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resultexamination` (
  `id` int NOT NULL AUTO_INCREMENT,
  `record_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `examination_id` int DEFAULT NULL,
  `result` text,
  `status` varchar(255) DEFAULT NULL,
  `requested_date` datetime(6) DEFAULT NULL,
  `result_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `examination_id` (`examination_id`),
  CONSTRAINT `resultexamination_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `resultexamination_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `resultexamination_ibfk_4` FOREIGN KEY (`examination_id`) REFERENCES `medicalexamination` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resultexamination`
--

LOCK TABLES `resultexamination` WRITE;
/*!40000 ALTER TABLE `resultexamination` DISABLE KEYS */;
INSERT INTO `resultexamination` VALUES (4,4,4,2,NULL,'PAID','2025-10-27 09:35:53.888805',NULL),(5,6,6,1,NULL,'PAID','2025-11-01 04:33:55.806456',NULL);
/*!40000 ALTER TABLE `resultexamination` ENABLE KEYS */;
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
  `status` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `room_number` varchar(255) NOT NULL,
  `capacity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `department_id` (`department_id`),
  KEY `room_type_id` (`room_type_id`),
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `room_ibfk_2` FOREIGN KEY (`room_type_id`) REFERENCES `roomtypes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,1,1,'Phòng A1','AVAILABLE',NULL,'A1',1),(2,1,1,'Phòng A2','AVAILABLE',NULL,'A2',1),(3,2,1,'Phòng B1','AVAILABLE',NULL,'B1',1),(4,2,1,'Phòng B2','AVAILABLE',NULL,'B2',1),(5,8,4,'Phòng xét nghiệm 1','AVAILABLE',NULL,'BT1',4),(6,1,1,'Phòng A3','AVAILABLE',NULL,'A3',1),(7,1,1,'Phòng A4','AVAILABLE',NULL,'A4',1),(8,1,1,'Phòng A5','AVAILABLE',NULL,'A5',1),(9,1,1,'Phòng A6','AVAILABLE',NULL,'A6',1),(10,2,1,'Phòng B3','AVAILABLE',NULL,'B3',1),(11,2,1,'Phòng B4','AVAILABLE',NULL,'B4',1),(12,2,1,'Phòng B5','AVAILABLE',NULL,'B5',1),(13,2,1,'Phòng B6','AVAILABLE',NULL,'B6',1),(14,2,1,'Phòng B7','AVAILABLE',NULL,'B7',1),(15,3,1,'Phòng C1','AVAILABLE',NULL,'C1',1),(16,3,1,'Phòng C2','AVAILABLE',NULL,'C2',1),(17,2,2,'Phòng A22','AVAILABLE',NULL,'A22',1),(18,2,2,'Phòng A23','AVAILABLE','Phòng chăm sóc bệnh nhân nội trú','A23',1),(19,1,1,'Phòng 106','OCCUPIED','','106',1),(20,1,1,'Phòng A7K','OCCUPIED','','A7',0),(21,1,1,'Phòng A8K','OCCUPIED','','A8',0),(22,1,1,'Phòng A9K','OCCUPIED','','A9',0),(23,2,1,'Phòng D01','AVAILABLE','','D1',0),(24,2,1,'Phòng D2','AVAILABLE','','D2',0);
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
  `code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roomtypes`
--

LOCK TABLES `roomtypes` WRITE;
/*!40000 ALTER TABLE `roomtypes` DISABLE KEYS */;
INSERT INTO `roomtypes` VALUES (1,'Phòng khám','PHONG_KHAM'),(2,'Phòng thường','PHONG_THUONG'),(3,'Phòng VIP','PHONG_VIP'),(4,'Phòng Xét Nghiệm','XET_NGHIEM'),(5,'Phòng Chẩn đoán Hình ảnh','CHAN_DOAN_HINH_ANH'),(6,'Phòng Tiếp nhận','TIEP_NHAN'),(7,'Phòng Thu ngân','THU_NGAN'),(8,'Phòng Thuốc','PHONG_THUOC');
/*!40000 ALTER TABLE `roomtypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_slots`
--

DROP TABLE IF EXISTS `schedule_slots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_slots` (
  `id` int NOT NULL AUTO_INCREMENT,
  `doctor_schedule_id` int NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `is_booked` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_slot_unique` (`doctor_schedule_id`,`start_time`,`end_time`),
  KEY `idx_slot_sched` (`doctor_schedule_id`),
  KEY `idx_slot_booked` (`is_booked`),
  CONSTRAINT `fk_slot_schedule` FOREIGN KEY (`doctor_schedule_id`) REFERENCES `doctor_schedules` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1423 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_slots`
--

LOCK TABLES `schedule_slots` WRITE;
/*!40000 ALTER TABLE `schedule_slots` DISABLE KEYS */;
INSERT INTO `schedule_slots` VALUES (1,2,'08:00:00','08:20:00',1),(2,2,'08:20:00','08:40:00',0),(3,2,'08:40:00','09:00:00',1),(4,2,'09:00:00','09:20:00',0),(5,2,'09:20:00','09:40:00',0),(6,2,'09:40:00','10:00:00',0),(7,2,'10:00:00','10:20:00',0),(8,2,'10:20:00','10:40:00',0),(9,2,'10:40:00','11:00:00',0),(10,2,'11:00:00','11:20:00',0),(11,2,'11:20:00','11:40:00',0),(12,2,'11:40:00','12:00:00',0),(13,152,'18:00:00','18:20:00',0),(14,152,'18:20:00','18:40:00',0),(15,152,'18:40:00','19:00:00',1),(16,152,'19:00:00','19:20:00',0),(17,152,'19:20:00','19:40:00',0),(18,152,'19:40:00','20:00:00',0),(19,152,'20:00:00','20:20:00',0),(20,152,'20:20:00','20:40:00',0),(21,152,'20:40:00','21:00:00',0),(22,253,'08:00:00','08:20:00',0),(23,253,'08:20:00','08:40:00',0),(24,253,'08:40:00','09:00:00',0),(25,253,'09:00:00','09:20:00',0),(26,253,'09:20:00','09:40:00',0),(27,253,'09:40:00','10:00:00',0),(28,253,'10:00:00','10:20:00',1),(29,253,'10:20:00','10:40:00',0),(30,253,'10:40:00','11:00:00',0),(31,253,'11:00:00','11:20:00',0),(32,253,'11:20:00','11:40:00',0),(33,253,'11:40:00','12:00:00',0),(34,316,'18:00:00','18:20:00',0),(35,316,'18:20:00','18:40:00',0),(36,316,'18:40:00','19:00:00',0),(37,316,'19:00:00','19:20:00',1),(38,316,'19:20:00','19:40:00',0),(39,316,'19:40:00','20:00:00',0),(40,316,'20:00:00','20:20:00',0),(41,316,'20:20:00','20:40:00',0),(42,316,'20:40:00','21:00:00',0),(43,315,'13:00:00','13:20:00',0),(44,315,'13:20:00','13:40:00',0),(45,315,'13:40:00','14:00:00',1),(46,315,'14:00:00','14:20:00',0),(47,315,'14:20:00','14:40:00',0),(48,315,'14:40:00','15:00:00',0),(49,315,'15:00:00','15:20:00',0),(50,315,'15:20:00','15:40:00',0),(51,315,'15:40:00','16:00:00',0),(52,315,'16:00:00','16:20:00',0),(53,315,'16:20:00','16:40:00',0),(54,315,'16:40:00','17:00:00',0),(55,306,'08:00:00','08:20:00',0),(56,306,'08:20:00','08:40:00',0),(57,306,'08:40:00','09:00:00',0),(58,306,'09:00:00','09:20:00',0),(59,306,'09:20:00','09:40:00',0),(60,306,'09:40:00','10:00:00',0),(61,306,'10:00:00','10:20:00',1),(62,306,'10:20:00','10:40:00',0),(63,306,'10:40:00','11:00:00',0),(64,306,'11:00:00','11:20:00',0),(65,306,'11:20:00','11:40:00',0),(66,306,'11:40:00','12:00:00',0),(67,3,'13:00:00','13:20:00',1),(68,3,'13:20:00','13:40:00',0),(69,3,'13:40:00','14:00:00',0),(70,3,'14:00:00','14:20:00',0),(71,3,'14:20:00','14:40:00',0),(72,3,'14:40:00','15:00:00',0),(73,3,'15:00:00','15:20:00',0),(74,3,'15:20:00','15:40:00',0),(75,3,'15:40:00','16:00:00',0),(76,3,'16:00:00','16:20:00',0),(77,3,'16:20:00','16:40:00',0),(78,3,'16:40:00','17:00:00',0),(79,317,'08:00:00','08:20:00',0),(80,317,'08:20:00','08:40:00',0),(81,317,'08:40:00','09:00:00',0),(82,317,'09:00:00','09:20:00',0),(83,317,'09:20:00','09:40:00',0),(84,317,'09:40:00','10:00:00',0),(85,317,'10:00:00','10:20:00',0),(86,317,'10:20:00','10:40:00',0),(87,317,'10:40:00','11:00:00',0),(88,317,'11:00:00','11:20:00',0),(89,317,'11:20:00','11:40:00',0),(90,317,'11:40:00','12:00:00',0),(91,318,'08:00:00','08:20:00',0),(92,318,'08:20:00','08:40:00',0),(93,318,'08:40:00','09:00:00',0),(94,318,'09:00:00','09:20:00',0),(95,318,'09:20:00','09:40:00',0),(96,318,'09:40:00','10:00:00',0),(97,318,'10:00:00','10:20:00',0),(98,318,'10:20:00','10:40:00',0),(99,318,'10:40:00','11:00:00',0),(100,318,'11:00:00','11:20:00',0),(101,318,'11:20:00','11:40:00',0),(102,318,'11:40:00','12:00:00',0),(103,319,'08:00:00','08:20:00',0),(104,319,'08:20:00','08:40:00',1),(105,319,'08:40:00','09:00:00',0),(106,319,'09:00:00','09:20:00',0),(107,319,'09:20:00','09:40:00',0),(108,319,'09:40:00','10:00:00',0),(109,319,'10:00:00','10:20:00',0),(110,319,'10:20:00','10:40:00',0),(111,319,'10:40:00','11:00:00',0),(112,319,'11:00:00','11:20:00',0),(113,319,'11:20:00','11:40:00',0),(114,319,'11:40:00','12:00:00',0),(115,320,'13:00:00','13:20:00',0),(116,320,'13:20:00','13:40:00',0),(117,320,'13:40:00','14:00:00',0),(118,320,'14:00:00','14:20:00',0),(119,320,'14:20:00','14:40:00',0),(120,320,'14:40:00','15:00:00',0),(121,320,'15:00:00','15:20:00',0),(122,320,'15:20:00','15:40:00',0),(123,320,'15:40:00','16:00:00',0),(124,320,'16:00:00','16:20:00',0),(125,320,'16:20:00','16:40:00',0),(126,320,'16:40:00','17:00:00',0),(127,321,'13:00:00','13:20:00',0),(128,321,'13:20:00','13:40:00',0),(129,321,'13:40:00','14:00:00',0),(130,321,'14:00:00','14:20:00',0),(131,321,'14:20:00','14:40:00',0),(132,321,'14:40:00','15:00:00',0),(133,321,'15:00:00','15:20:00',0),(134,321,'15:20:00','15:40:00',0),(135,321,'15:40:00','16:00:00',0),(136,321,'16:00:00','16:20:00',0),(137,321,'16:20:00','16:40:00',0),(138,321,'16:40:00','17:00:00',0),(139,322,'13:00:00','13:20:00',0),(140,322,'13:20:00','13:40:00',0),(141,322,'13:40:00','14:00:00',0),(142,322,'14:00:00','14:20:00',0),(143,322,'14:20:00','14:40:00',0),(144,322,'14:40:00','15:00:00',0),(145,322,'15:00:00','15:20:00',0),(146,322,'15:20:00','15:40:00',0),(147,322,'15:40:00','16:00:00',0),(148,322,'16:00:00','16:20:00',0),(149,322,'16:20:00','16:40:00',0),(150,322,'16:40:00','17:00:00',0),(151,323,'18:00:00','18:20:00',0),(152,323,'18:20:00','18:40:00',0),(153,323,'18:40:00','19:00:00',0),(154,323,'19:00:00','19:20:00',0),(155,323,'19:20:00','19:40:00',0),(156,323,'19:40:00','20:00:00',0),(157,323,'20:00:00','20:20:00',0),(158,323,'20:20:00','20:40:00',0),(159,323,'20:40:00','21:00:00',0),(160,324,'08:00:00','08:20:00',0),(161,324,'08:20:00','08:40:00',0),(162,324,'08:40:00','09:00:00',0),(163,324,'09:00:00','09:20:00',0),(164,324,'09:20:00','09:40:00',0),(165,324,'09:40:00','10:00:00',0),(166,324,'10:00:00','10:20:00',0),(167,324,'10:20:00','10:40:00',0),(168,324,'10:40:00','11:00:00',0),(169,324,'11:00:00','11:20:00',0),(170,324,'11:20:00','11:40:00',0),(171,324,'11:40:00','12:00:00',0),(184,326,'08:00:00','08:20:00',0),(185,326,'08:20:00','08:40:00',1),(186,326,'08:40:00','09:00:00',0),(187,326,'09:00:00','09:20:00',0),(188,326,'09:20:00','09:40:00',0),(189,326,'09:40:00','10:00:00',0),(190,326,'10:00:00','10:20:00',0),(191,326,'10:20:00','10:40:00',0),(192,326,'10:40:00','11:00:00',0),(193,326,'11:00:00','11:20:00',0),(194,326,'11:20:00','11:40:00',0),(195,326,'11:40:00','12:00:00',0),(196,327,'13:00:00','13:20:00',0),(197,327,'13:20:00','13:40:00',0),(198,327,'13:40:00','14:00:00',0),(199,327,'14:00:00','14:20:00',0),(200,327,'14:20:00','14:40:00',0),(201,327,'14:40:00','15:00:00',0),(202,327,'15:00:00','15:20:00',0),(203,327,'15:20:00','15:40:00',0),(204,327,'15:40:00','16:00:00',0),(205,327,'16:00:00','16:20:00',0),(206,327,'16:20:00','16:40:00',0),(207,327,'16:40:00','17:00:00',0),(208,328,'08:00:00','08:20:00',1),(209,328,'08:20:00','08:40:00',0),(210,328,'08:40:00','09:00:00',0),(211,328,'09:00:00','09:20:00',0),(212,328,'09:20:00','09:40:00',0),(213,328,'09:40:00','10:00:00',0),(214,328,'10:00:00','10:20:00',0),(215,328,'10:20:00','10:40:00',0),(216,328,'10:40:00','11:00:00',0),(217,328,'11:00:00','11:20:00',0),(218,328,'11:20:00','11:40:00',0),(219,328,'11:40:00','12:00:00',0),(220,329,'13:00:00','13:20:00',0),(221,329,'13:20:00','13:40:00',0),(222,329,'13:40:00','14:00:00',0),(223,329,'14:00:00','14:20:00',0),(224,329,'14:20:00','14:40:00',0),(225,329,'14:40:00','15:00:00',0),(226,329,'15:00:00','15:20:00',0),(227,329,'15:20:00','15:40:00',0),(228,329,'15:40:00','16:00:00',0),(229,329,'16:00:00','16:20:00',0),(230,329,'16:20:00','16:40:00',0),(231,329,'16:40:00','17:00:00',0),(232,330,'08:00:00','08:20:00',0),(233,330,'08:20:00','08:40:00',0),(234,330,'08:40:00','09:00:00',0),(235,330,'09:00:00','09:20:00',0),(236,330,'09:20:00','09:40:00',0),(237,330,'09:40:00','10:00:00',0),(238,330,'10:00:00','10:20:00',0),(239,330,'10:20:00','10:40:00',0),(240,330,'10:40:00','11:00:00',0),(241,330,'11:00:00','11:20:00',0),(242,330,'11:20:00','11:40:00',0),(243,330,'11:40:00','12:00:00',0),(244,331,'13:00:00','13:20:00',0),(245,331,'13:20:00','13:40:00',0),(246,331,'13:40:00','14:00:00',0),(247,331,'14:00:00','14:20:00',0),(248,331,'14:20:00','14:40:00',0),(249,331,'14:40:00','15:00:00',0),(250,331,'15:00:00','15:20:00',0),(251,331,'15:20:00','15:40:00',0),(252,331,'15:40:00','16:00:00',0),(253,331,'16:00:00','16:20:00',0),(254,331,'16:20:00','16:40:00',0),(255,331,'16:40:00','17:00:00',0),(256,332,'08:00:00','08:20:00',0),(257,332,'08:20:00','08:40:00',0),(258,332,'08:40:00','09:00:00',0),(259,332,'09:00:00','09:20:00',0),(260,332,'09:20:00','09:40:00',0),(261,332,'09:40:00','10:00:00',0),(262,332,'10:00:00','10:20:00',0),(263,332,'10:20:00','10:40:00',0),(264,332,'10:40:00','11:00:00',0),(265,332,'11:00:00','11:20:00',0),(266,332,'11:20:00','11:40:00',0),(267,332,'11:40:00','12:00:00',0),(268,333,'08:00:00','08:20:00',0),(269,333,'08:20:00','08:40:00',0),(270,333,'08:40:00','09:00:00',0),(271,333,'09:00:00','09:20:00',0),(272,333,'09:20:00','09:40:00',0),(273,333,'09:40:00','10:00:00',0),(274,333,'10:00:00','10:20:00',0),(275,333,'10:20:00','10:40:00',0),(276,333,'10:40:00','11:00:00',0),(277,333,'11:00:00','11:20:00',0),(278,333,'11:20:00','11:40:00',0),(279,333,'11:40:00','12:00:00',0),(280,334,'13:00:00','13:20:00',0),(281,334,'13:20:00','13:40:00',0),(282,334,'13:40:00','14:00:00',0),(283,334,'14:00:00','14:20:00',0),(284,334,'14:20:00','14:40:00',0),(285,334,'14:40:00','15:00:00',0),(286,334,'15:00:00','15:20:00',0),(287,334,'15:20:00','15:40:00',0),(288,334,'15:40:00','16:00:00',0),(289,334,'16:00:00','16:20:00',0),(290,334,'16:20:00','16:40:00',0),(291,334,'16:40:00','17:00:00',0),(292,335,'13:00:00','13:20:00',0),(293,335,'13:20:00','13:40:00',0),(294,335,'13:40:00','14:00:00',0),(295,335,'14:00:00','14:20:00',0),(296,335,'14:20:00','14:40:00',0),(297,335,'14:40:00','15:00:00',0),(298,335,'15:00:00','15:20:00',0),(299,335,'15:20:00','15:40:00',0),(300,335,'15:40:00','16:00:00',0),(301,335,'16:00:00','16:20:00',0),(302,335,'16:20:00','16:40:00',0),(303,335,'16:40:00','17:00:00',0),(304,336,'18:00:00','18:20:00',0),(305,336,'18:20:00','18:40:00',0),(306,336,'18:40:00','19:00:00',0),(307,336,'19:00:00','19:20:00',0),(308,336,'19:20:00','19:40:00',0),(309,336,'19:40:00','20:00:00',0),(310,336,'20:00:00','20:20:00',0),(311,336,'20:20:00','20:40:00',0),(312,336,'20:40:00','21:00:00',0),(313,337,'08:00:00','08:20:00',0),(314,337,'08:20:00','08:40:00',0),(315,337,'08:40:00','09:00:00',0),(316,337,'09:00:00','09:20:00',0),(317,337,'09:20:00','09:40:00',0),(318,337,'09:40:00','10:00:00',0),(319,337,'10:00:00','10:20:00',0),(320,337,'10:20:00','10:40:00',0),(321,337,'10:40:00','11:00:00',0),(322,337,'11:00:00','11:20:00',0),(323,337,'11:20:00','11:40:00',0),(324,337,'11:40:00','12:00:00',0),(325,338,'13:00:00','13:20:00',0),(326,338,'13:20:00','13:40:00',0),(327,338,'13:40:00','14:00:00',0),(328,338,'14:00:00','14:20:00',0),(329,338,'14:20:00','14:40:00',0),(330,338,'14:40:00','15:00:00',0),(331,338,'15:00:00','15:20:00',0),(332,338,'15:20:00','15:40:00',0),(333,338,'15:40:00','16:00:00',0),(334,338,'16:00:00','16:20:00',0),(335,338,'16:20:00','16:40:00',0),(336,338,'16:40:00','17:00:00',0),(337,339,'08:00:00','08:20:00',0),(338,339,'08:20:00','08:40:00',0),(339,339,'08:40:00','09:00:00',0),(340,339,'09:00:00','09:20:00',0),(341,339,'09:20:00','09:40:00',0),(342,339,'09:40:00','10:00:00',0),(343,339,'10:00:00','10:20:00',0),(344,339,'10:20:00','10:40:00',0),(345,339,'10:40:00','11:00:00',0),(346,339,'11:00:00','11:20:00',0),(347,339,'11:20:00','11:40:00',0),(348,339,'11:40:00','12:00:00',0),(349,340,'13:00:00','13:20:00',0),(350,340,'13:20:00','13:40:00',0),(351,340,'13:40:00','14:00:00',0),(352,340,'14:00:00','14:20:00',0),(353,340,'14:20:00','14:40:00',0),(354,340,'14:40:00','15:00:00',0),(355,340,'15:00:00','15:20:00',0),(356,340,'15:20:00','15:40:00',0),(357,340,'15:40:00','16:00:00',0),(358,340,'16:00:00','16:20:00',0),(359,340,'16:20:00','16:40:00',0),(360,340,'16:40:00','17:00:00',0),(361,341,'08:00:00','08:20:00',0),(362,341,'08:20:00','08:40:00',0),(363,341,'08:40:00','09:00:00',0),(364,341,'09:00:00','09:20:00',0),(365,341,'09:20:00','09:40:00',0),(366,341,'09:40:00','10:00:00',0),(367,341,'10:00:00','10:20:00',0),(368,341,'10:20:00','10:40:00',0),(369,341,'10:40:00','11:00:00',0),(370,341,'11:00:00','11:20:00',0),(371,341,'11:20:00','11:40:00',0),(372,341,'11:40:00','12:00:00',0),(373,342,'13:00:00','13:20:00',0),(374,342,'13:20:00','13:40:00',0),(375,342,'13:40:00','14:00:00',0),(376,342,'14:00:00','14:20:00',0),(377,342,'14:20:00','14:40:00',0),(378,342,'14:40:00','15:00:00',0),(379,342,'15:00:00','15:20:00',0),(380,342,'15:20:00','15:40:00',0),(381,342,'15:40:00','16:00:00',0),(382,342,'16:00:00','16:20:00',0),(383,342,'16:20:00','16:40:00',0),(384,342,'16:40:00','17:00:00',0),(385,343,'08:00:00','08:20:00',0),(386,343,'08:20:00','08:40:00',0),(387,343,'08:40:00','09:00:00',0),(388,343,'09:00:00','09:20:00',0),(389,343,'09:20:00','09:40:00',0),(390,343,'09:40:00','10:00:00',0),(391,343,'10:00:00','10:20:00',0),(392,343,'10:20:00','10:40:00',0),(393,343,'10:40:00','11:00:00',0),(394,343,'11:00:00','11:20:00',0),(395,343,'11:20:00','11:40:00',0),(396,343,'11:40:00','12:00:00',0),(397,344,'13:00:00','13:20:00',0),(398,344,'13:20:00','13:40:00',0),(399,344,'13:40:00','14:00:00',0),(400,344,'14:00:00','14:20:00',0),(401,344,'14:20:00','14:40:00',0),(402,344,'14:40:00','15:00:00',0),(403,344,'15:00:00','15:20:00',0),(404,344,'15:20:00','15:40:00',0),(405,344,'15:40:00','16:00:00',0),(406,344,'16:00:00','16:20:00',0),(407,344,'16:20:00','16:40:00',0),(408,344,'16:40:00','17:00:00',0),(409,345,'13:00:00','13:20:00',0),(410,345,'13:20:00','13:40:00',0),(411,345,'13:40:00','14:00:00',0),(412,345,'14:00:00','14:20:00',0),(413,345,'14:20:00','14:40:00',0),(414,345,'14:40:00','15:00:00',0),(415,345,'15:00:00','15:20:00',0),(416,345,'15:20:00','15:40:00',0),(417,345,'15:40:00','16:00:00',0),(418,345,'16:00:00','16:20:00',0),(419,345,'16:20:00','16:40:00',0),(420,345,'16:40:00','17:00:00',0),(421,346,'18:00:00','18:20:00',0),(422,346,'18:20:00','18:40:00',0),(423,346,'18:40:00','19:00:00',0),(424,346,'19:00:00','19:20:00',0),(425,346,'19:20:00','19:40:00',0),(426,346,'19:40:00','20:00:00',0),(427,346,'20:00:00','20:20:00',0),(428,346,'20:20:00','20:40:00',0),(429,346,'20:40:00','21:00:00',0),(430,347,'08:00:00','08:20:00',0),(431,347,'08:20:00','08:40:00',0),(432,347,'08:40:00','09:00:00',0),(433,347,'09:00:00','09:20:00',0),(434,347,'09:20:00','09:40:00',0),(435,347,'09:40:00','10:00:00',0),(436,347,'10:00:00','10:20:00',0),(437,347,'10:20:00','10:40:00',0),(438,347,'10:40:00','11:00:00',0),(439,347,'11:00:00','11:20:00',0),(440,347,'11:20:00','11:40:00',0),(441,347,'11:40:00','12:00:00',0),(442,348,'13:00:00','13:20:00',0),(443,348,'13:20:00','13:40:00',0),(444,348,'13:40:00','14:00:00',0),(445,348,'14:00:00','14:20:00',0),(446,348,'14:20:00','14:40:00',0),(447,348,'14:40:00','15:00:00',0),(448,348,'15:00:00','15:20:00',0),(449,348,'15:20:00','15:40:00',0),(450,348,'15:40:00','16:00:00',0),(451,348,'16:00:00','16:20:00',0),(452,348,'16:20:00','16:40:00',0),(453,348,'16:40:00','17:00:00',0),(454,349,'13:00:00','13:20:00',0),(455,349,'13:20:00','13:40:00',0),(456,349,'13:40:00','14:00:00',0),(457,349,'14:00:00','14:20:00',0),(458,349,'14:20:00','14:40:00',0),(459,349,'14:40:00','15:00:00',0),(460,349,'15:00:00','15:20:00',0),(461,349,'15:20:00','15:40:00',0),(462,349,'15:40:00','16:00:00',0),(463,349,'16:00:00','16:20:00',0),(464,349,'16:20:00','16:40:00',0),(465,349,'16:40:00','17:00:00',0),(475,351,'13:00:00','13:20:00',0),(476,351,'13:20:00','13:40:00',0),(477,351,'13:40:00','14:00:00',0),(478,351,'14:00:00','14:20:00',0),(479,351,'14:20:00','14:40:00',0),(480,351,'14:40:00','15:00:00',0),(481,351,'15:00:00','15:20:00',0),(482,351,'15:20:00','15:40:00',0),(483,351,'15:40:00','16:00:00',0),(484,351,'16:00:00','16:20:00',0),(485,351,'16:20:00','16:40:00',0),(486,351,'16:40:00','17:00:00',0),(487,352,'08:00:00','08:20:00',0),(488,352,'08:20:00','08:40:00',0),(489,352,'08:40:00','09:00:00',0),(490,352,'09:00:00','09:20:00',0),(491,352,'09:20:00','09:40:00',0),(492,352,'09:40:00','10:00:00',0),(493,352,'10:00:00','10:20:00',0),(494,352,'10:20:00','10:40:00',0),(495,352,'10:40:00','11:00:00',0),(496,352,'11:00:00','11:20:00',0),(497,352,'11:20:00','11:40:00',0),(498,352,'11:40:00','12:00:00',0),(499,353,'13:00:00','13:20:00',0),(500,353,'13:20:00','13:40:00',0),(501,353,'13:40:00','14:00:00',0),(502,353,'14:00:00','14:20:00',0),(503,353,'14:20:00','14:40:00',0),(504,353,'14:40:00','15:00:00',0),(505,353,'15:00:00','15:20:00',0),(506,353,'15:20:00','15:40:00',0),(507,353,'15:40:00','16:00:00',0),(508,353,'16:00:00','16:20:00',0),(509,353,'16:20:00','16:40:00',0),(510,353,'16:40:00','17:00:00',0),(511,354,'13:00:00','13:20:00',0),(512,354,'13:20:00','13:40:00',0),(513,354,'13:40:00','14:00:00',0),(514,354,'14:00:00','14:20:00',0),(515,354,'14:20:00','14:40:00',0),(516,354,'14:40:00','15:00:00',0),(517,354,'15:00:00','15:20:00',0),(518,354,'15:20:00','15:40:00',0),(519,354,'15:40:00','16:00:00',0),(520,354,'16:00:00','16:20:00',0),(521,354,'16:20:00','16:40:00',0),(522,354,'16:40:00','17:00:00',0),(523,355,'18:00:00','18:20:00',0),(524,355,'18:20:00','18:40:00',0),(525,355,'18:40:00','19:00:00',0),(526,355,'19:00:00','19:20:00',0),(527,355,'19:20:00','19:40:00',0),(528,355,'19:40:00','20:00:00',0),(529,355,'20:00:00','20:20:00',0),(530,355,'20:20:00','20:40:00',0),(531,355,'20:40:00','21:00:00',0),(532,356,'08:00:00','08:20:00',0),(533,356,'08:20:00','08:40:00',0),(534,356,'08:40:00','09:00:00',0),(535,356,'09:00:00','09:20:00',0),(536,356,'09:20:00','09:40:00',0),(537,356,'09:40:00','10:00:00',0),(538,356,'10:00:00','10:20:00',0),(539,356,'10:20:00','10:40:00',0),(540,356,'10:40:00','11:00:00',0),(541,356,'11:00:00','11:20:00',0),(542,356,'11:20:00','11:40:00',0),(543,356,'11:40:00','12:00:00',0),(544,357,'13:00:00','13:20:00',0),(545,357,'13:20:00','13:40:00',0),(546,357,'13:40:00','14:00:00',0),(547,357,'14:00:00','14:20:00',0),(548,357,'14:20:00','14:40:00',0),(549,357,'14:40:00','15:00:00',0),(550,357,'15:00:00','15:20:00',0),(551,357,'15:20:00','15:40:00',0),(552,357,'15:40:00','16:00:00',0),(553,357,'16:00:00','16:20:00',0),(554,357,'16:20:00','16:40:00',0),(555,357,'16:40:00','17:00:00',0),(556,358,'08:00:00','08:20:00',1),(557,358,'08:20:00','08:40:00',0),(558,358,'08:40:00','09:00:00',0),(559,358,'09:00:00','09:20:00',0),(560,358,'09:20:00','09:40:00',0),(561,358,'09:40:00','10:00:00',0),(562,358,'10:00:00','10:20:00',0),(563,358,'10:20:00','10:40:00',0),(564,358,'10:40:00','11:00:00',0),(565,358,'11:00:00','11:20:00',0),(566,358,'11:20:00','11:40:00',0),(567,358,'11:40:00','12:00:00',0),(568,359,'08:00:00','08:20:00',0),(569,359,'08:20:00','08:40:00',0),(570,359,'08:40:00','09:00:00',0),(571,359,'09:00:00','09:20:00',0),(572,359,'09:20:00','09:40:00',0),(573,359,'09:40:00','10:00:00',0),(574,359,'10:00:00','10:20:00',0),(575,359,'10:20:00','10:40:00',0),(576,359,'10:40:00','11:00:00',0),(577,359,'11:00:00','11:20:00',0),(578,359,'11:20:00','11:40:00',0),(579,359,'11:40:00','12:00:00',0),(580,360,'13:00:00','13:20:00',0),(581,360,'13:20:00','13:40:00',0),(582,360,'13:40:00','14:00:00',0),(583,360,'14:00:00','14:20:00',0),(584,360,'14:20:00','14:40:00',0),(585,360,'14:40:00','15:00:00',0),(586,360,'15:00:00','15:20:00',0),(587,360,'15:20:00','15:40:00',0),(588,360,'15:40:00','16:00:00',0),(589,360,'16:00:00','16:20:00',0),(590,360,'16:20:00','16:40:00',0),(591,360,'16:40:00','17:00:00',0),(592,361,'08:00:00','08:20:00',0),(593,361,'08:20:00','08:40:00',0),(594,361,'08:40:00','09:00:00',0),(595,361,'09:00:00','09:20:00',0),(596,361,'09:20:00','09:40:00',0),(597,361,'09:40:00','10:00:00',0),(598,361,'10:00:00','10:20:00',0),(599,361,'10:20:00','10:40:00',0),(600,361,'10:40:00','11:00:00',0),(601,361,'11:00:00','11:20:00',0),(602,361,'11:20:00','11:40:00',0),(603,361,'11:40:00','12:00:00',0),(604,362,'08:00:00','08:20:00',0),(605,362,'08:20:00','08:40:00',0),(606,362,'08:40:00','09:00:00',0),(607,362,'09:00:00','09:20:00',0),(608,362,'09:20:00','09:40:00',0),(609,362,'09:40:00','10:00:00',0),(610,362,'10:00:00','10:20:00',0),(611,362,'10:20:00','10:40:00',0),(612,362,'10:40:00','11:00:00',0),(613,362,'11:00:00','11:20:00',0),(614,362,'11:20:00','11:40:00',0),(615,362,'11:40:00','12:00:00',0),(616,363,'13:00:00','13:20:00',0),(617,363,'13:20:00','13:40:00',0),(618,363,'13:40:00','14:00:00',0),(619,363,'14:00:00','14:20:00',0),(620,363,'14:20:00','14:40:00',0),(621,363,'14:40:00','15:00:00',0),(622,363,'15:00:00','15:20:00',0),(623,363,'15:20:00','15:40:00',0),(624,363,'15:40:00','16:00:00',0),(625,363,'16:00:00','16:20:00',0),(626,363,'16:20:00','16:40:00',0),(627,363,'16:40:00','17:00:00',0),(628,364,'13:00:00','13:20:00',0),(629,364,'13:20:00','13:40:00',0),(630,364,'13:40:00','14:00:00',0),(631,364,'14:00:00','14:20:00',0),(632,364,'14:20:00','14:40:00',0),(633,364,'14:40:00','15:00:00',0),(634,364,'15:00:00','15:20:00',0),(635,364,'15:20:00','15:40:00',0),(636,364,'15:40:00','16:00:00',0),(637,364,'16:00:00','16:20:00',0),(638,364,'16:20:00','16:40:00',0),(639,364,'16:40:00','17:00:00',0),(640,365,'08:00:00','08:20:00',0),(641,365,'08:20:00','08:40:00',0),(642,365,'08:40:00','09:00:00',0),(643,365,'09:00:00','09:20:00',0),(644,365,'09:20:00','09:40:00',0),(645,365,'09:40:00','10:00:00',0),(646,365,'10:00:00','10:20:00',0),(647,365,'10:20:00','10:40:00',0),(648,365,'10:40:00','11:00:00',0),(649,365,'11:00:00','11:20:00',0),(650,365,'11:20:00','11:40:00',0),(651,365,'11:40:00','12:00:00',0),(652,366,'13:00:00','13:20:00',0),(653,366,'13:20:00','13:40:00',0),(654,366,'13:40:00','14:00:00',0),(655,366,'14:00:00','14:20:00',0),(656,366,'14:20:00','14:40:00',0),(657,366,'14:40:00','15:00:00',0),(658,366,'15:00:00','15:20:00',0),(659,366,'15:20:00','15:40:00',0),(660,366,'15:40:00','16:00:00',0),(661,366,'16:00:00','16:20:00',0),(662,366,'16:20:00','16:40:00',0),(663,366,'16:40:00','17:00:00',0),(664,367,'13:00:00','13:20:00',0),(665,367,'13:20:00','13:40:00',0),(666,367,'13:40:00','14:00:00',0),(667,367,'14:00:00','14:20:00',0),(668,367,'14:20:00','14:40:00',0),(669,367,'14:40:00','15:00:00',0),(670,367,'15:00:00','15:20:00',0),(671,367,'15:20:00','15:40:00',0),(672,367,'15:40:00','16:00:00',0),(673,367,'16:00:00','16:20:00',0),(674,367,'16:20:00','16:40:00',0),(675,367,'16:40:00','17:00:00',0),(676,368,'18:00:00','18:20:00',0),(677,368,'18:20:00','18:40:00',0),(678,368,'18:40:00','19:00:00',0),(679,368,'19:00:00','19:20:00',0),(680,368,'19:20:00','19:40:00',0),(681,368,'19:40:00','20:00:00',0),(682,368,'20:00:00','20:20:00',0),(683,368,'20:20:00','20:40:00',0),(684,368,'20:40:00','21:00:00',0),(685,369,'08:00:00','08:20:00',0),(686,369,'08:20:00','08:40:00',0),(687,369,'08:40:00','09:00:00',0),(688,369,'09:00:00','09:20:00',0),(689,369,'09:20:00','09:40:00',0),(690,369,'09:40:00','10:00:00',0),(691,369,'10:00:00','10:20:00',0),(692,369,'10:20:00','10:40:00',0),(693,369,'10:40:00','11:00:00',0),(694,369,'11:00:00','11:20:00',0),(695,369,'11:20:00','11:40:00',0),(696,369,'11:40:00','12:00:00',0),(697,370,'13:00:00','13:20:00',0),(698,370,'13:20:00','13:40:00',0),(699,370,'13:40:00','14:00:00',0),(700,370,'14:00:00','14:20:00',0),(701,370,'14:20:00','14:40:00',0),(702,370,'14:40:00','15:00:00',0),(703,370,'15:00:00','15:20:00',0),(704,370,'15:20:00','15:40:00',0),(705,370,'15:40:00','16:00:00',0),(706,370,'16:00:00','16:20:00',0),(707,370,'16:20:00','16:40:00',0),(708,370,'16:40:00','17:00:00',0),(709,371,'13:00:00','13:20:00',0),(710,371,'13:20:00','13:40:00',0),(711,371,'13:40:00','14:00:00',0),(712,371,'14:00:00','14:20:00',0),(713,371,'14:20:00','14:40:00',0),(714,371,'14:40:00','15:00:00',0),(715,371,'15:00:00','15:20:00',0),(716,371,'15:20:00','15:40:00',0),(717,371,'15:40:00','16:00:00',0),(718,371,'16:00:00','16:20:00',0),(719,371,'16:20:00','16:40:00',0),(720,371,'16:40:00','17:00:00',0),(721,372,'08:00:00','08:20:00',0),(722,372,'08:20:00','08:40:00',0),(723,372,'08:40:00','09:00:00',0),(724,372,'09:00:00','09:20:00',0),(725,372,'09:20:00','09:40:00',0),(726,372,'09:40:00','10:00:00',0),(727,372,'10:00:00','10:20:00',0),(728,372,'10:20:00','10:40:00',0),(729,372,'10:40:00','11:00:00',0),(730,372,'11:00:00','11:20:00',0),(731,372,'11:20:00','11:40:00',0),(732,372,'11:40:00','12:00:00',0),(733,373,'13:00:00','13:20:00',0),(734,373,'13:20:00','13:40:00',0),(735,373,'13:40:00','14:00:00',0),(736,373,'14:00:00','14:20:00',0),(737,373,'14:20:00','14:40:00',0),(738,373,'14:40:00','15:00:00',0),(739,373,'15:00:00','15:20:00',0),(740,373,'15:20:00','15:40:00',0),(741,373,'15:40:00','16:00:00',0),(742,373,'16:00:00','16:20:00',0),(743,373,'16:20:00','16:40:00',0),(744,373,'16:40:00','17:00:00',0),(745,374,'13:00:00','13:20:00',0),(746,374,'13:20:00','13:40:00',0),(747,374,'13:40:00','14:00:00',0),(748,374,'14:00:00','14:20:00',0),(749,374,'14:20:00','14:40:00',0),(750,374,'14:40:00','15:00:00',0),(751,374,'15:00:00','15:20:00',0),(752,374,'15:20:00','15:40:00',0),(753,374,'15:40:00','16:00:00',0),(754,374,'16:00:00','16:20:00',0),(755,374,'16:20:00','16:40:00',0),(756,374,'16:40:00','17:00:00',0),(757,375,'18:00:00','18:20:00',0),(758,375,'18:20:00','18:40:00',0),(759,375,'18:40:00','19:00:00',0),(760,375,'19:00:00','19:20:00',0),(761,375,'19:20:00','19:40:00',0),(762,375,'19:40:00','20:00:00',0),(763,375,'20:00:00','20:20:00',0),(764,375,'20:20:00','20:40:00',0),(765,375,'20:40:00','21:00:00',0),(766,376,'08:00:00','08:20:00',0),(767,376,'08:20:00','08:40:00',0),(768,376,'08:40:00','09:00:00',0),(769,376,'09:00:00','09:20:00',0),(770,376,'09:20:00','09:40:00',0),(771,376,'09:40:00','10:00:00',0),(772,376,'10:00:00','10:20:00',0),(773,376,'10:20:00','10:40:00',0),(774,376,'10:40:00','11:00:00',0),(775,376,'11:00:00','11:20:00',0),(776,376,'11:20:00','11:40:00',0),(777,376,'11:40:00','12:00:00',0),(778,377,'08:00:00','08:20:00',0),(779,377,'08:20:00','08:40:00',0),(780,377,'08:40:00','09:00:00',0),(781,377,'09:00:00','09:20:00',0),(782,377,'09:20:00','09:40:00',0),(783,377,'09:40:00','10:00:00',0),(784,377,'10:00:00','10:20:00',0),(785,377,'10:20:00','10:40:00',0),(786,377,'10:40:00','11:00:00',0),(787,377,'11:00:00','11:20:00',0),(788,377,'11:20:00','11:40:00',0),(789,377,'11:40:00','12:00:00',0),(790,378,'08:00:00','08:20:00',0),(791,378,'08:20:00','08:40:00',0),(792,378,'08:40:00','09:00:00',0),(793,378,'09:00:00','09:20:00',0),(794,378,'09:20:00','09:40:00',0),(795,378,'09:40:00','10:00:00',0),(796,378,'10:00:00','10:20:00',0),(797,378,'10:20:00','10:40:00',0),(798,378,'10:40:00','11:00:00',0),(799,378,'11:00:00','11:20:00',0),(800,378,'11:20:00','11:40:00',0),(801,378,'11:40:00','12:00:00',0),(802,379,'13:00:00','13:20:00',0),(803,379,'13:20:00','13:40:00',0),(804,379,'13:40:00','14:00:00',0),(805,379,'14:00:00','14:20:00',0),(806,379,'14:20:00','14:40:00',0),(807,379,'14:40:00','15:00:00',0),(808,379,'15:00:00','15:20:00',0),(809,379,'15:20:00','15:40:00',0),(810,379,'15:40:00','16:00:00',0),(811,379,'16:00:00','16:20:00',0),(812,379,'16:20:00','16:40:00',0),(813,379,'16:40:00','17:00:00',1),(814,380,'08:00:00','08:20:00',0),(815,380,'08:20:00','08:40:00',1),(816,380,'08:40:00','09:00:00',0),(817,380,'09:00:00','09:20:00',0),(818,380,'09:20:00','09:40:00',0),(819,380,'09:40:00','10:00:00',0),(820,380,'10:00:00','10:20:00',0),(821,380,'10:20:00','10:40:00',0),(822,380,'10:40:00','11:00:00',0),(823,380,'11:00:00','11:20:00',0),(824,380,'11:20:00','11:40:00',0),(825,380,'11:40:00','12:00:00',0),(826,381,'08:00:00','08:20:00',0),(827,381,'08:20:00','08:40:00',0),(828,381,'08:40:00','09:00:00',0),(829,381,'09:00:00','09:20:00',0),(830,381,'09:20:00','09:40:00',0),(831,381,'09:40:00','10:00:00',0),(832,381,'10:00:00','10:20:00',0),(833,381,'10:20:00','10:40:00',0),(834,381,'10:40:00','11:00:00',0),(835,381,'11:00:00','11:20:00',0),(836,381,'11:20:00','11:40:00',0),(837,381,'11:40:00','12:00:00',0),(838,382,'13:00:00','13:20:00',0),(839,382,'13:20:00','13:40:00',0),(840,382,'13:40:00','14:00:00',0),(841,382,'14:00:00','14:20:00',0),(842,382,'14:20:00','14:40:00',0),(843,382,'14:40:00','15:00:00',0),(844,382,'15:00:00','15:20:00',0),(845,382,'15:20:00','15:40:00',0),(846,382,'15:40:00','16:00:00',0),(847,382,'16:00:00','16:20:00',0),(848,382,'16:20:00','16:40:00',0),(849,382,'16:40:00','17:00:00',0),(850,383,'08:00:00','08:20:00',1),(851,383,'08:20:00','08:40:00',0),(852,383,'08:40:00','09:00:00',1),(853,383,'09:00:00','09:20:00',0),(854,383,'09:20:00','09:40:00',0),(855,383,'09:40:00','10:00:00',0),(856,383,'10:00:00','10:20:00',0),(857,383,'10:20:00','10:40:00',0),(858,383,'10:40:00','11:00:00',0),(859,383,'11:00:00','11:20:00',0),(860,383,'11:20:00','11:40:00',0),(861,383,'11:40:00','12:00:00',0),(862,384,'13:00:00','13:20:00',0),(863,384,'13:20:00','13:40:00',0),(864,384,'13:40:00','14:00:00',0),(865,384,'14:00:00','14:20:00',0),(866,384,'14:20:00','14:40:00',0),(867,384,'14:40:00','15:00:00',0),(868,384,'15:00:00','15:20:00',0),(869,384,'15:20:00','15:40:00',0),(870,384,'15:40:00','16:00:00',0),(871,384,'16:00:00','16:20:00',0),(872,384,'16:20:00','16:40:00',0),(873,384,'16:40:00','17:00:00',0),(874,385,'08:00:00','08:20:00',0),(875,385,'08:20:00','08:40:00',0),(876,385,'08:40:00','09:00:00',0),(877,385,'09:00:00','09:20:00',0),(878,385,'09:20:00','09:40:00',0),(879,385,'09:40:00','10:00:00',0),(880,385,'10:00:00','10:20:00',0),(881,385,'10:20:00','10:40:00',0),(882,385,'10:40:00','11:00:00',0),(883,385,'11:00:00','11:20:00',0),(884,385,'11:20:00','11:40:00',0),(885,385,'11:40:00','12:00:00',0),(886,386,'08:00:00','08:20:00',0),(887,386,'08:20:00','08:40:00',0),(888,386,'08:40:00','09:00:00',0),(889,386,'09:00:00','09:20:00',0),(890,386,'09:20:00','09:40:00',0),(891,386,'09:40:00','10:00:00',0),(892,386,'10:00:00','10:20:00',0),(893,386,'10:20:00','10:40:00',0),(894,386,'10:40:00','11:00:00',0),(895,386,'11:00:00','11:20:00',0),(896,386,'11:20:00','11:40:00',0),(897,386,'11:40:00','12:00:00',0),(898,387,'13:00:00','13:20:00',0),(899,387,'13:20:00','13:40:00',0),(900,387,'13:40:00','14:00:00',0),(901,387,'14:00:00','14:20:00',0),(902,387,'14:20:00','14:40:00',0),(903,387,'14:40:00','15:00:00',0),(904,387,'15:00:00','15:20:00',0),(905,387,'15:20:00','15:40:00',0),(906,387,'15:40:00','16:00:00',0),(907,387,'16:00:00','16:20:00',0),(908,387,'16:20:00','16:40:00',0),(909,387,'16:40:00','17:00:00',0),(910,388,'08:00:00','08:20:00',0),(911,388,'08:20:00','08:40:00',0),(912,388,'08:40:00','09:00:00',0),(913,388,'09:00:00','09:20:00',0),(914,388,'09:20:00','09:40:00',0),(915,388,'09:40:00','10:00:00',0),(916,388,'10:00:00','10:20:00',0),(917,388,'10:20:00','10:40:00',0),(918,388,'10:40:00','11:00:00',0),(919,388,'11:00:00','11:20:00',0),(920,388,'11:20:00','11:40:00',0),(921,388,'11:40:00','12:00:00',0),(922,389,'13:00:00','13:20:00',0),(923,389,'13:20:00','13:40:00',0),(924,389,'13:40:00','14:00:00',0),(925,389,'14:00:00','14:20:00',0),(926,389,'14:20:00','14:40:00',0),(927,389,'14:40:00','15:00:00',0),(928,389,'15:00:00','15:20:00',0),(929,389,'15:20:00','15:40:00',0),(930,389,'15:40:00','16:00:00',0),(931,389,'16:00:00','16:20:00',0),(932,389,'16:20:00','16:40:00',0),(933,389,'16:40:00','17:00:00',0),(934,390,'13:00:00','13:20:00',0),(935,390,'13:20:00','13:40:00',0),(936,390,'13:40:00','14:00:00',0),(937,390,'14:00:00','14:20:00',0),(938,390,'14:20:00','14:40:00',0),(939,390,'14:40:00','15:00:00',0),(940,390,'15:00:00','15:20:00',0),(941,390,'15:20:00','15:40:00',0),(942,390,'15:40:00','16:00:00',0),(943,390,'16:00:00','16:20:00',0),(944,390,'16:20:00','16:40:00',0),(945,390,'16:40:00','17:00:00',0),(946,391,'08:00:00','08:20:00',0),(947,391,'08:20:00','08:40:00',0),(948,391,'08:40:00','09:00:00',0),(949,391,'09:00:00','09:20:00',0),(950,391,'09:20:00','09:40:00',0),(951,391,'09:40:00','10:00:00',0),(952,391,'10:00:00','10:20:00',0),(953,391,'10:20:00','10:40:00',0),(954,391,'10:40:00','11:00:00',0),(955,391,'11:00:00','11:20:00',0),(956,391,'11:20:00','11:40:00',0),(957,391,'11:40:00','12:00:00',0),(958,392,'13:00:00','13:20:00',0),(959,392,'13:20:00','13:40:00',0),(960,392,'13:40:00','14:00:00',0),(961,392,'14:00:00','14:20:00',0),(962,392,'14:20:00','14:40:00',0),(963,392,'14:40:00','15:00:00',0),(964,392,'15:00:00','15:20:00',0),(965,392,'15:20:00','15:40:00',0),(966,392,'15:40:00','16:00:00',0),(967,392,'16:00:00','16:20:00',0),(968,392,'16:20:00','16:40:00',0),(969,392,'16:40:00','17:00:00',0),(970,393,'08:00:00','08:20:00',0),(971,393,'08:20:00','08:40:00',0),(972,393,'08:40:00','09:00:00',0),(973,393,'09:00:00','09:20:00',0),(974,393,'09:20:00','09:40:00',0),(975,393,'09:40:00','10:00:00',0),(976,393,'10:00:00','10:20:00',0),(977,393,'10:20:00','10:40:00',0),(978,393,'10:40:00','11:00:00',0),(979,393,'11:00:00','11:20:00',0),(980,393,'11:20:00','11:40:00',0),(981,393,'11:40:00','12:00:00',0),(982,394,'13:00:00','13:20:00',0),(983,394,'13:20:00','13:40:00',0),(984,394,'13:40:00','14:00:00',0),(985,394,'14:00:00','14:20:00',0),(986,394,'14:20:00','14:40:00',0),(987,394,'14:40:00','15:00:00',0),(988,394,'15:00:00','15:20:00',0),(989,394,'15:20:00','15:40:00',0),(990,394,'15:40:00','16:00:00',0),(991,394,'16:00:00','16:20:00',0),(992,394,'16:20:00','16:40:00',0),(993,394,'16:40:00','17:00:00',0),(994,395,'08:00:00','08:20:00',0),(995,395,'08:20:00','08:40:00',0),(996,395,'08:40:00','09:00:00',0),(997,395,'09:00:00','09:20:00',0),(998,395,'09:20:00','09:40:00',0),(999,395,'09:40:00','10:00:00',0),(1000,395,'10:00:00','10:20:00',0),(1001,395,'10:20:00','10:40:00',0),(1002,395,'10:40:00','11:00:00',0),(1003,395,'11:00:00','11:20:00',0),(1004,395,'11:20:00','11:40:00',0),(1005,395,'11:40:00','12:00:00',0),(1006,396,'13:00:00','13:20:00',0),(1007,396,'13:20:00','13:40:00',0),(1008,396,'13:40:00','14:00:00',0),(1009,396,'14:00:00','14:20:00',0),(1010,396,'14:20:00','14:40:00',0),(1011,396,'14:40:00','15:00:00',0),(1012,396,'15:00:00','15:20:00',0),(1013,396,'15:20:00','15:40:00',0),(1014,396,'15:40:00','16:00:00',0),(1015,396,'16:00:00','16:20:00',0),(1016,396,'16:20:00','16:40:00',0),(1017,396,'16:40:00','17:00:00',0),(1018,397,'08:00:00','08:20:00',0),(1019,397,'08:20:00','08:40:00',0),(1020,397,'08:40:00','09:00:00',0),(1021,397,'09:00:00','09:20:00',0),(1022,397,'09:20:00','09:40:00',0),(1023,397,'09:40:00','10:00:00',0),(1024,397,'10:00:00','10:20:00',0),(1025,397,'10:20:00','10:40:00',0),(1026,397,'10:40:00','11:00:00',0),(1027,397,'11:00:00','11:20:00',0),(1028,397,'11:20:00','11:40:00',0),(1029,397,'11:40:00','12:00:00',0),(1030,398,'13:00:00','13:20:00',0),(1031,398,'13:20:00','13:40:00',0),(1032,398,'13:40:00','14:00:00',0),(1033,398,'14:00:00','14:20:00',0),(1034,398,'14:20:00','14:40:00',0),(1035,398,'14:40:00','15:00:00',0),(1036,398,'15:00:00','15:20:00',0),(1037,398,'15:20:00','15:40:00',0),(1038,398,'15:40:00','16:00:00',0),(1039,398,'16:00:00','16:20:00',0),(1040,398,'16:20:00','16:40:00',0),(1041,398,'16:40:00','17:00:00',0),(1042,399,'13:00:00','13:20:00',0),(1043,399,'13:20:00','13:40:00',0),(1044,399,'13:40:00','14:00:00',0),(1045,399,'14:00:00','14:20:00',0),(1046,399,'14:20:00','14:40:00',0),(1047,399,'14:40:00','15:00:00',0),(1048,399,'15:00:00','15:20:00',0),(1049,399,'15:20:00','15:40:00',0),(1050,399,'15:40:00','16:00:00',0),(1051,399,'16:00:00','16:20:00',0),(1052,399,'16:20:00','16:40:00',0),(1053,399,'16:40:00','17:00:00',0),(1054,400,'08:00:00','08:20:00',0),(1055,400,'08:20:00','08:40:00',0),(1056,400,'08:40:00','09:00:00',0),(1057,400,'09:00:00','09:20:00',0),(1058,400,'09:20:00','09:40:00',0),(1059,400,'09:40:00','10:00:00',0),(1060,400,'10:00:00','10:20:00',0),(1061,400,'10:20:00','10:40:00',0),(1062,400,'10:40:00','11:00:00',0),(1063,400,'11:00:00','11:20:00',0),(1064,400,'11:20:00','11:40:00',0),(1065,400,'11:40:00','12:00:00',0),(1066,401,'08:00:00','08:20:00',0),(1067,401,'08:20:00','08:40:00',0),(1068,401,'08:40:00','09:00:00',0),(1069,401,'09:00:00','09:20:00',0),(1070,401,'09:20:00','09:40:00',0),(1071,401,'09:40:00','10:00:00',0),(1072,401,'10:00:00','10:20:00',0),(1073,401,'10:20:00','10:40:00',0),(1074,401,'10:40:00','11:00:00',0),(1075,401,'11:00:00','11:20:00',0),(1076,401,'11:20:00','11:40:00',0),(1077,401,'11:40:00','12:00:00',0),(1078,402,'13:00:00','13:20:00',0),(1079,402,'13:20:00','13:40:00',0),(1080,402,'13:40:00','14:00:00',0),(1081,402,'14:00:00','14:20:00',0),(1082,402,'14:20:00','14:40:00',0),(1083,402,'14:40:00','15:00:00',0),(1084,402,'15:00:00','15:20:00',0),(1085,402,'15:20:00','15:40:00',0),(1086,402,'15:40:00','16:00:00',0),(1087,402,'16:00:00','16:20:00',0),(1088,402,'16:20:00','16:40:00',0),(1089,402,'16:40:00','17:00:00',0),(1090,403,'08:00:00','08:20:00',0),(1091,403,'08:20:00','08:40:00',0),(1092,403,'08:40:00','09:00:00',0),(1093,403,'09:00:00','09:20:00',0),(1094,403,'09:20:00','09:40:00',0),(1095,403,'09:40:00','10:00:00',0),(1096,403,'10:00:00','10:20:00',0),(1097,403,'10:20:00','10:40:00',0),(1098,403,'10:40:00','11:00:00',0),(1099,403,'11:00:00','11:20:00',0),(1100,403,'11:20:00','11:40:00',0),(1101,403,'11:40:00','12:00:00',0),(1102,404,'13:00:00','13:20:00',0),(1103,404,'13:20:00','13:40:00',0),(1104,404,'13:40:00','14:00:00',0),(1105,404,'14:00:00','14:20:00',0),(1106,404,'14:20:00','14:40:00',0),(1107,404,'14:40:00','15:00:00',0),(1108,404,'15:00:00','15:20:00',0),(1109,404,'15:20:00','15:40:00',0),(1110,404,'15:40:00','16:00:00',0),(1111,404,'16:00:00','16:20:00',0),(1112,404,'16:20:00','16:40:00',0),(1113,404,'16:40:00','17:00:00',0),(1114,405,'08:00:00','08:20:00',0),(1115,405,'08:20:00','08:40:00',0),(1116,405,'08:40:00','09:00:00',0),(1117,405,'09:00:00','09:20:00',0),(1118,405,'09:20:00','09:40:00',0),(1119,405,'09:40:00','10:00:00',0),(1120,405,'10:00:00','10:20:00',0),(1121,405,'10:20:00','10:40:00',0),(1122,405,'10:40:00','11:00:00',0),(1123,405,'11:00:00','11:20:00',0),(1124,405,'11:20:00','11:40:00',0),(1125,405,'11:40:00','12:00:00',0),(1126,406,'13:00:00','13:20:00',0),(1127,406,'13:20:00','13:40:00',0),(1128,406,'13:40:00','14:00:00',0),(1129,406,'14:00:00','14:20:00',0),(1130,406,'14:20:00','14:40:00',0),(1131,406,'14:40:00','15:00:00',0),(1132,406,'15:00:00','15:20:00',0),(1133,406,'15:20:00','15:40:00',0),(1134,406,'15:40:00','16:00:00',0),(1135,406,'16:00:00','16:20:00',0),(1136,406,'16:20:00','16:40:00',0),(1137,406,'16:40:00','17:00:00',0),(1138,407,'08:00:00','08:20:00',0),(1139,407,'08:20:00','08:40:00',0),(1140,407,'08:40:00','09:00:00',0),(1141,407,'09:00:00','09:20:00',0),(1142,407,'09:20:00','09:40:00',0),(1143,407,'09:40:00','10:00:00',0),(1144,407,'10:00:00','10:20:00',0),(1145,407,'10:20:00','10:40:00',0),(1146,407,'10:40:00','11:00:00',0),(1147,407,'11:00:00','11:20:00',0),(1148,407,'11:20:00','11:40:00',0),(1149,407,'11:40:00','12:00:00',0),(1150,408,'08:00:00','08:20:00',0),(1151,408,'08:20:00','08:40:00',0),(1152,408,'08:40:00','09:00:00',0),(1153,408,'09:00:00','09:20:00',0),(1154,408,'09:20:00','09:40:00',0),(1155,408,'09:40:00','10:00:00',0),(1156,408,'10:00:00','10:20:00',0),(1157,408,'10:20:00','10:40:00',0),(1158,408,'10:40:00','11:00:00',0),(1159,408,'11:00:00','11:20:00',0),(1160,408,'11:20:00','11:40:00',0),(1161,408,'11:40:00','12:00:00',0),(1162,409,'13:00:00','13:20:00',0),(1163,409,'13:20:00','13:40:00',0),(1164,409,'13:40:00','14:00:00',0),(1165,409,'14:00:00','14:20:00',0),(1166,409,'14:20:00','14:40:00',0),(1167,409,'14:40:00','15:00:00',0),(1168,409,'15:00:00','15:20:00',0),(1169,409,'15:20:00','15:40:00',0),(1170,409,'15:40:00','16:00:00',0),(1171,409,'16:00:00','16:20:00',0),(1172,409,'16:20:00','16:40:00',0),(1173,409,'16:40:00','17:00:00',0),(1174,410,'08:00:00','08:20:00',0),(1175,410,'08:20:00','08:40:00',0),(1176,410,'08:40:00','09:00:00',0),(1177,410,'09:00:00','09:20:00',0),(1178,410,'09:20:00','09:40:00',0),(1179,410,'09:40:00','10:00:00',0),(1180,410,'10:00:00','10:20:00',0),(1181,410,'10:20:00','10:40:00',0),(1182,410,'10:40:00','11:00:00',0),(1183,410,'11:00:00','11:20:00',0),(1184,410,'11:20:00','11:40:00',0),(1185,410,'11:40:00','12:00:00',0),(1186,411,'08:00:00','08:20:00',0),(1187,411,'08:20:00','08:40:00',0),(1188,411,'08:40:00','09:00:00',0),(1189,411,'09:00:00','09:20:00',0),(1190,411,'09:20:00','09:40:00',0),(1191,411,'09:40:00','10:00:00',0),(1192,411,'10:00:00','10:20:00',0),(1193,411,'10:20:00','10:40:00',0),(1194,411,'10:40:00','11:00:00',0),(1195,411,'11:00:00','11:20:00',0),(1196,411,'11:20:00','11:40:00',0),(1197,411,'11:40:00','12:00:00',0),(1198,412,'13:00:00','13:20:00',0),(1199,412,'13:20:00','13:40:00',0),(1200,412,'13:40:00','14:00:00',0),(1201,412,'14:00:00','14:20:00',0),(1202,412,'14:20:00','14:40:00',0),(1203,412,'14:40:00','15:00:00',0),(1204,412,'15:00:00','15:20:00',0),(1205,412,'15:20:00','15:40:00',0),(1206,412,'15:40:00','16:00:00',0),(1207,412,'16:00:00','16:20:00',0),(1208,412,'16:20:00','16:40:00',0),(1209,412,'16:40:00','17:00:00',0),(1210,413,'13:00:00','13:20:00',0),(1211,413,'13:20:00','13:40:00',0),(1212,413,'13:40:00','14:00:00',0),(1213,413,'14:00:00','14:20:00',0),(1214,413,'14:20:00','14:40:00',0),(1215,413,'14:40:00','15:00:00',0),(1216,413,'15:00:00','15:20:00',0),(1217,413,'15:20:00','15:40:00',0),(1218,413,'15:40:00','16:00:00',0),(1219,413,'16:00:00','16:20:00',0),(1220,413,'16:20:00','16:40:00',0),(1221,413,'16:40:00','17:00:00',0),(1222,414,'08:00:00','08:20:00',0),(1223,414,'08:20:00','08:40:00',0),(1224,414,'08:40:00','09:00:00',0),(1225,414,'09:00:00','09:20:00',0),(1226,414,'09:20:00','09:40:00',0),(1227,414,'09:40:00','10:00:00',0),(1228,414,'10:00:00','10:20:00',0),(1229,414,'10:20:00','10:40:00',0),(1230,414,'10:40:00','11:00:00',0),(1231,414,'11:00:00','11:20:00',0),(1232,414,'11:20:00','11:40:00',0),(1233,414,'11:40:00','12:00:00',0),(1234,415,'13:00:00','13:20:00',0),(1235,415,'13:20:00','13:40:00',0),(1236,415,'13:40:00','14:00:00',0),(1237,415,'14:00:00','14:20:00',0),(1238,415,'14:20:00','14:40:00',0),(1239,415,'14:40:00','15:00:00',0),(1240,415,'15:00:00','15:20:00',0),(1241,415,'15:20:00','15:40:00',0),(1242,415,'15:40:00','16:00:00',0),(1243,415,'16:00:00','16:20:00',0),(1244,415,'16:20:00','16:40:00',0),(1245,415,'16:40:00','17:00:00',0),(1246,416,'08:00:00','08:20:00',0),(1247,416,'08:20:00','08:40:00',0),(1248,416,'08:40:00','09:00:00',0),(1249,416,'09:00:00','09:20:00',0),(1250,416,'09:20:00','09:40:00',0),(1251,416,'09:40:00','10:00:00',0),(1252,416,'10:00:00','10:20:00',0),(1253,416,'10:20:00','10:40:00',0),(1254,416,'10:40:00','11:00:00',0),(1255,416,'11:00:00','11:20:00',0),(1256,416,'11:20:00','11:40:00',0),(1257,416,'11:40:00','12:00:00',0),(1258,417,'13:00:00','13:20:00',0),(1259,417,'13:20:00','13:40:00',0),(1260,417,'13:40:00','14:00:00',0),(1261,417,'14:00:00','14:20:00',0),(1262,417,'14:20:00','14:40:00',0),(1263,417,'14:40:00','15:00:00',0),(1264,417,'15:00:00','15:20:00',0),(1265,417,'15:20:00','15:40:00',0),(1266,417,'15:40:00','16:00:00',0),(1267,417,'16:00:00','16:20:00',0),(1268,417,'16:20:00','16:40:00',0),(1269,417,'16:40:00','17:00:00',0),(1270,418,'08:00:00','08:20:00',0),(1271,418,'08:20:00','08:40:00',0),(1272,418,'08:40:00','09:00:00',0),(1273,418,'09:00:00','09:20:00',0),(1274,418,'09:20:00','09:40:00',0),(1275,418,'09:40:00','10:00:00',0),(1276,418,'10:00:00','10:20:00',0),(1277,418,'10:20:00','10:40:00',0),(1278,418,'10:40:00','11:00:00',0),(1279,418,'11:00:00','11:20:00',0),(1280,418,'11:20:00','11:40:00',0),(1281,418,'11:40:00','12:00:00',0),(1282,419,'13:00:00','13:20:00',0),(1283,419,'13:20:00','13:40:00',0),(1284,419,'13:40:00','14:00:00',0),(1285,419,'14:00:00','14:20:00',0),(1286,419,'14:20:00','14:40:00',0),(1287,419,'14:40:00','15:00:00',0),(1288,419,'15:00:00','15:20:00',0),(1289,419,'15:20:00','15:40:00',0),(1290,419,'15:40:00','16:00:00',0),(1291,419,'16:00:00','16:20:00',0),(1292,419,'16:20:00','16:40:00',0),(1293,419,'16:40:00','17:00:00',0),(1294,420,'08:00:00','08:20:00',0),(1295,420,'08:20:00','08:40:00',0),(1296,420,'08:40:00','09:00:00',0),(1297,420,'09:00:00','09:20:00',0),(1298,420,'09:20:00','09:40:00',0),(1299,420,'09:40:00','10:00:00',0),(1300,420,'10:00:00','10:20:00',0),(1301,420,'10:20:00','10:40:00',0),(1302,420,'10:40:00','11:00:00',0),(1303,420,'11:00:00','11:20:00',0),(1304,420,'11:20:00','11:40:00',0),(1305,420,'11:40:00','12:00:00',0),(1306,421,'13:00:00','13:20:00',0),(1307,421,'13:20:00','13:40:00',0),(1308,421,'13:40:00','14:00:00',0),(1309,421,'14:00:00','14:20:00',0),(1310,421,'14:20:00','14:40:00',0),(1311,421,'14:40:00','15:00:00',0),(1312,421,'15:00:00','15:20:00',0),(1313,421,'15:20:00','15:40:00',0),(1314,421,'15:40:00','16:00:00',0),(1315,421,'16:00:00','16:20:00',0),(1316,421,'16:20:00','16:40:00',0),(1317,421,'16:40:00','17:00:00',0),(1318,422,'08:00:00','08:20:00',0),(1319,422,'08:20:00','08:40:00',0),(1320,422,'08:40:00','09:00:00',0),(1321,422,'09:00:00','09:20:00',0),(1322,422,'09:20:00','09:40:00',0),(1323,422,'09:40:00','10:00:00',0),(1324,422,'10:00:00','10:20:00',0),(1325,422,'10:20:00','10:40:00',0),(1326,422,'10:40:00','11:00:00',0),(1327,422,'11:00:00','11:20:00',0),(1328,422,'11:20:00','11:40:00',0),(1329,422,'11:40:00','12:00:00',0),(1330,423,'13:00:00','13:20:00',0),(1331,423,'13:20:00','13:40:00',0),(1332,423,'13:40:00','14:00:00',0),(1333,423,'14:00:00','14:20:00',0),(1334,423,'14:20:00','14:40:00',0),(1335,423,'14:40:00','15:00:00',0),(1336,423,'15:00:00','15:20:00',0),(1337,423,'15:20:00','15:40:00',0),(1338,423,'15:40:00','16:00:00',0),(1339,423,'16:00:00','16:20:00',0),(1340,423,'16:20:00','16:40:00',0),(1341,423,'16:40:00','17:00:00',0),(1342,424,'08:00:00','08:20:00',0),(1343,424,'08:20:00','08:40:00',0),(1344,424,'08:40:00','09:00:00',0),(1345,424,'09:00:00','09:20:00',0),(1346,424,'09:20:00','09:40:00',0),(1347,424,'09:40:00','10:00:00',0),(1348,424,'10:00:00','10:20:00',0),(1349,424,'10:20:00','10:40:00',0),(1350,424,'10:40:00','11:00:00',0),(1351,424,'11:00:00','11:20:00',0),(1352,424,'11:20:00','11:40:00',0),(1353,424,'11:40:00','12:00:00',0),(1354,425,'08:00:00','08:20:00',0),(1355,425,'08:20:00','08:40:00',0),(1356,425,'08:40:00','09:00:00',0),(1357,425,'09:00:00','09:20:00',0),(1358,425,'09:20:00','09:40:00',0),(1359,425,'09:40:00','10:00:00',0),(1360,425,'10:00:00','10:20:00',0),(1361,425,'10:20:00','10:40:00',0),(1362,425,'10:40:00','11:00:00',0),(1363,425,'11:00:00','11:20:00',0),(1364,425,'11:20:00','11:40:00',0),(1365,425,'11:40:00','12:00:00',0),(1366,426,'13:00:00','13:20:00',0),(1367,426,'13:20:00','13:40:00',0),(1368,426,'13:40:00','14:00:00',0),(1369,426,'14:00:00','14:20:00',0),(1370,426,'14:20:00','14:40:00',0),(1371,426,'14:40:00','15:00:00',0),(1372,426,'15:00:00','15:20:00',0),(1373,426,'15:20:00','15:40:00',0),(1374,426,'15:40:00','16:00:00',0),(1375,426,'16:00:00','16:20:00',0),(1376,426,'16:20:00','16:40:00',0),(1377,426,'16:40:00','17:00:00',0),(1378,427,'08:00:00','08:20:00',0),(1379,427,'08:20:00','08:40:00',0),(1380,427,'08:40:00','09:00:00',0),(1381,427,'09:00:00','09:20:00',0),(1382,427,'09:20:00','09:40:00',0),(1383,427,'09:40:00','10:00:00',0),(1384,427,'10:00:00','10:20:00',0),(1385,427,'10:20:00','10:40:00',0),(1386,427,'10:40:00','11:00:00',0),(1387,427,'11:00:00','11:20:00',0),(1388,427,'11:20:00','11:40:00',0),(1389,427,'11:40:00','12:00:00',0),(1390,428,'13:00:00','13:20:00',0),(1391,428,'13:20:00','13:40:00',0),(1392,428,'13:40:00','14:00:00',0),(1393,428,'14:00:00','14:20:00',0),(1394,428,'14:20:00','14:40:00',0),(1395,428,'14:40:00','15:00:00',0),(1396,428,'15:00:00','15:20:00',0),(1397,428,'15:20:00','15:40:00',0),(1398,428,'15:40:00','16:00:00',0),(1399,428,'16:00:00','16:20:00',0),(1400,428,'16:20:00','16:40:00',0),(1401,428,'16:40:00','17:00:00',0),(1402,429,'13:00:00','13:20:00',0),(1403,429,'13:20:00','13:40:00',0),(1404,429,'13:40:00','14:00:00',0),(1405,429,'14:00:00','14:20:00',0),(1406,429,'14:20:00','14:40:00',0),(1407,429,'14:40:00','15:00:00',0),(1408,429,'15:00:00','15:20:00',0),(1409,429,'15:20:00','15:40:00',0),(1410,429,'15:40:00','16:00:00',0),(1411,429,'16:00:00','16:20:00',0),(1412,429,'16:20:00','16:40:00',0),(1413,429,'16:40:00','17:00:00',0),(1414,430,'18:00:00','18:20:00',0),(1415,430,'18:20:00','18:40:00',0),(1416,430,'18:40:00','19:00:00',0),(1417,430,'19:00:00','19:20:00',0),(1418,430,'19:20:00','19:40:00',0),(1419,430,'19:40:00','20:00:00',0),(1420,430,'20:00:00','20:20:00',0),(1421,430,'20:20:00','20:40:00',0),(1422,430,'20:40:00','21:00:00',0);
/*!40000 ALTER TABLE `schedule_slots` ENABLE KEYS */;
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
INSERT INTO `shift_type` VALUES (1,'Sáng','08:00:00','12:00:00'),(2,'Chiều','13:00:00','17:00:00'),(3,'Tối','18:00:00','21:00:00');
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialty`
--

LOCK TABLES `specialty` WRITE;
/*!40000 ALTER TABLE `specialty` DISABLE KEYS */;
INSERT INTO `specialty` VALUES (1,1,'Tim mạch','Điều trị các bệnh về tim','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//tim_mach.jpg'),(2,1,'Hô hấp','Chuyên điều trị các bệnh liên quan đến phổi','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//ho_hap.jpg'),(3,2,'Ngoại tổng quát','Phẫu thuật cơ bản','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//Ngoai_tong_quat.jpg'),(4,3,'Nhi hô hấp','Chăm sóc bệnh hô hấp cho trẻ','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//tre_em.png'),(6,2,'Chấn thương chỉnh hình','Điều trị các chấn thương xương khớp','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//chan_thuong_chinh_hinh.jpg'),(7,5,'Thai sản','','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//thai_san.jpg'),(8,2,'Răng hàm mặt','','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//rang_ham_mat.jpg'),(9,2,'Da liễu','Chuyên điều trị các bệnh lý về da, tóc, móng','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//Da_lieu.jpg'),(10,1,'Nội tiết','Chuyên chẩn đoán và điều trị các bệnh liên quan đến hormone và các tuyến nội tiết như tuyến giáp, tuyến yên, tuyến thượng thận, tuyến tụy (tiểu đường), và rối loạn chuyển hóa.','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/noitiet.jpg'),(11,2,'Ung Bướu','Chuyên điều trị và chăm sóc bệnh nhân ung thư','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/ungbuou.png'),(12,1,'Huyết Học','Chuyên điều trị các bệnh về máu và cơ quan tạo máu','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/huyethoc.png'),(13,1,'Thần Kinh','Chuyên khám và điều trị các bệnh lý thần kinh','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/thankinh.png'),(14,1,'Tâm Thần','Chuyên điều trị các rối loạn tâm thần và hành vi','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/tamthan.png'),(15,1,'Tiêu Hóa','Chuyên điều trị các bệnh lý hệ tiêu hóa như dạ dày, gan, ruột','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/tieuhoa.png'),(16,1,'Thận - Tiết Niệu','Chuyên điều trị các bệnh lý thận và đường tiết niệu','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/than_tietnieu.png'),(18,3,'Nhiễm Nhi','Chuyên điều trị các bệnh truyền nhiễm ở trẻ em','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/nhiemnhi.png');
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (1,1,4,1),(2,1,5,1),(3,1,7,5),(4,1,8,2),(5,5,11,12),(6,1,14,1),(7,2,18,2),(8,3,19,1),(10,4,21,9),(11,7,22,10),(12,3,23,8),(13,3,24,8),(14,3,25,8),(15,3,26,8),(17,1,28,1),(18,6,29,11),(19,1,30,1),(20,1,31,1),(21,1,32,1),(22,1,33,1),(23,1,34,1),(24,1,35,1),(25,1,36,2),(26,1,37,1);
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
INSERT INTO `staff_position` VALUES (6,'Cashier'),(1,'Doctor'),(3,'Lab Technician'),(4,'Medical Imaging Technician'),(2,'Nurse '),(7,'Patient Receptionist'),(5,'Pharmacist');
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
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `staff_id` (`staff_id`),
  KEY `shift_type_id` (`shift_type_id`),
  KEY `room_id` (`room_id`),
  CONSTRAINT `staff_schedules_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`id`),
  CONSTRAINT `staff_schedules_ibfk_2` FOREIGN KEY (`shift_type_id`) REFERENCES `shift_type` (`id`),
  CONSTRAINT `staff_schedules_ibfk_3` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff_schedules`
--

LOCK TABLES `staff_schedules` WRITE;
/*!40000 ALTER TABLE `staff_schedules` DISABLE KEYS */;
INSERT INTO `staff_schedules` VALUES (1,8,1,5,'2025-10-26','ACTIVE'),(2,14,1,5,'2025-10-26','ACTIVE'),(3,15,1,5,'2025-10-26','ACTIVE'),(4,15,1,5,'2025-11-07','ACTIVE'),(5,14,1,5,'2025-11-10',NULL),(6,14,2,5,'2025-11-10',NULL),(7,14,1,5,'2025-11-11',NULL),(8,14,1,5,'2025-11-12',NULL),(9,14,2,5,'2025-11-11',NULL),(10,14,2,5,'2025-11-12',NULL),(11,14,1,5,'2025-11-28',NULL);
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
  `test_name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `department_id` int DEFAULT NULL,
  `test_code` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_tt_d` (`department_id`),
  CONSTRAINT `FK_tt_d` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `testtypes`
--

LOCK TABLES `testtypes` WRITE;
/*!40000 ALTER TABLE `testtypes` DISABLE KEYS */;
INSERT INTO `testtypes` VALUES (1,'Xét nghiệm Huyết học (Máu tổng quát)',200000,'Kiểm tra chỉ số huyết học',1,'TEST1',1),(2,'Xét nghiệm sinh hóa',200000,'Đánh giá chức năng gan, thận',1,'TEST2',1),(3,'Xét nghiệm Đông cầm máu',180000,'Phục vụ chuẩn bị phẫu thuật ngoại khoa',2,'TEST3',1),(4,'Xét nghiệm Hóa sinh/Tế bào nước tiểu',120000,'Đánh giá chức năng thận và tiết niệu',3,'TEST4',1),(5,'Xét nghiệm nội tiết',250000,'Theo dõi sức khỏe thai kỳ và nội tiết tố',5,'TEST5',1),(6,'Xét nghiệm Vi sinh',350000,'Phân lập, xác định vi khuẩn, nấm; làm kháng sinh đồ.',1,'TEST6',1),(7,'Xét nghiệm Miễn dịch',400000,'Phát hiện kháng thể, kháng nguyên (Viêm gan B/C, HIV, Sốt xuất huyết).',1,'TEST7',1),(8,'Xét nghiệm Ký sinh trùng',200000,'Tìm kiếm trứng, ấu trùng giun sán trong máu/phân/nước tiểu.',1,'TEST8',1),(9,'Xét nghiệm Di truyền/Phân tử',1500000,'Phân tích DNA/RNA (PCR, Genotype), thường phục vụ cho Ung bướu, Truyền nhiễm.',1,'TEST9',1),(10,'Xét nghiệm Giải phẫu bệnh',800000,'Sinh thiết, tế bào học (pap smear, chọc hút kim nhỏ) để chẩn đoán ung thư.',1,'TEST10',1);
/*!40000 ALTER TABLE `testtypes` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_testtypes` BEFORE INSERT ON `testtypes` FOR EACH ROW BEGIN
    DECLARE max_id INT DEFAULT 0;
    DECLARE new_code VARCHAR(20);

    -- Lấy id lớn nhất hiện có
    SELECT IFNULL(MAX(id), 0) INTO max_id FROM testtypes;

    -- Sinh mã mới
    SET new_code = CONCAT('TEST', max_id + 1);

    -- Gán vào cột imagingCode
    SET NEW.test_code = new_code;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Temporary view structure for view `unified_service_orders`
--

DROP TABLE IF EXISTS `unified_service_orders`;
/*!50001 DROP VIEW IF EXISTS `unified_service_orders`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `unified_service_orders` AS SELECT 
 1 AS `order_id`,
 1 AS `service_type`,
 1 AS `service_name`,
 1 AS `requested_date`,
 1 AS `result_date`,
 1 AS `result`,
 1 AS `status`,
 1 AS `doctor_id`,
 1 AS `record_id`,
 1 AS `staff_name`,
 1 AS `staff_code`*/;
SET character_set_client = @saved_cs_client;

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
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Huỳnh Minh Hoàng','hoanghm4869@gmail.com','0337023824','2003-11-12',0,'TBH TDM','$2a$10$b6E2VmRKJ8pB25uky/vWN.1XnlYvFSOEwK3jtxPchw5nqbLmqQoPC',0,'2025-04-10 00:00:00',NULL),(3,'Nguyễn Văn A','nguyenvanA@example.com','0123456789','1995-05-20',0,'123 Đường Lê Lợi, Tdm, TP HCM','$2a$10$XDLLf84jVbgGduWVA4N/.e2/ZqEVVhZe5d0ljbaxetYsLS.Az55I6',1,'2025-04-10 04:48:35',NULL),(4,'Trần Văn C','tranvanc@hospital.com','0323456709','1985-06-20',0,'123 Lê Lợi, TP.HCM','$2a$10$XiutYothZ4rma/P.J5c2leYOLb/hTj6V677Re5gy5hSWeB1yBe71y',2,'2025-04-10 08:28:32','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1747710399077.png'),(5,'Nguyễn Minh Thuận','Thuannm@example.com','0223456789','1985-06-20',0,'123 Nguyễn Trãi, TP.HCM','$2a$10$o/BAwrm8rlbRUtVKepG8iuhlFxvVMfn6LVhLyaaiLtmdvUAIkGLbC',2,'2025-04-16 08:06:58','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//bs_nam.png'),(7,'Nguyễn Hồng Yến','Yennh@example.com','0423456789','1995-06-20',1,'123 Lý Thái Tổ, TP.HCM','$2a$10$ZRb1sOPqiTR1NgDn7l9ryuU7JbBOT6QnX2.ai.skFTmg/VMVFNu5u',2,'2025-05-18 06:40:23','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//bs_nu.jpg'),(8,'Trần Thu Phương','Phuongtt20@example.com','0523456789','1994-08-20',1,'123 Phan Châu Trinh, TP.HCM','$2a$10$qFTu3C44lJEAz4.AEo/qIOnwihykzMMB5mYTqv0nKcnFePOhvBtxK',2,'2025-05-18 07:36:34','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//bs_nu.jpg'),(9,'Nguyễn Việt Trung','Truntnv@gmail.com','0336013824','2003-06-25',0,'Phú Hòa','$2a$10$kvsD41NR8tMs6ZFfth6wI.ksdSKpySwLVYW2Y/CT/0KoryGjrzxCq',1,'2025-05-19 00:13:09',''),(10,'Nguyễn Tuấn Nam','Namnt@gmail.com','0723456789','2002-05-23',0,'Củ Chi','$2a$10$WYEA1.VFcVZYAR1tAqY8vO/Sr/C1Y3bVp6TDVwyKAbQIfPcTuBCQm',1,'2025-05-23 17:17:39',''),(11,'Hồ Ngọc Châu','Chaunh@gmail.com','0823456789','2000-06-24',1,'TP HCM','$2a$10$.lsebnvPVVON2BoJQ6wKxODpJ4KOe3.c0QKTG037ZOAa9fFQf/Qve',2,'2025-05-24 16:47:19',NULL),(12,'Nguyễn Ngọc Hà','hann@gmail.com','038023564','2003-11-25',1,'phường Thủ Dầu Một','$2a$10$jtuaGLERzhkK9ScR63RZPO1VLcFgTratjYAmWhWZXz1.PHKX2p3XK',1,'2025-08-21 09:18:07',NULL),(13,'Nguyễn Thanh Thảo','thaont@gmail.com','0320156489','2002-05-14',1,'phường Chánh Hiệp','$2a$10$Ru49ve58Uu1O0CXhf0.Lqu48tpwLWf32xZVJ32FS9VL91up0IulK6',1,'2025-08-21 16:05:53','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1755825874773.jpg'),(14,'Trần Văn C','Thuann1m@example.com','0323456789','1985-06-20',0,'123 Lê Lợi, TP.HCM','$2a$10$iwcOFEhX3qKwdYfG/kQ6QuPxQUokKlxOENmeSJ/VYgu4918xbGLhq',2,'2025-08-22 01:23:35','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1755825850325.jpg'),(15,'Nguyễn Thanh Hằng','hangnt@gmail.com','0320156487','2002-05-14',1,'phường Chánh Hiệp','$2a$10$v4mss5YDekozGY9xkvRQIe/FdEjz8uFNx1U9u84TzjQkASAvkUdpO',1,'2025-08-22 01:26:01',NULL),(16,'Nguyễn Thanh Thu','nguyenvan123@example.com','0320156487','2002-05-14',0,'phường Chánh Hiệp','$2a$10$.cnH7Hx/jTUaCll79EDPk.q5zlRJ1ntCUmGtV9sQSULT8ybVYnCH6',1,'2025-08-22 01:26:52',NULL),(17,'Nguyễn Thanh Thuỷ','thuynt@gmail.com','0320156488','2002-05-14',1,'phường Chánh Hiệp','$2a$10$4iEmWwD6Ju6U/vamg5MbMOjWERSQ2PHCPA67uCq42eqQlKDxYlrtm',1,'2025-08-22 13:10:33',''),(18,'Nguyễn Thị Ngân','ngannt@gmail.com','0337033824','2000-09-28',0,'Chánh Hiệp','$2a$10$aOyLpSBFMRaj1n8MRk2.EOma7w1gsf/gIjKB4aMFkRd4Te./hMaAm',2,'2025-09-28 17:36:54','string'),(19,'Nguyễn Tiến Duy','duynt@gmail.com','0337043824','1999-10-28',1,'Phú Lợi','$2a$10$ixigocA1JDl.IF/Nu7vb4eh6.JAdWX7eZWGfX/J6C.8oDt5YPGtRO',2,'2025-09-28 17:39:05',''),(21,'Trần Quốc Hưng','hungtq@gmail.com','0335021425','1998-10-24',0,'Chánh Hiệp','$2a$10$OqWuoiRwDumfhosnXeY89eQtU1aYl0KRh3YFIOENknGbMQoHL1mVe',2,'2025-10-24 15:36:57',NULL),(22,'Nguyễn Ngọc Hiền','hiennn@gmail.com','0331052674','1998-10-26',1,'Phú Thọ','$2a$10$TiRHyAvjV776S5vTqHkS5.P0MPdB53nPfCNRvdaWIyZjMl4SdYYRy',2,'2025-10-26 03:13:19',''),(23,'Lưu Đức Thịnh','thinhld@gmail.com','0334013754','2000-09-26',0,'Phường Sài Gòn','$2a$10$4UMopa1Mav7yU/cKqs77AuL4CNwQITdaxT/vlBPCODRnEbM5vrfQe',2,'2025-10-26 04:02:22',''),(24,'Trần Thanh Yến','yentt@gmail.com','0322063455','1999-09-02',1,'Phường Thuận An','$2a$10$vtO/28Wsx0xIl5J9c8Ev3ORYuv2.hDR4fnhUrCU34ss9Si0.zrYJW',2,'2025-10-26 04:03:47',''),(25,'Nguyễn Thanh Hoa','hoant@gmail.com','0935451060','1998-07-19',1,'Phường Thuận An','$2a$10$xpNbX//Nwwt/DQkBeJ9uOeFlH.tEJcEWdcVU4SVuhYjimpMhnHgw2',2,'2025-10-26 04:04:58',''),(26,'Nguyễn Thanh Phúc','phucnt@gmail.com','0925522406','1998-10-19',1,'Phường Hiệp An','$2a$10$uKMUKGbqsZKZkRl75cKskutuR6UmPG9Qh48.wQ6bg9b5lY8yihn7e',2,'2025-10-26 04:05:50',''),(28,'Nguyễn Thanh Hải','haint@gmail.com','0337062145','1992-04-23',0,'phường Phú Thọ','$2a$10$bU0L3VQY0654v7JueeFlOeSOE4G.MdjPaIEDGKpIO0k/1ffKVHr3q',2,'2025-10-31 16:23:13','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1761927791946.jpeg'),(29,'Nguyễn Thanh Liên','liennt@gmail.com','0335021454','1997-12-23',1,'Phường Phú Thọ','$2a$10$TB9nGpI82SaGLnXIwXHpH.qkCJ6lv7A0cTCR49EvAXEqSwa16Kvya',2,'2025-11-01 04:55:33','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/cashier1.jpeg'),(30,'Nguyễn Văn An','nguyenvana.tm@gmail.com','0911222333','1980-05-01',0,'123 Đường Tim Mạch, Hà Nội','$2a$10$4CD0F9dO3R6TyxE/Fi63lui2N5wQ2NaRe/Z0U0QFzkgn1SG.AywKe',2,'2025-11-13 07:50:05','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1763020203787.jpeg'),(31,'Trần Lan Hương','tranlanhuong.tm@gmail.com','0911222444','1990-06-25',1,'456 Đường Tim Mạch, Hà Nội','$2a$10$tu7cyy7pw4d5iAzPvzfkLOCCMbwOCgk6hjbnNrWxtZduAAIK3Z962',2,'2025-11-13 08:04:47','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1763021086187.jpeg'),(32,'Lê Văn Dũng','levandung@gmail.com','0911222555','1981-03-16',0,'789 Đường Tim Mạch, Hà Nội','$2a$10$bb72O7y3EWBKROPD9TVfROhPYKPDub7U04fvXk.EwK1.ZbOmy1AYG',2,'2025-11-13 08:07:42','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1763021261222.jpg'),(33,'Phạm Yến Nhi','phamyennhi@gmail.com','0911333111','1989-06-25',1,'101 Đường Hô Hấp, TP.HCM','$2a$10$F0TD18byahJ.T29EbUTazO.v9ou48I1zXVOJG3r7kyP423IbYugve',2,'2025-11-13 08:09:20','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1763021359543.jpg'),(34,'Hoàng Thị Diễm Ngân','hoangthidiemngan@gmail.com','0911333222','1989-10-12',1,'202 Đường Hô Hấp, TP.HCM','$2a$10$t5.dDx5c8vwCQd4D1rnOEuXUBe4iP8L7WT7ylSKaCR6gjbi1Hf742',2,'2025-11-13 08:11:07','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1763021466423.jpg'),(35,'Đặng Văn Sĩ','dangvansi.hh@gmail.com','0911333333','1988-05-14',0,'303 Đường Hô Hấp, TP.HCM','$2a$10$He/ejCMZ15tfGTGtlVd7le110X1kKsJqw9gon9aE9ccSPyxBsDUXy',2,'2025-11-13 08:12:50','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1763021569006.jpg'),(36,'Lê Thị Thảo Vy','tranquocdanh.ctch@gmail.com','0336021454','1981-10-14',1,'123 Đường Lê Lợi, Phường Sài Gòn, TP HCM','$2a$10$Uo6EaJRWbd/2Xm9DB9dfiOiXLMiP.5dr8FpVAdmpmm7gI0ZhqqVZW',2,'2025-11-15 08:19:08','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1763194746482.jpg'),(37,'Nguyễn Ngọc Thanh','nguyenngocthanh.nt@gmail.com','0335021451','1982-04-25',1,'136 Đường Lê Lợi, Phường Sài Gòn, TP HCM','$2a$10$AbH1btzW2aUn8OY6.U3CoOD4Fc3ehFfuYYzR9t.aqzMUucEzSeTGe',2,'2025-11-19 16:18:33','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1763569111276.jpeg'),(38,'Nguyễn Đình Phú','nguyendinhphu@gmail.com','0334056789','2008-05-13',0,'Phường Thuận An','$2a$10$JO9WyyhL/o2w8Us1Bos14u1XIeoc70Ld8mCMUN2JkifcnRBAU9JDq',1,'2025-11-21 09:10:08',NULL),(39,'Trần Thanh Tâm','tranthanhtam@gmail.com','0335012546','1999-04-23',1,'03 đường Hùng Vương, Phường Bình Dương, TP HCM','$2a$10$4eIrKS4ZbM8eAT8nFT2e6up7Kk6LOEFXrYelxg0itTG.Zk.5fC9Lu',1,'2025-11-22 02:37:29','');
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
-- Dumping routines for database 'clinicbooking'
--
/*!50003 DROP PROCEDURE IF EXISTS `generate_slots_for_schedule` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `generate_slots_for_schedule`(
  IN p_doctor_schedule_id INT,
  IN p_slot_minutes INT
)
BEGIN
  DECLARE v_start TIME;
  DECLARE v_end   TIME;
  DECLARE v_curr  TIME;
  DECLARE v_next  TIME;
  DECLARE v_shift INT;

  -- Lấy khung giờ từ shift_type của doctor_schedule
  SELECT st.start_time, st.end_time, ds.shift_type_id
    INTO v_start, v_end, v_shift
  FROM doctor_schedules ds
  JOIN shift_type st ON st.id = ds.shift_type_id
  WHERE ds.id = p_doctor_schedule_id;

  IF v_start IS NULL OR v_end IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Schedule or shift_type not found.';
  END IF;

  -- Xóa slot cũ (nếu muốn tái sinh)
  DELETE FROM schedule_slots WHERE doctor_schedule_id = p_doctor_schedule_id;

  SET v_curr = v_start;
  WHILE ADDTIME(v_curr, SEC_TO_TIME(p_slot_minutes*60)) <= v_end DO
    SET v_next = ADDTIME(v_curr, SEC_TO_TIME(p_slot_minutes*60));

    INSERT INTO schedule_slots (doctor_schedule_id, start_time, end_time, is_booked)
    VALUES (p_doctor_schedule_id, v_curr, v_next, 0);

    SET v_curr = v_next;
  END WHILE;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `unified_service_orders`
--

/*!50001 DROP VIEW IF EXISTS `unified_service_orders`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `unified_service_orders` AS select `lt`.`id` AS `order_id`,'LAB' AS `service_type`,`t`.`test_name` AS `service_name`,`lt`.`requested_date` AS `requested_date`,`lt`.`result_date` AS `result_date`,`lt`.`result` AS `result`,`lt`.`status` AS `status`,`lt`.`doctor_id` AS `doctor_id`,`lt`.`record_id` AS `record_id`,`u`.`fullname` AS `staff_name`,`ls`.`lab_scode` AS `staff_code` from ((((`lab_tests` `lt` left join `lab_staff` `ls` on((`lt`.`lab_staff_id` = `ls`.`id`))) left join `staff` `s` on((`ls`.`staff_id` = `s`.`id`))) left join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `testtypes` `t` on((`lt`.`test_type_id` = `t`.`id`))) union all select `it`.`id` AS `order_id`,'IMAGING' AS `service_type`,`i`.`imaging_name` AS `service_name`,`it`.`requested_date` AS `requested_date`,`it`.`result_date` AS `result_date`,`it`.`result` AS `result`,`it`.`status` AS `status`,`it`.`doctor_id` AS `doctor_id`,`it`.`record_id` AS `record_id`,`u`.`fullname` AS `staff_name`,`ims`.`img_scode` AS `staff_code` from ((((`imaging_tests` `it` left join `imaging_staff` `ims` on((`it`.`imaging_staff_id` = `ims`.`id`))) left join `staff` `s` on((`ims`.`staff_id` = `s`.`id`))) left join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `imagingtypes` `i` on((`it`.`image_type_id` = `i`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

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
/*!50001 VIEW `v_staff_unified` AS select `d`.`id` AS `id`,'DOCTOR' AS `roleType`,`d`.`doctorcode` AS `code`,`s`.`department_id` AS `departmentId`,`dept`.`name` AS `department`,`s`.`position_id` AS `positionId`,`pos`.`position` AS `position`,`d`.`specialty_id` AS `specialtyId`,`sp`.`name` AS `specialty`,`d`.`experience_years` AS `experienceYears`,`u`.`fullname` AS `fullname`,`u`.`email` AS `email`,`u`.`phone_number` AS `phoneNumber`,`u`.`date_of_birth` AS `dateOfBirth`,`u`.`gender` AS `gender`,`u`.`address` AS `address`,`u`.`avartar_url` AS `avatar_url`,`s`.`id` AS `staffId` from (((((`doctors` `d` join `staff` `s` on((`d`.`staff_id` = `s`.`id`))) join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `department` `dept` on((`s`.`department_id` = `dept`.`id`))) left join `staff_position` `pos` on((`s`.`position_id` = `pos`.`id`))) left join `specialty` `sp` on((`d`.`specialty_id` = `sp`.`id`))) union all select `n`.`id` AS `id`,'NURSE' AS `roleType`,`n`.`nurse_code` AS `code`,`s`.`department_id` AS `departmentId`,`dept`.`name` AS `department`,`s`.`position_id` AS `positionId`,`pos`.`position` AS `position`,NULL AS `specialtyId`,NULL AS `specialty`,`n`.`experience_years` AS `experienceYears`,`u`.`fullname` AS `fullname`,`u`.`email` AS `email`,`u`.`phone_number` AS `phoneNumber`,`u`.`date_of_birth` AS `dateOfBirth`,`u`.`gender` AS `gender`,`u`.`address` AS `address`,`u`.`avartar_url` AS `avatar_url`,`s`.`id` AS `staffId` from ((((`nurses` `n` join `staff` `s` on((`n`.`staff_id` = `s`.`id`))) join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `department` `dept` on((`s`.`department_id` = `dept`.`id`))) left join `staff_position` `pos` on((`s`.`position_id` = `pos`.`id`))) union all select `l`.`id` AS `id`,'LAB' AS `roleType`,`l`.`lab_scode` AS `code`,`s`.`department_id` AS `departmentId`,`dept`.`name` AS `department`,`s`.`position_id` AS `positionId`,`pos`.`position` AS `position`,NULL AS `specialtyId`,NULL AS `specialty`,`l`.`experience_years` AS `experienceYears`,`u`.`fullname` AS `fullname`,`u`.`email` AS `email`,`u`.`phone_number` AS `phoneNumber`,`u`.`date_of_birth` AS `dateOfBirth`,`u`.`gender` AS `gender`,`u`.`address` AS `address`,`u`.`avartar_url` AS `avatar_url`,`s`.`id` AS `staffId` from ((((`lab_staff` `l` join `staff` `s` on((`l`.`staff_id` = `s`.`id`))) join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `department` `dept` on((`s`.`department_id` = `dept`.`id`))) left join `staff_position` `pos` on((`s`.`position_id` = `pos`.`id`))) union all select `i`.`id` AS `id`,'IMAGING' AS `roleType`,`i`.`img_scode` AS `code`,`s`.`department_id` AS `departmentId`,`dept`.`name` AS `department`,`s`.`position_id` AS `positionId`,`pos`.`position` AS `position`,NULL AS `specialtyId`,NULL AS `specialty`,`i`.`experience_years` AS `experienceYears`,`u`.`fullname` AS `fullname`,`u`.`email` AS `email`,`u`.`phone_number` AS `phoneNumber`,`u`.`date_of_birth` AS `dateOfBirth`,`u`.`gender` AS `gender`,`u`.`address` AS `address`,`u`.`avartar_url` AS `avatar_url`,`s`.`id` AS `staffId` from ((((`imaging_staff` `i` join `staff` `s` on((`i`.`staff_id` = `s`.`id`))) join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `department` `dept` on((`s`.`department_id` = `dept`.`id`))) left join `staff_position` `pos` on((`s`.`position_id` = `pos`.`id`))) union all select `p`.`id` AS `id`,'PHA' AS `roleType`,`p`.`pha_scode` AS `code`,`s`.`department_id` AS `departmentId`,`dept`.`name` AS `department`,`s`.`position_id` AS `positionId`,`pos`.`position` AS `position`,NULL AS `specialtyId`,NULL AS `specialty`,`p`.`experience_years` AS `experienceYears`,`u`.`fullname` AS `fullname`,`u`.`email` AS `email`,`u`.`phone_number` AS `phoneNumber`,`u`.`date_of_birth` AS `dateOfBirth`,`u`.`gender` AS `gender`,`u`.`address` AS `address`,`u`.`avartar_url` AS `avatar_url`,`s`.`id` AS `staffId` from ((((`pharmacy_staff` `p` join `staff` `s` on((`p`.`staff_id` = `s`.`id`))) join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `department` `dept` on((`s`.`department_id` = `dept`.`id`))) left join `staff_position` `pos` on((`s`.`position_id` = `pos`.`id`))) union all select `r`.`id` AS `id`,'REC' AS `roleType`,`r`.`receptionist_code` AS `code`,`s`.`department_id` AS `departmentId`,`dept`.`name` AS `department`,`s`.`position_id` AS `positionId`,`pos`.`position` AS `position`,NULL AS `specialtyId`,NULL AS `specialty`,`r`.`experience_years` AS `experienceYears`,`u`.`fullname` AS `fullname`,`u`.`email` AS `email`,`u`.`phone_number` AS `phoneNumber`,`u`.`date_of_birth` AS `dateOfBirth`,`u`.`gender` AS `gender`,`u`.`address` AS `address`,`u`.`avartar_url` AS `avatar_url`,`s`.`id` AS `staffId` from ((((`receptionist` `r` join `staff` `s` on((`r`.`staff_id` = `s`.`id`))) join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `department` `dept` on((`s`.`department_id` = `dept`.`id`))) left join `staff_position` `pos` on((`s`.`position_id` = `pos`.`id`))) union all select `c`.`id` AS `id`,'CASHIER' AS `roleType`,`c`.`cas_scode` AS `code`,`s`.`department_id` AS `departmentId`,`dept`.`name` AS `department`,`s`.`position_id` AS `positionId`,`pos`.`position` AS `position`,NULL AS `specialtyId`,NULL AS `specialty`,`c`.`experience_years` AS `experienceYears`,`u`.`fullname` AS `fullname`,`u`.`email` AS `email`,`u`.`phone_number` AS `phoneNumber`,`u`.`date_of_birth` AS `dateOfBirth`,`u`.`gender` AS `gender`,`u`.`address` AS `address`,`u`.`avartar_url` AS `avatar_url`,`s`.`id` AS `staffId` from ((((`cashier` `c` join `staff` `s` on((`c`.`staff_id` = `s`.`id`))) join `users` `u` on((`s`.`user_id` = `u`.`id`))) left join `department` `dept` on((`s`.`department_id` = `dept`.`id`))) left join `staff_position` `pos` on((`s`.`position_id` = `pos`.`id`))) */;
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

-- Dump completed on 2025-12-04 12:33:13
