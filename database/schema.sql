CREATE DATABASE IF NOT EXISTS inventario
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE inventario;

-- BLOQUE: Usuarios
-- Para: Guardar credenciales y roles usados en el login.
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    contrasena VARCHAR(120) NOT NULL,
    rol ENUM('admin', 'usuario', 'consultor') NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- BLOQUE: Productos
-- Para: Guardar el catalogo y stock actual del inventario.
CREATE TABLE IF NOT EXISTS productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    precio DECIMAL(12, 2) NOT NULL DEFAULT 0,
    cantidad INT NOT NULL DEFAULT 0,
    CONSTRAINT chk_productos_precio CHECK (precio >= 0),
    CONSTRAINT chk_productos_cantidad CHECK (cantidad >= 0)
);

-- BLOQUE: Proveedores
-- Para: Registrar contactos de proveedores sin borrar historial.
CREATE TABLE IF NOT EXISTS proveedores (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    contacto VARCHAR(120),
    telefono VARCHAR(40),
    email VARCHAR(120),
    direccion VARCHAR(200),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- BLOQUE: Movimientos
-- Para: Auditar entradas, salidas y ajustes de cada producto.
CREATE TABLE IF NOT EXISTS movimientos (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    tipo ENUM('ENTRADA', 'SALIDA', 'AJUSTE') NOT NULL,
    cantidad INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario VARCHAR(80) NOT NULL,
    observaciones VARCHAR(255),
    CONSTRAINT fk_movimientos_producto
        FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- BLOQUE: Datos iniciales
-- Para: Poder entrar y probar el sistema despues de crear la base.
INSERT INTO usuarios (nombre, contrasena, rol)
VALUES
    ('admin', 'admin123', 'admin'),
    ('user1', 'password1', 'usuario')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);
