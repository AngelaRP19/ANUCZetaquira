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
        getContentPane().removeAll();
        getContentPane().add(nuevoPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    public void bienvenida (){
        panelBienvenida = new Bienvenida();
        cambiarPanel(new Principal(listener, panelBienvenida));
    }
    public void crearProyecto (){
        panelCrearProyecto = new CrearProyecto(listener);
        cambiarPanel(new Principal(listener, panelCrearProyecto));
    }
    public void verProyectos ( List<String> proyectos){
        panelVerProyectos = new VerProyectos(listener, proyectos);
        cambiarPanel(new Principal(listener, panelVerProyectos));
    }
    public void editarProyecto (List<String> datosProyecto){
        panelEditarProyecto = new EditarProyecto(listener, datosProyecto);
        cambiarPanel(new Principal(listener, panelEditarProyecto));
    }
    public void crearActividad (String [] tiposActividad, String nombreProyecto){
        panelCrearActividad = new CrearActividad(listener, tiposActividad, nombreProyecto);
        cambiarPanel(new Principal(listener, panelCrearActividad));
    }
    public void verActividades (List<String> actividades, String nombreProyecto){
        panelVerActividades = new VerActividades(listener, actividades, nombreProyecto);
        cambiarPanel(new Principal(listener, panelVerActividades));
    }
    public void editarActividad (String [] tiposActividad, String nombreProyecto, List<String> datosActividad){
        panelEditarActividad = new EditarActividad(listener, tiposActividad, nombreProyecto, datosActividad);
        cambiarPanel(new Principal(listener, panelEditarActividad));
    }
    public void subirDocumento (String [] tiposDocumento, String nombreProyecto){
        panelSubirDocumento = new SubirDocumento(listener, tiposDocumento, nombreProyecto);
        cambiarPanel(new Principal(listener, panelSubirDocumento));
    }
    public void verDocumentos (List<String> documentos, String nombreProyecto){
        panelVerDocumentos = new VerDocumentos(listener, documentos, nombreProyecto);
        cambiarPanel(new Principal(listener, panelVerDocumentos));
    }

    public void showErrorMessage(String message){
        DialogoPersonalizado.mostrarError(this, message, "Error");
       
    }
    
    public void showMessage(String message){
        DialogoPersonalizado.mostrarInfo(this, message, "Información");
    }

    public void descargarManualUsuario() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'descargarManualUsuario'");
    }

    //Información de panel de crear  proyecto
    public String getNombreProyecto() {
        return panelCrearProyecto.getNombreProyecto();
    }
    public Date getFechaInicioProyecto(){
        return panelCrearProyecto.getFechaInicio();
    }
    public Date getFechaFinProyecto(){
        return panelCrearProyecto.getFechaFin();
    }
    public String getEstadoProyecto(){
        return panelCrearProyecto.getEstado();
    }
    public String getDescripcionProyecto(){
        return panelCrearProyecto.getDescripcion();
    }
}
