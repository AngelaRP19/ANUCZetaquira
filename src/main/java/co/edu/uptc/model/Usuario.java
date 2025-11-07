package co.edu.uptc.model;

public class Usuario {
    
    private String nombre;
    private String nombre_usuario;
    private String contrasena;
    private String cedula;

    

    public Usuario(String nombre, String nombre_usuario, String contrasena, String cedula) {
        this.nombre = nombre;
        this.nombre_usuario = nombre_usuario;
        this.contrasena = contrasena;
        this.cedula = cedula;
    }

    public boolean autenticar(String nombre_usuario, String contrasena){
        return false;
    }

    public boolean cambiarContrasena (String nuevaContrasena){
        return false;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre_usuario() {
        return nombre_usuario;
    }
    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public String getCedula() {
        return cedula;
    }
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }



}

