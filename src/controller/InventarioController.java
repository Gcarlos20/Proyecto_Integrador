package controller;

import model.producto;
import java.util.List;

/**
 * CONTROLADOR: Inventario Controller
 * Descripción: Gestiona estadísticas y estado general del inventario
 * Funcionalidades: Obtener totales, estadísticas, alertas de stock
 */
public class InventarioController {

    private final productoController productoController; // Instancia del controlador de productos para acceder a sus métodos
    private final MovimientoController movimientoController; // Instancia del controlador de movimientos para acceder a sus métodos relacionados con movimientos de inventario

    public InventarioController() { // Contructor 
        this.productoController = new productoController(); // Inicializa el controlador de productos para poder usar sus métodos en este controlador
        this.movimientoController = new MovimientoController();
    }

    /**
     * BLOQUE: Obtener Estadísticas Generales
     * Para: Calcular totales del inventario
     * @return Array con [total_productos, cantidad_total, valor_total]
     */
    public Object[] obtenerEstadisticas() {
        try {
            int totalProductos = productoController.listar().size(); // obtiene la lista de los productor y los cuenta con el .size()
            int cantidadTotal = productoController.obtenerCantidadTotal(); // obtiene la cantidad total de unidades en stock sumando el stock de cada producto
            double valorTotal = productoController.obtenerValorTotal(); // obtiene el valor total del inventario multiplicando el stock de cada producto por su precio y sumando esos valores para todos los productos

            return new Object[]{totalProductos, cantidadTotal, valorTotal};
        } catch (Exception e) {
            System.out.println("Error al obtener estadísticas: " + e.getMessage());
            return new Object[]{0, 0, 0.0}; // Devuelve valores por defecto en caso de error
        }
    }

    /**
     * BLOQUE: Obtener Alertas de Stock
     * Para: Identificar productos con stock bajo
     * @param minimo Cantidad mínima de alerta
     * @return Cantidad de productos en alerta
     */
    public int obtenerAlertasStock(int minimo) {
        try {
            List<producto> stockBajo = productoController.obtenerProductosConStockBajo(minimo);
            return stockBajo.size();
        } catch (Exception e) {
            System.out.println("Error al obtener alertas: " + e.getMessage());
            return 0;
        }
    }

    /**
     * BLOQUE: Obtener Productos con Stock Bajo
     * Para: Listar productos que necesitan reabastecimiento
     * @param minimo Cantidad mínima
     * @return Lista de productos con stock bajo
     */
    public List<producto> obtenerProductosStockBajo(int minimo) {
        return productoController.obtenerProductosConStockBajo(minimo);
    }

    /**
     * BLOQUE: Obtener Producto Más Vendido
     * Para: Identificar el producto con más salidas
     * @return ID del producto más vendido
     */
    public int obtenerProductoMasVendido() {
        try {
            int maxId = -1;
            int maxSalidas = 0;

            for (producto p : productoController.listar()) {
                int salidas = 0;
                for (var m : movimientoController.obtenerPorProducto(p.getId())) {
                    if (m.getTipo().equals("SALIDA")) {
                        salidas += m.getCantidad();
                    }
                }

                if (salidas > maxSalidas) {
                    maxSalidas = salidas;
                    maxId = p.getId();
                }
            }
            return maxId;
        } catch (Exception e) {
            System.out.println("Error al obtener producto más vendido: " + e.getMessage());
            return -1;
        }
    }

    /**
     * BLOQUE: Obtener Rotación de Inventario
     * Para: Calcular cuántas veces se renueva el inventario
     * @return Tasa de rotación
     */
    public double obtenerRotacionInventario() {
        try {
            int salidas = 0;
            for (var movimiento : movimientoController.obtenerPorTipo("SALIDA")) {
                salidas += movimiento.getCantidad();
            }

            double valorInventario = obtenerValorInventario();
            return valorInventario == 0 ? 0.0 : salidas / valorInventario;
        } catch (Exception e) {
            System.out.println("Error al calcular rotación: " + e.getMessage());
            return 0.0;
        }
    }

    /**
     * BLOQUE: Obtener Valor del Inventario
     * Para: Calcular el costo total del inventario
     * @return Valor total en dinero
     */
    public double obtenerValorInventario() {
        return productoController.obtenerValorTotal();
    }

    /**
     * BLOQUE: Obtener Cantidad Total de Unidades
     * Para: Sumar todas las unidades en stock
     * @return Total de unidades
     */
    public int obtenerCantidadTotal() {
        return productoController.obtenerCantidadTotal();
    }

    /**
     * BLOQUE: Obtener Total de Productos
     * Para: Contar cantidad de productos diferentes
     * @return Total de productos
     */
    public int obtenerTotalProductos() {
        return productoController.listar().size();
    }
}
