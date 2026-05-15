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
 * 
 */
public class CompraDao {

    /**
     * BLOQUE: Registrar compra
     * Para: Crear una compra, guardar su detalle y sumar la cantidad al stock del producto.
     */
    public boolean registrarCompra(int productoId, int cantidad, double precio, String usuario, String observaciones) {
        String insertarCompra = "INSERT INTO COMPRAS "
        + "(ID_PROVEEDOR, ID_USUARIO, FECHA, TOTAL, USUARIO, OBSERVACIONES) "
        + "VALUES (?, ?, NOW(), ?, ?, ?)";

        String insertarDetalle = "INSERT INTO DETALLE_COMPRA "
                + "(ID_COMPRA, ID_PRODUCTO, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL) "
                + "VALUES (?, ?, ?, ?, ?)";

        String actualizarStock = "UPDATE PRODUCTOS "
                + "SET CANTIDAD = CANTIDAD + ? "
                + "WHERE ID_PRODUCTO = ?";

        double subtotal = cantidad * precio;
        double total = subtotal;

        try (Connection conn = conexionBD.getConexion()) {

            conn.setAutoCommit(false);

            try (
                PreparedStatement psCompra = conn.prepareStatement(insertarCompra, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement psDetalle = conn.prepareStatement(insertarDetalle);
                PreparedStatement psStock = conn.prepareStatement(actualizarStock)
            ) {

                // Insertar compra principal
                psCompra.setInt(1, 1); // ID_PROVEEDOR
                psCompra.setInt(2, 1); // ID_USUARIO
                psCompra.setDouble(3, total);
                psCompra.setString(4, usuario);
                psCompra.setString(5, observaciones);
                psCompra.executeUpdate();

                int compraId = obtenerIdGenerado(psCompra);

                // Insertar detalle de compra
               
             // Insertar detalle de compra
                    psDetalle.setInt(1, compraId);
                    psDetalle.setInt(2, productoId);
                    psDetalle.setInt(3, cantidad);
                    psDetalle.setDouble(4, precio);
                    psDetalle.setDouble(5, subtotal);
                    psDetalle.executeUpdate();

                // Actualizar stock
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

        String sql = "SELECT dc.ID_DETALLE, c.ID_COMPRA, c.FECHA, "
                + "p.NOMBRE AS PRODUCTO, dc.CANTIDAD, dc.PRECIO "
                + "FROM DETALLE_COMPRA dc "
                + "INNER JOIN COMPRAS c ON c.ID_COMPRA = dc.ID_COMPRA "
                + "INNER JOIN PRODUCTOS p ON p.ID_PRODUCTO = dc.ID_PRODUCTO "
                + "ORDER BY c.FECHA DESC, dc.ID_DETALLE DESC";

        return consultarFilas(sql, 6);
    }

    /**
     * BLOQUE: Listar ventas
     * Para: Mostrar la tabla de ventas completa para relacionarla después.
     */
    public List<Object[]> listarVentas() {

        String sql = "SELECT ID_VENTA, FECHA, TOTAL, USUARIO "
                + "FROM VENTAS "
                + "ORDER BY FECHA DESC, ID_VENTA DESC";

        return consultarFilas(sql, 4);
    }

    /**
     * BLOQUE: Listar detalle de venta
     * Para: Mostrar los productos vendidos y su venta asociada.
     */
    public List<Object[]> listarDetalleVenta() {

        String sql = "SELECT dv.ID_DETALLE, v.ID_VENTA, v.FECHA, "
                + "p.NOMBRE AS PRODUCTO, dv.CANTIDAD, dv.PRECIO "
                + "FROM DETALLE_VENTA dv "
                + "INNER JOIN VENTAS v ON v.ID_VENTA = dv.ID_VENTA "
                + "INNER JOIN PRODUCTOS p ON p.ID_PRODUCTO = dv.ID_PRODUCTO "
                + "ORDER BY v.FECHA DESC, dv.ID_DETALLE DESC";

        return consultarFilas(sql, 6);
    }

    /**
     * BLOQUE: Obtener ID generado
     * Para: Recuperar el ID_COMPRA creado y usarlo en DETALLE_COMPRA.
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
     * BLOQUE: Consulta genérica
     * Para: Convertir cualquier SELECT en filas Object[] listas para JTable.
     */
    private List<Object[]> consultarFilas(String sql, int columnas) {

        List<Object[]> filas = new ArrayList<>();

        try (
            Connection conn = conexionBD.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Object[] fila = new Object[columnas];

                for (int i = 0; i < columnas; i++) {

                    fila[i] = rs.getObject(i + 1);

                    if (fila[i] == null
                            && rs.getMetaData().getColumnType(i + 1) == Types.VARCHAR) {

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
