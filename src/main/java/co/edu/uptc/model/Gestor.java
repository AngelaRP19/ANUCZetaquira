package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.uptc.dao.ActividadDAO;
import co.edu.uptc.dao.DocumentoDAO;
import co.edu.uptc.dao.ProyectoDAO;

public class Gestor {

    private List<Proyecto> proyectos;
    private Proyecto proyectoTemporal;
    private List<Documento> documentosProyectoTemporal;

    public Gestor() {
        this.proyectos = new ArrayList<>();
        this.documentosProyectoTemporal = new ArrayList<>();

    }

    public void cargarTodoDesdeBD() {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        ActividadDAO actividadDAO = new ActividadDAO();
        DocumentoDAO documentoDAO = new DocumentoDAO();

        proyectos.clear();
        List<Proyecto> proyectosBD = proyectoDAO.obtenerTodosLosProyectos();

        for (Proyecto proyecto : proyectosBD) {
            List<Actividad> actividades = actividadDAO.obtenerActividadesPorProyecto(proyecto.getIdentificador());
            proyecto.cargarActividades(actividades);
            List<Documento> documentos = documentoDAO.obtenerDocumentosPorProyecto(proyecto.getIdentificador());
            proyecto.cargarDocumentos(documentos);
            proyectos.add(proyecto);
        }
        System.out.println("Datos cargados correctamente desde la base de datos ("
                + proyectos.size() + " proyectos con sus actividades y documentos).");
    }

    public boolean agregarProyecto(String nombre, Date fechaInicio, Date fechaFin, String estado, String descripcion) {
        if (!proyectoExiste(nombre)) {
            EstadoProyecto estadoP = EstadoProyecto.valueOf(estado.toUpperCase());
            if (fechaFin != null) {
                Proyecto proyecto = new Proyecto(nombre, descripcion, fechaInicio, fechaFin, estadoP);
                for (Documento doc : documentosProyectoTemporal) {
                    proyecto.agregarDocumento(doc);
                }
                proyectos.add(proyecto);
                System.out.println("Proyecto agregado exitosamente.");
                registrarProyectoBD(proyecto);
                System.out.println("Proyecto agregado exitosamente a la bd.");
                 this.documentosProyectoTemporal = new ArrayList<>();
                return true;
            } else {
                Proyecto proyecto = new Proyecto(nombre, descripcion, fechaInicio, estadoP);
                for (Documento doc : documentosProyectoTemporal) {
                    proyecto.agregarDocumento(doc);
                }
                proyectos.add(proyecto);
                System.out.println("Proyecto agregado exitosamente.");
                registrarProyectoBD(proyecto);
                System.out.println("Proyecto agregado exitosamente a la bd.");
                 this.documentosProyectoTemporal = new ArrayList<>();
                return true;
            }
        } else {
            this.documentosProyectoTemporal = new ArrayList<>();
            return false;
        }
    }

    private void registrarProyectoBD(Proyecto proyecto) {
        ProyectoDAO pdao = new ProyectoDAO();
        boolean insertado = pdao.registrarProyecto(proyecto);

        if (insertado) {
            System.out.println("Proyecto registrado exitosamente.");
        } else {
            System.out.println("Error al registrar el proyecto en la base de datos.");
        }
    }

    public void eliminarProyectoConDependencias(String nombreProyecto) {
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);

        if (proyecto == null) {
            System.out.println("El proyecto '" + nombreProyecto + "' no existe.");
            return;
        }

        DocumentoDAO documentoDAO = new DocumentoDAO();
        List<Documento> documentos = proyecto.eliminarDocumentosProyecto();
        for (Documento doc : documentos) {
            boolean eliminadoBD = documentoDAO.eliminarDocumento(doc.getNombre());
            if (eliminadoBD) {
                System.out.println("Documento '" + doc.getNombre() + "' eliminado de la BD.");
            } else {
                System.out.println("No se pudo eliminar el documento '" + doc.getNombre() + "' de la BD.");
            }
        }

        ActividadDAO actividadDAO = new ActividadDAO();
        List<Actividad> actividades = proyecto.getActividades();
        for (Actividad act : actividades) {
            boolean eliminadoBD = actividadDAO.eliminarActividad(act.getNombre(), proyecto.getIdentificador());
            if (eliminadoBD) {
                System.out.println("Actividad '" + act.getNombre() + "' eliminada de la BD.");
            } else {
                System.out.println("No se pudo eliminar la actividad '" + act.getNombre() + "' de la BD.");
            }
        }

