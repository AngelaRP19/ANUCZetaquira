package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Principal extends JPanel{

    private final ActionListener listener;        
    private Boton registrarProyecto;
    private Boton verProyectos;
    private Boton manualUsuario;
    private Boton cerrar;
    private Boton minimizar;
    private JPanel logo;
    private JPanel principal;
    private JPanel menuLateral;
    private ColorConstante color;

//recibir por parametro el panel de información.
    public Principal(ActionListener listener, JPanel principal) {
        this.listener = listener;
        this.principal = principal;
        inicializarComponentes();
        agregarComponentes();
        agregarActionListener();
    }

    private void agregarActionListener() {
        this.registrarProyecto.addActionListener(listener);
        this.verProyectos.addActionListener(listener); 
        this.manualUsuario.addActionListener(listener);
        this.cerrar.addActionListener(listener);
        this.minimizar.addActionListener(listener);  
    }

    private void inicializarComponentes() {
        this.color = new ColorConstante();
        this.registrarProyecto = new Boton("Registrar Proyecto", 300, 50, "REGISTRAR_PROYECTO", 15, color.getVerdeOpciones());
        this.verProyectos = new Boton("Ver Proyectos", 300, 50, "VER_PROYECTOS", 15, color.getVerdeOpciones());
        this.manualUsuario = new Boton("Manual de Usuario", 300, 50, "MANUAL_USUARIO", 15, color.getVerdeOscuro());
        this.botonCerrar();
        this.botonMinimizar();  
        this.logo = new JPanel();
        this.menuLateral = new JPanel();
        this.leerLogo();
        this.setBackground(new ColorConstante().getBlanco());
    }

    private void botonMinimizar() {
        this.minimizar = new Boton("-",60, 60, "MINIMIZAR",10, color.getBlanco());
        Font fuente = new Font(this.minimizar.getFont().getFontName(), Font.BOLD, 50);
        this.minimizar.setFont(fuente);
        this.minimizar.setForeground(new ColorConstante().getVerdeOscuro());
    }

    private void botonCerrar() {
        this.cerrar = new Boton("X",60, 60, "CERRAR",10, color.getBlanco());
        Font fuente = new Font(this.cerrar.getFont().getFontName(), Font.BOLD, 30);
        this.cerrar.setFont(fuente);
        this.cerrar.setForeground(new ColorConstante().getVerdeOscuro());
    }


    private void leerLogo() {
        try {
            URL rutaLogo = getClass().getResource("/imagenes/logo2.png");
            if (rutaLogo == null) {
                System.err.println("No se pudo encontrar el recurso: /imagenes/logo2.png");
                return;
            }
            ImageIcon imagenLogo = new ImageIcon(rutaLogo);
            Image redimension = imagenLogo.getImage().getScaledInstance(290, 200, Image.SCALE_DEFAULT);
            ImageIcon iconoEscalado = new ImageIcon(redimension); 
            this.logo.add(new JLabel(iconoEscalado));
            this.logo.setBackground(new ColorConstante().getBlanco());
        } catch (Exception e) {
            System.err.println("Error al cargar el logo: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void agregarComponentes() {
        this.setLayout(new BorderLayout());
        this.agregarCabecera();
        this.agregarMenuLateral();
        this.agregarPanelPrincipal();
        this.agregarPiePagina();
    }

    private void agregarCabecera() {
        JPanel cabecera = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        cabecera.setBackground(new ColorConstante().getBlanco());
        cabecera.add(this.minimizar);
        cabecera.add(this.cerrar);
        this.add(cabecera, BorderLayout.NORTH);
    }

    private void agregarMenuLateral() {
        // Panel Lateral (con borde general)
        this.menuLateral.setLayout(new BorderLayout());
        this.menuLateral.setBackground(new ColorConstante().getBlanco());
        this.menuLateral.setBorder(BorderFactory.createLineBorder(new ColorConstante().getVerdeOscuro(), 1));

        // Contenedor con padding interno para separar el contenido del borde
        JPanel contenedorContenido = new JPanel();
        contenedorContenido.setLayout(new BorderLayout());
        contenedorContenido.setBackground(new ColorConstante().getBlanco());
        contenedorContenido.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20)); // top, left, bottom, right

        // Panel vertical para los componentes (logo, título, botones)
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(new ColorConstante().getBlanco());

        contenido.add(Box.createRigidArea(new Dimension(0, 10)));
        logo.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        contenido.add(logo);

        contenido.add(Box.createRigidArea(new Dimension(0, 8)));
        
        Texto titulo = new Texto("<html>Gestión de<br>Proyectos</html>", TipoTexto.SUBTITULO, "VERDE");
        titulo.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        titulo.setHorizontalAlignment(JLabel.CENTER);
        contenido.add(titulo);

        contenido.add(Box.createRigidArea(new Dimension(0, 20)));

        this.registrarProyecto.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        contenido.add(this.registrarProyecto);

        contenido.add(Box.createRigidArea(new Dimension(0, 15)));

        this.verProyectos.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        contenido.add(this.verProyectos);

        contenido.add(Box.createRigidArea(new Dimension(0, 20)));
        this.manualUsuario.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        contenido.add(this.manualUsuario);

        contenido.add(Box.createRigidArea(new Dimension(0, 20)));

        // Montar jerarquía: contenido -> contenedorContenido -> menuLateral -> this
        contenedorContenido.add(contenido, BorderLayout.CENTER);
        this.menuLateral.add(contenedorContenido, BorderLayout.CENTER);

        this.add(menuLateral, BorderLayout.WEST);
    }
    
    private void agregarPiePagina() {
        JPanel panelPiePagina = new JPanel();
        panelPiePagina.setLayout(new BoxLayout(panelPiePagina, BoxLayout.Y_AXIS));
        panelPiePagina.setBackground(new ColorConstante().getBlanco());

        Texto direccion = new Texto("Calle 3 N° 4 – 87, Barrio El Paraíso, Zetaquira - Boyacá.", TipoTexto.INSTRUCCION, "VERDE");
        direccion.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        Texto contacto = new Texto("Celular: 3102731504, Correo Electrónico: anuczetaquira@yahoo.com", TipoTexto.INSTRUCCION, "VERDE");
        contacto.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        panelPiePagina.add(Box.createRigidArea(new Dimension(0, 5)));
        panelPiePagina.add(direccion);
        panelPiePagina.add(Box.createRigidArea(new Dimension(0, 5)));
        panelPiePagina.add(contacto);
        panelPiePagina.add(Box.createRigidArea(new Dimension(0, 10)));

        this.add(panelPiePagina, BorderLayout.SOUTH);
    }

    private void agregarPanelPrincipal() {
        this.principal.setBackground(new ColorConstante().getBeige());
        this.add(this.principal, BorderLayout.CENTER);
    }
    
}
