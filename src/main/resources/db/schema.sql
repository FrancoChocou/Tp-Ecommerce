-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS peluqueria_db;
USE peluqueria_db;

-- Tabla de clientes
CREATE TABLE IF NOT EXISTS clientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(150),
    fecha_registro DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de servicios
CREATE TABLE IF NOT EXISTS servicios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    duracion_minutos INT NOT NULL,
    tipo_servicio VARCHAR(50) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de turnos
CREATE TABLE IF NOT EXISTS turnos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cliente_id INT NOT NULL,
    servicio_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    notas TEXT,
    estado VARCHAR(20) NOT NULL DEFAULT 'CONFIRMADO',
    estado_pago VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    forma_pago VARCHAR(20),
    monto_pagado DECIMAL(10,2) DEFAULT 0.00,
    fecha_creacion DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Claves foráneas
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE,
    FOREIGN KEY (servicio_id) REFERENCES servicios(id) ON DELETE CASCADE,
    
    -- Índices para mejor performance
    INDEX idx_fecha_hora (fecha_hora),
    INDEX idx_cliente_id (cliente_id),
    INDEX idx_estado (estado),
    INDEX idx_estado_pago (estado_pago)
);

-- Insertar datos de ejemplo para servicios
INSERT INTO servicios (nombre, descripcion, precio, duracion_minutos, tipo_servicio) VALUES
('Corte Caballero', 'Corte de cabello para hombres', 25.00, 30, 'CORTE'),
('Corte Dama', 'Corte de cabello para mujeres', 35.00, 45, 'CORTE'),
('Corte Niño', 'Corte de cabello para niños', 20.00, 25, 'CORTE'),
('Tintura Completa', 'Tintura de cabello completo', 80.00, 120, 'TINTURA'),
('Mechas', 'Aplicación de mechas', 120.00, 150, 'MECHAS'),
('Alisado Brasileño', 'Tratamiento de alisado brasileño', 200.00, 180, 'ALISADO'),
('Peinado Novia', 'Peinado especial para novias', 150.00, 90, 'PEINADO'),
('Arreglo Barba', 'Arreglo y perfilado de barba', 15.00, 20, 'BARBA'),
('Cejas', 'Perfilado de cejas', 10.00, 15, 'CEJAS'),
('Tratamiento Keratina', 'Tratamiento con keratina', 90.00, 60, 'TRATAMIENTO');

-- Insertar algunos clientes de ejemplo
INSERT INTO clientes (nombre, apellido, telefono, email, fecha_registro) VALUES
('María', 'González', '123456789', 'maria.gonzalez@email.com', CURDATE()),
('Carlos', 'López', '987654321', 'carlos.lopez@email.com', CURDATE()),
('Ana', 'Martínez', '555444333', 'ana.martinez@email.com', CURDATE()),
('Pedro', 'Rodríguez', '111222333', 'pedro.rodriguez@email.com', CURDATE()),
('Laura', 'Fernández', '444555666', 'laura.fernandez@email.com', CURDATE());

-- Insertar algunos turnos de ejemplo
INSERT INTO turnos (cliente_id, servicio_id, fecha_hora, notas, estado, estado_pago, forma_pago, monto_pagado, fecha_creacion) VALUES
(1, 1, DATE_ADD(NOW(), INTERVAL 1 DAY), 'Cliente prefiere corte corto', 'CONFIRMADO', 'PENDIENTE', NULL, 0.00, NOW()),
(2, 3, DATE_ADD(NOW(), INTERVAL 2 DAY), 'Niño de 8 años', 'CONFIRMADO', 'PENDIENTE', NULL, 0.00, NOW()),
(3, 5, DATE_ADD(NOW(), INTERVAL 3 DAY), 'Mechas californianas', 'CONFIRMADO', 'PENDIENTE', NULL, 0.00, NOW()),
(4, 2, DATE_ADD(NOW(), INTERVAL 1 DAY), 'Corte con capas', 'CONFIRMADO', 'PENDIENTE', NULL, 0.00, NOW());