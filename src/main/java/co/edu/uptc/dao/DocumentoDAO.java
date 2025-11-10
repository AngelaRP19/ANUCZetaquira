package co.edu.uptc.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.database.Conexion;
import co.edu.uptc.model.Documento;
import co.edu.uptc.model.TipoDocumento;

public class DocumentoDAO {

    public DocumentoDAO() {
    }

    public boolean insertarDocumento(int proyectoId, String nombre, String tipo, String rutaArchivo) {
        String sql = "INSERT INTO documentos (proyecto_id, nombre, tipo, archivo, extension) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            byte[] archivoBytes = Files.readAllBytes(Path.of(rutaArchivo));

            String extension = "";
            int puntoIndex = rutaArchivo.lastIndexOf('.');
            if (puntoIndex != -1 && puntoIndex < rutaArchivo.length() - 1) {
                extension = rutaArchivo.substring(puntoIndex + 1);
            }

            pstmt.setInt(1, proyectoId);
            pstmt.setString(2, nombre);
            pstmt.setString(3, tipo.toUpperCase());
            pstmt.setBytes(4, archivoBytes);
            pstmt.setString(5, extension);

            int filas = pstmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error SQL al guardar el documento: " + e.getMessage());
            e.printStackTrace();
            return false;

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();
            return false;

        } catch (Exception e) {
            System.err.println("Error inesperado al guardar el documento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean descargarDocumento(String nombreDocumento, String rutaDestino) {
        String sql = "SELECT nombre, archivo, extension FROM documentos WHERE nombre = ?";

        try (Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreDocumento);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                byte[] archivoBytes = rs.getBytes("archivo");
                String extension = rs.getString("extension");

                Path rutaCompleta = Path.of(rutaDestino, nombre + "." + extension);

                Files.write(rutaCompleta, archivoBytes);
                System.out.println("Documento descargado correctamente en: " + rutaCompleta);

                return true;
            } else {
                System.out.println("No se encontró un documento con el nombre '" + nombreDocumento + "'.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al descargar el documento: " + e.getMessage());
            return false;

        } catch (IOException e) {
            System.err.println("Error al guardar el archivo en disco: " + e.getMessage());
            return false;
        }
    }

    public List<String> obtenerNombresDocumentosPorProyecto(int proyectoId) {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre FROM documentos WHERE proyecto_id = ?";

        try (Connection conn = Conexion.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, proyectoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener nombres de documentos: " + e.getMessage());
            e.printStackTrace();
        }

        return nombres;
    }
    

    public boolean eliminarDocumento(String nombreDocumento) {
    String sql = "DELETE FROM documentos WHERE nombre = ?";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, nombreDocumento);
        int filas = pstmt.executeUpdate();

        if (filas > 0) {
            System.out.println("Documento '" + nombreDocumento + "' eliminado de la base de datos.");
            return true;
        } else {
            System.out.println("No se encontró el documento '" + nombreDocumento + "' en la base de datos.");
            return false;
        }

    } catch (SQLException e) {
        System.err.println("Error SQL al eliminar el documento: " + e.getMessage());
        return false;
    }
    }

    public List<Documento> obtenerDocumentosPorProyecto(int proyectoId) {
    List<Documento> documentos = new ArrayList<>();
    String sql = "SELECT nombre, tipo, fecha_carga, archivo, extension FROM documentos WHERE proyecto_id = ?";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, proyectoId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String nombre = rs.getString("nombre");
            TipoDocumento tipo = TipoDocumento.valueOf(rs.getString("tipo"));
            byte[] archivoBytes = rs.getBytes("archivo");
            String extension = rs.getString("extension");

            java.sql.Date fecha = null;
            String fechaStr = rs.getString("fecha_carga");
            if (fechaStr != null && !fechaStr.isEmpty()) {
                try {
                    fecha = java.sql.Date.valueOf(fechaStr.trim());
                } catch (IllegalArgumentException e) {
                    System.err.println("⚠️ Fecha con formato no estándar: " + fechaStr);
                }
            }

            Documento doc = new Documento(nombre, tipo, archivoBytes, extension);
            doc.setFechaCarga(fecha);
            documentos.add(doc);
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener documentos: " + e.getMessage());
        e.printStackTrace();
    }

    return documentos;
    }

    
}
