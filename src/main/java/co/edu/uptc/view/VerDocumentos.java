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
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

public class VerDocumentos extends JPanel {
    private ActionListener listener;
    private List<String> documentos;
    private String nombreProyecto = "";
    private Texto titulo;
    private Texto instruccion;
    private IngresarCampo campoBusqueda;
    private Boton botonBuscar;
    private Boton cancelar;
    private PanelRedondeado contenedorLista;
    private JPanel panelCentral;
    private final ColorConstante color = new ColorConstante();

    public VerDocumentos(ActionListener listener, List<String> documentos, String nombreProyecto) {
        this.listener = listener;
        this.documentos = documentos;
        this.nombreProyecto = nombreProyecto;
        setLayout(new BorderLayout());
        setBackground(color.getBeige());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        inicializarComponentes();
        ubicarComponentes();
        agregarListeners();
    }

    private void inicializarComponentes() {
        titulo = new Texto("DOCUMENTOS DEL PROYECTO", TipoTexto.TITULO, "VERDE");
        instruccion = new Texto("Haga click en descargar o eliminar un documento.", TipoTexto.INSTRUCCION, "VERDE");
        campoBusqueda = new IngresarCampo(18);
        campoBusqueda.setPlaceholder("Nombre del documento");
        botonBuscar = new Boton("BUSCAR", 180, 40, "BUSCAR_DOCUMENTO", 15, color.getVerdeBotones());
        cancelar = new Boton("CANCELAR", 180, 40, "VOLVER_VER_PROYECTO/" + nombreProyecto, 15, color.getVerdeOscuro());

        contenedorLista = new PanelRedondeado();
        contenedorLista.setLayout(new BoxLayout(contenedorLista, BoxLayout.Y_AXIS));
        contenedorLista.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // padding interno
        
        panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.X_AXIS));
        panelCentral.setBackground(color.getBeige());
        panelCentral.setOpaque(false);
        
        cargarDocumentos();
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

    private void cargarDocumentos() {
        cargarDocumentos(this.documentos);
    }

    private void cargarDocumentos(List<String> listaDocumentos) {
        System.out.println("[LOG] VerDocumentos.cargarDocumentos() - Cargando " + (listaDocumentos != null ? listaDocumentos.size() : 0) + " documentos");
        contenedorLista.removeAll();
        if (listaDocumentos == null || listaDocumentos.isEmpty()) {
            System.out.println("[LOG] VerDocumentos.cargarDocumentos() - No hay documentos, mostrando mensaje vacío");
            Texto vacio = new Texto("No hay documentos registrados para este proyecto", TipoTexto.NORMAL, "VERDE");
            vacio.setAlignmentX(CENTER_ALIGNMENT);
            contenedorLista.add(vacio);
        } else {
            System.out.println("[LOG] VerDocumentos.cargarDocumentos() - Documentos a mostrar: " + listaDocumentos);
            for (String nombre : listaDocumentos) {
                JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                fila.setOpaque(false);
                // Botón con el nombre del documento
                Boton btnDoc = new Boton(nombre, "DESCARGAR_DOCUMENTO/" + nombre + "/" + nombreProyecto, 500, 40, Boton.BotonEstilo.LISTA_PROYECTO);
                fila.add(btnDoc);
                // Botón de descargar
                JButton btnDescargar = new JButton();
                btnDescargar.setToolTipText("Descargar");
                btnDescargar.setActionCommand("DESCARGAR_DOCUMENTO/" + nombre + "/" + nombreProyecto);
                btnDescargar.setPreferredSize(new java.awt.Dimension(40, 40));
                java.net.URL rutaDescargar = getClass().getResource("/imagenes/descargar.png");
                if (rutaDescargar != null) {
                    Image imgDescargar = new ImageIcon(rutaDescargar).getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
                    btnDescargar.setIcon(new ImageIcon(imgDescargar));
                } else {
                    btnDescargar.setText("↓");
                }
                fila.add(btnDescargar);
                
                // Botón de eliminar
                JButton btnEliminar = new JButton();
                btnEliminar.setToolTipText("Eliminar");
                btnEliminar.setActionCommand("ELIMINAR_DOCUMENTO/" + nombre + "/" + nombreProyecto);
                btnEliminar.setPreferredSize(new java.awt.Dimension(40, 40));
                java.net.URL rutaEliminar = getClass().getResource("/imagenes/eliminar.png");
                if (rutaEliminar != null) {
                    Image imgEliminar = new ImageIcon(rutaEliminar).getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
                    btnEliminar.setIcon(new ImageIcon(imgEliminar));
                } else {
                    btnEliminar.setText("✗");
                }
                fila.add(btnEliminar);
                
                if (listener != null) {
                    btnDoc.addActionListener(listener);
                    btnDescargar.addActionListener(listener);
                    btnEliminar.addActionListener(listener);
                }
                contenedorLista.add(fila);
                contenedorLista.add(Box.createVerticalStrut(10));
            }
        }
        contenedorLista.revalidate();
        contenedorLista.repaint();
    }

    /**
     * Abre un selector de carpetas apuntando al Escritorio (o carpeta Home) y retorna
     * la ruta absoluta de la carpeta seleccionada. Si el usuario cancela, retorna null.
     */
    public String seleccionarCarpetaDescarga() {
        // Directorio inicial: Escritorio/Home del SO
        java.io.File home = FileSystemView.getFileSystemView().getHomeDirectory();
        JFileChooser chooser = new JFileChooser(home);
        chooser.setDialogTitle("Selecciona la carpeta de destino");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setApproveButtonText("Seleccionar");

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File dir = chooser.getSelectedFile();
            return dir != null ? dir.getAbsolutePath() : null;
        }
        return null;
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
    public void setDocumentos(List<String> nuevos) {
        this.documentos = nuevos;
        cargarDocumentos();
    }

    public void mostrarResultadosBusqueda(List<String> documentosFiltrados) {
        if (documentosFiltrados == null || documentosFiltrados.isEmpty()) {
            contenedorLista.removeAll();
            Texto sinResultados = new Texto("No se encontraron documentos que coincidan con la búsqueda", TipoTexto.NORMAL, "VERDE");
            sinResultados.setAlignmentX(CENTER_ALIGNMENT);
            contenedorLista.add(sinResultados);
            contenedorLista.revalidate();
            contenedorLista.repaint();
        } else {
            cargarDocumentos(documentosFiltrados);
        }
    }
}
