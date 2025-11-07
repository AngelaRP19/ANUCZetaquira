package co.edu.uptc.presenter;
/*import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {

        Presenter presenter = new Presenter();

        presenter.cargarTodoDesdeBD();


        //List<Proyecto> proyectos = presenter.getProyectos();

        //Actividad actividad = presenter.consultarActividad("Proyecto AgroInnovar", "Reunión de seguimiento");

        /*presenter.actualizarActividad("Proyecto AgroInnovar", "Reunión de seguimiento", "tipo", "ASISTENCIA_TECNICA");
        if (actividad != null) {
            System.out.println(actividad);
        }
        //presenter.eliminarDocumento("Proyecto AgroInnovar", "Recursos");
        //presenter.subir_documento("Proyecto AgroInnovar", "Recursos", "PROPUESTa", "C:\\Users\\Liceth\\Documents\\SoftwareI\\Recursos.xlsx");
    }
}
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Cargar el driver de SQLite
            Class.forName("org.sqlite.JDBC");

            // Crear la URL para tu base de datos
            String url = "jdbc:sqlite:ANUCProyectos.db"; // Reemplaza con la ruta y nombre de tu archivo

            // Establecer la conexión
            connection = DriverManager.getConnection(url);

            System.out.println("¡Conexión a la base de datos SQLite exitosa!");

        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver JDBC de SQLite no encontrado. " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("Conexión cerrada.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
