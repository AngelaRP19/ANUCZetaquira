package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class CrearActividad extends JPanel{

    private ActionListener listener;  
    private String [] tiposActividad;
    private String nombreProyecto = "";
    private Texto titulo; //subtitulo
    private Texto subtitulo; //normal
    private Texto instruccion; 

    private IngresarCampo campoNombre;
    private IngresarFecha campoFecha;
    private CajaOpciones tipo;
    private AreaTexto descripcion;

    private Boton crearActividad;
    private Boton cancelar;

    private final ColorConstante color = new ColorConstante();

    public CrearActividad(ActionListener listener, String [] tiposActividad, String nombreProyecto){
        this.listener = listener;
        this.tiposActividad = tiposActividad;
        this.nombreProyecto = nombreProyecto;
        setLayout(new BorderLayout());
        setBackground(color.getBeige());
        setBorder(BorderFactory.createEmptyBorder(10, 15, 20, 10));
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
        this.titulo = new Texto("Crear Actividad", TipoTexto.TITULO, "VERDE");
        this.subtitulo = new Texto("  "+nombreProyecto.toUpperCase() , TipoTexto.NORMAL, "VERDE");
        this.instruccion = new Texto("  Por favor, llene los campos obligatorios * correspondientes a la actividad o evento a registrar", TipoTexto.INSTRUCCION, "VERDE");
    }
    private void camposFormulario(){
        this.campoNombre = new IngresarCampo(13);
        this.campoFecha = new IngresarFecha(320);
        this.tipo = new CajaOpciones(tiposActividad, 283);
        this.descripcion = new AreaTexto(5, 30);
        this.descripcion.setPreferredSize(new Dimension(500, 200));
    }
    private void botones(){
        this.crearActividad = new Boton("Guardar", 160, 40, "GUARDAR_ACTIVIDAD/"+nombreProyecto, 15, color.getVerdeClaro());
        if (nombreProyecto.equals("NUEVO_PROYECTO")) {
            this.cancelar = new Boton("Cancelar", 160, 40, "VOLVER_VER_PROYECTO/NUEVO_PROYECTO", 15, color.getVerdeOscuro());
        } else {
            this.cancelar = new Boton("Cancelar", 160, 40, "VOLVER_BIENVENIDA", 15, color.getVerdeOscuro());
        }
    }

    private void ubicarComponentes(){
        this.agregarCabecera();
        this.agregarContenidoCentral();

        // Pie de página con botones
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pie.setBackground(color.getBeige());
        pie.add(cancelar);
        pie.add(crearActividad);
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

        panelCentral.add(this.panelIzquierdo());
        panelCentral.add(Box.createHorizontalStrut(20));
        panelCentral.add(this.panelDerecho());
        add(panelCentral, BorderLayout.CENTER);

    }
    private JPanel panelDerecho() {
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(color.getBeige());
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
    
        JPanel pDescripcion = crearFila(new Texto("* Descripción:      ", TipoTexto.NORMAL, "VERDE"), descripcion);
        panelDerecho.add(pDescripcion);

        return panelDerecho;
    }


    private JPanel panelIzquierdo(){
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(color.getBeige());
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelIzquierdo.add(Box.createVerticalStrut(20));
        
        JPanel pNombre = crearFila(new Texto(     "* Nombre:  ", TipoTexto.NORMAL, "VERDE"), campoNombre);
        panelIzquierdo.add(pNombre);

        JPanel pFechaInicio = crearFila(new Texto("* Fecha:     ", TipoTexto.NORMAL, "VERDE"), campoFecha);
        panelIzquierdo.add(pFechaInicio);

        JPanel pTipo = crearFila(new Texto(   "* Tipo:       ", TipoTexto.NORMAL, "VERDE"), tipo);
        panelIzquierdo.add(pTipo);
        panelIzquierdo.add(Box.createVerticalStrut(20));

        return panelIzquierdo;
    }

    private void agregarActionListeners(){
        if(listener == null) return;
        this.crearActividad.addActionListener(listener);
        this.cancelar.addActionListener(listener);
        
    }
    
    public String getNombreProyecto() {
        return campoNombre.getText();
    }
    public Date getFechaInicio() {
        return campoFecha.getFechaCalendar();
    }
    public String getTipoActividad() {
        return tipo.getSelectedItem().toString();
    }

    public String getDescripcion() {
        return descripcion.getText();
    }
}
