package co.edu.uptc.presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import co.edu.uptc.model.Proyecto;
import co.edu.uptc.model.Gestor;
import co.edu.uptc.view.VistaGestor;

public class Presenter implements ActionListener{

    private Gestor gestorProyecto;
    private VistaGestor vista;

    public Presenter() {
        this.gestorProyecto = new Gestor();
        this.gestorProyecto.cargarTodoDesdeBD();
        this.vista = new VistaGestor(this);
    }

    public void cargarTodoDesdeBD() {
        gestorProyecto.cargarTodoDesdeBD();
    }

    public List<Proyecto> getProyectos() {
       return gestorProyecto.getProyectos();
    }

    /**
     * Elimina proyectos con nombres inválidos: vacío, "x" o "aaaaa" (ignorando mayúsculas/minúsculas).
     * Retorna la cantidad de proyectos eliminados.
     */
    public int eliminarProyectosInvalidos() {
        List<Proyecto> actuales = gestorProyecto.getProyectos();
        // Copia defensiva de los nombres para evitar ConcurrentModification
        java.util.ArrayList<String> nombres = new java.util.ArrayList<>();
        for (Proyecto p : actuales) {
            nombres.add(p.getNombre());
        }

        int eliminados = 0;
        for (String nombre : nombres) {
            String n = (nombre == null) ? "" : nombre.trim();
            if (n.isEmpty() || n.equalsIgnoreCase("x") || n.equalsIgnoreCase("aaaaa")) {
                gestorProyecto.eliminarProyectoConDependencias(nombre);
                eliminados++;
            }
        }
        return eliminados;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand().toUpperCase()) {
            case "MINIMIZAR":
                vista.setExtendedState(JFrame.ICONIFIED);
                break;
            case "CERRAR":
                System.exit(0); 
                break;
            case "CREAR_PROYECTO":
                this.crearProyecto();
                break;
            case "VER_PROYECTOS":
                this.verProyectos();
                break;
            case "MANUAL_USUARIO":
                vista.descargarManualUsuario();
                break;
            case "GUARDAR_PROYECTO":
                this.guardarProyecto();
                break;
            case "VOLVER_BIENVENIDA":
                vista.bienvenida();
                break;
            default:
                throw new AssertionError();
        }    
    }
    private void guardarProyecto() {
        // 1) Leer valores desde la vista y sanear entradas de texto
        String nombreRaw = vista.getNombreProyecto();
        String nombre = (nombreRaw != null) ? nombreRaw.trim() : null;

        Date fechaInicio = vista.getFechaInicioProyecto();
        Date fechaFin = vista.getFechaFinProyecto();

        String descripcionRaw = vista.getDescripcionProyecto();
        String descripcion = (descripcionRaw != null) ? descripcionRaw.trim() : null;

        String estadoRaw = vista.getEstadoProyecto();
        String estado = (estadoRaw != null) ? estadoRaw.trim() : null;

        // 2) Validaciones de obligatorios: nombre, ambas fechas, descripcion y estado
        if (nombre == null || nombre.isEmpty()) {
            vista.showErrorMessage("El nombre del proyecto es obligatorio.");
            return;
        }
        if (fechaInicio == null ) {
            vista.showErrorMessage("Debe ingresar la fecha de inicio y del proyecto.");
            return;
        }
        if (descripcion == null || descripcion.isEmpty()) {
            vista.showErrorMessage("La descripción del proyecto es obligatoria.");
            return;
        }
        if (estado == null || estado.isEmpty()) {
            vista.showErrorMessage("El estado del proyecto es obligatorio.");
            return;
        }

        // 3) Validación de consistencia de fechas
        if (fechaFin.before(fechaInicio)) {
            vista.showErrorMessage("La fecha de finalización no puede ser anterior a la fecha de inicio.");
            return;
        }

        // 4) Validación de unicidad del nombre (ya saneado)
        if (gestorProyecto.proyectoExiste(nombre)) {
            vista.showErrorMessage("El nombre del proyecto ya existe. Por favor, ingrese otro nombre.");
            return;
        }

        // 5) Persistencia
        if (gestorProyecto.agregarProyecto(nombre, fechaInicio, fechaFin, estado, descripcion)) {
            vista.showMessage("Proyecto creado exitosamente.");
            vista.bienvenida();
        } else {
            vista.showErrorMessage("No se pudo crear el proyecto. Por favor, intente de nuevo.");
        }
    }

    private void crearProyecto() {
        vista.crearProyecto();
    }
    private void verProyectos() {
        List<String> nombresProyectos = gestorProyecto.getNombresProyectos();
        vista.verProyectos(nombresProyectos);
    }
}