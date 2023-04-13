-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: preupmoney
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `auth_data`
--

DROP TABLE IF EXISTS `auth_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_data` (
  `id_auth` int NOT NULL,
  `phone_number_auth` varchar(12) NOT NULL,
  `password_auth` varchar(32) NOT NULL,
  `pin_code` varchar(4) NOT NULL,
  PRIMARY KEY (`id_auth`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_data`
--

LOCK TABLES `auth_data` WRITE;
/*!40000 ALTER TABLE `auth_data` DISABLE KEYS */;
INSERT INTO `auth_data` VALUES (1,'+79223209959','1234','1234'),(2,'+79129853420','12345','4321');
/*!40000 ALTER TABLE `auth_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank_account`
--

DROP TABLE IF EXISTS `bank_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank_account` (
  `id_client` int NOT NULL,
  `id_company` int NOT NULL,
  `id_bank_account` int NOT NULL,
  `date_of_open` date NOT NULL,
  `status_of_account` varchar(20) NOT NULL,
  `tariff` varchar(20) NOT NULL,
  PRIMARY KEY (`id_bank_account`),
  KEY `fk_id_client_bank_account` (`id_client`),
  KEY `fk_id_company_bank_account` (`id_company`),
  CONSTRAINT `fk_id_client_bank_account` FOREIGN KEY (`id_client`) REFERENCES `clients` (`id_client`),
  CONSTRAINT `fk_id_company_bank_account` FOREIGN KEY (`id_company`) REFERENCES `company` (`id_company`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank_account`
--

LOCK TABLES `bank_account` WRITE;
/*!40000 ALTER TABLE `bank_account` DISABLE KEYS */;
INSERT INTO `bank_account` VALUES (1,1,1,'2022-01-01','Открыт','BlueCard'),(2,1,2,'2022-01-01','Открыт','BlueCard');
/*!40000 ALTER TABLE `bank_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank_requs`
--

DROP TABLE IF EXISTS `bank_requs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank_requs` (
  `id_bank_account` int NOT NULL,
  `bik` int NOT NULL,
  `inn` int NOT NULL,
  `kpp` int NOT NULL,
  `corp_account` int NOT NULL,
  KEY `fk_id_bank_account_requs` (`id_bank_account`),
  CONSTRAINT `fk_id_bank_account_requs` FOREIGN KEY (`id_bank_account`) REFERENCES `bank_account` (`id_bank_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank_requs`
--

LOCK TABLES `bank_requs` WRITE;
/*!40000 ALTER TABLE `bank_requs` DISABLE KEYS */;
INSERT INTO `bank_requs` VALUES (1,1,1,1,1),(2,2,2,2,2);
/*!40000 ALTER TABLE `bank_requs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clients` (
  `id_client` int NOT NULL,
  `FIO` varchar(50) NOT NULL,
  `gender` varchar(10) NOT NULL,
  `passport_data` varchar(50) NOT NULL,
  `address` varchar(50) NOT NULL,
  `id_auth` int NOT NULL,
  PRIMARY KEY (`id_client`),
  KEY `fk_auth_idx` (`id_auth`),
  CONSTRAINT `fk_auth` FOREIGN KEY (`id_auth`) REFERENCES `auth_data` (`id_auth`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
INSERT INTO `clients` VALUES (1,'Иванов Артём Викторович','Мужской','0000 00000','г. Пермь, ул. Чернышевского, д.28',1),(2,'Иванов Артём Викторович','Женский','0000 00000','г. Пермь, ул. Чернышевского, д.28',2);
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `id_company` int NOT NULL,
  `name_company` varchar(20) NOT NULL,
  `address` varchar(100) NOT NULL,
  `license` varchar(50) NOT NULL,
  `phone` int NOT NULL,
  PRIMARY KEY (`id_company`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (1,'ПАО \'Преапмани\'','г. Пермь, ул. Чернышевского, д.28','здесь будет ченить',123123123);
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consult`
--

DROP TABLE IF EXISTS `consult`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consult` (
  `id_client` int NOT NULL,
  `id_worker` int NOT NULL,
  `id_consult` int NOT NULL,
  `date_consult` date NOT NULL,
  `type_consult` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`id_consult`),
  KEY `fk_id_worker_consult` (`id_worker`),
  KEY `fk_id_client_consult` (`id_client`),
  CONSTRAINT `fk_id_client_consult` FOREIGN KEY (`id_client`) REFERENCES `clients` (`id_client`),
  CONSTRAINT `fk_id_worker_consult` FOREIGN KEY (`id_worker`) REFERENCES `workers` (`id_worker`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consult`
--

LOCK TABLES `consult` WRITE;
/*!40000 ALTER TABLE `consult` DISABLE KEYS */;
/*!40000 ALTER TABLE `consult` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workers`
--

DROP TABLE IF EXISTS `workers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `workers` (
  `id_company` int NOT NULL,
  `id_worker` int NOT NULL,
  `FIO` varchar(50) NOT NULL,
  `post` varchar(50) NOT NULL,
  `phone` int NOT NULL,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`id_worker`),
  KEY `fk_id_company` (`id_company`),
  CONSTRAINT `fk_id_company` FOREIGN KEY (`id_company`) REFERENCES `company` (`id_company`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workers`
--

LOCK TABLES `workers` WRITE;
/*!40000 ALTER TABLE `workers` DISABLE KEYS */;
/*!40000 ALTER TABLE `workers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-11 15:48:50
