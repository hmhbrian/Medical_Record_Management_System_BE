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
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment_status`
--

LOCK TABLES `appointment_status` WRITE;
/*!40000 ALTER TABLE `appointment_status` DISABLE KEYS */;
INSERT INTO `appointment_status` VALUES (1,1,1,NULL,3,'2025-05-16 09:29:47'),(2,1,2,'',4,'2025-05-16 09:41:30'),(8,6,1,NULL,3,'2025-05-19 00:17:16'),(13,7,1,NULL,3,'2025-05-25 02:11:33'),(14,6,6,'Bận đột xuất',3,'2025-05-25 02:12:04'),(15,8,1,NULL,3,'2025-08-22 11:11:38'),(16,9,1,NULL,9,'2025-08-24 04:00:07'),(17,10,1,NULL,3,'2025-08-25 06:57:26'),(18,7,6,'Bận đột xuất',3,'2025-08-25 06:58:13'),(22,12,1,'Đau răng hàm',10,'2025-10-16 03:05:55'),(23,12,6,'Bận đột xuất',10,'2025-10-16 03:11:37'),(24,13,1,'Đau răng hàm',10,'2025-10-16 08:15:39'),(25,14,1,'Đau răng hàm',3,'2025-10-19 02:23:07'),(26,14,2,NULL,8,'2025-10-23 08:20:16'),(27,14,3,NULL,22,'2025-10-23 08:24:22'),(28,14,4,NULL,8,'2025-10-23 08:26:27'),(29,13,2,NULL,8,'2025-10-31 01:59:07'),(30,13,3,NULL,NULL,'2025-10-31 02:01:37'),(31,15,1,'Khát và Uống nhiều, Đi tiểu nhiều',10,'2025-11-01 03:47:13'),(32,15,2,NULL,28,'2025-11-01 03:52:13'),(33,15,3,NULL,NULL,'2025-11-01 03:56:42'),(34,15,4,NULL,28,'2025-11-01 03:58:57');
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
END ;;
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
  PRIMARY KEY (`id`),
  KEY `patient_id` (`patient_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `doctor_schedule_id` (`doctor_schedule_id`),
  KEY `fk_a_slot_schedule` (`schedule_slot_id`),
  CONSTRAINT `appointments_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `appointments_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `appointments_ibfk_3` FOREIGN KEY (`doctor_schedule_id`) REFERENCES `doctor_schedules` (`id`),
  CONSTRAINT `fk_a_slot_schedule` FOREIGN KEY (`schedule_slot_id`) REFERENCES `schedule_slots` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES (1,1,1,2,'2025-05-16 09:29:47',3,'LH1'),(6,1,1,152,'2025-05-19 00:17:16',15,'LH6'),(7,1,2,253,'2025-05-25 02:11:33',28,'LH7'),(8,1,3,316,'2025-08-22 11:11:38',37,'LH8'),(9,3,3,315,'2025-08-24 04:00:07',45,'LH9'),(10,1,1,306,'2025-08-25 06:57:26',61,'LH10'),(12,4,4,3,'2025-10-16 03:05:55',67,'LH11'),(13,4,4,3,'2025-10-16 08:15:39',67,'LH13'),(14,1,4,319,'2025-10-19 02:23:06',104,'LH14'),(15,4,6,326,'2025-11-01 03:47:13',185,'LH15');
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
) ENGINE=InnoDB AUTO_INCREMENT=327 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_schedules`
--

LOCK TABLES `doctor_schedules` WRITE;
/*!40000 ALTER TABLE `doctor_schedules` DISABLE KEYS */;
INSERT INTO `doctor_schedules` VALUES (1,1,2,3,'2025-05-10','ACTIVE',15,0),(2,1,1,3,'2025-05-12','ACTIVE',15,1),(3,4,2,17,'2025-10-17','ACTIVE',15,1),(52,2,2,2,'2025-05-13','ACTIVE',15,0),(53,2,1,1,'2025-05-14','ACTIVE',15,0),(102,1,2,3,'2025-05-20','ACTIVE',15,0),(152,1,3,1,'2025-05-20','ACTIVE',10,0),(153,1,1,1,'2025-05-21','ACTIVE',14,0),(154,1,2,3,'2025-05-22','ACTIVE',14,0),(155,1,3,3,'2025-05-22','ACTIVE',10,0),(156,1,1,3,'2025-05-23','ACTIVE',12,0),(157,1,2,3,'2025-05-23','ACTIVE',12,0),(158,1,2,3,'2025-05-24','ACTIVE',12,0),(202,4,1,3,'2025-05-29','ACTIVE',15,0),(252,2,1,2,'2025-05-26','ACTIVE',15,0),(253,2,1,2,'2025-05-27','ACTIVE',15,0),(254,2,2,2,'2025-05-27','ACTIVE',10,0),(255,2,2,6,'2025-05-28','ACTIVE',15,0),(256,2,3,6,'2025-05-28','ACTIVE',8,0),(257,2,1,2,'2025-05-30','ACTIVE',15,0),(258,2,2,2,'2025-05-30','ACTIVE',15,0),(259,2,1,2,'2025-06-01','ACTIVE',15,0),(260,2,2,6,'2025-06-02','ACTIVE',15,0),(261,2,3,6,'2025-06-02','ACTIVE',8,0),(262,1,1,1,'2025-05-27','ACTIVE',15,0),(263,1,1,1,'2025-05-28','ACTIVE',15,0),(264,1,2,1,'2025-05-28','ACTIVE',15,0),(265,1,2,7,'2025-05-29','ACTIVE',15,0),(266,1,3,7,'2025-05-29','ACTIVE',8,0),(302,1,2,3,'2025-08-24','ACTIVE',20,0),(303,1,1,3,'2025-08-24','ACTIVE',20,0),(304,1,2,3,'2025-08-25','ACTIVE',20,0),(305,1,3,3,'2025-08-25','ACTIVE',20,0),(306,1,1,2,'2025-08-27','ACTIVE',20,0),(307,1,2,2,'2025-08-27','ACTIVE',20,0),(308,2,2,5,'2025-08-24','ACTIVE',20,0),(309,2,3,5,'2025-08-24','ACTIVE',20,0),(310,2,2,5,'2025-08-25','ACTIVE',20,0),(311,2,1,5,'2025-08-26','ACTIVE',20,0),(312,2,2,5,'2025-08-26','ACTIVE',20,0),(313,3,1,7,'2025-08-24','ACTIVE',20,0),(314,3,2,7,'2025-08-24','ACTIVE',20,0),(315,3,2,7,'2025-08-25','ACTIVE',20,0),(316,3,3,7,'2025-08-25','ACTIVE',20,0),(317,4,1,17,'2025-10-18','ACTIVE',15,0),(318,4,1,3,'2025-10-21','ACTIVE',15,0),(319,4,1,3,'2025-10-20','ACTIVE',15,1),(320,4,2,3,'2025-10-20','ACTIVE',15,0),(321,4,2,3,'2025-10-21','ACTIVE',15,0),(322,4,2,4,'2025-10-22','ACTIVE',15,0),(323,4,3,4,'2025-10-22','ACTIVE',15,0),(324,2,1,7,'2025-10-30','ACTIVE',15,0),(326,6,1,8,'2025-11-03','ACTIVE',15,1);
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
INSERT INTO `doctor_schedules_seq` VALUES (151);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES (1,1,'DOC1',1,9,'Chứng chỉ Nội khoa','Bộ Y tế','2015-08-20'),(2,2,'DOC2',1,10,'Chứng chỉ Nội khoa','Bộ Y tế','2015-08-20'),(3,3,'DOC3',7,10,'Chứng chỉ Nội khoa','Bộ Y tế','2020-08-20'),(4,4,'DOC4',8,11,'Chứng chỉ Nội khoa','Bộ Y tế','2020-08-20'),(5,6,'DOC5',1,9,'Chứng chỉ Nội khoa','Bộ Y tế','2015-08-20'),(6,17,'DOC6',10,7,'Chứng chỉ Nội khoa','Bộ Y tế','2018-10-12');
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
  `is_main_image` tinyint(1) DEFAULT NULL COMMENT 'Đánh dấu đây là hình ảnh chính',
  `name` varchar(255) DEFAULT NULL,
  `imaging_tests_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlsmdkp9aiovlxjgy6r3296urd` (`imaging_tests_id`),
  KEY `imaging_test_id` (`imaging_test_id`),
  CONSTRAINT `FKpqfvemaaxx0o7iq7jh81n4h74` FOREIGN KEY (`imaging_tests_id`) REFERENCES `imaging_tests` (`id`),
  CONSTRAINT `imaging_result_files_ibfk_1` FOREIGN KEY (`imaging_test_id`) REFERENCES `imaging_tests` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Danh sách kết quả của 1 chẩn đoán';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imaging_result_files`
