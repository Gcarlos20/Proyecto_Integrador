package model;

public class Usuario {
    
    private String nombre;
    private String contraseña;
    private String rol; // Ejemplo: "admin", "usuario", consultor

    public Usuario(String nombre, String contraseña, String rol) {
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.rol = rol;
    }
    
    public String getNombre() {return nombre; }
    public String getContraseña() {return contraseña; }
    public String getRol() {return rol; }




}