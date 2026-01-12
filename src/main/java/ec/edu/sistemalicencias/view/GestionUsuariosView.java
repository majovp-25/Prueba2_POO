package ec.edu.sistemalicencias.view;

import ec.edu.sistemalicencias.controller.UsuarioController;
import ec.edu.sistemalicencias.model.Usuario;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList; // Importante

public class GestionUsuariosView extends JFrame {

    private final UsuarioController controller = new UsuarioController();
    private String usuarioLogueadoUsername;
    private JTable tabla;
    private DefaultTableModel modelo;

    // Campos de cuenta
    private JTextField txtId;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cboRol;

    // Campos personales
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtCorreo;

    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JButton btnCerrar;
    private JButton btnImprimir;

    // --- IMPORTANTE: Lista para recordar los datos completos al dar clic en la tabla ---
    private List<Usuario> listaUsuariosActual = new ArrayList<>();

    public GestionUsuariosView(String usernameLogueado) {
        this.usuarioLogueadoUsername = usernameLogueado;

        setTitle("Gestión de Usuarios - Sesión de: " + usernameLogueado);
        setSize(900, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        cargarTabla();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.setBackground(new Color(245, 247, 250));

        // =========================
        // 1. TABLA (IZQUIERDA)
        // =========================
        modelo = new DefaultTableModel(new Object[]{"ID", "Username", "Rol"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista de Usuarios"));
        scroll.getViewport().setBackground(Color.WHITE);

        // =========================
        // 2. FORMULARIO (DERECHA)
        // =========================
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setOpaque(false);

        // Contenedor del formulario (usamos GridBag para control total)
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        // --- SECCIÓN 1: DATOS PERSONALES ---
        addSeparator(form, "Datos Personales", 0);

        c.gridy = 1; c.gridx = 0; form.add(new JLabel("Nombres:"), c);
        c.gridx = 1; 
        txtNombre = new JTextField(15);
        form.add(txtNombre, c);

        c.gridy = 2; c.gridx = 0; form.add(new JLabel("Apellidos:"), c);
        c.gridx = 1; 
        txtApellido = new JTextField(15);
        form.add(txtApellido, c);

        c.gridy = 3; c.gridx = 0; form.add(new JLabel("Teléfono:"), c);
        c.gridx = 1; 
        txtTelefono = new JTextField(15);
        form.add(txtTelefono, c);

        c.gridy = 4; c.gridx = 0; form.add(new JLabel("Correo:"), c);
        c.gridx = 1; 
        txtCorreo = new JTextField(15);
        form.add(txtCorreo, c);

        // --- SECCIÓN 2: CUENTA DE USUARIO ---
        addSeparator(form, "Datos de Cuenta", 5);

        c.gridy = 6; c.gridx = 0; form.add(new JLabel("ID:"), c);
        c.gridx = 1; 
        txtId = new JTextField();
        txtId.setEnabled(false);
        txtId.setBackground(new Color(240,240,240));
        form.add(txtId, c);

        c.gridy = 7; c.gridx = 0; form.add(new JLabel("Rol:"), c);
        c.gridx = 1; 
        cboRol = new JComboBox<>(new String[]{"Administrador", "Analista"});
        form.add(cboRol, c);

        c.gridy = 8; c.gridx = 0; form.add(new JLabel("Username:"), c);
        c.gridx = 1; 
        txtUsername = new JTextField(15);
        txtUsername.setToolTipText("Dejar vacío para generar automáticamente");
        form.add(txtUsername, c);

        c.gridy = 9; c.gridx = 0; form.add(new JLabel("Password:"), c);
        c.gridx = 1; 
        txtPassword = new JPasswordField(15);
        txtPassword.setToolTipText("Dejar vacío para contraseña por defecto (Sist.1234!)");
        form.add(txtPassword, c);
        
        // Mensaje de ayuda pequeño
        c.gridy = 10; c.gridx = 0; c.gridwidth = 2;
        JLabel lblInfo = new JLabel("<html><font color='gray' size='2'>* Si deja Username/Pass vacíos, se generan solos.</font></html>");
        form.add(lblInfo, c);


        // =========================
        // 3. BOTONES
        // =========================
        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 5, 5)); // Rejilla de botones
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(40, 167, 69)); 
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnRefrescar = new JButton("Limpiar");
        btnImprimir = new JButton("PDF");
        btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnImprimir);
        panelBotones.add(btnCerrar);

        //Colores de botones
        Color azulBorde = new Color(0, 102, 204); // Azul similar al que ya usas en títulos

        JButton[] botones = {btnGuardar, btnEditar, btnEliminar, btnRefrescar, btnImprimir, btnCerrar};

        for (JButton b : botones) {
            b.setBackground(UIManager.getColor("Button.background"));
            b.setForeground(UIManager.getColor("Button.foreground"));
            b.setOpaque(true);
            b.setContentAreaFilled(true);

            // Borde azul (1.5px aproximado con 2px)
            b.setBorder(BorderFactory.createLineBorder(azulBorde, 2, true));

            b.setFocusPainted(false);
        }




        // Agregamos el formulario al panel derecho (con BorderLayout.NORTH para que no se estire feo)
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setOpaque(false);
        formContainer.add(form, BorderLayout.NORTH);
        
