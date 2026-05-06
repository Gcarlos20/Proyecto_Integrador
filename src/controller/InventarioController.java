package controller;

import model.producto;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLADOR: Inventario Controller
 * Descripción: Gestiona estadísticas y estado general del inventario
 * Funcionalidades: Obtener totales, estadísticas, alertas de stock
 * Nota: Conectar a base de datos en los métodos comentados
 */
public class InventarioController {

    private productoController productoController;
    private MovimientoController movimientoController;

    public InventarioController() {
        this.productoController = new productoController();
        this.movimientoController = new MovimientoController();
    }

    /**
     * BLOQUE: Obtener Estadísticas Generales
     * Para: Calcular totales del inventario
     * @return Array con [total_productos, cantidad_total, valor_total]
     */
    public Object[] obtenerEstadisticas() {
        try {
            // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
            // String query = "SELECT COUNT(*), SUM(stock), SUM(precio * stock) FROM productos";
            // return db.obtenerEstadisticas(query);

            int totalProductos = productoController.listar().size();
            int cantidadTotal = productoController.obtenerCantidadTotal();
            double valorTotal = productoController.obtenerValorTotal();

            return new Object[]{totalProductos, cantidadTotal, valorTotal};
        } catch (Exception e) {
            System.out.println("Error al obtener estadísticas: " + e.getMessage());
            return new Object[]{0, 0, 0.0};
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
            List<Integer> contador = new ArrayList<>();
            
            for (producto p : productoController.listar()) {
                int salidas = 0;
                for (var m : movimientoController.obtenerPorProducto(p.getId())) {
                    if (m.getTipo().equals("SALIDA")) {
                        salidas += m.getCantidad();
                    }
                }
                
                if (!contador.isEmpty()) {
                    int maxId = 0;
                    int maxSalidas = 0;
                    for (int i = 0; i < contador.size(); i += 2) {
                        if (contador.get(i + 1) > maxSalidas) {
                            maxSalidas = contador.get(i + 1);
                            maxId = contador.get(i);
                        }
                    }
                    return maxId;
                }
            }
            return -1;
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
            // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
            // String query = "SELECT SUM(cantidad) FROM movimientos WHERE tipo = 'SALIDA' AND MONTH(fecha) = MONTH(NOW())";
            // int ventasDelMes = db.obtenerValor(query);
            // double costoPromedio = obtenerValorTotal() / listar().size();
            // return ventasDelMes / costoPromedio;

            return 0.0;
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