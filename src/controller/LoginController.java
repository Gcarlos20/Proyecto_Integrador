package controller;

import model.Usuario;
import java.util.ArrayList; // quitar despues
import java.util.List; // quitar despues 


public class LoginController {
    
    private List<Usuario> usuarios; // quitar despues

    public LoginController() {
        this.usuarios = new ArrayList<>(); // quitar despues
        // Agregar algunos usuarios de ejemplo (quitar despues)
        usuarios.add(new Usuario("admin", "admin123", "admin"));
        usuarios.add(new Usuario("user1", "password1", "usuario"));
    }

    public Usuario login(String username, String password, String rol) {
        for (Usuario usuario : usuarios) { // quitar despues
            if (usuario.getNombre().equals(username) && usuario.getContraseña().equals(password) && usuario.getRol().equals(rol)) {
                return usuario; // Login exitoso, devolver usuario
            }
        }
        return null; // Login fallido
    }


}