        panelDerecho.add(formContainer, BorderLayout.CENTER);
        panelDerecho.add(panelBotones, BorderLayout.SOUTH);

        // Split Pane
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, panelDerecho);
        split.setResizeWeight(0.65); // 65% para la tabla
        split.setOpaque(false);
        split.setBorder(null);

        root.add(split, BorderLayout.CENTER);
        setContentPane(root);

        // Eventos
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccionEnFormulario();
        });

        btnGuardar.addActionListener(e -> crearUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnRefrescar.addActionListener(e -> { limpiarFormulario(); cargarTabla(); });
        btnImprimir.addActionListener(e -> generarReportePDF());
        btnCerrar.addActionListener(e -> dispose());
    }
    
    // Método auxiliar para títulos bonitos en el form
    private void addSeparator(JPanel panel, String text, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = y; c.gridwidth = 2; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 5, 0);
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(0, 102, 204));
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        panel.add(lbl, c);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        // 1. Guardamos la lista completa en memoria para poder usarla luego
        listaUsuariosActual = controller.listarUsuarios();
        
        for (Usuario u : listaUsuariosActual) {
            modelo.addRow(new Object[]{u.getId(), u.getUsername(), u.getRol()});
        }
    }

    private void cargarSeleccionEnFormulario() {
        int row = tabla.getSelectedRow();
        if (row < 0 || row >= listaUsuariosActual.size()) return;

        // 2. Sacamos el objeto completo de la lista
        Usuario u = listaUsuariosActual.get(row);

        txtId.setText(String.valueOf(u.getId()));
        txtUsername.setText(u.getUsername());
        cboRol.setSelectedItem(u.getRol());
        
        // Ahora sí se llenan los campos personales
        txtNombre.setText(u.getNombre());
        txtApellido.setText(u.getApellido());
        txtTelefono.setText(u.getTelefono());
        txtCorreo.setText(u.getEmail());
        
        txtPassword.setText(""); // Por seguridad, limpio
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cboRol.setSelectedIndex(0);
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        tabla.clearSelection();
    }

    // --- LÓGICA DE GENERACIÓN AUTOMÁTICA ---
    private void generarCredencialesSiVacio() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();

        // Solo generamos si hay nombre y apellido
        if (!nombre.isEmpty() && !apellido.isEmpty()) {
            
            // Si el username está vacío, creamos uno: PrimeraLetraNombre + Apellido
            if (txtUsername.getText().trim().isEmpty()) {
                // Quitamos espacios del apellido por si acaso
                String apellidoLimpio = apellido.split(" ")[0]; 
                String autoUser = (nombre.charAt(0) + apellidoLimpio).toLowerCase();
                txtUsername.setText(autoUser);
            }

            // Si el password está vacío, ponemos el default
            String passActual = new String(txtPassword.getPassword());
            if (passActual.isEmpty()) {
                txtPassword.setText("Sist.1234!"); // Contraseña por defecto
            }
        }
    }

    private void crearUsuario() {
        // 1. Validar campos personales primero
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre, Apellido, Teléfono y Correo son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "El correo no es válido.\nDebe tener el formato: usuario@dominio.com", "Validación de Correo", JOptionPane.WARNING_MESSAGE);
            return; 
        }
        
        // 2. Intentar autogenerar credenciales si están vacías
        generarCredencialesSiVacio();

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String rol = (String) cboRol.getSelectedItem();

        // 3. Validar credenciales
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se pudo generar Username/Password. Ingréselos manualmente.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }


        try {
            Usuario creado = controller.crearUsuario(username, password, rol, nombre, apellido, telefono, correo, this.usuarioLogueadoUsername);
            if (creado != null) {
                JOptionPane.showMessageDialog(this, "✅ Usuario creado con éxito.\nUsername: " + username + "\nPass: " + password, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarUsuario() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = Integer.parseInt(txtId.getText().trim());
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String rol = (String) cboRol.getSelectedItem();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Para editar, ingrese la contraseña nueva o la actual.", "Seguridad", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean ok = controller.actualizarUsuario(id, username, password, rol, nombre, apellido, telefono, correo);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado.");
                cargarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminarUsuario() {
        if (txtId.getText().trim().isEmpty()) return;
        int id = Integer.parseInt(txtId.getText());
        
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar usuario ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (controller.eliminarUsuario(id)) {
                JOptionPane.showMessageDialog(this, "Usuario Eliminado Exitosamente.");
                cargarTabla();
                limpiarFormulario();
            }
        }
    }

    private void generarReportePDF() {
        try {
            List<Usuario> listaCompleta = controller.listarUsuarios();
            if (listaCompleta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay datos para imprimir.");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Reporte");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF (*.pdf)", "pdf"));
            fileChooser.setSelectedFile(new File("Reporte_Usuarios_" + System.currentTimeMillis() + ".pdf"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();
                String ruta = archivo.getAbsolutePath();
                if (!ruta.endsWith(".pdf")) ruta += ".pdf";

                ec.edu.sistemalicencias.util.GeneradorReporteUsuarios.generarPDF(listaCompleta, ruta);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    // Bloque setupUI de IntelliJ (No tocar)
    private void $$$setupUI$$$() {}
}