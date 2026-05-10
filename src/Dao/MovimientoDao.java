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

public class MovimientoDao {

    // BLOQUE: Registrar movimiento
    // Para: Guardar el movimiento y ajustar el stock del producto en una transaccion.
    public boolean registrar(int productoId, String tipo, int cantidad, String usuario, String observaciones) {
        String insertar = "INSERT INTO movimientos (id_producto, tipo, cantidad, fecha, usuario, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
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

    // BLOQUE: Listar movimientos
    // Para: Obtener el historial completo del inventario.
    public List<movimiento> obtenerTodos() {
        List<movimiento> lista = new ArrayList<>();
        String sql = "SELECT id_movimiento, id_producto, tipo, cantidad, fecha, usuario, observaciones "
                + "FROM movimientos ORDER BY fecha DESC, id_movimiento DESC";

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

    // BLOQUE: Movimientos recientes
    // Para: Consultar los ultimos registros del historial.
    public List<movimiento> obtenerRecientes(int limite) {
        List<movimiento> lista = new ArrayList<>();
        String sql = "SELECT id_movimiento, id_producto, tipo, cantidad, fecha, usuario, observaciones "
                + "FROM movimientos ORDER BY fecha DESC, id_movimiento DESC LIMIT ?";

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

    // BLOQUE: Filtrar por producto
    // Para: Consultar el historial de un producto especifico.
    public List<movimiento> obtenerPorProducto(int productoId) {
        return filtrar("id_producto = ?", productoId);
    }

    // BLOQUE: Filtrar por tipo
    // Para: Consultar entradas, salidas o ajustes.
    public List<movimiento> obtenerPorTipo(String tipo) {
        List<movimiento> lista = new ArrayList<>();
        String sql = "SELECT id_movimiento, id_producto, tipo, cantidad, fecha, usuario, observaciones "
                + "FROM movimientos WHERE tipo = ? ORDER BY fecha DESC, id_movimiento DESC";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipo);
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

    // BLOQUE: Saldo por producto
    // Para: Calcular el efecto neto de todos los movimientos de un producto.
    public int calcularSaldo(int productoId) {
        String sql = "SELECT COALESCE(SUM(CASE "
                + "WHEN tipo = 'ENTRADA' THEN cantidad "
                + "WHEN tipo = 'SALIDA' THEN -cantidad "
                + "ELSE cantidad END), 0) AS saldo "
                + "FROM movimientos WHERE id_producto = ?";

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
        String sql = "SELECT id_movimiento, id_producto, tipo, cantidad, fecha, usuario, observaciones "
                + "FROM movimientos WHERE " + condicion + " ORDER BY fecha DESC, id_movimiento DESC";

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
    private int calcularDelta(String tipo, int cantidad) {
        if ("SALIDA".equalsIgnoreCase(tipo)) {
            return -cantidad;
        }
        return cantidad;
    }

    // BLOQUE: Mapear resultado
    // Para: Convertir una fila SQL en un objeto movimiento.
    private movimiento mapearMovimiento(ResultSet rs) throws SQLException {
        Timestamp fecha = rs.getTimestamp("fecha");
        return new movimiento(
            rs.getInt("id_movimiento"),
            rs.getInt("id_producto"),
            rs.getString("tipo"),
            rs.getInt("cantidad"),
            fecha == null ? LocalDateTime.now() : fecha.toLocalDateTime(),
            rs.getString("usuario"),
            rs.getString("observaciones")
        );
    }
}
