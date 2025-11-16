package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class CrearProyecto extends JPanel{
    
    private  ActionListener listener;  
    private Texto titulo; //subtitulo
    private Texto subtitulo; //normal
    private Texto instruccion; 

    private IngresarCampo campoNombre;
    private IngresarFecha campoFechaInicio;
    private IngresarFecha campoFechaFin;
    private AreaTexto descripcion;
    private CajaOpciones estado;

    private Boton crearProyecto;
    private Boton cancelar;
    private Boton registrarActividad;
    private Boton verActividades;
    private Boton subirDocumento;
    private Boton verDocumentos;

    private final ColorConstante color = new ColorConstante();

    public CrearProyecto(ActionListener listener){
        this.listener = listener;
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
        this.titulo = new Texto("Crear Proyecto", TipoTexto.TITULO, "VERDE");
        this.subtitulo = new Texto("  DATOS DEL PROYECTO", TipoTexto.NORMAL, "VERDE");
        this.instruccion = new Texto("  Por favor, llene los campos obligatorios *", TipoTexto.INSTRUCCION, "VERDE");
    }
    private void camposFormulario(){
        this.campoNombre = new IngresarCampo(14);
        this.campoFechaInicio = new IngresarFecha(340);
        this.campoFechaFin = new IngresarFecha(340);
        this.descripcion = new AreaTexto(5, 22);
        this.descripcion.setPreferredSize(new Dimension(400, 120));
        this.estado = new CajaOpciones(new String[]{"ACTIVO", "FINALIZADO", "SUSPENDIDO"}, 300);
    }
    private void botones(){
        this.crearProyecto = new Boton("Guardar", 160, 40, "GUARDAR_PROYECTO", 15, color.getVerdeClaro());
        this.cancelar = new Boton("Cancelar", 160, 40, "VOLVER_BIENVENIDA", 15, color.getVerdeOscuro());
        this.subirDocumento = new Boton("Subir documento", 250, 40, "PANEL_SUBIR_DOCUMENTO_NUEVO_PROYECTO", 15, color.getVerdeOpciones());
        this.verDocumentos = new Boton("Ver documentos", 250, 40, "PANEL_VER_DOCUMENTOS_NUEVO_PROYECTO", 15, color.getVerdeOpciones());
        this.registrarActividad = new Boton("Crear Actividad", 250, 40, "PANEL_AGREGAR_ACTIVIDAD_NUEVO_PROYECTO", 15, color.getVerdeOpciones());
        this.verActividades = new Boton("Ver Actividades", 250, 40, "PANEL_VER_ACTIVIDADES_NUEVO_PROYECTO", 15, color.getVerdeOpciones());
    }

    private void ubicarComponentes(){
        this.agregarCabecera();
        this.agregarContenidoCentral();

        // Pie de página con botones
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pie.setBackground(color.getBeige());
        pie.add(cancelar);
        pie.add(crearProyecto);
        add(pie, BorderLayout.SOUTH);
    }
    private void agregarCabecera(){
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        cabecera.setBackground(color.getBeige());
        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(5)); 
        //cabecera.add(crearSubPanel(subtitulo));
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

        panelCentral.add(this.panelIzquierdo());
        panelCentral.add(Box.createHorizontalStrut(20));
        panelCentral.add(this.panelDerecho());
        add(panelCentral, BorderLayout.CENTER);

    }
    private JPanel panelDerecho() {
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(color.getBeige());
        panelDerecho.setAlignmentY(CENTER_ALIGNMENT);
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 150));

        Texto lblDocs = new Texto("Documentos", TipoTexto.NORMAL, "VERDE");
        lblDocs.setAlignmentX(CENTER_ALIGNMENT);
        panelDerecho.add(lblDocs);
        panelDerecho.add(Box.createVerticalStrut(8));
        panelDerecho.add(crearSubPanel(subirDocumento));
        panelDerecho.add(crearSubPanel(verDocumentos));

        panelDerecho.add(Box.createVerticalStrut(16));

        Texto lblActs = new Texto("Actividades", TipoTexto.NORMAL, "VERDE");
        lblActs.setAlignmentX(CENTER_ALIGNMENT);
        panelDerecho.add(lblActs);
        panelDerecho.add(Box.createVerticalStrut(8));
        panelDerecho.add(crearSubPanel(registrarActividad));
        panelDerecho.add(crearSubPanel(verActividades));

        return panelDerecho;
    }

    private JPanel crearSubPanel(JComponent comp) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        p.setBackground(color.getBeige());
        p.add(comp);
        p.setAlignmentX(CENTER_ALIGNMENT);
        return p;
    }

    private JPanel panelIzquierdo(){
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(color.getBeige());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pNombre = crearFila(new Texto(     "* Nombre:                         ", TipoTexto.NORMAL, "VERDE"), campoNombre);
        panelIzquierdo.add(pNombre);

        JPanel pFechaInicio = crearFila(new Texto("* Fecha de Inicio:              ", TipoTexto.NORMAL, "VERDE"), campoFechaInicio);
        panelIzquierdo.add(pFechaInicio);

        JPanel pFechaFin = crearFila(new Texto(   "  Fecha de Finalización:    ", TipoTexto.NORMAL, "VERDE"), campoFechaFin);
        panelIzquierdo.add(pFechaFin);

        JPanel pEstado = crearFila(new Texto(      "* Estado:                          ", TipoTexto.NORMAL, "VERDE"), estado);
        panelIzquierdo.add(pEstado);

        JPanel pDescripcion = crearFila(new Texto("* Descripción:    ", TipoTexto.NORMAL, "VERDE"), descripcion);
        panelIzquierdo.add(pDescripcion);
        return panelIzquierdo;
    }

    private void agregarActionListeners(){
        if(listener == null) return;
        this.crearProyecto.addActionListener(listener);
        this.cancelar.addActionListener(listener);
        this.subirDocumento.addActionListener(listener);
        this.verDocumentos.addActionListener(listener);
        this.registrarActividad.addActionListener(listener);
        this.verActividades.addActionListener(listener);
    }

    
    public String getNombreProyecto() {
        return campoNombre.getText();
    }
    public Date getFechaInicio() {
        return campoFechaInicio.getFechaCalendar();
    }
    public Date getFechaFin() {
        return campoFechaFin.getFechaCalendar();
    }
    public String getEstado() {
        return (String) estado.getSelectedItem();
    }
    public String getDescripcion() {
        return descripcion.getText();
    }
    
    // Métodos para establecer valores (para datos temporales)
    public void setNombreProyecto(String nombre) {
        if (nombre != null) {
            campoNombre.setText(nombre);
        }
    }
    
    public void setFechaInicio(Date fecha) {
        if (fecha != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecha);
            campoFechaInicio.setFecha(cal);
        }
    }
    
    public void setFechaFin(Date fecha) {
        if (fecha != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecha);
            campoFechaFin.setFecha(cal);
        }
    }
    
    public void setEstado(String estadoTexto) {
        if (estadoTexto != null) {
            estado.setSelectedItem(estadoTexto);
        }
    }
    
    public void setDescripcion(String desc) {
        if (desc != null) {
            descripcion.setText(desc);
        }
    }
    
    // Método para cargar todos los datos temporales
    public void cargarDatosTemporales(String nombre, Date fechaInicio, Date fechaFin, String estado, String descripcion) {
        setNombreProyecto(nombre);
        setFechaInicio(fechaInicio);
        setFechaFin(fechaFin);
        setEstado(estado);
        setDescripcion(descripcion);
        System.out.println("[LOG] CrearProyecto.cargarDatosTemporales() - Datos temporales cargados en el formulario");
    }
}
