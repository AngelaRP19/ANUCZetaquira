package co.edu.uptc.view;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Bienvenida extends JPanel{
    
    private ImageIcon imagen;
    private JLabel labelImagen;
    private Texto mensaje;
    private Texto instruccion;

    public Bienvenida (){
        this.inicializar();
        this.leerImagen();
        this.estilo();
        this.agregarComponentes();
    }

    private void inicializar() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new ColorConstante().getBeige());
        
        this.mensaje = new Texto("<html><center>Asociación Municipal de Usuarios<br>Campesinos de Zetaquira<br>ANUC Zetaquira</center></html>", TipoTexto.TITULO, "VERDE");
        this.instruccion = new Texto("Seleccione una opción del menú lateral para comenzar", TipoTexto.INSTRUCCION, "VERDE");
    }

    private void leerImagen() {
        try {
            URL rutaLogo = getClass().getResource("/imagenes/logo2.png");
            if (rutaLogo == null) {
                System.err.println("No se pudo encontrar el recurso: /imagenes/logo2.png");
                return;
            }
            ImageIcon imagenLogo = new ImageIcon(rutaLogo);
            Image redimension = imagenLogo.getImage().getScaledInstance(450, 370, Image.SCALE_DEFAULT);
            ImageIcon iconoEscalado = new ImageIcon(redimension); 
            this.imagen = iconoEscalado;
            this.labelImagen = new JLabel(iconoEscalado);
        } catch (Exception e) {
            System.err.println("Error al cargar el logo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    private void estilo() {
        if (labelImagen != null) {
            labelImagen.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        }
        mensaje.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        mensaje.setHorizontalAlignment(JLabel.CENTER);
        instruccion.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        instruccion.setHorizontalAlignment(JLabel.CENTER);
    }

    private void agregarComponentes() {
        this.add(Box.createRigidArea(new Dimension(0,5)));
        this.add(labelImagen);
        this.add(Box.createRigidArea(new Dimension(0,15)));
        this.add(mensaje);
        this.add(Box.createRigidArea(new Dimension(0,8)));
        this.add(instruccion);
    }
}
