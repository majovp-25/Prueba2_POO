package ec.edu.sistemalicencias.view;

import ec.edu.sistemalicencias.controller.UsuarioController;
import ec.edu.sistemalicencias.model.Usuario;

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

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JButton btnCerrar;

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

        // ===== Tabla =====
        modelo = new DefaultTableModel(new Object[]{"ID", "Username", "Rol"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Usuarios registrados"));

        // ===== Formulario =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Formulario"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.anchor = GridBagConstraints.NORTHWEST; // pega todo arriba/izquierda


        txtId = new JTextField(6);
        txtId.setEnabled(false);

        txtUsername = new JTextField(14);
        txtPassword = new JPasswordField(14);
        cboRol = new JComboBox<>(new String[]{"Administrador", "Analista"});

        c.gridx = 0; c.gridy = 0; form.add(new JLabel("ID:"), c);
        c.gridx = 1; c.gridy = 0; form.add(txtId, c);

        c.gridx = 0; c.gridy = 1; form.add(new JLabel("Username:"), c);
        c.gridx = 1; c.gridy = 1; form.add(txtUsername, c);

        c.gridx = 0; c.gridy = 2; form.add(new JLabel("Password:"), c);
        c.gridx = 1; c.gridy = 2; form.add(txtPassword, c);

        c.gridx = 0; c.gridy = 3; form.add(new JLabel("Rol:"), c);
        c.gridx = 1; c.gridy = 3; form.add(cboRol, c);

        // ===== Botones =====
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnNuevo = new JButton("Nuevo");
        btnGuardar = new JButton("Guardar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnRefrescar = new JButton("Refrescar");
        btnCerrar = new JButton("Cerrar");

        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnRefrescar);
        botones.add(btnCerrar);

        // ===== Layout =====
        JPanel derecha = new JPanel(new BorderLayout(10, 10));

        // envolver el formulario para que quede arriba
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.add(form, BorderLayout.NORTH);

        derecha.add(formWrapper, BorderLayout.CENTER);
        derecha.add(botones, BorderLayout.SOUTH);


        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, derecha);
        split.setResizeWeight(0.70);

        root.add(split, BorderLayout.CENTER);
        setContentPane(root);

        // ===== Eventos =====
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccionEnFormulario();
        });

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> crearUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnRefrescar.addActionListener(e -> cargarTabla());
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
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cboRol.setSelectedIndex(0);
        tabla.clearSelection();
        txtUsername.requestFocus();
    }

    private void crearUsuario() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String rol = (String) cboRol.getSelectedItem();

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
            Usuario creado = controller.crearUsuario(username, password, rol);
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
            boolean ok = controller.actualizarUsuario(id, username, password, rol);
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

    // ===== Validación de password (MAYÚSCULA + NÚMERO + ESPECIAL) =====
    private boolean passwordValida(String password) {
        if (password == null) return false;

        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneNumero = password.matches(".*\\d.*");
        boolean tieneEspecial = password.matches(".*[^A-Za-z0-9].*");

        return tieneMayuscula && tieneNumero && tieneEspecial;
    }
}
