package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;


public class VistaEjemplo extends JFrame {

    public VistaEjemplo() {
        // Cerrar al salir
        System.out.println("Iniciando VistaEjemplo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Hacer que el frame ocupe toda la pantalla
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false); // si quieres ocultar la barra de título, cambia a true

        // Usar BorderLayout para que el panel Principal ocupe todo el espacio
        setLayout(new BorderLayout());

        // Agregar el panel Principal
        List<String> datos = new ArrayList<>();
        datos.add("Proyecto de Ejemplo");
        datos.add("01-01-2024");
        datos.add("31-12-2024");
        datos.add("Descripción del proyecto de ejempl Java.");
        datos.add("Proyecto de Ejemplo");
        datos.add("01-01-2024");
        datos.add("31-12-2024");
        datos.add("Descripción del proyecto de ejempl Java.");
        datos.add("Proyecto de Ejemplo");
        datos.add("01-01-2024");
        datos.add("31-12-2024");
        datos.add("Descripción del proyecto de ejempl Java.");
        datos.add("Proyecto de Ejemplo");
        datos.add("01-01-2024");
        datos.add("31-12-2024");
        datos.add("Descripción del proyecto de ejempl Java.");
        datos.add("Proyecto de Ejemplo");
        datos.add("01-01-2024");
        datos.add("31-12-2024");
        datos.add("Descripción del proyecto de ejempl Java.");
        
        // Crear datos de prueba para VerProyecto (solo necesita 4 campos: nombre, fecha inicio, fecha fin, descripción)
        List<String> datosProyecto = new ArrayList<>();
        datosProyecto.add("Sistema de Gestión ANUC Zetaquira");
        datosProyecto.add("15-01-2025");
        datosProyecto.add("30-11-2025");
        datosProyecto.add("Este proyecto tiene como objetivo desarrollar un sistema integral para la gestión de proyectos, actividades y documentos de la Asociación Nacional de Usuarios Campesinos (ANUC) en Zetaquira. El sistema permitirá el registro, seguimiento y control de todas las iniciativas comunitarias.");
        
        //add(new Principal(null, new VerTodosProyectos(null, datos)), BorderLayout.CENTER);
        add(new Principal(null, new CrearProyecto(null)), BorderLayout.CENTER);
        //add(new Principal(null, new VerProyecto(null, datosProyecto)), BorderLayout.CENTER);

        // Mostrar la ventana después de agregar los componentes
        setVisible(true);
        setResizable(true);
    }

    public static void main(String[] args) {
        System.out.println("Iniciando VistaEjemplo");
         new VistaEjemplo();
    }
}