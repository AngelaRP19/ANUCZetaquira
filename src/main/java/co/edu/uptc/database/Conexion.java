package co.edu.uptc.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String DB_URL = getDatabasePath();

    private Conexion() {}

    private static String getDatabasePath() {
        // Directorio en el home del usuario para guardar la base de datos
        String userHome = System.getProperty("user.home");
        File appDir = new File(userHome, ".anuc");
        
        // Crear el directorio si no existe
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        
        File dbFile = new File(appDir, "ANUC.db");
        
        // Si la base de datos no existe, copiarla desde resources
        if (!dbFile.exists()) {
            copyDatabaseFromResources(dbFile);
        }
        
        return "jdbc:sqlite:" + dbFile.getAbsolutePath();
    }

    private static void copyDatabaseFromResources(File targetFile) {
        try (InputStream inputStream = Conexion.class.getResourceAsStream("/database/ANUC.db")) {
            if (inputStream == null) {
                System.err.println("No se encontró la base de datos en resources. Se creará una nueva.");
                return;
            }
            
            try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                System.out.println("Base de datos copiada exitosamente a: " + targetFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error al copiar la base de datos: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite driver not found", e);
        }
    }
}
