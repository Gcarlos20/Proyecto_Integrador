package controller;

import Dao.CompraDao;
import java.util.List;

/**
 * CONTROLADOR: Compra
 * Para: Validar datos de compra antes de enviarlos al DAO.
 */
public class CompraController {

    private final CompraDao compraDao;

    public CompraController() {
        this.compraDao = new CompraDao();
    }

    /**
     * BLOQUE: Registrar compra
     * Para: Comprar un producto y aumentar su stock en inventario.
     */
    public boolean registrarCompra(int productoId, int cantidad, double precioUnitario, String usuario, String observaciones) {
        if (productoId <= 0 || cantidad <= 0 || precioUnitario < 0) {
            return false;
        }
        return compraDao.registrarCompra(productoId, cantidad, precioUnitario, usuario, observaciones);
    }

    /**
     * BLOQUE: Detalle compra
     * Para: Entregar a la vista las filas de detalle_compra.
     */
    public List<Object[]> listarDetalleCompra() {
        return compraDao.listarDetalleCompra();
    }

    /**
     * BLOQUE: Ventas
     * Para: Entregar a la vista las filas de venta.
     */
    public List<Object[]> listarVentas() {
        return compraDao.listarVentas();
    }

    /**
     * BLOQUE: Detalle venta
     * Para: Entregar a la vista las filas de detalle_venta.
     */
    public List<Object[]> listarDetalleVenta() {
        return compraDao.listarDetalleVenta();
    }
}
