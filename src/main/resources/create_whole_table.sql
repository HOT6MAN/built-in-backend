drop database if exists `ssafy`;
create database if not exists `ssafy`;
use `ssafy`;

-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Table `ssafy`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`member` (
                                                `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(50) NOT NULL,
    `nickname` VARCHAR(50) NOT NULL,
    `name` VARCHAR(20) NOT NULL,
    `profile_url` VARCHAR(255),
    `phone` VARCHAR(20),
    `address` VARCHAR(255),
    `role` varchar(50) NOT NULL,
    `lgn_mtd` varchar(50) NOT NULL,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `member_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE)
    ENGINE = InnoDB;
-- -----------------------------------------------------
-- Table `ssafy`.`member_image`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `ssafy`.`member_image` (
                                                      `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT(20) NOT NULL,
    `origin_name` VARCHAR(255),
    `fixed_name` VARCHAR(255),
    `save_folder` VARCHAR(255),
    `type` VARCHAR(50),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`member_id`) REFERENCES `member`(`id`)
    ) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`service_schedule`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `ssafy`.`service_schedule` (
                                                          `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `team_id` BIGINT(20) ,
    `is_used` VARCHAR(255),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`team_id`) REFERENCES `team`(`id`)
    ) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`member_project_credential`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `ssafy`.`member_project_credential` (
                                                                   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT(20) NOT NULL,
    `job_name` VARCHAR(30) NOT NULL,
    `git_username` VARCHAR(30) NOT NULL,
    `git_token` VARCHAR(100) NOT NULL,
    `docker_username` VARCHAR(30) NOT NULL,
    `docker_token` VARCHAR(100) NOT NULL,
    `git_credential_id` VARCHAR(50),
    `docker_credential_id` VARCHAR(50),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`member_id`) REFERENCES `member`(`id`)
    ) ENGINE = InnoDB;
