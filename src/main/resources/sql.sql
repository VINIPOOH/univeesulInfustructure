-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema trainingdb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `trainingdb` ;

-- -----------------------------------------------------
-- Schema trainingdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `trainingdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `trainingdb` ;

-- -----------------------------------------------------
-- Table `trainingdb`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trainingdb`.`user` ;

CREATE TABLE IF NOT EXISTS `trainingdb`.`user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `account_non_expired` BIT(1) NOT NULL DEFAULT b'1',
  `account_non_locked` BIT(1) NOT NULL DEFAULT b'1',
  `credentials_non_expired` BIT(1) NOT NULL DEFAULT b'1',
  `email` VARCHAR(255) NOT NULL,
  `enabled` BIT(1) NOT NULL DEFAULT b'1',
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(255) NOT NULL DEFAULT 'ROLE_USER',
  `user_money_in_cents` BIGINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE UNIQUE INDEX `UK_ob8kqyqqgmefl0aco34akdtpe` ON `trainingdb`.`user` (`email` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trainingdb`.`locality`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trainingdb`.`locality` ;

CREATE TABLE IF NOT EXISTS `trainingdb`.`locality` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name_en` VARCHAR(255) NOT NULL,
  `name_ru` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trainingdb`.`way`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trainingdb`.`way` ;

CREATE TABLE IF NOT EXISTS `trainingdb`.`way` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `distance_in_kilometres` INT NOT NULL,
  `price_for_kilometer_in_cents` INT NOT NULL,
  `time_on_way_in_days` INT NOT NULL,
  `locality_get_id` BIGINT NOT NULL,
  `locality_send_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK9cdoukvc31ea903e9gaa8fijl`
    FOREIGN KEY (`locality_get_id`)
    REFERENCES `trainingdb`.`locality` (`id`),
  CONSTRAINT `FKo7ugu1yg0aawpbvtwhyhn4glk`
    FOREIGN KEY (`locality_send_id`)
    REFERENCES `trainingdb`.`locality` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE UNIQUE INDEX `UK5uyqyuwallk6ncd2r78h6e2gt` ON `trainingdb`.`way` (`locality_send_id` ASC, `locality_get_id` ASC) VISIBLE;

