drop database if exists `ssafy`;
create database `ssafy`;
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



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

