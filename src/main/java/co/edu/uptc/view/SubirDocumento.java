package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

public class SubirDocumento extends JPanel{
    
     private final ColorConstante color = new ColorConstante();

    private ActionListener listener;  
    private String [] tiposDocumento;
    private String nombreProyecto = "";

    private Texto titulo; //subtitulo
    private Texto subtitulo; //normal
    private Texto instruccion; 

    private IngresarCampo nombreDoc;
    private CajaOpciones tipo;
    private Boton subirDocumento;

    private Boton guardar;
    private Boton cancelar;
    private Boton limpiarArchivo;
    
    private PanelRedondeado panelArchivoSubido;
    private Texto labelArchivoSubido;

    private String rutaArchivoSeleccionado;

    public SubirDocumento(ActionListener listener, String [] tiposDocumento, String nombreProyecto){
        this.listener = listener;
        this.tiposDocumento = tiposDocumento;
        this.nombreProyecto = nombreProyecto;
        this.rutaArchivoSeleccionado = null;
        setLayout(new BorderLayout());
        setBackground(color.getBeige());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        inicializarComponentes();
        ubicarComponentes();
        agregarActionListeners();
    }

    private void inicializarComponentes(){
        this.encabezados();
        this.camposFormulario();
        this.botones();
    }
    private void encabezados(){
        this.titulo = new Texto("Subir Documento", TipoTexto.TITULO, "VERDE");
        this.subtitulo = new Texto(nombreProyecto.toUpperCase() + ":  DOCUMENTOS. ", TipoTexto.NORMAL, "VERDE");
        this.instruccion = new Texto("<html>  Por favor, llene los campos obligatorios * y seleccione el archivo a subir haciendo clic en el botón subir Archivo, luego guarde <br> el registro en el botón Guardar.</html>", TipoTexto.INSTRUCCION, "VERDE");
    }
    private void camposFormulario(){
        this.nombreDoc = new IngresarCampo(13);
        this.tipo = new CajaOpciones(tiposDocumento, 283);
        
        // Panel redondeado para mostrar el archivo subido
        this.panelArchivoSubido = new PanelRedondeado();
        this.panelArchivoSubido.setLayout(new BoxLayout(panelArchivoSubido, BoxLayout.Y_AXIS));
        this.panelArchivoSubido.setPreferredSize(new java.awt.Dimension(400, 200));
        this.panelArchivoSubido.setBackground(java.awt.Color.WHITE);
        
        this.labelArchivoSubido = new Texto("Ningún archivo seleccionado", TipoTexto.INSTRUCCION, "VERDE");
        this.labelArchivoSubido.setAlignmentX(CENTER_ALIGNMENT);
        this.panelArchivoSubido.add(Box.createVerticalStrut(40));
        this.panelArchivoSubido.add(labelArchivoSubido);
    }
    private void botones(){
        this.subirDocumento = new Boton("Subir Archivo", 200, 40, "SUBIR_ARCHIVO/"+nombreProyecto, 15, color.getVerdeBotones());
        this.limpiarArchivo = new Boton("Limpiar", 120, 35, "LIMPIAR_ARCHIVO/"+nombreProyecto, 14, color.getVerdeOpciones());
        this.guardar = new Boton("Guardar", 160, 40, "GUARDAR_DOCUMENTO/"+nombreProyecto, 15, color.getVerdeClaro());
        this.cancelar = new Boton("Cancelar", 160, 40, "VOLVER_PROYECTO/"+nombreProyecto, 15, color.getVerdeOscuro());
    }

    private void ubicarComponentes(){
        this.agregarCabecera();
        this.agregarContenidoCentral();

        // Pie de página con botones
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pie.setBackground(color.getBeige());
        pie.add(cancelar);
        pie.add(guardar);
        add(pie, BorderLayout.SOUTH);
    }
    private void agregarCabecera(){
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        cabecera.setBackground(color.getBeige());
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(5)); 
        cabecera.add(crearSubPanel(subtitulo));
        cabecera.add(crearSubPanel(instruccion));

