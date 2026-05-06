package main;

import vista.LoginVista;

public class main {
    public static void main(String[] args) {
        // Iniciar la aplicación mostrando la vista de login
        LoginVista loginVista = new LoginVista();
        loginVista.setVisible(true);
    }
}