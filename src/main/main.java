package main;

import conexion.conexionBD;
import vista.LoginVista;

public class main {
    public static void main(String[] args) {
        // BLOQUE: Probar conexion inicial
        // Para: Mostrar en consola si la base de datos conecta al iniciar el programa.
        conexionBD.probarConexion();

        // Iniciar la aplicación mostrando la vista de login
        LoginVista loginVista = new LoginVista();
        loginVista.setVisible(true);
    }
}
