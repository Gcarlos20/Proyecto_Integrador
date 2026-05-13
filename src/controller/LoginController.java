package controller;

import Dao.UsuarioDao;
import model.Usuario;


public class LoginController {
    
    private final UsuarioDao usuarioDao;

    public LoginController() {
        this.usuarioDao = new UsuarioDao();
    }

    // BLOQUE: Login
    // Para: Validar usuario, contrasena y rol contra la base de datos.
  public Usuario login(String username, String password, String rol) {
    return usuarioDao.login(username, password, rol);
}

}
