USE inventario;

ALTER TABLE productos
    ADD COLUMN codigo VARCHAR(60) NULL AFTER id_producto;

UPDATE productos
SET codigo = CONCAT('PROD-', id_producto)
WHERE codigo IS NULL OR codigo = '';

ALTER TABLE productos
    MODIFY COLUMN codigo VARCHAR(60) NOT NULL,
    ADD CONSTRAINT uq_productos_codigo UNIQUE (codigo);
