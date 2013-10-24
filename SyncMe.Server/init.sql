SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `syncmedb` ;
CREATE SCHEMA IF NOT EXISTS `syncmedb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `syncmedb` ;

-- -----------------------------------------------------
-- Table `syncmedb`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `syncmedb`.`users` ;

CREATE  TABLE IF NOT EXISTS `syncmedb`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `regid` VARCHAR(250) NULL ,
  `firstname` VARCHAR(45) NULL ,
  `lastname` VARCHAR(45) NULL ,
  `email` VARCHAR(200) NULL ,
  `created_at` TIMESTAMP NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `syncmedb`.`messages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `syncmedb`.`messages` ;

CREATE  TABLE IF NOT EXISTS `syncmedb`.`messages` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `type` VARCHAR(45) NULL ,
  `data` TEXT NULL ,
  `userId` INT NOT NULL ,
  PRIMARY KEY (`id`, `userId`) ,
  INDEX `fk_messages_users1_idx` (`userId` ASC) ,
  CONSTRAINT `fk_messages_users1`
    FOREIGN KEY (`userId` )
    REFERENCES `syncmedb`.`users` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `syncmedb`.`user_friends`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `syncmedb`.`user_friends` ;

CREATE  TABLE IF NOT EXISTS `syncmedb`.`user_friends` (
  `user` INT NOT NULL ,
  `friend` INT NOT NULL ,
  PRIMARY KEY (`user`, `friend`) ,
  INDEX `fk_users_has_users_users1_idx` (`friend` ASC) ,
  INDEX `fk_users_has_users_users_idx` (`user` ASC) ,
  CONSTRAINT `fk_users_has_users_users`
    FOREIGN KEY (`user` )
    REFERENCES `syncmedb`.`users` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_users_users1`
    FOREIGN KEY (`friend` )
    REFERENCES `syncmedb`.`users` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `syncmedb`.`broadcast_messages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `syncmedb`.`broadcast_messages` ;

CREATE  TABLE IF NOT EXISTS `syncmedb`.`broadcast_messages` (
  `sender` INT NOT NULL ,
  `reciever` INT NOT NULL ,
  `messageId` INT NOT NULL ,
  PRIMARY KEY (`sender`, `reciever`, `messageId`) ,
  INDEX `fk_userFriends_has_messages_messages1_idx` (`messageId` ASC) ,
  INDEX `fk_userFriends_has_messages_userFriends1_idx` (`sender` ASC, `reciever` ASC) ,
  CONSTRAINT `fk_userFriends_has_messages_userFriends1`
    FOREIGN KEY (`sender` , `reciever` )
    REFERENCES `syncmedb`.`user_friends` (`user` , `friend` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_userFriends_has_messages_messages1`
    FOREIGN KEY (`messageId` )
    REFERENCES `syncmedb`.`messages` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `syncmedb` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


/* INSERT DATA 

INSERT INTO `users`(`regid`, `firstname`, `lastname`, `email`, `created_at`) VALUES ('dasdadasd' , 'noam', 'tzumie', 'noam@gmail.com', NOW())

INSERT INTO `users`(`regid`, `firstname`, `lastname`, `email`, `created_at`) VALUES ('dasdadasd' , 'noam', 'tzumie', 'noam@gmail.com', NOW())

*/