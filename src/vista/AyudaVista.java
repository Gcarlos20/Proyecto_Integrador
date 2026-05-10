package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * VISTA: Ayuda y Soporte
 * Descripción: Centro de ayuda y documentación del sistema
 * Funcionalidades: Tutoriales, FAQ, soporte técnico, guías de uso
 * Características: Interfaz amigable, navegable
 */
public class AyudaVista extends JFrame {

    public AyudaVista() {
        setTitle("Ayuda y Soporte");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(700, 500));

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
        JLabel titleLabel = new JLabel("❓ AYUDA Y SOPORTE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ============================================
        // PANEL CENTRAL CON PESTAÑA
        // ============================================
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        tabbedPane.setBackground(new Color(40, 50, 75));
        tabbedPane.setForeground(Color.WHITE);

        // Pestaña: Guía Rápida
        tabbedPane.addTab("Guía Rápida", crearPanelGuiaRapida());

        // Pestaña: FAQ
        tabbedPane.addTab("Preguntas Frecuentes", crearPanelFAQ());

        // Pestaña: Módulos
        tabbedPane.addTab("Módulos del Sistema", crearPanelModulos());

        // Pestaña: Soporte
        tabbedPane.addTab("Contacto y Soporte", crearPanelSoporte());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    /**
     * BLOQUE: Panel Guía Rápida
     * Para: Mostrar guía de inicio rápido
     */
    private JComponent crearPanelGuiaRapida() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 50, 75));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel[] titulos = {
            crearTitulo("1. Acceder al Sistema"),
            crearTitulo("2. Navegar por el Dashboard"),
            crearTitulo("3. Gestionar Productos"),
            crearTitulo("4. Generar Reportes"),
            crearTitulo("5. Ver Movimientos")
        };

        String[] descripciones = {
            "Ingrese sus credenciales de usuario y seleccione su rol para acceder al sistema.",
            "Desde el dashboard puede acceder a todos los módulos disponibles según su rol.",
            "En la sección de productos puede agregar, editar y eliminar productos del inventario.",
            "Genere reportes en formato PDF y Excel con la información que necesite.",
            "Revise el historial de movimientos de inventario en tiempo real."
        };

        for (int i = 0; i < titulos.length; i++) {
            panel.add(titulos[i]);
            JLabel desc = new JLabel(descripciones[i]);
            desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            desc.setForeground(new Color(200, 200, 220));
            panel.add(desc);
            panel.add(Box.createVerticalStrut(15));
        }

        panel.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    /**
     * BLOQUE: Panel FAQ
     * Para: Mostrar preguntas frecuentes
     */
    private JComponent crearPanelFAQ() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 50, 75));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        String[][] faqData = {
            {"¿Cómo cambio mi contraseña?", "Dirígase a Configuración > Cambiar Contraseña"},
            {"¿Puedo exportar los reportes?", "Sí, todos los reportes se pueden exportar a PDF o Excel"},
            {"¿Cuántos usuarios puedo crear?", "Sin límite, depende de su plan de suscripción"},
            {"¿Los datos se guardan automáticamente?", "Sí, todos los cambios se guardan en tiempo real"},
            {"¿Necesito conexión a internet?", "Sí, para sincronizar con la base de datos en la nube"}
        };

        for (String[] faq : faqData) {
            panel.add(crearTitulo("P: " + faq[0]));
            JLabel resp = new JLabel("R: " + faq[1]);
            resp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            resp.setForeground(new Color(180, 200, 220));
            panel.add(resp);
            panel.add(Box.createVerticalStrut(15));
        }

        panel.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    /**
     * BLOQUE: Panel Módulos
     * Para: Describir cada módulo del sistema
     */
    private JComponent crearPanelModulos() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 50, 75));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        String[][] modulos = {
            {"📦 PRODUCTOS", "Gestione el catálogo de productos con precios y cantidad"},
            {"📊 INVENTARIO", "Visualice el estado actual del inventario en tiempo real"},
            {"📈 REPORTES", "Genere reportes detallados del inventario y movimientos"},
            {"⏱️ MOVIMIENTOS", "Revise el historial de entradas, salidas y ajustes"},
            {"⚙️ CONFIGURACIÓN", "Ajuste parámetros del sistema y base de datos"},
            {"❓ AYUDA", "Acceda a documentación y soporte técnico"}
        };

        for (String[] mod : modulos) {
            panel.add(crearTitulo(mod[0]));
            JLabel desc = new JLabel(mod[1]);
            desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            desc.setForeground(new Color(200, 200, 220));
            panel.add(desc);
            panel.add(Box.createVerticalStrut(15));
        }

        panel.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    /**
     * BLOQUE: Panel Soporte
     * Para: Información de contacto y soporte
     */
    private JComponent crearPanelSoporte() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 50, 75));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        panel.add(crearTitulo("Contacto de Soporte"));
        panel.add(crearLineaContacto("📧 Email", "soporte@inventario.com"));
        panel.add(crearLineaContacto("📞 Teléfono", "+34 912 345 678"));
        panel.add(crearLineaContacto("🌐 Sitio Web", "www.inventario.com"));
        panel.add(Box.createVerticalStrut(30));

        panel.add(crearTitulo("Horario de Atención"));
        JLabel horario = new JLabel("Lunes a Viernes: 9:00 - 18:00\nSábado: 10:00 - 14:00");
        horario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        horario.setForeground(new Color(200, 200, 220));
        panel.add(horario);
        panel.add(Box.createVerticalStrut(30));

        panel.add(crearTitulo("Información del Sistema"));
        panel.add(crearLineaContacto("Versión", "1.0.0"));
        panel.add(crearLineaContacto("Última Actualización", "30/04/2026"));
        panel.add(crearLineaContacto("© Derechos", "2026 - Sistema de Inventario"));

        panel.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    /**
     * BLOQUE: Método Auxiliar para Crear Título
     * Para: Estandarizar títulos
     */
    private JLabel crearTitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(70, 150, 220));
        return label;
    }

    /**
     * BLOQUE: Método Auxiliar para Crear Línea de Contacto
     * Para: Mostrar información estructurada
     */
    private JPanel crearLineaContacto(String clave, String valor) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel keyLabel = new JLabel(clave);
        keyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        keyLabel.setForeground(new Color(150, 150, 170));

        JLabel valueLabel = new JLabel(valor);
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        valueLabel.setForeground(Color.WHITE);

        panel.add(keyLabel, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.EAST);

        return panel;
    }
}
