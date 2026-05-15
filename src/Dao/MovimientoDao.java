package Dao;

import conexion.conexionBD;
import model.movimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO: Movimiento
 * Para: Gestionar el historial de movimientos del inventario (entradas, salidas, ajustes).
 * Operaciones CRUD:
 * - Create: registrar() - Inserta un nuevo movimiento y actualiza stock.
 * - Read: obtenerTodos(), obtenerRecientes(), obtenerPorProducto(), obtenerPorTipo(), calcularSaldo() - Consultas de movimientos.
 */
public class MovimientoDao {

    /**
     * CRUD - CREATE: Registrar movimiento
     * Para: Guardar el movimiento y ajustar el stock del producto en una transaccion.
     */
    public boolean registrar(int productoId, String tipo, int cantidad, String usuario, String observaciones) {
        String insertar = "INSERT INTO MOVIMIENTOS "
        + "(ID_PRODUCTO, ID_USUARIO, CANTIDAD, FECHA, DESCRIPCION, ESTADO, ID_VENTA) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String actualizarStock = "UPDATE productos SET cantidad = cantidad + ? WHERE id_producto = ?";

        try (Connection conn = conexionBD.getConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psMovimiento = conn.prepareStatement(insertar);
                 PreparedStatement psStock = conn.prepareStatement(actualizarStock)) {

                psMovimiento.setInt(1, productoId);
                psMovimiento.setString(2, tipo);
                psMovimiento.setInt(3, cantidad);
                psMovimiento.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                psMovimiento.setString(5, usuario);
                psMovimiento.setString(6, observaciones);
                psMovimiento.setString(7, "activo"); // Valor predeterminado para ESTADO
                psMovimiento.setNull(8, java.sql.Types.INTEGER); // ID_VENTA es nulo por defecto
                psMovimiento.executeUpdate();

                psStock.setInt(1, calcularDelta(tipo, cantidad));
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
            System.out.println("Error al registrar movimiento: " + e.getMessage());
            return false;
        }
    }

    /**
     * CRUD - READ: Listar movimientos
     * Para: Obtener el historial completo del inventario.
     */
    public List<movimiento> obtenerTodos() {
        List<movimiento> lista = new ArrayList<>();
     String sql = "SELECT ID_MOVIMIENTO, ID_PRODUCTO,TIPO_MOVIMIENTO, ID_USUARIO, "
           + "CANTIDAD, FECHA, DESCRIPCION, ESTADO, ID_VENTA "
           + "FROM MOVIMIENTOS "
           + "ORDER BY FECHA DESC, ID_MOVIMIENTO DESC LIMIT ?";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearMovimiento(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar movimientos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * CRUD - READ: Movimientos recientes
     * Para: Consultar los ultimos registros del historial.
     */
    public List<movimiento> obtenerRecientes(int limite) {
        List<movimiento> lista = new ArrayList<>();
        String sql ="SELECT ID_MOVIMIENTO, ID_PRODUCTO, TIPO_MOVIMIENTO, CANTIDAD, FECHA, ID_USUARIO, DESCRIPCION, ESTADO, ID_VENTA "
           + "FROM MOVIMIENTOS "
           + "ORDER BY FECHA DESC, ID_MOVIMIENTO DESC LIMIT ?";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMovimiento(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar movimientos recientes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * CRUD - READ: Filtrar por producto
     * Para: Consultar el historial de un producto especifico.
     */
    public List<movimiento> obtenerPorProducto(int productoId) {
        return filtrar("id_producto = ?", productoId);
    }

    /**
     * CRUD - READ: Filtrar por tipo
     * Para: Consultar entradas, salidas o ajustes.
     */
    public List<movimiento> obtenerPorTipo(String TIPO_MOVIMIENTO) {
        List<movimiento> lista = new ArrayList<>();
        String sql = "SELECT ID_MOVIMIENTO, ID_PRODUCTO, TIPO_MOVIMIENTO, CANTIDAD, FECHA, ID_USUARIO, DESCRIPCION "
                + "FROM MOVIMIENTOS WHERE TIPO_MOVIMIENTO = ? ORDER BY FECHA DESC, ID_MOVIMIENTO DESC";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, TIPO_MOVIMIENTO);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMovimiento(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al filtrar movimientos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * CRUD - READ: Saldo por producto
     * Para: Calcular el efecto neto de todos los movimientos de un producto.
     */
    public int calcularSaldo(int productoId) {
        String sql = "SELECT COALESCE(SUM(CASE "
                + "WHEN TIPO_MOVIMIENTO = 'ENTRADA' THEN cantidad "
                + "WHEN TIPO_MOVIMIENTO = 'SALIDA' THEN -cantidad "
                + "ELSE cantidad END), 0) AS saldo "
                + "FROM MOVIMIENTOS WHERE id_producto = ?";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("saldo");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al calcular saldo: " + e.getMessage());
        }
        return 0;
    }

    // BLOQUE: Consulta comun
    // Para: Evitar repetir mapeo en filtros simples por entero.
    private List<movimiento> filtrar(String condicion, int valor) {
        List<movimiento> lista = new ArrayList<>();
        String sql = "SELECT ID_MOVIMIENTO, ID_PRODUCTO, TIPO_MOVIMIENTO, CANTIDAD, FECHA, ID_USUARIO, DESCRIPCION "
                + "FROM MOVIMIENTOS WHERE " + condicion + " ORDER BY FECHA DESC, ID_MOVIMIENTO DESC";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, valor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMovimiento(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al filtrar movimientos: " + e.getMessage());
        }
        return lista;
    }

    // BLOQUE: Calcular cambio de stock
    // Para: Convertir el tipo de movimiento en suma o resta de inventario.
    private int calcularDelta(String TIPO_MOVIMIENTO, int cantidad) {
        if ("SALIDA".equalsIgnoreCase(TIPO_MOVIMIENTO)) {
            return -cantidad;
        }
        return cantidad;
    }

    // BLOQUE: Mapear resultado
    // Para: Convertir una fila SQL en un objeto movimiento.
    private movimiento mapearMovimiento(ResultSet rs) throws SQLException {
        Timestamp fecha = rs.getTimestamp("fecha");
        return new movimiento(
            rs.getInt("ID_MOVIMIENTO"),
            rs.getInt("ID_PRODUCTO"),
            rs.getString("TIPO_MOVIMIENTO"),
            rs.getInt("CANTIDAD"),
            fecha == null ? LocalDateTime.now() : fecha.toLocalDateTime(),
            rs.getString("ID_USUARIO"),
            rs.getString("DESCRIPCION")
        );
    }
}
