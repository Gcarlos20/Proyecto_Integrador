package vista;

import controller.LoginController;
import model.Usuario;
import util.session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginVista extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> cbRol;
    private JButton btnLogin;

    public LoginVista() {
        setTitle("Sistema de Inventario - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setMinimumSize(new Dimension(400, 500));

        // Panel principal con gradiente
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(20, 30, 48);
                Color color2 = new Color(36, 59, 97);
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Panel superior con icono/título
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(30, 20, 20, 20));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("INICIAR SESIÓN");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Sistema de Gestión de Inventario");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(180, 180, 200));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titulo);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitulo);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel central con formulario
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(20, 50, 40, 50));

        // Panel del formulario con fondo
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(40, 50, 75));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(30, 25, 30, 25));

        // Usuario
        JLabel lblUser = new JLabel("Usuario");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUser.setForeground(new Color(200, 200, 220));
        formPanel.add(lblUser);

        txtUser = new JTextField();
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUser.setPreferredSize(new Dimension(0, 38));
        txtUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 120, 160), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtUser.setBackground(new Color(50, 65, 95));
        txtUser.setForeground(Color.WHITE);
        formPanel.add(txtUser);
        formPanel.add(Box.createVerticalStrut(15));

        // Contraseña
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPass.setForeground(new Color(200, 200, 220));
        formPanel.add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPass.setPreferredSize(new Dimension(0, 38));
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 120, 160), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtPass.setBackground(new Color(50, 65, 95));
        txtPass.setForeground(Color.WHITE);
        formPanel.add(txtPass);
        formPanel.add(Box.createVerticalStrut(15));

        // Rol
        JLabel lblRol = new JLabel("Rol");
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRol.setForeground(new Color(200, 200, 220));
        formPanel.add(lblRol);

        cbRol = new JComboBox<>(new String[]{"admin", "usuario", "consultor"});
        cbRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbRol.setPreferredSize(new Dimension(0, 38));
        cbRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        cbRol.setBackground(new Color(50, 65, 95));
        cbRol.setForeground(Color.WHITE);
        formPanel.add(cbRol);

        centerPanel.add(formPanel);

        // Botón Login
        btnLogin = new JButton("INGRESAR");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(0, 45));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnLogin.setBackground(new Color(70, 150, 220));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(90, 170, 240));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(70, 150, 220));
            }
        });
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(btnLogin);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Panel inferior
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        JLabel footerLabel = new JLabel("© 2026 Sistema de Inventario");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(120, 120, 140));
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Lógica del Login
        LoginController controller = new LoginController();

        btnLogin.addActionListener(e -> {
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());
            String rolSeleccionado = (String) cbRol.getSelectedItem();

            Usuario u = controller.login(user, pass, rolSeleccionado);

            if (u != null && u.getRol().equals(rolSeleccionado)) {
                session.usuarioActual = u;

                JOptionPane.showMessageDialog(this, "Bienvenido " + u.getRol(), 
                    "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);

                new DeshboardVista().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales o rol incorrecto", 
                    "Error de Login", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}