        add(cabecera, BorderLayout.NORTH);
    }
    private JPanel crearSubPanel(Texto componente) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        panel.setBackground(color.getBeige());
        panel.add(componente);
        return panel;
    }

    private JPanel crearFila(Texto etiqueta, JComponent campo){
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        fila.setBackground(color.getBeige());
        fila.add(etiqueta);
        fila.add(campo);
        return fila;
    }
    private void agregarContenidoCentral(){
    
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.X_AXIS));
        panelCentral.setBackground(color.getBeige());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 50));
        
        panelCentral.add(this.panelIzquierdo());
        panelCentral.add(Box.createHorizontalStrut(20));
        panelCentral.add(this.panelDerecho());
        add(panelCentral, BorderLayout.CENTER);

    }
    private JPanel panelDerecho() {
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(color.getBeige());

        panelDerecho.add(Box.createVerticalStrut(25));
        
        JPanel pBotonSubir = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pBotonSubir.setBackground(color.getBeige());
        pBotonSubir.add(subirDocumento);
        panelDerecho.add(pBotonSubir);
        
        panelDerecho.add(Box.createVerticalStrut(15));
        
        JPanel pPanelArchivo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pPanelArchivo.setBackground(color.getBeige());
        pPanelArchivo.add(panelArchivoSubido);
        panelDerecho.add(pPanelArchivo);
        
        panelDerecho.add(Box.createVerticalStrut(10));
        
        JPanel pBotonLimpiar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pBotonLimpiar.setBackground(color.getBeige());
        pBotonLimpiar.add(limpiarArchivo);
        panelDerecho.add(pBotonLimpiar);

        return panelDerecho;
    }


    private JPanel panelIzquierdo(){
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(color.getBeige());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelIzquierdo.add(Box.createVerticalStrut(50));
        JPanel pNombre = crearFila(new Texto(     "* Nombre:  ", TipoTexto.NORMAL, "VERDE"), nombreDoc);
        panelIzquierdo.add(pNombre);

        JPanel pTipo = crearFila(new Texto(   "* Tipo:       ", TipoTexto.NORMAL, "VERDE"), tipo);
        panelIzquierdo.add(pTipo);
        panelIzquierdo.add(Box.createVerticalStrut(20));

        return panelIzquierdo;
    }

    private void agregarActionListeners(){
        if(listener == null) return;
        this.subirDocumento.addActionListener(listener);
        this.limpiarArchivo.addActionListener(listener);
        this.guardar.addActionListener(listener);
        this.cancelar.addActionListener(listener);
    }

    
    public String getNombreProyecto() {
        return nombreDoc.getText();
    }
    
    public String getTipoActividad() {
        return tipo.getSelectedItem().toString();
    }
    
 
    public void mostrarArchivoSubido(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            labelArchivoSubido.setText("Ningún archivo seleccionado");
        } else {
            // Mostrar el nombre del archivo (sin ruta completa si es muy largo)
            String nombreCorto = nombreArchivo;
            if (nombreArchivo.length() > 30) {
                nombreCorto = "..." + nombreArchivo.substring(nombreArchivo.length() - 27);
            }
            labelArchivoSubido.setText("<html><center>Archivo:<br>" + nombreCorto + "</center></html>");
        }
        panelArchivoSubido.revalidate();
        panelArchivoSubido.repaint();
    }

    public String seleccionarArchivo() {
        java.io.File home = FileSystemView.getFileSystemView().getHomeDirectory();
        JFileChooser chooser = new JFileChooser(home);
        chooser.setDialogTitle("Selecciona el archivo a subir");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setApproveButtonText("Seleccionar");

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File archivo = chooser.getSelectedFile();
            this.rutaArchivoSeleccionado = archivo.getAbsolutePath();
            return archivo != null ? archivo.getAbsolutePath() : null;
        }
        return null;
    }
    
    /**
     * Limpia el archivo seleccionado, restaurando el panel al estado inicial.
     * Llama internamente a mostrarArchivoSubido(null).
     */
    public void limpiarArchivoSeleccionado() {
        mostrarArchivoSubido(null);
    }

    public Texto getLabelArchivoSubido() {
        return labelArchivoSubido;
    }

    public void setLabelArchivoSubido(Texto labelArchivoSubido) {
        this.labelArchivoSubido = labelArchivoSubido;
    }

    public String getRutaArchivoSeleccionado() {
        return rutaArchivoSeleccionado;
    }

    public void setRutaArchivoSeleccionado(String rutaArchivoSeleccionado) {
        this.rutaArchivoSeleccionado = rutaArchivoSeleccionado;
    }

    

}