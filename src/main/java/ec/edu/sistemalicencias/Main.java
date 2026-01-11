package ec.edu.sistemalicencias;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.Usuario;
import ec.edu.sistemalicencias.view.LoginView;
import ec.edu.sistemalicencias.view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
        }

        DatabaseConfig.getInstance();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mostrarLogin();
            }
        });
    }

    // IMPORTANTE: esto lo llamas al inicio y también al cerrar sesión
    public static void mostrarLogin() {

        final UsuarioDAO dao = new UsuarioDAO();
        final LoginView loginView = new LoginView();
        loginView.setVisible(true);

        loginView.addIngresarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = loginView.getUsername();
                String password = loginView.getPassword();

                Usuario usuario = dao.login(username, password);

                if (usuario == null) {
                    loginView.showError("Credenciales incorrectas o error de conexión.");
                    loginView.clearFields();
                    return;
                }
                // =======================================================
                // NUEVA LÓGICA: Verificar si usa la contraseña por defecto
                // =======================================================
                if (password.equals("Sist.1234!")) {
                    
                    // 1. Pedimos la nueva contraseña usando el método que creamos en LoginView
                    String nuevaPass = loginView.solicitarNuevaContrasena();

                    // 2. Si cancela o lo deja vacío, no lo dejamos entrar
                    if (nuevaPass == null || nuevaPass.trim().isEmpty()) {
                        loginView.showError("Debe cambiar la contraseña para poder ingresar.");
                        return;
                    }
                    boolean tieneMayuscula = nuevaPass.matches(".*[A-Z].*");
                    boolean tieneNumero = nuevaPass.matches(".*\\d.*");
                    boolean tieneEspecial = nuevaPass.matches(".*[^A-Za-z0-9].*");

                    if (!tieneMayuscula || !tieneNumero || !tieneEspecial) {
                        loginView.showError("Contraseña insegura.\n\nDebe contener al menos:\n- Una letra MAYÚSCULA\n- Un NÚMERO\n- Un carácter ESPECIAL (.,-!@#$)\n\nInténtelo de nuevo.");
                        return;
                    }
                    boolean actualizo = dao.actualizarPassword(usuario.getId(), nuevaPass);
                    if (!actualizo) {
                        loginView.showError("Error al actualizar la contraseña en la base de datos.");
                        return;
                    }
                    JOptionPane.showMessageDialog(loginView, " Contraseña actualizada correctamente.\nBienvenido al sistema.");
                }
                // =======================================================
                // FIN NUEVA LÓGICA
                // =======================================================

                String rol = usuario.getRol();

                MainView mainView = new MainView(usuario);
                mainView.setVisible(true);
                loginView.dispose();
            }
        });
    }
}