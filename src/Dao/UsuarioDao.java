package Dao;

import conexion.conexionBD;
import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDao {

    // BLOQUE: Validar credenciales
    // Para: Buscar un usuario activo con nombre, contraseña y rol correctos.
    public Usuario login(String nombre, String contrasena, String rol) {
        System.out.println("Intentando login con nombre: '" + nombre + "', contraseña: '" + contrasena + "', rol: '" + rol + "'");

        String sql = """
            SELECT 
                u.ID_USUARIO AS id_usuario,
                u.NOMBRE AS nombre,
                u.PASSWORD AS contrasena,
                r.TIPO AS rol
            FROM USUARIO u
            INNER JOIN ROLES r 
                ON u.ID_ROL = r.ID_ROL
            WHERE u.NOMBRE = ?
              AND u.PASSWORD = ?
              AND LOWER(r.TIPO) = LOWER(?)
              AND u.ESTADO = 'ACTIVO'
              AND r.ESTADO = 'ACTIVO'
        """;

        try (
            Connection conn = conexionBD.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, nombre);
            ps.setString(2, contrasena);
            ps.setString(3, rol);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Usuario encontrado: " + rs.getString("nombre"));
                    return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("contrasena"),
                        rs.getString("rol")
                    );
                } else {
                    System.out.println("Usuario no encontrado");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
        }

        return null;
    }
}
