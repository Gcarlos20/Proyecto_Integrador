package model;

import java.time.LocalDateTime;

/**
 * MODELO: Movimiento
 * Descripción: Registra todos los movimientos de inventario (entrada, salida, ajuste)
 * Campos: id, producto, tipo, cantidad, fecha, usuario, observaciones
 */
public class movimiento {
    private int id;
    private int productoId;
    private String TIPO_MOVIMIENTO; // ENTRADA, SALIDA, AJUSTE
    private int cantidad;
    private LocalDateTime fecha;
    private String usuario;
    private String observaciones;

    // Constructor completo
    public movimiento(int id, int productoId, String TIPO_MOVIMIENTO, int cantidad, LocalDateTime fecha, String usuario, String observaciones) {
        this.id = id;
        this.productoId = productoId;
        this.TIPO_MOVIMIENTO = TIPO_MOVIMIENTO;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.usuario = usuario;
        this.observaciones = observaciones;
    }

    // Constructor sin ID (para nuevos movimientos)
    public movimiento(int productoId, String TIPO_MOVIMIENTO, int cantidad, String usuario, String observaciones) {
        this.productoId = productoId;
        this.TIPO_MOVIMIENTO = TIPO_MOVIMIENTO;
        this.cantidad = cantidad;
        this.fecha = LocalDateTime.now();
        this.usuario = usuario;
        this.observaciones = observaciones;
    }

    // Getters
    public int getId() { return id; }
    public int getProductoId() { return productoId; }
    public String getTipo() { return TIPO_MOVIMIENTO; }
    public int getCantidad() { return cantidad; }
    public LocalDateTime getFecha() { return fecha; }
    public String getUsuario() { return usuario; }
    public String getObservaciones() { return observaciones; }

    // Setters
    public void setTipo(String tipo) { this.TIPO_MOVIMIENTO = tipo; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