--

LOCK TABLES `imaging_result_files` WRITE;
/*!40000 ALTER TABLE `imaging_result_files` DISABLE KEYS */;
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
  `inpatient_record_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `imaging_staff_id` int DEFAULT NULL,
  `image_type_id` int DEFAULT NULL,
  `requested_date` datetime DEFAULT NULL,
  `result_date` datetime DEFAULT NULL,
  `image_url` text,
  `result` text,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `inpatient_record_id` (`inpatient_record_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `imageType_id` (`image_type_id`),
  KEY `imagingtests_ibfk_6` (`imaging_staff_id`),
  CONSTRAINT `imaging_tests_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `imaging_tests_ibfk_2` FOREIGN KEY (`inpatient_record_id`) REFERENCES `inpatient_records` (`id`),
  CONSTRAINT `imaging_tests_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `imaging_tests_ibfk_5` FOREIGN KEY (`image_type_id`) REFERENCES `imagingtypes` (`id`),
  CONSTRAINT `imaging_tests_ibfk_6` FOREIGN KEY (`imaging_staff_id`) REFERENCES `imaging_staff` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imaging_tests`
--

LOCK TABLES `imaging_tests` WRITE;
/*!40000 ALTER TABLE `imaging_tests` DISABLE KEYS */;
INSERT INTO `imaging_tests` VALUES (2,4,NULL,4,NULL,7,'2025-10-27 09:54:16',NULL,NULL,NULL,'PAID');
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imagingtypes`
--

