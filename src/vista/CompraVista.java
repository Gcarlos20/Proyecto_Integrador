package vista;

import controller.CompraController;
import controller.ProveedorController;
import controller.productoController;
import model.producto;
import model.proveedor;
import util.Permisos;
import util.session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * VISTA: Compra
 * Para: Comprar productos, aumentar inventario y consultar detalle_compra, detalle_venta y ventas.
 */
public class CompraVista extends JFrame {

    private JComboBox<ProductoItem> cbProducto;
    private JComboBox<ProveedorItem> cbProveedor;
    private JTextField txtCantidad;
    private JTextField txtPrecioUnitario;
    private JTextField txtSubtotal;
    private JTextArea txtObservaciones;
    private JButton btnComprar;
    private JButton btnVender;
    private JButton btnLimpiar;
    private JButton btnRefrescar;
    private DefaultTableModel modeloDetalleCompra;
    private DefaultTableModel modeloCompras;
    private DefaultTableModel modeloDetalleVenta;
    private DefaultTableModel modeloVentas;
    private final CompraController compraController = new CompraController();
    private final productoController productoController = new productoController();
    private final ProveedorController proveedorController = new ProveedorController();

    public CompraVista() {
        setTitle("Compras y Ventas de Productos");
        setSize(1150, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(850, 500));

        JPanel mainPanel = crearPanelPrincipal();
        mainPanel.add(crearEncabezado(), BorderLayout.NORTH);
        mainPanel.add(crearContenido(), BorderLayout.CENTER);

        add(mainPanel);
        aplicarPermisos();
        cargarProductos();
        cargarProveedores();
        cargarTablas();
    }

