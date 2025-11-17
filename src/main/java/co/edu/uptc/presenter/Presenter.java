package co.edu.uptc.presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import co.edu.uptc.model.Actividad;
import co.edu.uptc.model.Gestor;
import co.edu.uptc.model.Proyecto;
import co.edu.uptc.view.VistaGestor;

public class Presenter implements ActionListener{

    private Gestor gestorProyecto;
    private VistaGestor vista;
    
    // Estado actual de navegación
    private String proyectoActual;
    private String actividadActual;
    String[] tiposDocumento;
    String[] tiposActividad;
    
    public Presenter() {
        this.gestorProyecto = new Gestor();
        this.vista = new VistaGestor(this);
        this.proyectoActual = null;
        this.actividadActual = null;
        this.tiposDocumento = new String[] {"ACTA", "PROPUESTA", "INFORMES", "SOPORTE_FINANCIERO", "MATERIAL_TECNICO"};
        this.tiposActividad = new String[] {"CAPACITACION", "ASISTENCIA_TECNICA", "DOTACION_EQUIPOS", "SEGUIMIENTO", "SOCIALIZACION"};
    }
/*
    public void cargarTodoDesdeBD() {
        gestorProyecto.cargarTodoDesdeBD();
    }
*/
    public List<Proyecto> getProyectos() {
       return gestorProyecto.getProyectos();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand().toUpperCase();
        System.out.println("[LOG] Presenter.actionPerformed() - Comando recibido: " + comando);
        switch (comando) {
            case "MINIMIZAR":
                System.out.println("[LOG] Presenter - Minimizando ventana");
                vista.minimizarVentana();
                break;
            case "CERRAR":
                System.out.println("[LOG] Presenter - Cerrando aplicación");
                System.exit(0);
                break;
            case "CREAR_PROYECTO":
                System.out.println("[LOG] Presenter - Ir a crear proyecto");
                crearProyecto();
                break;
            case "VER_PROYECTOS":
                System.out.println("[LOG] Presenter - Ir a ver proyectos");
                verProyectos();
                break;
            case "MANUAL_USUARIO":
                System.out.println("[LOG] Presenter - Descargar manual de usuario");
                vista.descargarManualUsuario();
                break;
            case "VOLVER_BIENVENIDA":
                System.out.println("[LOG] Presenter - Volver a bienvenida");
                vista.bienvenida();
                break;
            case "PANEL_SUBIR_DOCUMENTO_NUEVO_PROYECTO":
                System.out.println("[LOG] Presenter - Ir a subir documento del proyecto que se está creando.");
                subirDocumentoNuevoProyeto();
                break;
            case "GUARDAR_DOCUMENTO/NUEVO_PROYECTO":
                System.out.println("[LOG] Presenter - Guardar documento de nuevo proyecto");
                guardarDocumentoNuevoProyecto();
                break;
            case "PANEL_VER_DOCUMENTOS_NUEVO_PROYECTO":
                System.out.println("[LOG] Presenter - Ver documentos del proyecto que se está creando.");
                verDocumentosNuevoProyecto();
                break;
            case "PANEL_AGREGAR_ACTIVIDAD_NUEVO_PROYECTO":
                System.out.println("[LOG] Presenter - Ir a agregar actividad del proyecto que se está creando.");
                agregarActividadNuevoProyecto();
                break;
            case "GUARDAR_ACTIVIDAD/NUEVO_PROYECTO":
                System.out.println("[LOG] Presenter - Guardar actividad de nuevo proyecto");
                guardarActividadNuevoProyecto();
                break;
            case "PANEL_VER_ACTIVIDADES_NUEVO_PROYECTO":
                System.out.println("[LOG] Presenter - Ver actividades del proyecto que se está creando.");
                verActividadesNuevoProyecto();
                break;
            case "CANCELAR_BUSQUEDA_PROYECTO":
                System.out.println("[LOG] Presenter - Cancelar búsqueda de proyectos y mostrar todos.");
                vista.bienvenida();
                break;
            case "VOLVER_VER_PROYECTO/NUEVO_PROYECTO":
                System.out.println("[LOG] Presenter - Volver a ver proyecto que se está creando.");
                volverACrearProyecto();
                break;
            case "GUARDAR_PROYECTO":
                guardarProyecto();
                break;
            case "GUARDAR_CAMBIOS":
                guardarProyecto();
                break;
            case "CANCELAR":
                cancelar();
                break;
        }
        if (comando.startsWith("DESCARGAR_DOCUMENTO/")) {
            String[] partes = comando.substring("DESCARGAR_DOCUMENTO/".length()).split("/");
            System.out.println("[DEBUG] Presenter - Comando DESCARGAR_DOCUMENTO recibido: " + comando);
            System.out.println("[DEBUG] Presenter - Partes del comando: " + java.util.Arrays.toString(partes));
            System.out.println("[DEBUG] Presenter - Número de partes: " + partes.length);
            
            if (partes.length >= 2) {
                System.out.println("[DEBUG] Presenter - Parte[0] (documento): '" + partes[0] + "'");
                System.out.println("[DEBUG] Presenter - Parte[1] (proyecto): '" + partes[1] + "'");
                
                if(partes[1].equals("NUEVO_PROYECTO")){
                    System.out.println("[LOG] Presenter - Descargando documento temporal: " + partes[0]);
                    descargarDocumentoTemp(partes[0]);
                } else {
                    System.out.println("[LOG] Presenter - Descargando documento de proyecto existente: " + partes[0] + " del proyecto: " + partes[1]);
                    descargarDocumento(partes[0], partes[1]);
                }
            } else {
                System.out.println("[ERROR] Presenter - Comando DESCARGAR_DOCUMENTO mal formado: " + comando);
            }
        }
        else if (comando.startsWith("ELIMINAR_DOCUMENTO/")) {
            String[] partes = comando.substring("ELIMINAR_DOCUMENTO/".length()).split("/");
            System.out.println("[LOG] Presenter - ELIMINAR_DOCUMENTO procesado. Partes: [" + String.join(", ", partes) + "]");
            if(partes[1].equals("NUEVO_PROYECTO")){
                System.out.println("[LOG] Presenter - Eliminando documento '" + partes[0] + "' del proyecto temporal");
                // Mostrar cantidad antes de eliminar
                List<String> documentosAntes = gestorProyecto.getNombresDocumentosProyectoTemporal();
                System.out.println("[LOG] Presenter - Documentos antes de eliminar: " + documentosAntes.size());
                
                gestorProyecto.eliminarDocumentoProyectoTemporal(partes[0]);
                
                // Mostrar cantidad después de eliminar
                List<String> documentosDespues = gestorProyecto.getNombresDocumentosProyectoTemporal();
                System.out.println("[LOG] Presenter - Documentos después de eliminar: " + documentosDespues.size());
                System.out.println("[LOG] Presenter - Lista actualizada: " + documentosDespues);
                
                vista.showMessage("Documento eliminado");
                System.out.println("[LOG] Presenter - Actualizando vista de documentos...");
                verDocumentosNuevoProyecto();
            }else{
                if (partes.length >= 2) {
                eliminarDocumento(partes[0], partes[1]);
            }
            }
        }
        else if (comando.startsWith("ELIMINAR_ACTIVIDAD/")) {
            String[] partes = comando.substring("ELIMINAR_ACTIVIDAD/".length()).split("/");
            System.out.println("[LOG] Presenter - ELIMINAR_ACTIVIDAD procesado. Partes: [" + String.join(", ", partes) + "]");
            if(partes[1].equals("NUEVO_PROYECTO")){
                System.out.println("[LOG] Presenter - Eliminando actividad '" + partes[0] + "' del proyecto temporal");
                // Mostrar cantidad antes de eliminar
                List<String> actividadesAntes = gestorProyecto.getNombresActividadesProyectoTemporal();
                System.out.println("[LOG] Presenter - Actividades antes de eliminar: " + actividadesAntes.size());
                
                gestorProyecto.eliminarActividadProyectoTemporal(partes[0]);
                
                // Mostrar cantidad después de eliminar
                List<String> actividadesDespues = gestorProyecto.getNombresActividadesProyectoTemporal();
                System.out.println("[LOG] Presenter - Actividades después de eliminar: " + actividadesDespues.size());
                System.out.println("[LOG] Presenter - Lista actualizada: " + actividadesDespues);
                
                vista.showMessage("Actividad eliminada");
                System.out.println("[LOG] Presenter - Actualizando vista de actividades...");
                verActividadesNuevoProyecto();
            }else{
                if (partes.length >= 2) {
                    // Para proyectos existentes, necesitamos establecer el proyecto actual
                    this.proyectoActual = partes[1];
                    eliminarActividad(partes[0]);
                }
            }
        }else if (comando.startsWith("VER_PROYECTO/")) {
            String nombre = comando.substring("VER_PROYECTO/".length());
            verProyecto(nombre);
        }
       
        else if (comando.startsWith("GUARDAR_ACTIVIDAD/")) {
            String nombreProyecto = comando.substring("GUARDAR_ACTIVIDAD/".length());
            if ("NUEVO_PROYECTO".equals(nombreProyecto)) {
                System.out.println("[LOG] Presenter - Guardar actividad de nuevo proyecto");
                
            } else {
                System.out.println("[LOG] Presenter - Guardar actividad en proyecto existente: " + nombreProyecto);
                guardarActividad();
            }
        }
        else if (comando.equals("ELIMINAR_PROYECTO")) {
            eliminarProyecto();
        }
        else if (comando.startsWith("ELIMINAR_PROYECTO/")) {
            String nombre = comando.substring("ELIMINAR_PROYECTO/".length());
            if (!nombre.isEmpty()) {
                proyectoActual = nombre;
                eliminarProyecto();
            }
        }
        else if (comando.equals("REGISTRAR_ACTIVIDAD")) {
            crearActividad();
        }
        else if (comando.equals("VER_ACTIVIDADES")) {
            verActividades();
        }
        else if (comando.startsWith("VER_ACTIVIDAD/")) {
            String nombre = comando.substring("VER_ACTIVIDAD/".length());
            verActividad(nombre);
        }
        else if (comando.startsWith("VER_ACTIVIDAD_TEMPORAL/")) {
            String nombre = comando.substring("VER_ACTIVIDAD_TEMPORAL/".length());
            System.out.println("[LOG] Presenter - Ver actividad temporal: " + nombre);
            verActividadTemporal(nombre);
        }
        else if (comando.startsWith("EDITAR_ACTIVIDAD_TEMPORAL/")) {
            String nombre = comando.substring("EDITAR_ACTIVIDAD_TEMPORAL/".length());
            System.out.println("[LOG] Presenter - Editar actividad temporal: " + nombre);
            editarActividadTemporal(nombre);
        }
        else if (comando.equals("CREAR_ACTIVIDAD")) {
            guardarActividad();
        }
        else if (comando.equals("EDITAR_ACTIVIDAD")) {
            guardarCambiosActividad();
        }
        else if (comando.startsWith("EDITAR_ACTIVIDAD/")) {
            String nombreProyecto = comando.substring("EDITAR_ACTIVIDAD/".length());
            System.out.println("[LOG] Presenter - Editar actividad de proyecto existente: " + nombreProyecto);
            guardarCambiosActividad();
        }
        else if (comando.equals("EDITAR_ACTIVIDAD")) {
            // Comando sin proyecto específico (comportamiento anterior)
            guardarCambiosActividad();
        }
        else if (comando.startsWith("VOLVER_VER_ACTIVIDADES")) {
            if (comando.contains("/")) {
                String nombreProyecto = comando.substring(comando.indexOf("/") + 1);
                System.out.println("[LOG] Presenter - Volver a ver actividades del proyecto: " + nombreProyecto);
                if (nombreProyecto.equals("NUEVO_PROYECTO")) {
                    verActividadesNuevoProyecto();
                } else {
                    proyectoActual = nombreProyecto;
                    verActividades();
                }
            } else {
                // Comando sin proyecto específico (comportamiento anterior)
                verActividades();
            }
        }
        
        // Documentos
        else if (comando.equals("PANEL_SUBIR_DOCUMENTO")) {
            subirDocumento();
        }
        else if (comando.equals("VER_DOCUMENTOS")) {
            verDocumentos();
        }

        else if (comando.equals("GUARDAR_DOCUMENTO")) {
            guardarDocumento();
        }
        else if (comando.startsWith("GUARDAR_DOCUMENTO/")) {
            String nombre = comando.substring("GUARDAR_DOCUMENTO/".length());
            if (!nombre.isEmpty() && !nombre.equals("NUEVO_PROYECTO")) {
                // Solo manejar proyectos existentes aquí
                // NUEVO_PROYECTO ya se maneja en el switch case arriba
                guardarDocumento();
            }
        }
        else if (comando.startsWith("LIMPIAR_ARCHIVO")) {
            // Manejado directamente por la vista SubirDocumento
        }
        
        // Comandos adicionales que faltaban
        else if (comando.startsWith("EDITAR_PROYECTO")) {
            String nombre = comando.substring("EDITAR_PROYECTO".length());
            if (!nombre.isEmpty()) {
                verProyecto(nombre);
            }
        }
        else if (comando.startsWith("VOLVER_VER_PROYECTOS")) {
            verProyectos();
        }
        else if (comando.startsWith("VOLVER_VER_PROYECTO/")) {
            String nombreProyecto = comando.substring("VOLVER_VER_PROYECTO/".length());
            System.out.println("[LOG] Presenter - VOLVER_VER_PROYECTO procesado. Proyecto: '" + nombreProyecto + "'");
            if (nombreProyecto.equals("NUEVO_PROYECTO")) {
                System.out.println("[LOG] Presenter - Volviendo a crear proyecto temporal");
                volverACrearProyecto();
            } else {
                System.out.println("[LOG] Presenter - Volviendo a ver proyecto existente: " + nombreProyecto);
                verProyecto(nombreProyecto);
            }
        }
        else if (comando.startsWith("PANEL_VER_ACTIVIDADES")) {
            String nombre = comando.substring("PANEL_VER_ACTIVIDADES".length());
            if (!nombre.isEmpty() && !nombre.equals("_NUEVO_PROYECTO")) {
                proyectoActual = nombre;
                verActividades();
            }
        }
        else if (comando.startsWith("PANEL_VER_DOCUMENTOS") && !comando.equals("PANEL_VER_DOCUMENTOS_NUEVO_PROYECTO")) {
            if (comando.contains("/")) {
                String nombre = comando.substring(comando.indexOf("/") + 1);
                if (!nombre.isEmpty()) {
                    proyectoActual = nombre;
                    verDocumentos();
                }
            } else {
                verDocumentos();
            }
        }
        else if (comando.equals("PANEL_SUBIR_DOCUMENTO")) {
            if (proyectoActual != null) {
                subirDocumento();
            }
        }
        else if (comando.equals("BUSCAR_PROYECTO")) {
            buscarProyectos();
        }
        else if (comando.equals("BUSCAR_ACTIVIDAD")) {
            buscarActividades();
        }
        else if (comando.equals("BUSCAR_DOCUMENTO")) {
            buscarDocumentos();
        }
        else if (comando.startsWith("VOLVER_PROYECTO")) {
            String nombre = comando.substring("VOLVER_PROYECTO/".length());
            if (!nombre.isEmpty()) {
                proyectoActual = nombre;
                verProyecto(nombre);
            }
        }
        else if (comando.equals("PANEL_REGISTRAR_ACTIVIDAD")) {
            if (proyectoActual != null) {
                crearActividad();
            } else {
                System.out.println("[LOG] Presenter - ERROR: No hay proyecto seleccionado para registrar actividad");
                vista.showErrorMessage("Debe seleccionar un proyecto primero");
            }
        }
        else {
            System.out.println("[LOG] Presenter - Comando no reconocido: " + comando);
        }
    }
    private void guardarProyectoTemporal(){
        System.out.println("[LOG] Presenter.subirDocumentoNuevoProyeto() - Yendo a subir documento de nuevo proyecto");
        String nombreTemp = vista.getNombreProyecto();
        Date fechaInicioTemp = vista.getFechaInicioProyecto();
        Date fechaFinTemp = vista.getFechaFinProyecto();
        String estadoTemp = vista.getEstadoProyecto();
        String descripcionTemp = vista.getDescripcionProyecto();
        gestorProyecto.guardarProyectoTemporal(nombreTemp, fechaInicioTemp, fechaFinTemp, estadoTemp, descripcionTemp);
    }
    private void subirDocumentoNuevoProyeto() {
        this.guardarProyectoTemporal();
        vista.subirDocumento(tiposDocumento, "NUEVO_PROYECTO");
        //gestorProyecto.subirDocumentoNuevoProyecto();
        System.out.println("[LOG] Presenter.subirDocumentoNuevoProyeto() - Completado");
    }
    private void guardarDocumentoNuevoProyecto() {
        System.out.println("[LOG] Presenter.guardarDocumentoNuevoProyecto() - Iniciando guardado");
        
        try {
            String nombreDocumento = vista.getNombreDocumento();
            String tipoDocumento = vista.getTipoDocumento();
            String rutaArchivo = vista.getRutaArchivoDocumento();
            
            validarDatosDocumento(nombreDocumento, tipoDocumento, rutaArchivo);
            gestorProyecto.guardarDocumentoNuevoProyecto(nombreDocumento, tipoDocumento, rutaArchivo);
            
            System.out.println("[LOG] Presenter.guardarDocumentoNuevoProyecto() - Documento guardado exitosamente");
            vista.showMessage("Documento guardado exitosamente.");
            volverACrearProyecto();
            
        } catch (IllegalArgumentException e) {
            System.err.println("[ERROR] Presenter.guardarDocumentoNuevoProyecto() - Error de validación: " + e.getMessage());
            // El mensaje ya se mostró en la validación
        } catch (Exception e) {
            System.err.println("[ERROR] Presenter.guardarDocumentoNuevoProyecto() - Error inesperado: " + e.getMessage());
            vista.showErrorMessage("Error inesperado al guardar el documento: " + e.getMessage());
        }
    }
    private void volverACrearProyecto() {
        System.out.println("[LOG] Presenter.volverACrearProyecto() - Volviendo a crear proyecto");
        vista.crearProyecto();
        vista.setNombreProyecto(gestorProyecto.getProyectoTemporal().getNombre());
        vista.setFechaInicioProyecto(gestorProyecto.getProyectoTemporal().getFechaInicio());
        vista.setFechaFinProyecto(gestorProyecto.getProyectoTemporal().getFechaFin());
        vista.setEstadoProyecto(gestorProyecto.getProyectoTemporal().getEstado().toString());
        vista.setDescripcionProyecto(gestorProyecto.getProyectoTemporal().getDescripcion());
        // Limpiar paneles de edición para evitar que se lean datos antiguos
        vista.limpiarPanelEditarActividad();
        System.out.println("[LOG] Presenter.volverACrearProyecto() - Completado");
    }
    private void verDocumentosNuevoProyecto() {
        System.out.println("[LOG] Presenter.verDocumentosNuevoProyecto() - Iniciando");
        this.guardarProyectoTemporal();
        List<String> nombresDocumentos = gestorProyecto.getNombresDocumentosProyectoTemporal();
        System.out.println("[LOG] Presenter.verDocumentosNuevoProyecto() - Encontrados " + nombresDocumentos.size() + " documentos");
        vista.verDocumentos(nombresDocumentos, "NUEVO_PROYECTO");
        System.out.println("[LOG] Presenter.verDocumentosNuevoProyecto() - Completado");
    }
    private void descargarDocumentoTemp(String nombreDocumento) {
        System.out.println("[LOG] Presenter.descargarDocumentoTemp() - Iniciando descarga: " + nombreDocumento);
        
        // Permitir al usuario seleccionar carpeta de descarga
        String rutaDestino = vista.seleccionarCarpetaDescarga();
        if (rutaDestino == null) {
            System.out.println("[LOG] Presenter.descargarDocumentoTemp() - Descarga cancelada por el usuario");
            return;
        }
        
        System.out.println("[LOG] Presenter.descargarDocumentoTemp() - Carpeta seleccionada: " + rutaDestino);
        
        try {
            gestorProyecto.descargarDocumentoTemp(nombreDocumento, rutaDestino);
            vista.showMessage("Documento descargado exitosamente en: " + rutaDestino);
            System.out.println("[LOG] Presenter.descargarDocumentoTemp() - Completado exitosamente");
        } catch (Exception e) {
            System.err.println("[ERROR] Presenter.descargarDocumentoTemp() - Error al descargar: " + e.getMessage());
            vista.showErrorMessage("Error al descargar el documento: " + e.getMessage());
        }
    }
    private void guardarProyecto() {
        System.out.println("[LOG] Presenter.guardarProyecto() - Iniciando");
        
        // 0) Guardar proyecto temporal (actualiza datos del proyecto en memoria)
        this.guardarProyectoTemporal();
        System.out.println("[LOG] Presenter.guardarProyecto() - Proyecto temporal actualizado");
        
        // 1) Leer valores desde la vista y sanear entradas de texto
        String nombreRaw = vista.getNombreProyecto();
        String nombre = (nombreRaw != null) ? nombreRaw.trim() : null;

        Date fechaInicio = vista.getFechaInicioProyecto();
        Date fechaFin = vista.getFechaFinProyecto();

        String descripcionRaw = vista.getDescripcionProyecto();
        String descripcion = (descripcionRaw != null) ? descripcionRaw.trim() : null;

        String estadoRaw = vista.getEstadoProyecto();
        String estado = (estadoRaw != null) ? estadoRaw.trim() : null;

        System.out.println("[LOG] Presenter.guardarProyecto() - Datos leídos: nombre=" + nombre);
        System.out.println("[LOG] Presenter.guardarProyecto() - Fechas: inicio=" + fechaInicio + ", fin=" + fechaFin);
        System.out.println("[LOG] Presenter.guardarProyecto() - Estado: " + estado + ", Descripción: " + descripcion);

        // 2) Validaciones de obligatorios: nombre, ambas fechas, descripcion y estado
        if (nombre == null || nombre.isEmpty()) {
            System.out.println("[LOG] Presenter.guardarProyecto() - ERROR: Nombre vacío");
            vista.showErrorMessage("El nombre del proyecto es obligatorio.");
            return;
        }
        if (fechaInicio == null ) {
            System.out.println("[LOG] Presenter.guardarProyecto() - ERROR: Fecha inicio vacía");
            vista.showErrorMessage("Debe ingresar la fecha de inicio y del proyecto.");
            return;
        }
        if (descripcion == null || descripcion.isEmpty()) {
            System.out.println("[LOG] Presenter.guardarProyecto() - ERROR: Descripción vacía");
            vista.showErrorMessage("La descripción del proyecto es obligatoria.");
            return;
        }
        if (estado == null || estado.isEmpty()) {
            System.out.println("[LOG] Presenter.guardarProyecto() - ERROR: Estado vacío");
            vista.showErrorMessage("El estado del proyecto es obligatorio.");
            return;
        }

        // 3) Validación de consistencia de fechas
        if (fechaFin != null && fechaFin.before(fechaInicio)) {
            System.out.println("[LOG] Presenter.guardarProyecto() - ERROR: Fechas inconsistentes");
            vista.showErrorMessage("La fecha de finalización no puede ser anterior a la fecha de inicio.");
            return;
        }

        // 4) Validación de unicidad del nombre (ya saneado)
        if (gestorProyecto.proyectoExiste(nombre)) {
            System.out.println("[LOG] Presenter.guardarProyecto() - ERROR: Proyecto ya existe");
            vista.showErrorMessage("El nombre del proyecto ya existe. Por favor, ingrese otro nombre.");
            return;
        }

        // 5) Persistencia
        System.out.println("[LOG] Presenter.guardarProyecto() - Guardando proyecto en BD");
        System.out.println("[LOG] Presenter.guardarProyecto() - Actividades temporales: " + gestorProyecto.getNombresActividadesProyectoTemporal().size());
        System.out.println("[LOG] Presenter.guardarProyecto() - Documentos temporales: " + gestorProyecto.getNombresDocumentosProyectoTemporal().size());
        
        if (gestorProyecto.agregarProyecto(nombre, fechaInicio, fechaFin, estado, descripcion)) {
            System.out.println("[LOG] Presenter.guardarProyecto() - Proyecto guardado exitosamente");
            vista.showMessage("Proyecto creado exitosamente.");
            vista.bienvenida();
        } else {
            System.out.println("[LOG] Presenter.guardarProyecto() - ERROR: No se pudo guardar");
            vista.showErrorMessage("No se pudo crear el proyecto. Por favor, intente de nuevo.");
        }
    }

