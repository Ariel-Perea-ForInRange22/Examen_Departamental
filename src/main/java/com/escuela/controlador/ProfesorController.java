package com.escuela.controlador;

import com.escuela.modelo.Materia;
import com.escuela.modelo.Profesor;
import com.escuela.modelo.ProfesorDAO;
import com.escuela.modelo.MateriaDAO;
import com.escuela.vista.ProfesorView;
import com.escuela.vista.TablaProfesorView;
import javax.swing.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class ProfesorController {
    private final ProfesorView view;
    private final Set<String> numerosEmpleado;
    private final ProfesorDAO profesorDAO;
    private final MateriaDAO materiaDAO;

    public ProfesorController(ProfesorView view, ProfesorDAO profesorDAO) {
        this.view = view;
        this.profesorDAO = profesorDAO;
        this.materiaDAO = new MateriaDAO();
        this.numerosEmpleado = new HashSet<>();
        
        // Cargar profesores existentes
        cargarProfesoresExistentes();
        
        // Cargar materias en el ComboBox
        view.setMaterias(materiaDAO.getMaterias());
          // Agregar los action listeners
        this.view.addGuardarListener(e -> guardarProfesor());
        this.view.addLimpiarListener(e -> view.limpiarCampos());
        this.view.addVerListaListener(e -> mostrarListaProfesores());
    }

    private void cargarProfesoresExistentes() {
        List<Profesor> profesores = profesorDAO.cargarProfesores();
        for (Profesor profesor : profesores) {
            numerosEmpleado.add(profesor.getNumEmpleado());
        }
    }

    private void guardarProfesor() {
        try {
            // Obtener los datos del formulario
            String nombre = view.getNombre();
            String apellido = view.getApellido();
            String numEmpleado = view.getNumEmpleado();
            List<Materia> materias = view.getMateriasSeleccionadas();

            // Validar que tenga al menos una materia
            if (materias.isEmpty()) {
                view.mostrarMensaje(
                    "Debe seleccionar al menos una materia",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Validar que el número de empleado no esté duplicado
            if (numerosEmpleado.contains(numEmpleado)) {
                view.mostrarMensaje(
                    "El número de empleado ya existe",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Crear nuevo profesor
            Profesor profesor = new Profesor(nombre, apellido, numEmpleado);
            profesor.setMaterias(materias);

            // Guardar en archivo JSON
            profesorDAO.guardarProfesor(profesor);

            // Agregar el número de empleado al set
            numerosEmpleado.add(numEmpleado);

            // Mostrar mensaje de éxito
            view.mostrarMensaje(
                "Profesor guardado exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );

            // Limpiar el formulario
            view.limpiarCampos();

            // Mostrar la tabla con todos los profesores
            List<Profesor> profesores = profesorDAO.cargarProfesores();
            TablaProfesorView tabla = new TablaProfesorView(view, profesores);
            tabla.setVisible(true);

        } catch (Exception e) {
            view.mostrarMensaje(
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void mostrarListaProfesores() {
        try {
            List<Profesor> profesores = profesorDAO.cargarProfesores();
            TablaProfesorView tabla = new TablaProfesorView(view, profesores);
            tabla.setVisible(true);
        } catch (Exception e) {
            view.mostrarMensaje(
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
