package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;


public class VistaEjemplo extends JFrame implements ActionListener{

    VerDocumentos vd;
    SubirDocumento vistaSubirDocumento;
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
        datosProyecto.add("Capacitacion de istema de Gestión ANUC Zetaquira");
        datosProyecto.add("15-01-2025");
        datosProyecto.add("CAPACITACION");
        datosProyecto.add("Este proyecto tiene como objetivo desarrollar un sistema integral para la gestión de proyectos, actividades y documentos de la Asociación Nacional de Usuarios Campesinos (ANUC) en Zetaquira. El sistema permitirá el registro, seguimiento y control de todas las iniciativas comunitarias.");
        
        //add(new Principal(null, new VerTodosProyectos(null, datos)), BorderLayout.CENTER);
        add(new Principal(null, new CrearProyecto(null)), BorderLayout.CENTER);
        //add(new Principal(null, new VerProyecto(null, datosProyecto)), BorderLayout.CENTER);
/*
        String[] opciones = {"Opción 1", "Opción 2", "Opción 3", "Opción 4", "Opción 5", "Opción 6", "Opción 7", "Opción 8", "Opción 9", "Opción 10"};
        JPanel panelOpciones = new JPanel();
        panelOpciones.add(new CajaOpciones(opciones, 500));*/
        //add(new Principal(null, new EditarActividad(null, new String[]{"CAPACITACION", "ASISTENCIA_TECNICA", "DOTACION_EQUIPOS", "SEGUIMIENTO", "SOCIALIZACION"}, "Proyecto de Ejemplo", datosProyecto)), BorderLayout.CENTER);
        //this.vd = new VerDocumentos(this, datos, "nose");
        //add(new Principal(null, vd));
        vistaSubirDocumento = new SubirDocumento(this, new String[]{"ACTA","PROPUESTA","INFORMES","SOPORTE_FINANCIERO","MATERIAL_TECNICO"}, "Proyecto de Ejemplo");
        //add(new Principal(this, vistaSubirDocumento));
        // Mostrar la ventana después de agregar los componentes
        setVisible(true);
        setResizable(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getActionCommand().contains("DESCARGAR_DOCUMENTO/")) {
            System.out.println(this.vd.seleccionarCarpetaDescarga());
        } 
        if (e.getActionCommand().contains("SUBIR_ARCHIVO/")) {
            String rutaArchivo = vistaSubirDocumento.seleccionarArchivo();
            if (rutaArchivo != null) {
                // Obtener solo el nombre del archivo para mostrar
                String nombreArchivo = new java.io.File(rutaArchivo).getName();
                vistaSubirDocumento.mostrarArchivoSubido(nombreArchivo);
                // Guardar rutaArchivo para usarlo después al guardar el documento
            }
        }
        if (e.getActionCommand().startsWith("LIMPIAR_ARCHIVO")) {
            System.out.println(vistaSubirDocumento.getRutaArchivoSeleccionado());
        vistaSubirDocumento.limpiarArchivoSeleccionado();
        vistaSubirDocumento.setRutaArchivoSeleccionado(null);
        System.out.println(vistaSubirDocumento.getRutaArchivoSeleccionado());
        }
    }
    public static void main(String[] args) {
        System.out.println("Iniciando VistaEjemplo");
         new VistaEjemplo();
    }

    
}