package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import com.toedter.calendar.JCalendar;

public class IngresarFecha extends JPanel {
    
    private JTextField campoTexto;
    private JLabel iconoCalendario;
    private Font fuente;
    private JPopupMenu popupCalendario;
    private JCalendar calendario;
    private SimpleDateFormat formatoFecha;
    private boolean seleccionada = false; // true solo cuando el usuario elige explícitamente una fecha
    
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
        
        // Popup con el calendario
        popupCalendario = new JPopupMenu();
        calendario = new JCalendar();
        calendario.setPreferredSize(new Dimension(300, 200));
        popupCalendario.add(calendario);
        
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
        
        // Listener para cuando se selecciona una fecha
        calendario.addPropertyChangeListener("calendar", evt -> {
            Calendar fechaSeleccionada = calendario.getCalendar();
            campoTexto.setText(formatoFecha.format(fechaSeleccionada.getTime()));
            seleccionada = true;
            popupCalendario.setVisible(false);
        });
    }
    
    private void mostrarCalendario() {
        popupCalendario.show(campoTexto, 0, campoTexto.getHeight());
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
