package co.edu.uptc.model;

import java.util.Date;

public class Documento {
 
    private int identificador;
    private String nombre;
    private TipoDocumento tipo;
    private byte[] archivo;
    private String extension;
    private Date fechaCarga;

    public Documento(int identificador, String nombre, TipoDocumento tipo, byte[] archivo, String extension) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.tipo = tipo;
        this.archivo=archivo;
        this.extension=extension;
    }

    public Documento(String nombre, TipoDocumento tipo, byte[] archivo, String extension) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.archivo=archivo;
        this.extension=extension;
    }

    public Documento(String nombre, TipoDocumento tipo, byte[] archivo, Date fechaCarga) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.archivo=archivo;
        this.fechaCarga=fechaCarga;
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

    public TipoDocumento getTipo() {
        return tipo;
    }

    public void setTipo(TipoDocumento tipo) {
        this.tipo = tipo;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
    
    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }
}
