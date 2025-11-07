package co.edu.uptc.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class IngresarCampo extends JTextField{
    
    private Font fuente;
    
    public IngresarCampo(int ancho) {
        super(ancho);   
        setOpaque(false); 
        setPreferredSize(new Dimension(ancho, 35));
        cargarFuente();
        setFont(fuente);
        setForeground(new ColorConstante().getVerdeOscuro());
        setMargin(new java.awt.Insets(1, 5, 1, 10)); 
    }

    private void cargarFuente() {
        try (InputStream resourceStream = getClass().getResourceAsStream("/letras/Cambay/Cambay-Regular.ttf")) {
            if (resourceStream == null) {
                throw new IllegalArgumentException("Recurso de fuente no encontrado en classpath: /letras/Cambay/Cambay-Regular.ttf");
            }
            try (InputStream myStream = new BufferedInputStream(resourceStream)) {
                Font myFont = Font.createFont(Font.TRUETYPE_FONT, myStream); 
                this.fuente = myFont.deriveFont(25f);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.fuente = new Font(Font.SERIF, Font.PLAIN, 25);
        }    
    }

    @Override
    protected void paintComponent(Graphics graphics) {
         graphics.setColor(new ColorConstante().getBlanco());
         graphics.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
         super.paintComponent(graphics);
    }
    @Override
    protected void paintBorder(Graphics graphics) {
         graphics.setColor(new ColorConstante().getVerdeOscuro());
         graphics.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
    }

    public void setPlaceholder(String string) {
        TextPrompt placeholder = new TextPrompt(string, this);
        placeholder.changeAlpha(0.3f);
        placeholder.setHorizontalAlignment(JLabel.CENTER);
    }
}
