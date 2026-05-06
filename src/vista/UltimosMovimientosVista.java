package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * VISTA: Últimos Movimientos
 * Descripción: Muestra el historial de movimientos recientes del inventario
 * Funcionalidades: Ver entradas, salidas y ajustes de stock
 * Características: Filtrado por tipo, búsqueda, ordenamiento
 */
public class UltimosMovimientosVista extends JFrame {

    public UltimosMovimientosVista() {
        setTitle("Últimos Movimientos");
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
        // PANEL SUPERIOR
        // ============================================
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("⏱️ ÚLTIMOS MOVIMIENTOS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ============================================
        // PANEL CENTRAL
        // ============================================
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Panel de filtros
        JPanel filterPanel = new JPanel();
        filterPanel.setOpaque(false);
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setForeground(Color.WHITE);
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Todos", "ENTRADA", "SALIDA", "AJUSTE"});
        cbTipo.setBackground(new Color(50, 65, 95));
        cbTipo.setForeground(Color.WHITE);

        filterPanel.add(lblTipo);
        filterPanel.add(cbTipo);
        centerPanel.add(filterPanel, BorderLayout.NORTH);

        // Panel de tabla
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(new Color(40, 50, 75));
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Producto", "Tipo", "Cantidad", "Fecha", "Usuario", "Observaciones"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setBackground(new Color(50, 65, 95));
        tabla.setForeground(Color.WHITE);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setBackground(new Color(30, 45, 70));
        tabla.getTableHeader().setForeground(Color.WHITE);

        // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
        // List<movimiento> movimientos = controller.obtenerMovimientosRecientes();
        // for (movimiento m : movimientos) {
        //     modelo.addRow(new Object[]{m.getId(), m.getProductoId(), m.getTipo(), 
        //         m.getCantidad(), m.getFecha(), m.getUsuario(), m.getObservaciones()});
        // }

        // DATOS DE PRUEBA (ELIMINAR CUANDO SE CONECTE BD)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        modelo.addRow(new Object[]{1, "Laptop", "ENTRADA", 10, "28/04/2026 10:30", "admin", "Compra a proveedor X"});
        modelo.addRow(new Object[]{2, "Mouse", "SALIDA", 5, "28/04/2026 11:15", "usuario1", "Venta a cliente"});
        modelo.addRow(new Object[]{3, "Teclado", "AJUSTE", -2, "28/04/2026 12:00", "admin", "Corrección de inventario"});
        modelo.addRow(new Object[]{4, "Monitor", "ENTRADA", 3, "28/04/2026 14:45", "admin", "Compra"});
        modelo.addRow(new Object[]{5, "Cable USB", "SALIDA", 20, "28/04/2026 16:20", "usuario1", "Venta a cliente"});

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        tablePanel.add(scroll, BorderLayout.CENTER);

        centerPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }
}