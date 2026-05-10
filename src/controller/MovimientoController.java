package controller;

import Dao.MovimientoDao;
import model.movimiento;
import java.util.List;

/**
 * CONTROLADOR: Movimiento Controller
 * Descripción: Gestiona el registro de movimientos de inventario
 * Funcionalidades: Registrar entradas, salidas, ajustes; obtener historial
 */
public class MovimientoController {

    private final MovimientoDao movimientoDao;

    public MovimientoController() {
        this.movimientoDao = new MovimientoDao();
    }

    /**
     * BLOQUE: Registrar Movimiento
     * Para: Insertar nuevo movimiento en la base de datos
     * @param productoId ID del producto
     * @param tipo Tipo de movimiento (ENTRADA, SALIDA, AJUSTE)
     * @param cantidad Cantidad movida
     * @param usuario Usuario que realiza el movimiento
     * @param observaciones Notas adicionales
     */
    public boolean registrar(int productoId, String tipo, int cantidad, String usuario, String observaciones) {
        try {
            return movimientoDao.registrar(productoId, tipo, cantidad, usuario, observaciones);
        } catch (Exception e) {
            System.out.println("Error al registrar movimiento: " + e.getMessage());
            return false;
        }
    }

    /**
     * BLOQUE: Obtener Todos los Movimientos
     * Para: Recuperar lista de movimientos
     * @return Lista de movimientos
     */
    public List<movimiento> obtenerTodos() {
        try {
            return movimientoDao.obtenerTodos();
        } catch (Exception e) {
            System.out.println("Error al obtener movimientos: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * BLOQUE: Obtener Movimientos Recientes
     * Para: Obtener los últimos N movimientos
     * @param limite Número de movimientos a recuperar
     * @return Lista de movimientos recientes
     */
    public List<movimiento> obtenerRecientes(int limite) {
        return movimientoDao.obtenerRecientes(limite);
    }

    /**
     * BLOQUE: Obtener Movimientos por Producto
     * Para: Ver historial de un producto específico
     * @param productoId ID del producto
     * @return Lista de movimientos del producto
     */
    public List<movimiento> obtenerPorProducto(int productoId) {
        return movimientoDao.obtenerPorProducto(productoId);
    }

    /**
     * BLOQUE: Obtener Movimientos por Tipo
     * Para: Filtrar movimientos por tipo
     * @param tipo Tipo de movimiento (ENTRADA, SALIDA, AJUSTE)
     * @return Lista de movimientos del tipo especificado
     */
    public List<movimiento> obtenerPorTipo(String tipo) {
        return movimientoDao.obtenerPorTipo(tipo);
    }

    /**
     * BLOQUE: Calcular Saldo de Producto
     * Para: Sumar todas las entradas y salidas de un producto
     * @param productoId ID del producto
     * @return Saldo neto del producto
     */
    public int calcularSaldo(int productoId) {
        return movimientoDao.calcularSaldo(productoId);
    }
}