CREATE INDEX `FK9cdoukvc31ea903e9gaa8fijl` ON `trainingdb`.`way` (`locality_get_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trainingdb`.`delivery`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trainingdb`.`delivery` ;

CREATE TABLE IF NOT EXISTS `trainingdb`.`delivery` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `is_package_received` BIT(1) NULL DEFAULT b'0',
  `weight` INT NOT NULL,
  `addressee_id` BIGINT NOT NULL,
  `way_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKmh780m8tinmu5vv2k2yvgapyy`
    FOREIGN KEY (`addressee_id`)
    REFERENCES `trainingdb`.`user` (`id`),
  CONSTRAINT `FKtq40wblx9awh2fum7c11ik1c1`
    FOREIGN KEY (`way_id`)
    REFERENCES `trainingdb`.`way` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX `FKmh780m8tinmu5vv2k2yvgapyy` ON `trainingdb`.`delivery` (`addressee_id` ASC) VISIBLE;

CREATE INDEX `FKtq40wblx9awh2fum7c11ik1c1` ON `trainingdb`.`delivery` (`way_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trainingdb`.`bill`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trainingdb`.`bill` ;

CREATE TABLE IF NOT EXISTS `trainingdb`.`bill` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `cost_in_cents` BIGINT NOT NULL,
  `date_of_pay` DATE NULL DEFAULT NULL,
  `is_delivery_paid` BIT(1) NULL DEFAULT b'0',
  `delivery_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK8i5q3657tfusgk53x7i4r5w04`
    FOREIGN KEY (`delivery_id`)
    REFERENCES `trainingdb`.`delivery` (`id`),
  CONSTRAINT `FKqhq5aolak9ku5x5mx11cpjad9`
    FOREIGN KEY (`user_id`)
    REFERENCES `trainingdb`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX `FK8i5q3657tfusgk53x7i4r5w04` ON `trainingdb`.`bill` (`delivery_id` ASC) VISIBLE;

CREATE INDEX `FKqhq5aolak9ku5x5mx11cpjad9` ON `trainingdb`.`bill` (`user_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trainingdb`.`tariff_weight_factor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trainingdb`.`tariff_weight_factor` ;

CREATE TABLE IF NOT EXISTS `trainingdb`.`tariff_weight_factor` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `max_weight_range` INT NOT NULL,
  `min_weight_range` INT NOT NULL,
  `over_pay_on_kilometer` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `trainingdb`.`way_tariff_weight_factor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trainingdb`.`way_tariff_weight_factor` ;

CREATE TABLE IF NOT EXISTS `trainingdb`.`way_tariff_weight_factor` (
  `way_id` BIGINT NOT NULL,
  `tariff_weight_factor_id` BIGINT NOT NULL,
  CONSTRAINT `FK5bpbgkkh4sw0tds9gilau5uau`
    FOREIGN KEY (`way_id`)
    REFERENCES `trainingdb`.`way` (`id`),
  CONSTRAINT `FKt75qu3a8h7qsy726oe3ylpea6`
    FOREIGN KEY (`tariff_weight_factor_id`)
    REFERENCES `trainingdb`.`tariff_weight_factor` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX `FKt75qu3a8h7qsy726oe3ylpea6` ON `trainingdb`.`way_tariff_weight_factor` (`tariff_weight_factor_id` ASC) VISIBLE;

CREATE INDEX `FK5bpbgkkh4sw0tds9gilau5uau` ON `trainingdb`.`way_tariff_weight_factor` (`way_id` ASC) VISIBLE;

INSERT INTO `trainingdb`.`user` (`account_non_expired`, `account_non_locked`, `credentials_non_expired`, `email`, `enabled`, `password`, `role`, `user_money_in_cents`) VALUES (b'1', b'1', b'1', 'adminSpring@ukr.net', b'1', '$2a$10$SwoEEvxPuCmPHJPDAqsXJ.MilZLjMBYHiP.ugcDE413zRjEjtjBKy', 'ROLE_ADMIN', b'0');
INSERT INTO `trainingdb`.`user` (`account_non_expired`, `account_non_locked`, `credentials_non_expired`, `email`, `enabled`, `password`, `role`, `user_money_in_cents`) VALUES (b'1', b'1', b'1', 'userSpring@ukr.net', b'1', '$2a$10$gZ27Uy0Yw.xw6IsGT0UL/e8dSJiuOeys6pEiXchSLyMEverNl6KDW', 'ROLE_USER', b'0');
INSERT INTO `trainingdb`.`user` (`account_non_expired`, `account_non_locked`, `credentials_non_expired`, `email`, `enabled`, `password`, `role`, `user_money_in_cents`) VALUES (b'1', b'1', b'1', 'adminServlet@ukr.net', b'1', '21232f297a57a5a743894a0e4a801fc3', 'ROLE_ADMIN', b'0');
INSERT INTO `trainingdb`.`user` (`account_non_expired`, `account_non_locked`, `credentials_non_expired`, `email`, `enabled`, `password`, `role`, `user_money_in_cents`) VALUES (b'1', b'1', b'1', 'userServlet@ukr.net', b'1', 'ee11cbb19052e40b07aac0ca060c23ee', 'ROLE_USER', b'0');

INSERT INTO `trainingdb`.`tariff_weight_factor` (`max_weight_range`, `min_weight_range`, `over_pay_on_kilometer`) VALUES ('100', '0', '100');
INSERT INTO `trainingdb`.`tariff_weight_factor` (`max_weight_range`, `min_weight_range`, `over_pay_on_kilometer`) VALUES ('200', '100', '200');
INSERT INTO `trainingdb`.`tariff_weight_factor` (`max_weight_range`, `min_weight_range`, `over_pay_on_kilometer`) VALUES ('300', '200', '300');

INSERT INTO `trainingdb`.`locality` (`name_en`, `name_ru`) VALUES ('Kiev', 'Киев');
INSERT INTO `trainingdb`.`locality` (`name_en`, `name_ru`) VALUES ('Kharkov', 'Харьков');
INSERT INTO `trainingdb`.`locality` (`name_en`, `name_ru`) VALUES ('Odessa', 'Одесса');

INSERT INTO `trainingdb`.`way` (`distance_in_kilometres`, `price_for_kilometer_in_cents`, `time_on_way_in_days`, `locality_get_id`, `locality_send_id`) VALUES ('526', '13', '1', '1', '2');
INSERT INTO `trainingdb`.`way` (`distance_in_kilometres`, `price_for_kilometer_in_cents`, `time_on_way_in_days`, `locality_get_id`, `locality_send_id`) VALUES ('475', '20', '1', '1', '3');
INSERT INTO `trainingdb`.`way` (`distance_in_kilometres`, `price_for_kilometer_in_cents`, `time_on_way_in_days`, `locality_get_id`, `locality_send_id`) VALUES ('670', '15', '2', '2', '3');
INSERT INTO `trainingdb`.`way` (`distance_in_kilometres`, `price_for_kilometer_in_cents`, `time_on_way_in_days`, `locality_get_id`, `locality_send_id`) VALUES ('540', '14', '1', '2', '1');
INSERT INTO `trainingdb`.`way` (`distance_in_kilometres`, `price_for_kilometer_in_cents`, `time_on_way_in_days`, `locality_get_id`, `locality_send_id`) VALUES ('490', '18', '1', '3', '1');
-- INSERT INTO `trainingdb`.`way` (`distance_in_kilometres`, `price_for_kilometer_in_cents`, `time_on_way_in_days`, `locality_get_id`, `locality_send_id`) VALUES ('700', '22', '2', '3', '2');


insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(1,1);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(1,2);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(1,3);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(2,1);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(2,2);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(2,3);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(3,1);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(3,2);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(3,3);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(4,1);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(4,2);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(4,3);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(5,1);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(5,2);
insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(5,3);
-- insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(6,1);
-- insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(6,2);
-- insert into `trainingdb`.`way_tariff_weight_factor` (`way_id`, `tariff_weight_factor_id`) values(6,3);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;