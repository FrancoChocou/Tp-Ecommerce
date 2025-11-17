-- Eliminar base de datos existente
DROP DATABASE IF EXISTS ecommerce_stats;

-- Crear base de datos nueva
CREATE DATABASE ecommerce_stats;
USE ecommerce_stats;

-- Tabla categorías (SIMPLIFICADA - sin zona)
CREATE TABLE categorias (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  descripcion VARCHAR(200) NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX nombre (nombre)
);

-- Tabla métodos_pago (SIMPLIFICADA)
CREATE TABLE metodos_pago (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(30) NOT NULL,
  descripcion VARCHAR(100) NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX nombre (nombre)
);

-- Tabla clientes (ADAPTADA - sin zona)
CREATE TABLE clientes (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  apellido VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  telefono VARCHAR(20) NULL,
  edad INT NULL,
  id_zona INT NULL DEFAULT 1,  -- Mantenemos el campo pero con valor por defecto
  fecha_registro DATE NOT NULL,
  activo TINYINT(1) NULL DEFAULT 1,
  PRIMARY KEY (id),
  UNIQUE INDEX email (email)
);

-- Tabla productos
CREATE TABLE productos (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(300) NULL,
  id_categoria INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL DEFAULT 0,
  activo TINYINT(1) NULL DEFAULT 1,
  fecha_creacion DATE NOT NULL,
  PRIMARY KEY (id),
  INDEX idx_productos_categoria (id_categoria),
  FOREIGN KEY (id_categoria) REFERENCES categorias(id)
);

-- Tabla ventas
CREATE TABLE ventas (
  id INT NOT NULL AUTO_INCREMENT,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  id_cliente INT NOT NULL,
  id_producto INT NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  id_metodo_pago INT NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  INDEX id_metodo_pago (id_metodo_pago),
  INDEX idx_ventas_fecha (fecha),
  INDEX idx_ventas_cliente (id_cliente),
  INDEX idx_ventas_producto (id_producto),
  FOREIGN KEY (id_cliente) REFERENCES clientes(id),
  FOREIGN KEY (id_producto) REFERENCES productos(id),
  FOREIGN KEY (id_metodo_pago) REFERENCES metodos_pago(id)
); 

-- Insertar categorías
INSERT INTO categorias (nombre, descripcion) VALUES
('Electrónicos', 'Dispositivos electrónicos y tecnología'),
('Ropa', 'Prendas de vestir'),
('Hogar', 'Artículos para el hogar'),
('Deportes', 'Equipamiento deportivo'),
('Libros', 'Libros físicos y digitales');

-- Insertar métodos de pago
INSERT INTO metodos_pago (nombre, descripcion) VALUES
('Tarjeta Crédito', 'Pago con tarjeta de crédito'),
('Tarjeta Débito', 'Pago con tarjeta de débito'),
('Efectivo', 'Pago en efectivo'),
('Transferencia', 'Transferencia bancaria'),
('Mercado Pago', 'Pago mediante Mercado Pago');  

