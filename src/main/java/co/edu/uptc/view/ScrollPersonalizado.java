package co.edu.uptc.view;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Componente de scroll personalizado con estilo consistente para toda la aplicación.
 * Incluye scrollbar vertical con estilo personalizado y opciones de configuración.
 */
public class ScrollPersonalizado extends JScrollPane {
    
    private final ColorConstante color = new ColorConstante();

    /**
     * Constructor principal que crea un scroll personalizado con opciones completas.
     * @param componente El componente a colocar dentro del scroll
     * @param ancho Ancho preferido del scroll
     * @param alto Alto preferido del scroll
     * @param mostrarBordeVerde Si se debe mostrar borde verde oscuro
     */
    public ScrollPersonalizado(JComponent componente, int ancho, int alto, boolean mostrarBordeVerde) {
        super(componente, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        configurarEstilo(ancho, alto, mostrarBordeVerde);
        personalizarScrollBar();
    }

    /**
     * Constructor simplificado con borde verde por defecto.
     */
    public ScrollPersonalizado(JComponent componente, int ancho, int alto) {
        this(componente, ancho, alto, true);
    }

    /**
     * Constructor para scroll transparente (sin borde, fondo transparente).
     * Útil para componentes como AreaTexto que dibujan su propio fondo.
     */
    public ScrollPersonalizado(JComponent componente, boolean transparente) {
        super(componente, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        if (transparente) {
            configurarEstiloTransparente();
        }
        personalizarScrollBar();
    }

    private void configurarEstilo(int ancho, int alto, boolean mostrarBordeVerde) {
        setPreferredSize(new Dimension(ancho, alto));
        if (mostrarBordeVerde) {
            setBorder(BorderFactory.createLineBorder(color.getVerdeOscuro()));
        }
        getViewport().setBackground(color.getBeige());
    }

    private void configurarEstiloTransparente() {
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(false);
        getViewport().setOpaque(false);
    }

    private void personalizarScrollBar() {
        JScrollBar vertical = getVerticalScrollBar();
        vertical.setPreferredSize(new Dimension(12, 0));
        vertical.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = color.getVerdeOpciones();
                this.trackColor = color.getBeige();
            }

            @Override
            protected javax.swing.JButton createDecreaseButton(int orientation) {
                return crearBotonInvisible();
            }

            @Override
            protected javax.swing.JButton createIncreaseButton(int orientation) {
                return crearBotonInvisible();
            }

            private javax.swing.JButton crearBotonInvisible() {
                javax.swing.JButton boton = new javax.swing.JButton();
                boton.setPreferredSize(new Dimension(0, 0));
                boton.setMinimumSize(new Dimension(0, 0));
                boton.setMaximumSize(new Dimension(0, 0));
                return boton;
            }

            @Override
            protected void paintThumb(java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }
                g.setColor(thumbColor);
                g.fillRoundRect(thumbBounds.x + 2, thumbBounds.y, thumbBounds.width - 4, thumbBounds.height, 8, 8);
            }

            @Override
            protected void paintTrack(java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle trackBounds) {
                g.setColor(trackColor);
                g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            }
        });
    }
}