-- -----------------------------------------------------
-- Table `ssafy`.`team_project_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`team_project_info` (
                                                           `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `team_id` BIGINT(20),
    `title` VARCHAR(30),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_team_project_info_team`
    FOREIGN KEY (`team_id`)
    REFERENCES `ssafy`.`team` (`id`)
    ON DELETE CASCADE
    ) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`backend_config`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`backend_config` (
                                                        `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `project_info_id` BIGINT(20),
    `backend_job_name` VARCHAR(255),
    `git_url` VARCHAR(255),
    `git_branch` VARCHAR(255),
    `git_username` VARCHAR(255),
    `git_access_token` VARCHAR(255),
    `language` VARCHAR(255),
    `language_version` VARCHAR(255),
    `framework` VARCHAR(255),
    `build_tool` VARCHAR(255),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_backend_config_project_info`
    FOREIGN KEY (`project_info_id`)
    REFERENCES `ssafy`.`team_project_info` (`id`)
    ON DELETE CASCADE
    ) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`frontend_config`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`frontend_config` (
                                                         `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `project_info_id` BIGINT(20),
    `frontend_job_name` VARCHAR(255),
    `framework` VARCHAR(255),
    `version` VARCHAR(255),
    `git_url` VARCHAR(255),
    `git_branch` VARCHAR(255),
    `git_username` VARCHAR(255),
    `git_access_token` VARCHAR(255),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_frontend_config_project_info`
    FOREIGN KEY (`project_info_id`)
    REFERENCES `ssafy`.`team_project_info` (`id`)
    ON DELETE CASCADE
    ) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`database_config`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`database_config` (
                                                         `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `project_info_id` BIGINT(20),
    `database_job_name` VARCHAR(255),
    `url` VARCHAR(255),
    `schema_name` VARCHAR(255),
    `username` VARCHAR(255),
    `password` VARCHAR(255),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_database_config_project_info`
    FOREIGN KEY (`project_info_id`)
    REFERENCES `ssafy`.`team_project_info` (`id`)
    ON DELETE CASCADE
    ) ENGINE = InnoDB;
-- -----------------------------------------------------
-- Table `ssafy`.`following`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`following` (
                                                   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,

    `follower_id` BIGINT(20) NULL,
    `followed_id` BIGINT(20) NULL,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_member_following_id_follower_id_idx` (`follower_id` ASC) VISIBLE,
    INDEX `fk_member_following_id_followed_id_idx` (`followed_id` ASC) VISIBLE,
    INDEX `following_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    CONSTRAINT `fk_member_following_id_follower_id`
    FOREIGN KEY (`follower_id`)
    REFERENCES `ssafy`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_member_following_id_followed_id`
    FOREIGN KEY (`followed_id`)
    REFERENCES `ssafy`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`tech`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`tech` (
                                              `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NULL,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `tech_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`member_tech`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`member_tech` (
                                                     `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT(20) NULL,
    `tech_id` BIGINT(20) NULL,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `member_tech_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    INDEX `fk_member_member_tech_id_member_id_idx` (`member_id` ASC) VISIBLE,
    INDEX `fk_tech_member_tech_id_tech_id_idx` (`tech_id` ASC) VISIBLE,
    CONSTRAINT `fk_member_member_tech_id_member_id`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafy`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_tech_member_tech_id_tech_id`
    FOREIGN KEY (`tech_id`)
    REFERENCES `ssafy`.`tech` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`member_chat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`member_chat` (
                                                     `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `from_id` BIGINT(20) NULL,
    `to_id` BIGINT(20) NULL,
    `subject` VARCHAR(127) NULL,
    `status` VARCHAR(20) NULL,
    `time` TIMESTAMP NULL,
    `member_chatcol` VARCHAR(45) NULL,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_member_member_chat_id_from_id_idx` (`from_id` ASC) VISIBLE,
    INDEX `fk_member_member_chat_id_to_id_idx` (`to_id` ASC) VISIBLE,
    INDEX `member_chat_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    CONSTRAINT `fk_member_member_chat_id_from_id`
    FOREIGN KEY (`from_id`)
    REFERENCES `ssafy`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_member_member_chat_id_to_id`
    FOREIGN KEY (`to_id`)
    REFERENCES `ssafy`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`message` (
                                                 `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `member_chat_id` BIGINT(20) NULL,
    `is_from_sender` BOOLEAN NULL,
    `read` BOOLEAN NULL,
    `content` VARCHAR(127) NULL,
    `message_visible_to` VARCHAR(20) NULL,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_member_chat_message_id_member_chat_id_idx` (`member_chat_id` ASC) VISIBLE,
    INDEX `message_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    CONSTRAINT `fk_member_chat_message_id_member_chat_id`
    FOREIGN KEY (`member_chat_id`)
    REFERENCES `ssafy`.`member_chat` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`team`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`team` (
                                              `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NULL,
    `status` VARCHAR(20) NULL,
    `content` VARCHAR(255) NULL,
    `start_time` TIMESTAMP NULL,
    `end_time` TIMESTAMP NULL,
    `git_url` VARCHAR(255) NULL,
    `jira_url` VARCHAR(255) NULL,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `team_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`member_team`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`member_team` (
                                                     `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT(20) NULL,
    `team_id` BIGINT(20) NULL,
    `leader` BOOLEAN NULL,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `member_team_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) INVISIBLE,
    INDEX `fk_member_member_team_id_member_id_idx` (`member_id` ASC) VISIBLE,
    INDEX `fk_team_member_team_id_team_id_idx` (`team_id` ASC) VISIBLE,
    CONSTRAINT `fk_member_member_team_id_member_id`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafy`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_team_member_team_id_team_id`
    FOREIGN KEY (`team_id`)
    REFERENCES `ssafy`.`team` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`project` (
                                                 `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NULL,
    `content` VARCHAR(255) NULL,
    `team_id` BIGINT(20) NULL,
    `start_time` TIMESTAMP NULL,
    `end_time` TIMESTAMP NULL,
    `git_url` VARCHAR(255) NULL,
    `jira_url` VARCHAR(255) NULL,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `project_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE,
    INDEX `fk_team_project_id_team_id_idx` (`team_id` ASC) VISIBLE,
    CONSTRAINT `fk_team_project_id_team_id`
    FOREIGN KEY (`team_id`)
    REFERENCES `ssafy`.`team` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


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
-- Table `ssafy`.`meeting`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`meeting` (
                                                 `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `team_id` BIGINT(20) NULL,
    `name` VARCHAR(20) NULL,
    `user_count` INT NULL,
    `max_user_count` INT NULL,
    `secret` TINYINT NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `meeting_idx_01` (`created_at` ASC, `updated_at` ASC) VISIBLE,
    INDEX `fk_team_meeting_id_team_id_idx` (`team_id` ASC) VISIBLE,
    CONSTRAINT `fk_team_meeting_id_team_id`
    FOREIGN KEY (`team_id`)
    REFERENCES `ssafy`.`team` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ssafy`.`apply_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`apply_info` (
                                                    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `team_id` BIGINT(20) NULL,
    `member_id` BIGINT(20) NULL,
    `read` TINYINT NULL,
    `content` VARCHAR(255) NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `apply_info_idx_01` (`created_at` ASC, `updated_at` ASC) INVISIBLE,
    INDEX `fk_team_apply_info_id_team_id_idx` (`team_id` ASC) VISIBLE,
    INDEX `fk_member_apply_info_id_member_id_idx` (`member_id` ASC) VISIBLE,
    CONSTRAINT `fk_team_apply_info_id_team_id`
    FOREIGN KEY (`team_id`)
    REFERENCES `ssafy`.`team` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_member_apply_info_id_member_id`
    FOREIGN KEY (`member_id`)
    REFERENCES `ssafy`.`member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ssafy`.`apply_info_position`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`apply_info_position` (
                                                             `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `position_id` BIGINT(20) NULL,
    `apply_info_id` BIGINT(20) NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `apply_info_position_idx_01` (`updated_at` ASC, `created_at` ASC) VISIBLE,
    INDEX `fk_position_apply_info_position_id_position_id_idx` (`position_id` ASC) VISIBLE,
    INDEX `fk_apply_info_apply_info_position_id_apply_info_id_idx` (`apply_info_id` ASC) VISIBLE,
    CONSTRAINT `fk_position_apply_info_position_id_position_id`
    FOREIGN KEY (`position_id`)
    REFERENCES `ssafy`.`position` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_apply_info_apply_info_position_id_apply_info_id`
    FOREIGN KEY (`apply_info_id`)
    REFERENCES `ssafy`.`apply_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ssafy`.`apply_info_tech`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`apply_info_tech` (
                                                         `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `tech_id` BIGINT(20) NULL,
    `apply_info_id` BIGINT(20) NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `apply_info_tech_idx_01` (`created_at` ASC, `updated_at` ASC) VISIBLE,
    INDEX `fk_tech_apply_info_tech_id_tech_id_idx` (`tech_id` ASC) VISIBLE,
    INDEX `fk_apply_info_apply_info_tech_id_apply_info_id_idx` (`apply_info_id` ASC) VISIBLE,
    CONSTRAINT `fk_tech_apply_info_tech_id_tech_id`
    FOREIGN KEY (`tech_id`)
    REFERENCES `ssafy`.`tech` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_apply_info_apply_info_tech_id_apply_info_id`
    FOREIGN KEY (`apply_info_id`)
    REFERENCES `ssafy`.`apply_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ssafy`.`apply_info_domain`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`apply_info_domain` (
                                                           `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `domain_id` BIGINT(20) NULL,
    `apply_info_id` BIGINT(20) NULL,
    `created_at` TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `apply_info_domain_idx_01` (`created_at` ASC, `updated_at` ASC) VISIBLE,
    INDEX `fk_domain_apply_info_domain_id_domain_id_idx` (`domain_id` ASC) VISIBLE,
    INDEX `fk_apply_info_apply_info_domain_id_apply_info_id_idx` (`apply_info_id` ASC) VISIBLE,
    CONSTRAINT `fk_domain_apply_info_domain_id_domain_id`
    FOREIGN KEY (`domain_id`)
    REFERENCES `ssafy`.`domain` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_apply_info_apply_info_domain_id_apply_info_id`
    FOREIGN KEY (`apply_info_id`)
    REFERENCES `ssafy`.`apply_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`notification`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`notification` (
                                                      `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `receiver` BIGINT NOT NULL,
    `sender` BIGINT NOT NULL,
    `type` VARCHAR(15) NOT NULL,
    `url` VARCHAR(50) NOT NULL,
    `is_read` BOOLEAN NOT NULL,
    `notify_date` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`chatroom`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`chatroom` (
                                                  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(30),
    `create_date` TIMESTAMP,
    `last_message` VARCHAR(200),
    `last_message_date` TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ssafy`.`chatroom_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafy`.`chatroom_status` (
                                                         `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `chatroom_id` BIGINT(20) NOT NULL,
    `user_id` BIGINT(20) NOT NULL,
    `unread_count` INT DEFAULT 0,
    `online` BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`chatroom_id`) REFERENCES `chatroom`(`id`),
    UNIQUE (`chatroom_id`, `user_id`)
    ) ENGINE = InnoDB;

create table if not exists `ssafy`.`board` (
                                               `id` bigint(20) not null auto_increment,
    `title` varchar(40) not null,
    `type` enum('RECRUIT') not null,
    `content` varchar(500) not null,
    `hit` int not null,
    `author_id` bigint(20) not null,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    primary key (`id`),
    foreign key (`author_id`) references `member`(`id`)
    ) ENGINE = InnoDB;

create table if not exists `ssafy`.`recruit` (
                                                 `id` bigint(20) not null,
    `introduction` varchar(50) not null,
    `thumbnail` varchar(100) not null,
    `domain` varchar(20) not null,
    `desired_pos_list` json not null,
    `team_id` bigint(20) not null,
    primary key (`id`),
    foreign key (`id`) references `board`(`id`),
    foreign key (`team_id`) references `team`(`id`)
    ) ENGINE = InnoDB;

-- build_result table 생성
CREATE TABLE if not exists `ssafy`.`build_result` (
                                                      `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                      `team_project_info_id` BIGINT,
                                                      `deploy_num` BIGINT,
                                                      `status` VARCHAR(255),
    `build_time` TIMESTAMP,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    FOREIGN KEY (team_project_info_id) REFERENCES team_project_info(id),
    INDEX `build_result_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE) ENGINE = InnoDB;

CREATE TABLE if not exists `ssafy`.`build_stage` (
                                                     `id` BIGINT AUTO_INCREMENT NOT NULL,
                                                     `build_result_id` BIGINT,
                                                     `name` VARCHAR(255),
    `stage_id` BIGINT,
    `status` VARCHAR(255),
    `duration` INT,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (build_result_id) REFERENCES build_result(id),
    INDEX `build_stage_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE) ENGINE = InnoDB;

CREATE TABLE if not exists `ssafy`.`build_log` (
                                                   `id` BIGINT AUTO_INCREMENT NOT NULL,
                                                   `build_stage_id` BIGINT,
                                                   `title` VARCHAR(255),
    `description` TEXT,
    `DEL_YN` BOOLEAN NOT NULL,
    `REG_DTTM` TIMESTAMP NOT NULL,
    `REG_USER_SEQ` BIGINT(20) NOT NULL,
    `MOD_DTTM` TIMESTAMP NOT NULL,
    `MOD_USER_SEQ` BIGINT(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (build_stage_id) REFERENCES build_stage(id),
    INDEX `build_log_idx_01` (`DEL_YN` ASC, `REG_USER_SEQ` ASC, `MOD_USER_SEQ` ASC) VISIBLE) ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- member dummy data 생성
INSERT INTO member (id, email, nickname, name, profile_url, phone, address, role, lgn_mtd, DEL_YN, REG_DTTM, REG_USER_SEQ, MOD_DTTM, MOD_USER_SEQ)
VALUES (1, 'ssafy@gmail.com', '싸피', '김싸피', 'https://asdf.com', '010-1234-5678', '역삼동', 'A', 'some_method', FALSE, '2024-04-25 00:00:00', 1, '2024-04-25 00:00:00', 1234);

-- team dummy data 생성
INSERT INTO team (id, name, status, content, start_time, end_time, git_url, jira_url, DEL_YN, REG_DTTM, REG_USER_SEQ, MOD_DTTM, MOD_USER_SEQ)
values (1, '김싸피와 아이들', 'FINISH', '인공지능 서비스를 개발하고 있습니다', '2024-04-25 00:00:00', '2024-04-25 00:00:00', 'https://www.naver.com', 'https://www.naver.com', FALSE, '2024-04-25 00:00:00', 1, '2024-04-25 00:00:00', 1234);

-- team_project_info dummy data 생성
INSERT INTO team_project_info (id, team_id, title)
values (1, 1, '1번 세팅');

