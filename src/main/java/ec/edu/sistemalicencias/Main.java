package ec.edu.sistemalicencias;

import com.formdev.flatlaf.FlatIntelliJLaf; 
import ec.edu.sistemalicencias.model.Usuario;
import ec.edu.sistemalicencias.view.GestionUsuariosView;
import ec.edu.sistemalicencias.view.LoginView;
import ec.edu.sistemalicencias.view.MainView; 

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // 1. CONFIGURAR DISEÑO (FLATLAF)
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 12);
            UIManager.put("TextComponent.arc", 12);
        } catch (Exception ex) {
            System.err.println("Falló la carga del tema. Usando defecto.");
        }

        // 2. INICIAR APLICACIÓN
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }

    // Navegación
    public static void abrirMenuPrincipal(Usuario usuarioLogueado) {
        MainView menu = new MainView(usuarioLogueado); 
        menu.setVisible(true);
    }
    
    // MÉTODO QUE FALTABA Y DABA ERROR EN MAINVIEW
    public static void mostrarLogin() {
        new LoginView().setVisible(true);
    }
    
    // Auxiliar para pruebas directas
    public static void abrirGestionUsuarios(Usuario u) {
        new GestionUsuariosView(u.getUsername()).setVisible(true);
    }
}