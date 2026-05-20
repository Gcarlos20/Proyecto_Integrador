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
    public boolean registrarCompra(int productoId, int proveedorId, int cantidad, double precio, int usuarioId, String usuario, String observaciones) {
        String insertarDetalle = "INSERT INTO DETALLE_COMPRA "
                + "(ID_COMPRA, ID_PRODUCTO, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL) "
                + "VALUES (?, ?, ?, ?, ?)";

        String actualizarStock = "UPDATE PRODUCTOS "
                + "SET CANTIDAD = CANTIDAD + ? "
                + "WHERE ID_PRODUCTO = ?";

        double subtotal = cantidad * precio;
        double total = subtotal;

        try (Connection conn = conexionBD.getConexion()) {

            conn.setAutoCommit(false); // Inicia la transacción de 
            String insertarCompra = crearSqlInsertarCompra(conn); // esto es para insertar la compra principal y obtener su ID para el detalle, se adapta segun las columnas de la tabla COMPRAS

            try (
                PreparedStatement psCompra = conn.prepareStatement(insertarCompra, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement psDetalle = conn.prepareStatement(insertarDetalle);
                PreparedStatement psStock = conn.prepareStatement(actualizarStock)
            ) {

                // Insertar compra principal con el usuario que inicio sesion.
                llenarCompra(psCompra, conn, total, proveedorId, usuarioId, usuario, observaciones);
                psCompra.executeUpdate();

                int compraId = obtenerIdGenerado(psCompra);

                // Insertar detalle de compra con precio y subtotal psdetakkea el precio unitario y el subtotal de la compra.
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
     * BLOQUE: Registrar venta
     * Para: Crear una venta, guardar su detalle y restar la cantidad del stock.
     */
    public boolean registrarVenta(int productoId, int cantidad, double precio, String usuario, String observacion) {
        String insertarDetalle = "INSERT INTO DETALLE_VENTA "
                + "(ID_VENTA, ID_PRODUCTO, CANTIDAD, PRECIO) "
                + "VALUES (?, ?, ?, ?)";

        String actualizarStock = "UPDATE PRODUCTOS "
                + "SET CANTIDAD = CANTIDAD - ? "
                + "WHERE ID_PRODUCTO = ? AND CANTIDAD >= ?";

        double total = cantidad * precio;

        try (Connection conn = conexionBD.getConexion()) {
            conn.setAutoCommit(false);
            String insertarVenta = crearSqlInsertarVenta(conn);

            try (
                PreparedStatement psVenta = conn.prepareStatement(insertarVenta, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement psDetalle = conn.prepareStatement(insertarDetalle);
                PreparedStatement psStock = conn.prepareStatement(actualizarStock)
            ) {

                // Insertar venta principal con fecha, total, usuario y observacion.
                psVenta.setDouble(1, total);
                psVenta.setString(2, usuario);
                psVenta.setString(3, observacion);
                psVenta.executeUpdate();

                int ventaId = obtenerIdGenerado(psVenta);

                // Insertar detalle_venta con las columnas reales de la tabla.
                psDetalle.setInt(1, ventaId);
                psDetalle.setInt(2, productoId);
                psDetalle.setInt(3, cantidad);
                psDetalle.setDouble(4, precio);
                psDetalle.executeUpdate();

                // Restar stock solo si hay unidades suficientes.
                psStock.setInt(1, cantidad);
                psStock.setInt(2, productoId);
                psStock.setInt(3, cantidad);

                if (psStock.executeUpdate() == 0) {
                    throw new SQLException("Stock insuficiente o producto inexistente");
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
            System.out.println("Error al registrar venta: " + e.getMessage());
            return false;
        }
    }

    /**
     * BLOQUE: Listar detalle de compra
     * Para: Mostrar cada producto comprado junto con su compra principal.
     */
    public List<Object[]> listarDetalleCompra() {
        boolean tieneProveedor = compraTieneProveedor();
        String proveedorSelect = tieneProveedor ? "COALESCE(pr.NOMBRE, 'Sin proveedor')" : "'Sin proveedor'";
        String joinProveedor = tieneProveedor ? "LEFT JOIN PROVEEDORES pr ON pr.ID_PROVEEDOR = c.ID_PROVEEDOR " : "";

        String sql = "SELECT dc.ID_DETALLE, c.ID_COMPRA, c.FECHA, "
                + proveedorSelect + " AS PROVEEDOR, "
                + "p.NOMBRE AS PRODUCTO, dc.CANTIDAD, dc.PRECIO_UNITARIO, "
                + "dc.SUBTOTAL, c.USUARIO, c.OBSERVACIONES "
                + "FROM DETALLE_COMPRA dc "
                + "INNER JOIN COMPRAS c ON c.ID_COMPRA = dc.ID_COMPRA "
                + "INNER JOIN PRODUCTOS p ON p.ID_PRODUCTO = dc.ID_PRODUCTO "
                + joinProveedor
                + "ORDER BY c.FECHA DESC, dc.ID_DETALLE DESC";

        return consultarFilas(sql, 10);
    }

    /**
     * BLOQUE: Listar compras
     * Para: Saber a que proveedor se le realizo cada compra.
     */
    public List<Object[]> listarCompras() {
        boolean tieneProveedor = compraTieneProveedor();
        String proveedorSelect = tieneProveedor ? "COALESCE(p.NOMBRE, 'Sin proveedor')" : "'Sin proveedor'";
        String joinProveedor = tieneProveedor ? "LEFT JOIN PROVEEDORES p ON p.ID_PROVEEDOR = c.ID_PROVEEDOR " : "";

        String sql = "SELECT c.ID_COMPRA, c.FECHA, " + proveedorSelect + " AS PROVEEDOR, "
                + "c.TOTAL, c.USUARIO, c.OBSERVACIONES "
                + "FROM COMPRAS c "
                + joinProveedor
                + "ORDER BY c.FECHA DESC, c.ID_COMPRA DESC";

        return consultarFilas(sql, 6);
    }

    /**
     * BLOQUE: Listar ventas
     * Para: Mostrar la tabla de ventas completa para relacionarla después.
     */
    public List<Object[]> listarVentas() {
        String columnaObservacion = columnaObservacionVentas();

        String sql = "SELECT ID_VENTA, FECHA, TOTAL, USUARIO, " + columnaObservacion + " AS OBSERVACION "
                + "FROM VENTAS "
                + "ORDER BY FECHA DESC, ID_VENTA DESC";

        return consultarFilas(sql, 5);
    }

    /**
     * BLOQUE: SQL de venta
     * Para: Insertar en VENTAS usando OBSERVACION u OBSERVACIONES segun la tabla.
     */
    private String crearSqlInsertarVenta(Connection conn) throws SQLException {
        String columnaObservacion = existeColumna(conn, "VENTAS", "OBSERVACIONES") ? "OBSERVACIONES" : "OBSERVACION";
        return "INSERT INTO VENTAS (FECHA, TOTAL, USUARIO, " + columnaObservacion + ") "
                + "VALUES (NOW(), ?, ?, ?)";
    }

    /**
     * BLOQUE: Listar detalle de venta
     * Para: Mostrar los productos vendidos y su venta asociada.
     */
    public List<Object[]> listarDetalleVenta() {

        String sql = "SELECT ID_DETALLE, ID_VENTA, ID_PRODUCTO, CANTIDAD, PRECIO "
                + "FROM DETALLE_VENTA "
                + "ORDER BY ID_DETALLE DESC";

        return consultarFilas(sql, 5);
    }

    /**
     * BLOQUE: SQL de compra
     * Para: Usar ID_USUARIO si la tabla lo tiene, sin romper bases que solo tienen USUARIO.
     */
    private String crearSqlInsertarCompra(Connection conn) throws SQLException {
        boolean tieneProveedor = existeColumna(conn, "COMPRAS", "ID_PROVEEDOR");
        boolean tieneUsuarioId = existeColumna(conn, "COMPRAS", "ID_USUARIO");

        if (tieneProveedor && tieneUsuarioId) {
            return "INSERT INTO COMPRAS (ID_PROVEEDOR, ID_USUARIO, FECHA, TOTAL, USUARIO, OBSERVACIONES) "
                    + "VALUES (?, ?, NOW(), ?, ?, ?)";
        }
        if (tieneUsuarioId) {
            return "INSERT INTO COMPRAS (ID_USUARIO, FECHA, TOTAL, USUARIO, OBSERVACIONES) "
                    + "VALUES (?, NOW(), ?, ?, ?)";
        }
        return "INSERT INTO COMPRAS (FECHA, TOTAL, USUARIO, OBSERVACIONES) "
                + "VALUES (NOW(), ?, ?, ?)";
    }

    /**
     * BLOQUE: Parametros de compra
     * Para: Llenar el PreparedStatement segun las columnas disponibles.
     */
    private void llenarCompra(PreparedStatement ps, Connection conn, double total, int proveedorId, int usuarioId,
                              String usuario, String observaciones) throws SQLException {
        boolean tieneProveedor = existeColumna(conn, "COMPRAS", "ID_PROVEEDOR");
        boolean tieneUsuarioId = existeColumna(conn, "COMPRAS", "ID_USUARIO");
        int i = 1;

        if (tieneProveedor) {
            ps.setInt(i++, proveedorId); // Proveedor elegido en el formulario de compra.
        }
        if (tieneUsuarioId) {
            ps.setInt(i++, usuarioId <= 0 ? 1 : usuarioId);
        }
        ps.setDouble(i++, total);
        ps.setString(i++, usuario);
        ps.setString(i, observaciones);
    }

    /**
     * BLOQUE: Revisar columna
     * Para: Saber si la base actual tiene una columna opcional.
     */
    private boolean existeColumna(Connection conn, String tabla, String columna) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getColumns(conn.getCatalog(), null, tabla, columna)) {
            if (rs.next()) {
                return true;
            }
        }
        try (ResultSet rs = conn.getMetaData().getColumns(conn.getCatalog(), null, tabla.toLowerCase(), columna.toLowerCase())) {
            return rs.next();
        }
    }

    /**
     * BLOQUE: Validar proveedor en compras
     * Para: Consultar proveedor solo si COMPRAS tiene ID_PROVEEDOR.
     */
    private boolean compraTieneProveedor() {
        try (Connection conn = conexionBD.getConexion()) {
            return existeColumna(conn, "COMPRAS", "ID_PROVEEDOR");
        } catch (SQLException e) {
            System.out.println("No se pudo revisar proveedor en compras: " + e.getMessage());
            return false;
        }
    }

    /**
     * BLOQUE: Columna observacion ventas
     * Para: Leer VENTAS aunque la base use OBSERVACION u OBSERVACIONES.
     */
    private String columnaObservacionVentas() {
        try (Connection conn = conexionBD.getConexion()) {
            return existeColumna(conn, "VENTAS", "OBSERVACIONES") ? "OBSERVACIONES" : "OBSERVACION";
        } catch (SQLException e) {
            System.out.println("No se pudo revisar observacion en ventas: " + e.getMessage());
            return "OBSERVACION";
        }
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