LOCK TABLES `imagingtypes` WRITE;
/*!40000 ALTER TABLE `imagingtypes` DISABLE KEYS */;
INSERT INTO `imagingtypes` VALUES (1,'X-quang ngực thẳng',300000,'Chẩn đoán các bệnh lý phổi và tim mạch',1,'IMG1',1),(2,'Siêu âm ổ bụng',350000,'Đánh giá các cơ quan trong ổ bụng',1,'IMG2',1),(3,'CT Scanner vùng bụng',1200000,'Chẩn đoán chi tiết trước phẫu thuật ngoại khoa',2,'IMG3',1),(4,'Siêu âm tim',400000,'Đánh giá bệnh lý tim mạch ở trẻ em',3,'IMG4',1),(5,'Siêu âm thai',300000,'Theo dõi sự phát triển của thai nhi',5,'IMG5',1),(6,'Siêu âm sản phụ khoa',350000,'Đánh giá sức khỏe sinh sản',5,'IMG6',1),(7,'X-quang răng',200000,'Đánh giá bệnh lý trong răng',5,'IMG7',1);
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
INSERT INTO `invoice_sequence` VALUES (1,'2025','AA/20E',NULL,2);
/*!40000 ALTER TABLE `invoice_sequence` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_test_details`
--

LOCK TABLES `lab_test_details` WRITE;
/*!40000 ALTER TABLE `lab_test_details` DISABLE KEYS */;
INSERT INTO `lab_test_details` VALUES (15,3,9,'8.5','Glucose','mmol/L','3.9','6.4',1,'Tăng cao, nghi ngờ Đái tháo đường.'),(16,3,10,'55','ALT (SGPT)','U/L','0','40',1,'Men gan tăng nhẹ.'),(17,3,13,'6.1','Cholesterol','mmol/L','0','5.2',1,'Tăng cao.'),(18,3,19,'2.5','Triglycerides','mmol/L','0','1.7',1,'Tăng cao.'),(19,3,21,'4.2','LDL-C','mmol/L','0','3.37',1,'Tăng cao.'),(20,3,22,'8','CRP (Định lượng)','mg/L','0','5',1,'Tăng nhẹ, có thể do viêm nhiễm.'),(21,3,11,'30','AST (SGOT)','U/L','0','40',0,NULL),(22,3,12,'88','Creatinine','umol/L','60','110',0,NULL),(23,3,14,'5.5','Urea','mmol/L','2.5','7.5',0,NULL),(24,3,15,'350','Acid Uric','umol/L','140','420',0,NULL),(25,3,16,'72','Total Protein','g/L','60','80',0,NULL),(26,3,17,'40','Albumin','g/L','35','50',0,NULL),(27,3,18,'15','Bilirubin T.T','umol/L','0','21',0,NULL),(28,3,20,'1.1','HDL-C','mmol/L','0.9','3.0',0,NULL);
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
  `inpatient_record_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `lab_staff_id` int DEFAULT NULL,
  `test_type_id` int DEFAULT NULL,
  `requested_date` datetime DEFAULT NULL,
  `result_date` datetime DEFAULT NULL,
  `result` text,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `inpatient_record_id` (`inpatient_record_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `labStaff_id` (`lab_staff_id`),
  KEY `testType_id` (`test_type_id`),
  CONSTRAINT `lab_tests_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `lab_tests_ibfk_2` FOREIGN KEY (`inpatient_record_id`) REFERENCES `inpatient_records` (`id`),
  CONSTRAINT `lab_tests_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `lab_tests_ibfk_4` FOREIGN KEY (`lab_staff_id`) REFERENCES `lab_staff` (`id`),
  CONSTRAINT `lab_tests_ibfk_5` FOREIGN KEY (`test_type_id`) REFERENCES `testtypes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_tests`
--

LOCK TABLES `lab_tests` WRITE;
/*!40000 ALTER TABLE `lab_tests` DISABLE KEYS */;
INSERT INTO `lab_tests` VALUES (3,6,NULL,6,2,2,'2025-11-01 04:39:09','2025-11-01 05:00:09',NULL,'COMPLETED'),(4,6,NULL,6,NULL,4,'2025-11-01 04:39:09',NULL,NULL,'IN_PROGRESS');
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Liên kết chẩn đoán ICD-10 với hồ sơ bệnh án';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_record_icd10`
--

LOCK TABLES `medical_record_icd10` WRITE;
/*!40000 ALTER TABLE `medical_record_icd10` DISABLE KEYS */;
INSERT INTO `medical_record_icd10` VALUES (5,4,44,1,0),(6,4,45,0,1),(10,6,13,0,0);
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
  `visit_date` date DEFAULT NULL,
  `visit_number` int DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_records`
--

LOCK TABLES `medical_records` WRITE;
/*!40000 ALTER TABLE `medical_records` DISABLE KEYS */;
INSERT INTO `medical_records` VALUES (1,1,1,'2025-05-12',1,'Thiếu máu nhẹ',1,'Đau đầu, chóng mặt',NULL,'PENDING_RESULTS','MR1'),(2,1,1,'2025-05-24',1,'Loét dạ dày',1,'Đau bụng',NULL,'WAITING','MR2'),(3,1,3,'2025-08-22',1,'Loét dạ dày',8,'Đau bụng',NULL,'WAITING','MR3'),(4,1,4,'2025-10-23',1,'đau răng do sâu',14,'Đau răng hàm','Đau răng do sâu','COMPLETED','MR4'),(5,4,4,'2025-10-31',1,NULL,13,'Đau răng',NULL,'WAITING','MR5'),(6,4,6,'2025-11-01',1,'Nghi ngờ Đái tháo đường',15,'Khát và Uống nhiều, Đi tiểu nhiều','Bệnh nhân có tứ chứng kinh điển (đa khát, đa niệu), sụt 5kg/tháng. Đề nghị xét nghiệm Glucose máu lúc đói và HbA1c khẩn.','PENDING_PREPAYMENT','MR6');
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
INSERT INTO `medicines` VALUES (1,'Paracetamol 500 mg','viên','2025-07-24',2000,100,2000,'500 mg','Traphaco',0,'2023-07-24',3,'Paracetamol','Viên nén',NULL),(2,'Amoxicillin 500 mg','viên','2025-08-24',1500,100,1500,'500 mg','DHG Pharma',0,'2023-08-24',1,'Amoxicillin','Viên nén',NULL),(3,'Ascorbic acid 500mg','viên','2025-08-01',1000,50,1000,'500 mg','Traphaco',0,'2023-08-01',5,'Ascorbic acid','Viên nén',NULL),(4,'Ibuprofen 400mg','viên','2025-11-20',2500,100,2500,'400 mg','Sanofi',0,'2023-11-20',3,'Ibuprofen','Viên nén',NULL),(5,'Acetylsalicylic acid 81mg','viên','2025-05-30',1800,100,1800,'81 mg','Bayer',0,'2023-05-30',2,'Acetylsalicylic acid','Viên nén',NULL),(6,'Metformin hydrochloride 500mg','viên','2025-12-01',3000,100,3000,'500 mg','US Pharma',0,'2023-12-01',4,'Metformin hydrochloride','Viên nén',NULL),(7,'Azithromycin 500mg','viên','0202-10-05',5000,100,5000,'500 mg','Pfizer',0,'2022-10-05',1,'Azithromycin','Viên nén',NULL),(8,'Loratadine 10mg','viên','2026-02-18',1200,50,1200,'10 mg','Stada',0,'2026-02-18',6,'Loratadine','Viên nén',NULL),(9,'Cefixime 200mg','viên','2025-09-15',3500,100,3500,'200 mg','Domesco',0,'2023-09-15',1,'Cefixime','Viên nén',NULL),(10,'Omeprazole 20mg','viên','2026-03-01',2200,100,2200,'20 mg','Mekophar',0,'2024-03-01',7,'Omeprazole','Viên nang',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (1,3,'PAT1','Tiền sử cao huyết áp','BH123456789',0.7),(3,9,'PAT2','Bệnh tim bẩm sinh','SV1234567',0.8),(4,10,'PAT4','','',0),(5,12,'PAT5','Phổi yếu','SV1526425888',0.8),(6,13,'PAT6','','',0),(7,15,'PAT7','','',0),(8,16,'PAT8','','',0),(9,17,'PAT9','','',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentdetails`
--

