package Dao;

import conexion.conexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: Compra
 * Para: Guardar compras, actualizar inventario y consultar compras, ventas y detalles.
 */
public class CompraDao {

    /**
     * BLOQUE: Registrar compra
     * Para: Crear una compra, guardar su detalle y sumar la cantidad al stock del producto.
     */
    public boolean registrarCompra(int productoId, int cantidad, double precioUnitario, String usuario, String observaciones) {
        String insertarCompra = "INSERT INTO compras (fecha, total, usuario, observaciones) VALUES (NOW(), ?, ?, ?)";
        String insertarDetalle = "INSERT INTO detalle_compra (id_compra, id_producto, cantidad, precio_unitario, subtotal) "
                + "VALUES (?, ?, ?, ?, ?)";
        String actualizarStock = "UPDATE productos SET cantidad = cantidad + ? WHERE id_producto = ?";
        double subtotal = cantidad * precioUnitario;

        try (Connection conn = conexionBD.getConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psCompra = conn.prepareStatement(insertarCompra, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psDetalle = conn.prepareStatement(insertarDetalle);
                 PreparedStatement psStock = conn.prepareStatement(actualizarStock)) {

                psCompra.setDouble(1, subtotal);
                psCompra.setString(2, usuario);
                psCompra.setString(3, observaciones);
                psCompra.executeUpdate();

                int compraId = obtenerIdGenerado(psCompra);

                psDetalle.setInt(1, compraId);
                psDetalle.setInt(2, productoId);
                psDetalle.setInt(3, cantidad);
                psDetalle.setDouble(4, precioUnitario);
                psDetalle.setDouble(5, subtotal);
                psDetalle.executeUpdate();

                psStock.setInt(1, cantidad);
                psStock.setInt(2, productoId);
                if (psStock.executeUpdate() == 0) {
                    throw new SQLException("No existe el producto con ID " + productoId);
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar compra: " + e.getMessage());
            return false;
        }
    }

    /**
     * BLOQUE: Listar detalle de compra
     * Para: Mostrar cada producto comprado junto con su compra principal.
     */
    public List<Object[]> listarDetalleCompra() {
        String sql = "SELECT dc.id_detalle_compra, c.id_compra, c.fecha, p.nombre AS producto, "
                + "dc.cantidad, dc.precio_unitario, dc.subtotal, c.usuario, c.observaciones "
                + "FROM detalle_compra dc "
                + "INNER JOIN compras c ON c.id_compra = dc.id_compra "
                + "INNER JOIN productos p ON p.id_producto = dc.id_producto "
                + "ORDER BY c.fecha DESC, dc.id_detalle_compra DESC";
        return consultarFilas(sql, 9);
    }

    /**
     * BLOQUE: Listar ventas
     * Para: Mostrar la tabla venta completa para relacionarla despues.
     */
    public List<Object[]> listarVentas() {
        String sql = "SELECT id_venta, fecha, total, usuario, observaciones "
                + "FROM ventas ORDER BY fecha DESC, id_venta DESC";
        return consultarFilas(sql, 5);
    }

    /**
     * BLOQUE: Listar detalle de venta
     * Para: Mostrar los productos vendidos y su venta asociada.
     */
    public List<Object[]> listarDetalleVenta() {
        String sql = "SELECT dv.id_detalle_venta, v.id_venta, v.fecha, p.nombre AS producto, "
                + "dv.cantidad, dv.precio_unitario, dv.subtotal, v.usuario, v.observaciones "
                + "FROM detalle_venta dv "
                + "INNER JOIN ventas v ON v.id_venta = dv.id_venta "
                + "INNER JOIN productos p ON p.id_producto = dv.id_producto "
                + "ORDER BY v.fecha DESC, dv.id_detalle_venta DESC";
        return consultarFilas(sql, 9);
    }

    /**
     * BLOQUE: Obtener ID generado
     * Para: Recuperar el id_compra creado y usarlo en detalle_compra.
     */
    private int obtenerIdGenerado(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo obtener el ID generado de la compra");
    }

    /**
     * BLOQUE: Consulta generica
     * Para: Convertir cualquier SELECT en filas Object[] listas para JTable.
     */
    private List<Object[]> consultarFilas(String sql, int columnas) {
        List<Object[]> filas = new ArrayList<>();
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[columnas];
                for (int i = 0; i < columnas; i++) {
                    fila[i] = rs.getObject(i + 1);
                    if (fila[i] == null && rs.getMetaData().getColumnType(i + 1) == Types.VARCHAR) {
                        fila[i] = "";
                    }
                }
                filas.add(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar compras/ventas: " + e.getMessage());
        }
        return filas;
    }
}