        proyecto.getActividades().clear();

        ProyectoDAO proyectoDAO = new ProyectoDAO();
        boolean eliminadoProyecto = proyectoDAO.eliminarProyecto(proyecto.getIdentificador());
        if (eliminadoProyecto) {
            proyectos.remove(proyecto);
            System.out.println("Proyecto '" + nombreProyecto + "' eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar el proyecto '" + nombreProyecto);
        }
    }

    public List<String> obtenerNombresDocumentosDeProyecto(String nombreProyecto) {
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);

        if (proyecto == null) {
            System.out.println("No se encontró el proyecto '" + nombreProyecto);
            return new ArrayList<>();
        }

        return proyecto.obtenerNombresDocumentosProyecto();
    }

    private void eliminarProyectoBD(Proyecto proyecto) {
        ProyectoDAO dao = new ProyectoDAO();
        boolean eliminado = dao.eliminarProyecto(proyecto.getIdentificador());

        if (!eliminado) {
            System.out.println("Error al eliminar el proyecto de la base de datos.");
        }
    }

    public void actualizarProyectoCampo(String nombreProyecto, String campo, Object nuevoValor) {
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);

        if (proyecto == null) {
            System.out.println("El proyecto no existe.");
            return;
        }
        switch (campo.toLowerCase()) {
            case "descripcion":
                proyecto.setDescripcion((String) nuevoValor);
                break;
            case "fecha_inicio":
                if (nuevoValor instanceof Date) {
                    proyecto.setFechaInicio((Date) nuevoValor);
                } else {
                    System.out.println("Valor no válido para fecha de inicio.");
                    return;
                }
                break;
            case "fecha_fin":
                if (nuevoValor instanceof Date) {
                    proyecto.setFechaFin((Date) nuevoValor);
                } else {
                    proyecto.setFechaFin(null);
                }
                break;
            case "estado":
                proyecto.setEstado(EstadoProyecto.valueOf(((String) nuevoValor).toUpperCase()));
                break;
            default:
                System.out.println("Campo no válido.");
                return;
        }

        ProyectoDAO dao = new ProyectoDAO();
        Object valorDAO = nuevoValor;

        if (nuevoValor instanceof java.util.Date) {
            valorDAO = new java.sql.Date(((Date) nuevoValor).getTime());
        }

        boolean actualizado = dao.actualizarCampoProyecto(proyecto.getIdentificador(), campo, valorDAO);

        if (actualizado) {
            System.out.println("Campo '" + campo + "' actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar el campo en la base de datos.");
        }
    }

    public List<Proyecto> consultarTodosLosProyectos() {
        return new ArrayList<>(proyectos);
    }

    public Proyecto buscarProyectoPorNombre(String nombre) {
        for (Proyecto p : proyectos) {
            if (p.getNombre().equalsIgnoreCase(nombre))
                return p;
        }
        return null;
    }

    public boolean proyectoExiste(String nombre) {
        for (Proyecto p : proyectos) {
            if (p.getNombre().equals(nombre))
                return true;
        }
        return false;
    }

    public void registrarActividad(String nombreProyecto, String nombre, String descripcion, TipoActividad tipo,
            Date fecha) {
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);
        if (!validarProyectoYActividad(proyecto, nombre, tipo, fecha))
            return;

        Actividad actividad = new Actividad(nombre, descripcion, tipo, fecha);
        registrarActividadMemoria(proyecto, actividad);
        registrarActividadBD(proyecto, actividad);
    }

    public void eliminarActividad(String nombreProyecto, String nombre) {
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);
        if (!validarProyectoYNombre(proyecto, nombre))
            return;

        eliminarActividadBD(proyecto, nombre);
        eliminarActividadMemoria(proyecto, nombre);
    }

    public Actividad consultarActividad(String nombreProyecto, String nombreActividad) {
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);

        if (proyecto == null) {
            System.out.println("El proyecto no existe.");
            return null;
        }

        for (Actividad act : proyecto.getActividades()) {
            if (act.getNombre().equalsIgnoreCase(nombreActividad)) {
                return act;
            }
        }

        System.out.println("No se encontró una actividad con ese nombre en el proyecto.");
        return null;
    }

    public void actualizarActividadCampo(String nombreProyecto, String nombreActividad, String campo,
            Object nuevoValor) {
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);

        if (proyecto == null) {
            System.out.println("El proyecto '" + nombreProyecto + "' no existe.");
            return;
        }

        boolean actualizadoEnMemoria = proyecto.actualizarCampoEnMemoria(nombreActividad, campo, nuevoValor);

        if (!actualizadoEnMemoria) {
            System.out.println("No se pudo actualizar en memoria.");
            return;
        }

        ActividadDAO dao = new ActividadDAO();

        Object valorDAO = nuevoValor;
        if (nuevoValor instanceof java.util.Date) {
            valorDAO = new java.sql.Date(((Date) nuevoValor).getTime());
        }

        boolean actualizado = dao.actualizarCampoActividad(nombreActividad, campo, valorDAO);

        if (actualizado) {
            System.out.println("Campo '" + campo + "' actualizado correctamente en la base de datos.");
        } else {
            System.out.println("Error al actualizar el campo '" + campo + "' en la base de datos.");
        }
    }

    public List<Actividad> consultarActividadesDeProyecto(String nombreProyecto) {
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);

        if (proyecto == null) {
            System.out.println("El proyecto no existe.");
            return new ArrayList<>();
        }

        List<Actividad> actividades = proyecto.getActividades();

        if (actividades.isEmpty()) {
            System.out.println("El proyecto no tiene actividades registradas.");
        }

        return actividades;
    }

    private boolean validarProyectoYActividad(Proyecto proyecto, String nombre, TipoActividad tipo, Date fecha) {
        if (proyecto == null) {
            System.out.println("Proyecto no encontrado.");
            return false;
        }
        if (nombre == null || nombre.isEmpty()) {
            System.out.println("El nombre de la actividad es obligatorio.");
            return false;
        }
        if (tipo == null) {
            System.out.println("Debe seleccionar un tipo de actividad.");
            return false;
        }
        if (fecha == null) {
            System.out.println("Debe ingresar una fecha válida.");
            return false;
        }
        return true;
    }

    private boolean validarProyectoYNombre(Proyecto proyecto, String nombre) {
        if (proyecto == null) {
            System.out.println("Proyecto no encontrado.");
            return false;
        }
        if (nombre == null || nombre.isEmpty()) {
            System.out.println("El nombre de la actividad es obligatorio.");
            return false;
        }
        return true;
    }

    private void registrarActividadMemoria(Proyecto proyecto, Actividad actividad) {
        proyecto.registrarActividad(actividad);
    }

    private void registrarActividadBD(Proyecto proyecto, Actividad actividad) {
        ActividadDAO dao = new ActividadDAO();
        boolean insertado = dao.registrarActividad(proyecto.getNombre(), actividad);

        if (insertado) {
            System.out.println("Actividad registrada exitosamente.");
        } else {
            System.out.println("Error al registrar la actividad en la base de datos.");
        }
    }

    private void eliminarActividadMemoria(Proyecto proyecto, String nombre) {
        boolean eliminado = proyecto.eliminarActividad(nombre);
        if (eliminado) {
            System.out.println("Actividad eliminada exitosamente.");
        } else {
            System.out.println("Error al eliminar la actividad en el proyecto.");
        }
    }

    private void eliminarActividadBD(Proyecto proyecto, String nombre) {
        ActividadDAO dao = new ActividadDAO();
        boolean eliminado = dao.eliminarActividad(nombre, proyecto.getIdentificador());

        if (!eliminado) {
            System.out.println("Error al eliminar la actividad de la base de datos.");
        }
    }

    public void registrarDocumento(String nombreProyecto, String nombre, String tipo, String rutaArchivo) {
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);

        if (proyecto == null) {
            System.out.println("El proyecto '" + nombreProyecto + "' no existe.");
            return;
        }

        proyecto.registrarDocumento(nombre, tipo, rutaArchivo);

        DocumentoDAO dao = new DocumentoDAO();
        boolean insertado = dao.insertarDocumento(proyecto.getIdentificador(), nombre, tipo, rutaArchivo);

        if (insertado) {
            System.out.println("Documento '" + nombre + "' guardado correctamente en la base de datos.");
        } else {
            System.out.println("Error al guardar el documento en la base de datos.");
        }
    }

    public void descargarDocumento(String nombreProyecto, String nombreDocumento, String rutaDestino) {
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);

        if (proyecto == null) {
            System.out.println("El proyecto '" + nombreProyecto + "' no existe.");
            return;
        }

        boolean descargado = proyecto.descargarDocumento(nombreDocumento, rutaDestino);

        if (descargado) {
            System.out.println("El documento '" + nombreDocumento + "' se descargó correctamente.");
        } else {
            System.out.println("No se pudo descargar el documento '" + nombreDocumento + "'.");
        }
    }

    public void eliminarDocumento(String nombreProyecto, String nombreDocumento) {
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);

        if (proyecto == null) {
            System.out.println("El proyecto '" + nombreProyecto + "' no existe.");
            return;
        }

        boolean eliminadoMemoria = proyecto.eliminarDocumentoEnMemoria(nombreDocumento);

        if (eliminadoMemoria) {
            DocumentoDAO dao = new DocumentoDAO();
            boolean eliminadoBD = dao.eliminarDocumento(nombreDocumento);

            if (eliminadoBD) {
                System.out.println("Documento '" + nombreDocumento + "' eliminado correctamente.");
            } else {
                System.out.println("Error al eliminar el documento en la base de datos.");
            }
        } else {
            System.out.println("No se encontró el documento '" + nombreDocumento + "' en memoria.");
        }
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public List<String> getNombresProyectos() {
        List<String> nombres = new ArrayList<>();
        for (Proyecto p : proyectos) {
            nombres.add(p.getNombre());
        }
        return nombres;
    }

    public void guardarProyectoTemporal(String nombre, Date fechaInicio, Date fechaFin, String estado, String descripcion) {
        EstadoProyecto estadoP = EstadoProyecto.valueOf(estado.toUpperCase());
        proyectoTemporal = new Proyecto(nombre, descripcion, fechaInicio, fechaFin, estadoP);
        
        System.out.println("[LOG] Gestor.guardarProyectoTemporal() - Proyecto temporal guardado.");
    }
    public List<String> getDatosTemporales() {
        List<String> datos = new ArrayList<>();
        if (proyectoTemporal != null) {
            datos.add(proyectoTemporal.getNombre());
            datos.add(proyectoTemporal.getFechaInicio().toString());
            datos.add(proyectoTemporal.getFechaFin() != null ? proyectoTemporal.getFechaFin().toString() : "null");
            datos.add(proyectoTemporal.getEstado().toString());
            datos.add(proyectoTemporal.getDescripcion());
            System.out.println("[LOG] Gestor.getDatosTemporales() - Datos temporales obtenidos.");
        } else {
            System.out.println("[LOG] Gestor.getDatosTemporales() - No hay proyecto temporal guardado.");
        }
        return datos;
    }
    public void guardarDocumentoNuevoProyecto(String nombreDocumento, String tipoDocumento, String rutaArchivo) {
        System.out.println("[LOG] Gestor.guardarDocumentoNuevoProyecto() - Parámetros: nombre='" + nombreDocumento + "', tipo='" + tipoDocumento + "', ruta='" + rutaArchivo + "'");
        System.out.println("[DEBUG] Gestor.guardarDocumentoNuevoProyecto() - Longitud del nombre: " + nombreDocumento.length());
        System.out.println("[DEBUG] Gestor.guardarDocumentoNuevoProyecto() - Caracteres del nombre: " + java.util.Arrays.toString(nombreDocumento.toCharArray()));
        
        Documento docCreado = proyectoTemporal.registrarDocumento(nombreDocumento, tipoDocumento, rutaArchivo);
        if (docCreado != null) {
            documentosProyectoTemporal.add(docCreado);
            System.out.println("[LOG] Gestor.guardarDocumentoNuevoProyecto() - Documento '" + nombreDocumento + "' agregado exitosamente. Total documentos temporales: " + documentosProyectoTemporal.size());
            
            // Verificar que se guardó correctamente
            System.out.println("[DEBUG] Gestor.guardarDocumentoNuevoProyecto() - Nombre almacenado en el documento: '" + docCreado.getNombre() + "'");
            System.out.println("[DEBUG] Gestor.guardarDocumentoNuevoProyecto() - Caracteres del nombre almacenado: " + java.util.Arrays.toString(docCreado.getNombre().toCharArray()));
        } else {
            System.out.println("[ERROR] Gestor.guardarDocumentoNuevoProyecto() - No se pudo crear el documento");
        }
    }
    public List<String> getNombresDocumentosProyectoTemporal() {
        System.out.println("[LOG] Gestor.getNombresDocumentosProyectoTemporal() - Total documentos: " + documentosProyectoTemporal.size());
        List<String> nombres = new ArrayList<>();
        for (Documento doc : this.documentosProyectoTemporal) {
            System.out.println("[LOG] Gestor.getNombresDocumentosProyectoTemporal() - Documento: '" + doc.getNombre() + "', Extensión: '" + doc.getExtension() + "'");
            nombres.add(doc.getNombre());
        }
        return nombres;
    }
    public Proyecto getProyectoTemporal() {
        return proyectoTemporal;
    }

    public void eliminarDocumentoProyectoTemporal(String nombre) {
        System.out.println("[DEBUG] Gestor.eliminarDocumentoProyectoTemporal() - Buscando documento: '" + nombre + "'");
        for (int i = 0; i < documentosProyectoTemporal.size(); i++) {
            String nombreDocumento = documentosProyectoTemporal.get(i).getNombre();
            System.out.println("[DEBUG] Gestor.eliminarDocumentoProyectoTemporal() - Comparando '" + nombreDocumento + "' con '" + nombre + "'");
            if (nombreDocumento.equalsIgnoreCase(nombre)) {
                documentosProyectoTemporal.remove(i);
                System.out.println("[LOG] Gestor.eliminarDocumentoProyectoTemporal() - Documento '" + nombreDocumento + "' eliminado del proyecto temporal.");
                return;
            }
        }
        System.out.println("[LOG] Gestor.eliminarDocumentoProyectoTemporal() - Documento '" + nombre + "' no encontrado en el proyecto temporal.");
    }

    public void descargarDocumentoTemp(String nombreDocumento, String rutaDestino) throws Exception {
        System.out.println("[LOG] Gestor.descargarDocumentoTemp() - Buscando documento: '" + nombreDocumento + "'");
        System.out.println("[DEBUG] Gestor.descargarDocumentoTemp() - Longitud del nombre buscado: " + nombreDocumento.length());
        System.out.println("[DEBUG] Gestor.descargarDocumentoTemp() - Caracteres del nombre buscado: " + java.util.Arrays.toString(nombreDocumento.toCharArray()));
        System.out.println("[LOG] Gestor.descargarDocumentoTemp() - Total documentos temporales: " + documentosProyectoTemporal.size());
        
        for (Documento doc : documentosProyectoTemporal) {
            String nombreAlmacenado = doc.getNombre().toUpperCase();
            System.out.println("[LOG] Gestor.descargarDocumentoTemp() - Comparando con: '" + nombreAlmacenado + "'");
            System.out.println("[DEBUG] Gestor.descargarDocumentoTemp() - Longitud del nombre almacenado: " + nombreAlmacenado.length());
            System.out.println("[DEBUG] Gestor.descargarDocumentoTemp() - Caracteres del nombre almacenado: " + java.util.Arrays.toString(nombreAlmacenado.toCharArray()));
            System.out.println("[DEBUG] Gestor.descargarDocumentoTemp() - ¿Son iguales? " + nombreAlmacenado.equals(nombreDocumento));
            
            if (nombreAlmacenado.equals(nombreDocumento.toUpperCase())) {
                System.out.println("[LOG] Gestor.descargarDocumentoTemp() - Documento encontrado, descargando...");
                
                // Obtener el contenido del documento
                byte[] contenidoArchivo = doc.getArchivo();
                if (contenidoArchivo == null || contenidoArchivo.length == 0) {
                    String error = "El documento '" + nombreDocumento + "' no tiene contenido";
                    System.out.println("[ERROR] Gestor.descargarDocumentoTemp() - " + error);
                    throw new Exception(error);
                }
                
                // Crear directorio destino si no existe
                java.io.File directorioDestino = new java.io.File(rutaDestino);
                if (!directorioDestino.exists()) {
                    boolean creado = directorioDestino.mkdirs();
                    if (!creado) {
                        String error = "No se pudo crear el directorio destino: " + rutaDestino;
                        System.out.println("[ERROR] Gestor.descargarDocumentoTemp() - " + error);
                        throw new Exception(error);
                    }
                }
                
                // Crear archivo destino con extensión
                String nombreArchivo = nombreDocumento;
                if (doc.getExtension() != null && !doc.getExtension().isEmpty()) {
                    nombreArchivo += "." + doc.getExtension();
                }
                java.io.File archivoDestino = new java.io.File(directorioDestino, nombreArchivo);
                
                // Escribir contenido al archivo
                java.nio.file.Files.write(archivoDestino.toPath(), contenidoArchivo);
                
                System.out.println("[LOG] Gestor.descargarDocumentoTemp() - Documento descargado a: " + archivoDestino.getAbsolutePath());
                return;
            }
        }
        
        String error = "Documento '" + nombreDocumento + "' no encontrado en el proyecto temporal";
        System.out.println("[ERROR] Gestor.descargarDocumentoTemp() - " + error);
        throw new Exception(error);
    }
}
