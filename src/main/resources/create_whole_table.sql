drop database if exists `ssafy`;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ssafy
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ssafy
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ssafy` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ssafy` ;

-- -----------------------------------------------------
-- Table `ssafy`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`member` (
                                                `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                `email` VARCHAR(50) NOT NULL,
    `nickname` VARCHAR(50) NOT NULL,
    `name` VARCHAR(20) NOT NULL,
    `profile_url` VARCHAR(255) NULL DEFAULT NULL,
    `phone` VARCHAR(20) NULL DEFAULT NULL,
    `address` VARCHAR(255) NULL DEFAULT NULL,
    `role` VARCHAR(50) NOT NULL,
    `lgn_mtd` VARCHAR(50) NOT NULL,
    `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `member_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE)
    ENGINE = InnoDB
    AUTO_INCREMENT = 2
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`member_project_credential`
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `ssafy`.`team`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`team` (
                                              `id` BIGINT NOT NULL AUTO_INCREMENT,
                                              `name` VARCHAR(50) NULL DEFAULT NULL,
    `status` VARCHAR(20) NULL DEFAULT NULL,
    `content` VARCHAR(255) NULL DEFAULT NULL,
    `start_time` TIMESTAMP NULL DEFAULT NULL,
    `end_time` TIMESTAMP NULL DEFAULT NULL,
    `git_url` VARCHAR(255) NULL DEFAULT NULL,
    `jira_url` VARCHAR(255) NULL DEFAULT NULL,
    `session_id` VARCHAR(255) NULL,
    `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `team_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`apply_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`apply_info` (
                                                    `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                    `team_id` BIGINT NULL DEFAULT NULL,
                                                    `member_id` BIGINT NULL DEFAULT NULL,
                                                    `read` TINYINT NULL DEFAULT NULL,
                                                    `content` VARCHAR(255) NULL DEFAULT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `apply_info_idx_01` (`created_at` ASC, `updated_at` ASC) INVISIBLE,
    INDEX `fk_team_apply_info_id_team_id_idx` (`team_id` ASC) VISIBLE,
    INDEX `fk_member_apply_info_id_member_id_idx` (`member_id` ASC) VISIBLE,
    CONSTRAINT `fk_member_apply_info_id_member_id`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafy`.`member` (`id`),
    CONSTRAINT `fk_team_apply_info_id_team_id`
    FOREIGN KEY (`team_id`)
    REFERENCES `ssafy`.`team` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`apply_info_domain`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`apply_info_domain` (
                                                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                           `domain_id` BIGINT NULL DEFAULT NULL,
                                                           `apply_info_id` BIGINT NULL DEFAULT NULL,
                                                           `created_at` TIMESTAMP NOT NULL,
                                                           `updated_at` TIMESTAMP NOT NULL,
                                                           PRIMARY KEY (`id`),
    INDEX `apply_info_domain_idx_01` (`created_at` ASC, `updated_at` ASC) VISIBLE,
    INDEX `fk_domain_apply_info_domain_id_domain_id_idx` (`domain_id` ASC) VISIBLE,
    INDEX `fk_apply_info_apply_info_domain_id_apply_info_id_idx` (`apply_info_id` ASC) VISIBLE,
    CONSTRAINT `fk_apply_info_apply_info_domain_id_apply_info_id`
    FOREIGN KEY (`apply_info_id`)
    REFERENCES `ssafy`.`apply_info` (`id`),
    CONSTRAINT `fk_domain_apply_info_domain_id_domain_id`
    FOREIGN KEY (`domain_id`)
    REFERENCES `ssafy`.`domain` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`apply_info_position`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`apply_info_position` (
                                                             `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                             `position_id` BIGINT NULL DEFAULT NULL,
                                                             `apply_info_id` BIGINT NULL DEFAULT NULL,
                                                             `created_at` TIMESTAMP NOT NULL,
                                                             `updated_at` TIMESTAMP NOT NULL,
                                                             PRIMARY KEY (`id`),
    INDEX `apply_info_position_idx_01` (`updated_at` ASC, `created_at` ASC) VISIBLE,
    INDEX `fk_position_apply_info_position_id_position_id_idx` (`position_id` ASC) VISIBLE,
    INDEX `fk_apply_info_apply_info_position_id_apply_info_id_idx` (`apply_info_id` ASC) VISIBLE,
    CONSTRAINT `fk_apply_info_apply_info_position_id_apply_info_id`
    FOREIGN KEY (`apply_info_id`)
    REFERENCES `ssafy`.`apply_info` (`id`),
    CONSTRAINT `fk_position_apply_info_position_id_position_id`
    FOREIGN KEY (`position_id`)
    REFERENCES `ssafy`.`position` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;




-- -----------------------------------------------------
-- Table `ssafy`.`team_project_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`team_project_info` (
                                                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                           `team_id` BIGINT NULL DEFAULT NULL,
                                                           `title` VARCHAR(30) NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_team_project_info_team` (`team_id` ASC) VISIBLE,
    CONSTRAINT `fk_team_project_info_team`
    FOREIGN KEY (`team_id`)
    REFERENCES `ssafy`.`team` (`id`)
    ON DELETE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`backend_config`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`backend_config` (
                                                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                        `project_info_id` BIGINT NULL DEFAULT NULL,
                                                        `backend_job_name` VARCHAR(255) NULL DEFAULT NULL,
    `config_name` VARCHAR(255) NULL,
    `git_url` VARCHAR(255) NULL DEFAULT NULL,
    `git_branch` VARCHAR(255) NULL DEFAULT NULL,
    `git_username` VARCHAR(255) NULL DEFAULT NULL,
    `git_access_token` VARCHAR(255) NULL DEFAULT NULL,
    `language` VARCHAR(255) NULL DEFAULT NULL,
    `language_version` VARCHAR(255) NULL DEFAULT NULL,
    `framework` VARCHAR(255) NULL DEFAULT NULL,
    `build_tool` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_backend_config_project_info` (`project_info_id` ASC) VISIBLE,
    CONSTRAINT `fk_backend_config_project_info`
    FOREIGN KEY (`project_info_id`)
    REFERENCES `ssafy`.`team_project_info` (`id`)
    ON DELETE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`board` (
                                               `id` BIGINT NOT NULL AUTO_INCREMENT,
                                               `title` VARCHAR(40) NOT NULL,
    `type` ENUM('RECRUIT') NOT NULL,
    `content` VARCHAR(500) NOT NULL,
    `hit` INT NOT NULL,
    `author_id` BIGINT NOT NULL,
    `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `author_id` (`author_id` ASC) VISIBLE,
    CONSTRAINT `board_ibfk_1`
    FOREIGN KEY (`author_id`)
    REFERENCES `ssafy`.`member` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`build_result`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`build_result` (
                                                      `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                      `team_project_info_id` BIGINT NULL DEFAULT NULL,
                                                      `deploy_num` BIGINT NULL DEFAULT NULL,
                                                      `status` VARCHAR(255) NULL DEFAULT NULL,
    `build_time` TIMESTAMP NULL DEFAULT NULL,
    `grafana_uid` VARCHAR(255),
    `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `team_project_info_id` (`team_project_info_id` ASC) VISIBLE,
    INDEX `build_result_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    CONSTRAINT `build_result_ibfk_1`
    FOREIGN KEY (`team_project_info_id`)
    REFERENCES `ssafy`.`team_project_info` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `ssafy`.`build_jenkins_job`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`build_jenkins_job` (
                                                      `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                      `build_result_id` BIGINT NULL DEFAULT NULL,
                                                      `build_num` BIGINT NULL DEFAULT NULL,
                                                      `job_name` VARCHAR(255) NULL DEFAULT NULL,
    `result` VARCHAR(255),
    `job_type` VARCHAR(255),
    `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `build_result_id` (`build_result_id` ASC) VISIBLE,
    INDEX `build_jenkins_job_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    CONSTRAINT `build_jenkins_job_ibfk_1`
    FOREIGN KEY (`build_result_id`)
    REFERENCES `ssafy`.`build_result` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`build_stage`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`build_stage` (
                                                     `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                     `build_jenkins_job_id` BIGINT NULL DEFAULT NULL,
                                                     `name` VARCHAR(255) NULL DEFAULT NULL,
    `stage_id` BIGINT NULL DEFAULT NULL,
    `status` VARCHAR(255) NULL DEFAULT NULL,
    `duration` INT NULL DEFAULT NULL,
    `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `build_jenkins_job_id` (`build_jenkins_job_id` ASC) VISIBLE,
    INDEX `build_stage_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    CONSTRAINT `build_stage_ibfk_1`
    FOREIGN KEY (`build_jenkins_job_id`)
    REFERENCES `ssafy`.`build_jenkins_job` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`build_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`build_log` (
                                                   `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                   `build_stage_id` BIGINT NULL DEFAULT NULL,
                                                   `title` VARCHAR(255) NULL DEFAULT NULL,
    `description` TEXT NULL DEFAULT NULL,
    `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `build_stage_id` (`build_stage_id` ASC) VISIBLE,
    INDEX `build_log_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    CONSTRAINT `build_log_ibfk_1`
    FOREIGN KEY (`build_stage_id`)
    REFERENCES `ssafy`.`build_stage` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`build_setting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`build_setting` (
                                                       `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                       `build_id` BIGINT NULL DEFAULT NULL,
                                                       `git_token` VARCHAR(127) NULL DEFAULT NULL,
    `jenkins_url` VARCHAR(127) NULL DEFAULT NULL,
    `docker_image` VARCHAR(127) NULL DEFAULT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `build_setting_idx_01` (`created_at` ASC, `updated_at` ASC) VISIBLE,
    INDEX `fk_build_build_setting_id_build_id_idx` (`build_id` ASC) VISIBLE,
    CONSTRAINT `fk_build_build_setting_id_build_id`
    FOREIGN KEY (`build_id`)
    REFERENCES `ssafy`.`build` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`chatroom`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`chatroom` (
                                                  `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                  `name` VARCHAR(30) NULL DEFAULT NULL,
    `create_date` TIMESTAMP NULL DEFAULT NULL,
    `last_message` VARCHAR(200) NULL DEFAULT NULL,
    `last_message_date` TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`chatroom_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`chatroom_status` (
                                                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                         `chatroom_id` BIGINT NOT NULL,
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


-- -----------------------------------------------------
-- Table `ssafy`.`database_config`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`database_config` (
                                                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                         `project_info_id` BIGINT NULL DEFAULT NULL,
                                                         `database_job_name` VARCHAR(255) NULL DEFAULT NULL,
    `config_name` VARCHAR(255) NULL,
    `url` VARCHAR(255) NULL DEFAULT NULL,
    `schema_name` VARCHAR(255) NULL DEFAULT NULL,
    `username` VARCHAR(255) NULL DEFAULT NULL,
    `password` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_database_config_project_info` (`project_info_id` ASC) VISIBLE,
    CONSTRAINT `fk_database_config_project_info`
    FOREIGN KEY (`project_info_id`)
    REFERENCES `ssafy`.`team_project_info` (`id`)
    ON DELETE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`following`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`following` (
                                                   `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                   `follower_id` BIGINT NULL DEFAULT NULL,
                                                   `followed_id` BIGINT NULL DEFAULT NULL,
                                                   `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_member_following_id_follower_id_idx` (`follower_id` ASC) VISIBLE,
    INDEX `fk_member_following_id_followed_id_idx` (`followed_id` ASC) VISIBLE,
    INDEX `following_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    CONSTRAINT `fk_member_following_id_followed_id`
    FOREIGN KEY (`followed_id`)
    REFERENCES `ssafy`.`member` (`id`),
    CONSTRAINT `fk_member_following_id_follower_id`
    FOREIGN KEY (`follower_id`)
    REFERENCES `ssafy`.`member` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`frontend_config`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`frontend_config` (
                                                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                         `project_info_id` BIGINT NULL DEFAULT NULL,
                                                         `frontend_job_name` VARCHAR(255) NULL DEFAULT NULL,
    `config_name` VARCHAR(255) NULL,
    `framework` VARCHAR(255) NULL DEFAULT NULL,
    `version` VARCHAR(255) NULL DEFAULT NULL,
    `git_url` VARCHAR(255) NULL DEFAULT NULL,
    `git_branch` VARCHAR(255) NULL DEFAULT NULL,
    `git_username` VARCHAR(255) NULL DEFAULT NULL,
    `git_access_token` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_frontend_config_project_info` (`project_info_id` ASC) VISIBLE,
    CONSTRAINT `fk_frontend_config_project_info`
    FOREIGN KEY (`project_info_id`)
    REFERENCES `ssafy`.`team_project_info` (`id`)
    ON DELETE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`meeting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`meeting` (
                                                 `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                 `team_id` BIGINT NULL DEFAULT NULL,
                                                 `name` VARCHAR(20) NULL DEFAULT NULL,
    `user_count` INT NULL DEFAULT NULL,
    `max_user_count` INT NULL DEFAULT NULL,
    `secret` TINYINT NULL DEFAULT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `meeting_idx_01` (`created_at` ASC, `updated_at` ASC) VISIBLE,
    INDEX `fk_team_meeting_id_team_id_idx` (`team_id` ASC) VISIBLE,
    CONSTRAINT `fk_team_meeting_id_team_id`
    FOREIGN KEY (`team_id`)
    REFERENCES `ssafy`.`team` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`member_chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`member_chat` (
                                                     `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                     `from_id` BIGINT NULL DEFAULT NULL,
                                                     `to_id` BIGINT NULL DEFAULT NULL,
                                                     `subject` VARCHAR(127) NULL DEFAULT NULL,
    `status` VARCHAR(20) NULL DEFAULT NULL,
    `time` TIMESTAMP NULL DEFAULT NULL,
    `member_chatcol` VARCHAR(45) NULL DEFAULT NULL,
    `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_member_member_chat_id_from_id_idx` (`from_id` ASC) VISIBLE,
    INDEX `fk_member_member_chat_id_to_id_idx` (`to_id` ASC) VISIBLE,
    INDEX `member_chat_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    CONSTRAINT `fk_member_member_chat_id_from_id`
    FOREIGN KEY (`from_id`)
    REFERENCES `ssafy`.`member` (`id`),
    CONSTRAINT `fk_member_member_chat_id_to_id`
    FOREIGN KEY (`to_id`)
    REFERENCES `ssafy`.`member` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`member_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`member_image` (
                                                      `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                      `member_id` BIGINT NOT NULL,
                                                      `origin_name` VARCHAR(255) NULL DEFAULT NULL,
    `fixed_name` VARCHAR(255) NULL DEFAULT NULL,
    `save_folder` VARCHAR(255) NULL DEFAULT NULL,
    `type` VARCHAR(50) NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `member_id` (`member_id` ASC) VISIBLE,
    CONSTRAINT `member_image_ibfk_1`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafy`.`member` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`member_team`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`member_team` (
                                                     `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                     `member_id` BIGINT NULL DEFAULT NULL,
                                                     `team_id` BIGINT NULL DEFAULT NULL,
                                                     `leader` TINYINT(1) NULL DEFAULT NULL,
    `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `member_team_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) INVISIBLE,
    INDEX `fk_member_member_team_id_member_id_idx` (`member_id` ASC) VISIBLE,
    INDEX `fk_team_member_team_id_team_id_idx` (`team_id` ASC) VISIBLE,
    CONSTRAINT `fk_member_member_team_id_member_id`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafy`.`member` (`id`),
    CONSTRAINT `fk_team_member_team_id_team_id`
    FOREIGN KEY (`team_id`)
    REFERENCES `ssafy`.`team` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;



-- -----------------------------------------------------
-- Table `ssafy`.`build_setting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`build_setting` (
                                                       `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `build_id` BIGINT(20) NULL,
    `git_token` VARCHAR(127) NULL,
    `jenkins_url` VARCHAR(127) NULL,
    `docker_image` VARCHAR(127) NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `build_setting_idx_01` (`created_at` ASC, `updated_at` ASC) VISIBLE,
    INDEX `fk_build_build_setting_id_build_id_idx` (`build_id` ASC) VISIBLE,
    CONSTRAINT `fk_build_build_setting_id_build_id`
    FOREIGN KEY (`build_id`)
    REFERENCES `ssafy`.`build` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ssafy`.`message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`message` (
                                                 `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                 `member_chat_id` BIGINT NULL DEFAULT NULL,
                                                 `is_from_sender` TINYINT(1) NULL DEFAULT NULL,
    `read` TINYINT(1) NULL DEFAULT NULL,
    `content` VARCHAR(127) NULL DEFAULT NULL,
    `message_visible_to` VARCHAR(20) NULL DEFAULT NULL,
    `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_member_chat_message_id_member_chat_id_idx` (`member_chat_id` ASC) VISIBLE,
    INDEX `message_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    CONSTRAINT `fk_member_chat_message_id_member_chat_id`
    FOREIGN KEY (`member_chat_id`)
    REFERENCES `ssafy`.`member_chat` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`notification`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`notification` (
                                                      `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                      `receiver` BIGINT NOT NULL,
                                                      `sender` BIGINT NOT NULL,
                                                      `type` VARCHAR(15) NOT NULL,
    `url` VARCHAR(50) NOT NULL,
    `is_read` TINYINT(1) NOT NULL,
    `notify_date` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`project` (
                                                 `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                 `name` VARCHAR(50) NULL DEFAULT NULL,
    `content` VARCHAR(255) NULL DEFAULT NULL,
    `team_id` BIGINT NULL DEFAULT NULL,
    `start_time` TIMESTAMP NULL DEFAULT NULL,
    `end_time` TIMESTAMP NULL DEFAULT NULL,
    `git_url` VARCHAR(255) NULL DEFAULT NULL,
    `jira_url` VARCHAR(255) NULL DEFAULT NULL,
    `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `project_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    INDEX `fk_team_project_id_team_id_idx` (`team_id` ASC) VISIBLE,
    CONSTRAINT `fk_team_project_id_team_id`
    FOREIGN KEY (`team_id`)
    REFERENCES `ssafy`.`team` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafy`.`recruit`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`recruit` (
                                                 `id` BIGINT NOT NULL,
                                                 `introduction` VARCHAR(50) NOT NULL,
    `thumbnail` VARCHAR(100) NOT NULL,
    `domain` VARCHAR(20) NOT NULL,
    `desired_pos_list` JSON NOT NULL,
    `team_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `team_id` (`team_id` ASC) VISIBLE,
    CONSTRAINT `recruit_ibfk_1`
    FOREIGN KEY (`id`)
    REFERENCES `ssafy`.`board` (`id`),
    CONSTRAINT `recruit_ibfk_2`
    FOREIGN KEY (`team_id`)
    REFERENCES `ssafy`.`team` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

create table if not exists `ssafy`.`resume` (
                                                `id` bigint(20) not null auto_increment,
    `title` varchar(40) not null,
    `profile` varchar(100) not null,
    `position` varchar(40) not null,
    `tech_stack` json not null,
    `comment` varchar(40) not null,
    `author_id` bigint(20) not null,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    primary key (`id`),
    foreign key (`author_id`) references `member`(`id`)
    ) ENGINE = InnoDB;

create table if not exists `ssafy`.`resume_experience` (
                                                           `id` bigint(20) not null auto_increment,
    `title` varchar(40) not null,
    `description` varchar(500) not null,
    `resume_id` bigint(20) not null,
    primary key (`id`),
    foreign key (`resume_id`) references `resume`(`id`)
    ) ENGINE = InnoDB;

create table if not exists `ssafy`.`apply` (
                                               `team_id` bigint(20) not null,
    `resume_id` bigint(20) not null,
    `status` enum('applied', 'accepted', 'rejected') not null,
    `DEL_YN` TINYINT(1) NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT NOT NULL,
    primary key(`team_id`, `resume_id`),
    foreign key(`team_id`) references `team`(`id`),
    foreign key(`resume_id`) references `resume`(`id`)
    ) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`service_schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`service_schedule` (
                                                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                          `team_id` BIGINT NULL DEFAULT NULL,
                                                          `team_project_info_id` BIGINT NULL DEFAULT NULL,
                                                          `build_status` VARCHAR(255),
    PRIMARY KEY (`id`),
    INDEX `team_id` (`team_id` ASC) VISIBLE,
    INDEX `team_project_info_id` (`team_project_info_id` ASC) VISIBLE,
    CONSTRAINT `service_schedule_ibfk_1`
    FOREIGN KEY (`team_id`)
    REFERENCES `ssafy`.`team` (`id`),
    CONSTRAINT `service_schedule_ibfk_2`
    FOREIGN KEY (`team_project_info_id`)
    REFERENCES `ssafy`.`team_project_info` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -- member dummy data 생성
-- INSERT INTO member (id, email, nickname, name, profile_url, phone, address, role, lgn_mtd, DEL_YN, REG_DTTM, REG_USER_SEQ, MOD_DTTM, MOD_USER_SEQ)
-- VALUES (3, 'ssafy@gmail.com', '싸피', '김싸피', 'https://asdf.com', '010-1234-5678', '역삼동', 'A', 'some_method', FALSE, '2024-04-25 00:00:00', 1, '2024-04-25 00:00:00', 1234);

-- -- team dummy data 생성
-- INSERT INTO team (id, name, status, content, start_time, end_time, git_url, jira_url, DEL_YN, REG_DTTM, REG_USER_SEQ, MOD_DTTM, MOD_USER_SEQ)
-- values (1, '김싸피와 아이들', 'FINISH', '인공지능 서비스를 개발하고 있습니다', '2024-04-25 00:00:00', '2024-04-25 00:00:00', 'https://www.naver.com', 'https://www.naver.com', FALSE, '2024-04-25 00:00:00', 1, '2024-04-25 00:00:00', 1234);
--
-- -- team_project_info dummy data 생성
-- INSERT INTO team_project_info (id, team_id, title)
-- values (1, 1, '1번 세팅');
--
-- -- team dummy data 생성
INSERT INTO `ssafy`.`team` (
    `name`, `status`, `content`, `start_time`, `end_time`, `git_url`, `jira_url`, `session_id`, `DEL_YN`, `REG_DTTM`, `REG_USER_SEQ`, `MOD_DTTM`, `MOD_USER_SEQ`
) VALUES
      ('Team Alpha', 'active', 'Content for team alpha', '2024-01-01 10:00:00', '2024-12-31 18:00:00', 'https://github.com/team-alpha', 'https://jira.team-alpha.com', NULL,  0, NOW(), 1, NOW(), 1),
      ('Team Beta', 'inactive', 'Content for team beta', '2024-02-01 11:00:00', '2024-11-30 17:00:00', 'https://github.com/team-beta', 'https://jira.team-beta.com', NULL,  0, NOW(), 2, NOW(), 2),
      ('Team Gamma', 'active', 'Content for team gamma', '2024-03-01 12:00:00', '2024-10-31 16:00:00', 'https://github.com/team-gamma', 'https://jira.team-gamma.com', NULL,  0, NOW(), 3, NOW(), 3),
      ('Team Delta', 'inactive', 'Content for team delta', '2024-04-01 13:00:00', '2024-09-30 15:00:00', 'https://github.com/team-delta', 'https://jira.team-delta.com', NULL,  0, NOW(), 4, NOW(), 4),
      ('Team Epsilon', 'active', 'Content for team epsilon', '2024-05-01 14:00:00', '2024-08-31 14:00:00', 'https://github.com/team-epsilon', 'https://jira.team-epsilon.com', 'session_5',  0, NOW(), 5, NOW(), 5);
--

insert into service_schedule(id, team_id, team_project_info_id, build_status)
values(1, null, null, 'SUCCESS');
insert into service_schedule(id, team_id, team_project_info_id, build_status)
values(2, null, null, 'SUCCESS');
insert into service_schedule(id, team_id, team_project_info_id, build_status)
values(3, null, null, 'SUCCESS');
insert into service_schedule(id, team_id, team_project_info_id, build_status)
values(4, null, null, 'EMPTY');
insert into service_schedule(id, team_id, team_project_info_id, build_status)
values(5, null, null, 'EMPTY');
insert into service_schedule(id, team_id, team_project_info_id, build_status)
values(6, null, null, 'EMPTY');
insert into service_schedule(id, team_id, team_project_info_id, build_status)
values(7, null, null, 'EMPTY');
insert into service_schedule(id, team_id, team_project_info_id, build_status)
values(8, null, null, 'EMPTY');
insert into service_schedule(id, team_id, team_project_info_id, build_status)
values(9, null, null, 'EMPTY');
insert into service_schedule(id, team_id, team_project_info_id, build_status)
values(10, null, null, 'EMPTY');