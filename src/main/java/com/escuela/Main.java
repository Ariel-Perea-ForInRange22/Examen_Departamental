package com.escuela;

import com.escuela.controlador.ProfesorController;
import com.escuela.modelo.ProfesorDAO;
import com.escuela.vista.ProfesorView;
import javax.swing.*;
import java.awt.Color;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando aplicación...");
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Configurar el look and feel para una apariencia más moderna
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Configurar colores personalizados para la interfaz
                UIManager.put("Button.background", new Color(238, 238, 238));
                UIManager.put("Button.foreground", new Color(51, 51, 51));
                UIManager.put("Button.select", new Color(51, 122, 183));
                UIManager.put("Button.focus", new Color(51, 122, 183));
                UIManager.put("Label.foreground", new Color(51, 51, 51));
                UIManager.put("Panel.background", new Color(240, 240, 240));
                UIManager.put("ComboBox.background", Color.WHITE);
                UIManager.put("ComboBox.foreground", new Color(51, 51, 51));
                UIManager.put("TextField.background", Color.WHITE);
                UIManager.put("TextField.foreground", new Color(51, 51, 51));

                // Crear instancias de los componentes MVC
                ProfesorView vista = new ProfesorView();
                ProfesorDAO modelo = new ProfesorDAO();
                ProfesorController controlador = new ProfesorController(vista, modelo);

                // Hacer visible la ventana
                vista.setVisible(true);

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                System.err.println("Error al configurar look and feel: " + e.getMessage());
                try {
                    // Si falla el look and feel del sistema, intentar con el cross-platform
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception ex) {
                    System.err.println("Error al configurar look and feel alternativo: " + ex.getMessage());
                }
            }
        });
    }
}
