SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `syncme` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `syncme` ;

-- -----------------------------------------------------
-- Table `syncme`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `syncme`.`users` ;

CREATE  TABLE IF NOT EXISTS `syncme`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `regid` VARCHAR(250) NULL ,
  `firstname` VARCHAR(45) NULL ,
  `lastname` VARCHAR(45) NULL ,
  `email` VARCHAR(200) NULL ,
  `created_at` TIMESTAMP NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `syncme`.`messages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `syncme`.`messages` ;

CREATE  TABLE IF NOT EXISTS `syncme`.`messages` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `from` VARCHAR(200) NULL ,
  `to` VARCHAR(200) NULL ,
  `type` VARCHAR(45) NULL ,
  `data` TEXT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;

USE `syncme` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