    /**
     * BLOQUE: Panel principal
     * Para: Mantener el mismo fondo degradado de las otras secciones.
     */
    private JPanel crearPanelPrincipal() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(15, 25, 45),
                        0, getHeight(), new Color(30, 50, 90)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BorderLayout());
        return panel;
    }

    /**
     * BLOQUE: Encabezado
     * Para: Mostrar el logo y titulo de la seccion Compra.
     */
    private JPanel crearEncabezado() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("COMPRAS Y VENTAS DE PRODUCTOS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);
        return panel;
    }

    /**
     * BLOQUE: Contenido
     * Para: Dividir formulario de compra y tablas relacionadas.
     */
    private JPanel crearContenido() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearPanelFormulario(), crearPanelTablas());
        splitPane.setOpaque(false);
        splitPane.setDividerLocation(360);
        splitPane.setResizeWeight(0.32);
        centerPanel.add(splitPane, BorderLayout.CENTER);
        return centerPanel;
    }

    /**
     * BLOQUE: Formulario
     * Para: Capturar producto, cantidad, precio y observaciones de la compra.
     */
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 50, 75));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel formTitle = new JLabel("REGISTRAR COMPRA / VENTA");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(Color.WHITE);
        formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(formTitle);
        panel.add(Box.createVerticalStrut(20));

        cbProducto = crearComboProducto(panel, "Producto");
        cbProveedor = crearComboProveedor(panel, "Proveedor");
        txtCantidad = crearCampo(panel, "Cantidad");
        txtPrecioUnitario = crearCampo(panel, "Precio unitario");
        txtSubtotal = crearCampo(panel, "Subtotal");
        txtSubtotal.setEditable(false);
        txtSubtotal.setText("0.00");
        agregarCalculoSubtotal();
        txtObservaciones = crearArea(panel, "Observaciones");

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setOpaque(false);
        btnComprar = crearBoton("COMPRAR", new Color(70, 150, 220), this::registrarCompra);
        btnVender = crearBoton("VENDER", new Color(220, 135, 70), this::registrarVenta);
        btnLimpiar = crearBoton("LIMPIAR", new Color(150, 150, 150), this::limpiarCampos);
        btnRefrescar = crearBoton("REFRESCAR", new Color(100, 180, 120), this::refrescarTodo);
        buttonPanel.add(btnComprar);
        buttonPanel.add(btnVender);
        buttonPanel.add(btnLimpiar);
        buttonPanel.add(btnRefrescar);

        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(crearNotaPermisos());
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    /**
     * BLOQUE: Combo de productos
     * Para: Seleccionar el producto que se va a comprar.
     */
    private JComboBox<ProductoItem> crearComboProducto(JPanel panel, String etiqueta) {
        JLabel label = crearEtiqueta(etiqueta);
        panel.add(label);

        JComboBox<ProductoItem> combo = new JComboBox<>();
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        combo.setBackground(new Color(50, 65, 95));
        combo.setForeground(Color.WHITE);
        panel.add(combo);
        panel.add(Box.createVerticalStrut(12));
        return combo;
    }

    /**
     * BLOQUE: Combo de proveedores
     * Para: Seleccionar a que proveedor se le hace la compra.
     */
    private JComboBox<ProveedorItem> crearComboProveedor(JPanel panel, String etiqueta) {
        JLabel label = crearEtiqueta(etiqueta);
        panel.add(label);

        JComboBox<ProveedorItem> combo = new JComboBox<>();
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        combo.setBackground(new Color(50, 65, 95));
        combo.setForeground(Color.WHITE);
        panel.add(combo);
        panel.add(Box.createVerticalStrut(12));
        return combo;
    }

    /**
     * BLOQUE: Campo de texto
     * Para: Crear entradas con el mismo estilo visual del sistema.
     */
    private JTextField crearCampo(JPanel panel, String etiqueta) {
        panel.add(crearEtiqueta(etiqueta));
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
     * BLOQUE: Area de observaciones
     * Para: Guardar una nota libre de la compra.
     */
    private JTextArea crearArea(JPanel panel, String etiqueta) {
        panel.add(crearEtiqueta(etiqueta));
        JTextArea area = new JTextArea(4, 20);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(50, 65, 95));
        area.setForeground(Color.WHITE);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 120, 160), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        panel.add(scroll);
        panel.add(Box.createVerticalStrut(12));
        return area;
    }

    /**
     * BLOQUE: Calcular subtotal
     * Para: Mostrar cantidad * precio antes de guardar la compra.
     */
    private void agregarCalculoSubtotal() {
        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarSubtotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarSubtotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarSubtotal();
            }
        };
        txtCantidad.getDocument().addDocumentListener(listener);
        txtPrecioUnitario.getDocument().addDocumentListener(listener);
    }

    /**
     * BLOQUE: Actualizar subtotal
     * Para: Recalcular el valor mostrado sin guardar todavia.
     */
    private void actualizarSubtotal() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double precio = Double.parseDouble(txtPrecioUnitario.getText().trim());
            txtSubtotal.setText(String.format("%.2f", cantidad * precio));
        } catch (NumberFormatException e) {
            txtSubtotal.setText("0.00");
        }
    }

    /**
     * BLOQUE: Etiqueta
     * Para: Unificar textos pequenos de formulario.
     */
    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        label.setForeground(new Color(200, 200, 220));
        return label;
    }

    /**
     * BLOQUE: Nota de permisos
     * Para: Explicar por que el boton comprar puede estar deshabilitado.
     */
    private JLabel crearNotaPermisos() {
        JLabel label = new JLabel("<html><center>Admin y usuario pueden comprar/vender.<br>Consultor solo puede consultar tablas.</center></html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        label.setForeground(new Color(180, 190, 210));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /**
     * BLOQUE: Panel de tablas
     * Para: Ver detalle_compra, detalle_venta y venta en una sola seccion.
     */
    private JTabbedPane crearPanelTablas() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabs.addTab("Compras", crearTablaCompras());
        tabs.addTab("Detalle Compra", crearTablaDetalleCompra());
        tabs.addTab("Detalle Venta", crearTablaDetalleVenta());
        tabs.addTab("Ventas", crearTablaVentas());
        return tabs;
    }

    /**
     * BLOQUE: Tabla compras
     * Para: Mostrar cada compra con su proveedor, total y usuario.
     */
    private JPanel crearTablaCompras() {
        modeloCompras = new DefaultTableModel(
                new String[]{"ID Compra", "Fecha", "Proveedor", "Total", "Usuario", "Observaciones"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        return crearPanelTabla("COMPRAS", modeloCompras);
    }

    /**
     * BLOQUE: Tabla detalle_compra
     * Para: Mostrar cada compra con producto, cantidad y subtotal.
     */
    private JPanel crearTablaDetalleCompra() {
        modeloDetalleCompra = new DefaultTableModel(
                new String[]{"ID Detalle", "ID Compra", "Fecha", "Proveedor", "Producto", "Cantidad", "Precio", "Subtotal", "Usuario", "Observaciones"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        return crearPanelTabla("DETALLE_COMPRA", modeloDetalleCompra);
    }

    /**
     * BLOQUE: Tabla detalle_venta
     * Para: Mostrar detalle_venta cuando ya exista informacion de ventas.
     */
    private JPanel crearTablaDetalleVenta() {
        modeloDetalleVenta = new DefaultTableModel(
                new String[]{"ID Detalle", "ID Venta", "ID Producto", "Cantidad", "Precio"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        return crearPanelTabla("DETALLE_VENTA", modeloDetalleVenta);
    }

    /**
     * BLOQUE: Tabla ventas
     * Para: Mostrar la tabla venta principal.
     */
    private JPanel crearTablaVentas() {
        modeloVentas = new DefaultTableModel(
                new String[]{"ID Venta", "Fecha", "Total", "Usuario", "Observacion"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        return crearPanelTabla("VENTAS", modeloVentas);
    }

    /**
     * BLOQUE: Panel tabla
     * Para: Aplicar el mismo diseno a todas las tablas de la seccion.
     */
    private JPanel crearPanelTabla(String titulo, DefaultTableModel modelo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 50, 75));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel tableTitle = new JLabel(titulo);
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(Color.WHITE);
        panel.add(tableTitle, BorderLayout.NORTH);

        JTable tabla = new JTable(modelo);
        tabla.setBackground(new Color(50, 65, 95));
        tabla.setForeground(Color.WHITE);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setBackground(new Color(30, 45, 70));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBackground(new Color(50, 65, 95));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    /**
     * BLOQUE: Boton
     * Para: Crear botones con color y accion reutilizable.
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
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(color.brighter());
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    /**
     * BLOQUE: Aplicar permisos
     * Para: Desactivar compra al rol consultor y dejarle solo consulta.
     */
    private void aplicarPermisos() {
        boolean puedeComprar = Permisos.puedeRegistrarCompras();
        btnComprar.setEnabled(puedeComprar);
        btnVender.setEnabled(puedeComprar);
        cbProducto.setEnabled(puedeComprar);
        cbProveedor.setEnabled(puedeComprar);
        txtCantidad.setEnabled(puedeComprar);
        txtPrecioUnitario.setEnabled(puedeComprar);
        txtSubtotal.setEnabled(false);
        txtObservaciones.setEnabled(puedeComprar);
    }

    /**
     * BLOQUE: Cargar productos
     * Para: Llenar el combo con productos existentes.
     */
    private void cargarProductos() {
        cbProducto.removeAllItems();
        List<producto> productos = productoController.listar();
        for (producto p : productos) {
            cbProducto.addItem(new ProductoItem(p));
        }
    }

    /**
     * BLOQUE: Cargar proveedores
     * Para: Llenar el combo con proveedores existentes.
     */
    private void cargarProveedores() {
        cbProveedor.removeAllItems();
        List<proveedor> proveedores = proveedorController.obtenerTodos();
        for (proveedor p : proveedores) {
            cbProveedor.addItem(new ProveedorItem(p));
        }
    }

    /**
     * BLOQUE: Cargar tablas
     * Para: Refrescar detalle_compra, detalle_venta y ventas desde MySQL.
     */
    private void cargarTablas() {
        cargarModelo(modeloCompras, compraController.listarCompras());
        cargarModelo(modeloDetalleCompra, compraController.listarDetalleCompra());
        cargarModelo(modeloDetalleVenta, compraController.listarDetalleVenta());
        cargarModelo(modeloVentas, compraController.listarVentas());
    }

    /**
     * BLOQUE: Cargar modelo
     * Para: Pasar filas Object[] al DefaultTableModel.
     */
    private void cargarModelo(DefaultTableModel modelo, List<Object[]> filas) {
        modelo.setRowCount(0);
        for (Object[] fila : filas) {
            modelo.addRow(fila);
        }
    }

    /**
     * BLOQUE: Registrar compra
     * Para: Validar campos, guardar compra y actualizar tablas.
     */
    private void registrarCompra() {
        try {
            ProductoItem item = (ProductoItem) cbProducto.getSelectedItem();
            if (item == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto",
                        "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ProveedorItem proveedorItem = (ProveedorItem) cbProveedor.getSelectedItem();
            if (proveedorItem == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un proveedor",
                        "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double precioUnitario = Double.parseDouble(txtPrecioUnitario.getText().trim());
            String usuario = session.usuarioActual == null ? "sin_usuario" : session.usuarioActual.getNombre();
            int usuarioId = session.usuarioActual == null ? 1 : session.usuarioActual.getId();

            boolean guardado = compraController.registrarCompra(
                    item.getId(),
                    proveedorItem.getId(),
                    cantidad,
                    precioUnitario,
                    usuarioId,
                    usuario,
                    txtObservaciones.getText().trim()
            );

            if (guardado) {
                JOptionPane.showMessageDialog(this, "Compra registrada exitosamente",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                refrescarTodo();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la compra. Revise que la migracion de compras exista.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser numeros validos",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * BLOQUE: Registrar venta
     * Para: Guardar venta, detalle_venta y descontar stock.
     */
    private void registrarVenta() {
        try {
            ProductoItem item = (ProductoItem) cbProducto.getSelectedItem();
            if (item == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto",
                        "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double precio = Double.parseDouble(txtPrecioUnitario.getText().trim());
            String usuario = session.usuarioActual == null ? "sin_usuario" : session.usuarioActual.getNombre();

            boolean guardado = compraController.registrarVenta(
                    item.getId(),
                    cantidad,
                    precio,
                    usuario,
                    txtObservaciones.getText().trim()
            );

            if (guardado) {
                JOptionPane.showMessageDialog(this, "Venta registrada exitosamente",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                refrescarTodo();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la venta. Revise stock y datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser numeros validos",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * BLOQUE: Refrescar todo
     * Para: Actualizar productos y tablas despues de comprar.
     */
    private void refrescarTodo() {
        cargarProductos();
        cargarProveedores();
        cargarTablas();
    }

    /**
     * BLOQUE: Limpiar
     * Para: Vaciar campos del formulario.
     */
    private void limpiarCampos() {
        txtCantidad.setText("");
        txtPrecioUnitario.setText("");
        txtSubtotal.setText("0.00");
        txtObservaciones.setText("");
        if (cbProducto.getItemCount() > 0) {
            cbProducto.setSelectedIndex(0);
        }
        if (cbProveedor.getItemCount() > 0) {
            cbProveedor.setSelectedIndex(0);
        }
    }

    /**
     * CLASE: ProductoItem
     * Para: Mostrar nombre del producto en el combo y conservar su ID.
     */
    private static class ProductoItem {
        private final producto producto;

        ProductoItem(producto producto) {
            this.producto = producto;
        }

        int getId() {
            return producto.getId();
        }

        @Override
        public String toString() {
            return producto.getId() + " - " + producto.getNombre() + " (stock: " + producto.getStock() + ")";
        }
    }

    /**
     * CLASE: ProveedorItem
     * Para: Mostrar nombre del proveedor y conservar su ID.
     */
    private static class ProveedorItem {
        private final proveedor proveedor;

        ProveedorItem(proveedor proveedor) {
            this.proveedor = proveedor;
        }

        int getId() {
            return proveedor.getId();
        }

        @Override
        public String toString() {
            return proveedor.getId() + " - " + proveedor.getNombre();
        }
    }
}
