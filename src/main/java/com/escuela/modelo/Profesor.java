package com.escuela.modelo;

import java.util.ArrayList;
import java.util.List;

public class Profesor {
    private String nombre;
    private String apellido;
    private String numEmpleado;
    private List<Materia> materias;

    public Profesor() {
        this.materias = new ArrayList<>();
    }

    public Profesor(String nombre, String apellido, String numEmpleado) {
        this.setNombre(nombre);
        this.setApellido(apellido);
        this.setNumEmpleado(numEmpleado);
        this.materias = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        this.apellido = apellido.trim();
    }

    public String getNumEmpleado() {
        return numEmpleado;
    }

    public void setNumEmpleado(String numEmpleado) {
        if (numEmpleado == null || !numEmpleado.matches("\\d{10}")) {
            throw new IllegalArgumentException("El número de empleado debe tener exactamente 10 dígitos");
        }
        this.numEmpleado = numEmpleado;
    }

    public List<Materia> getMaterias() {
        return materias != null ? materias : new ArrayList<>();
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias != null ? materias : new ArrayList<>();
    }

    public void agregarMateria(Materia materia) {
        if (materia != null) {
            if (materias == null) {
                materias = new ArrayList<>();
            }
            materias.add(materia);
        }
    }

    public void eliminarMateria(Materia materia) {
        if (materia != null && materias != null) {
            materias.remove(materia);
        }
    }

    // For backwards compatibility with the old format
    public String getIdMateria() {
        if (materias != null && !materias.isEmpty()) {
            return materias.get(0).getId();
        }
        return "";
    }

    // For backwards compatibility with the old format
    public String getNombreMateria() {
        if (materias != null && !materias.isEmpty()) {
            return materias.get(0).getNombre();
        }
        return "";
    }

    @Override
    public String toString() {
        StringBuilder materiasStr = new StringBuilder();
        if (materias != null) {
            for (Materia materia : materias) {
                if (materiasStr.length() > 0) {
                    materiasStr.append(", ");
                }
                materiasStr.append(materia.getNombre());
            }
        }
        
        return "Profesor{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", numEmpleado='" + numEmpleado + '\'' +
                ", materias=[" + materiasStr + "]" +
                '}';
    }
}