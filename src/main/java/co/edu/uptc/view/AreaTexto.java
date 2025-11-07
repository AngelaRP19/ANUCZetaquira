package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class AreaTexto extends JPanel {

    private Font fuente;
    private final JTextArea area;
    private final ScrollPersonalizado scroll;

    public AreaTexto(int filas, int columnas) {
        setOpaque(false); // pintamos nuestro propio fondo redondeado
        setLayout(new BorderLayout());

        // Área de texto interna
        area = new JTextArea(filas, columnas);
        area.setOpaque(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        cargarFuente();
        area.setFont(fuente);
        area.setForeground(new ColorConstante().getVerdeOscuro());
        area.setMargin(new java.awt.Insets(8, 5, 8, 10));
        area.setCaretColor(new ColorConstante().getVerdeOscuro());
    // Mantener la vista siguiendo al cursor cuando se escribe
    DefaultCaret caret = (DefaultCaret) area.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // Scroll que contiene el área de texto usando el componente ScrollPersonalizado
        scroll = new ScrollPersonalizado(area, true);

        // Separar el scrollbar del borde redondeado para que no lo "corte"
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 6)); // margen derecho para ver el borde curvo
        wrapper.add(scroll, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
    }

    private void cargarFuente() {
        try (InputStream resourceStream = getClass().getResourceAsStream("/letras/Cambay/Cambay-Regular.ttf")) {
            if (resourceStream == null) {
                throw new IllegalArgumentException("Recurso de fuente no encontrado en classpath: /letras/Cambay/Cambay-Regular.ttf");
            }
            try (InputStream myStream = new BufferedInputStream(resourceStream)) {
                Font myFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
                this.fuente = myFont.deriveFont(22f);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.fuente = new Font(Font.SERIF, Font.PLAIN, 22);
        }
    }

    // Delegar métodos comunes para mantener compatibilidad
    public String getText() {
        return area.getText();
    }

    public void setText(String text) {
        area.setText(text);
    }

    public void setEditable(boolean editable) {
        area.setEditable(editable);
    }

    public JTextArea getInnerTextArea() {
        return area;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new ColorConstante().getBlanco());
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(new ColorConstante().getVerdeOscuro());
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
    }
    
    public void setPlaceholder(String texto) {
        TextPrompt placeholder = new TextPrompt(texto, area);
        placeholder.changeAlpha(0.3f);
        placeholder.setHorizontalAlignment(JLabel.LEFT);
    }
}
