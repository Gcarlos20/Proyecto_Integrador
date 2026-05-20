package vista;

import controller.productoController;
import model.producto;
import util.Permisos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * VISTA: Gestión de Productos
 * Descripción: CRUD de productos con tabla y formulario
 * Funcionalidades: Agregar, editar, eliminar productos
 * Características: Diseño responsive, validación de campos
 */
public class productoVista extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtCodigo, txtNombre, txtCategoria, txtDescripcion, txtPrecio, txtCantidad, txtStockMinimo, txtProveedorId;
    private JButton btnAgregar, btnEditar, btnEliminar, btnLimpiar;
    private productoController controller = new productoController();
    private int idSeleccionado = -1;

    public productoVista() {
        setTitle("Gestión de Productos");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(700, 400));

        // ============================================
        // PANEL PRINCIPAL CON GRADIENTE
        // ============================================
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(15, 25, 45);
                Color color2 = new Color(30, 50, 90);
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // ============================================
        // PANEL SUPERIOR - ENCABEZADO
        // ============================================
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("📦 GESTIÓN DE PRODUCTOS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ============================================
        // PANEL CENTRAL - CONTENIDO
        // ============================================
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Panel izquierdo - Formulario
        JPanel formPanel = crearPanelFormulario();
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setOpaque(false);
        formScroll.getViewport().setOpaque(false);
        formScroll.setBorder(null);

        // Panel derecho - Tabla
        JPanel tablePanel = crearPanelTabla();

        // Dividir con JSplitPane para mejor responsividad
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formScroll, tablePanel);
        splitPane.setOpaque(false);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.35);
        centerPanel.add(splitPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        aplicarPermisos();
        cargarTabla();
    }

    /**
     * BLOQUE: Crear Panel de Formulario
     * Para: Ingresar datos del producto (nombre, precio, cantidad)
     */
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 50, 75));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título del formulario
        JLabel formTitle = new JLabel("AGREGAR / EDITAR PRODUCTO");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(Color.WHITE);
        formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(formTitle);
        panel.add(Box.createVerticalStrut(20));

        txtCodigo = crearCampo(panel, "Código");
        txtNombre = crearCampo(panel, "Nombre del Producto");
        txtCategoria = crearCampo(panel, "Categoría");
        txtDescripcion = crearCampo(panel, "Descripción");
        txtPrecio = crearCampo(panel, "Precio (USD)");
        txtCantidad = crearCampo(panel, "Cantidad");
        txtStockMinimo = crearCampo(panel, "Stock Mínimo");
        txtProveedorId = crearCampo(panel, "ID Proveedor (opcional)");

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10));

        btnAgregar = crearBoton("AGREGAR", new Color(70, 150, 220), this::agregarProducto);
        btnEditar = crearBoton("EDITAR", new Color(100, 180, 120), this::editarProducto);
        btnEliminar = crearBoton("ELIMINAR", new Color(220, 100, 80), this::eliminarProducto);
        btnLimpiar = crearBoton("LIMPIAR", new Color(150, 150, 150), this::limpiarCampos);

        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnLimpiar);

        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JTextField crearCampo(JPanel panel, String etiqueta) {
        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        label.setForeground(new Color(200, 200, 220));
        panel.add(label);

        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 120, 160), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setBackground(new Color(50, 65, 95));
        campo.setForeground(Color.WHITE);
        panel.add(campo);
        panel.add(Box.createVerticalStrut(12));
        return campo;
    }

    /**
     * BLOQUE: Crear Panel de Tabla
     * Para: Mostrar productos en tabla con scroll
     */
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 50, 75));
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JLabel tableTitle = new JLabel("LISTA DE PRODUCTOS");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(Color.WHITE);
        panel.add(tableTitle, BorderLayout.NORTH);

        // Tabla
        modelo = new DefaultTableModel(
            new String[]{"ID Producto", "Código", "Nombre", "Categoría", "Descripción", "Precio", "Cantidad", "Stock Mínimo", "ID Proveedor"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setBackground(new Color(50, 65, 95));
        tabla.setForeground(Color.WHITE);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setBackground(new Color(30, 45, 70));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Listener de selección
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    idSeleccionado = (int) modelo.getValueAt(fila, 0);
                    txtCodigo.setText(modelo.getValueAt(fila, 1).toString());
                    txtNombre.setText(modelo.getValueAt(fila, 2).toString());
                    txtCategoria.setText(valorTabla(fila, 3));
                    txtDescripcion.setText(valorTabla(fila, 4));
                    txtPrecio.setText(modelo.getValueAt(fila, 5).toString());
                    txtCantidad.setText(modelo.getValueAt(fila, 6).toString());
                    txtStockMinimo.setText(modelo.getValueAt(fila, 7).toString());
                    txtProveedorId.setText(valorTabla(fila, 8));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBackground(new Color(50, 65, 95));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    /**
     * BLOQUE: Método Auxiliar para crear Botones
     * Para: Estandarizar estilo de botones
     */
    private JButton crearBoton(String texto, Color color, Runnable action) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    private String valorTabla(int fila, int columna) {
        Object valor = modelo.getValueAt(fila, columna);
        return valor == null ? "" : valor.toString();
    }

    private Integer obtenerProveedorId() {
        String valor = txtProveedorId.getText().trim();
        return valor.isEmpty() ? null : Integer.parseInt(valor);
    }

    // ============================================
    // MÉTODOS DE LÓGICA - CRUD
    // ============================================

    /**
     * BLOQUE: Aplicar permisos
     * Para: Permitir al consultor ver productos sin agregar, editar o eliminar.
     */
    private void aplicarPermisos() {
        boolean puedeEditar = Permisos.puedeGestionarProductos();
        btnAgregar.setEnabled(puedeEditar);
        btnEditar.setEnabled(puedeEditar);
        btnEliminar.setEnabled(puedeEditar);
        txtCodigo.setEnabled(puedeEditar);
        txtNombre.setEnabled(puedeEditar);
        txtCategoria.setEnabled(puedeEditar);
        txtDescripcion.setEnabled(puedeEditar);
        txtPrecio.setEnabled(puedeEditar);
        txtCantidad.setEnabled(puedeEditar);
        txtStockMinimo.setEnabled(puedeEditar);
        txtProveedorId.setEnabled(puedeEditar);
    }

    /**
     * BLOQUE: Agregar Producto
     * Para: Insertar nuevo producto en la base de datos
     */
    private void agregarProducto() {
        try {
            if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty()
                    || txtCantidad.getText().isEmpty() || txtStockMinimo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            controller.agregar(
                    txtCodigo.getText(),
                    txtNombre.getText(),
                    txtCategoria.getText(),
                    txtDescripcion.getText(),
                    Double.parseDouble(txtPrecio.getText()),
                    Integer.parseInt(txtCantidad.getText()),
                    Integer.parseInt(txtStockMinimo.getText()),
                    obtenerProveedorId()
            );
            JOptionPane.showMessageDialog(this, "Producto agregado exitosamente", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio, cantidad, stock mínimo e ID proveedor deben ser números válidos",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * BLOQUE: Editar Producto
     * Para: Actualizar datos de un producto existente
     */
    private void editarProducto() {
        try {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty()
                    || txtCantidad.getText().isEmpty() || txtStockMinimo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            controller.actualizar(
                    idSeleccionado,
                    txtCodigo.getText(),
                    txtNombre.getText(),
                    txtCategoria.getText(),
                    txtDescripcion.getText(),
                    Double.parseDouble(txtPrecio.getText()),
                    Integer.parseInt(txtCantidad.getText()),
                    Integer.parseInt(txtStockMinimo.getText()),
                    obtenerProveedorId()
            );
            JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio, cantidad, stock mínimo e ID proveedor deben ser números válidos",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * BLOQUE: Eliminar Producto
     * Para: Borrar un producto de la base de datos
     */
    private void eliminarProducto() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea eliminar este producto?", 
            "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            controller.eliminar(idSeleccionado);
            JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarTabla();
        }
    }

    /**
     * BLOQUE: Limpiar Campos
     * Para: Vaciar formulario y deseleccionar producto
     */
    private void limpiarCampos() {
        txtNombre.setText("");
        txtCodigo.setText("");
        txtCategoria.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtStockMinimo.setText("");
        txtProveedorId.setText("");
        idSeleccionado = -1;
        tabla.clearSelection();
    }

    /**
     * BLOQUE: Cargar Tabla
     * Para: Actualizar tabla con datos de la base de datos
     */
    private void cargarTabla() {
        modelo.setRowCount(0);

        // BLOQUE: Cargar productos
        // Para: Mostrar en la tabla los productos guardados en la base de datos.
        List<producto> productos = controller.listar();
        for (producto p : productos) {
            modelo.addRow(new Object[]{ // addRow agrega una fila a la tabla con los datos del producto
                p.getId(),
                p.getCodigo(),
                p.getNombre(),
                p.getCategoria(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getStock(),
                p.getStockMinimo(),
                p.getProveedorId()
            });
        }
    }
}
