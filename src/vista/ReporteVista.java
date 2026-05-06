package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * VISTA: Reportes
 * Descripción: Genera reportes del inventario (PDF, Excel, etc)
 * Funcionalidades: Reportes de stock, ventas, movimientos, análisis
 * Características: Rango de fechas, exportación de datos
 */
public class ReporteVista extends JFrame {

    public ReporteVista() {
        setTitle("Reportes");
        setSize(900, 600);
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
        JLabel titleLabel = new JLabel("📈 REPORTES");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ============================================
        // PANEL CENTRAL
        // ============================================
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridLayout(2, 2, 20, 20));
        centerPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // BLOQUE: Reportes disponibles
        centerPanel.add(crearTarjetaReporte(
            "Reporte de Stock",
            "Estado actual del inventario",
            new Color(70, 150, 220),
            this::generarReporteStock
        ));

        centerPanel.add(crearTarjetaReporte(
            "Reporte de Movimientos",
            "Historial de entradas y salidas",
            new Color(100, 180, 120),
            this::generarReporteMovimientos
        ));

        centerPanel.add(crearTarjetaReporte(
            "Reporte de Proveedores",
            "Análisis de proveedores",
            new Color(220, 150, 70),
            this::generarReporteProveedores
        ));

        centerPanel.add(crearTarjetaReporte(
            "Reporte Personalizado",
            "Crear reporte personalizado",
            new Color(180, 100, 150),
            this::generarReportePersonalizado
        ));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // ============================================
        // PANEL INFERIOR - INFORMACIÓN
        // ============================================
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel footerLabel = new JLabel("Los reportes se descargarán en formato PDF y Excel");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(150, 150, 170));
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * BLOQUE: Crear Tarjeta de Reporte
     * Para: Mostrar opciones de reportes disponibles
     */
    private JPanel crearTarjetaReporte(String titulo, String descripcion, Color color, Runnable action) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 15, 15);
                
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 15, 15);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><center>" + descripcion + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(220, 220, 230));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btn = new JButton("Generar");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(new Color(0, 0, 0, 100));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());

        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(descLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btn);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // ============================================
    // MÉTODOS DE LÓGICA - GENERACIÓN DE REPORTES
    // ============================================

    /**
     * BLOQUE: Generar Reporte de Stock
     * Para: Exportar estado actual del inventario
     */
    private void generarReporteStock() {
        // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
        // List<producto> productos = controller.obtenerTodos();
        // File archivo = ExportarPDF.generarReporteStock(productos);
        // Desktop.getDesktop().open(archivo);

        JOptionPane.showMessageDialog(this, 
            "Reporte de Stock generado y descargado", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * BLOQUE: Generar Reporte de Movimientos
     * Para: Exportar historial de movimientos
     */
    private void generarReporteMovimientos() {
        // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
        // List<movimiento> movimientos = controller.obtenerMovimientos();
        // File archivo = ExportarPDF.generarReporteMovimientos(movimientos);
        // Desktop.getDesktop().open(archivo);

        JOptionPane.showMessageDialog(this, 
            "Reporte de Movimientos generado y descargado", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * BLOQUE: Generar Reporte de Proveedores
     * Para: Análisis de proveedores
     */
    private void generarReporteProveedores() {
        // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
        // List<proveedor> proveedores = controller.obtenerProveedores();
        // File archivo = ExportarPDF.generarReporteProveedores(proveedores);
        // Desktop.getDesktop().open(archivo);

        JOptionPane.showMessageDialog(this, 
            "Reporte de Proveedores generado y descargado", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * BLOQUE: Generar Reporte Personalizado
     * Para: Crear reporte con filtros personalizados
     */
    private void generarReportePersonalizado() {
        // Aquí iría una vista para seleccionar parámetros del reporte
        JOptionPane.showMessageDialog(this, 
            "Función de reporte personalizado en desarrollo", 
            "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}