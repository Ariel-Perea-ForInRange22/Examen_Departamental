package com.escuela.vista;

import com.escuela.modelo.Materia;
import com.escuela.modelo.Profesor;
import com.escuela.modelo.ProfesorDAO;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

public class TablaProfesorView extends JDialog {
    private JTable tabla;
    private DefaultTableModel modelo;
    private List<Profesor> profesores;
    private ProfesorDAO profesorDAO;

    public TablaProfesorView(JFrame parent, List<Profesor> profesores) {
        super(parent, "Lista de Profesores", true);
        this.profesores = profesores;
        this.profesorDAO = new ProfesorDAO();
        initComponents();
        setSize(800, 400);
        setLocationRelativeTo(parent);
    }

    private void editarProfesorSeleccionado() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Profesor profesor = profesores.get(filaSeleccionada);
            EditarProfesorDialog dialogo = new EditarProfesorDialog(this, profesor);
            dialogo.setVisible(true);
            // Si el profesor fue editado, actualizar la tabla
            if (dialogo.fueEditado()) {
                modelo.setValueAt(profesor.getNombre(), filaSeleccionada, 0);
                modelo.setValueAt(profesor.getApellido(), filaSeleccionada, 1);
                modelo.setValueAt(profesor.getNumEmpleado(), filaSeleccionada, 2);
                String materiasStr = profesor.getMaterias().stream()
                    .map(Materia::getNombre)
                    .collect(Collectors.joining(", "));
                modelo.setValueAt(materiasStr, filaSeleccionada, 3);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione un profesor para editar",
                "Selección requerida",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void eliminarProfesorSeleccionado() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar este profesor?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    // Eliminar de la lista y actualizar el archivo
                    profesores.remove(filaSeleccionada);
                    profesorDAO.actualizarProfesores(profesores);
                    
                    // Eliminar de la tabla
                    modelo.removeRow(filaSeleccionada);
                    
                    JOptionPane.showMessageDialog(this,
                        "Profesor eliminado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                        "Error al eliminar el profesor: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Por favor, seleccione un profesor para eliminar",
                "Selección requerida",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void configureButton(JButton button, String text, Color bg, Color fg) {
        button.setText(text);
        button.setPreferredSize(new Dimension(100, 30));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
    }

    private void initComponents() {
        // Crear el modelo de la tabla
        String[] columnas = {"Nombre", "Apellido", "Número de Empleado", "Materias"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        // Llenar la tabla con los datos
        for (Profesor profesor : profesores) {
            String materiasStr = profesor.getMaterias().stream()
                .map(Materia::getNombre)
                .collect(Collectors.joining(", "));
            
            Object[] fila = {
                profesor.getNombre(),
                profesor.getApellido(),
                profesor.getNumEmpleado(),
                materiasStr
            };
            modelo.addRow(fila);
        }

        // Crear la tabla
        tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        tabla.setShowGrid(true);
        tabla.setGridColor(Color.BLACK);
        tabla.setRowHeight(25);
        tabla.setForeground(Color.BLACK);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));

        // Configurar el encabezado
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(51, 122, 183)); // Azul más brillante
        tabla.getTableHeader().setForeground(Color.WHITE);

        // Crear el panel con scroll
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configurar el layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // Panel para los botones
        JPanel panelBoton = new JPanel();
        panelBoton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Crear y configurar los botones
        JButton btnEditar = new JButton();
        configureButton(btnEditar, "Editar", new Color(51, 122, 183), Color.WHITE);
        btnEditar.addActionListener(e -> editarProfesorSeleccionado());
        
        JButton btnEliminar = new JButton();
        configureButton(btnEliminar, "Eliminar", new Color(217, 83, 79), Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarProfesorSeleccionado());
        
        JButton btnCerrar = new JButton();
        configureButton(btnCerrar, "Cerrar", new Color(238, 238, 238), new Color(51, 51, 51));
        btnCerrar.addActionListener(e -> dispose());
        
        // Agregar espaciado entre botones
        panelBoton.add(btnEditar);
        panelBoton.add(Box.createRigidArea(new Dimension(20, 0))); // Espacio entre botones
        panelBoton.add(btnEliminar);
        panelBoton.add(Box.createRigidArea(new Dimension(20, 0))); // Espacio entre botones
        panelBoton.add(btnCerrar);
        
        add(panelBoton, BorderLayout.SOUTH);
    }
}