    public void crearProyecto() {
        System.out.println("[LOG] Presenter.crearProyecto() - Iniciando");
        vista.crearProyecto();
        System.out.println("[LOG] Presenter.crearProyecto() - Completado");
    }
    
    private void verProyectos() {
        System.out.println("[LOG] Presenter.verProyectos() - Iniciando");
        // Cargar proyectos bajo demanda (solo cuando el usuario los pide)
        gestorProyecto.cargarTodoDesdeBD();
        List<String> nombresProyectos = gestorProyecto.getNombresProyectos();
        System.out.println("[LOG] Presenter.verProyectos() - Encontrados " + nombresProyectos.size() + " proyectos");
        vista.verProyectos(nombresProyectos);
        System.out.println("[LOG] Presenter.verProyectos() - Completado");
    }
    
    private void verProyecto(String nombreProyecto) {
        System.out.println("[LOG] Presenter.verProyecto() - Buscando proyecto: " + nombreProyecto);
        this.proyectoActual = nombreProyecto;
        Proyecto proyecto = gestorProyecto.buscarProyectoPorNombre(nombreProyecto);
        
        if (proyecto == null) {
            System.out.println("[LOG] Presenter.verProyecto() - ERROR: Proyecto no encontrado");
            vista.showErrorMessage("Proyecto no encontrado: " + nombreProyecto);
            return;
        }
        
        System.out.println("[LOG] Presenter.verProyecto() - Proyecto encontrado: " + proyecto.getNombre());
        // Preparar datos del proyecto para la vista (formato dd-MM-yyyy como en actividades)
        List<String> datosProyecto = new java.util.ArrayList<>();
        datosProyecto.add(proyecto.getNombre());
        
        // Formatear fecha inicio correctamente para dd-MM-yyyy
        String fechaInicioFormateada = "";
        if (proyecto.getFechaInicio() != null) {
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            fechaInicioFormateada = formato.format(proyecto.getFechaInicio());
        }
        datosProyecto.add(fechaInicioFormateada);
        
        // Formatear fecha fin correctamente para dd-MM-yyyy
        String fechaFinFormateada = "";
        if (proyecto.getFechaFin() != null) {
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            fechaFinFormateada = formato.format(proyecto.getFechaFin());
        }
        datosProyecto.add(fechaFinFormateada);
        
        datosProyecto.add(proyecto.getEstado().toString());
        datosProyecto.add(proyecto.getDescripcion());
        
        vista.editarProyecto(datosProyecto);
        System.out.println("[LOG] Presenter.verProyecto() - Completado");
    }
    
