USE inventario;

-- BLOQUE: Usuario consultor
-- Para: Probar el rol consultor, que solo puede consultar informacion.
INSERT INTO usuarios (nombre, contrasena, rol)
VALUES ('consultor', 'consultor123', 'consultor')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- BLOQUE: Compras
-- Para: Guardar cada compra principal con fecha, total, usuario y observaciones.
CREATE TABLE IF NOT EXISTS compras (
    id_compra INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(12, 2) NOT NULL DEFAULT 0,
    usuario VARCHAR(80) NOT NULL,
    observaciones VARCHAR(255),
    CONSTRAINT chk_compras_total CHECK (total >= 0)
);

-- BLOQUE: Detalle compra
-- Para: Relacionar una compra con los productos comprados y sus valores.
CREATE TABLE IF NOT EXISTS detalle_compra (
    id_detalle_compra INT AUTO_INCREMENT PRIMARY KEY,
    id_compra INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12, 2) NOT NULL DEFAULT 0,
    subtotal DECIMAL(12, 2) NOT NULL DEFAULT 0,
    CONSTRAINT chk_detalle_compra_cantidad CHECK (cantidad > 0),
    CONSTRAINT chk_detalle_compra_precio CHECK (precio_unitario >= 0),
    CONSTRAINT chk_detalle_compra_subtotal CHECK (subtotal >= 0),
    CONSTRAINT fk_detalle_compra_compra
        FOREIGN KEY (id_compra) REFERENCES compras(id_compra)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_detalle_compra_producto
        FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- BLOQUE: Ventas
-- Para: Guardar cada venta principal para relacionarla con detalle_venta.
CREATE TABLE IF NOT EXISTS ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(12, 2) NOT NULL DEFAULT 0,
    usuario VARCHAR(80) NOT NULL,
    observaciones VARCHAR(255),
    CONSTRAINT chk_ventas_total CHECK (total >= 0)
);

-- BLOQUE: Detalle venta
-- Para: Relacionar una venta con los productos vendidos y sus valores.
CREATE TABLE IF NOT EXISTS detalle_venta (
    id_detalle_venta INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12, 2) NOT NULL DEFAULT 0,
    subtotal DECIMAL(12, 2) NOT NULL DEFAULT 0,
    CONSTRAINT chk_detalle_venta_cantidad CHECK (cantidad > 0),
    CONSTRAINT chk_detalle_venta_precio CHECK (precio_unitario >= 0),
    CONSTRAINT chk_detalle_venta_subtotal CHECK (subtotal >= 0),
    CONSTRAINT fk_detalle_venta_venta
        FOREIGN KEY (id_venta) REFERENCES ventas(id_venta)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_detalle_venta_producto
        FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);
