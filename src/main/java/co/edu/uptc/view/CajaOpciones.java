package co.edu.uptc.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class CajaOpciones extends JComboBox<String> {
    
    private Font fuente;
    
    public CajaOpciones(String[] opciones, int ancho) {
        super(opciones);
        setOpaque(false);
        setPreferredSize(new Dimension(ancho, 35));
        cargarFuente();
        setFont(fuente);
        setForeground(new ColorConstante().getVerdeOscuro());
        setBackground(new ColorConstante().getBlanco());
        setBorder(new EmptyBorder(1, 10, 1, 10));
        
        // Evitar selección de texto visible
        setEditor(null);
        
        // Renderizador personalizado para las opciones del desplegable
        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(fuente);
                setBorder(new EmptyBorder(5, 10, 5, 10));
                
                if (isSelected) {
                    setBackground(new ColorConstante().getVerdeClaro());
                    setForeground(new ColorConstante().getVerdeOscuro());
                } else {
                    setBackground(new ColorConstante().getBlanco());
                    setForeground(new ColorConstante().getVerdeOscuro());
                }
                return this;
            }
        });

        setUI(new CustomComboUI());
    }

    private void cargarFuente() {
        try (InputStream resourceStream = getClass().getResourceAsStream("/letras/Cambay/Cambay-Regular.ttf")) {
            if (resourceStream == null) {
                throw new IllegalArgumentException("Recurso de fuente no encontrado en classpath: /letras/Cambay/Cambay-Regular.ttf");
            }
            try (InputStream myStream = new BufferedInputStream(resourceStream)) {
                Font myFont = Font.createFont(Font.TRUETYPE_FONT, myStream); 
                this.fuente = myFont.deriveFont(20f);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.fuente = new Font(Font.SERIF, Font.PLAIN, 20);
        }    
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fondo blanco con bordes redondeados, con inset de 1px para que el borde no se corte
        g2d.setColor(new ColorConstante().getBlanco());
        g2d.fillRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
        
        g2d.dispose();
        super.paintComponent(graphics);
    }
    
    @Override
    protected void paintBorder(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Borde verde oscuro con offset de 1px para evitar corte en los bordes
        g2d.setColor(new ColorConstante().getVerdeOscuro());
        g2d.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);
        
        g2d.dispose();
    }

    /**
     * UI personalizada del combo para usar un popup con esquinas redondeadas
     * y con ScrollPersonalizado para la lista de opciones.
     */
    private class CustomComboUI extends BasicComboBoxUI {
        @Override
        protected ComboPopup createPopup() {
            return new RoundedComboPopup(comboBox);
        }
        
        @Override
        protected javax.swing.JButton createArrowButton() {
            javax.swing.JButton button = new javax.swing.JButton() {
                @Override
                public void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Fondo del botón
                    g2.setColor(new ColorConstante().getVerdeOpciones());
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Flecha hacia abajo
                    int arrowSize = 6;
                    int x = getWidth() / 2;
                    int y = getHeight() / 2;
                    
                    int[] xPoints = {x - arrowSize, x + arrowSize, x};
                    int[] yPoints = {y - 2, y - 2, y + arrowSize - 2};
                    
                    g2.setColor(new ColorConstante().getBlanco());
                    g2.fillPolygon(xPoints, yPoints, 3);
                    
                    g2.dispose();
                }
            };
            button.setPreferredSize(new Dimension(20, 35)); // Reducir ancho del botón
            button.setBorder(new EmptyBorder(0, 0, 0, 0));
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
            return button;
        }
    }

    /**
     * Popup redondeado con scrollbar personalizado reutilizando ScrollPersonalizado.
     */
    private class RoundedComboPopup extends BasicComboPopup {
        private static final long serialVersionUID = 1L;

        RoundedComboPopup(JComboBox<Object> combo) {
            super(combo);
            setOpaque(false); // Permitimos pintar el fondo manualmente
            setBorder(new EmptyBorder(6, 6, 6, 6)); // margen para el borde redondeado
        }

        @Override
        protected javax.swing.JScrollPane createScroller() {
            int ancho = Math.max(150, CajaOpciones.this.getWidth());
            int alto = 150;
            ScrollPersonalizado scroll = new ScrollPersonalizado(list, ancho, alto, false);
            scroll.getViewport().setBackground(new ColorConstante().getBlanco());
            return scroll;
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new ColorConstante().getBlanco());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
            g2.setColor(new ColorConstante().getVerdeOscuro());
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}

