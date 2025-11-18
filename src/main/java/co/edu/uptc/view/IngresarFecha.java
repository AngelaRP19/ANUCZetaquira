package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.toedter.calendar.JCalendar;

public class IngresarFecha extends JPanel {
    
    private JTextField campoTexto;
    private JLabel iconoCalendario;
    private Font fuente;
    private JDialog dialogoCalendario;
    private JCalendar calendario;
    private SimpleDateFormat formatoFecha;
    private boolean seleccionada = false;
    private AWTEventListener clickFueraListener; 
    
    public IngresarFecha(int ancho) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(ancho, 35));
        
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        
        inicializarComponentes(ancho);
        cargarFuente();
        configurarEventos();
    }
    
    private void inicializarComponentes(int ancho) {
        // Campo de texto personalizado
        campoTexto = new JTextField() {
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
        };
        
        campoTexto.setOpaque(false);
        campoTexto.setEditable(false);
        campoTexto.setForeground(new ColorConstante().getVerdeOscuro());
        campoTexto.setMargin(new java.awt.Insets(1, 5, 1, 20));
        
        // Icono de calendario
        try {
            URL rutaIcono = getClass().getResource("/imagenes/calendario.png");
            if (rutaIcono == null) {
                System.err.println("No se pudo encontrar el recurso: /imagenes/calendario.png");
                // Usar emoji como fallback
                iconoCalendario = new JLabel("📅");
                iconoCalendario.setFont(new Font("Arial", Font.PLAIN, 20));
            } else {
                ImageIcon iconoOriginal = new ImageIcon(rutaIcono);
                // Redimensionar el icono a 25x25
                Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                ImageIcon iconoRedimensionado = new ImageIcon(imagenRedimensionada);
                iconoCalendario = new JLabel(iconoRedimensionado);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el icono: " + e.getMessage());
            iconoCalendario = new JLabel("📅");
            iconoCalendario.setFont(new Font("Arial", Font.PLAIN, 20));
        }
        iconoCalendario.setPreferredSize(new Dimension(30, 30));
        
        // Panel para el icono
        JPanel panelIcono = new JPanel();
        panelIcono.setOpaque(false);
        panelIcono.add(iconoCalendario);
        
        // Dialogo con el calendario
        calendario = new JCalendar();
        calendario.setPreferredSize(new Dimension(300, 200));
        
        // Agregar componentes
        add(campoTexto, BorderLayout.CENTER);
        add(panelIcono, BorderLayout.EAST);
    }
    
    private void cargarFuente() {
        try {
            InputStream myStream = new BufferedInputStream(new FileInputStream("/resources/Cambay/Cambay-Regular.ttf"));
            Font myFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
            this.fuente = myFont.deriveFont(25f);
            myStream.close();
        } catch (Exception e) {
            this.fuente = new Font(Font.SERIF, Font.PLAIN, 25);
        }
        campoTexto.setFont(fuente);
    }
    
    private void configurarEventos() {
        // Click en el campo de texto
        campoTexto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarCalendario();
            }
        });

        // Click en el icono
        iconoCalendario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarCalendario();
            }
        });

        // Listener para cuando se selecciona un día (no solo cambio de mes/año)
        calendario.getDayChooser().addPropertyChangeListener("day", evt -> {
            // Solo cerrar el dialogo si el usuario selecciona un día
            Calendar fechaSeleccionada = calendario.getCalendar();
            campoTexto.setText(formatoFecha.format(fechaSeleccionada.getTime()));
            seleccionada = true;
            cerrarCalendario();
        });
    }
    
    private void mostrarCalendario() {
        if (dialogoCalendario == null || !dialogoCalendario.isVisible()) {
            dialogoCalendario = new JDialog(SwingUtilities.getWindowAncestor(this));
            dialogoCalendario.setUndecorated(true);
            dialogoCalendario.setModal(false);
            dialogoCalendario.add(calendario);
            dialogoCalendario.pack();
            
            // Posicionar el dialogo debajo del campo de texto
            java.awt.Point ubicacion = campoTexto.getLocationOnScreen();
            dialogoCalendario.setLocation(ubicacion.x, ubicacion.y + campoTexto.getHeight());
            
            // Agregar listener para cerrar al hacer clic fuera
            agregarListenerClickFuera();
            
            dialogoCalendario.setVisible(true);
        }
    }
    
    private void agregarListenerClickFuera() {
        // Remover listener anterior si existe
        if (clickFueraListener != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(clickFueraListener);
        }
        
        clickFueraListener = new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event instanceof MouseEvent && event.getID() == MouseEvent.MOUSE_PRESSED) {
                    MouseEvent mouseEvent = (MouseEvent) event;
                    
                    // Verificar si el clic fue fuera del diálogo del calendario
                    if (dialogoCalendario != null && dialogoCalendario.isVisible()) {
                        java.awt.Point puntoClick = mouseEvent.getLocationOnScreen();
                        java.awt.Rectangle limites = dialogoCalendario.getBounds();
                        
                        // Si el clic no está dentro del diálogo, cerrarlo
                        if (!limites.contains(puntoClick)) {
                            cerrarCalendario();
                        }
                    }
                }
            }
        };
        
        Toolkit.getDefaultToolkit().addAWTEventListener(
            clickFueraListener,
            AWTEvent.MOUSE_EVENT_MASK
        );
    }
    
    private void cerrarCalendario() {
        if (dialogoCalendario != null && dialogoCalendario.isVisible()) {
            dialogoCalendario.dispose();
            
            // Remover el listener cuando se cierra
            if (clickFueraListener != null) {
                Toolkit.getDefaultToolkit().removeAWTEventListener(clickFueraListener);
                clickFueraListener = null;
            }
        }
    }
    
    public String getFecha() {
        return seleccionada ? campoTexto.getText() : "";
    }
    
    public Date getFechaCalendar() {
        return seleccionada ? calendario.getCalendar().getTime() : null;
    }
    
    public void setFecha(Calendar fecha) {
        calendario.setCalendar(fecha);
        campoTexto.setText(formatoFecha.format(fecha.getTime()));
        seleccionada = true;
    }
    
    public void setPlaceholder(String texto) {
        TextPrompt placeholder = new TextPrompt(texto, campoTexto);
        placeholder.changeAlpha(0.3f);
        placeholder.setHorizontalAlignment(JLabel.LEFT);
    }
    
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
    }


   
}
