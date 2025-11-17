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
    private List<Actividad> actividadesProyectoTemporal;

    

    public Gestor() {
        this.proyectos = new ArrayList<>();
        this.documentosProyectoTemporal = new ArrayList<>();
        this.actividadesProyectoTemporal = new ArrayList<>();

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
                
                System.out.println("[LOG] Gestor.agregarProyecto() - Agregando " + documentosProyectoTemporal.size() + " documentos temporales al proyecto");
                for (Documento doc : documentosProyectoTemporal) {
                    proyecto.agregarDocumento(doc);
                    System.out.println("[LOG] Gestor.agregarProyecto() - Documento agregado: " + doc.getNombre());
                }
                
                System.out.println("[LOG] Gestor.agregarProyecto() - Agregando " + actividadesProyectoTemporal.size() + " actividades temporales al proyecto");
                for (Actividad act : actividadesProyectoTemporal) {
                    proyecto.registrarActividad(act);
                    System.out.println("[LOG] Gestor.agregarProyecto() - Actividad agregada: " + act.getNombre());
                }
                
                proyectos.add(proyecto);
                System.out.println("[LOG] Gestor.agregarProyecto() - Proyecto agregado a lista en memoria");
                registrarProyectoBD(proyecto);
                System.out.println("[LOG] Gestor.agregarProyecto() - Proyecto registrado en BD");
                
                this.documentosProyectoTemporal = new ArrayList<>();
                this.actividadesProyectoTemporal = new ArrayList<>();
                System.out.println("[LOG] Gestor.agregarProyecto() - Listas temporales limpiadas");
                return true;
            } else {
                Proyecto proyecto = new Proyecto(nombre, descripcion, fechaInicio, estadoP);
                
                System.out.println("[LOG] Gestor.agregarProyecto() - Agregando " + documentosProyectoTemporal.size() + " documentos temporales al proyecto");
                for (Documento doc : documentosProyectoTemporal) {
                    proyecto.agregarDocumento(doc);
                    System.out.println("[LOG] Gestor.agregarProyecto() - Documento agregado: " + doc.getNombre());
                }
                
                System.out.println("[LOG] Gestor.agregarProyecto() - Agregando " + actividadesProyectoTemporal.size() + " actividades temporales al proyecto");
                for (Actividad act : actividadesProyectoTemporal) {
                    proyecto.registrarActividad(act);
                    System.out.println("[LOG] Gestor.agregarProyecto() - Actividad agregada: " + act.getNombre());
                }
                
                proyectos.add(proyecto);
                System.out.println("[LOG] Gestor.agregarProyecto() - Proyecto agregado a lista en memoria");
                registrarProyectoBD(proyecto);
                System.out.println("[LOG] Gestor.agregarProyecto() - Proyecto registrado en BD");
                
                this.documentosProyectoTemporal = new ArrayList<>();
                this.actividadesProyectoTemporal = new ArrayList<>();
                System.out.println("[LOG] Gestor.agregarProyecto() - Listas temporales limpiadas");
                return true;
            }
        } else {
            this.documentosProyectoTemporal = new ArrayList<>();
            this.actividadesProyectoTemporal = new ArrayList<>();
            System.out.println("[LOG] Gestor.agregarProyecto() - Proyecto ya existe, listas temporales limpiadas");
            return false;
        }
    }

    private void registrarProyectoBD(Proyecto proyecto) {
        System.out.println("[LOG] Gestor.registrarProyectoBD() - Iniciando registro en BD");
        System.out.println("[LOG] Gestor.registrarProyectoBD() - Documentos en proyecto: " + proyecto.getDocumentos().size());
        for (Documento doc : proyecto.getDocumentos()) {
            System.out.println("[LOG] Gestor.registrarProyectoBD() - Documento en memoria: " + doc.getNombre());
        }
        System.out.println("[LOG] Gestor.registrarProyectoBD() - Actividades en proyecto: " + proyecto.getActividades().size());
        for (Actividad act : proyecto.getActividades()) {
            System.out.println("[LOG] Gestor.registrarProyectoBD() - Actividad en memoria: " + act.getNombre());
        }
        
        // 1) Registrar el proyecto
        ProyectoDAO pdao = new ProyectoDAO();
        boolean insertado = pdao.registrarProyecto(proyecto);

        if (!insertado) {
            System.out.println("[ERROR] Gestor.registrarProyectoBD() - Error al registrar el proyecto en la base de datos");
            return;
        }
        
        System.out.println("[LOG] Gestor.registrarProyectoBD() - Proyecto registrado exitosamente en BD");
        
        // 2) Registrar documentos del proyecto
        System.out.println("[LOG] Gestor.registrarProyectoBD() - Registrando " + proyecto.getDocumentos().size() + " documentos");
        DocumentoDAO documentoDAO = new DocumentoDAO();
        for (Documento doc : proyecto.getDocumentos()) {
            // Obtener el ID del proyecto que acabamos de insertar
            int proyectoId = pdao.obtenerIdProyectoPorNombre(proyecto.getNombre());
            if (proyectoId != -1) {
                boolean docRegistrado = documentoDAO.insertarDocumentoDesdeMemoria(proyectoId, doc);
                if (docRegistrado) {
                    System.out.println("[LOG] Gestor.registrarProyectoBD() - Documento '" + doc.getNombre() + "' registrado");
                } else {
                    System.err.println("[ERROR] Gestor.registrarProyectoBD() - Error al registrar documento '" + doc.getNombre() + "'");
                }
            }
        }
        
        // 3) Registrar actividades del proyecto
        System.out.println("[LOG] Gestor.registrarProyectoBD() - Registrando " + proyecto.getActividades().size() + " actividades");
        ActividadDAO actividadDAO = new ActividadDAO();
        for (Actividad act : proyecto.getActividades()) {
            boolean actRegistrada = actividadDAO.registrarActividad(proyecto.getNombre(), act);
            if (actRegistrada) {
                System.out.println("[LOG] Gestor.registrarProyectoBD() - Actividad '" + act.getNombre() + "' registrada");
            } else {
                System.err.println("[ERROR] Gestor.registrarProyectoBD() - Error al registrar actividad '" + act.getNombre() + "'");
            }
        }
        
        System.out.println("[LOG] Gestor.registrarProyectoBD() - Proyecto y sus relaciones registrados exitosamente en BD");
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
        System.out.println("[DEBUG] Gestor.obtenerNombresDocumentosDeProyecto() - nombreProyecto recibido: '" + nombreProyecto + "'");
        System.out.println("[DEBUG] Gestor.obtenerNombresDocumentosDeProyecto() - proyectoTemporal es null: " + (proyectoTemporal == null));
        if (proyectoTemporal != null) {
            System.out.println("[DEBUG] Gestor.obtenerNombresDocumentosDeProyecto() - proyectoTemporal.getNombre(): '" + proyectoTemporal.getNombre() + "'");
            System.out.println("[DEBUG] Gestor.obtenerNombresDocumentosDeProyecto() - Comparación equals: " + proyectoTemporal.getNombre().equals(nombreProyecto));
        }
        System.out.println("[DEBUG] Gestor.obtenerNombresDocumentosDeProyecto() - documentosProyectoTemporal.size(): " + documentosProyectoTemporal.size());
        
        // Si estamos editando un proyecto, trabajar solo con la lista temporal
        // (que ya contiene los documentos de BD cargados por guardarMemoriaProyectoActual)
        if (proyectoTemporal != null && proyectoTemporal.getNombre().toUpperCase().equals(nombreProyecto.toUpperCase())) {
            List<String> documentos = new ArrayList<>();
            for (Documento doc : documentosProyectoTemporal) {
                documentos.add(doc.getNombre());
            }
            System.out.println("[LOG] Gestor.obtenerNombresDocumentosDeProyecto() - Retornando documentos temporales: " + documentos.size());
            return documentos;
        }

        // Si no hay proyecto temporal activo, buscar en BD (caso de solo lectura)
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);
        if (proyecto == null) {
            System.out.println("No se encontró el proyecto '" + nombreProyecto);
            return new ArrayList<>();
        }

        List<String> documentos = proyecto.obtenerNombresDocumentosProyecto();
        System.out.println("[LOG] Gestor.obtenerNombresDocumentosDeProyecto() - Retornando documentos de BD: " + documentos.size());
        return documentos;
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
        System.out.println("[DEBUG] Gestor.consultarActividad() - Buscando actividad '" + nombreActividad + "' en proyecto '" + nombreProyecto + "'");
        
        // Si estamos editando un proyecto, buscar en la lista temporal
        if (proyectoTemporal != null && proyectoTemporal.getNombre().equalsIgnoreCase(nombreProyecto)) {
            System.out.println("[DEBUG] Gestor.consultarActividad() - Buscando en lista temporal (" + actividadesProyectoTemporal.size() + " actividades)");
            for (Actividad act : actividadesProyectoTemporal) {
                System.out.println("[DEBUG] Gestor.consultarActividad() - Comparando '" + act.getNombre() + "' con '" + nombreActividad + "'");
                if (act.getNombre().equalsIgnoreCase(nombreActividad)) {
                    System.out.println("[LOG] Gestor.consultarActividad() - Actividad encontrada en lista temporal");
                    return act;
                }
            }
            System.out.println("[LOG] Gestor.consultarActividad() - Actividad no encontrada en lista temporal");
            return null;
        }
        
        // Si no hay proyecto temporal, buscar en BD
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);
        if (proyecto == null) {
            System.out.println("El proyecto no existe.");
            return null;
        }

        for (Actividad act : proyecto.getActividades()) {
            if (act.getNombre().equalsIgnoreCase(nombreActividad)) {
                System.out.println("[LOG] Gestor.consultarActividad() - Actividad encontrada en BD");
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
        System.out.println("[DEBUG] Gestor.consultarActividadesDeProyecto() - nombreProyecto recibido: '" + nombreProyecto + "'");
        System.out.println("[DEBUG] Gestor.consultarActividadesDeProyecto() - proyectoTemporal es null: " + (proyectoTemporal == null));
        if (proyectoTemporal != null) {
            System.out.println("[DEBUG] Gestor.consultarActividadesDeProyecto() - proyectoTemporal.getNombre(): '" + proyectoTemporal.getNombre() + "'");
        }
        System.out.println("[DEBUG] Gestor.consultarActividadesDeProyecto() - actividadesProyectoTemporal.size(): " + actividadesProyectoTemporal.size());
        
        // Si estamos editando un proyecto, trabajar solo con la lista temporal
        // (que ya contiene las actividades de BD cargadas por guardarMemoriaProyectoActual)
        if (proyectoTemporal != null && proyectoTemporal.getNombre().equalsIgnoreCase(nombreProyecto)) {
            System.out.println("[LOG] Gestor.consultarActividadesDeProyecto() - Retornando actividades temporales: " + actividadesProyectoTemporal.size());
            return new ArrayList<>(actividadesProyectoTemporal);
        }

        // Si no hay proyecto temporal activo, buscar en BD (caso de solo lectura)
        Proyecto proyecto = buscarProyectoPorNombre(nombreProyecto);
        if (proyecto == null) {
            System.out.println("El proyecto no existe.");
            return new ArrayList<>();
        }

        List<Actividad> actividades = proyecto.getActividades();
        if (actividades.isEmpty()) {
            System.out.println("El proyecto no tiene actividades registradas.");
        }

        System.out.println("[LOG] Gestor.consultarActividadesDeProyecto() - Retornando actividades de BD: " + actividades.size());
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

    // Métodos para manejar actividades temporales
    public void guardarActividadNuevoProyecto(String nombre, Date fecha, String tipo, String descripcion) throws Exception {
        System.out.println("[LOG] Gestor.guardarActividadNuevoProyecto() - Parámetros: nombre='" + nombre + "', tipo='" + tipo + "'");
        
        // Validar que no exista una actividad con el mismo nombre
        for (Actividad act : actividadesProyectoTemporal) {
            if (act.getNombre().equalsIgnoreCase(nombre)) {
                String error = "Ya existe una actividad con el nombre '" + nombre + "' en el proyecto temporal";
                System.out.println("[ERROR] Gestor.guardarActividadNuevoProyecto() - " + error);
                throw new Exception(error);
            }
        }
        
        try {
            // Crear la actividad temporal
            TipoActividad tipoActividad = TipoActividad.valueOf(tipo.toUpperCase());
            Actividad actividadCreada = new Actividad(nombre, descripcion, tipoActividad, fecha);
            
            actividadesProyectoTemporal.add(actividadCreada);
            System.out.println("[LOG] Gestor.guardarActividadNuevoProyecto() - Actividad '" + nombre + "' agregada exitosamente. Total actividades temporales: " + actividadesProyectoTemporal.size());
            
        } catch (Exception e) {
            String error = "Error al crear actividad '" + nombre + "': " + e.getMessage();
            System.out.println("[ERROR] Gestor.guardarActividadNuevoProyecto() - " + error);
            throw new Exception(error);
        }
    }

    public List<String> getNombresActividadesProyectoTemporal() {
        System.out.println("[LOG] Gestor.getNombresActividadesProyectoTemporal() - Total actividades: " + actividadesProyectoTemporal.size());
        List<String> nombres = new ArrayList<>();
        for (Actividad act : this.actividadesProyectoTemporal) {
            nombres.add(act.getNombre());
            System.out.println("[LOG] Gestor.getNombresActividadesProyectoTemporal() - Actividad: '" + act.getNombre() + "', Tipo: '" + act.getTipo() + "'");
        }
        return nombres;
    }

    public void eliminarActividadProyectoTemporal(String nombre) {
        System.out.println("[DEBUG] Gestor.eliminarActividadProyectoTemporal() - Buscando actividad: '" + nombre + "'");
        for (int i = 0; i < actividadesProyectoTemporal.size(); i++) {
            String nombreActividad = actividadesProyectoTemporal.get(i).getNombre();
            System.out.println("[DEBUG] Gestor.eliminarActividadProyectoTemporal() - Comparando '" + nombreActividad + "' con '" + nombre + "'");
            if (nombreActividad.equalsIgnoreCase(nombre)) {
                actividadesProyectoTemporal.remove(i);
                System.out.println("[LOG] Gestor.eliminarActividadProyectoTemporal() - Actividad '" + nombreActividad + "' eliminada del proyecto temporal.");
                return;
            }
        }
        System.out.println("[LOG] Gestor.eliminarActividadProyectoTemporal() - Actividad '" + nombre + "' no encontrada en el proyecto temporal.");
    }

    public Actividad getActividadTemporal(String nombre) {
        for (Actividad act : actividadesProyectoTemporal) {
            if (act.getNombre().equalsIgnoreCase(nombre)) {
                return act;
            }
        }
        return null;
    }

    public void actualizarActividadTemporal(String nombreAntiguo, Actividad actividadActualizada) {
        if (actividadActualizada == null) {
            System.out.println("[ERROR] Gestor.actualizarActividadTemporal() - Actividad nula");
            return;
        }
        
        // Buscar la actividad en la lista temporal por el nombre antiguo y actualizar
        for (int i = 0; i < actividadesProyectoTemporal.size(); i++) {
            if (actividadesProyectoTemporal.get(i).getNombre().equalsIgnoreCase(nombreAntiguo)) {
                actividadesProyectoTemporal.set(i, actividadActualizada);
                System.out.println("[LOG] Gestor.actualizarActividadTemporal() - Actividad '" + nombreAntiguo + "' actualizada a '" + actividadActualizada.getNombre() + "' exitosamente");
                return;
            }
        }
        
        System.out.println("[ERROR] Gestor.actualizarActividadTemporal() - Actividad '" + nombreAntiguo + "' no encontrada en el proyecto temporal");
    }

    public void guardarMemoriaProyectoActual (String nombre){
        // Si ya hay un proyecto temporal activo con el mismo nombre, NO recargar
        // para evitar perder los cambios temporales (actividades/documentos agregados)
        if (proyectoTemporal != null && proyectoTemporal.getNombre().equalsIgnoreCase(nombre)) {
            System.out.println("[LOG] Gestor.guardarMemoriaProyectoActual() - Proyecto temporal ya activo para '" + nombre + "', conservando cambios temporales");
            return;
        }
        
        System.out.println("[LOG] Gestor.guardarMemoriaProyectoActual() - Cargando proyecto '" + nombre + "' en memoria temporal");
        this.proyectoTemporal = this.buscarProyectoPorNombre(nombre);
        this.documentosProyectoTemporal = new ArrayList<>(this.proyectoTemporal.getDocumentos());
        this.actividadesProyectoTemporal = new ArrayList<>(this.proyectoTemporal.getActividades());
        System.out.println("[LOG] Gestor.guardarMemoriaProyectoActual() - Cargados " + documentosProyectoTemporal.size() + " documentos y " + actividadesProyectoTemporal.size() + " actividades");
    }

    /**
     * Guarda una actividad en la lista temporal (para proyectos existentes en edición).
     * Similar a guardarActividadNuevoProyecto pero con parámetros en diferente orden.
     */
    public void guardarActividadProyectoTemporal(String nombre, String descripcion, TipoActividad tipo, Date fecha) throws Exception {
        System.out.println("[LOG] Gestor.guardarActividadProyectoTemporal() - Parámetros: nombre='" + nombre + "', tipo='" + tipo + "'");
        
        // Validar que no exista una actividad con el mismo nombre
        for (Actividad act : actividadesProyectoTemporal) {
            if (act.getNombre().equalsIgnoreCase(nombre)) {
                String error = "Ya existe una actividad con el nombre '" + nombre + "' en el proyecto temporal";
                System.out.println("[ERROR] Gestor.guardarActividadProyectoTemporal() - " + error);
                throw new Exception(error);
            }
        }
        
        try {
            // Crear la actividad temporal
            Actividad actividadCreada = new Actividad(nombre, descripcion, tipo, fecha);
            actividadesProyectoTemporal.add(actividadCreada);
            System.out.println("[LOG] Gestor.guardarActividadProyectoTemporal() - Actividad '" + nombre + "' agregada exitosamente. Total actividades temporales: " + actividadesProyectoTemporal.size());
            
        } catch (Exception e) {
            String error = "Error al crear actividad '" + nombre + "': " + e.getMessage();
            System.out.println("[ERROR] Gestor.guardarActividadProyectoTemporal() - " + error);
            throw new Exception(error);
        }
    }
}
