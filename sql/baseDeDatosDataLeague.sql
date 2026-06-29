-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: dataleague
-- ------------------------------------------------------
-- Server version	9.4.0

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
-- Table structure for table `carnets`
--

DROP TABLE IF EXISTS `carnets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carnets` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha_emision` date NOT NULL,
  `fecha_vencimiento` date NOT NULL,
  `fecha_limite_renovacion` date NOT NULL DEFAULT '2000-01-01',
  `estado` tinyint(1) NOT NULL DEFAULT '1',
  `ruta_foto_jugador` varchar(255) DEFAULT NULL,
  `jugador_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `jugador_id` (`jugador_id`),
  CONSTRAINT `carnets_ibfk_1` FOREIGN KEY (`jugador_id`) REFERENCES `jugadores` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carnets`
--

LOCK TABLES `carnets` WRITE;
/*!40000 ALTER TABLE `carnets` DISABLE KEYS */;
INSERT INTO `carnets` VALUES (1,'2026-06-27','2027-06-14','2027-08-27',1,NULL,1),(2,'2026-06-27','2027-06-14','2027-08-27',1,NULL,2),(3,'2026-06-27','2027-06-14','2027-08-27',1,NULL,3),(4,'2026-06-27','2027-06-14','2027-08-27',1,NULL,4),(5,'2026-06-27','2027-06-14','2027-08-27',1,NULL,5),(6,'2026-06-27','2027-06-14','2027-08-27',1,NULL,6),(8,'2026-06-28','2027-06-14','2027-08-27',1,NULL,8),(9,'2026-06-28','2027-06-14','2027-08-27',1,NULL,9),(10,'2026-06-28','2027-06-14','2027-08-27',1,NULL,10),(11,'2026-06-28','2027-06-14','2027-08-27',1,NULL,11),(12,'2026-06-28','2027-06-14','2027-08-27',1,NULL,12),(13,'2026-06-28','2027-06-14','2027-08-27',1,NULL,13),(14,'2026-06-28','2027-06-14','2027-08-27',1,NULL,14),(15,'2026-06-28','2027-06-14','2027-08-27',1,NULL,15),(16,'2026-06-28','2027-06-14','2027-08-27',1,NULL,16),(17,'2026-06-28','2027-06-14','2027-08-27',1,NULL,23),(18,'2026-06-28','2027-06-14','2027-08-27',1,NULL,24),(19,'2026-06-28','2027-06-14','2027-08-27',1,NULL,25),(20,'2026-06-28','2027-06-14','2027-08-27',1,NULL,26),(21,'2026-06-28','2027-06-14','2027-08-27',1,NULL,27),(22,'2026-06-28','2027-06-14','2027-08-27',1,NULL,28),(23,'2026-06-28','2027-06-14','2027-08-27',1,NULL,29),(24,'2026-06-28','2027-06-14','2027-08-27',1,NULL,30),(25,'2026-06-28','2027-06-14','2027-08-27',1,NULL,31),(39,'2026-06-28','2026-12-14','2027-02-26',1,NULL,38),(41,'2026-06-28','2026-12-14','2027-02-26',1,NULL,42),(42,'2026-06-28','2026-12-14','2027-02-26',1,NULL,43),(43,'2026-06-28','2026-12-14','2027-02-26',1,NULL,48),(44,'2026-06-28','2026-12-14','2027-02-26',1,NULL,49);
/*!40000 ALTER TABLE `carnets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id` int NOT NULL AUTO_INCREMENT,
  `division` varchar(50) NOT NULL,
  `cantidad_jugadores` int NOT NULL DEFAULT '0',
  `edad_minima` int DEFAULT NULL,
  `edad_maxima` int DEFAULT NULL,
  `club_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `club_id` (`club_id`),
  CONSTRAINT `categorias_ibfk_1` FOREIGN KEY (`club_id`) REFERENCES `clubes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=256 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,'Sub-5',28,5,5,1),(2,'Sub-5',28,5,5,2),(3,'Sub-5',28,5,5,3),(4,'Sub-5',28,5,5,4),(5,'Sub-5',28,5,5,5),(6,'Sub-5',28,5,5,6),(7,'Sub-5',28,5,5,7),(8,'Sub-5',28,5,5,8),(9,'Sub-5',28,5,5,9),(10,'Sub-5',28,5,5,10),(11,'Sub-5',28,5,5,11),(12,'Sub-5',28,5,5,12),(13,'Sub-5',28,5,5,13),(14,'Sub-5',28,5,5,14),(15,'Sub-5',28,5,5,15),(16,'Sub-6',28,6,6,1),(17,'Sub-6',28,6,6,2),(18,'Sub-6',28,6,6,3),(19,'Sub-6',28,6,6,4),(20,'Sub-6',28,6,6,5),(21,'Sub-6',28,6,6,6),(22,'Sub-6',28,6,6,7),(23,'Sub-6',28,6,6,8),(24,'Sub-6',28,6,6,9),(25,'Sub-6',28,6,6,10),(26,'Sub-6',28,6,6,11),(27,'Sub-6',28,6,6,12),(28,'Sub-6',28,6,6,13),(29,'Sub-6',28,6,6,14),(30,'Sub-6',28,6,6,15),(31,'Sub-7',28,7,7,1),(32,'Sub-7',28,7,7,2),(33,'Sub-7',28,7,7,3),(34,'Sub-7',28,7,7,4),(35,'Sub-7',28,7,7,5),(36,'Sub-7',28,7,7,6),(37,'Sub-7',28,7,7,7),(38,'Sub-7',28,7,7,8),(39,'Sub-7',28,7,7,9),(40,'Sub-7',28,7,7,10),(41,'Sub-7',28,7,7,11),(42,'Sub-7',28,7,7,12),(43,'Sub-7',28,7,7,13),(44,'Sub-7',28,7,7,14),(45,'Sub-7',28,7,7,15),(46,'Sub-8',28,8,8,1),(47,'Sub-8',28,8,8,2),(48,'Sub-8',28,8,8,3),(49,'Sub-8',28,8,8,4),(50,'Sub-8',28,8,8,5),(51,'Sub-8',28,8,8,6),(52,'Sub-8',28,8,8,7),(53,'Sub-8',28,8,8,8),(54,'Sub-8',28,8,8,9),(55,'Sub-8',28,8,8,10),(56,'Sub-8',28,8,8,11),(57,'Sub-8',28,8,8,12),(58,'Sub-8',28,8,8,13),(59,'Sub-8',28,8,8,14),(60,'Sub-8',28,8,8,15),(61,'Sub-9',28,9,9,1),(62,'Sub-9',28,9,9,2),(63,'Sub-9',28,9,9,3),(64,'Sub-9',28,9,9,4),(65,'Sub-9',28,9,9,5),(66,'Sub-9',28,9,9,6),(67,'Sub-9',28,9,9,7),(68,'Sub-9',28,9,9,8),(69,'Sub-9',28,9,9,9),(70,'Sub-9',28,9,9,10),(71,'Sub-9',28,9,9,11),(72,'Sub-9',28,9,9,12),(73,'Sub-9',28,9,9,13),(74,'Sub-9',28,9,9,14),(75,'Sub-9',28,9,9,15),(76,'Sub-10',28,10,10,1),(77,'Sub-10',28,10,10,2),(78,'Sub-10',28,10,10,3),(79,'Sub-10',28,10,10,4),(80,'Sub-10',28,10,10,5),(81,'Sub-10',28,10,10,6),(82,'Sub-10',28,10,10,7),(83,'Sub-10',28,10,10,8),(84,'Sub-10',28,10,10,9),(85,'Sub-10',28,10,10,10),(86,'Sub-10',28,10,10,11),(87,'Sub-10',28,10,10,12),(88,'Sub-10',28,10,10,13),(89,'Sub-10',28,10,10,14),(90,'Sub-10',28,10,10,15),(91,'Sub-11',28,11,11,1),(92,'Sub-11',28,11,11,2),(93,'Sub-11',28,11,11,3),(94,'Sub-11',28,11,11,4),(95,'Sub-11',28,11,11,5),(96,'Sub-11',28,11,11,6),(97,'Sub-11',28,11,11,7),(98,'Sub-11',28,11,11,8),(99,'Sub-11',28,11,11,9),(100,'Sub-11',28,11,11,10),(101,'Sub-11',28,11,11,11),(102,'Sub-11',28,11,11,12),(103,'Sub-11',28,11,11,13),(104,'Sub-11',28,11,11,14),(105,'Sub-11',28,11,11,15),(106,'Sub-12',28,12,12,1),(107,'Sub-12',28,12,12,2),(108,'Sub-12',28,12,12,3),(109,'Sub-12',28,12,12,4),(110,'Sub-12',28,12,12,5),(111,'Sub-12',28,12,12,6),(112,'Sub-12',28,12,12,7),(113,'Sub-12',28,12,12,8),(114,'Sub-12',28,12,12,9),(115,'Sub-12',28,12,12,10),(116,'Sub-12',28,12,12,11),(117,'Sub-12',28,12,12,12),(118,'Sub-12',28,12,12,13),(119,'Sub-12',28,12,12,14),(120,'Sub-12',28,12,12,15),(121,'Sub-13',28,13,13,1),(122,'Sub-13',28,13,13,2),(123,'Sub-13',28,13,13,3),(124,'Sub-13',28,13,13,4),(125,'Sub-13',28,13,13,5),(126,'Sub-13',28,13,13,6),(127,'Sub-13',28,13,13,7),(128,'Sub-13',28,13,13,8),(129,'Sub-13',28,13,13,9),(130,'Sub-13',28,13,13,10),(131,'Sub-13',28,13,13,11),(132,'Sub-13',28,13,13,12),(133,'Sub-13',28,13,13,13),(134,'Sub-13',28,13,13,14),(135,'Sub-13',28,13,13,15),(136,'Sub-14',28,14,14,1),(137,'Sub-14',28,14,14,2),(138,'Sub-14',28,14,14,3),(139,'Sub-14',28,14,14,4),(140,'Sub-14',28,14,14,5),(141,'Sub-14',28,14,14,6),(142,'Sub-14',28,14,14,7),(143,'Sub-14',28,14,14,8),(144,'Sub-14',28,14,14,9),(145,'Sub-14',28,14,14,10),(146,'Sub-14',28,14,14,11),(147,'Sub-14',28,14,14,12),(148,'Sub-14',28,14,14,13),(149,'Sub-14',28,14,14,14),(150,'Sub-14',28,14,14,15),(151,'5ta division',28,15,16,1),(152,'5ta division',28,15,16,2),(153,'5ta division',28,15,16,3),(154,'5ta division',28,15,16,4),(155,'5ta division',28,15,16,5),(156,'5ta division',28,15,16,6),(157,'5ta division',28,15,16,7),(158,'5ta division',28,15,16,8),(159,'5ta division',28,15,16,9),(160,'5ta division',28,15,16,10),(161,'5ta division',28,15,16,11),(162,'5ta division',28,15,16,12),(163,'5ta division',28,15,16,13),(164,'5ta division',28,15,16,14),(165,'5ta division',28,15,16,15),(166,'4ta division',28,17,18,1),(167,'4ta division',28,17,18,2),(168,'4ta division',28,17,18,3),(169,'4ta division',28,17,18,4),(170,'4ta division',28,17,18,5),(171,'4ta division',28,17,18,6),(172,'4ta division',28,17,18,7),(173,'4ta division',28,17,18,8),(174,'4ta division',28,17,18,9),(175,'4ta division',28,17,18,10),(176,'4ta division',28,17,18,11),(177,'4ta division',28,17,18,12),(178,'4ta division',28,17,18,13),(179,'4ta division',28,17,18,14),(180,'4ta division',28,17,18,15),(181,'Primera division',28,19,29,1),(182,'Primera division',28,19,29,2),(183,'Primera division',28,19,29,3),(184,'Primera division',28,19,29,4),(185,'Primera division',28,19,29,5),(186,'Primera division',28,19,29,6),(187,'Primera division',28,19,29,7),(188,'Primera division',28,19,29,8),(189,'Primera division',28,19,29,9),(190,'Primera division',28,19,29,10),(191,'Primera division',28,19,29,11),(192,'Primera division',28,19,29,12),(193,'Primera division',28,19,29,13),(194,'Primera division',28,19,29,14),(195,'Primera division',28,19,29,15),(196,'Veteranos',28,30,60,1),(197,'Veteranos',28,30,60,2),(198,'Veteranos',28,30,60,3),(199,'Veteranos',28,30,60,4),(200,'Veteranos',28,30,60,5),(201,'Veteranos',28,30,60,6),(202,'Veteranos',28,30,60,7),(203,'Veteranos',28,30,60,8),(204,'Veteranos',28,30,60,9),(205,'Veteranos',28,30,60,10),(206,'Veteranos',28,30,60,11),(207,'Veteranos',28,30,60,12),(208,'Veteranos',28,30,60,13),(209,'Veteranos',28,30,60,14),(210,'Veteranos',28,30,60,15);
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clubes`
--

