package co.edu.uptc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String DB_URL = "jdbc:sqlite:ANUC.db"; 

    private Conexion() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite driver not found", e);
        }
    }
}
