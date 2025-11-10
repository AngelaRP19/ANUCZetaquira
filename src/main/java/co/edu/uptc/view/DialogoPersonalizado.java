package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * Utilidades para mostrar diálogos (JOptionPane) con el estilo visual de la aplicación.
 * Se evita depender directamente de JOptionPane por defecto construyendo un panel
 * redondeado con los colores y fuentes propios.
 */
public class DialogoPersonalizado {

    private static final ColorConstante color = new ColorConstante();
    private static Font fuenteBase;

    /** Configura valores globales (fuente, colores básicos) para JOptionPane. */
    public static void configurarUIDefaults() {
        if (fuenteBase == null) {
            // Intentar reutilizar la fuente usada en Texto NORMAL (fallback si falla)
            fuenteBase = new Font(Font.SERIF, Font.PLAIN, 25);
        }
        UIManager.put("OptionPane.messageFont", fuenteBase);
        UIManager.put("OptionPane.background", color.getBlanco());
        UIManager.put("Panel.background", color.getBlanco());
        UIManager.put("Button.background", color.getVerdeBotones());
        UIManager.put("Button.foreground", color.getBlanco());
    }

    /** Muestra un mensaje informativo estilizado. */
    public static void mostrarInfo(Component parent, String mensaje, String titulo) {
        mostrarDialogo(parent, mensaje, titulo, TipoMensaje.INFO);
    }

    /** Muestra un mensaje de error estilizado. */
    public static void mostrarError(Component parent, String mensaje, String titulo) {
        mostrarDialogo(parent, mensaje, titulo, TipoMensaje.ERROR);
    }

    /** Muestra un diálogo de confirmación estilizado. Retorna true si usuario confirma. */
    public static boolean mostrarConfirmacion(Component parent, String mensaje, String titulo) {
        return mostrarDialogo(parent, mensaje, titulo, TipoMensaje.CONFIRM) == JOptionPane.YES_OPTION;
    }

    private enum TipoMensaje { INFO, ERROR, CONFIRM }

