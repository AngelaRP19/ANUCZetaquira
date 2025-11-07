package co.edu.uptc.model;

import java.util.Date;

public class Actividad {

    private int identificador;
    private String nombre;
    private String descripcion;
    private TipoActividad tipo;
    private Date fecha; 

    public Actividad(int identificador, String nombre, String descripcion, TipoActividad tipo, Date fecha) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public Actividad(String nombre, String descripcion, TipoActividad tipo, Date fecha) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.fecha = fecha;
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

    public TipoActividad getTipo() {
        return tipo;
    }

    public void setTipo(TipoActividad tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Actividad: " + nombre +
            " | Tipo: " + tipo +
            " | Fecha: " + fecha +
            " | Descripción: " + descripcion;
    }

}