    private void guardarCambiosProyecto() {
        System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Iniciando para proyecto: " + proyectoActual);
        if (proyectoActual == null) {
            System.out.println("[LOG] Presenter.guardarCambiosProyecto() - ERROR: No hay proyecto seleccionado");
            vista.showErrorMessage("No hay proyecto seleccionado");
            return;
        }
        
        // Leer valores desde la vista
        String nombreRaw = vista.getNombreProyecto();
        String nombre = (nombreRaw != null) ? nombreRaw.trim() : null;
        Date fechaInicio = vista.getFechaInicioProyecto();
        Date fechaFin = vista.getFechaFinProyecto();
        String descripcionRaw = vista.getDescripcionProyecto();
        String descripcion = (descripcionRaw != null) ? descripcionRaw.trim() : null;
        String estadoRaw = vista.getEstadoProyecto();
        String estado = (estadoRaw != null) ? estadoRaw.trim() : null;
        
        System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Datos leídos");
        
        // Validaciones
        if (nombre == null || nombre.isEmpty()) {
            System.out.println("[LOG] Presenter.guardarCambiosProyecto() - ERROR: Nombre vacío");
            vista.showErrorMessage("El nombre del proyecto es obligatorio.");
            return;
        }
        if (fechaInicio == null) {
            System.out.println("[LOG] Presenter.guardarCambiosProyecto() - ERROR: Fecha inicio vacía");
            vista.showErrorMessage("La fecha de inicio es obligatoria.");
            return;
        }
        if (descripcion == null || descripcion.isEmpty()) {
            System.out.println("[LOG] Presenter.guardarCambiosProyecto() - ERROR: Descripción vacía");
            vista.showErrorMessage("La descripción es obligatoria.");
            return;
        }
        if (estado == null || estado.isEmpty()) {
            System.out.println("[LOG] Presenter.guardarCambiosProyecto() - ERROR: Estado vacío");
            vista.showErrorMessage("El estado es obligatorio.");
            return;
        }
        if (fechaFin != null && fechaFin.before(fechaInicio)) {
            System.out.println("[LOG] Presenter.guardarCambiosProyecto() - ERROR: Fechas inconsistentes");
            vista.showErrorMessage("La fecha de finalización no puede ser anterior a la fecha de inicio.");
            return;
        }
        
        // Actualizar en el modelo
        System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Actualizando campos en BD");
        
        // Actualizar nombre si cambió
        if (!nombre.equals(proyectoActual)) {
            System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Actualizando nombre de '" + proyectoActual + "' a '" + nombre + "'");
            gestorProyecto.actualizarProyectoCampo(proyectoActual, "nombre", nombre);
            System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Actualización nombre completada");
            proyectoActual = nombre; // Actualizar referencia
            System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Referencia interna actualizada a: " + proyectoActual);
        }
        
        System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Actualizando descripción: '" + descripcion + "'");
        gestorProyecto.actualizarProyectoCampo(proyectoActual, "descripcion", descripcion);
        System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Actualización descripción completada");
        
        System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Actualizando fecha inicio: " + fechaInicio);
        gestorProyecto.actualizarProyectoCampo(proyectoActual, "fecha_inicio", fechaInicio);
        System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Actualización fecha inicio completada");
        
        if (fechaFin != null) {
            System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Actualizando fecha fin: " + fechaFin);
            gestorProyecto.actualizarProyectoCampo(proyectoActual, "fecha_fin", fechaFin);
            System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Actualización fecha fin completada");
        }
        
        System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Actualizando estado: '" + estado + "'");
        gestorProyecto.actualizarProyectoCampo(proyectoActual, "estado", estado);
        System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Actualización estado completada");
        
        System.out.println("[LOG] Presenter.guardarCambiosProyecto() - Proyecto actualizado exitosamente");
        vista.showMessage("Proyecto actualizado exitosamente.");
        vista.bienvenida();
    }
    
