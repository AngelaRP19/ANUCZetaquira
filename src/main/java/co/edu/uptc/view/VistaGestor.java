package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class VistaGestor extends JFrame{
    
    private ActionListener listener;
    
    private Bienvenida panelBienvenida;
    private CrearProyecto panelCrearProyecto;
    private VerProyectos panelVerProyectos;
    private EditarProyecto panelEditarProyecto;
    private CrearActividad panelCrearActividad;
    private VerActividades panelVerActividades;
    private EditarActividad panelEditarActividad;
    private SubirDocumento panelSubirDocumento;
    private VerDocumentos panelVerDocumentos;

    public VistaGestor(ActionListener listener){
        this.listener = listener;
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setLayout(new BorderLayout());
        setVisible(true);
        setResizable(false); 
        bienvenida();
    }

    public void cambiarPanel(JPanel nuevoPanel) {
        System.out.println("[LOG] VistaGestor.cambiarPanel() - Cambiando panel");
        getContentPane().removeAll();
        getContentPane().add(nuevoPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        System.out.println("[LOG] VistaGestor.cambiarPanel() - Panel cambiado exitosamente");
    }
    public void bienvenida (){
        System.out.println("[LOG] VistaGestor.bienvenida() - Mostrando pantalla de bienvenida");
        panelBienvenida = new Bienvenida();
        cambiarPanel(new Principal(listener, panelBienvenida));
    }
    public void crearProyecto (){
        System.out.println("[LOG] VistaGestor.crearProyecto() - Mostrando formulario crear proyecto");
        panelCrearProyecto = new CrearProyecto(listener);
        cambiarPanel(new Principal(listener, panelCrearProyecto));
    }
    public void verProyectos ( List<String> proyectos){
        System.out.println("[LOG] VistaGestor.verProyectos() - Mostrando " + (proyectos != null ? proyectos.size() : 0) + " proyectos");
        panelVerProyectos = new VerProyectos(listener, proyectos);
        cambiarPanel(new Principal(listener, panelVerProyectos));
    }
    public void editarProyecto (List<String> datosProyecto){
        System.out.println("[LOG] VistaGestor.editarProyecto() - Mostrando detalles del proyecto");
        panelEditarProyecto = new EditarProyecto(listener, datosProyecto);
        cambiarPanel(new Principal(listener, panelEditarProyecto));
    }
    public void crearActividad (String [] tiposActividad, String nombreProyecto){
        System.out.println("[LOG] VistaGestor.crearActividad() - Mostrando formulario crear actividad para proyecto: " + nombreProyecto);
        panelCrearActividad = new CrearActividad(listener, tiposActividad, nombreProyecto);
        cambiarPanel(new Principal(listener, panelCrearActividad));
    }
    public void verActividades (List<String> actividades, String nombreProyecto){
        System.out.println("[LOG] VistaGestor.verActividades() - Mostrando " + (actividades != null ? actividades.size() : 0) + " actividades del proyecto: " + nombreProyecto);
        panelVerActividades = new VerActividades(listener, actividades, nombreProyecto);
        cambiarPanel(new Principal(listener, panelVerActividades));
    }
    public void editarActividad (String [] tiposActividad, String nombreProyecto, List<String> datosActividad){
        System.out.println("[LOG] VistaGestor.editarActividad() - Mostrando detalles de actividad del proyecto: " + nombreProyecto);
        panelEditarActividad = new EditarActividad(listener, tiposActividad, nombreProyecto, datosActividad);
        cambiarPanel(new Principal(listener, panelEditarActividad));
    }
    public void subirDocumento (String [] tiposDocumento, String nombreProyecto){
        System.out.println("[LOG] VistaGestor.subirDocumento() - Mostrando formulario subir documento para proyecto: " + nombreProyecto);
        panelSubirDocumento = new SubirDocumento(listener, tiposDocumento, nombreProyecto);
        cambiarPanel(new Principal(listener, panelSubirDocumento));
    }
    public void verDocumentos (List<String> documentos, String nombreProyecto){
        System.out.println("[LOG] VistaGestor.verDocumentos() - Mostrando " + (documentos != null ? documentos.size() : 0) + " documentos del proyecto: " + nombreProyecto);
        panelVerDocumentos = new VerDocumentos(listener, documentos, nombreProyecto);
        cambiarPanel(new Principal(listener, panelVerDocumentos));
    }

    public void showErrorMessage(String message){
        System.out.println("[LOG] VistaGestor.showErrorMessage() - Error: " + message);
        DialogoPersonalizado.mostrarError(this, message, "Error");
       
    }
    
    public void showMessage(String message){
        System.out.println("[LOG] VistaGestor.showMessage() - Información: " + message);
        DialogoPersonalizado.mostrarInfo(this, message, "Información");
    }

    public void descargarManualUsuario() {
        System.out.println("[LOG] VistaGestor.descargarManualUsuario() - Iniciando descarga de manual");
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Manual de usuario en desarrollo.\nPróximamente disponible.",
            "Manual de Usuario",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
        System.out.println("[LOG] VistaGestor.descargarManualUsuario() - Completado");
    }

    //Información de panel de crear proyecto
    public String getNombreProyecto() {
        if (panelCrearProyecto != null) {
            return panelCrearProyecto.getNombreProyecto();
        }
        if (panelEditarProyecto != null) {
            return panelEditarProyecto.getNombreProyecto();
        }
        return null;
    }
    public Date getFechaInicioProyecto(){
        if (panelCrearProyecto != null) {
            return panelCrearProyecto.getFechaInicio();
        }
        if (panelEditarProyecto != null) {
            return panelEditarProyecto.getFechaInicio();
        }
        return null;
    }
    public Date getFechaFinProyecto(){
        if (panelCrearProyecto != null) {
            return panelCrearProyecto.getFechaFin();
        }
        if (panelEditarProyecto != null) {
            return panelEditarProyecto.getFechaFin();
        }
        return null;
    }
    public String getEstadoProyecto(){
        if (panelCrearProyecto != null) {
            return panelCrearProyecto.getEstado();
        }
        // EditarProyecto no tiene getEstado(), solo CrearProyecto
        return null;
    }
    public String getDescripcionProyecto(){
        if (panelCrearProyecto != null) {
            return panelCrearProyecto.getDescripcion();
        }
        if (panelEditarProyecto != null) {
            return panelEditarProyecto.getDescripcion();
        }
        return null;
    }
    
    // Información de panel de crear actividad
    public String getNombreActividad() {
        if (panelCrearActividad != null) {
            return panelCrearActividad.getNombreProyecto();
        }
        if (panelEditarActividad != null) {
            return panelEditarActividad.getNombreProyecto();
        }
        return null;
    }
    public Date getFechaActividad() {
        if (panelCrearActividad != null) {
            return panelCrearActividad.getFechaInicio();
        }
        if (panelEditarActividad != null) {
            return panelEditarActividad.getFechaInicio();
        }
        return null;
    }
    public String getTipoActividad() {
        if (panelCrearActividad != null) {
            return panelCrearActividad.getTipoActividad();
        }
        if (panelEditarActividad != null) {
            return panelEditarActividad.getTipoActividad();
        }
        return null;
    }
    public String getDescripcionActividad() {
        if (panelCrearActividad != null) {
            return panelCrearActividad.getDescripcion();
        }
        if (panelEditarActividad != null) {
            return panelEditarActividad.getDescripcion();
        }
        return null;
    }
    
    // Información de panel de subir documento
    public String getNombreDocumento() {
        if (panelSubirDocumento != null) {
            return panelSubirDocumento.getNombreProyecto();
        }
        return null;
    }
    public String getTipoDocumento() {
        if (panelSubirDocumento != null) {
            return panelSubirDocumento.getTipoActividad();
        }
        return null;
    }
    public String getRutaArchivoDocumento() {
        if (panelSubirDocumento != null) {
            return panelSubirDocumento.getRutaArchivoSeleccionado();
        }
        return null;
    }
    
    public void setArchivoSeleccionado(String rutaArchivo, String nombreArchivo) {
        if (panelSubirDocumento != null) {
            panelSubirDocumento.setRutaArchivoSeleccionado(rutaArchivo, nombreArchivo);
        }
    }
    
    // Métodos para búsquedas
    public String getTextoBusquedaProyectos() {
        if (panelVerProyectos != null) {
            return panelVerProyectos.getTextoBusqueda();
        }
        return null;
    }
    
    public String getTextoBusquedaActividades() {
        if (panelVerActividades != null) {
            return panelVerActividades.getTextoBusqueda();
        }
        return null;
    }
    
    public String getTextoBusquedaDocumentos() {
        if (panelVerDocumentos != null) {
            return panelVerDocumentos.getTextoBusqueda();
        }
        return null;
    }
    
    public void showInfoMessage(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje, "Información", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}
