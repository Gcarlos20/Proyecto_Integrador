package model;

/**
 * MODELO: Proveedor
 * Descripción: Almacena información de proveedores de productos
 * Campos: id, nombre, contacto, telefono, email, direccion, activo
 */
public class proveedor {
    private int id;
    private String nombre;
    private String contacto;
    private String telefono;
    private String email;
    private String direccion;
    private boolean activo;

    // Constructor completo
    public proveedor(int id, String nombre, String contacto, String telefono, String email, String direccion, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.activo = activo;
    }

    // Constructor sin ID
    public proveedor(String nombre, String contacto, String telefono, String email, String direccion) {
        this.nombre = nombre;
        this.contacto = contacto;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.activo = true;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getContacto() { return contacto; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getDireccion() { return direccion; }
    public boolean isActivo() { return activo; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
