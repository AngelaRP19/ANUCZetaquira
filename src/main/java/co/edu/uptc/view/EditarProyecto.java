package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EditarProyecto extends JPanel{
    
    private  ActionListener listener;  
    private Texto titulo; //subtitulo
    private Texto subtitulo; //normal
    private Texto instruccion; 

    private IngresarCampo campoNombre;
    private IngresarFecha campoFechaInicio;
    private IngresarFecha campoFechaFin;
    private AreaTexto descripcion;
    private CajaOpciones estado;
    private Icon editar;

    private Boton guardarCambios;
    private Boton cancelar;
    private Boton registrarActividad;
    private Boton verActividades;
    private Boton subirDocumento;
    private Boton verDocumentos;
    private Boton eliminarProyecto;

    private List<String> datos;
    private final ColorConstante color = new ColorConstante();

    public EditarProyecto(ActionListener listener, List<String> datos){
        this.listener = listener;
        this.datos = datos;
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
        this.titulo = new Texto("Editar o Eliminar Proyecto", TipoTexto.TITULO, "VERDE");
        this.subtitulo = new Texto("  DATOS DEL PROYECTO: "+this.datos.get(0), TipoTexto.NORMAL, "VERDE");
        this.instruccion = new Texto("Puedes modificar los datos, para guardar los cambios has click en el botón 'GUARDAR CAMBIOS'  ", TipoTexto.INSTRUCCION, "VERDE");
    }
    private void camposFormulario(){
        this.campoNombre = new IngresarCampo(13);
        this.campoNombre.setText(this.datos.get(0));
        this.campoFechaInicio = new IngresarFecha(331);
        setFechaDesdeString(this.campoFechaInicio, this.datos.get(1));
        this.campoFechaFin = new IngresarFecha(331);
        setFechaDesdeString(this.campoFechaFin, this.datos.get(2));
        this.estado = new CajaOpciones(new String[]{"ACTIVO", "FINALIZADO", "SUSPENDIDO"}, 300);
        this.estado.setSelectedItem(this.datos.get(3));
        this.descripcion = new AreaTexto(5, 22);
        this.descripcion.setPreferredSize(new Dimension(400, 120));
        this.descripcion.setText(this.datos.get(4));
        this.editar = this.leerIcono();
    }
    
    private Icon leerIcono() {
        try {
            URL rutaIcono = getClass().getResource("/imagenes/editar.png");
            if (rutaIcono == null) {
                System.err.println("No se pudo encontrar el recurso: /imagenes/editar.png");
                return null;
            }
            ImageIcon iconoOriginal = new ImageIcon(rutaIcono);
            Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            return new ImageIcon(imagenRedimensionada);
        } catch (Exception e) {
            System.err.println("Error al cargar el icono: " + e.getMessage());
            return null;
        }
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
    private void botones(){
        this.guardarCambios = new Boton("Guardar Cambios", 200, 40, "GUARDAR_CAMBIOS", 15, color.getVerdeClaro());
        this.cancelar = new Boton("Cancelar", 200, 40, "VOLVER_VER_PROYECTOS", 15, color.getVerdeOscuro());
        this.subirDocumento = new Boton("Subir documento", 250, 40, "PANEL_SUBIR_DOCUMENTO", 15, color.getVerdeOpciones());
        this.verDocumentos = new Boton("Ver documentos", 250, 40, "PANEL_VER_DOCUMENTOS/"+datos.get(0), 15, color.getVerdeOpciones());
        this.registrarActividad = new Boton("Crear Actividad", 250, 40, "PANEL_REGISTRAR_ACTIVIDAD", 15, color.getVerdeOpciones());
        this.verActividades = new Boton("Ver Actividades", 250, 40, "PANEL_VER_ACTIVIDADES"+datos.get(0), 15, color.getVerdeOpciones());
        this.eliminarProyecto = new Boton("Eliminar Proyecto", 200, 40, "ELIMINAR_PROYECTO/"+datos.get(0), 15, color.getVerdeOscuro());
    }

    private void ubicarComponentes(){
        this.agregarCabecera();
        this.agregarContenidoCentral();

        // Pie de página con botones
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pie.setBackground(color.getBeige());
        pie.add(cancelar);
        pie.add(guardarCambios);
        pie.add(eliminarProyecto);
        add(pie, BorderLayout.SOUTH);
    }
    private void agregarCabecera(){
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        cabecera.setBackground(color.getBeige());
        titulo.setAlignmentX(CENTER_ALIGNMENT);
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
        if (!(campo instanceof IngresarFecha) && editar != null) {
            fila.add(new JLabel(editar));
        }
        return fila;
    }
    private void agregarContenidoCentral(){
    
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.X_AXIS));
        panelCentral.setBackground(color.getBeige());

        panelCentral.add(this.panelIzquierdo());
        panelCentral.add(Box.createHorizontalStrut(20));
        // Panel derecho extraído a método dedicado
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
        this.guardarCambios.addActionListener(listener);
        this.cancelar.addActionListener(listener);
        this.eliminarProyecto.addActionListener(listener);
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
}
