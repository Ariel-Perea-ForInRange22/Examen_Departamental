package com.escuela.modelo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.escuela.modelo.Profesor;
import com.escuela.modelo.Materia;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {
    private static final String FILE_PATH = "profesores.json";
    private final ObjectMapper mapper;
    private final MateriaDAO materiaDAO;

    public ProfesorDAO() {
        this.mapper = new ObjectMapper();
        this.materiaDAO = new MateriaDAO();
        // Configurar Jackson para manejar mejor las referencias circulares
        this.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public void guardarProfesor(Profesor profesor) throws IOException {
        List<Profesor> profesores = cargarProfesores();
        profesores.add(profesor);
        mapper.writeValue(new File(FILE_PATH), profesores);
    }

    public List<Profesor> cargarProfesores() {
        try {
            File file = new File(FILE_PATH);
            List<Profesor> profesores = new ArrayList<>();
            
            if (!file.exists()) {
                return profesores;
            }

            JsonNode root = mapper.readTree(file);
            if (root.isArray()) {
                for (JsonNode node : root) {
                    Profesor profesor = new Profesor(
                        node.get("nombre").asText(),
                        node.get("apellido").asText(),
                        node.get("numEmpleado").asText()
                    );
                    
                    // Si el profesor tiene un solo ID de materia (formato antiguo)
                    if (node.has("idMateria")) {
                        String idMateria = node.get("idMateria").asText();
                        String nombreMateria = node.get("nombreMateria").asText();
                        Materia materia = new Materia(idMateria, nombreMateria);
                        profesor.agregarMateria(materia);
                    } 
                    // Si el profesor tiene una lista de materias (nuevo formato)
                    else if (node.has("materias") && node.get("materias").isArray()) {
                        ArrayNode materiasNode = (ArrayNode) node.get("materias");
                        for (JsonNode materiaNode : materiasNode) {
                            Materia materia = mapper.treeToValue(materiaNode, Materia.class);
                            profesor.agregarMateria(materia);
                        }
                    }
                    
                    profesores.add(profesor);
                }
            }
            
            return profesores;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void actualizarProfesores(List<Profesor> profesores) throws IOException {
        mapper.writeValue(new File(FILE_PATH), profesores);
    }
}
