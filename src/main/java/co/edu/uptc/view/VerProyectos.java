package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class VerProyectos extends JPanel {
    
    private ActionListener listener;
    private List<String> proyectos;
    private Texto titulo;
    private Texto instruccion;
    private IngresarCampo campoBusqueda;
    private Boton botonBuscar;
    private Boton cancelar;
    private PanelRedondeado contenedorLista;
    private JPanel panelCentral;
    private final ColorConstante color = new ColorConstante();

    public VerProyectos(ActionListener listener, List<String> proyectos) {
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
        titulo = new Texto("Proyectos Registrados", TipoTexto.TITULO, "VERDE");
        instruccion = new Texto("Haga click sobre un proyecto para ver sus detalles, editar o eliminar. ", TipoTexto.INSTRUCCION, "VERDE");
        campoBusqueda = new IngresarCampo(18);
        campoBusqueda.setPlaceholder("Nombre del proyecto");
        botonBuscar = new Boton("BUSCAR", 180, 40, "BUSCAR_PROYECTO", 15, color.getVerdeBotones());
        cancelar = new Boton("CANCELAR", 180, 40, "CANCELAR_BUSQUEDA_PROYECTO", 15, color.getVerdeOscuro());

        contenedorLista = new PanelRedondeado();
        contenedorLista.setLayout(new BoxLayout(contenedorLista, BoxLayout.Y_AXIS));
        contenedorLista.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // padding interno
        
        panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.X_AXIS));
        panelCentral.setBackground(color.getBeige());
        panelCentral.setOpaque(false);
        
        cargarProyectos();
    }

    private void ubicarComponentes() {
        this.agregarCabecera();
        this.agregarCentralIzquierdo();
        this.agregarCentralDerecho();
        add(panelCentral, BorderLayout.CENTER);
    }
    private void agregarCentralIzquierdo(){
        // Panel izquierdo: lista de proyectos con scroll (PanelRedondeado)
        PanelRedondeado panelListaRedondeado = new PanelRedondeado();
        panelListaRedondeado.setLayout(new BorderLayout());
        panelListaRedondeado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        ScrollPersonalizado scroll = new ScrollPersonalizado(contenedorLista, 600, 450, false);
        panelListaRedondeado.add(scroll, BorderLayout.CENTER);

        panelCentral.add(panelListaRedondeado);
    }
    private void agregarCentralDerecho() {
        PanelRedondeado panelBusquedaRedondeado = new PanelRedondeado();
        panelBusquedaRedondeado.setLayout(new BoxLayout(panelBusquedaRedondeado, BoxLayout.Y_AXIS));
        panelBusquedaRedondeado.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        Texto lblBusqueda = new Texto("Busqueda por nombre", TipoTexto.NORMAL, "VERDE");
        lblBusqueda.setAlignmentX(CENTER_ALIGNMENT);
        panelBusquedaRedondeado.add(Box.createVerticalStrut(10));
        panelBusquedaRedondeado.add(lblBusqueda);
        panelBusquedaRedondeado.add(Box.createVerticalStrut(15));

        Texto lblInstruccion = new Texto("<html>Ingresa el nombre del proyecto o una <br> palabra clave que quieres buscar:</html>", TipoTexto.INSTRUCCION, "VERDE");
        lblInstruccion.setAlignmentX(LEFT_ALIGNMENT);
        
        JPanel panelInstruccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelInstruccion.setBackground(color.getBeige());
        panelInstruccion.setOpaque(false);
        panelInstruccion.add(lblInstruccion);
        panelBusquedaRedondeado.add(panelInstruccion);
        panelBusquedaRedondeado.add(Box.createVerticalStrut(10));

        JPanel campoYBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        campoYBoton.setBackground(color.getBeige());
        campoYBoton.setOpaque(false);
        campoYBoton.add(campoBusqueda);
        java.net.URL rutaBuscar = getClass().getResource("/imagenes/buscar.png");
        if (rutaBuscar != null) {
            Image imgBuscar = new ImageIcon(rutaBuscar).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            JLabel iconoBuscar = new JLabel(new ImageIcon(imgBuscar));
            campoYBoton.add(iconoBuscar);
        }
        panelBusquedaRedondeado.add(campoYBoton);
        panelBusquedaRedondeado.add(Box.createVerticalStrut(25));
        
        JPanel pBuscar = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        pBuscar.setBackground(color.getBeige());
        pBuscar.setOpaque(false);
        pBuscar.add(botonBuscar);
        panelBusquedaRedondeado.add(pBuscar);
        panelBusquedaRedondeado.add(Box.createVerticalStrut(10));
        panelBusquedaRedondeado.add(cancelar);

        panelCentral.add(Box.createHorizontalStrut(15));
        panelCentral.add(panelBusquedaRedondeado);
    }
    private void agregarCabecera() {
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        cabecera.setBackground(color.getBeige());
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(8));
        JPanel pInstruccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        pInstruccion.setBackground(color.getBeige());
        pInstruccion.add(instruccion);
        cabecera.add(pInstruccion);
        add(cabecera, BorderLayout.NORTH);
    }

    private void cargarProyectos() {
        cargarProyectos(this.proyectos);
    }

    private void cargarProyectos(List<String> listaProyectos) {
        contenedorLista.removeAll();
        if (listaProyectos == null || listaProyectos.isEmpty()) {
            Texto vacio = new Texto("No hay proyectos registrados", TipoTexto.NORMAL, "VERDE");
            vacio.setAlignmentX(CENTER_ALIGNMENT);
            contenedorLista.add(vacio);
        } else {
            for (String nombre : listaProyectos) {
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
            cancelar.addActionListener(listener);
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

    public void mostrarResultadosBusqueda(List<String> proyectosFiltrados) {
        if (proyectosFiltrados == null || proyectosFiltrados.isEmpty()) {
            contenedorLista.removeAll();
            Texto sinResultados = new Texto("No se encontraron proyectos que coincidan con la búsqueda", TipoTexto.NORMAL, "VERDE");
            sinResultados.setAlignmentX(CENTER_ALIGNMENT);
            contenedorLista.add(sinResultados);
            contenedorLista.revalidate();
            contenedorLista.repaint();
        } else {
            cargarProyectos(proyectosFiltrados);
        }
    }
}
