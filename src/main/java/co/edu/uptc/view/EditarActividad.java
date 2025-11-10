package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;



public class EditarActividad extends JPanel{
    
    private ActionListener listener;  
    private String [] tiposActividad;
    private List<String> datosActividad;
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
    private Boton eliminar;

    private final ColorConstante color = new ColorConstante();

    public EditarActividad(ActionListener listener, String [] tiposActividad, String nombreProyecto, List<String> datosActividad){
        this.listener = listener;
        this.tiposActividad = tiposActividad;
        this.nombreProyecto = nombreProyecto;
        this.datosActividad = datosActividad;
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
        this.titulo = new Texto("Actualizar o Eliminar Actividad", TipoTexto.TITULO, "VERDE");
        this.subtitulo = new Texto(nombreProyecto.toUpperCase() , TipoTexto.NORMAL, "VERDE");
        this.instruccion = new Texto("<html> Puedes modificar la información de los campos, para guardar los cambios has click en el botón 'Guardar',  para eliminar el registro <br> has click en el botón 'Eliminar'</html>", TipoTexto.INSTRUCCION, "VERDE");
    }
    private void camposFormulario(){
        this.campoNombre = new IngresarCampo(15);
        this.campoNombre.setText(datosActividad.get(0));
        this.campoFecha = new IngresarFecha(365);
        setFechaDesdeString(this.campoFecha, this.datosActividad.get(1));
        this.tipo = new CajaOpciones(tiposActividad, 330);
        this.tipo.setSelectedItem(datosActividad.get(2));
        this.descripcion = new AreaTexto(5, 22);
        this.descripcion.setPreferredSize(new Dimension(450, 200));
        this.descripcion.setText(datosActividad.get(3));
    }
    private void botones(){
        this.crearActividad = new Boton("Actualizar", 160, 40, "EDITAR_ACTIVIDAD"+nombreProyecto, 15, color.getVerdeClaro());
        this.cancelar = new Boton("Cancelar", 160, 40, "VOLVER_VER_ACTIVIDADES", 15, color.getVerdeOscuro());
        this.eliminar = new Boton("Eliminar", 160, 40, "ELIMINAR_ACTIVIDAD"+nombreProyecto, 15, color.getVerdeOscuro());
    }

    private void setFechaDesdeString(IngresarFecha campo, String fechaStr) {
        if (fechaStr != null && !fechaStr.isEmpty()) {
            try {
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                Date fecha = formato.parse(fechaStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(fecha);
                campo.setFecha(cal);
            } catch (ParseException e) {
                System.err.println("Error al parsear fecha: " + fechaStr);
            }
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
        pie.add(eliminar);
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
