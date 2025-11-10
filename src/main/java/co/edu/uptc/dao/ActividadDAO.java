package co.edu.uptc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import co.edu.uptc.database.Conexion;
import co.edu.uptc.model.Actividad;
import co.edu.uptc.model.TipoActividad;

public class ActividadDAO {

    public ActividadDAO() {
    }

    public boolean registrarActividad(String nombreProyecto, Actividad actividad) {
        try (Connection connection = Conexion.getConnection()) {
            int idProyecto = obtenerIdProyectoPorNombre(nombreProyecto, connection);
            if (idProyecto == -1) {
                System.err.println("Proyecto no encontrado: " + nombreProyecto);
                return false;
            }

            String sql = "INSERT INTO actividades (nombre, descripcion, tipo, fecha, proyecto_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, actividad.getNombre());
            ps.setString(2, actividad.getDescripcion());
            ps.setString(3, actividad.getTipo().name());
            ps.setDate(4, new java.sql.Date(actividad.getFecha().getTime()));
            ps.setInt(5, idProyecto);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar actividad: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarActividad(String nombre, int proyectoId) {
        String sql = "DELETE FROM actividades WHERE nombre = ? AND proyecto_id = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setInt(2, proyectoId);

            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarCampoActividad(String nombreActividad, String campo, Object valor) {
        String sql = "UPDATE actividades SET " + campo + " = ? WHERE nombre = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (valor instanceof String) {
                pstmt.setString(1, (String) valor);
            } else if (valor instanceof java.util.Date) {
                pstmt.setDate(1, new java.sql.Date(((Date) valor).getTime()));
            } else if (valor == null) {
                pstmt.setNull(1, java.sql.Types.NULL);
            }

            pstmt.setString(2, nombreActividad);

            int filas = pstmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar campo de actividad: " + e.getMessage());
            return false;
        }
    }


    private int obtenerIdProyectoPorNombre(String nombreProyecto, Connection connection) {
        try {
            String sql = "SELECT proyecto_id FROM proyectos WHERE nombre = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, nombreProyecto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("proyecto_id");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de proyecto: " + e.getMessage());
        }
        return -1;
    }

    public List<Actividad> obtenerActividadesPorProyecto(String nombreProyecto) {
        List<Actividad> lista = new ArrayList<>();
        try (Connection connection = Conexion.getConnection()) {
            int idProyecto = obtenerIdProyectoPorNombre(nombreProyecto, connection);
            if (idProyecto == -1) return lista;

            String sql = "SELECT nombre, descripcion, tipo, fecha FROM actividades WHERE proyecto_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idProyecto);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Actividad act = new Actividad(
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        TipoActividad.valueOf(rs.getString("tipo")),
                        rs.getDate("fecha")
                );
                lista.add(act);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener actividades: " + e.getMessage());
        }
        return lista;
    }

    public List<Actividad> obtenerActividadesPorProyecto(int proyectoId) {
        List<Actividad> actividades = new ArrayList<>();
        String sql = "SELECT nombre, descripcion, tipo, fecha FROM actividades WHERE proyecto_id = ?";

        try (PreparedStatement stmt = Conexion.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, proyectoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                TipoActividad tipo = TipoActividad.valueOf(rs.getString("tipo"));
                
                String fechaStr = rs.getString("fecha");
                Date fecha = (fechaStr != null) ? Date.valueOf(fechaStr) : null;


                actividades.add(new Actividad(nombre, descripcion, tipo, fecha));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return actividades;
}

}