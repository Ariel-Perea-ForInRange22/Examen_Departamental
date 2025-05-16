package com.escuela.vista;

import com.escuela.modelo.Materia;
import com.escuela.modelo.Profesor;
import com.escuela.modelo.ProfesorDAO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ProfesorView extends JFrame {
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtNumEmpleado;
    private JComboBox<Materia> comboMaterias;
    private DefaultListModel<Materia> listModel;
    private JList<Materia> listaMaterias;
    private JButton btnAgregar;
    private JButton btnQuitar;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnVerLista;

    public ProfesorView() {
        setTitle("Gestión de Profesores");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
    }    private void initComponents() {
        // Inicializar el modelo de la lista
        listModel = new DefaultListModel<>();
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 30, 20, 30),
            BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            )
        ));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(8, 5, 8, 15);
        gbc.weightx = 0.3;

        Font labelFont = new Font("Arial", Font.BOLD, 12);

        // Campos de texto
        addTextField(mainPanel, gbc, "Nombre:", txtNombre = new JTextField(20), labelFont);
        addTextField(mainPanel, gbc, "Apellido:", txtApellido = new JTextField(20), labelFont);
        addTextField(mainPanel, gbc, "Número de Empleado:", txtNumEmpleado = new JTextField(20), labelFont);

        // Panel de materias
        JPanel materiasPanel = new JPanel(new GridBagLayout());
        materiasPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Materias", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            labelFont,
            new Color(51, 51, 51) // Color del título
        ));

        GridBagConstraints gbcMaterias = new GridBagConstraints();
        gbcMaterias.insets = new Insets(5, 5, 5, 5);

        // ComboBox de materias disponibles
        comboMaterias = new JComboBox<>();
        comboMaterias.setPreferredSize(new Dimension(300, 25));
        comboMaterias.setFont(new Font("Arial", Font.PLAIN, 12));
        gbcMaterias.gridx = 0;
        gbcMaterias.gridy = 0;
        gbcMaterias.gridwidth = 2;
        gbcMaterias.fill = GridBagConstraints.HORIZONTAL;
        materiasPanel.add(comboMaterias, gbcMaterias);

        // Botones de agregar/quitar
        btnAgregar = new JButton("Agregar");
        btnQuitar = new JButton("Quitar");
        
        // Configurar colores de botones de materias
        configureButton(btnAgregar, new Dimension(120, 30), labelFont, new Color(51, 122, 183), Color.WHITE);
        configureButton(btnQuitar, new Dimension(120, 30), labelFont, new Color(217, 83, 79), Color.WHITE);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botonesPanel.add(btnAgregar);
        botonesPanel.add(btnQuitar);
        gbcMaterias.gridy = 1;
        materiasPanel.add(botonesPanel, gbcMaterias);

        // Lista de materias seleccionadas
        listModel = new DefaultListModel<>();
        listaMaterias = new JList<>(listModel);
        listaMaterias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaMaterias.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(listaMaterias);
        scrollPane.setPreferredSize(new Dimension(300, 150));
        gbcMaterias.gridy = 2;
        materiasPanel.add(scrollPane, gbcMaterias);

        // Agregar panel de materias al panel principal
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(materiasPanel, gbc);

        // Panel de botones principales
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        btnGuardar = new JButton("Guardar");
        btnLimpiar = new JButton("Limpiar");
        btnVerLista = new JButton("Ver Lista");
        
        Dimension buttonSize = new Dimension(130, 35);
        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        
        configureButton(btnGuardar, buttonSize, buttonFont, new Color(51, 122, 183), Color.WHITE);
        configureButton(btnLimpiar, buttonSize, buttonFont, new Color(238, 238, 238), new Color(51, 51, 51));
        configureButton(btnVerLista, buttonSize, buttonFont, new Color(92, 184, 92), Color.WHITE);
        
        buttonGbc.insets = new Insets(10, 25, 10, 25);
        buttonPanel.add(btnGuardar, buttonGbc);
        buttonGbc.gridx = 1;
        buttonPanel.add(btnLimpiar, buttonGbc);
        buttonGbc.gridx = 2;
        buttonPanel.add(btnVerLista, buttonGbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 5, 10, 5);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        // Configurar listeners internos
        btnAgregar.addActionListener(e -> agregarMateriaSeleccionada());
        btnQuitar.addActionListener(e -> quitarMateriaSeleccionada());
        btnVerLista.addActionListener(e -> mostrarListaProfesores());
    }

    private void mostrarListaProfesores() {
        try {
            ProfesorDAO profesorDAO = new ProfesorDAO();
            List<Profesor> profesores = profesorDAO.cargarProfesores();
            TablaProfesorView tabla = new TablaProfesorView(this, profesores);
            tabla.setVisible(true);
        } catch (Exception e) {
            mostrarMensaje(
                "Error al cargar la lista de profesores: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addTextField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, Font font) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(font);
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        field.setPreferredSize(new Dimension(200, 25));
        panel.add(field, gbc);
        
        gbc.gridy++;
    }

    private void configureButton(JButton button, Dimension size, Font font, Color bg, Color fg) {
        button.setPreferredSize(size);
        button.setFont(font);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setOpaque(true);
    }

    private void agregarMateriaSeleccionada() {
        Materia materia = (Materia) comboMaterias.getSelectedItem();
        if (materia != null && !listModel.contains(materia)) {
            listModel.addElement(materia);
        }
    }

    private void quitarMateriaSeleccionada() {
        int selectedIndex = listaMaterias.getSelectedIndex();
        if (selectedIndex != -1) {
            listModel.remove(selectedIndex);
        }
    }

    public void setMaterias(List<Materia> materias) {
        comboMaterias.removeAllItems();
        for (Materia materia : materias) {
            comboMaterias.addItem(materia);
        }
    }

    public List<Materia> getMateriasSeleccionadas() {
        List<Materia> materias = new java.util.ArrayList<>();
        for (int i = 0; i < listModel.getSize(); i++) {
            materias.add(listModel.getElementAt(i));
        }
        return materias;
    }

    // Getters
    public String getNombre() { return txtNombre.getText(); }
    public String getApellido() { return txtApellido.getText(); }
    public String getNumEmpleado() { return txtNumEmpleado.getText(); }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtNumEmpleado.setText("");
        listModel.clear();
        txtNombre.requestFocus();
    }

    public void addGuardarListener(ActionListener listener) {
        btnGuardar.addActionListener(listener);
    }

    public void addLimpiarListener(ActionListener listener) {
        btnLimpiar.addActionListener(listener);
    }

    public void addVerListaListener(ActionListener listener) {
        btnVerLista.addActionListener(listener);
    }

    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
}
