package ec.edu.sistemalicencias.view;

import com.intellij.uiDesigner.core.GridLayoutManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    private final JTextField txtUsername = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(18);
    private final JButton btnIngresar = new JButton("Ingresar");
    private final JButton btnSalir = new JButton("Salir");

    public LoginView() {
        setTitle("Login - Sistema Licencias");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Inicio de sesión");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));

        // Estilos y Colores
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

        txtUsername.setPreferredSize(new Dimension(260, 34));
        txtPassword.setPreferredSize(new Dimension(260, 34));
        btnIngresar.setPreferredSize(new Dimension(110, 34));
        btnSalir.setPreferredSize(new Dimension(90, 34));

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(lblTitulo, c);

        c.gridwidth = 1; c.gridx = 0; c.gridy = 1;
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setForeground(text);
        panel.add(lblUser, c);

        c.gridx = 1; c.gridy = 1;
        panel.add(txtUsername, c);

        c.gridx = 0; c.gridy = 2;
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setForeground(text);
        panel.add(lblPass, c);

        c.gridx = 1; c.gridy = 2;
        panel.add(txtPassword, c);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnSalir);
        panelBotones.add(btnIngresar);
        panelBotones.setOpaque(false);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        panel.add(panelBotones, c);

        // Evento Salir
        btnSalir.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "¿Desea salir?", "Salir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        setContentPane(panel);
        getRootPane().setDefaultButton(btnIngresar);
        pack();
        setLocationRelativeTo(null);
    }

    public String getUsername() { return txtUsername.getText().trim(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public void addIngresarListener(ActionListener listener) { btnIngresar.addActionListener(listener); }
    public void showError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE); }
    public void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtUsername.requestFocus();
    }

    // --- NUEVO METODO PARA PEDIR CAMBIO DE CONTRASEÑA ---
    public String solicitarNuevaContrasena() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("⚠ Es su primer ingreso (o usa clave por defecto)."));
        panel.add(new JLabel("Por seguridad, ingrese una nueva contraseña:"));
        
        JPasswordField pf = new JPasswordField();
        int ok = JOptionPane.showConfirmDialog(this, new Object[]{panel, pf}, "Cambio de Contraseña Obligatorio", JOptionPane.OK_CANCEL_OPTION);
        
        if (ok == JOptionPane.OK_OPTION) {
            return new String(pf.getPassword());
        }
        return null; // Canceló
    }

    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }
    { $$$setupUI$$$(); }
}