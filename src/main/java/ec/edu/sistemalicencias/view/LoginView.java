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

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Inicio de sesión");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));

        // =========================
        // DISEÑO (SOLO VISUAL)
        // =========================
        Color bg = new Color(245, 247, 250);
        Color cardBorder = new Color(225, 230, 236);
        Color inputBorder = new Color(210, 216, 224);
        Color text = new Color(25, 30, 35);
        Color red = new Color(220, 53, 69);

        panel.setBackground(bg);

        // Mantengo tu borde original y le agrego un borde tipo “tarjeta” SIN BORRAR tu línea
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

        // Bordes con padding interno (más bonito)
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(inputBorder, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(inputBorder, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        // Botón Ingresar (rojo sólido)
        btnIngresar.setBackground(red);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setOpaque(true);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Botón Salir (blanco con borde rojo)
        btnSalir.setBackground(Color.WHITE);
        btnSalir.setForeground(red);
        btnSalir.setOpaque(true);
        btnSalir.setBorder(BorderFactory.createLineBorder(red, 1, true));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Tipografía de botones
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Un poco más de tamaño visual (solo UI)
        txtUsername.setPreferredSize(new Dimension(260, 34));
        txtPassword.setPreferredSize(new Dimension(260, 34));
        btnIngresar.setPreferredSize(new Dimension(110, 34));
        btnSalir.setPreferredSize(new Dimension(90, 34));
        // =========================
        // FIN DISEÑO
        // =========================

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(lblTitulo, c);

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Usuario:"), c);

        // DISEÑO: color de labels (sin cambiar lógica)
        ((JLabel) panel.getComponent(panel.getComponentCount() - 1)).setForeground(text);

        c.gridx = 1;
        c.gridy = 1;
        panel.add(txtUsername, c);

        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("Contraseña:"), c);

        // DISEÑO: color de labels (sin cambiar lógica)
        ((JLabel) panel.getComponent(panel.getComponentCount() - 1)).setForeground(text);

        c.gridx = 1;
        c.gridy = 2;
        panel.add(txtPassword, c);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnSalir);
        panelBotones.add(btnIngresar);

        // DISEÑO: fondo transparente para que se vea el panel bonito
        panelBotones.setOpaque(false);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.EAST;   // para alinearlo a la derecha
        c.fill = GridBagConstraints.NONE;     // para que no estire el panel
        panel.add(panelBotones, c);

        // ==========
        // TU LÓGICA (NO TOCADA)
        // ==========
        btnSalir.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea salir del sistema?",
                    "Salir",
                    JOptionPane.YES_NO_OPTION
            );
            if (op == JOptionPane.YES_OPTION) System.exit(0);
        });

        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;

        setContentPane(panel);
        getRootPane().setDefaultButton(btnIngresar);
        pack();
        setLocationRelativeTo(null);
    }

    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public void addIngresarListener(ActionListener listener) {
        btnIngresar.addActionListener(listener);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtUsername.requestFocus();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }
}
