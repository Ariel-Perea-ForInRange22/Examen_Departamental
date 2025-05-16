package com.escuela.modelo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {
    private static final String FILE_PATH = "materias.json";
    private final ObjectMapper mapper;
    private List<Materia> materias;

    public MateriaDAO() {
        this.mapper = new ObjectMapper();
        this.materias = cargarMaterias();
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    private List<Materia> cargarMaterias() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            CollectionType listType = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, Materia.class);
            return mapper.readValue(file, listType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
