package vista;

import controller.InventarioController;
import controller.productoController;
import model.producto;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * VISTA: Inventario General
 * Descripción: Muestra el estado actual del inventario con cantidades totales
 * Funcionalidades: Ver cantidad total, cantidad de productos, valor total del inventario
 * Características: Diseño responsive, estadísticas en tiempo real
 */
public class InventarioVista extends JFrame {

    private final InventarioController inventarioController = new InventarioController();
    private final productoController productoController = new productoController();

    public InventarioVista() {
        setTitle("Estado del Inventario");
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
        JLabel titleLabel = new JLabel("📊 ESTADO DEL INVENTARIO");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ============================================
        // PANEL CENTRAL - ESTADÍSTICAS Y TABLA
        // ============================================
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Panel de estadísticas
        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        statsPanel.setLayout(new GridLayout(1, 4, 15, 0));
        statsPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        Object[] estadisticas = inventarioController.obtenerEstadisticas();
        statsPanel.add(crearTarjetaEstadistica("Total Productos", String.valueOf(estadisticas[0]), new Color(70, 150, 220)));
        statsPanel.add(crearTarjetaEstadistica("Cantidad Total", estadisticas[1] + " unidades", new Color(100, 180, 120)));
        statsPanel.add(crearTarjetaEstadistica("Valor Inventario", "$" + String.format("%.2f", estadisticas[2]), new Color(220, 150, 70)));
        statsPanel.add(crearTarjetaEstadistica("Cantidad Baja", inventarioController.obtenerAlertasStock(10) + " productos", new Color(220, 100, 80)));

        centerPanel.add(statsPanel, BorderLayout.NORTH);

        // Panel de tabla
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(new Color(40, 50, 75));
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Tabla de inventario
        DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID Producto", "Nombre", "Cantidad", "Precio", "Valor Total", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setBackground(new Color(50, 65, 95));
        tabla.setForeground(Color.WHITE);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setBackground(new Color(30, 45, 70));
        tabla.getTableHeader().setForeground(Color.WHITE);

        // BLOQUE: Cargar inventario
        // Para: Mostrar productos reales con su valor total y estado de cantidad.
        List<producto> productos = productoController.listar();
        for (producto p : productos) {
            int cantidad = p.getStock();
            String estado = cantidad <= 10 ? "Cantidad Baja" : cantidad <= 50 ? "Normal" : "Alto";
            modelo.addRow(new Object[]{p.getId(), p.getNombre(), cantidad, p.getPrecio(),
                cantidad * p.getPrecio(), estado});
        }

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        tablePanel.add(scroll, BorderLayout.CENTER);

        centerPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    /**
     * BLOQUE: Crear Tarjeta de Estadística
     * Para: Mostrar información clave en tarjetas coloridas
     */
    private JPanel crearTarjetaEstadistica(String titulo, String valor, Color color) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 10, 10);
                
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 10, 10);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(220, 220, 230));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(valor);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(valueLabel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }
}
