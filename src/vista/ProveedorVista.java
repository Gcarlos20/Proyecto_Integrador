package vista;

import controller.ProveedorController;
import model.proveedor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * VISTA: Gestion de Proveedores
 * Descripcion: CRUD de proveedores alineado con la tabla proveedores.
 */
public class ProveedorVista extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre, txtContacto, txtTelefono, txtEmail, txtDireccion;
    private final ProveedorController controller = new ProveedorController();
    private int idSeleccionado = -1;

    public ProveedorVista() {
        setTitle("Gestion de Proveedores");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(800, 450));

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

        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("PROVEEDORES");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearPanelFormulario(), crearPanelTabla());
        splitPane.setOpaque(false);
        splitPane.setDividerLocation(360);
        splitPane.setResizeWeight(0.35);
        centerPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        cargarTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 50, 75));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel formTitle = new JLabel("AGREGAR / EDITAR PROVEEDOR");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(Color.WHITE);
        formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(formTitle);
        panel.add(Box.createVerticalStrut(20));

        txtNombre = crearCampo(panel, "Nombre");
        txtContacto = crearCampo(panel, "Contacto");
        txtTelefono = crearCampo(panel, "Telefono");
        txtEmail = crearCampo(panel, "Email");
        txtDireccion = crearCampo(panel, "Direccion");

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(crearBoton("AGREGAR", new Color(70, 150, 220), this::agregarProveedor));
        buttonPanel.add(crearBoton("EDITAR", new Color(100, 180, 120), this::editarProveedor));
        buttonPanel.add(crearBoton("ELIMINAR", new Color(220, 100, 80), this::eliminarProveedor));
        buttonPanel.add(crearBoton("LIMPIAR", new Color(150, 150, 150), this::limpiarCampos));
        panel.add(Box.createVerticalStrut(10));
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

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 50, 75));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel tableTitle = new JLabel("LISTA DE PROVEEDORES");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(Color.WHITE);
        panel.add(tableTitle, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
            new String[]{"ID Proveedor", "Nombre", "Contacto", "Telefono", "Email", "Direccion", "Activo"}, 0
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
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    idSeleccionado = (int) modelo.getValueAt(fila, 0);
                    txtNombre.setText(String.valueOf(modelo.getValueAt(fila, 1)));
                    txtContacto.setText(String.valueOf(modelo.getValueAt(fila, 2)));
                    txtTelefono.setText(String.valueOf(modelo.getValueAt(fila, 3)));
                    txtEmail.setText(String.valueOf(modelo.getValueAt(fila, 4)));
                    txtDireccion.setText(String.valueOf(modelo.getValueAt(fila, 5)));
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

    private JButton crearBoton(String texto, Color color, Runnable action) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    private void agregarProveedor() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del proveedor es obligatorio",
                    "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        controller.agregar(
                txtNombre.getText().trim(),
                txtContacto.getText().trim(),
                txtTelefono.getText().trim(),
                txtEmail.getText().trim(),
                txtDireccion.getText().trim()
        );
        JOptionPane.showMessageDialog(this, "Proveedor agregado exitosamente",
                "Exito", JOptionPane.INFORMATION_MESSAGE);
        limpiarCampos();
        cargarTabla();
    }

    private void editarProveedor() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor de la tabla",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del proveedor es obligatorio",
                    "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        controller.actualizar(
                idSeleccionado,
                txtNombre.getText().trim(),
                txtContacto.getText().trim(),
                txtTelefono.getText().trim(),
                txtEmail.getText().trim(),
                txtDireccion.getText().trim()
        );
        JOptionPane.showMessageDialog(this, "Proveedor actualizado exitosamente",
                "Exito", JOptionPane.INFORMATION_MESSAGE);
        limpiarCampos();
        cargarTabla();
    }

    private void eliminarProveedor() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor de la tabla",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Esta seguro que desea desactivar este proveedor?",
                "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.eliminar(idSeleccionado);
            JOptionPane.showMessageDialog(this, "Proveedor desactivado exitosamente",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarTabla();
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtContacto.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
        idSeleccionado = -1;
        tabla.clearSelection();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<proveedor> proveedores = controller.obtenerTodos();
        for (proveedor p : proveedores) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getContacto(),
                p.getTelefono(),
                p.getEmail(),
                p.getDireccion(),
                p.isActivo()
            });
        }
    }
}
