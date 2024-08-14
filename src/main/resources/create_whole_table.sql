-- MySQL dump 10.13  Distrib 8.0.39, for Linux (x86_64)
--
-- Host: localhost    Database: ssafy
-- ------------------------------------------------------
-- Server version       8.0.39

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
-- Current Database: `ssafy`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `ssafy` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `ssafy`;

--
-- Table structure for table `apply`
--

DROP TABLE IF EXISTS `apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apply` (
                         `team_id` bigint NOT NULL,
                         `resume_id` bigint NOT NULL,
                         `status` enum('applied','accepted','rejected') NOT NULL,
                         `DEL_YN` tinyint(1) NOT NULL,
                         `REG_DTTM` timestamp NOT NULL,
                         `REG_USER_SEQ` bigint NOT NULL,
                         `MOD_DTTM` timestamp NOT NULL,
                         `MOD_USER_SEQ` bigint NOT NULL,
                         PRIMARY KEY (`team_id`,`resume_id`),
                         KEY `resume_id` (`resume_id`),
                         CONSTRAINT `apply_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`),
                         CONSTRAINT `apply_ibfk_2` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apply`
--

LOCK TABLES `apply` WRITE;
/*!40000 ALTER TABLE `apply` DISABLE KEYS */;
/*!40000 ALTER TABLE `apply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `apply_info`
--

DROP TABLE IF EXISTS `apply_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apply_info` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `team_id` bigint DEFAULT NULL,
                              `member_id` bigint DEFAULT NULL,
                              `read` tinyint DEFAULT NULL,
                              `content` varchar(255) DEFAULT NULL,
                              `created_at` timestamp NOT NULL,
                              `updated_at` timestamp NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `apply_info_idx_01` (`created_at`,`updated_at`) /*!80000 INVISIBLE */,
                              KEY `fk_team_apply_info_id_team_id_idx` (`team_id`),
                              KEY `fk_member_apply_info_id_member_id_idx` (`member_id`),
                              CONSTRAINT `fk_member_apply_info_id_member_id` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
                              CONSTRAINT `fk_team_apply_info_id_team_id` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apply_info`
--

LOCK TABLES `apply_info` WRITE;
/*!40000 ALTER TABLE `apply_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `apply_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `apply_info_domain`
--

DROP TABLE IF EXISTS `apply_info_domain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apply_info_domain` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `domain_id` bigint DEFAULT NULL,
                                     `apply_info_id` bigint DEFAULT NULL,
                                     `created_at` timestamp NOT NULL,
                                     `updated_at` timestamp NOT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `apply_info_domain_idx_01` (`created_at`,`updated_at`),
                                     KEY `fk_domain_apply_info_domain_id_domain_id_idx` (`domain_id`),
                                     KEY `fk_apply_info_apply_info_domain_id_apply_info_id_idx` (`apply_info_id`),
                                     CONSTRAINT `fk_apply_info_apply_info_domain_id_apply_info_id` FOREIGN KEY (`apply_info_id`) REFERENCES `apply_info` (`id`),
                                     CONSTRAINT `fk_domain_apply_info_domain_id_domain_id` FOREIGN KEY (`domain_id`) REFERENCES `domain` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apply_info_domain`
--

