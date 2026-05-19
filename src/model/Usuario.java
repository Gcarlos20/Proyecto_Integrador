package model;

public class Usuario {
    
    private int id;
    private String nombre;
    private String contraseña;
    private String rol; // Ejemplo: "admin", "usuario", consultor

    public Usuario(int id, String nombre, String contraseña, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public Usuario(String nombre, String contraseña, String rol) {
        this(0, nombre, contraseña, rol);
    }
    
    public int getId() {return id; }
    public String getNombre() {return nombre; }
    public String getContraseña() {return contraseña; }
    public String getRol() {return rol; }




}
