package co.edu.uptc.view;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class PanelRedondeado extends JPanel {

    private final ColorConstante colores = new ColorConstante();
    private int radio = 18;          
    private float grosorBorde = 1;
    private boolean mostrarBorde = true;

    public PanelRedondeado() {
        super();
        setOpaque(false); 
        setBackground(colores.getBeige());
    }

    public PanelRedondeado(int radio) {
        this();
        this.radio = radio;
    }

    public PanelRedondeado(int radio, float grosorBorde) {
        this(radio);
        this.grosorBorde = grosorBorde;
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
        repaint();
    }

    public float getGrosorBorde() {
        return grosorBorde;
    }

    public void setGrosorBorde(float grosorBorde) {
        this.grosorBorde = grosorBorde;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        int inset = (mostrarBorde && grosorBorde > 0) ? (int) Math.ceil(grosorBorde / 2f) : 0;
        g2.fillRoundRect(inset, inset, getWidth() - (inset * 2), getHeight() - (inset * 2), radio, radio);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (!mostrarBorde || grosorBorde <= 0) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(colores.getVerdeOscuro());
        g2.setStroke(new BasicStroke(grosorBorde));
        int offset = (int) Math.ceil(grosorBorde / 2f);
        int w = getWidth() - offset * 2 - 1;
        int h = getHeight() - offset * 2 - 1;
        g2.drawRoundRect(offset, offset, w, h, radio, radio);
        g2.dispose();
    }

    public void setMostrarBorde(boolean mostrar) {
        this.mostrarBorde = mostrar;
        repaint();
    }
}