LOCK TABLES `apply_info_domain` WRITE;
/*!40000 ALTER TABLE `apply_info_domain` DISABLE KEYS */;
/*!40000 ALTER TABLE `apply_info_domain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `apply_info_position`
--

DROP TABLE IF EXISTS `apply_info_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apply_info_position` (
                                       `id` bigint NOT NULL AUTO_INCREMENT,
                                       `position_id` bigint DEFAULT NULL,
                                       `apply_info_id` bigint DEFAULT NULL,
                                       `created_at` timestamp NOT NULL,
                                       `updated_at` timestamp NOT NULL,
                                       PRIMARY KEY (`id`),
                                       KEY `apply_info_position_idx_01` (`updated_at`,`created_at`),
                                       KEY `fk_position_apply_info_position_id_position_id_idx` (`position_id`),
                                       KEY `fk_apply_info_apply_info_position_id_apply_info_id_idx` (`apply_info_id`),
                                       CONSTRAINT `fk_apply_info_apply_info_position_id_apply_info_id` FOREIGN KEY (`apply_info_id`) REFERENCES `apply_info` (`id`),
                                       CONSTRAINT `fk_position_apply_info_position_id_position_id` FOREIGN KEY (`position_id`) REFERENCES `position` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apply_info_position`
--

LOCK TABLES `apply_info_position` WRITE;
/*!40000 ALTER TABLE `apply_info_position` DISABLE KEYS */;
/*!40000 ALTER TABLE `apply_info_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `backend_config`
--

DROP TABLE IF EXISTS `backend_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `backend_config` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `project_info_id` bigint DEFAULT NULL,
                                  `backend_job_name` varchar(255) DEFAULT NULL,
                                  `config_name` varchar(255) DEFAULT NULL,
                                  `git_url` varchar(255) DEFAULT NULL,
                                  `git_branch` varchar(255) DEFAULT NULL,
                                  `git_username` varchar(255) DEFAULT NULL,
                                  `git_access_token` varchar(255) DEFAULT NULL,
                                  `language` varchar(255) DEFAULT NULL,
                                  `language_version` varchar(255) DEFAULT NULL,
                                  `framework` varchar(255) DEFAULT NULL,
                                  `build_tool` varchar(255) DEFAULT NULL,
                                  `context_path` varchar(255) DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `fk_backend_config_project_info` (`project_info_id`),
                                  CONSTRAINT `fk_backend_config_project_info` FOREIGN KEY (`project_info_id`) REFERENCES `team_project_info` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `backend_config`
--

LOCK TABLES `backend_config` WRITE;
/*!40000 ALTER TABLE `backend_config` DISABLE KEYS */;
INSERT INTO `backend_config` VALUES (1,1,NULL,'환경설정 1','https://lab.ssafy.com/s11-webmobile1-sub2/S11P12A606.git','BE-Develop','17452','nZhB4Uus8zfsAxKsZxCA','Java/Spring','17',NULL,'Gradle', 'hot6man');
/*!40000 ALTER TABLE `backend_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `title` varchar(40) NOT NULL,
                         `type` enum('RECRUIT') NOT NULL,
                         `content` varchar(500) NOT NULL,
                         `hit` int NOT NULL,
                         `author_id` bigint NOT NULL,
                         `DEL_YN` tinyint(1) NOT NULL,
                         `REG_DTTM` timestamp NOT NULL,
                         `REG_USER_SEQ` bigint NOT NULL,
                         `MOD_DTTM` timestamp NOT NULL,
                         `MOD_USER_SEQ` bigint NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `author_id` (`author_id`),
                         CONSTRAINT `board_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board`
--

LOCK TABLES `board` WRITE;
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
/*!40000 ALTER TABLE `board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `build_jenkins_job`
--

DROP TABLE IF EXISTS `build_jenkins_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `build_jenkins_job` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `build_result_id` bigint DEFAULT NULL,
                                     `build_num` bigint DEFAULT NULL,
                                     `job_name` varchar(255) DEFAULT NULL,
                                     `result` varchar(255) DEFAULT NULL,
                                     `job_type` varchar(255) DEFAULT NULL,
                                     `DEL_YN` tinyint(1) NOT NULL,
                                     `REG_DTTM` timestamp NOT NULL,
                                     `REG_USER_SEQ` bigint NOT NULL,
                                     `MOD_DTTM` timestamp NOT NULL,
                                     `MOD_USER_SEQ` bigint NOT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `build_result_id` (`build_result_id`),
                                     KEY `build_jenkins_job_idx_01` (`DEL_YN`,`REG_USER_SEQ`,`MOD_USER_SEQ`),
                                     CONSTRAINT `build_jenkins_job_ibfk_1` FOREIGN KEY (`build_result_id`) REFERENCES `build_result` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `build_jenkins_job`
--

LOCK TABLES `build_jenkins_job` WRITE;
/*!40000 ALTER TABLE `build_jenkins_job` DISABLE KEYS */;
/*!40000 ALTER TABLE `build_jenkins_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `build_log`
--

DROP TABLE IF EXISTS `build_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `build_log` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `build_stage_id` bigint DEFAULT NULL,
                             `title` varchar(255) DEFAULT NULL,
                             `description` text,
                             `DEL_YN` tinyint(1) NOT NULL,
                             `REG_DTTM` timestamp NOT NULL,
                             `REG_USER_SEQ` bigint NOT NULL,
                             `MOD_DTTM` timestamp NOT NULL,
                             `MOD_USER_SEQ` bigint NOT NULL,
                             PRIMARY KEY (`id`),
                             KEY `build_stage_id` (`build_stage_id`),
                             KEY `build_log_idx_01` (`DEL_YN`,`REG_USER_SEQ`,`MOD_USER_SEQ`),
                             CONSTRAINT `build_log_ibfk_1` FOREIGN KEY (`build_stage_id`) REFERENCES `build_stage` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `build_log`
--

LOCK TABLES `build_log` WRITE;
/*!40000 ALTER TABLE `build_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `build_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `build_result`
--

DROP TABLE IF EXISTS `build_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `build_result` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `team_project_info_id` bigint DEFAULT NULL,
                                `deploy_num` bigint DEFAULT NULL,
                                `status` varchar(255) DEFAULT NULL,
                                `build_time` timestamp NULL DEFAULT NULL,
                                `grafana_uid` varchar(255) DEFAULT NULL,
                                `DEL_YN` tinyint(1) NOT NULL,
                                `REG_DTTM` timestamp NOT NULL,
                                `REG_USER_SEQ` bigint NOT NULL,
                                `MOD_DTTM` timestamp NOT NULL,
                                `MOD_USER_SEQ` bigint NOT NULL,
                                PRIMARY KEY (`id`),
                                KEY `team_project_info_id` (`team_project_info_id`),
                                KEY `build_result_idx_01` (`DEL_YN`,`REG_USER_SEQ`,`MOD_USER_SEQ`),
                                CONSTRAINT `build_result_ibfk_1` FOREIGN KEY (`team_project_info_id`) REFERENCES `team_project_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `build_result`
--

LOCK TABLES `build_result` WRITE;
/*!40000 ALTER TABLE `build_result` DISABLE KEYS */;
INSERT INTO `build_result` VALUES (1,1,1,'PENDING',NULL,NULL,0,'2024-08-12 16:16:16',1,'2024-08-12 16:16:16',1);
/*!40000 ALTER TABLE `build_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `build_setting`
--

DROP TABLE IF EXISTS `build_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `build_setting` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `build_id` bigint DEFAULT NULL,
                                 `git_token` varchar(127) DEFAULT NULL,
                                 `jenkins_url` varchar(127) DEFAULT NULL,
                                 `docker_image` varchar(127) DEFAULT NULL,
                                 `created_at` timestamp NOT NULL,
                                 `updated_at` timestamp NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `build_setting_idx_01` (`created_at`,`updated_at`),
                                 KEY `fk_build_build_setting_id_build_id_idx` (`build_id`),
                                 CONSTRAINT `fk_build_build_setting_id_build_id` FOREIGN KEY (`build_id`) REFERENCES `build` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `build_setting`
--

LOCK TABLES `build_setting` WRITE;
/*!40000 ALTER TABLE `build_setting` DISABLE KEYS */;
/*!40000 ALTER TABLE `build_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `build_stage`
--

DROP TABLE IF EXISTS `build_stage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `build_stage` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `build_jenkins_job_id` bigint DEFAULT NULL,
                               `name` varchar(255) DEFAULT NULL,
                               `stage_id` bigint DEFAULT NULL,
                               `status` varchar(255) DEFAULT NULL,
                               `duration` int DEFAULT NULL,
                               `DEL_YN` tinyint(1) NOT NULL,
                               `REG_DTTM` timestamp NOT NULL,
                               `REG_USER_SEQ` bigint NOT NULL,
                               `MOD_DTTM` timestamp NOT NULL,
                               `MOD_USER_SEQ` bigint NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `build_jenkins_job_id` (`build_jenkins_job_id`),
                               KEY `build_stage_idx_01` (`DEL_YN`,`REG_USER_SEQ`,`MOD_USER_SEQ`),
                               CONSTRAINT `build_stage_ibfk_1` FOREIGN KEY (`build_jenkins_job_id`) REFERENCES `build_jenkins_job` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `build_stage`
--

LOCK TABLES `build_stage` WRITE;
/*!40000 ALTER TABLE `build_stage` DISABLE KEYS */;
/*!40000 ALTER TABLE `build_stage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chatroom`
--

DROP TABLE IF EXISTS `chatroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chatroom` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `create_date` timestamp NULL DEFAULT NULL,
                            `last_message` varchar(200) DEFAULT NULL,
                            `last_message_date` timestamp NULL DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chatroom`
--

LOCK TABLES `chatroom` WRITE;
/*!40000 ALTER TABLE `chatroom` DISABLE KEYS */;
/*!40000 ALTER TABLE `chatroom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chatroom_status`
--

DROP TABLE IF EXISTS `chatroom_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chatroom_status` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `room_name` varchar(30) DEFAULT NULL,
                                   `chatroom_id` bigint NOT NULL,
                                   `user_id` bigint NOT NULL,
                                   `unread_count` int DEFAULT '0',
                                   `online` tinyint(1) DEFAULT '0',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `chatroom_id` (`chatroom_id`,`user_id`),
                                   CONSTRAINT `chatroom_status_ibfk_1` FOREIGN KEY (`chatroom_id`) REFERENCES `chatroom` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chatroom_status`
--

LOCK TABLES `chatroom_status` WRITE;
/*!40000 ALTER TABLE `chatroom_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `chatroom_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `database_config`
--

DROP TABLE IF EXISTS `database_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `database_config` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `project_info_id` bigint DEFAULT NULL,
                                   `database_job_name` varchar(255) DEFAULT NULL,
                                   `config_name` varchar(255) DEFAULT NULL,
                                   `url` varchar(255) DEFAULT NULL,
                                   `schema_name` varchar(255) DEFAULT NULL,
                                   `username` varchar(255) DEFAULT NULL,
                                   `password` varchar(255) DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `fk_database_config_project_info` (`project_info_id`),
                                   CONSTRAINT `fk_database_config_project_info` FOREIGN KEY (`project_info_id`) REFERENCES `team_project_info` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `database_config`
--

LOCK TABLES `database_config` WRITE;
/*!40000 ALTER TABLE `database_config` DISABLE KEYS */;
INSERT INTO `database_config` VALUES (1,1,NULL,'환경설정 1','jdbc-url: jdbc:mysql://mysql-0:3306/ssafy?serverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8','ssafy','ssafy','ssafy');
/*!40000 ALTER TABLE `database_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `following`
--

DROP TABLE IF EXISTS `following`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `following` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `follower_id` bigint DEFAULT NULL,
                             `followed_id` bigint DEFAULT NULL,
                             `DEL_YN` tinyint(1) NOT NULL,
                             `REG_DTTM` timestamp NOT NULL,
                             `REG_USER_SEQ` bigint NOT NULL,
                             `MOD_DTTM` timestamp NOT NULL,
                             `MOD_USER_SEQ` bigint NOT NULL,
                             PRIMARY KEY (`id`),
                             KEY `fk_member_following_id_follower_id_idx` (`follower_id`),
                             KEY `fk_member_following_id_followed_id_idx` (`followed_id`),
                             KEY `following_idx_01` (`DEL_YN`,`REG_USER_SEQ`,`MOD_USER_SEQ`),
                             CONSTRAINT `fk_member_following_id_followed_id` FOREIGN KEY (`followed_id`) REFERENCES `member` (`id`),
                             CONSTRAINT `fk_member_following_id_follower_id` FOREIGN KEY (`follower_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `following`
--

LOCK TABLES `following` WRITE;
/*!40000 ALTER TABLE `following` DISABLE KEYS */;
/*!40000 ALTER TABLE `following` ENABLE KEYS */;
UNLOCK TABLES;
-- -----------------------------------------------------
-- Table `ssafy`.`chatroom`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`chatroom` (
                                                  `id` BIGINT NOT NULL AUTO_INCREMENT,
    `create_date` TIMESTAMP NULL DEFAULT NULL,
    `last_message` VARCHAR(200) NULL DEFAULT NULL,
    `last_message_date` TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

--
-- Table structure for table `frontend_config`
--

DROP TABLE IF EXISTS `frontend_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `frontend_config` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `project_info_id` bigint DEFAULT NULL,
                                   `frontend_job_name` varchar(255) DEFAULT NULL,
                                   `config_name` varchar(255) DEFAULT NULL,
                                   `framework` varchar(255) DEFAULT NULL,
                                   `version` varchar(255) DEFAULT NULL,
                                   `git_url` varchar(255) DEFAULT NULL,
                                   `git_branch` varchar(255) DEFAULT NULL,
                                   `git_username` varchar(255) DEFAULT NULL,
                                   `git_access_token` varchar(255) DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `fk_frontend_config_project_info` (`project_info_id`),
                                   CONSTRAINT `fk_frontend_config_project_info` FOREIGN KEY (`project_info_id`) REFERENCES `team_project_info` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `frontend_config`
--
-- -----------------------------------------------------
-- Table `ssafy`.`chatroom_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`chatroom_status` (
                                                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                         `chatroom_id` BIGINT NOT NULL,
    `room_name` VARCHAR(255) NULL,
                                                         `user_id` BIGINT NOT NULL,
                                                         `unread_count` INT NULL DEFAULT '0',
                                                         `online` TINYINT(1) NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `chatroom_id` (`chatroom_id` ASC, `user_id` ASC) VISIBLE,
    CONSTRAINT `chatroom_status_ibfk_1`
    FOREIGN KEY (`chatroom_id`)
    REFERENCES `ssafy`.`chatroom` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

LOCK TABLES `frontend_config` WRITE;
/*!40000 ALTER TABLE `frontend_config` DISABLE KEYS */;
INSERT INTO `frontend_config` VALUES (1,1,NULL,'환경설정 1','Vue.js','3.2.37','https://lab.ssafy.com/s11-webmobile1-sub2/S11P12A606.git','FE-Develop','17452','nZhB4Uus8zfsAxKsZxCA');
/*!40000 ALTER TABLE `frontend_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meeting`
--

DROP TABLE IF EXISTS `meeting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meeting` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `team_id` bigint DEFAULT NULL,
                           `name` varchar(20) DEFAULT NULL,
                           `user_count` int DEFAULT NULL,
                           `max_user_count` int DEFAULT NULL,
                           `secret` tinyint DEFAULT NULL,
                           `created_at` timestamp NOT NULL,
                           `updated_at` timestamp NOT NULL,
                           `reg_dttm` datetime(6) NOT NULL,
                           `del_yn` bit(1) NOT NULL,
                           `mod_user_seq` int NOT NULL,
                           `mod_dttm` datetime(6) NOT NULL,
                           `reg_user_seq` int NOT NULL,
                           `connection_id` varchar(100) DEFAULT NULL,
                           `session_id` varchar(100) DEFAULT NULL,
                           `session_status` tinyint(1) DEFAULT '0',
                           PRIMARY KEY (`id`),
                           KEY `meeting_idx_01` (`created_at`,`updated_at`),
                           KEY `fk_team_meeting_id_team_id_idx` (`team_id`),
                           CONSTRAINT `fk_team_meeting_id_team_id` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting`
--

LOCK TABLES `meeting` WRITE;
/*!40000 ALTER TABLE `meeting` DISABLE KEYS */;
/*!40000 ALTER TABLE `meeting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `email` varchar(50) NOT NULL,
                          `nickname` varchar(50) NOT NULL,
                          `name` varchar(20) NOT NULL,
                          `profile_url` varchar(255) DEFAULT NULL,
                          `phone` varchar(20) DEFAULT NULL,
                          `address` varchar(255) DEFAULT NULL,
                          `role` varchar(50) NOT NULL,
                          `lgn_mtd` varchar(50) NOT NULL,
                          `DEL_YN` tinyint(1) NOT NULL,
                          `REG_DTTM` timestamp NOT NULL,
                          `REG_USER_SEQ` bigint NOT NULL,
                          `MOD_DTTM` timestamp NOT NULL,
                          `MOD_USER_SEQ` bigint NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `member_idx_01` (`DEL_YN`,`REG_USER_SEQ`,`MOD_USER_SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (2,'soo_0201@naver.com','조용수','조용수',NULL,NULL,NULL,'ROLE_USER','naver',0,'2024-08-12 16:11:02',1,'2024-08-12 16:11:02',1);
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member_chat`
--

DROP TABLE IF EXISTS `member_chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_chat` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `from_id` bigint DEFAULT NULL,
                               `to_id` bigint DEFAULT NULL,
                               `subject` varchar(127) DEFAULT NULL,
                               `status` varchar(20) DEFAULT NULL,
                               `time` timestamp NULL DEFAULT NULL,
                               `member_chatcol` varchar(45) DEFAULT NULL,
                               `DEL_YN` tinyint(1) NOT NULL,
                               `REG_DTTM` timestamp NOT NULL,
                               `REG_USER_SEQ` bigint NOT NULL,
                               `MOD_DTTM` timestamp NOT NULL,
                               `MOD_USER_SEQ` bigint NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `fk_member_member_chat_id_from_id_idx` (`from_id`),
                               KEY `fk_member_member_chat_id_to_id_idx` (`to_id`),
                               KEY `member_chat_idx_01` (`DEL_YN`,`REG_USER_SEQ`,`MOD_USER_SEQ`),
                               CONSTRAINT `fk_member_member_chat_id_from_id` FOREIGN KEY (`from_id`) REFERENCES `member` (`id`),
                               CONSTRAINT `fk_member_member_chat_id_to_id` FOREIGN KEY (`to_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_chat`
--

LOCK TABLES `member_chat` WRITE;
/*!40000 ALTER TABLE `member_chat` DISABLE KEYS */;
/*!40000 ALTER TABLE `member_chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member_image`
--

DROP TABLE IF EXISTS `member_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_image` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `member_id` bigint NOT NULL,
                                `origin_name` varchar(255) DEFAULT NULL,
                                `fixed_name` varchar(255) DEFAULT NULL,
                                `save_folder` varchar(255) DEFAULT NULL,
                                `type` varchar(50) DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `member_id` (`member_id`),
                                CONSTRAINT `member_image_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_image`
--

LOCK TABLES `member_image` WRITE;
/*!40000 ALTER TABLE `member_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `member_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member_team`
--

DROP TABLE IF EXISTS `member_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_team` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `member_id` bigint DEFAULT NULL,
                               `team_id` bigint DEFAULT NULL,
                               `leader` tinyint(1) DEFAULT NULL,
                               `DEL_YN` tinyint(1) NOT NULL,
                               `REG_DTTM` timestamp NOT NULL,
                               `REG_USER_SEQ` bigint NOT NULL,
                               `MOD_DTTM` timestamp NOT NULL,
                               `MOD_USER_SEQ` bigint NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `member_team_idx_01` (`DEL_YN`,`REG_USER_SEQ`,`MOD_USER_SEQ`) /*!80000 INVISIBLE */,
                               KEY `fk_member_member_team_id_member_id_idx` (`member_id`),
                               KEY `fk_team_member_team_id_team_id_idx` (`team_id`),
                               CONSTRAINT `fk_member_member_team_id_member_id` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE,
                               CONSTRAINT `fk_team_member_team_id_team_id` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_team`
--

LOCK TABLES `member_team` WRITE;
/*!40000 ALTER TABLE `member_team` DISABLE KEYS */;
INSERT INTO `member_team` VALUES (1,2,6,1,0,'2024-08-12 16:11:55',1,'2024-08-12 16:11:55',1);
/*!40000 ALTER TABLE `member_team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `member_chat_id` bigint DEFAULT NULL,
                           `is_from_sender` tinyint(1) DEFAULT NULL,
                           `read` tinyint(1) DEFAULT NULL,
                           `content` varchar(127) DEFAULT NULL,
                           `message_visible_to` varchar(20) DEFAULT NULL,
                           `DEL_YN` tinyint(1) NOT NULL,
                           `REG_DTTM` timestamp NOT NULL,
                           `REG_USER_SEQ` bigint NOT NULL,
                           `MOD_DTTM` timestamp NOT NULL,
                           `MOD_USER_SEQ` bigint NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `fk_member_chat_message_id_member_chat_id_idx` (`member_chat_id`),
                           KEY `message_idx_01` (`DEL_YN`,`REG_USER_SEQ`,`MOD_USER_SEQ`),
                           CONSTRAINT `fk_member_chat_message_id_member_chat_id` FOREIGN KEY (`member_chat_id`) REFERENCES `member_chat` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `receiver` bigint NOT NULL,
                                `sender` bigint NOT NULL,
                                `type` varchar(15) NOT NULL,
                                `url` varchar(50) NOT NULL,
                                `is_read` tinyint(1) NOT NULL,
                                `notify_date` varchar(20) NOT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `name` varchar(50) DEFAULT NULL,
                           `content` varchar(255) DEFAULT NULL,
                           `team_id` bigint DEFAULT NULL,
                           `start_time` timestamp NULL DEFAULT NULL,
                           `end_time` timestamp NULL DEFAULT NULL,
                           `git_url` varchar(255) DEFAULT NULL,
                           `jira_url` varchar(255) DEFAULT NULL,
                           `DEL_YN` tinyint(1) NOT NULL,
                           `REG_DTTM` timestamp NOT NULL,
                           `REG_USER_SEQ` bigint NOT NULL,
                           `MOD_DTTM` timestamp NOT NULL,
                           `MOD_USER_SEQ` bigint NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `project_idx_01` (`DEL_YN`,`REG_USER_SEQ`,`MOD_USER_SEQ`),
                           KEY `fk_team_project_id_team_id_idx` (`team_id`),
                           CONSTRAINT `fk_team_project_id_team_id` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recruit`
--

DROP TABLE IF EXISTS `recruit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recruit` (
                           `id` bigint NOT NULL,
                           `introduction` varchar(50) NOT NULL,
                           `thumbnail` varchar(100) NOT NULL,
                           `domain` varchar(20) NOT NULL,
                           `desired_pos_list` json NOT NULL,
                           `team_id` bigint NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `team_id` (`team_id`),
                           CONSTRAINT `recruit_ibfk_1` FOREIGN KEY (`id`) REFERENCES `board` (`id`),
                           CONSTRAINT `recruit_ibfk_2` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recruit`
--

LOCK TABLES `recruit` WRITE;
/*!40000 ALTER TABLE `recruit` DISABLE KEYS */;
/*!40000 ALTER TABLE `recruit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume`
--

DROP TABLE IF EXISTS `resume`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resume` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `title` varchar(40) NOT NULL,
                          `profile` varchar(100) NOT NULL,
                          `position` varchar(40) NOT NULL,
                          `tech_stack` json NOT NULL,
                          `comment` varchar(40) NOT NULL,
                          `author_id` bigint NOT NULL,
                          `DEL_YN` tinyint(1) NOT NULL,
                          `REG_DTTM` timestamp NOT NULL,
                          `REG_USER_SEQ` bigint NOT NULL,
                          `MOD_DTTM` timestamp NOT NULL,
                          `MOD_USER_SEQ` bigint NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `author_id` (`author_id`),
                          CONSTRAINT `resume_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume`
--

LOCK TABLES `resume` WRITE;
/*!40000 ALTER TABLE `resume` DISABLE KEYS */;
/*!40000 ALTER TABLE `resume` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume_experience`
--

DROP TABLE IF EXISTS `resume_experience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resume_experience` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `title` varchar(40) NOT NULL,
                                     `description` varchar(500) NOT NULL,
                                     `resume_id` bigint NOT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `resume_id` (`resume_id`),
                                     CONSTRAINT `resume_experience_ibfk_1` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume_experience`
--

LOCK TABLES `resume_experience` WRITE;
/*!40000 ALTER TABLE `resume_experience` DISABLE KEYS */;
/*!40000 ALTER TABLE `resume_experience` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_schedule`
--

DROP TABLE IF EXISTS `service_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_schedule` (
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `team_id` bigint DEFAULT NULL,
                                    `team_project_info_id` bigint DEFAULT NULL,
                                    `build_status` varchar(255) DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `team_id` (`team_id`),
                                    KEY `team_project_info_id` (`team_project_info_id`),
                                    CONSTRAINT `service_schedule_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`),
                                    CONSTRAINT `service_schedule_ibfk_2` FOREIGN KEY (`team_project_info_id`) REFERENCES `team_project_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_schedule`
--

LOCK TABLES `service_schedule` WRITE;
/*!40000 ALTER TABLE `service_schedule` DISABLE KEYS */;
INSERT INTO `service_schedule` VALUES (1,NULL,NULL,'SUCCESS'),(2,NULL,NULL,'SUCCESS'),(3,NULL,NULL,'SUCCESS'),(4,NULL,NULL,'EMPTY'),(5,NULL,NULL,'EMPTY'),(6,NULL,NULL,'EMPTY'),(7,NULL,NULL,'EMPTY'),(8,NULL,NULL,'EMPTY'),(9,NULL,NULL,'EMPTY'),(10,NULL,NULL,'EMPTY');
/*!40000 ALTER TABLE `service_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `name` varchar(50) DEFAULT NULL,
                        `status` varchar(20) DEFAULT NULL,
                        `content` varchar(255) DEFAULT NULL,
                        `start_time` timestamp NULL DEFAULT NULL,
                        `end_time` timestamp NULL DEFAULT NULL,
                        `git_url` varchar(255) DEFAULT NULL,
                        `jira_url` varchar(255) DEFAULT NULL,
                        `session_id` varchar(255) DEFAULT NULL,
                        `DEL_YN` tinyint(1) NOT NULL,
                        `REG_DTTM` timestamp NOT NULL,
                        `REG_USER_SEQ` bigint NOT NULL,
                        `MOD_DTTM` timestamp NOT NULL,
                        `MOD_USER_SEQ` bigint NOT NULL,
                        PRIMARY KEY (`id`),
                        KEY `team_idx_01` (`DEL_YN`,`REG_USER_SEQ`,`MOD_USER_SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES (1,'Team Alpha','active','Content for team alpha','2024-01-01 01:00:00','2024-12-31 09:00:00','https://github.com/team-alpha','https://jira.team-alpha.com',NULL,0,'2024-08-13 01:08:09',1,'2024-08-13 01:08:09',1),(2,'Team Beta','inactive','Content for team beta','2024-02-01 02:00:00','2024-11-30 08:00:00','https://github.com/team-beta','https://jira.team-beta.com',NULL,0,'2024-08-13 01:08:09',2,'2024-08-13 01:08:09',2),(3,'Team Gamma','active','Content for team gamma','2024-03-01 03:00:00','2024-10-31 07:00:00','https://github.com/team-gamma','https://jira.team-gamma.com',NULL,0,'2024-08-13 01:08:09',3,'2024-08-13 01:08:09',3),(4,'Team Delta','inactive','Content for team delta','2024-04-01 04:00:00','2024-09-30 06:00:00','https://github.com/team-delta','https://jira.team-delta.com',NULL,0,'2024-08-13 01:08:09',4,'2024-08-13 01:08:09',4),(5,'Team Epsilon','active','Content for team epsilon','2024-05-01 05:00:00','2024-08-31 05:00:00','https://github.com/team-epsilon','https://jira.team-epsilon.com','session_5',0,'2024-08-13 01:08:09',5,'2024-08-13 01:08:09',5),(6,'ds_study','RECRUIT','ds_study','2024-08-12 16:11:55','2024-08-12 16:11:55',NULL,NULL,NULL,0,'2024-08-12 16:11:55',1,'2024-08-12 16:11:55',1);
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_project_info`
--

DROP TABLE IF EXISTS `team_project_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_project_info` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `team_id` bigint DEFAULT NULL,
                                     `title` varchar(30) DEFAULT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `fk_team_project_info_team` (`team_id`),
                                     CONSTRAINT `fk_team_project_info_team` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_project_info`
--

LOCK TABLES `team_project_info` WRITE;
/*!40000 ALTER TABLE `team_project_info` DISABLE KEYS */;
INSERT INTO `team_project_info` VALUES (1,6,'설정 1번');
/*!40000 ALTER TABLE `team_project_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;