    private static int mostrarDialogo(Component parent, String mensaje, String titulo, TipoMensaje tipo) {
        configurarUIDefaults();
        
        // Obtener la ventana padre y crear el panel oscurecido
        java.awt.Window owner = SwingUtilities.getWindowAncestor(parent);
        JComponent glassPane = null;
        
        if (owner instanceof javax.swing.JFrame) {
            javax.swing.JFrame frame = (javax.swing.JFrame) owner;
            glassPane = (JComponent) frame.getGlassPane();
            configurarGlassPane(glassPane);
            glassPane.setVisible(true);
        } else if (owner instanceof javax.swing.JDialog) {
            javax.swing.JDialog dialog = (javax.swing.JDialog) owner;
            glassPane = (JComponent) dialog.getGlassPane();
            configurarGlassPane(glassPane);
            glassPane.setVisible(true);
        }
        
        final JComponent glassPaneFinal = glassPane;
        
        JPanel contenido = crearPanelContenido(mensaje, tipo);

        // Botones personalizados solo para CONFIRM, para otros se usa showMessageDialog simple
        if (tipo == TipoMensaje.CONFIRM) {
            JButton btnSi = crearBoton("Sí", color.getVerdeClaro(), color.getBlanco());
            JButton btnNo = crearBoton("No", color.getVerdeOscuro(), color.getBlanco());

            final int[] respuesta = { JOptionPane.NO_OPTION };
            btnSi.addActionListener(e -> {
                respuesta[0] = JOptionPane.YES_OPTION;
                cerrarDialogo(btnSi, glassPaneFinal);
            });
            btnNo.addActionListener(e -> {
                respuesta[0] = JOptionPane.NO_OPTION;
                cerrarDialogo(btnNo, glassPaneFinal);
            });

            JPanel panelBotones = new JPanel();
            panelBotones.setOpaque(false);
            panelBotones.add(btnSi);
            panelBotones.add(btnNo);
            contenido.add(panelBotones, BorderLayout.SOUTH);

            JDialog dialog = crearDialog(parent, titulo, contenido);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    if (glassPaneFinal != null) glassPaneFinal.setVisible(false);
                }
            });
            dialog.setVisible(true);
            return respuesta[0];
        } else {
            // Info / Error usan un solo botón OK estándar pero con panel estilizado
            JButton btnOk = crearBoton("OK", color.getVerdeBotones(), color.getBlanco());
            final int[] respuesta = { JOptionPane.OK_OPTION };
            btnOk.addActionListener(e -> {
                respuesta[0] = JOptionPane.OK_OPTION;
                cerrarDialogo(btnOk, glassPaneFinal);
            });
            JPanel panelBotones = new JPanel();
            panelBotones.setOpaque(false);
            panelBotones.add(btnOk);
            contenido.add(panelBotones, BorderLayout.SOUTH);
            JDialog dialog = crearDialog(parent, titulo, contenido);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    if (glassPaneFinal != null) glassPaneFinal.setVisible(false);
                }
            });
            dialog.setVisible(true);
            return respuesta[0];
        }
    }
    
    private static void configurarGlassPane(JComponent glassPane) {
        // Limpiar el glass pane
        glassPane.removeAll();
            glassPane.setLayout(new BorderLayout());
        glassPane.setOpaque(false);
        
        // Crear un panel overlay que pinte el fondo oscuro
        JPanel overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 120)); // Negro con 120/255 de opacidad (~47%)
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        overlayPanel.setOpaque(false);
        
        // Consumir eventos de mouse para bloquear interacción
        overlayPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Consumir el evento
            }
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    // Consumir el evento
                }
        });
        
            glassPane.add(overlayPanel, BorderLayout.CENTER);
            glassPane.revalidate();
            glassPane.repaint();
    }

    private static JPanel crearPanelContenido(String mensaje, TipoMensaje tipo) {
        JPanel wrapper = new PanelRedondeado() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Sombra suave alrededor (glow) para resaltar el diálogo
                int arc = 24;              // curvatura
                int shadowSize = 12;       // grosor/alcance de la sombra
                for (int i = shadowSize; i >= 1; i--) {
                    int alpha = 10 + (int) Math.round(6.0 * i); // 16..82 aprox
                    if (alpha > 90) alpha = 90;
                    g2.setColor(new Color(4, 74, 25, alpha));
                    int grow = (shadowSize - i);
                    // leve desplazamiento hacia abajo para efecto de sombra realista
                    g2.fillRoundRect(grow, grow + 2, getWidth() - 1 - grow * 2, getHeight() - 1 - grow * 2, arc, arc);
                }

                // Cuerpo principal blanco con esquinas redondeadas
                int inset = 6; // deja ver la sombra
                g2.setColor(color.getBlanco());
                g2.fillRoundRect(inset, inset, getWidth() - inset * 2, getHeight() - inset * 2, arc, arc);

                g2.dispose();
            }
        };
        wrapper.setLayout(new BorderLayout());
        wrapper.setPreferredSize(new Dimension(420, 160));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(20, 25, 15, 25));
        wrapper.setBackground(color.getBlanco());

        JLabel lbl = new JLabel("<html><body style='text-align:center;'>" + mensaje + "</body></html>", JLabel.CENTER);
        lbl.setForeground(color.getVerdeOscuro());
        lbl.setFont(fuenteBase);
        wrapper.add(lbl, BorderLayout.CENTER);

        if (tipo == TipoMensaje.ERROR) {
            lbl.setForeground(color.getCafe());
        }
        return wrapper;
    }

    private static JButton crearBoton(String texto, java.awt.Color fondo, java.awt.Color textoColor) {
        JButton b = new JButton(texto);
        b.setFocusPainted(false);
        b.setBackground(fondo);
        b.setForeground(textoColor);
        b.setFont(fuenteBase);
        b.setPreferredSize(new Dimension(110, 40));
        return b;
    }

    private static JDialog crearDialog(Component parent, String titulo, JPanel contenido) {
        java.awt.Window owner = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = owner instanceof java.awt.Frame ? new JDialog((java.awt.Frame) owner, titulo, true)
                : new JDialog((java.awt.Dialog) owner, titulo, true);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(contenido);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        return dialog;
    }

    private static void cerrarDialogo(Component c, JComponent glassPane) {
        JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(c);
        if (dialog != null) {
            dialog.dispose();
        }
        if (glassPane != null) {
            glassPane.setVisible(false);
        }
    }

    // Ejemplo de uso rápido desde cualquier parte:
    // DialogoPersonalizado.mostrarInfo(this, "Documento guardado correctamente", "Éxito");
}
