package Dao;

import conexion.conexionBD;
import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDao {

    private static final String TABLA_USUARIO = "USUARIO";

    // BLOQUE: Validar credenciales
    // Para: Buscar un usuario activo con nombre y contrasena correctos.
    public Usuario login(String nombre, String contrasena, String rol) {
        try (Connection conn = conexionBD.getConexion()) {
            String columnaContrasena = buscarColumna(conn, "contrasena", "contraseña", "password", "clave");
            if (columnaContrasena == null) {
                System.out.println("Error al validar usuario: no se encontro la columna de contraseña en la tabla " + TABLA_USUARIO);
                return null;
            }

            boolean tieneActivo = buscarColumna(conn, "activo") != null;
            String columnaSql = escaparIdentificador(columnaContrasena);
            String sql = "SELECT nombre, " + columnaSql + " AS contrasena FROM " + TABLA_USUARIO
                    + " WHERE nombre = ? AND " + columnaSql + " = ?"
                    + (tieneActivo ? " AND activo = TRUE" : "");

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, nombre);
                ps.setString(2, contrasena);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Usuario(
                            rs.getString("nombre"),
                            rs.getString("contrasena"),
                            rol
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
        }
        return null;
    }

    private String buscarColumna(Connection conn, String... nombres) throws SQLException {
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TABLA_USUARIO);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String columna = rs.getString("COLUMN_NAME");
                    for (String nombre : nombres) {
                        if (columna.equalsIgnoreCase(nombre)) {
                            return columna;
                        }
                    }
                }
            }
        }
        return null;
    }

    private String escaparIdentificador(String identificador) {
        return "`" + identificador.replace("`", "``") + "`";
    }
}