DROP TABLE IF EXISTS `clubes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clubes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `estadio_nombre` varchar(100) DEFAULT NULL,
  `direccion_estadio` varchar(200) DEFAULT NULL,
  `escudo_ruta` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clubes`
--

LOCK TABLES `clubes` WRITE;
/*!40000 ALTER TABLE `clubes` DISABLE KEYS */;
INSERT INTO `clubes` VALUES (1,'Atlético Salta','Estadio Central','Av. Principal 123','Imagenes/escudos/atletico.png'),(2,'Santa Rita Sur','Estadio Santa Rita Sur','Av. Sur 450','Imagenes/escudos/santa_rita_sur.png'),(3,'Deportivo Soledad','Estadio Soledad','Calle Soledad 120','Imagenes/escudos/deportivo_soledad.png'),(4,'Defensores Unidos','Estadio Defensores','Av. Belgrano 800','Imagenes/escudos/defensores_unidos.png'),(5,'Club Sportivo San Jorge','Estadio San Jorge','Calle San Jorge 33','Imagenes/escudos/sportivo_san_jorge.png'),(6,'Club Sportivo Sociedad Municipal','Estadio Municipal','Av. Municipal 200','Imagenes/escudos/sportivo_municipal.png'),(7,'Club Atlético La Loma','Estadio La Loma','Calle La Loma 75','Imagenes/escudos/atletico_la_loma.png'),(8,'Club Sportivo Calvimonte','Estadio Calvimonte','Ruta 9 Km 12','Imagenes/escudos/sportivo_calvimonte.png'),(9,'Santa Rita Norte','Estadio Santa Rita Norte','Av. Norte 310','Imagenes/escudos/santa_rita_norte.png'),(10,'Club Deportivo General Güemes','Estadio General Güemes','Av. Güemes 600','Imagenes/escudos/general_guemes.png'),(11,'Santa Rita Unida','Estadio Santa Rita Unida','Calle Unión 90','Imagenes/escudos/santa_rita_unida.png'),(12,'Deportivo San Antonio','Estadio San Antonio','Calle San Antonio 410','Imagenes/escudos/deportivo_san_antonio.png'),(13,'Club Juventud Unida Las Moras','Estadio Las Moras','Av. Las Moras 220','Imagenes/escudos/juventud_las_moras.png'),(14,'Club Atlético Zona Cero','Estadio Zona Cero','Ruta 51 Km 3','Imagenes/escudos/atletico_zona_cero.png'),(15,'Club Sportivo San Francisco','Estadio San Francisco','Calle San Francisco 55','Imagenes/escudos/sportivo_san_francisco.png');
/*!40000 ALTER TABLE `clubes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historial_clubes_jugador`
--

DROP TABLE IF EXISTS `historial_clubes_jugador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historial_clubes_jugador` (
  `jugador_id` int NOT NULL,
  `club_id` int NOT NULL,
  PRIMARY KEY (`jugador_id`,`club_id`),
  KEY `club_id` (`club_id`),
  CONSTRAINT `historial_clubes_jugador_ibfk_1` FOREIGN KEY (`jugador_id`) REFERENCES `jugadores` (`id`) ON DELETE CASCADE,
  CONSTRAINT `historial_clubes_jugador_ibfk_2` FOREIGN KEY (`club_id`) REFERENCES `clubes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historial_clubes_jugador`
--

LOCK TABLES `historial_clubes_jugador` WRITE;
/*!40000 ALTER TABLE `historial_clubes_jugador` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial_clubes_jugador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historial_partidos_jugador`
--

DROP TABLE IF EXISTS `historial_partidos_jugador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historial_partidos_jugador` (
  `id` int NOT NULL AUTO_INCREMENT,
  `jugador_id` int NOT NULL,
  `partido_info` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `jugador_id` (`jugador_id`),
  CONSTRAINT `historial_partidos_jugador_ibfk_1` FOREIGN KEY (`jugador_id`) REFERENCES `jugadores` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historial_partidos_jugador`
--

LOCK TABLES `historial_partidos_jugador` WRITE;
/*!40000 ALTER TABLE `historial_partidos_jugador` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial_partidos_jugador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jugadores`
--

DROP TABLE IF EXISTS `jugadores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jugadores` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `dni` varchar(10) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `domicilio` varchar(150) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `gmail` varchar(100) DEFAULT NULL,
  `ya_jugo` tinyint(1) NOT NULL DEFAULT '0',
  `carnet_vencido` tinyint(1) NOT NULL DEFAULT '0',
  `club_id` int NOT NULL,
  `categoria_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dni` (`dni`),
  UNIQUE KEY `gmail` (`gmail`),
  KEY `club_id` (`club_id`),
  KEY `categoria_id` (`categoria_id`),
  CONSTRAINT `jugadores_ibfk_1` FOREIGN KEY (`club_id`) REFERENCES `clubes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `jugadores_ibfk_2` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jugadores`
--

LOCK TABLES `jugadores` WRITE;
/*!40000 ALTER TABLE `jugadores` DISABLE KEYS */;
INSERT INTO `jugadores` VALUES (1,'Ousmane','Dembele','37894238','2017-06-13','Calle Falsa 123','3871234567','ousmane.dembele@gmail.com',0,0,1,61),(2,'Lautaro','Gimenez','46123890','2010-03-22','Av. Sur 88','3877654321','lautaro.gimenez@gmail.com',0,0,2,152),(3,'Mateo','Acosta','44567123','2008-01-15','Calle Soledad 45','3879988776','mateo.acosta@gmail.com',0,0,3,168),(4,'Franco','Rodriguez','40123456','2002-09-05','Av. Belgrano 200','3871112233','franco.rodriguez@gmail.com',0,0,4,184),(5,'Ricardo','Fernandez','28456789','1991-11-30','Calle San Jorge 10','3873334455','ricardo.fernandez@gmail.com',0,0,5,200),(6,'Benjamin','Torres','48901234','2014-04-18','Calle La Loma 5','3875556677','benjamin.torres@gmail.com',0,0,7,112),(7,'Osumane','Dembele','34093123','2012-06-08','Avenida Belgrano Casa N°12','3875467890','dembele@gmail.com',0,0,1,136),(8,'Nicolas','Paz','41001001','2003-02-10','Calle 1','3870000001','nicolas.paz@gmail.com',0,0,1,181),(9,'Emiliano','Vega','41001002','2002-05-14','Calle 2','3870000002','emiliano.vega@gmail.com',0,0,1,181),(10,'Tomas','Romero','41001003','2004-08-22','Calle 3','3870000003','tomas.romero@gmail.com',0,0,1,181),(11,'Agustin','Diaz','41001004','2001-11-30','Calle 4','3870000004','agustin.diaz@gmail.com',0,0,1,181),(12,'Bruno','Suarez','41001005','2005-01-18','Calle 5','3870000005','bruno.suarez@gmail.com',0,0,1,181),(13,'Ezequiel','Castro','41001006','2000-03-25','Calle 6','3870000006','ezequiel.castro@gmail.com',0,0,1,181),(14,'Ivan','Molina','41001007','2003-07-09','Calle 7','3870000007','ivan.molina@gmail.com',0,0,1,181),(15,'Gonzalo','Herrera','41001008','2002-09-12','Calle 8','3870000008','gonzalo.herrera@gmail.com',0,0,1,181),(16,'Federico','Ortiz','41001009','2004-12-05','Calle 9','3870000009','federico.ortiz@gmail.com',0,0,1,181),(23,'Matias','Lopez','41002001','2003-04-11','Av. Sur 1','3870000011','matias.lopez@gmail.com',0,0,2,182),(24,'Joaquin','Garcia','41002002','2002-06-17','Av. Sur 2','3870000012','joaquin.garcia@gmail.com',0,0,2,182),(25,'Lucas','Martinez','41002003','2001-08-29','Av. Sur 3','3870000013','lucas.martinez@gmail.com',0,0,2,182),(26,'Santiago','Flores','41002004','2005-02-02','Av. Sur 4','3870000014','santiago.flores@gmail.com',0,0,2,182),(27,'Maximiliano','Sosa','41002005','2000-10-19','Av. Sur 5','3870000015','maximiliano.sosa@gmail.com',0,0,2,182),(28,'Cristian','Aguirre','41002006','2004-03-23','Av. Sur 6','3870000016','cristian.aguirre@gmail.com',0,0,2,182),(29,'Diego','Ramos','41002007','2003-05-07','Av. Sur 7','3870000017','diego.ramos@gmail.com',0,0,2,182),(30,'Pablo','Medina','41002008','2002-12-15','Av. Sur 8','3870000018','pablo.medina@gmail.com',0,0,2,182),(31,'Marcos','Juarez','41002009','2001-01-27','Av. Sur 9','3870000019','marcos.juarez@gmail.com',0,0,2,182),(38,'Rodrigo','Vencido','41999001','2002-01-01','Calle Prueba 1','3870009001','rodrigo.vencido@gmail.com',0,0,1,181),(39,'Sebastian','Funes','41999003','2001-09-10','Calle Prueba 3','3870009003','sebastian.funes@gmail.com',0,0,1,181),(40,'Damian','Quiroga','41999004','2003-03-03','Calle Prueba 4','3870009004','damian.quiroga@gmail.com',0,0,1,181),(42,'Valentin','Caceres','41999005','2000-11-20','Calle Prueba 5','3870009005','valentin.caceres@gmail.com',0,0,1,181),(43,'Camila','Ibarra','41999006','2010-04-08','Calle Prueba 6','3870009006','camila.ibarra@gmail.com',0,0,1,151),(45,'Facundo','Costa','44678876','2016-06-16','pasaje belgrano casa 20','3876987654','facundo@gmail.com',0,0,1,76),(46,'Facundo Costa','Costa','32987342','1997-06-13','ewqeqwe','1241241341','asdsadqwdwq',0,0,1,181),(47,'Dembele','dembele','39876123','2018-06-08','23e2e32','12323123','wefewfew',0,0,1,46),(48,'Jiel','Karius','12345678','2014-06-06','Los Sauces CASA 2','38767898','joel@gmail.com',0,0,1,106),(49,'Gael','Lopez','32456765','2017-06-03','Avenida Belgrano Casa 12','3876765345','gael@gmail.com',0,0,1,61);
/*!40000 ALTER TABLE `jugadores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partido_jugadores_local`
--

DROP TABLE IF EXISTS `partido_jugadores_local`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partido_jugadores_local` (
  `partido_id` int NOT NULL,
  `jugador_id` int NOT NULL,
  PRIMARY KEY (`partido_id`,`jugador_id`),
  KEY `jugador_id` (`jugador_id`),
  CONSTRAINT `partido_jugadores_local_ibfk_1` FOREIGN KEY (`partido_id`) REFERENCES `partidos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `partido_jugadores_local_ibfk_2` FOREIGN KEY (`jugador_id`) REFERENCES `jugadores` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partido_jugadores_local`
--

LOCK TABLES `partido_jugadores_local` WRITE;
/*!40000 ALTER TABLE `partido_jugadores_local` DISABLE KEYS */;
INSERT INTO `partido_jugadores_local` VALUES (4,1),(4,8),(3,9),(4,9),(3,10),(4,10),(4,11),(3,12),(4,12),(3,13),(4,13),(4,14),(4,15),(4,16),(3,43);
/*!40000 ALTER TABLE `partido_jugadores_local` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partido_jugadores_visitante`
--

DROP TABLE IF EXISTS `partido_jugadores_visitante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partido_jugadores_visitante` (
  `partido_id` int NOT NULL,
  `jugador_id` int NOT NULL,
  PRIMARY KEY (`partido_id`,`jugador_id`),
  KEY `jugador_id` (`jugador_id`),
  CONSTRAINT `partido_jugadores_visitante_ibfk_1` FOREIGN KEY (`partido_id`) REFERENCES `partidos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `partido_jugadores_visitante_ibfk_2` FOREIGN KEY (`jugador_id`) REFERENCES `jugadores` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partido_jugadores_visitante`
--

LOCK TABLES `partido_jugadores_visitante` WRITE;
/*!40000 ALTER TABLE `partido_jugadores_visitante` DISABLE KEYS */;
/*!40000 ALTER TABLE `partido_jugadores_visitante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partidos`
--

DROP TABLE IF EXISTS `partidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partidos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `arbitro_id` int NOT NULL,
  `fecha_liga` varchar(50) DEFAULT NULL,
  `fecha_partido` date NOT NULL,
  `gol_local` int NOT NULL DEFAULT '0',
  `gol_visitante` int NOT NULL DEFAULT '0',
  `modificado` tinyint(1) NOT NULL DEFAULT '0',
  `club_local_id` int NOT NULL,
  `club_visitante_id` int NOT NULL,
  `categoria_local_id` int NOT NULL,
  `categoria_visitante_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `club_local_id` (`club_local_id`),
  KEY `club_visitante_id` (`club_visitante_id`),
  KEY `categoria_local_id` (`categoria_local_id`),
  KEY `categoria_visitante_id` (`categoria_visitante_id`),
  KEY `fk_partido_arbitro` (`arbitro_id`),
  CONSTRAINT `fk_partido_arbitro` FOREIGN KEY (`arbitro_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `partidos_ibfk_1` FOREIGN KEY (`club_local_id`) REFERENCES `clubes` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `partidos_ibfk_2` FOREIGN KEY (`club_visitante_id`) REFERENCES `clubes` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `partidos_ibfk_3` FOREIGN KEY (`categoria_local_id`) REFERENCES `categorias` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `partidos_ibfk_4` FOREIGN KEY (`categoria_visitante_id`) REFERENCES `categorias` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partidos`
--

LOCK TABLES `partidos` WRITE;
/*!40000 ALTER TABLE `partidos` DISABLE KEYS */;
INSERT INTO `partidos` VALUES (2,2,'Fecha 12','2026-07-01',2,0,1,7,5,7,5),(3,2,'Fecha 2','2026-06-28',0,0,0,1,3,1,3),(4,2,'Fecha 1','2026-07-05',0,0,0,1,2,181,182);
/*!40000 ALTER TABLE `partidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `secretarios`
--

DROP TABLE IF EXISTS `secretarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `secretarios` (
  `usuario_id` int NOT NULL,
  `club_id` int NOT NULL,
  PRIMARY KEY (`usuario_id`),
  KEY `club_id` (`club_id`),
  CONSTRAINT `secretarios_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `secretarios_ibfk_2` FOREIGN KEY (`club_id`) REFERENCES `clubes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `secretarios`
--

LOCK TABLES `secretarios` WRITE;
/*!40000 ALTER TABLE `secretarios` DISABLE KEYS */;
INSERT INTO `secretarios` VALUES (1,1),(6,1),(11,2),(12,11);
/*!40000 ALTER TABLE `secretarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transferencias`
--

DROP TABLE IF EXISTS `transferencias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transferencias` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha_solicitud` date NOT NULL,
  `fecha_respuesta` date DEFAULT NULL,
  `estado` varchar(30) NOT NULL DEFAULT 'Pendiente',
  `jugador_id` int NOT NULL,
  `club_origen_id` int NOT NULL,
  `club_destino_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `jugador_id` (`jugador_id`),
  KEY `club_origen_id` (`club_origen_id`),
  KEY `club_destino_id` (`club_destino_id`),
  CONSTRAINT `transferencias_ibfk_1` FOREIGN KEY (`jugador_id`) REFERENCES `jugadores` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `transferencias_ibfk_2` FOREIGN KEY (`club_origen_id`) REFERENCES `clubes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `transferencias_ibfk_3` FOREIGN KEY (`club_destino_id`) REFERENCES `clubes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transferencias`
--

LOCK TABLES `transferencias` WRITE;
/*!40000 ALTER TABLE `transferencias` DISABLE KEYS */;
INSERT INTO `transferencias` VALUES (1,'2026-06-28',NULL,'Pendiente',14,1,5),(2,'2026-06-28',NULL,'Pendiente',10,1,2);
/*!40000 ALTER TABLE `transferencias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `dni` varchar(10) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `gmail` varchar(100) NOT NULL,
  `contraseña` varchar(255) NOT NULL,
  `rol` enum('Administrador','Secretario','Arbitro') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dni` (`dni`),
  UNIQUE KEY `gmail` (`gmail`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Juan','Pérez','12345678','3874123456','juan@gmail.com','1234','Secretario'),(2,'Facundo','Costa','45262460','3874123456','facuss@gmail.com','45262460','Arbitro'),(3,'Tamara','Yonar','44328942','3874120856','tamiw@gmail.com','44328942','Administrador'),(6,'Raul','Huaill','12341213','123312312','fswdasdsa','q1241231','Secretario'),(11,'Joel','Costa','98765432','38766789','joel@gmail.com','9898','Secretario'),(12,'Gael ','Lopez','34897654','387987654','gael@gmail.com','34672','Secretario');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-28 21:42:52
