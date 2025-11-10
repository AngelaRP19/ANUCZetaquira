package co.edu.uptc.presenter;

import co.edu.uptc.model.Proyecto;
import co.edu.uptc.model.Actividad;
import co.edu.uptc.model.Documento;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Presenter presenter = new Presenter();
        int eliminados = presenter.eliminarProyectosInvalidos();
        if (eliminados > 0) {
            System.out.println("Proyectos inválidos eliminados: " + eliminados);
        } else {
            System.out.println("No se encontraron proyectos inválidos para eliminar.");
        }
    }
}
