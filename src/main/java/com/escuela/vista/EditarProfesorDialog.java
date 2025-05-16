package com.escuela.vista;

import com.escuela.modelo.Materia;
import com.escuela.modelo.Profesor;
import com.escuela.modelo.MateriaDAO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class EditarProfesorDialog extends JDialog {
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtNumEmpleado;
    private JComboBox<Materia> comboMaterias;
    private DefaultListModel<Materia> listModel;
    private JList<Materia> listaMaterias;
    private boolean editado = false;
    private final Profesor profesor;

    public EditarProfesorDialog(JDialog parent, Profesor profesor) {
        super(parent, "Editar Profesor", true);
        this.profesor = profesor;
        initComponents();
        cargarDatosProfesor();
        setSize(500, 600);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;        // Campos de texto
        Font labelFont = new Font("Arial", Font.BOLD, 12);
        txtNombre = new JTextField(20);
        txtApellido = new JTextField(20);
        txtNumEmpleado = new JTextField(20);
        txtNumEmpleado.setEnabled(false);
        txtNumEmpleado.setBackground(new Color(240, 240, 240));

        // Configurar la fuente y color de los campos
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 12));
        txtApellido.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNumEmpleado.setFont(new Font("Arial", Font.PLAIN, 12));

        // Agregar campos de texto
        gbc.gridx = 0; gbc.gridy = 0;        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblApellido = new JLabel("Apellido:");
        JLabel lblNumEmpleado = new JLabel("Número de Empleado:");
        
        // Configurar fuente de las etiquetas
        lblNombre.setFont(labelFont);
        lblApellido.setFont(labelFont);
        lblNumEmpleado.setFont(labelFont);
        lblNombre.setForeground(new Color(51, 51, 51));
        lblApellido.setForeground(new Color(51, 51, 51));
        lblNumEmpleado.setForeground(new Color(51, 51, 51));

        add(lblNombre, gbc);
        gbc.gridx = 1;
        add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(lblApellido, gbc);
        gbc.gridx = 1;
        add(txtApellido, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(lblNumEmpleado, gbc);
        gbc.gridx = 1;
        add(txtNumEmpleado, gbc);        // Panel de materias
        JPanel materiasPanel = new JPanel(new GridBagLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Materias",
            TitledBorder.LEFT,
            TitledBorder.TOP
        );
        titledBorder.setTitleFont(labelFont);
        titledBorder.setTitleColor(new Color(51, 51, 51));
        materiasPanel.setBorder(titledBorder);

        GridBagConstraints gbcMaterias = new GridBagConstraints();
        gbcMaterias.insets = new Insets(5, 5, 5, 5);
        gbcMaterias.fill = GridBagConstraints.HORIZONTAL;

        // ComboBox de materias disponibles
        comboMaterias = new JComboBox<>();
        MateriaDAO materiaDAO = new MateriaDAO();
        for (Materia materia : materiaDAO.getMaterias()) {
            comboMaterias.addItem(materia);
        }
        comboMaterias.setPreferredSize(new Dimension(300, 25));
        gbcMaterias.gridx = 0;
        gbcMaterias.gridy = 0;
        gbcMaterias.gridwidth = 2;
        materiasPanel.add(comboMaterias, gbcMaterias);

        // Botones de agregar/quitar
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnQuitar = new JButton("Quitar");
        botonesPanel.add(btnAgregar);
        botonesPanel.add(btnQuitar);
        gbcMaterias.gridy = 1;
        materiasPanel.add(botonesPanel, gbcMaterias);

        // Lista de materias seleccionadas
        listModel = new DefaultListModel<>();
        listaMaterias = new JList<>(listModel);
        listaMaterias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listaMaterias);
        scrollPane.setPreferredSize(new Dimension(300, 150));
        gbcMaterias.gridy = 2;
        materiasPanel.add(scrollPane, gbcMaterias);

        // Agregar panel de materias al diálogo
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(materiasPanel, gbc);        // Panel de botones principales
        JPanel buttonPanel = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        // Configurar estilo de los botones
        configureButton(btnAgregar, new Color(51, 122, 183), Color.WHITE);
        configureButton(btnQuitar, new Color(217, 83, 79), Color.WHITE);
        configureButton(btnGuardar, new Color(92, 184, 92), Color.WHITE);
        configureButton(btnCancelar, new Color(238, 238, 238), new Color(51, 51, 51));

        btnAgregar.addActionListener(e -> agregarMateriaSeleccionada());
        btnQuitar.addActionListener(e -> quitarMateriaSeleccionada());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }

    private void agregarMateriaSeleccionada() {
        Materia materia = (Materia) comboMaterias.getSelectedItem();
        if (materia != null && !contieneMateria(materia)) {
            listModel.addElement(materia);
        }
    }

    private boolean contieneMateria(Materia materia) {
        for (int i = 0; i < listModel.getSize(); i++) {
            if (listModel.getElementAt(i).getId().equals(materia.getId())) {
                return true;
            }
        }
        return false;
    }

    private void quitarMateriaSeleccionada() {
        int selectedIndex = listaMaterias.getSelectedIndex();
        if (selectedIndex != -1) {
            listModel.remove(selectedIndex);
        }
    }

    private void cargarDatosProfesor() {
        txtNombre.setText(profesor.getNombre());
        txtApellido.setText(profesor.getApellido());
        txtNumEmpleado.setText(profesor.getNumEmpleado());
        
        // Cargar materias del profesor
        listModel.clear();
        for (Materia materia : profesor.getMaterias()) {
            listModel.addElement(materia);
        }
    }

    private void guardarCambios() {
        try {
            profesor.setNombre(txtNombre.getText());
            profesor.setApellido(txtApellido.getText());
            
            // Actualizar materias
            List<Materia> materias = new ArrayList<>();
            for (int i = 0; i < listModel.getSize(); i++) {
                materias.add(listModel.getElementAt(i));
            }
            profesor.setMaterias(materias);
            
            editado = true;
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configureButton(JButton button, Color bg, Color fg) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
    }

    public boolean fueEditado() {
        return editado;
    }
}
