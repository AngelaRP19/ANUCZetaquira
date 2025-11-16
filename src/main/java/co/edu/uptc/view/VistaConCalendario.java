package co.edu.uptc.view;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class VistaConCalendario extends JFrame {

    private IngresarFecha campoFecha;

    public VistaConCalendario() {
        setTitle("Prueba - IngresarFecha");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        // Panel principal con BoxLayout vertical
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(new ColorConstante().getBeige());

        // Título
        JLabel titulo = new JLabel("Prueba del Componente IngresarFecha");
        titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        titulo.setForeground(new ColorConstante().getVerdeOscuro());
        titulo.setAlignmentX(CENTER_ALIGNMENT);

        // Panel para el campo de fecha
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panelFecha.setBackground(new ColorConstante().getBeige());
        
        JLabel etiqueta = new JLabel("Seleccione una fecha:");
        etiqueta.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        etiqueta.setForeground(new ColorConstante().getVerdeOscuro());
        
        campoFecha = new IngresarFecha(300);
        
        panelFecha.add(etiqueta);
        panelFecha.add(campoFecha);

        // Panel para botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setBackground(new ColorConstante().getBeige());
        
        JButton btnObtenerFecha = new JButton("Obtener Fecha Seleccionada");
        btnObtenerFecha.setBackground(new ColorConstante().getVerdeOpciones());
        btnObtenerFecha.setForeground(new ColorConstante().getBlanco());
        btnObtenerFecha.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        
        btnObtenerFecha.addActionListener(e -> {
            String fecha = campoFecha.getFecha();
            if (fecha != null && !fecha.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Fecha seleccionada: " + fecha, 
                    "Fecha", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, seleccione una fecha primero", 
                    "Advertencia", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        panelBotones.add(btnObtenerFecha);

        // Agregar componentes al panel principal
        panelPrincipal.add(javax.swing.Box.createVerticalStrut(30));
        panelPrincipal.add(titulo);
        panelPrincipal.add(javax.swing.Box.createVerticalStrut(20));
        panelPrincipal.add(panelFecha);
        panelPrincipal.add(javax.swing.Box.createVerticalStrut(10));
        panelPrincipal.add(panelBotones);

        setContentPane(panelPrincipal);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Ejecutar GUI en EDT
        javax.swing.SwingUtilities.invokeLater(() -> new VistaConCalendario());
    }
}