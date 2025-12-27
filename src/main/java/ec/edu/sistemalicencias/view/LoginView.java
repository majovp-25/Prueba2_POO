package ec.edu.sistemalicencias.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    private final JTextField txtUsername = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(18);
    private final JButton btnIngresar = new JButton("Ingresar");

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

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(lblTitulo, c);

        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("Usuario:"), c);
        c.gridx = 1; c.gridy = 1;
        panel.add(txtUsername, c);

        c.gridx = 0; c.gridy = 2;
        panel.add(new JLabel("Contraseña:"), c);
        c.gridx = 1; c.gridy = 2;
        panel.add(txtPassword, c);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        panel.add(btnIngresar, c);

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
}
