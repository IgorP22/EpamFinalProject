-- MySQL Script generated by MySQL Workbench
-- Sun Nov 21 03:26:48 2021
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema provider
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema provider
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `provider` DEFAULT CHARACTER SET utf8 COLLATE utf8_hungarian_ci ;
USE `provider` ;

-- -----------------------------------------------------
-- Table `provider`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `provider`.`user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(200) NOT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `surname` VARCHAR(45) NULL DEFAULT NULL,
  `phone` VARCHAR(45) NULL DEFAULT NULL,
  `balance` DECIMAL(9,2) NULL DEFAULT 0,
  `language` ENUM('ru', 'en') NULL DEFAULT 'ru',
  `role` ENUM('user', 'admin') NULL DEFAULT 'user',
  `notification` TINYINT NULL DEFAULT 0,
  `status` ENUM('active', 'blocked') NULL DEFAULT 'blocked',
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `id_UNIQUE` (`user_id` ASC) VISIBLE,
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `provider`.`service`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `provider`.`service` (
  `service_id` INT NOT NULL AUTO_INCREMENT,
  `title_ru` VARCHAR(200) NULL,
  `title_en` VARCHAR(200) NULL,
  PRIMARY KEY (`service_id`),
  UNIQUE INDEX `id_UNIQUE` (`service_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `provider`.`tariff`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `provider`.`tariff` (
  `tariff_id` INT NOT NULL AUTO_INCREMENT,
  `name_ru` VARCHAR(2000) NULL,
  `name_en` VARCHAR(2000) NULL,
  `price` DECIMAL(9,2) NULL,
  `service` INT NULL,
  PRIMARY KEY (`tariff_id`),
  UNIQUE INDEX `id_UNIQUE` (`tariff_id` ASC) VISIBLE,
  INDEX `serv_idx` (`service` ASC) VISIBLE,
  CONSTRAINT `serv`
    FOREIGN KEY (`service`)
    REFERENCES `provider`.`service` (`service_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `provider`.`users_tafiffs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `provider`.`users_tafiffs` (
  `user_id` INT NOT NULL,
  `tariff_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `tariff_id`),
  INDEX `tar_id_idx` (`tariff_id` ASC) VISIBLE,
  CONSTRAINT `usr_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `provider`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `tar_id`
    FOREIGN KEY (`tariff_id`)
    REFERENCES `provider`.`tariff` (`tariff_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `provider`.`user_payments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `provider`.`user_payments` (
  `user_id` INT NOT NULL,
  `date` DATETIME NOT NULL,
  `sum` DECIMAL(9,2) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `id_UNIQUE` (`user_id` ASC) VISIBLE,
  CONSTRAINT `id`
    FOREIGN KEY (`user_id`)
    REFERENCES `provider`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
