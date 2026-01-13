package ec.edu.sistemalicencias.view;

import ec.edu.sistemalicencias.controller.UsuarioController;
import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    private final UsuarioController controller;

    // Componentes Visuales
    private final JTextField txtUsername = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(18);
    private final JButton btnIngresar = new JButton("Ingresar");
    private final JButton btnSalir = new JButton("Salir");
    private final JCheckBox chkVer = new JCheckBox("Ver");

    public LoginView() {
        controller = new UsuarioController(); 
        setTitle("Login - Sistema Licencias");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initUI();
        pack(); 
        setLocationRelativeTo(null); 
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Inicio de sesión");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));

        Color bg = new Color(245, 247, 250);
        Color cardBorder = new Color(225, 230, 236);
        Color inputBorder = new Color(210, 216, 224);
        Color text = new Color(25, 30, 35);
        Color red = new Color(220, 53, 69);

        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorder, 1, true),
                panel.getBorder()
        ));

        lblTitulo.setForeground(text);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        Font f = new Font("Segoe UI", Font.PLAIN, 13);
        txtUsername.setFont(f);
        txtPassword.setFont(f);
        txtUsername.setBackground(Color.WHITE);
        txtPassword.setBackground(Color.WHITE);

        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(inputBorder, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(inputBorder, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        btnIngresar.setBackground(red);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setOpaque(true);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnSalir.setBackground(Color.WHITE);
        btnSalir.setForeground(red);
        btnSalir.setOpaque(true);
        btnSalir.setBorder(BorderFactory.createLineBorder(red, 1, true));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        chkVer.setBackground(bg);
        chkVer.setForeground(text);
        chkVer.setFocusPainted(false);
        chkVer.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        txtUsername.setPreferredSize(new Dimension(260, 34));
        txtPassword.setPreferredSize(new Dimension(260, 34));
        btnIngresar.setPreferredSize(new Dimension(110, 34));
        btnSalir.setPreferredSize(new Dimension(90, 34));

        c.gridx = 0; c.gridy = 0; c.gridwidth = 3; 
        panel.add(lblTitulo, c);

        c.gridwidth = 1; c.gridx = 0; c.gridy = 1;
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setForeground(text);
        panel.add(lblUser, c);

        c.gridx = 1; c.gridy = 1; c.gridwidth = 2;
        panel.add(txtUsername, c);

        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 2;
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setForeground(text);
        panel.add(lblPass, c);

        c.gridx = 1; c.gridy = 2;
        panel.add(txtPassword, c);
        
        c.gridx = 2; c.gridy = 2; 
        c.weightx = 0; 
        panel.add(chkVer, c);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnSalir);
        panelBotones.add(btnIngresar);
        panelBotones.setOpaque(false);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 3;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        panel.add(panelBotones, c);

        chkVer.addActionListener(e -> {
            if (chkVer.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });

        btnSalir.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "¿Desea salir?", "Salir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        btnIngresar.addActionListener(e -> login());

        setContentPane(panel);
        getRootPane().setDefaultButton(btnIngresar);
    }

    private void login() {
        String u = txtUsername.getText().trim();
        String p = new String(txtPassword.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese sus credenciales.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = controller.login(u, p);

            if (usuario != null) {
                // A. VALIDACIÓN DE SEGURIDAD (Cambio de clave obligatorio)
                if (p.equals("Sist.1234!")) {
                    JOptionPane.showMessageDialog(this, "⚠️ Por seguridad, debe cambiar su contraseña inicial.", "Cambio Requerido", JOptionPane.WARNING_MESSAGE);
                    String nuevaPass = JOptionPane.showInputDialog(this, "Ingrese su NUEVA contraseña:");

                    if (nuevaPass != null && !nuevaPass.trim().isEmpty()) {
                        if (nuevaPass.equals("Sist.1234!")) {
                            JOptionPane.showMessageDialog(this, "No puede usar la misma contraseña por defecto.");
                            return;
                        }
                        new UsuarioDAO().actualizarPassword(usuario.getId(), nuevaPass.trim());
                        JOptionPane.showMessageDialog(this, "✅ Contraseña actualizada. Ingrese nuevamente.");
                        txtPassword.setText("");
                        return; 
                    } else {
                        return; 
                    }
                }

                // B. REDIRECCIÓN SEGÚN ROL
                this.dispose(); 

                if (usuario.getRol().equalsIgnoreCase("Administrador")) {
                    new GestionUsuariosView(usuario.getUsername()).setVisible(true);
                } else if (usuario.getRol().equalsIgnoreCase("Analista")) {
                    new MainView(usuario).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Rol no reconocido: " + usuario.getRol());
                }

            } else {
                // Si retorna null es credencial incorrecta
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (RuntimeException re) {
            // AQUÍ CAPTURAMOS TU MENSAJE PERSONALIZADO "Usuario Inactivo"
            JOptionPane.showMessageDialog(this, re.getMessage(), "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error del sistema: " + e.getMessage());
        }
    }
}