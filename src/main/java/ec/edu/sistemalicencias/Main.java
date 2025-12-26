package ec.edu.sistemalicencias;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.view.MainView;

import javax.swing.*;

/**
 * Clase principal del Sistema de Licencias de Conducir del Ecuador.
 * Punto de entrada de la aplicación.
 *
 * @author Sistema Licencias Ecuador
 * @version 1.0
 */
public class Main {

/**
     * Método principal MODIFICADO PARA PRUEBAS
     */
    public static void main(String[] args) {
        // 1. Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
        }

        // 2. Instanciamos la configuración
        DatabaseConfig dbConfig = DatabaseConfig.getInstance();

        // ==========================================
        // 🧪 ZONA DE PRUEBAS DEL LOGIN (CORREGIDA)
        // ==========================================
        System.out.println("\n🛠️ --- INICIANDO PRUEBA DE BACKEND (RAILWAY) ---");

        // CORRECCIÓN AQUÍ: Quitamos el ".model" de la ruta del DAO
        ec.edu.sistemalicencias.dao.UsuarioDAO dao = new ec.edu.sistemalicencias.dao.UsuarioDAO();

        // TEST 1: Probamos con el ADMIN
        System.out.println("👉 Intentando login con 'admin'...");
        
        // El Usuario sí está en model, así que este se queda igual
        ec.edu.sistemalicencias.model.Usuario u1 = dao.login("admin", "1234"); 

        if (u1 != null) {
            System.out.println("✅ ¡ÉXITO! Usuario encontrado: " + u1.getUsername());
            System.out.println("🔹 Rol detectado: " + u1.getRol());
        } else {
            System.out.println("❌ ERROR: No se pudo conectar o usuario incorrecto.");
        }

        // TEST 2: Probamos con datos FALSOS
        System.out.println("\n👉 Intentando login con 'hacker'...");
        ec.edu.sistemalicencias.model.Usuario u2 = dao.login("hacker", "nadie");

        if (u2 == null) {
            System.out.println("✅ ¡CORRECTO! El sistema rechazó al intruso.");
        } else {
            System.out.println("⚠️ ALERTA: El sistema dejó pasar a un usuario falso.");
        }
        System.out.println("----------------------------------------------\n");

        // ==========================================
        // FIN DE PRUEBAS
        // ==========================================
    }
}