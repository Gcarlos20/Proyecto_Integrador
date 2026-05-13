package vista;

import util.session;
import util.Permisos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * VISTA: Dashboard Principal
 * Descripción: Panel principal donde el usuario accede a todos los módulos del sistema
 * Módulos: Productos, Inventario, Compra, Reportes, Últimos Movimientos, Configuración, Ayuda
 * Características: Diseño responsive, botones modernos, información del usuario
 */
public class DeshboardVista extends JFrame {

    public DeshboardVista() {
        setTitle("Sistema de Gestión de Inventario - Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(800, 600));

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
        // PANEL SUPERIOR - ENCABEZADO Y USUARIO
        // ============================================
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color headerColor = new Color(20, 35, 60);
                g2d.setColor(headerColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Línea divisoria
                g2d.setColor(new Color(70, 150, 220));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setOpaque(false);

        // Título
        JLabel titleLabel = new JLabel("SISTEMA DE GESTIÓN DE INVENTARIO");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        // Panel derecho con información del usuario
        JPanel userPanel = new JPanel();
        userPanel.setOpaque(false);
        userPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));

        JLabel userLabel = new JLabel("👤 " + session.usuarioActual.getNombre());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(new Color(180, 180, 200));

        JLabel rolLabel = new JLabel("Rol: " + session.usuarioActual.getRol());
        rolLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rolLabel.setForeground(new Color(70, 150, 220));

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLogout.setBackground(new Color(220, 80, 80));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            session.usuarioActual = null;
            new LoginVista().setVisible(true);
            dispose();
        });

        userPanel.add(userLabel);
        userPanel.add(new JSeparator(JSeparator.VERTICAL));
        userPanel.add(rolLabel);
        userPanel.add(btnLogout);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ============================================
        // PANEL CENTRAL - MÓDULOS
        // ============================================
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        contentPanel.setLayout(new GridLayout(0, 2, 20, 20));

        // Botones de módulos
        // Para: Abrir cada seccion y respetar los permisos del rol actual.
        contentPanel.add(crearBotoModulo("📦 PRODUCTOS", "Gestionar productos del inventario",
                new Color(70, 150, 220), Permisos.puedeVerReportes(), () -> new productoVista().setVisible(true)));

        contentPanel.add(crearBotoModulo("📊 INVENTARIO", "Ver estado actual del inventario",
                new Color(100, 180, 120), true, () -> new InventarioVista().setVisible(true)));

        contentPanel.add(crearBotoModulo("🛒 COMPRA", "Comprar productos y ver compras/ventas",
                new Color(80, 165, 185), true, () -> new CompraVista().setVisible(true)));

        contentPanel.add(crearBotoModulo("📈 REPORTES", "Generar reportes de inventario",
                new Color(220, 150, 70), Permisos.puedeVerReportes(), () -> new ReporteVista().setVisible(true)));

        contentPanel.add(crearBotoModulo("PROVEEDORES", "Gestionar proveedores registrados",
                new Color(90, 150, 170), Permisos.puedeGestionarProveedores(), () -> new ProveedorVista().setVisible(true)));

        contentPanel.add(crearBotoModulo("⏱️ ÚLTIMOS MOVIMIENTOS", "Ver movimientos recientes",
                new Color(180, 100, 150), true, () -> new UltimosMovimientosVista().setVisible(true)));

        contentPanel.add(crearBotoModulo("⚙️ CONFIGURACIÓN", "Configurar sistema y ajustes",
                new Color(150, 120, 180), Permisos.esAdmin(), () -> new ConfiguracionVista().setVisible(true)));

        contentPanel.add(crearBotoModulo("❓ AYUDA", "Ver documentación y soporte",
                new Color(100, 140, 180), true, () -> new AyudaVista().setVisible(true)));

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // ============================================
        // PANEL INFERIOR - INFORMACIÓN
        // ============================================
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        footerPanel.setLayout(new BorderLayout());

        JLabel footerLabel = new JLabel("© 2026 Sistema de Inventario | Versión 1.0");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(100, 120, 140));

        footerPanel.add(footerLabel, BorderLayout.WEST);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Método auxiliar para crear botones de módulos
     * Parámetros: título, descripción, color de fondo, acción
     */
    private JPanel crearBotoModulo(String titulo, String descripcion, Color color, boolean habilitado, Runnable action) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con sombra
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 15, 15);
                
                // Fondo principal
                // Para: Mostrar mas oscuro el modulo cuando el rol no tiene acceso.
                g2d.setColor(habilitado ? color : new Color(90, 95, 110));
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 15, 15);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setCursor(new Cursor(habilitado ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));

        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><center>" + descripcion + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(220, 220, 230));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(descLabel);
        panel.add(Box.createVerticalGlue());

        // Efecto hover
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setCursor(new Cursor(habilitado ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
                panel.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.repaint();
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (habilitado) {
                    action.run();
                } else {
                    JOptionPane.showMessageDialog(panel,
                            "Su rol no tiene permiso para usar esta seccion",
                            "Permiso denegado",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        return panel;
    }
}
