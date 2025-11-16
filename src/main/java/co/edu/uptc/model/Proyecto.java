package co.edu.uptc.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import co.edu.uptc.dao.DocumentoDAO;

public class Proyecto {

    private int identificador;
    private String nombre;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private EstadoProyecto estado;
    private List<Documento> documentos;
    private List<Actividad> actividades;

    public Proyecto(String nombre, String descripcion, Date fechaInicio, EstadoProyecto estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
        this.documentos = new ArrayList<>();
        this.actividades = new ArrayList<>();
    }

    public Proyecto(String nombre, String descripcion, Date fechaInicio, Date fechaFin,
            EstadoProyecto estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.documentos = new ArrayList<>();
        this.actividades = new ArrayList<>();
    }
    public Proyecto(int identificador, String nombre, String descripcion, Date fechaInicio, Date fechaFin,
            EstadoProyecto estado) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.documentos = new ArrayList<>();
        this.actividades = new ArrayList<>();
    }

    public void cargarActividades(List<Actividad> actividadesBD) {
        this.actividades.clear();
        this.actividades.addAll(actividadesBD);
    }

    public void registrarActividad(Actividad actividad) {
        actividades.add(actividad);
    }

    public boolean eliminarActividad(String nombre) {
        Iterator<Actividad> it = actividades.iterator();
        while (it.hasNext()) {
            Actividad act = it.next();
            if (act.getNombre().equalsIgnoreCase(nombre)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean actualizarCampoEnMemoria(String nombreActividad, String campo, Object nuevoValor) {
        for (Actividad actividad : actividades) {
            if (actividad.getNombre().equalsIgnoreCase(nombreActividad)) {
                switch (campo.toLowerCase()) {
                    case "descripcion":
                        actividad.setDescripcion((String) nuevoValor);
                        break;
                    case "tipo":
                        try {
                            if (nuevoValor instanceof TipoActividad) {
                                actividad.setTipo((TipoActividad) nuevoValor);
                            } else if (nuevoValor instanceof String) {
                                actividad.setTipo(TipoActividad.valueOf(((String) nuevoValor).toUpperCase()));
                            } else {
                                System.out.println("Tipo de valor no válido para el campo 'tipo'.");
                                return false;
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("El tipo de actividad '" + nuevoValor + "' no es válido.");
                            return false;
                        }
                        break;
                    case "fecha":
                        Date nuevaFecha = null;
                        try {
                            if (nuevoValor instanceof String) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                nuevaFecha = sdf.parse((String) nuevoValor);
                            } else if (nuevoValor instanceof Date) {
                                nuevaFecha = (Date) nuevoValor;
                            }
                            actividad.setFecha(nuevaFecha);
                        } catch (ParseException e) {
                            System.out.println("Error: formato de fecha inválido. Use yyyy-MM-dd");
                            return false;
                        }
                        break;
                    default:
                        System.out.println("El campo '" + campo + "' no existe en Actividad.");
                        return false;
                }
                return true;
            }
        }
        System.out.println("La actividad '" + nombreActividad + "' no existe en el proyecto.");
        return false;
    }

    public void cargarDocumentos(List<Documento> documentosBD) {
        this.documentos.clear();
        this.documentos.addAll(documentosBD);
    }

    public Documento registrarDocumento(String nombre, String tipo, String rutaArchivo) {
        Documento documento = null;
        try {
            byte[] archivoBytes = Files.readAllBytes(Path.of(rutaArchivo));
            TipoDocumento tipoDoc = TipoDocumento.valueOf(tipo.toUpperCase());

            String extension = "";
            int puntoIndex = rutaArchivo.lastIndexOf('.');
            if (puntoIndex != -1 && puntoIndex < rutaArchivo.length() - 1) {
                extension = rutaArchivo.substring(puntoIndex + 1);
            }

            documento = new Documento(nombre, tipoDoc, archivoBytes, extension);
            documentos.add(documento);

            System.out.println("Documento '" + nombre + "' agregado en memoria con extensión ." + extension);
            
        } catch (IllegalArgumentException e) {
            System.err.println("Tipo de documento inválido: " + tipo);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return documento;
    }

    public List<String> obtenerNombresDocumentosProyecto() {
        DocumentoDAO dao = new DocumentoDAO();
        List<String> documentosProyecto = dao.obtenerNombresDocumentosPorProyecto(this.identificador);
        return documentosProyecto;
    }

    public boolean descargarDocumento(String nombreDocumento, String rutaDestino) {
        DocumentoDAO dao = new DocumentoDAO();
        return dao.descargarDocumento(nombreDocumento, rutaDestino);
    }
 
    public Documento consultarDocumento(String nombreDocumento) {
        for (Documento d : documentos) {
            if (d.getNombre().equalsIgnoreCase(nombreDocumento)) {
                return d;
            }
        }
        return null;
    }
    public List<Documento> eliminarDocumentosProyecto() {
    if (this.documentos == null || this.documentos.isEmpty()) {
        DocumentoDAO documentoDAO = new DocumentoDAO();
        this.documentos = documentoDAO.obtenerDocumentosPorProyecto(this.identificador);
    }

    List<Documento> documentosAEliminar = new ArrayList<>(this.documentos);

    this.documentos.clear();

    return documentosAEliminar;
}

    public boolean eliminarDocumentoEnMemoria(String nombreDocumento) {
        Iterator<Documento> iterator = documentos.iterator();

        while (iterator.hasNext()) {
            Documento d = iterator.next();
            if (d.getNombre().equalsIgnoreCase(nombreDocumento)) {
                iterator.remove();
                System.out.println("Documento '" + nombreDocumento + "' eliminado en memoria.");
                return true;
            }
        }

        System.out.println("El documento '" + nombreDocumento + "' no existe en memoria.");
        return false;
    }

    public void agregarDocumento(Documento documento) {
        this.documentos.add(documento);
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoProyecto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProyecto estado) {
        this.estado = estado;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }
}