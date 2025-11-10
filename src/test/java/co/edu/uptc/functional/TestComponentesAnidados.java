package co.edu.uptc.functional;

import co.edu.uptc.view.*;
import co.edu.uptc.presenter.Presenter;

import javax.swing.*;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Test para encontrar componentes anidados en paneles
 */
public class TestComponentesAnidados {
    
    private static VistaGestor vista;
    private static Presenter presenter;
    
    public static void main(String[] args) {
        try {
            System.out.println("🔍 TEST DE COMPONENTES ANIDADOS");
            System.out.println("===============================");
            
            setup();
            
            testComponentesSubirDocumento();
            testComponentesCrearProyecto();
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }
    
    private static void setup() throws Exception {
        presenter = new Presenter();
        vista = new VistaGestor(presenter);
        vista.setSize(800, 600);
        vista.setVisible(true);
    }
    
    private static void testComponentesSubirDocumento() throws Exception {
        System.out.println("\n📁 TEST: Componentes en SubirDocumento");
        System.out.println("=====================================");
        
        // Navegar a subir documento
        String[] tipos = {"ACTA", "PROPUESTA"};
        vista.subirDocumento(tipos, "TEST_PROYECTO");
        Thread.sleep(200);
        
        // Obtener panel
        SubirDocumento panel = obtenerPanelDentroDePrincipal(SubirDocumento.class);
        
        // Buscar todos los componentes recursivamente
        List<Component> todosComponentes = findAllComponents(panel);
        
        System.out.println("📊 Análisis completo de componentes:");
        System.out.println("   📦 Total componentes (incluyendo anidados): " + todosComponentes.size());
        
        int botones = 0;
        int campos = 0;
        int areasTexto = 0;
        int cajasOpciones = 0;
        int otros = 0;
        
        for (Component comp : todosComponentes) {
            if (comp instanceof Boton) {
                Boton boton = (Boton) comp;
                String comando = boton.getActionCommand();
                System.out.println("   🔘 Botón: " + comando + " (texto: " + boton.getText() + ")");
                botones++;
            } else if (comp instanceof IngresarCampo) {
                System.out.println("   📝 Campo de texto: " + comp.getClass().getSimpleName());
                campos++;
            } else if (comp instanceof javax.swing.JTextArea) {
                System.out.println("   📄 Área de texto");
                areasTexto++;
            } else if (comp instanceof CajaOpciones) {
                System.out.println("   📋 Caja de opciones");
                cajasOpciones++;
            } else if (comp instanceof JPanel || comp instanceof Box) {
                // Ignorar paneles contenedores
            } else if (comp instanceof Texto) {
                System.out.println("   📖 Texto: " + ((Texto)comp).getText());
            } else {
                System.out.println("   ❓ Otro: " + comp.getClass().getSimpleName());
                otros++;
            }
        }
        
        System.out.println("\n📈 Resumen:");
        System.out.println("   🔘 Botones: " + botones);
        System.out.println("   📝 Campos: " + campos);
        System.out.println("   📄 Áreas: " + areasTexto);
        System.out.println("   📋 Cajas opciones: " + cajasOpciones);
        System.out.println("   ❓ Otros: " + otros);
        
        if (botones > 0) {
            System.out.println("   ✅ PANEL TIENE BOTONES FUNCIONALES");
        } else {
            System.out.println("   ❌ PANEL NO TIENE BOTONES");
        }
    }
    
    private static void testComponentesCrearProyecto() throws Exception {
        System.out.println("\n📝 TEST: Componentes en CrearProyecto");
        System.out.println("====================================");
        
        // Navegar a crear proyecto
        vista.crearProyecto();
        Thread.sleep(200);
        
        // Obtener panel
        CrearProyecto panel = obtenerPanelDentroDePrincipal(CrearProyecto.class);
        
        // Buscar todos los componentes recursivamente
        List<Component> todosComponentes = findAllComponents(panel);
        
        System.out.println("📊 Análisis completo de componentes:");
        System.out.println("   📦 Total componentes (incluyendo anidados): " + todosComponentes.size());
        
        int camposFecha = 0;
        int camposTexto = 0;
        int botones = 0;
        
        for (Component comp : todosComponentes) {
            if (comp instanceof IngresarFecha) {
                System.out.println("   📅 Campo de fecha: " + comp.getClass().getSimpleName());
                camposFecha++;
                
                // Intentar obtener la fecha actual
                try {
                    IngresarFecha campoFecha = (IngresarFecha) comp;
                    java.util.Date fechaDate = campoFecha.getFechaCalendar();
                    System.out.println("      📅 Date: " + fechaDate);
                } catch (Exception e) {
                    System.out.println("      ❌ Error leyendo fecha: " + e.getMessage());
                }
                
            } else if (comp instanceof IngresarCampo) {
                System.out.println("   📝 Campo de texto: " + comp.getClass().getSimpleName());
                camposTexto++;
            } else if (comp instanceof Boton) {
                Boton boton = (Boton) comp;
                System.out.println("   🔘 Botón: " + boton.getActionCommand());
                botones++;
            }
        }
        
        System.out.println("\n📈 Resumen:");
        System.out.println("   📅 Campos de fecha: " + camposFecha);
        System.out.println("   📝 Campos de texto: " + camposTexto);
        System.out.println("   🔘 Botones: " + botones);
        
        if (camposFecha > 0) {
            System.out.println("   ✅ PANEL TIENE CAMPOS DE FECHA");
        } else {
            System.out.println("   ❌ PANEL NO TIENE CAMPOS DE FECHA");
        }
    }
    
    /**
     * Encuentra todos los componentes recursivamente
     */
    private static List<Component> findAllComponents(Component container) {
        List<Component> components = new ArrayList<>();
        addComponents(container, components);
        return components;
    }
    
    private static void addComponents(Component component, List<Component> components) {
        if (component == null) return;
        
        components.add(component);
        
        if (component instanceof java.awt.Container) {
            java.awt.Container container = (java.awt.Container) component;
            for (Component child : container.getComponents()) {
                addComponents(child, components);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private static <T> T obtenerPanelDentroDePrincipal(Class<T> panelClass) throws Exception {
        Component currentComponent = vista.getContentPane().getComponent(0);
        if (!(currentComponent instanceof Principal)) {
            throw new Exception("Panel actual no es Principal");
        }
        
        Principal panelPrincipal = (Principal) currentComponent;
        T panel = null;
        for (Component comp : panelPrincipal.getComponents()) {
            if (panelClass.isInstance(comp)) {
                panel = panelClass.cast(comp);
                break;
            }
        }
        if (panel == null) {
            throw new Exception("No se encontró panel " + panelClass.getSimpleName() + " dentro de Principal");
        }
        return panel;
    }
    
    private static void cleanup() {
        if (vista != null) {
            vista.dispose();
        }
    }
}