LOCK TABLES `paymentdetails` WRITE;
/*!40000 ALTER TABLE `paymentdetails` DISABLE KEYS */;
INSERT INTO `paymentdetails` VALUES (4,2,'IMAGING_TEST',2,'X-quang răng',200000.00,140000.00,60000.00,'2025-10-27 09:54:16'),(5,3,'PRESCRIPTION',4,'Đơn thuốc ngày 2025-10-30T16:23:15.094107',34000.00,23800.00,10200.00,'2025-10-30 15:51:22'),(6,3,'EXAMINATION',4,'Khám ngoại tổng quát',250000.00,175000.00,75000.00,'2025-10-30 15:51:22'),(7,4,'LAB_TEST',3,'Xét nghiệm sinh hóa',200000.00,0.00,200000.00,'2025-11-01 04:39:09'),(8,4,'LAB_TEST',4,'Xét nghiệm Hóa sinh/Tế bào nước tiểu',120000.00,0.00,120000.00,'2025-11-01 04:39:09');
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
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `cashier_id` (`cashier_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`cashier_id`) REFERENCES `cashier` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (2,4,1,450000.00,'2025-11-02 17:07:06','CASH',315000.00,135000.00,'PAID','2025-10-27 09:35:54',135000.00,NULL,'AA/20E-2025-000001','AA/20E',_binary ''),(3,4,1,284000.00,'2025-11-03 09:32:08','CASH',198800.00,85200.00,'PAID','2025-10-30 15:51:22',100000.00,'Hoàn lại: 14800.00','AA/20E-2025-000002','AA/20E',_binary ''),(4,6,1,320000.00,'2025-11-01 04:50:09','CASH',0.00,320000.00,'PAID','2025-11-01 04:39:09',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptiondetails`
--

LOCK TABLES `prescriptiondetails` WRITE;
/*!40000 ALTER TABLE `prescriptiondetails` DISABLE KEYS */;
INSERT INTO `prescriptiondetails` VALUES (1,1,1,4,'2 lần/ngày','Sau bữa ăn',NULL,NULL),(2,1,5,2,'1 lần/ngày','Trước khi ngủ',NULL,NULL),(3,2,5,9,'3 lần/ngày','Sau bữa ăn',NULL,NULL),(4,2,8,6,'2 lần/ngày','Trước khi ăn',NULL,NULL),(5,2,10,3,'1 lần/ngày','Sau khi ăn bữa trưa',NULL,NULL),(12,3,1,6,'Uống 1 viên x 2 lần/ngày','',1,2),(13,3,2,9,'Uống 1 viên x 3 lần/ngày','',0,3),(14,4,1,8,'Uống 1 viên x 2 lần/ngày','',1,2),(15,4,2,12,'Uống 1 viên x 3 lần/ngày','',0,3);
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
  `prescription_date` datetime(6) NOT NULL,
  `total_days` int DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `inpatient_record_id` (`inpatient_record_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `prescriptions_ibfk_4_idx` (`pharmacist_id`),
  CONSTRAINT `prescriptions_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `prescriptions_ibfk_2` FOREIGN KEY (`inpatient_record_id`) REFERENCES `inpatient_records` (`id`),
  CONSTRAINT `prescriptions_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `prescriptions_ibfk_4` FOREIGN KEY (`pharmacist_id`) REFERENCES `pharmacy_staff` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES (1,1,NULL,1,1,'NEW','2025-05-24 00:00:00.000000',NULL,NULL),(2,2,NULL,1,1,'NEW','2025-05-24 00:00:00.000000',NULL,NULL),(3,4,NULL,4,NULL,'CANCELED','2025-10-30 09:23:15.094107',3,'PRE3'),(4,4,NULL,4,NULL,'PAID','2025-10-31 04:49:57.983889',4,'PRE4');
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
  `inpatient_record_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `examination_id` int DEFAULT NULL,
  `result` text,
  `status` varchar(255) DEFAULT NULL,
  `requested_date` datetime(6) DEFAULT NULL,
  `result_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `record_id` (`record_id`),
  KEY `inpatient_record_id` (`inpatient_record_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `examination_id` (`examination_id`),
  CONSTRAINT `resultexamination_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `medical_records` (`id`),
  CONSTRAINT `resultexamination_ibfk_2` FOREIGN KEY (`inpatient_record_id`) REFERENCES `inpatient_records` (`id`),
  CONSTRAINT `resultexamination_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `resultexamination_ibfk_4` FOREIGN KEY (`examination_id`) REFERENCES `medicalexamination` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resultexamination`
--

LOCK TABLES `resultexamination` WRITE;
/*!40000 ALTER TABLE `resultexamination` DISABLE KEYS */;
INSERT INTO `resultexamination` VALUES (4,4,NULL,4,2,NULL,'PAID','2025-10-27 09:35:53.888805',NULL),(5,6,NULL,6,1,NULL,'IN_PROGRESS','2025-11-01 04:33:55.806456',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,1,1,'Phòng A1','AVAILABLE',NULL,'A1',1),(2,1,1,'Phòng A2','AVAILABLE',NULL,'A2',1),(3,2,1,'Phòng B1','AVAILABLE',NULL,'B1',1),(4,2,1,'Phòng B2','AVAILABLE',NULL,'B2',1),(5,8,4,'Phòng xét nghiệm 1','AVAILABLE',NULL,'BT1',4),(6,1,1,'Phòng A3','AVAILABLE',NULL,'A3',1),(7,1,1,'Phòng A4','AVAILABLE',NULL,'A4',1),(8,1,1,'Phòng A5','AVAILABLE',NULL,'A5',1),(9,1,1,'Phòng A6','AVAILABLE',NULL,'A6',1),(10,2,1,'Phòng B3','AVAILABLE',NULL,'B3',1),(11,2,1,'Phòng B4','AVAILABLE',NULL,'B4',1),(12,2,1,'Phòng B5','AVAILABLE',NULL,'B5',1),(13,2,1,'Phòng B6','AVAILABLE',NULL,'B6',1),(14,2,1,'Phòng B7','AVAILABLE',NULL,'B7',1),(15,3,1,'Phòng C1','AVAILABLE',NULL,'C1',1),(16,3,1,'Phòng C2','AVAILABLE',NULL,'C2',1),(17,2,2,'Phòng A22','AVAILABLE',NULL,'A22',1),(18,2,2,'Phòng A23','AVAILABLE','Phòng chăm sóc bệnh nhân nội trú','A23',1),(19,1,1,'Phòng 106','OCCUPIED','','106',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=196 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_slots`
--

LOCK TABLES `schedule_slots` WRITE;
/*!40000 ALTER TABLE `schedule_slots` DISABLE KEYS */;
INSERT INTO `schedule_slots` VALUES (1,2,'08:00:00','08:20:00',0),(2,2,'08:20:00','08:40:00',0),(3,2,'08:40:00','09:00:00',1),(4,2,'09:00:00','09:20:00',0),(5,2,'09:20:00','09:40:00',0),(6,2,'09:40:00','10:00:00',0),(7,2,'10:00:00','10:20:00',0),(8,2,'10:20:00','10:40:00',0),(9,2,'10:40:00','11:00:00',0),(10,2,'11:00:00','11:20:00',0),(11,2,'11:20:00','11:40:00',0),(12,2,'11:40:00','12:00:00',0),(13,152,'18:00:00','18:20:00',0),(14,152,'18:20:00','18:40:00',0),(15,152,'18:40:00','19:00:00',1),(16,152,'19:00:00','19:20:00',0),(17,152,'19:20:00','19:40:00',0),(18,152,'19:40:00','20:00:00',0),(19,152,'20:00:00','20:20:00',0),(20,152,'20:20:00','20:40:00',0),(21,152,'20:40:00','21:00:00',0),(22,253,'08:00:00','08:20:00',0),(23,253,'08:20:00','08:40:00',0),(24,253,'08:40:00','09:00:00',0),(25,253,'09:00:00','09:20:00',0),(26,253,'09:20:00','09:40:00',0),(27,253,'09:40:00','10:00:00',0),(28,253,'10:00:00','10:20:00',1),(29,253,'10:20:00','10:40:00',0),(30,253,'10:40:00','11:00:00',0),(31,253,'11:00:00','11:20:00',0),(32,253,'11:20:00','11:40:00',0),(33,253,'11:40:00','12:00:00',0),(34,316,'18:00:00','18:20:00',0),(35,316,'18:20:00','18:40:00',0),(36,316,'18:40:00','19:00:00',0),(37,316,'19:00:00','19:20:00',1),(38,316,'19:20:00','19:40:00',0),(39,316,'19:40:00','20:00:00',0),(40,316,'20:00:00','20:20:00',0),(41,316,'20:20:00','20:40:00',0),(42,316,'20:40:00','21:00:00',0),(43,315,'13:00:00','13:20:00',0),(44,315,'13:20:00','13:40:00',0),(45,315,'13:40:00','14:00:00',1),(46,315,'14:00:00','14:20:00',0),(47,315,'14:20:00','14:40:00',0),(48,315,'14:40:00','15:00:00',0),(49,315,'15:00:00','15:20:00',0),(50,315,'15:20:00','15:40:00',0),(51,315,'15:40:00','16:00:00',0),(52,315,'16:00:00','16:20:00',0),(53,315,'16:20:00','16:40:00',0),(54,315,'16:40:00','17:00:00',0),(55,306,'08:00:00','08:20:00',0),(56,306,'08:20:00','08:40:00',0),(57,306,'08:40:00','09:00:00',0),(58,306,'09:00:00','09:20:00',0),(59,306,'09:20:00','09:40:00',0),(60,306,'09:40:00','10:00:00',0),(61,306,'10:00:00','10:20:00',1),(62,306,'10:20:00','10:40:00',0),(63,306,'10:40:00','11:00:00',0),(64,306,'11:00:00','11:20:00',0),(65,306,'11:20:00','11:40:00',0),(66,306,'11:40:00','12:00:00',0),(67,3,'13:00:00','13:20:00',1),(68,3,'13:20:00','13:40:00',0),(69,3,'13:40:00','14:00:00',0),(70,3,'14:00:00','14:20:00',0),(71,3,'14:20:00','14:40:00',0),(72,3,'14:40:00','15:00:00',0),(73,3,'15:00:00','15:20:00',0),(74,3,'15:20:00','15:40:00',0),(75,3,'15:40:00','16:00:00',0),(76,3,'16:00:00','16:20:00',0),(77,3,'16:20:00','16:40:00',0),(78,3,'16:40:00','17:00:00',0),(79,317,'08:00:00','08:20:00',0),(80,317,'08:20:00','08:40:00',0),(81,317,'08:40:00','09:00:00',0),(82,317,'09:00:00','09:20:00',0),(83,317,'09:20:00','09:40:00',0),(84,317,'09:40:00','10:00:00',0),(85,317,'10:00:00','10:20:00',0),(86,317,'10:20:00','10:40:00',0),(87,317,'10:40:00','11:00:00',0),(88,317,'11:00:00','11:20:00',0),(89,317,'11:20:00','11:40:00',0),(90,317,'11:40:00','12:00:00',0),(91,318,'08:00:00','08:20:00',0),(92,318,'08:20:00','08:40:00',0),(93,318,'08:40:00','09:00:00',0),(94,318,'09:00:00','09:20:00',0),(95,318,'09:20:00','09:40:00',0),(96,318,'09:40:00','10:00:00',0),(97,318,'10:00:00','10:20:00',0),(98,318,'10:20:00','10:40:00',0),(99,318,'10:40:00','11:00:00',0),(100,318,'11:00:00','11:20:00',0),(101,318,'11:20:00','11:40:00',0),(102,318,'11:40:00','12:00:00',0),(103,319,'08:00:00','08:20:00',0),(104,319,'08:20:00','08:40:00',1),(105,319,'08:40:00','09:00:00',0),(106,319,'09:00:00','09:20:00',0),(107,319,'09:20:00','09:40:00',0),(108,319,'09:40:00','10:00:00',0),(109,319,'10:00:00','10:20:00',0),(110,319,'10:20:00','10:40:00',0),(111,319,'10:40:00','11:00:00',0),(112,319,'11:00:00','11:20:00',0),(113,319,'11:20:00','11:40:00',0),(114,319,'11:40:00','12:00:00',0),(115,320,'13:00:00','13:20:00',0),(116,320,'13:20:00','13:40:00',0),(117,320,'13:40:00','14:00:00',0),(118,320,'14:00:00','14:20:00',0),(119,320,'14:20:00','14:40:00',0),(120,320,'14:40:00','15:00:00',0),(121,320,'15:00:00','15:20:00',0),(122,320,'15:20:00','15:40:00',0),(123,320,'15:40:00','16:00:00',0),(124,320,'16:00:00','16:20:00',0),(125,320,'16:20:00','16:40:00',0),(126,320,'16:40:00','17:00:00',0),(127,321,'13:00:00','13:20:00',0),(128,321,'13:20:00','13:40:00',0),(129,321,'13:40:00','14:00:00',0),(130,321,'14:00:00','14:20:00',0),(131,321,'14:20:00','14:40:00',0),(132,321,'14:40:00','15:00:00',0),(133,321,'15:00:00','15:20:00',0),(134,321,'15:20:00','15:40:00',0),(135,321,'15:40:00','16:00:00',0),(136,321,'16:00:00','16:20:00',0),(137,321,'16:20:00','16:40:00',0),(138,321,'16:40:00','17:00:00',0),(139,322,'13:00:00','13:20:00',0),(140,322,'13:20:00','13:40:00',0),(141,322,'13:40:00','14:00:00',0),(142,322,'14:00:00','14:20:00',0),(143,322,'14:20:00','14:40:00',0),(144,322,'14:40:00','15:00:00',0),(145,322,'15:00:00','15:20:00',0),(146,322,'15:20:00','15:40:00',0),(147,322,'15:40:00','16:00:00',0),(148,322,'16:00:00','16:20:00',0),(149,322,'16:20:00','16:40:00',0),(150,322,'16:40:00','17:00:00',0),(151,323,'18:00:00','18:20:00',0),(152,323,'18:20:00','18:40:00',0),(153,323,'18:40:00','19:00:00',0),(154,323,'19:00:00','19:20:00',0),(155,323,'19:20:00','19:40:00',0),(156,323,'19:40:00','20:00:00',0),(157,323,'20:00:00','20:20:00',0),(158,323,'20:20:00','20:40:00',0),(159,323,'20:40:00','21:00:00',0),(160,324,'08:00:00','08:20:00',0),(161,324,'08:20:00','08:40:00',0),(162,324,'08:40:00','09:00:00',0),(163,324,'09:00:00','09:20:00',0),(164,324,'09:20:00','09:40:00',0),(165,324,'09:40:00','10:00:00',0),(166,324,'10:00:00','10:20:00',0),(167,324,'10:20:00','10:40:00',0),(168,324,'10:40:00','11:00:00',0),(169,324,'11:00:00','11:20:00',0),(170,324,'11:20:00','11:40:00',0),(171,324,'11:40:00','12:00:00',0),(184,326,'08:00:00','08:20:00',0),(185,326,'08:20:00','08:40:00',1),(186,326,'08:40:00','09:00:00',0),(187,326,'09:00:00','09:20:00',0),(188,326,'09:20:00','09:40:00',0),(189,326,'09:40:00','10:00:00',0),(190,326,'10:00:00','10:20:00',0),(191,326,'10:20:00','10:40:00',0),(192,326,'10:40:00','11:00:00',0),(193,326,'11:00:00','11:20:00',0),(194,326,'11:20:00','11:40:00',0),(195,326,'11:40:00','12:00:00',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialty`
--

LOCK TABLES `specialty` WRITE;
/*!40000 ALTER TABLE `specialty` DISABLE KEYS */;
INSERT INTO `specialty` VALUES (1,1,'Tim mạch','Điều trị các bệnh về tim','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//tim_mach.jpg'),(2,1,'Hô hấp','Chuyên điều trị các bệnh liên quan đến phổi','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//ho_hap.jpg'),(3,2,'Ngoại tổng quát','Phẫu thuật cơ bản','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//Ngoai_tong_quat.jpg'),(4,3,'Nhi hô hấp','Chăm sóc bệnh hô hấp cho trẻ','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//tre_em.png'),(6,2,'Chấn thương chỉnh hình','Điều trị các chấn thương xương khớp','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//chan_thuong_chinh_hinh.jpg'),(7,5,'Thai sản','','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//thai_san.jpg'),(8,2,'Răng hàm mặt','','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//rang_ham_mat.jpg'),(9,2,'Da liễu','','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//Da_lieu.jpg'),(10,1,'Nội tiết','Chuyên chẩn đoán và điều trị các bệnh liên quan đến hormone và các tuyến nội tiết như tuyến giáp, tuyến yên, tuyến thượng thận, tuyến tụy (tiểu đường), và rối loạn chuyển hóa.','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/noitiet.jpg');
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (1,1,4,1),(2,1,5,1),(3,1,7,5),(4,1,8,2),(5,5,11,12),(6,1,14,1),(7,2,18,2),(8,3,19,1),(10,4,21,9),(11,7,22,10),(12,3,23,8),(13,3,24,8),(14,3,25,8),(15,3,26,8),(17,1,28,1),(18,6,29,11);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff_schedules`
--

LOCK TABLES `staff_schedules` WRITE;
/*!40000 ALTER TABLE `staff_schedules` DISABLE KEYS */;
INSERT INTO `staff_schedules` VALUES (1,8,1,5,'2025-10-26','ACTIVE'),(2,14,1,5,'2025-10-26','ACTIVE'),(3,15,1,5,'2025-10-26','ACTIVE');
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
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Huỳnh Minh Hoàng','hoanghm4869@gmail.com','0337023824','2003-11-12',0,'TBH TDM','$2a$10$b6E2VmRKJ8pB25uky/vWN.1XnlYvFSOEwK3jtxPchw5nqbLmqQoPC',0,'2025-04-10 00:00:00',NULL),(3,'Nguyễn Văn A','nguyenvanA@example.com','0123456789','1995-05-20',0,'123 Đường Lê Lợi, Tdm, TP HCM','$2a$10$XDLLf84jVbgGduWVA4N/.e2/ZqEVVhZe5d0ljbaxetYsLS.Az55I6',1,'2025-04-10 04:48:35',NULL),(4,'Trần Văn C','tranvanc@hospital.com','0323456709','1985-06-20',0,'123 Lê Lợi, TP.HCM','$2a$10$XiutYothZ4rma/P.J5c2leYOLb/hTj6V677Re5gy5hSWeB1yBe71y',2,'2025-04-10 08:28:32','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1747710399077.png'),(5,'Nguyễn Minh Thuận','Thuannm@example.com','0223456789','1985-06-20',0,'123 Nguyễn Trãi, TP.HCM','$2a$10$o/BAwrm8rlbRUtVKepG8iuhlFxvVMfn6LVhLyaaiLtmdvUAIkGLbC',2,'2025-04-16 08:06:58','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//bs_nam.png'),(7,'Nguyễn Hồng Yến','Yennh@example.com','0423456789','1995-06-20',1,'123 Lý Thái Tổ, TP.HCM','$2a$10$ZRb1sOPqiTR1NgDn7l9ryuU7JbBOT6QnX2.ai.skFTmg/VMVFNu5u',2,'2025-05-18 06:40:23','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//bs_nu.jpg'),(8,'Trần Thu Phương','Phuongtt20@example.com','0523456789','1994-08-20',1,'123 Phan Châu Trinh, TP.HCM','$2a$10$qFTu3C44lJEAz4.AEo/qIOnwihykzMMB5mYTqv0nKcnFePOhvBtxK',2,'2025-05-18 07:36:34','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital//bs_nu.jpg'),(9,'Nguyễn Việt Trung','Truntnv@gmail.com','0336013824','2003-06-25',0,'Phú Hòa','$2a$10$kvsD41NR8tMs6ZFfth6wI.ksdSKpySwLVYW2Y/CT/0KoryGjrzxCq',1,'2025-05-19 00:13:09',''),(10,'Nguyễn Thanh Nam','Namnt@gmail.com','0723456789','2002-05-23',0,'Củ Chi','$2a$10$WYEA1.VFcVZYAR1tAqY8vO/Sr/C1Y3bVp6TDVwyKAbQIfPcTuBCQm',1,'2025-05-23 17:17:39',''),(11,'Hồ Ngọc Châu','Chaunh@gmail.com','0823456789','2000-06-24',1,'TP HCM','$2a$10$XDLLf84jVbgGduWVA4N/.e2/ZqEVVhZe5d0ljbaxetYsLS.Az55I6',2,'2025-05-24 16:47:19',NULL),(12,'Nguyễn Ngọc Hà','hann@gmail.com','038023564','2003-11-25',1,'phường Thủ Dầu Một','$2a$10$jtuaGLERzhkK9ScR63RZPO1VLcFgTratjYAmWhWZXz1.PHKX2p3XK',1,'2025-08-21 09:18:07',NULL),(13,'Nguyễn Thanh Thảo','thaont@gmail.com','0320156487','2002-05-14',1,'phường Chánh Hiệp','$2a$10$Ru49ve58Uu1O0CXhf0.Lqu48tpwLWf32xZVJ32FS9VL91up0IulK6',1,'2025-08-21 16:05:53','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1755825874773.jpg'),(14,'Trần Văn C','Thuann1m@example.com','0323456789','1985-06-20',0,'123 Lê Lợi, TP.HCM','$2a$10$iwcOFEhX3qKwdYfG/kQ6QuPxQUokKlxOENmeSJ/VYgu4918xbGLhq',2,'2025-08-22 01:23:35','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1755825850325.jpg'),(15,'Nguyễn Thanh Hằng','hangnt@gmail.com','0320156487','2002-05-14',1,'phường Chánh Hiệp','$2a$10$v4mss5YDekozGY9xkvRQIe/FdEjz8uFNx1U9u84TzjQkASAvkUdpO',1,'2025-08-22 01:26:01',NULL),(16,'Nguyễn Thanh Thu','nguyenvan123@example.com','0320156487','2002-05-14',0,'phường Chánh Hiệp','$2a$10$.cnH7Hx/jTUaCll79EDPk.q5zlRJ1ntCUmGtV9sQSULT8ybVYnCH6',1,'2025-08-22 01:26:52',NULL),(17,'Nguyễn Thanh Thuỷ','thuynt@gmail.com','0320156487','2002-05-14',1,'phường Chánh Hiệp','$2a$10$4iEmWwD6Ju6U/vamg5MbMOjWERSQ2PHCPA67uCq42eqQlKDxYlrtm',1,'2025-08-22 13:10:33',''),(18,'Nguyễn Thị Ngân','ngannt@gmail.com','0337033824','2000-09-28',0,'Chánh Hiệp','$2a$10$aOyLpSBFMRaj1n8MRk2.EOma7w1gsf/gIjKB4aMFkRd4Te./hMaAm',2,'2025-09-28 17:36:54','string'),(19,'Nguyễn Tiến Duy','duynt@gmail.com','0337043824','1999-10-28',1,'Phú Lợi','$2a$10$ixigocA1JDl.IF/Nu7vb4eh6.JAdWX7eZWGfX/J6C.8oDt5YPGtRO',2,'2025-09-28 17:39:05',''),(21,'Trần Quốc Hưng','hungtq@gmail.com','0335021425','1998-10-24',0,'Chánh Hiệp','$2a$10$OqWuoiRwDumfhosnXeY89eQtU1aYl0KRh3YFIOENknGbMQoHL1mVe',2,'2025-10-24 15:36:57',NULL),(22,'Nguyễn Ngọc Hiền','hiennn@gmail.com','0331052674','1998-10-26',1,'Phú Thọ','$2a$10$TiRHyAvjV776S5vTqHkS5.P0MPdB53nPfCNRvdaWIyZjMl4SdYYRy',2,'2025-10-26 03:13:19',''),(23,'Lưu Đức Thịnh','thinhld@gmail.com','0334013754','2000-09-26',0,'Phường Sài Gòn','$2a$10$4UMopa1Mav7yU/cKqs77AuL4CNwQITdaxT/vlBPCODRnEbM5vrfQe',2,'2025-10-26 04:02:22',''),(24,'Trần Thanh Yến','yentt@gmail.com','0322063455','1999-09-02',1,'Phường Thuận An','$2a$10$vtO/28Wsx0xIl5J9c8Ev3ORYuv2.hDR4fnhUrCU34ss9Si0.zrYJW',2,'2025-10-26 04:03:47',''),(25,'Nguyễn Thanh Hoa','hoant@gmail.com','0935451060','1998-07-19',1,'Phường Thuận An','$2a$10$xpNbX//Nwwt/DQkBeJ9uOeFlH.tEJcEWdcVU4SVuhYjimpMhnHgw2',2,'2025-10-26 04:04:58',''),(26,'Nguyễn Thanh Phúc','phucnt@gmail.com','0925522406','1998-10-19',1,'Phường Hiệp An','$2a$10$uKMUKGbqsZKZkRl75cKskutuR6UmPG9Qh48.wQ6bg9b5lY8yihn7e',2,'2025-10-26 04:05:50',''),(28,'Nguyễn Thanh Hải','haint@gmail.com','0337062145','1992-04-23',0,'phường Phú Thọ','$2a$10$bU0L3VQY0654v7JueeFlOeSOE4G.MdjPaIEDGKpIO0k/1ffKVHr3q',2,'2025-10-31 16:23:13','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/user-avatars/1761927791946.jpeg'),(29,'Nguyễn Thanh Liên','liennt@gmail.com','0335021454','1997-12-23',1,'Phường Phú Thọ','$2a$10$TB9nGpI82SaGLnXIwXHpH.qkCJ6lv7A0cTCR49EvAXEqSwa16Kvya',2,'2025-11-01 04:55:33','https://jzfjnxlskhghjjsvecqj.supabase.co/storage/v1/object/public/hospital/cashier1.jpeg');
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

-- Dump completed on 2025-11-03 23:03:46
