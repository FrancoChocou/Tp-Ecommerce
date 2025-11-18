SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ecommerce_stats
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `ecommerce_stats` ;
CREATE SCHEMA IF NOT EXISTS `ecommerce_stats` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ecommerce_stats` ;

-- -----------------------------------------------------
-- Table `ecommerce_stats`.`categorias`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ecommerce_stats`.`categorias` ;

CREATE TABLE IF NOT EXISTS `ecommerce_stats`.`categorias` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `descripcion` VARCHAR(200) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nombre` (`nombre` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce_stats`.`zonas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ecommerce_stats`.`zonas` ;

CREATE TABLE IF NOT EXISTS `ecommerce_stats`.`zonas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `provincia` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nombre` (`nombre` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce_stats`.`clientes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ecommerce_stats`.`clientes` ;

CREATE TABLE IF NOT EXISTS `ecommerce_stats`.`clientes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `apellido` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `telefono` VARCHAR(20) NULL DEFAULT NULL,
  `edad` INT NULL DEFAULT NULL,
  `id_zona` INT NULL DEFAULT NULL,
  `fecha_registro` DATE NOT NULL,
  `activo` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email` (`email` ASC) VISIBLE,
  INDEX `idx_clientes_zona` (`id_zona` ASC) VISIBLE,
  CONSTRAINT `clientes_ibfk_1`
    FOREIGN KEY (`id_zona`)
    REFERENCES `ecommerce_stats`.`zonas` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce_stats`.`metodos_pago`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ecommerce_stats`.`metodos_pago` ;

CREATE TABLE IF NOT EXISTS `ecommerce_stats`.`metodos_pago` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(30) NOT NULL,
  `descripcion` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nombre` (`nombre` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce_stats`.`productos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ecommerce_stats`.`productos` ;

CREATE TABLE IF NOT EXISTS `ecommerce_stats`.`productos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `descripcion` VARCHAR(300) NULL DEFAULT NULL,
  `id_categoria` INT NOT NULL,
  `precio_unitario` DECIMAL(10,2) NOT NULL,
  `stock` INT NOT NULL DEFAULT '0',
  `activo` TINYINT(1) NULL DEFAULT '1',
  `fecha_creacion` DATE NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_productos_categoria` (`id_categoria` ASC) VISIBLE,
  CONSTRAINT `productos_ibfk_1`
    FOREIGN KEY (`id_categoria`)
    REFERENCES `ecommerce_stats`.`categorias` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ecommerce_stats`.`ventas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ecommerce_stats`.`ventas` ;

CREATE TABLE IF NOT EXISTS `ecommerce_stats`.`ventas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_cliente` INT NOT NULL,
  `id_producto` INT NOT NULL,
  `cantidad` INT NOT NULL,
  `precio_unitario` DECIMAL(10,2) NOT NULL,
  `id_metodo_pago` INT NOT NULL,
  `total` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `id_metodo_pago` (`id_metodo_pago` ASC) VISIBLE,
  INDEX `idx_ventas_fecha` (`fecha` ASC) VISIBLE,
  INDEX `idx_ventas_cliente` (`id_cliente` ASC) VISIBLE,
  INDEX `idx_ventas_producto` (`id_producto` ASC) VISIBLE,
  CONSTRAINT `ventas_ibfk_1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `ecommerce_stats`.`clientes` (`id`),
  CONSTRAINT `ventas_ibfk_2`
    FOREIGN KEY (`id_producto`)
    REFERENCES `ecommerce_stats`.`productos` (`id`),
  CONSTRAINT `ventas_ibfk_3`
    FOREIGN KEY (`id_metodo_pago`)
    REFERENCES `ecommerce_stats`.`metodos_pago` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- INSERCIÓN DE DATOS DE REFERENCIA (ESENCIAL)
-- -----------------------------------------------------

-- Insertar categorías (ESENCIAL para que funcione el generador)
INSERT INTO `categorias` (`nombre`, `descripcion`) VALUES
('Electrodomésticos', 'Dispositivos electrónicos para el hogar'),
('Tecnología', 'Dispositivos electrónicos y gadgets'),
('Hogar', 'Artículos para el hogar y decoración'),
('Deportes', 'Equipamiento deportivo y outdoor'),
('Moda', 'Ropa y accesorios de moda');

-- Insertar zonas (ESENCIAL para que funcione el generador)
INSERT INTO `zonas` (`nombre`, `provincia`) VALUES
('Centro', 'Buenos Aires'),
('Norte', 'Buenos Aires'),
('Sur', 'Buenos Aires'),
('Este', 'Buenos Aires'),
('Oeste', 'Buenos Aires'),
('Palermo', 'Buenos Aires'),
('Recoleta', 'Buenos Aires'),
('Belgrano', 'Buenos Aires'),
('Caballito', 'Buenos Aires'),
('Flores', 'Buenos Aires');

-- Insertar métodos de pago (ESENCIAL para que funcione el generador)
INSERT INTO `metodos_pago` (`nombre`, `descripcion`) VALUES
('Tarjeta Crédito', 'Pago con tarjeta de crédito'),
('Tarjeta Débito', 'Pago con tarjeta de débito'),
('Mercado Pago', 'Pago mediante Mercado Pago'),
('Transferencia', 'Transferencia bancaria'),
('Efectivo', 'Pago en efectivo');

-- -----------------------------------------------------
-- Views
-- -----------------------------------------------------

DROP VIEW IF EXISTS `ecommerce_stats`.`vista_resumen_productos`;
CREATE VIEW `ecommerce_stats`.`vista_resumen_productos` AS 
select `p`.`id` AS `id`,`p`.`nombre` AS `nombre`,`cat`.`nombre` AS `categoria`,count(`v`.`id`) AS `cantidad_ventas`,sum(`v`.`cantidad`) AS `unidades_vendidas`,sum(`v`.`total`) AS `ingreso_total`,avg(`v`.`total`) AS `promedio_venta`,`p`.`stock` AS `stock_actual` 
from ((`ecommerce_stats`.`productos` `p` left join `ecommerce_stats`.`ventas` `v` on((`p`.`id` = `v`.`id_producto`))) join `ecommerce_stats`.`categorias` `cat` on((`p`.`id_categoria` = `cat`.`id`))) 
group by `p`.`id`,`p`.`nombre`,`cat`.`nombre`,`p`.`stock`;

DROP VIEW IF EXISTS `ecommerce_stats`.`vista_ventas_completas`;
CREATE VIEW `ecommerce_stats`.`vista_ventas_completas` AS 
select `v`.`id` AS `id`,`v`.`fecha` AS `fecha`,`c`.`nombre` AS `cliente_nombre`,`c`.`apellido` AS `cliente_apellido`,`z`.`nombre` AS `zona`,`p`.`nombre` AS `producto`,`cat`.`nombre` AS `categoria`,`v`.`cantidad` AS `cantidad`,`v`.`precio_unitario` AS `precio_unitario`,`v`.`total` AS `total`,`mp`.`nombre` AS `metodo_pago`,dayname(`v`.`fecha`) AS `dia_semana`,month(`v`.`fecha`) AS `mes`,year(`v`.`fecha`) AS `anio` 
from (((((`ecommerce_stats`.`ventas` `v` join `ecommerce_stats`.`clientes` `c` on((`v`.`id_cliente` = `c`.`id`))) join `ecommerce_stats`.`zonas` `z` on((`c`.`id_zona` = `z`.`id`))) join `ecommerce_stats`.`productos` `p` on((`v`.`id_producto` = `p`.`id`))) join `ecommerce_stats`.`categorias` `cat` on((`p`.`id_categoria` = `cat`.`id`))) join `ecommerce_stats`.`metodos_pago` `mp` on((`v`.`id_metodo_pago` = `mp`.`id`)));

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;