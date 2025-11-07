package co.edu.uptc.presenter;

import java.util.List;

import co.edu.uptc.model.Proyecto;
import co.edu.uptc.model.Gestor;

public class Presenter {

    private Gestor gestorProyecto;

    public Presenter() {
        this.gestorProyecto = new Gestor();
    }

    public void cargarTodoDesdeBD() {
        gestorProyecto.cargarTodoDesdeBD();
    }

    public List<Proyecto> getProyectos() {
       return gestorProyecto.getProyectos();
    }

    /*public Actividad consultarActividad(String string, String string2) {
        return gestorProyecto.consultarActividad(string, string2);
    }*/

    /*public void actualizarActividad(String n_proyecto, String n_actividad, String campo, String valor_nuevo) {
        gestorProyecto.actualizarActividadCampo(n_proyecto, n_actividad, campo, valor_nuevo);
    }*/
    /*public void subir_documento(String nombre_proyecto,String nombre_documento,String tipo,String ruta){
        gestorProyecto.registrarDocumento(nombre_proyecto, nombre_documento, tipo, ruta);
    }*/

    /*public void descargarDocumento(String nombreProyecto, String nombreDocumento, String rutaDestino) {
    gestorProyecto.descargarDocumento(nombreProyecto, nombreDocumento, rutaDestino);
    }*/

    public void eliminarDocumento(String nombreProyecto, String nombreDocumento) {
    gestorProyecto.eliminarDocumento(nombreProyecto, nombreDocumento);
    }
}