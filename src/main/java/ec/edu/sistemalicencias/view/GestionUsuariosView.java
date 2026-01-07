package ec.edu.sistemalicencias.view;

import com.intellij.uiDesigner.core.GridLayoutManager;
import ec.edu.sistemalicencias.controller.UsuarioController;
import ec.edu.sistemalicencias.model.Usuario;
import ec.edu.sistemalicencias.util.GeneradorReporteUsuarios;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionUsuariosView extends JFrame {

    private final UsuarioController controller = new UsuarioController();

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtId;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cboRol;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtCorreo;


    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JButton btnCerrar;
    private JButton btnImprimir;


    public GestionUsuariosView() {
        setTitle("Gestión de Usuarios");
        setSize(820, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        cargarTabla();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        // =========================
        // DISEÑO (SOLO VISUAL)
        // =========================
        Color bg = new Color(245, 247, 250);
        Color cardBorder = new Color(225, 230, 236);
        Color inputBorder = new Color(210, 216, 224);
        Color text = new Color(25, 30, 35);
        Color red = new Color(220, 53, 69);
        Color grayBtn = new Color(108, 117, 125);

        root.setBackground(bg);
        // =========================

        // ===== Tabla =====
        modelo = new DefaultTableModel(new Object[]{"ID", "Username", "Rol"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Usuarios registrados"));

        // =========================
        // DISEÑO (SOLO VISUAL): tabla más presentable
        // =========================
        tabla.setRowHeight(26);
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionBackground(new Color(220, 53, 69, 35));
        tabla.setSelectionForeground(text);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        // =========================

        // =========================
        // DISEÑO (SOLO VISUAL): “tarjeta” para el scroll
        // =========================
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorder, 1, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        // =========================

        // ===== Formulario =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Formulario"));

        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorder, 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.anchor = GridBagConstraints.NORTHWEST; // pega todo arriba/izquierda

        txtId = new JTextField(6);
        txtId.setEnabled(false);

        txtUsername = new JTextField(14);
        txtPassword = new JPasswordField(14);
        cboRol = new JComboBox<>(new String[]{"Administrador", "Analista"});

        Font inputFont = new Font("Segoe UI", Font.PLAIN, 12);

        txtId.setFont(inputFont);
        txtUsername.setFont(inputFont);
        txtPassword.setFont(inputFont);
        cboRol.setFont(inputFont);

        txtId.setBackground(new Color(248, 249, 250));
        txtUsername.setBackground(Color.WHITE);
        txtPassword.setBackground(Color.WHITE);

        txtId.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(inputBorder, 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(inputBorder, 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(inputBorder, 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        cboRol.setBorder(BorderFactory.createLineBorder(inputBorder, 1, true));

        c.gridx = 0;
        c.gridy = 0;
        JLabel lblId = new JLabel("ID:");
        lblId.setForeground(text);
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        form.add(lblId, c);

        c.gridx = 1;
        c.gridy = 0;
        form.add(txtId, c);

        c.gridx = 0;
        c.gridy = 1;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(text);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        form.add(lblUser, c);

        c.gridx = 1;
        c.gridy = 1;
        form.add(txtUsername, c);

        c.gridx = 0;
        c.gridy = 2;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(text);
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        form.add(lblPass, c);

        c.gridx = 1;
        c.gridy = 2;
        form.add(txtPassword, c);

        c.gridx = 0;
        c.gridy = 3;
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setForeground(text);
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        form.add(lblRol, c);

        c.gridx = 1;
        c.gridy = 3;
        form.add(cboRol, c);

        // ===== Botones =====
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnGuardar = new JButton("Guardar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnRefrescar = new JButton("Refrescar");
        btnImprimir = new JButton("PDF");
        btnCerrar = new JButton("Cerrar");

        botones.add(btnGuardar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnRefrescar);
        botones.add(btnImprimir);
        botones.add(btnCerrar);

        // =========================
        // DISEÑO (SOLO VISUAL): estilo botones
        // =========================
        botones.setOpaque(false);
        botones.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 10));  // Mantener el espaciado entre los botones

        Font btnFont = new Font("Segoe UI", Font.BOLD, 12);

        // Botones secundarios
        JButton[] secundarios = {btnEditar, btnEliminar, btnRefrescar, btnImprimir};
        for (JButton b : secundarios) {
            b.setFont(btnFont);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setBackground(Color.WHITE);
            b.setForeground(text);
            b.setOpaque(true);
            b.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            b.setPreferredSize(new Dimension(80, 30));
        }

        // Guardar
        btnGuardar.setFont(btnFont);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setBackground(red);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setOpaque(true);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setPreferredSize(new Dimension(80, 30));

        // Cerrar
        btnCerrar.setFont(btnFont);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setBackground(Color.WHITE);
        btnCerrar.setForeground(red);
        btnCerrar.setOpaque(true);
        btnCerrar.setBorder(BorderFactory.createLineBorder(red, 2, true));
        btnCerrar.setPreferredSize(new Dimension(80, 30));



        // =========================
        // Layout de la interfaz
        // =========================
        JPanel derecha = new JPanel(new BorderLayout(10, 10));
        derecha.setBackground(bg);

        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.add(form, BorderLayout.NORTH);
        formWrapper.setOpaque(false);
        derecha.add(formWrapper, BorderLayout.CENTER);
        derecha.add(botones, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, derecha);
        split.setResizeWeight(0.70);
        split.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        split.setBackground(bg);

        root.add(split, BorderLayout.CENTER);
        setContentPane(root);

        // ===== Eventos =====
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccionEnFormulario();
        });

        btnGuardar.addActionListener(e -> crearUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnRefrescar.addActionListener(e -> cargarTabla());
        btnImprimir.addActionListener(e -> generarReportePDF());
        btnCerrar.addActionListener(e -> dispose());
    }


    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Usuario> usuarios = controller.listarUsuarios();
        for (Usuario u : usuarios) {
            modelo.addRow(new Object[]{u.getId(), u.getUsername(), u.getRol()});
        }
    }

    private void cargarSeleccionEnFormulario() {
        int row = tabla.getSelectedRow();
        if (row < 0) return;

        txtId.setText(modelo.getValueAt(row, 0).toString());
        txtUsername.setText(modelo.getValueAt(row, 1).toString());

        // Por seguridad no mostramos password real en tabla; se escribe nuevamente si se quiere cambiar.
        txtPassword.setText("");

        cboRol.setSelectedItem(modelo.getValueAt(row, 2).toString());

        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");

    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cboRol.setSelectedIndex(0);
        tabla.clearSelection();
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");

        txtUsername.requestFocus();
    }

    private void crearUsuario() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String rol = (String) cboRol.getSelectedItem();
        // ===== NUEVOS CAMPOS (lectura) =====
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

// ===== VALIDACIONES NUEVOS CAMPOS (manejo tipo excepción/validación) =====
        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nombre, Apellido, Teléfono y Correo son obligatorios.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!nombreValido(nombre)) {
            JOptionPane.showMessageDialog(
                    this,
                    "El Nombre solo debe contener letras y espacios (mínimo 2 caracteres).",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!apellidoValido(apellido)) {
            JOptionPane.showMessageDialog(
                    this,
                    "El Apellido solo debe contener letras y espacios (mínimo 2 caracteres).",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!telefonoValido(telefono)) {
            JOptionPane.showMessageDialog(
                    this,
                    "El Teléfono debe contener solo números (7 a 15 dígitos).",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!correoValido(correo)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un correo válido (ej: usuario@dominio.com).",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }


        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username y Password son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!passwordValida(password)) {
            JOptionPane.showMessageDialog(
                    this,
                    "La contraseña debe tener al menos:\n- 1 mayúscula\n- 1 número\n- 1 carácter especial",
                    "Validación de contraseña",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            Usuario creado = controller.crearUsuario(username, password, rol, nombre, apellido, telefono, correo);
            if (creado != null) {
                JOptionPane.showMessageDialog(this, "Usuario creado con ID: " + creado.getId(), "OK", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para editar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(txtId.getText().trim());
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String rol = (String) cboRol.getSelectedItem();
        // ===== NUEVOS CAMPOS (lectura) =====
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

// ===== VALIDACIONES NUEVOS CAMPOS =====
        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nombre, Apellido, Teléfono y Correo son obligatorios.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!nombreValido(nombre)) {
            JOptionPane.showMessageDialog(
                    this,
                    "El Nombre solo debe contener letras y espacios (mínimo 2 caracteres).",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!apellidoValido(apellido)) {
            JOptionPane.showMessageDialog(
                    this,
                    "El Apellido solo debe contener letras y espacios (mínimo 2 caracteres).",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!telefonoValido(telefono)) {
            JOptionPane.showMessageDialog(
                    this,
                    "El Teléfono debe contener solo números (7 a 15 dígitos).",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!correoValido(correo)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un correo válido (ej: usuario@dominio.com).",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }


        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Aquí sigues con la lógica actual: exigir password para editar.
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un password para editar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!passwordValida(password)) {
            JOptionPane.showMessageDialog(
                    this,
                    "La contraseña debe tener al menos:\n- 1 mayúscula\n- 1 número\n- 1 carácter especial",
                    "Validación de contraseña",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            boolean ok = controller.actualizarUsuario(id, username, password, rol, nombre, apellido, telefono, correo);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado.", "OK", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(txtId.getText().trim());

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea eliminar el usuario ID " + id + "?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            boolean ok = controller.eliminarUsuario(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado.", "OK", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Constructor Generador de reporte de Usuarios
    private void generarReportePDF() {
        try {
            // 1. Validar que haya datos
            List<Usuario> listaCompleta = controller.listarUsuarios();
            if (listaCompleta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay datos para imprimir.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Configurar el selector de archivos (JFileChooser)
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Reporte de Usuarios");

            // Filtro para que solo muestre archivos PDF
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf");
            fileChooser.setFileFilter(filter);

            // Sugerir un nombre por defecto
            String nombreSugerido = "Reporte_Usuarios_" + System.currentTimeMillis() + ".pdf";
            fileChooser.setSelectedFile(new File(nombreSugerido));

            // 3. Mostrar la ventana "Guardar Como"
            int seleccion = fileChooser.showSaveDialog(this);

            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File archivoSeleccionado = fileChooser.getSelectedFile();
                String ruta = archivoSeleccionado.getAbsolutePath();

                // Asegurar que termine en .pdf si el usuario no lo escribió
                if (!ruta.toLowerCase().endsWith(".pdf")) {
                    ruta += ".pdf";
                }

                // 4. Llamar al generador enviándole la ruta elegida
                ec.edu.sistemalicencias.util.GeneradorReporteUsuarios.generarPDF(listaCompleta, ruta);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ===== Validación de password (MAYÚSCULA + NÚMERO + ESPECIAL) =====
    private boolean passwordValida(String password) {
        if (password == null) return false;

        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneNumero = password.matches(".*\\d.*");
        boolean tieneEspecial = password.matches(".*[^A-Za-z0-9].*");

        return tieneMayuscula && tieneNumero && tieneEspecial;
    }
    // ===== Validaciones nuevos campos =====
    private boolean nombreValido(String nombre) {
        if (nombre == null) return false;
        String n = nombre.trim();
        if (n.length() < 2) return false;
        return n.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");
    }

    private boolean apellidoValido(String apellido) {
        if (apellido == null) return false;
        String a = apellido.trim();
        if (a.length() < 2) return false;
        return a.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");
    }

    private boolean telefonoValido(String telefono) {
        if (telefono == null) return false;
        String t = telefono.trim();
        // 7 a 15 dígitos, solo números
        return t.matches("^\\d{7,15}$");
    }

    private boolean correoValido(String correo) {
        if (correo == null) return false;
        String c = correo.trim();
        // Email básico (suficiente para UI)
        return c.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
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
