USE inventario;

ALTER TABLE productos
    ADD COLUMN categoria VARCHAR(80) NULL AFTER nombre,
    ADD COLUMN descripcion VARCHAR(255) NULL AFTER categoria,
    ADD COLUMN stock_minimo INT NOT NULL DEFAULT 0 AFTER cantidad,
    ADD COLUMN id_proveedor INT NULL AFTER stock_minimo,
    ADD CONSTRAINT chk_productos_stock_minimo CHECK (stock_minimo >= 0),
    ADD CONSTRAINT fk_productos_proveedor
        FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor)
        ON UPDATE CASCADE
        ON DELETE SET NULL;
