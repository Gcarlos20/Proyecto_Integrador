package conexion;

import java.sql.Connection;
import java.sql.DriverManager;

public class conexionBD {

    Connection conectar = null;

    String usuario = "root";
    String contraseña = "1234";
    String bd = "inventario";
    String ip = "localhost";
    String puerto = "3306";

    String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd;

    public Connection establecerConexion() {

        try {
            conectar = DriverManager.getConnection(cadena, usuario, contraseña);
            System.out.println("Conexión exitosa");
        } catch (Exception e) {
            System.out.println("Error en conexión: " + e.toString());
        }

        return conectar;
    }
}