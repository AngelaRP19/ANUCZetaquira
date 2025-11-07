package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Panel que muestra todos los proyectos registrados. Solo se listan los títulos.
 * La disposición replica el estilo mostrado en la imagen proporcionada: columna izquierda
 * con instrucción y botones (proyectos) y columna derecha con búsqueda por nombre.
 */
public class VerTodosProyectos extends JPanel {
    private ActionListener listener;
    private List<String> proyectos;
    private Texto titulo;
    private Texto instruccion;
    private IngresarCampo campoBusqueda;
    private Boton botonBuscar;
    private JPanel contenedorLista;
    private final ColorConstante color = new ColorConstante();

    public VerTodosProyectos(ActionListener listener, List<String> proyectos) {
        this.listener = listener;
        this.proyectos = proyectos;
        setLayout(new BorderLayout());
        setBackground(color.getBeige());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        inicializarComponentes();
        ubicarComponentes();
        agregarListeners();
    }

    private void inicializarComponentes() {
        titulo = new Texto("PROYECTOS REGISTRADOS", TipoTexto.TITULO, "VERDE");
        instruccion = new Texto("Haga click sobre un proyecto para ver sus detalles, editar o eliminar. ", TipoTexto.INSTRUCCION, "VERDE");
        campoBusqueda = new IngresarCampo(18);
        campoBusqueda.setPlaceholder("Nombre del proyecto");
        botonBuscar = new Boton("BUSCAR", 180, 40, "BUSCAR_PROYECTO", 15, color.getVerdeBotones());

        contenedorLista = new JPanel();
        contenedorLista.setLayout(new BoxLayout(contenedorLista, BoxLayout.Y_AXIS));
        contenedorLista.setBackground(color.getBeige());
        cargarProyectos();
    }

    private void ubicarComponentes() {
        // Cabecera en el norte
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        cabecera.setBackground(color.getBeige());
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(8));
        cabecera.add(crearSubPanel(instruccion));
        add(cabecera, BorderLayout.NORTH);

        // Panel central dividido (lista izquierda, búsqueda derecha)
        JPanel centro = new JPanel();
        centro.setBackground(color.getBeige());
        centro.setLayout(new BoxLayout(centro, BoxLayout.X_AXIS));

        // Izquierda: lista de proyectos dentro de un scroll
        ScrollPersonalizado scroll = new ScrollPersonalizado(contenedorLista, 600, 450);

        JPanel panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(color.getBeige());
        panelLista.setBorder(BorderFactory.createLineBorder(color.getVerdeOscuro()));
        panelLista.add(crearSubPanel(scroll));

        // Derecha: búsqueda
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new BoxLayout(panelBusqueda, BoxLayout.Y_AXIS));
        panelBusqueda.setBackground(color.getBeige());
        panelBusqueda.setBorder(BorderFactory.createLineBorder(color.getVerdeOscuro()));

        Texto lblBusqueda = new Texto("Busqueda por nombre", TipoTexto.NORMAL, "VERDE");
        lblBusqueda.setAlignmentX(CENTER_ALIGNMENT);
        panelBusqueda.add(Box.createVerticalStrut(10));
        panelBusqueda.add(lblBusqueda);
        panelBusqueda.add(Box.createVerticalStrut(15));

        JPanel campoYBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        campoYBoton.setBackground(color.getBeige());
        campoYBoton.add(campoBusqueda);
        panelBusqueda.add(campoYBoton);
        panelBusqueda.add(Box.createVerticalStrut(25));
        JPanel pBuscar = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        pBuscar.setBackground(color.getBeige());
        pBuscar.add(botonBuscar);
        panelBusqueda.add(pBuscar);

        centro.add(panelLista);
        centro.add(Box.createHorizontalStrut(15));
        centro.add(panelBusqueda);

        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearSubPanel(javax.swing.JComponent comp) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        p.setBackground(color.getBeige());
        p.add(comp);
        return p;
    }

    private void cargarProyectos() {
        contenedorLista.removeAll();
        if (proyectos == null || proyectos.isEmpty()) {
            Texto vacio = new Texto("No hay proyectos registrados", TipoTexto.NORMAL, "VERDE");
            vacio.setAlignmentX(CENTER_ALIGNMENT);
            contenedorLista.add(vacio);
        } else {
            for (String nombre : proyectos) {
                // Solo instanciamos el botón con el nuevo constructor de configuración interna
                Boton btn = new Boton(nombre, "VER_PROYECTO/" + nombre, 550, 40, Boton.BotonEstilo.LISTA_PROYECTO);
                contenedorLista.add(btn);
                contenedorLista.add(Box.createVerticalStrut(10));
                if (listener != null) {
                    btn.addActionListener(listener);
                }
            }
        }
        contenedorLista.revalidate();
        contenedorLista.repaint();
    }

    private void agregarListeners() {
        if (listener != null) {
            botonBuscar.addActionListener(listener);
        }
    }

    // Getter para texto de búsqueda
    public String getTextoBusqueda() {
        return campoBusqueda.getText();
    }

    // Método para refrescar la lista externamente si cambia
    public void setProyectos(List<String> nuevos) {
        this.proyectos = nuevos;
        cargarProyectos();
    }
}
