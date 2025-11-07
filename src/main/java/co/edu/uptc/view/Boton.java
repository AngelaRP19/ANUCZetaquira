package co.edu.uptc.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Boton extends JButton{
    private String texto;
    private int ancho;
    private int alto;
    private Font fuente;
    private ColorConstante color;
    private int radioEsquinas;
    private ImageIcon icono;
    private Color originalBackground;
    private Color originalForeground;

    public enum BotonEstilo { LISTA_PROYECTO, NORMAL }


    public Boton(String texto, int ancho, int alto, String comando, int radioEsquinas, Color backColor) {
    this.setActionCommand(comando);
        this.texto = texto;
        this.ancho = ancho;
        this.alto = alto;
        this.color = new ColorConstante();
        this.radioEsquinas = radioEsquinas;
        this.setText(texto);
        style(backColor);
    }

    public Boton(String texto, String comando, int ancho, int alto, BotonEstilo estilo) {
        this.setActionCommand(comando);
        this.texto = texto;
        this.ancho = ancho;
        this.alto = alto;
        this.color = new ColorConstante();
        this.setText(texto);
        if (estilo == BotonEstilo.LISTA_PROYECTO) {
            this.radioEsquinas = 20;
            this.setBackground(color.getBlanco());
            this.setForeground(color.getVerdeOscuro());
            Dimension size = new Dimension(ancho, alto);
            this.setMaximumSize(size);
            this.setPreferredSize(size);
            this.setMinimumSize(size);
            this.setAlignmentX(Component.CENTER_ALIGNMENT);
            this.setFocusPainted(false);
            this.setBorder(BorderFactory.createLineBorder(color.getVerdeOscuro()));
            this.aplicarFuenteNormalTexto();
            this.setContentAreaFilled(false); // usamos pintura personalizada
            habilitarHoverInversion();
        } else {
            this.radioEsquinas = 15;
            style(color.getVerdeOscuro());
            habilitarHoverBasico();
        }
    }

    public Boton(Icon icono, String textoTooltip,int ancho, int alto, String comando) {
        this.setActionCommand(comando);
        this.ancho = ancho;
        this.alto = alto;
        setToolTipText(textoTooltip);
        setIcon(icono);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        Dimension size = new Dimension(ancho, alto);
        this.setMaximumSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
    }
    public Boton(String texto, int ancho, int alto, String comando, Icon icono) {
        this.setActionCommand(comando);
        this.texto = texto;
        this.ancho = ancho;
        this.alto = alto;
        this.color = new ColorConstante();
        this.radioEsquinas = 15;
        this.setIcon(icono);
        this.setText(texto);
        style(this.color.getVerdeOscuro());
    }

    private void style(Color backColor) {
        this.setBackground(backColor);
        this.setForeground(new ColorConstante().getBlanco());
        Dimension size = new Dimension(ancho, alto);
        this.setMaximumSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setFocusPainted(false);
        this.aplicarFuenteNormalTexto();
        this.originalBackground = getBackground();
        this.originalForeground = getForeground();
    }

    private void habilitarHoverBasico() {
        this.originalBackground = getBackground();
        this.originalForeground = getForeground();
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                setBackground(color.getVerdeClaro());
                setForeground(color.getBlanco());
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e){
                setBackground(originalBackground);
                setForeground(originalForeground);
                repaint();
            }
        });
    }

    // Hover para botones de lista de proyectos: invertimos a verde oscuro con texto blanco
    private void habilitarHoverInversion() {
        this.originalBackground = getBackground();
        this.originalForeground = getForeground();
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                setBackground(color.getVerdeOscuro());
                setForeground(color.getBlanco());
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e){
                setBackground(originalBackground);
                setForeground(originalForeground);
                repaint();
            }
        });
    }

    public void aplicarFuenteNormalTexto() {
        try (InputStream resourceStream = getClass().getResourceAsStream("/letras/Cambay/Cambay-Bold.ttf")) {
            if (resourceStream != null) {
                try (InputStream myStream = new BufferedInputStream(resourceStream)) {
                    Font myFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
                    this.fuente = myFont.deriveFont(Font.BOLD, 20);
                }
            } else {
                this.fuente = new Font(Font.SERIF, Font.BOLD, 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.fuente = new Font(Font.SERIF, Font.BOLD, 20);
        }
        this.setFont(this.fuente);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (getModel().isArmed()) {
            graphics.setColor(Color.LIGHT_GRAY);
        } else {
            graphics.setColor(getBackground());
        }
        graphics.fillRoundRect(0, 0, getWidth(), getHeight(), this.radioEsquinas, this.radioEsquinas);
        super.paintComponent(graphics);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
    }
}