    private void eliminarProyecto() {
        System.out.println("[LOG] Presenter.eliminarProyecto() - Iniciando para proyecto: " + proyectoActual);
        if (proyectoActual == null) {
            System.out.println("[LOG] Presenter.eliminarProyecto() - ERROR: No hay proyecto seleccionado");
            vista.showErrorMessage("No hay proyecto seleccionado");
            return;
        }
        
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
            vista,
            "¿Está seguro de que desea eliminar el proyecto '" + proyectoActual + "'?",
            "Confirmar eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            System.out.println("[LOG] Presenter.eliminarProyecto() - Eliminando proyecto");
            gestorProyecto.eliminarProyectoConDependencias(proyectoActual);
            System.out.println("[LOG] Presenter.eliminarProyecto() - Proyecto eliminado exitosamente");
            vista.showMessage("Proyecto eliminado exitosamente.");
            proyectoActual = null;
            vista.bienvenida();
        } else {
            System.out.println("[LOG] Presenter.eliminarProyecto() - Eliminación cancelada por usuario");
        }
    }
    
    private void cancelar() {
        vista.bienvenida();
    }
    
    // ==================== ACTIVIDADES ====================
    
    private void crearActividad() {
        if (proyectoActual == null) {
            vista.showErrorMessage("No hay proyecto seleccionado");
            return;
        }
        
        String[] tiposActividad = {"CAPACITACION", "ASISTENCIA_TECNICA", "DOTACION_EQUIPOS", "SEGUIMIENTO", "SOCIALIZACION"};
        vista.crearActividad(tiposActividad, proyectoActual);
    }
    
    private void verActividades() {
        System.out.println("[LOG] Presenter.verActividades() - Iniciando para proyecto: " + proyectoActual);
        if (proyectoActual == null) {
            System.out.println("[LOG] Presenter.verActividades() - ERROR: No hay proyecto seleccionado");
            vista.showErrorMessage("No hay proyecto seleccionado");
            return;
        }
        
        List<Actividad> actividades = gestorProyecto.consultarActividadesDeProyecto(proyectoActual);
        List<String> nombresActividades = new java.util.ArrayList<>();
        
        for (Actividad act : actividades) {
            nombresActividades.add(act.getNombre());
        }
        
        System.out.println("[LOG] Presenter.verActividades() - Encontradas " + nombresActividades.size() + " actividades");
        vista.verActividades(nombresActividades, proyectoActual);
        System.out.println("[LOG] Presenter.verActividades() - Completado");
    }
    
    private void verActividad(String nombreActividad) {
        if (proyectoActual == null) {
            vista.showErrorMessage("No hay proyecto seleccionado");
            return;
        }
        
        this.actividadActual = nombreActividad;
        Actividad actividad = gestorProyecto.consultarActividad(proyectoActual, nombreActividad);
        
        if (actividad == null) {
            vista.showErrorMessage("Actividad no encontrada: " + nombreActividad);
            return;
        }
        
        // Preparar datos de la actividad (orden correcto para EditarActividad)
        List<String> datosActividad = new java.util.ArrayList<>();
        datosActividad.add(actividad.getNombre());                    // índice 0: Nombre
        datosActividad.add(actividad.getDescripcion());               // índice 1: Descripción
        // Formatear fecha correctamente para dd/MM/yyyy
        String fechaFormateada = "";
        if (actividad.getFecha() != null) {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            fechaFormateada = formato.format(actividad.getFecha());
        }
        datosActividad.add(fechaFormateada);                          // índice 2: Fecha
        datosActividad.add(actividad.getTipo().toString());           // índice 3: Tipo
        
        String[] tiposActividad = {"CAPACITACION", "ASISTENCIA_TECNICA", "DOTACION_EQUIPOS", "SEGUIMIENTO", "SOCIALIZACION"};
        vista.editarActividad(tiposActividad, proyectoActual, datosActividad);
    }
    
    private void guardarActividad() {
        if (proyectoActual == null) {
            vista.showErrorMessage("No hay proyecto seleccionado");
            return;
        }
        
        // Leer valores desde la vista
        String nombreRaw = vista.getNombreActividad();
        String nombre = (nombreRaw != null) ? nombreRaw.trim() : null;
        Date fecha = vista.getFechaActividad();
        String tipoRaw = vista.getTipoActividad();
        String tipo = (tipoRaw != null) ? tipoRaw.trim() : null;
        String descripcionRaw = vista.getDescripcionActividad();
        String descripcion = (descripcionRaw != null) ? descripcionRaw.trim() : null;
        
        // Validaciones
        if (nombre == null || nombre.isEmpty()) {
            vista.showErrorMessage("El nombre de la actividad es obligatorio.");
            return;
        }
        if (fecha == null) {
            vista.showErrorMessage("La fecha de la actividad es obligatoria.");
            return;
        }
        if (tipo == null || tipo.isEmpty()) {
            vista.showErrorMessage("El tipo de actividad es obligatorio.");
            return;
        }
        if (descripcion == null || descripcion.isEmpty()) {
            vista.showErrorMessage("La descripción de la actividad es obligatoria.");
            return;
        }
        
        // Registrar actividad
        try {
            co.edu.uptc.model.TipoActividad tipoActividad = co.edu.uptc.model.TipoActividad.valueOf(tipo.toUpperCase());
            gestorProyecto.registrarActividad(proyectoActual, nombre, descripcion, tipoActividad, fecha);
            vista.showMessage("Actividad creada exitosamente.");
            verActividades();
        } catch (IllegalArgumentException e) {
            vista.showErrorMessage("Tipo de actividad inválido: " + tipo);
        }
    }
    
    private void guardarCambiosActividad() {
        if (proyectoActual == null || actividadActual == null) {
            vista.showErrorMessage("No hay actividad seleccionada");
            return;
        }
        
        // Leer valores desde la vista
        String nombreRaw = vista.getNombreActividad();
        String nombre = (nombreRaw != null) ? nombreRaw.trim() : null;
        Date fecha = vista.getFechaActividad();
        String tipoRaw = vista.getTipoActividad();
        String tipo = (tipoRaw != null) ? tipoRaw.trim() : null;
        String descripcionRaw = vista.getDescripcionActividad();
        String descripcion = (descripcionRaw != null) ? descripcionRaw.trim() : null;
        
        // Validaciones
        if (nombre == null || nombre.isEmpty()) {
            vista.showErrorMessage("El nombre de la actividad es obligatorio.");
            return;
        }
        if (fecha == null) {
            vista.showErrorMessage("La fecha de la actividad es obligatoria.");
            return;
        }
        if (tipo == null || tipo.isEmpty()) {
            vista.showErrorMessage("El tipo de actividad es obligatorio.");
            return;
        }
        if (descripcion == null || descripcion.isEmpty()) {
            vista.showErrorMessage("La descripción de la actividad es obligatoria.");
            return;
        }
        
        // Actualizar actividad
        try {
            System.out.println("[LOG] Presenter.guardarCambiosActividad() - Actualizando campos en BD");
            
            // Actualizar nombre si cambió
            if (!nombre.equals(actividadActual)) {
                System.out.println("[LOG] Presenter.guardarCambiosActividad() - Actualizando nombre de '" + actividadActual + "' a '" + nombre + "'");
                gestorProyecto.actualizarActividadCampo(proyectoActual, actividadActual, "nombre", nombre);
                System.out.println("[LOG] Presenter.guardarCambiosActividad() - Actualización nombre completada");
                actividadActual = nombre; // Actualizar referencia
                System.out.println("[LOG] Presenter.guardarCambiosActividad() - Referencia interna actualizada a: " + actividadActual);
            }
            
            System.out.println("[LOG] Presenter.guardarCambiosActividad() - Actualizando descripción: '" + descripcion + "'");
            gestorProyecto.actualizarActividadCampo(proyectoActual, actividadActual, "descripcion", descripcion);
            System.out.println("[LOG] Presenter.guardarCambiosActividad() - Actualización descripción completada");
            
            System.out.println("[LOG] Presenter.guardarCambiosActividad() - Actualizando tipo: '" + tipo + "'");
            gestorProyecto.actualizarActividadCampo(proyectoActual, actividadActual, "tipo", tipo);
            System.out.println("[LOG] Presenter.guardarCambiosActividad() - Actualización tipo completada");
            
            System.out.println("[LOG] Presenter.guardarCambiosActividad() - Actualizando fecha: " + fecha);
            gestorProyecto.actualizarActividadCampo(proyectoActual, actividadActual, "fecha", fecha);
            System.out.println("[LOG] Presenter.guardarCambiosActividad() - Actualización fecha completada");
            
            System.out.println("[LOG] Presenter.guardarCambiosActividad() - Actividad actualizada exitosamente");
            vista.showMessage("Actividad actualizada exitosamente.");
            verActividades();
        } catch (Exception e) {
            System.err.println("[LOG] Presenter.guardarCambiosActividad() - ERROR: " + e.getMessage());
            vista.showErrorMessage("Error al actualizar la actividad: " + e.getMessage());
        }
    }
    
    private void eliminarActividad(String nombreActividad) {
        if (proyectoActual == null) {
            vista.showErrorMessage("No hay proyecto seleccionado");
            return;
        }
        
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
            null,
            "¿Está seguro de que desea eliminar la actividad '" + nombreActividad + "'?",
            "Confirmar eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            gestorProyecto.eliminarActividad(proyectoActual, nombreActividad);
            vista.showMessage("Actividad eliminada exitosamente.");
            actividadActual = null;
            verActividades();
        }
    }
    
    // ==================== DOCUMENTOS ====================
    
    private void subirDocumento() {
        System.out.println("[LOG] Presenter.subirDocumento() - Iniciando para proyecto: " + proyectoActual);

        
        System.out.println("[LOG] Presenter.subirDocumento() - Mostrando formulario subir documento");
        vista.subirDocumento(tiposDocumento, proyectoActual);
        System.out.println("[LOG] Presenter.subirDocumento() - Completado");
    }
    
    private void verDocumentos() {
        System.out.println("[LOG] Presenter.verDocumentos() - Iniciando para proyecto: " + proyectoActual);
        if (proyectoActual == null) {
            System.out.println("[LOG] Presenter.verDocumentos() - ERROR: No hay proyecto seleccionado");
            vista.showErrorMessage("No hay proyecto seleccionado");
            return;
        }
        
        List<String> documentos = gestorProyecto.obtenerNombresDocumentosDeProyecto(proyectoActual);
        System.out.println("[LOG] Presenter.verDocumentos() - Encontrados " + documentos.size() + " documentos");
        vista.verDocumentos(documentos, proyectoActual);
        System.out.println("[LOG] Presenter.verDocumentos() - Completado");
    }
    //se debe escoger la ruta
    private void descargarDocumento(String nombreDocumento, String nombreProyecto) {
        String rutaDestino = vista.seleccionarCarpetaDescarga();
        gestorProyecto.descargarDocumento(nombreProyecto, nombreDocumento, rutaDestino);
        vista.showMessage("Documento descargado exitosamente en: " + rutaDestino);
    }
    
    private void eliminarDocumento(String nombreDocumento, String nombreProyecto) {
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
            null,
            "¿Está seguro de que desea eliminar el documento '" + nombreDocumento + "'?",
            "Confirmar eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            gestorProyecto.eliminarDocumento(nombreProyecto, nombreDocumento);
            vista.showMessage("Documento eliminado exitosamente.");
            verDocumentos();
        }
    }
    
    private void guardarDocumento() {
        if (proyectoActual == null) {
            vista.showErrorMessage("No hay proyecto seleccionado");
            return;
        }
        
        // Leer valores desde la vista
        String nombreRaw = vista.getNombreDocumento();
        String nombre = (nombreRaw != null) ? nombreRaw.trim() : null;
        String tipoRaw = vista.getTipoDocumento();
        String tipo = (tipoRaw != null) ? tipoRaw.trim() : null;
        String rutaArchivo = vista.getRutaArchivoDocumento();
        
        // Validaciones
        validarDatosDocumento(nombre, tipo, rutaArchivo);
        
        // Registrar documento
        System.out.println("[LOG] Presenter.guardarDocumento() - Registrando documento:");
        System.out.println("   - Proyecto: " + proyectoActual);
        System.out.println("   - Nombre: " + nombre);
        System.out.println("   - Tipo: " + tipo);
        System.out.println("   - Ruta: " + rutaArchivo);
        
        gestorProyecto.registrarDocumento(proyectoActual, nombre, tipo, rutaArchivo);
        vista.showMessage("Documento guardado exitosamente.");
        this.verDocumentos();
    }
    private void validarDatosDocumento(String nombre, String tipo, String rutaArchivo) {
        System.out.println("[LOG] Presenter.validarDatosDocumento() - Validando: nombre='" + nombre + "', tipo='" + tipo + "', ruta='" + rutaArchivo + "'");
        
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("[ERROR] Presenter.validarDatosDocumento() - Nombre vacío");
            vista.showErrorMessage("El nombre del documento es obligatorio.");
            throw new IllegalArgumentException("El nombre del documento es obligatorio.");
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            System.out.println("[ERROR] Presenter.validarDatosDocumento() - Tipo vacío");
            vista.showErrorMessage("El tipo de documento es obligatorio.");
            throw new IllegalArgumentException("El tipo de documento es obligatorio.");
        }
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            System.out.println("[ERROR] Presenter.validarDatosDocumento() - Ruta de archivo vacía");
            vista.showErrorMessage("Debe seleccionar un archivo.");
            throw new IllegalArgumentException("Debe seleccionar un archivo.");
        }
        
        // Verificar que el archivo existe
        java.io.File archivo = new java.io.File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("[ERROR] Presenter.validarDatosDocumento() - Archivo no existe: " + rutaArchivo);
            vista.showErrorMessage("El archivo seleccionado no existe.");
            throw new IllegalArgumentException("El archivo seleccionado no existe.");
        }
        
        System.out.println("[LOG] Presenter.validarDatosDocumento() - Validación exitosa");
    }

    
    private void buscarProyectos() {
        System.out.println("[LOG] Presenter.buscarProyectos() - Iniciando búsqueda");
        
        String textoBusqueda = vista.getTextoBusquedaProyectos();
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            System.out.println("[LOG] Presenter.buscarProyectos() - Texto de búsqueda vacío, mostrando todos");
            verProyectos();
            return;
        }
        
        textoBusqueda = textoBusqueda.trim().toLowerCase();
        System.out.println("[LOG] Presenter.buscarProyectos() - Buscando: '" + textoBusqueda + "'");
        
        // Cargar todos los proyectos
        gestorProyecto.cargarTodoDesdeBD();
        List<String> todosLosProyectos = gestorProyecto.getNombresProyectos();
        
        // Filtrar proyectos que contienen el texto de búsqueda
        List<String> proyectosFiltrados = new java.util.ArrayList<>();
        for (String proyecto : todosLosProyectos) {
            if (proyecto.toLowerCase().contains(textoBusqueda)) {
                proyectosFiltrados.add(proyecto);
            }
        }
        
        System.out.println("[LOG] Presenter.buscarProyectos() - Encontrados " + proyectosFiltrados.size() + " proyectos");
        vista.verProyectos(proyectosFiltrados);
        
        if (proyectosFiltrados.isEmpty()) {
            vista.showInfoMessage("No se encontraron proyectos que contengan: '" + textoBusqueda + "'");
        }
    }
    
    private void buscarActividades() {
        System.out.println("[LOG] Presenter.buscarActividades() - Iniciando búsqueda");
        
        if (proyectoActual == null) {
            vista.showErrorMessage("No hay proyecto seleccionado");
            return;
        }
        
        String textoBusqueda = vista.getTextoBusquedaActividades();
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            System.out.println("[LOG] Presenter.buscarActividades() - Texto de búsqueda vacío, mostrando todas");
            verActividades();
            return;
        }
        
        textoBusqueda = textoBusqueda.trim().toLowerCase();
        System.out.println("[LOG] Presenter.buscarActividades() - Buscando: '" + textoBusqueda + "' en proyecto: " + proyectoActual);
        
        // Obtener todas las actividades del proyecto
        List<Actividad> actividades = gestorProyecto.consultarActividadesDeProyecto(proyectoActual);
        List<String> todasLasActividades = new java.util.ArrayList<>();
        for (Actividad act : actividades) {
            todasLasActividades.add(act.getNombre());
        }
        
        // Filtrar actividades que contienen el texto de búsqueda
        List<String> actividadesFiltradas = new java.util.ArrayList<>();
        for (String actividad : todasLasActividades) {
            if (actividad.toLowerCase().contains(textoBusqueda)) {
                actividadesFiltradas.add(actividad);
            }
        }
        
        System.out.println("[LOG] Presenter.buscarActividades() - Encontradas " + actividadesFiltradas.size() + " actividades");
        vista.verActividades(actividadesFiltradas, proyectoActual);
        
        if (actividadesFiltradas.isEmpty()) {
            vista.showInfoMessage("No se encontraron actividades que contengan: '" + textoBusqueda + "'");
        }
    }
    
    private void buscarDocumentos() {
        System.out.println("[LOG] Presenter.buscarDocumentos() - Iniciando búsqueda");
        
        if (proyectoActual == null) {
            vista.showErrorMessage("No hay proyecto seleccionado");
            return;
        }
        
        String textoBusqueda = vista.getTextoBusquedaDocumentos();
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            System.out.println("[LOG] Presenter.buscarDocumentos() - Texto de búsqueda vacío, mostrando todos");
            verDocumentos();
            return;
        }
        
        textoBusqueda = textoBusqueda.trim().toLowerCase();
        System.out.println("[LOG] Presenter.buscarDocumentos() - Buscando: '" + textoBusqueda + "' en proyecto: " + proyectoActual);
        
        // Obtener todos los documentos del proyecto
        List<String> todosLosDocumentos = gestorProyecto.obtenerNombresDocumentosDeProyecto(proyectoActual);
        
        // Filtrar documentos que contienen el texto de búsqueda
        List<String> documentosFiltrados = new java.util.ArrayList<>();
        for (String documento : todosLosDocumentos) {
            if (documento.toLowerCase().contains(textoBusqueda)) {
                documentosFiltrados.add(documento);
            }
        }
        
        System.out.println("[LOG] Presenter.buscarDocumentos() - Encontrados " + documentosFiltrados.size() + " documentos");
        vista.verDocumentos(documentosFiltrados, proyectoActual);
        
        if (documentosFiltrados.isEmpty()) {
            vista.showInfoMessage("No se encontraron documentos que contengan: '" + textoBusqueda + "'");
        }
    }

    // Métodos para manejar actividades temporales
    private void agregarActividadNuevoProyecto() {
        System.out.println("[LOG] Presenter.agregarActividadNuevoProyecto() - Iniciando");
        this.guardarProyectoTemporal();
        System.out.println("[LOG] Presenter.agregarActividadNuevoProyecto() - Creando nuevo panel CrearActividad");
        vista.crearActividad(tiposActividad, "NUEVO_PROYECTO");
        System.out.println("[LOG] Presenter.agregarActividadNuevoProyecto() - Panel CrearActividad mostrado");
        System.out.println("[LOG] Presenter.agregarActividadNuevoProyecto() - Completado");
    }

    private void guardarActividadNuevoProyecto() {
        System.out.println("[LOG] Presenter.guardarActividadNuevoProyecto() - Iniciando guardado");
        
        try {
            // Obtener datos de la vista
            String nombre = vista.getNombreActividad();
            Date fecha = vista.getFechaActividad();
            String tipo = vista.getTipoActividad();
            String descripcion = vista.getDescripcionActividad();
            
            System.out.println("[DEBUG] Presenter.guardarActividadNuevoProyecto() - Datos leídos:");
            System.out.println("   - Nombre: '" + nombre + "'");
            System.out.println("   - Tipo: '" + tipo + "'");
            System.out.println("   - Fecha: " + fecha);
            System.out.println("   - Descripción: '" + descripcion + "'");
            
            // Validar datos
            if (!validarDatosActividad(nombre, fecha, tipo, descripcion)) {
                return;
            }
            
            // Guardar en temporal
            gestorProyecto.guardarActividadNuevoProyecto(nombre, fecha, tipo, descripcion);
            
            System.out.println("[LOG] Presenter.guardarActividadNuevoProyecto() - Actividad guardada exitosamente");
            vista.showMessage("Actividad guardada exitosamente.");
            
            // Volver a la vista de crear proyecto (para que el usuario pueda agregar más actividades o documentos)
            volverACrearProyecto();
            
        } catch (Exception e) {
            System.out.println("[ERROR] Presenter.guardarActividadNuevoProyecto() - " + e.getMessage());
            vista.showErrorMessage("Error al guardar actividad: " + e.getMessage());
        }
    }

    private boolean validarDatosActividad(String nombre, Date fecha, String tipo, String descripcion) {
        System.out.println("[LOG] Presenter.validarDatosActividad() - Validando: nombre='" + nombre + "', tipo='" + tipo + "'");
        
        if (nombre == null || nombre.trim().isEmpty()) {
            vista.showErrorMessage("El nombre de la actividad es obligatorio.");
            return false;
        }
        
        if (fecha == null) {
            vista.showErrorMessage("La fecha de la actividad es obligatoria.");
            return false;
        }
        
        if (tipo == null || tipo.trim().isEmpty()) {
            vista.showErrorMessage("El tipo de actividad es obligatorio.");
            return false;
        }
        
        if (descripcion == null || descripcion.trim().isEmpty()) {
            vista.showErrorMessage("La descripción de la actividad es obligatoria.");
            return false;
        }
        
        System.out.println("[LOG] Presenter.validarDatosActividad() - Validación exitosa");
        return true;
    }

    private void verActividadesNuevoProyecto() {
        System.out.println("[LOG] Presenter.verActividadesNuevoProyecto() - Iniciando");
        this.guardarProyectoTemporal();
        List<String> nombresActividades = gestorProyecto.getNombresActividadesProyectoTemporal();
        System.out.println("[LOG] Presenter.verActividadesNuevoProyecto() - Encontradas " + nombresActividades.size() + " actividades");
        vista.verActividades(nombresActividades, "NUEVO_PROYECTO");
        System.out.println("[LOG] Presenter.verActividadesNuevoProyecto() - Completado");
    }

    private void verActividadTemporal(String nombreActividad) {
        System.out.println("[LOG] Presenter.verActividadTemporal() - Viendo actividad temporal: " + nombreActividad);
        
        // Obtener la actividad temporal
        Actividad actividad = gestorProyecto.getActividadTemporal(nombreActividad);
        
        if (actividad == null) {
            System.out.println("[ERROR] Presenter.verActividadTemporal() - Actividad temporal no encontrada: " + nombreActividad);
            vista.showErrorMessage("Actividad temporal no encontrada: " + nombreActividad);
            return;
        }
        
        // Establecer la actividad actual para navegación
        this.actividadActual = nombreActividad;
        
        // Convertir actividad a datos para la vista
        List<String> datosActividad = new java.util.ArrayList<>();
        datosActividad.add(actividad.getNombre());
        datosActividad.add(actividad.getDescripcion());
        
        // Formatear fecha
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        datosActividad.add(formatter.format(actividad.getFecha()));
        datosActividad.add(actividad.getTipo().toString());
        
        // Mostrar la vista de editar actividad (que sirve también para ver detalles)
        vista.editarActividad(tiposActividad, "NUEVO_PROYECTO", datosActividad);
        
        System.out.println("[LOG] Presenter.verActividadTemporal() - Completado");
    }

    private void editarActividadTemporal(String nombreActividad) {
        System.out.println("[LOG] Presenter.editarActividadTemporal() - Editando actividad temporal: " + nombreActividad);
        
        try {
            // Obtener la actividad temporal actual
            Actividad actividadAntigua = gestorProyecto.getActividadTemporal(nombreActividad);
            if (actividadAntigua == null) {
                vista.showErrorMessage("Actividad temporal no encontrada: " + nombreActividad);
                return;
            }
            
            // Obtener nuevos datos de la vista
            String nuevoNombre = vista.getNombreActividad();
            Date nuevaFecha = vista.getFechaActividad();
            String nuevoTipo = vista.getTipoActividad();
            String nuevaDescripcion = vista.getDescripcionActividad();
            
            System.out.println("[DEBUG] Presenter.editarActividadTemporal() - Datos leídos:");
            System.out.println("   - Nombre antiguo: '" + nombreActividad + "'");
            System.out.println("   - Nombre nuevo: '" + nuevoNombre + "'");
            System.out.println("   - Tipo: '" + nuevoTipo + "'");
            System.out.println("   - Fecha: " + nuevaFecha);
            System.out.println("   - Descripción: '" + nuevaDescripcion + "'");
            
            // Validar datos
            if (!validarDatosActividad(nuevoNombre, nuevaFecha, nuevoTipo, nuevaDescripcion)) {
                return;
            }
            
            // Crear una nueva actividad con los datos actualizados
            Actividad actividadActualizada = new Actividad(nuevoNombre, nuevaDescripcion, 
                    co.edu.uptc.model.TipoActividad.valueOf(nuevoTipo.toUpperCase()), nuevaFecha);
            
            // Actualizar en el gestor pasando el nombre antiguo para encontrarla
            gestorProyecto.actualizarActividadTemporal(nombreActividad, actividadActualizada);
            
            System.out.println("[LOG] Presenter.editarActividadTemporal() - Actividad actualizada en memoria");
            vista.showMessage("Actividad actualizada exitosamente.");
            
            // Volver a la vista de actividades temporales
            verActividadesNuevoProyecto();
            
        } catch (Exception e) {
            System.out.println("[ERROR] Presenter.editarActividadTemporal() - " + e.getMessage());
            e.printStackTrace();
            vista.showErrorMessage("Error al actualizar actividad: " + e.getMessage());
        }
    }


}