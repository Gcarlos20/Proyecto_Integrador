package model;
public class producto {
    private int id;
    private String codigo;
    private String nombre;
    private String categoria;
    private String descripcion;
    private double precio;
    private int stock;
    private int stockMinimo;
    private Integer proveedorId;

    public producto(int id, String nombre, double precio, int stock) { // Constructor simplificado para actualizar solo nombre, precio y stock
        this(id, null, nombre, null, null, precio, stock, 0, null);
    }

    public producto(int id, String codigo, String nombre, String categoria, String descripcion, // Constructor completo para crear o actualizar un producto con toda su información
                    double precio, int stock, int stockMinimo, Integer proveedorId) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.proveedorId = proveedorId;
    }
    // getters y setters para cada atributo del producto, necesarios para acceder y modificar los datos del producto desde otras clases como el controlador o la vista
    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public int getStockMinimo() { return stockMinimo; }
    public Integer getProveedorId() { return proveedorId; }

    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setStock(int stock) { this.stock = stock; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public void setProveedorId(Integer proveedorId) { this.proveedorId = proveedorId; }
}
