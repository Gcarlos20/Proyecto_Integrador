package vista;

import conexion.conexionBD;
import util.session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * VISTA: Configuración
 * Descripción: Panel de configuración del sistema
 * Funcionalidades: Ajustes de aplicación, preferencias de usuario, base de datos
 * Características: Interfaz intuitiva, opciones guardables
 */
public class ConfiguracionVista extends JFrame {

    public ConfiguracionVista() {
        setTitle("Configuración");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(600, 400));

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
        JLabel titleLabel = new JLabel("⚙️ CONFIGURACIÓN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ============================================
        // PANEL CENTRAL
        // ============================================
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Sección: Configuración General
        centerPanel.add(crearSeccionConfiguracion("Configuración General"));
        centerPanel.add(crearOpcionConfiguracion("Tema de Interfaz", "Oscuro (actual)"));
        centerPanel.add(crearOpcionConfiguracion("Idioma", "Español"));
        centerPanel.add(Box.createVerticalStrut(20));

        // Sección: Base de Datos
        centerPanel.add(crearSeccionConfiguracion("Base de Datos"));
        centerPanel.add(crearOpcionConfiguracion("Servidor BD", "localhost"));
        centerPanel.add(crearOpcionConfiguracion("Puerto", "3306"));
        centerPanel.add(crearOpcionConfiguracion("Base de Datos", "inventario"));

        JButton btnConectarBD = new JButton("Probar Conexión");
        btnConectarBD.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnConectarBD.setBackground(new Color(70, 150, 220));
        btnConectarBD.setForeground(Color.WHITE);
        btnConectarBD.setMaximumSize(new Dimension(200, 35));
        btnConectarBD.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnConectarBD.setFocusPainted(false);
        btnConectarBD.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConectarBD.addActionListener(e -> {
            // BLOQUE: Probar base de datos
            // Para: Confirmar si la aplicacion puede conectarse con MySQL.
            boolean conectado = conexionBD.probarConexion();
            if (conectado) {
                JOptionPane.showMessageDialog(this, "Conexión exitosa", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error de conexión", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bdPanel = new JPanel();
        bdPanel.setOpaque(false);
        bdPanel.add(btnConectarBD);
        centerPanel.add(bdPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Sección: Información del Usuarios
        centerPanel.add(crearSeccionConfiguracion("Información del Usuario"));
        centerPanel.add(crearOpcionConfiguracion("Usuario Actual", session.usuarioActual.getNombre()));
        centerPanel.add(crearOpcionConfiguracion("Rol", session.usuarioActual.getRol()));
        centerPanel.add(Box.createVerticalStrut(30));

        // Botones de acción
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

        JButton btnGuardar = crearBotonAccion("GUARDAR", new Color(100, 180, 120));
        JButton btnCancelar = crearBotonAccion("CANCELAR", new Color(150, 150, 150));

        btnCancelar.addActionListener(e -> dispose());

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        centerPanel.add(buttonPanel);

        centerPanel.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        mainPanel.add(scroll, BorderLayout.CENTER);

        add(mainPanel);
    }

    /**
     * BLOQUE: Crear Sección de Configuración
     * Para: Agrupar opciones en secciones
     */
    private JLabel crearSeccionConfiguracion(String titulo) {
        JLabel label = new JLabel(titulo);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(70, 150, 220));
        return label;
    }

    /**
     * BLOQUE: Crear Opción de Configuración
     * Para: Mostrar configuraciones en pares clave-valor
     */
    private JPanel crearOpcionConfiguracion(String clave, String valor) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel keyLabel = new JLabel(clave);
        keyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        keyLabel.setForeground(new Color(200, 200, 220));

        JLabel valueLabel = new JLabel(valor);
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        valueLabel.setForeground(Color.WHITE);

        panel.add(keyLabel, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.EAST);

        return panel;
    }

    /**
     * BLOQUE: Crear Botón de Acción
     * Para: Botones de guardar, cancelar, etc
     */
    private JButton crearBotonAccion(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
}
