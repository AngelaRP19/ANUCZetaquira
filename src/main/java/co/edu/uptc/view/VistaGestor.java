package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

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
        
        // Configuración básica de la ventana
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setLayout(new BorderLayout());
        setResizable(false);
        
        // Agregar listener para manejar cambios de estado de la ventana
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                // Cuando se hace clic en el icono de la barra de tareas después de minimizar
                if ((e.getNewState() & JFrame.ICONIFIED) == 0 && 
                    (e.getOldState() & JFrame.ICONIFIED) != 0) {
                    // La ventana se está restaurando desde el estado minimizado
                    restaurarVentana();
                }
            }
        });
        
        setVisible(true);
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
        
        return null;
    }
    public String getNombreProyectoEditado() {
        if (panelEditarProyecto != null) {
            return panelEditarProyecto.getNombreProyecto();
        }
        return null;
    }
    public Date getFechaInicioProyecto(){
        if (panelCrearProyecto != null) {
            return panelCrearProyecto.getFechaInicio();
        }
        return null;
    }
    public Date getFechaInicioProyectoEditado(){
        if (panelEditarProyecto != null) {
            return panelEditarProyecto.getFechaInicio();
        }
        return null;
    }
    public Date getFechaFinProyecto(){
        if (panelCrearProyecto != null) {
            return panelCrearProyecto.getFechaFin();
        }
        return null;
    }
    public Date getFechaFinProyectoEditado(){
        if (panelEditarProyecto != null) {
            return panelEditarProyecto.getFechaFin();
        }
        return null;
    }
    public String getEstadoProyecto(){
        if (panelCrearProyecto != null) {
            return panelCrearProyecto.getEstado();
        }
        return null;
    }
    public String getEstadoProyectoEditado(){
        if (panelEditarProyecto != null) {
            return panelEditarProyecto.getEstado();
        }
        return null;
    }
    public String getDescripcionProyecto(){
        if (panelCrearProyecto != null) {
            return panelCrearProyecto.getDescripcion();
        }
        return null;
    }
    public String getDescripcionProyectoEditado(){
        if (panelEditarProyecto != null) {
            return panelEditarProyecto.getDescripcion();
        }
        return null;
    }
    
    // Información de panel de crear/editar actividad
    // Prioriza panelEditarActividad si está disponible (para edición)
    public String getNombreActividad() {
        if (panelEditarActividad != null) {
            return panelEditarActividad.getNombreProyecto();
        }
        if (panelCrearActividad != null) {
            return panelCrearActividad.getNombreProyecto();
        }
        return null;
    }
    
    public Date getFechaActividad() {
        if (panelEditarActividad != null) {
            return panelEditarActividad.getFechaInicio();
        }
        if (panelCrearActividad != null) {
            return panelCrearActividad.getFechaInicio();
        }
        return null;
    }
    
    public String getTipoActividad() {
        if (panelEditarActividad != null) {
            return panelEditarActividad.getTipoActividad();
        }
        if (panelCrearActividad != null) {
            return panelCrearActividad.getTipoActividad();
        }
        return null;
    }
    
    public String getDescripcionActividad() {
        if (panelEditarActividad != null) {
            return panelEditarActividad.getDescripcion();
        }
        if (panelCrearActividad != null) {
            return panelCrearActividad.getDescripcion();
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
    
    public void setNombreProyecto(String nombre) {
        if (panelCrearProyecto != null) {
            panelCrearProyecto.setNombreProyecto(nombre);
        }
    }
    public void setFechaInicioProyecto(Date fecha) {
        if (panelCrearProyecto != null) {
            panelCrearProyecto.setFechaInicio(fecha);
        }
    }
    public void setFechaFinProyecto(Date fecha) {
        if (panelCrearProyecto != null) {
            panelCrearProyecto.setFechaFin(fecha);
        }
    }
    public void setEstadoProyecto(String estadoTexto) {
        if (panelCrearProyecto != null) {
            panelCrearProyecto.setEstado(estadoTexto);
        }
    }
    public void setDescripcionProyecto(String descripcion) {
        if (panelCrearProyecto != null) {
            panelCrearProyecto.setDescripcion(descripcion);
        }
    }
    private void restaurarVentana() {
        System.out.println("[LOG] VistaGestor.restaurarVentana() - Restaurando ventana desde estado minimizado");
        
        // Restaurar el estado maximizado
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Asegurar que la ventana esté visible y al frente
        setVisible(true);
        toFront();
        requestFocus();
        
        System.out.println("[LOG] VistaGestor.restaurarVentana() - Ventana restaurada exitosamente");
    }
    
    public void minimizarVentana() {
        System.out.println("[LOG] VistaGestor.minimizarVentana() - Minimizando ventana");
        setExtendedState(JFrame.ICONIFIED);
    }
    
    /**
     * Muestra un JFileChooser para que el usuario seleccione la carpeta donde descargar un archivo
     * @return La ruta de la carpeta seleccionada, o null si se canceló la selección
     */
    public void limpiarPanelEditarActividad() {
        System.out.println("[LOG] VistaGestor.limpiarPanelEditarActividad() - Limpiando panel de editar actividad");
        panelEditarActividad = null;
    }

    public String seleccionarCarpetaDescarga() {
        System.out.println("[LOG] VistaGestor.seleccionarCarpetaDescarga() - Iniciando selección de carpeta");
        
        try {
            // Configurar el JFileChooser en español
            UIManager.put("FileChooser.openDialogTitleText", "Seleccionar carpeta de descarga");
            UIManager.put("FileChooser.lookInLabelText", "Buscar en:");
            UIManager.put("FileChooser.openButtonText", "Seleccionar");
            UIManager.put("FileChooser.cancelButtonText", "Cancelar");
            UIManager.put("FileChooser.fileNameLabelText", "Nombre del archivo:");
            UIManager.put("FileChooser.filesOfTypeLabelText", "Tipo de archivo:");
            UIManager.put("FileChooser.openButtonToolTipText", "Seleccionar carpeta");
            UIManager.put("FileChooser.cancelButtonToolTipText", "Cancelar selección");
            UIManager.put("FileChooser.upFolderToolTipText", "Subir un nivel");
            UIManager.put("FileChooser.homeFolderToolTipText", "Ir a la carpeta personal");
            UIManager.put("FileChooser.newFolderToolTipText", "Crear nueva carpeta");
            UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
            UIManager.put("FileChooser.detailsViewButtonToolTipText", "Detalles");
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setLocale(new Locale("es", "ES"));
            
            // Configurar para seleccionar solo directorios
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDialogTitle("Seleccionar carpeta de descarga");
            
            // Establecer la carpeta de descargas por defecto
            String rutaDescargas = System.getProperty("user.home") + "\\Downloads";
            fileChooser.setCurrentDirectory(new java.io.File(rutaDescargas));
            
            // Mostrar el diálogo
            int resultado = fileChooser.showOpenDialog(this);
            
            if (resultado == JFileChooser.APPROVE_OPTION) {
                java.io.File carpetaSeleccionada = fileChooser.getSelectedFile();
                String rutaCarpeta = carpetaSeleccionada.getAbsolutePath();
                System.out.println("[LOG] VistaGestor.seleccionarCarpetaDescarga() - Carpeta seleccionada: " + rutaCarpeta);
                return rutaCarpeta;
            } else {
                System.out.println("[LOG] VistaGestor.seleccionarCarpetaDescarga() - Selección cancelada por el usuario");
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] VistaGestor.seleccionarCarpetaDescarga() - Error al mostrar selector: " + e.getMessage());
            e.printStackTrace();
            showErrorMessage("Error al mostrar el selector de carpeta: " + e.getMessage());
            return null;
        }
    }

}
