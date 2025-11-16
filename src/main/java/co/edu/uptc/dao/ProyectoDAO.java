package co.edu.uptc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.database.Conexion;
import co.edu.uptc.model.Proyecto;
import co.edu.uptc.model.EstadoProyecto;


public class ProyectoDAO {

    public ProyectoDAO() {
        
    }

    public List<Proyecto> obtenerTodosLosProyectos() {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT proyecto_id, nombre, descripcion, fecha_inicio, fecha_fin, estado FROM proyectos";

        try (Connection connection = Conexion.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("proyecto_id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");

                String fechaInicioStr = rs.getString("fecha_inicio");
                String fechaFinStr = rs.getString("fecha_fin");

                Date fechaInicio = convertirFecha(fechaInicioStr);
                Date fechaFin = convertirFecha(fechaFinStr);

                String estado = rs.getString("estado");
                EstadoProyecto estadoP = EstadoProyecto.valueOf(estado.toUpperCase());

                Proyecto proyecto = new Proyecto(id, nombre, descripcion, fechaInicio, fechaFin, estadoP);
                proyectos.add(proyecto);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener proyectos: " + e.getMessage());
        }

        return proyectos;
    }

    private java.sql.Date convertirFecha(String fechaStr) {
    if (fechaStr == null || fechaStr.isEmpty()) {
        return null;
    }

    try {
        if (fechaStr.matches("\\d+")) {
            long millis = Long.parseLong(fechaStr);
            return new java.sql.Date(millis);
        }

        java.util.Date utilDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
        return new java.sql.Date(utilDate.getTime());
    } catch (Exception e) {
        System.err.println("Error al convertir la fecha: " + fechaStr);
        return null;
    }
    }


    public boolean registrarProyecto(Proyecto proyecto) {
        try (Connection connection = Conexion.getConnection()) {

            String sql = "INSERT INTO proyectos (nombre, descripcion, fecha_inicio, fecha_fin, estado) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, proyecto.getNombre());
            ps.setString(2, proyecto.getDescripcion());
            ps.setDate(3, new java.sql.Date(proyecto.getFechaInicio().getTime()));
            if (proyecto.getFechaFin() != null) {
            ps.setDate(4, new java.sql.Date(proyecto.getFechaFin().getTime()));
            } else {
            ps.setNull(4, java.sql.Types.DATE);
            }
            ps.setString(5, proyecto.getEstado().name());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar actividad: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProyecto(int identificador) {
       String sql = "DELETE FROM proyectos WHERE proyecto_id = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, identificador);

            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarCampoProyecto(int proyectoId, String campo, Object valor) {
    String sql = "UPDATE proyectos SET " + campo + " = ? WHERE proyecto_id = ?";
    
    try (Connection conn = Conexion.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        if (valor instanceof String) {
            pstmt.setString(1, (String) valor);
        } else if (valor instanceof Date) {
            pstmt.setDate(1, new java.sql.Date(((Date) valor).getTime()));
        } else if (valor == null) {
            pstmt.setNull(1, java.sql.Types.NULL);
        }

        pstmt.setInt(2, proyectoId);

        int filas = pstmt.executeUpdate();
        return filas > 0;
    } catch (SQLException e) {
        System.err.println("Error al actualizar campo del proyecto: " + e.getMessage());
        return false;
    }
    }

    public int obtenerIdProyectoPorNombre(String nombreProyecto) {
        String sql = "SELECT proyecto_id FROM proyectos WHERE nombre = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombreProyecto);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("proyecto_id");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID del proyecto: " + e.getMessage());
        }
        
        return -1;
    }

}