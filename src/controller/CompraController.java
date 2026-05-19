package controller;

import Dao.CompraDao;
import java.util.List;

/**
 * CONTROLADOR: Compra
 * Para: Validar datos de compra antes de enviarlos al DAO.
 */
public class CompraController {

    private final CompraDao compraDao; // Instancia del DAO para operaciones de compra

    public CompraController() {  // Constructor que inicializa el DAO de compra
        this.compraDao = new CompraDao();
    }

    /**
     * BLOQUE: Registrar compra
     * Para: Comprar un producto y aumentar su stock en inventario.
     */
    public boolean registrarCompra(int productoId, int proveedorId, int cantidad, double precioUnitario, int usuarioId, String usuario, String observaciones) {
        if (productoId <= 0 || proveedorId <= 0 || cantidad <= 0 || precioUnitario < 0 || usuario == null || usuario.isBlank()) { // Validar que los datos sean correctos antes de registrar la compra
            return false; // Si los datos no son válidos, no se registra la compra
        }
        return compraDao.registrarCompra(productoId, proveedorId, cantidad, precioUnitario, usuarioId, usuario, observaciones); /*al llamar el método del DAO registra la compra  */ 
    }

    /**
     * BLOQUE: Registrar venta
     * Para: Vender un producto y disminuir su stock en inventario.
     */
    public boolean registrarVenta(int productoId, int cantidad, double precio, String usuario, String observacion) {
        if (productoId <= 0 || cantidad <= 0 || precio < 0 || usuario == null || usuario.isBlank()) {
            return false;
        }
        return compraDao.registrarVenta(productoId, cantidad, precio, usuario, observacion);
    }

    /**
     * BLOQUE: Compras
     * Para: Entregar a la vista las compras con proveedor, usuario y total.
     */
    public List<Object[]> listarCompras() {
        return compraDao.listarCompras();
    }

    /**
     * BLOQUE: Detalle compra
     * Para: Entregar a la vista las filas de detalle_compra. Es basicamente ir a la tabla detalle_compra y mostrar su contenido
     */
    public List<Object[]> listarDetalleCompra() { 
        return compraDao.listarDetalleCompra();
    }

    /**
     * BLOQUE: Ventas
     * Para: Entregar a la vista las filas de venta. es basicamente ir a la tabla ventas y mostrar su contenido
     */
    public List<Object[]> listarVentas() { //
        return compraDao.listarVentas();
    }

    /**
     * BLOQUE: Detalle venta
     * Para: Entregar a la vista las filas de detalle_venta. es basicamente ir a la tabla detalle_venta y mostrar su contenido
     */
    public List<Object[]> listarDetalleVenta() {
        return compraDao.listarDetalleVenta();
    }
}
