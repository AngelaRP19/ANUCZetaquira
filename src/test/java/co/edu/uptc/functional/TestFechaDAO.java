package co.edu.uptc.functional;

import co.edu.uptc.dao.ActividadDAO;
import co.edu.uptc.database.Conexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Test para verificar que la corrección de fechas en ActividadDAO funciona
 */
public class TestFechaDAO {
    
    public static void main(String[] args) {
        try {
            System.out.println("🧪 TEST: Corrección de fechas en ActividadDAO");
            System.out.println("==============================================");
            
            // 1. Verificar datos en la base de datos
            System.out.println("1️⃣ Verificando fechas en base de datos...");
            try (Connection conn = Conexion.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT nombre, fecha FROM actividades LIMIT 3")) {
                
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String fechaStr = rs.getString("fecha");
                    System.out.println("   📝 Actividad: " + nombre);
                    System.out.println("   📅 Fecha en BD: " + fechaStr);
                    
                    // Probar parseo como timestamp
                    try {
                        long timestamp = Long.parseLong(fechaStr);
                        java.util.Date fecha = new java.util.Date(timestamp);
                        System.out.println("   ✅ Timestamp parseado: " + fecha);
                    } catch (NumberFormatException e) {
                        System.out.println("   ❌ No es timestamp válido");
                    }
                    System.out.println();
                }
            }
            
            // 2. Probar el método corregido
            System.out.println("2️⃣ Probando ActividadDAO.obtenerActividadesPorProyecto()...");
            ActividadDAO dao = new ActividadDAO();
            
            // Obtener un proyecto_id válido
            int proyectoId = 1;
            try (Connection conn = Conexion.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT proyecto_id FROM proyectos LIMIT 1")) {
                if (rs.next()) {
                    proyectoId = rs.getInt("proyecto_id");
                }
            }
            
            System.out.println("   🔍 Usando proyecto_id: " + proyectoId);
            
            List<co.edu.uptc.model.Actividad> actividades = dao.obtenerActividadesPorProyecto(proyectoId);
            
            System.out.println("   ✅ Actividades encontradas: " + actividades.size());
            
            for (co.edu.uptc.model.Actividad act : actividades) {
                System.out.println("   📝 " + act.getNombre() + " - Fecha: " + act.getFecha());
            }
            
            if (actividades.size() > 0) {
                System.out.println("   ✅ ÉXITO: El método funciona sin errores");
            } else {
                System.out.println("   ⚠️  No se encontraron actividades, pero no hubo errores");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error en test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
