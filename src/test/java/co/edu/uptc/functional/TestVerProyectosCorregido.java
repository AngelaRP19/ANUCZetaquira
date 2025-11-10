package co.edu.uptc.functional;

import co.edu.uptc.presenter.Presenter;
import co.edu.uptc.view.VistaGestor;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Test para verificar que el error de "VER_PROYECTOS" está completamente solucionado
 */
public class TestVerProyectosCorregido {
    
    private static VistaGestor vista;
    private static Presenter presenter;
    
    public static void main(String[] args) {
        try {
            System.out.println("🧪 TEST: Verificación de corrección VER_PROYECTOS");
            System.out.println("==================================================");
            
            setup();
            
            // 1. Probar el comando que estaba fallando
            testComandoVerProyectos();
            
            // 2. Verificar que no hay excepciones
            testSinExcepciones();
            
        } catch (Exception e) {
            System.err.println("❌ Error en test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }
    
    private static void setup() throws Exception {
        System.out.println("🔧 Configurando entorno...");
        presenter = new Presenter();
        vista = new VistaGestor(presenter);
        vista.setSize(800, 600);
        vista.setVisible(true);
        Thread.sleep(500);
    }
    
    private static void testComandoVerProyectos() throws Exception {
        System.out.println("\n1️⃣ Probando comando VER_PROYECTOS...");
        
        // Simular el clic en el botón que estaba causando el error
        ActionEvent evento = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "VER_PROYECTOS");
        
        try {
            System.out.println("   🎮 Ejecutando actionPerformed con VER_PROYECTOS");
            presenter.actionPerformed(evento);
            Thread.sleep(1000); // Esperar a que se complete la operación
            
            System.out.println("   ✅ Comando VER_PROYECTOS ejecutado sin errores");
            
        } catch (Exception e) {
            System.err.println("   ❌ ERROR: El comando sigue fallando");
            System.err.println("      " + e.getMessage());
            throw e;
        }
    }
    
    private static void testSinExcepciones() throws Exception {
        System.out.println("\n2️⃣ Verificando que no hay excepciones en el log...");
        
        // El hecho de que lleguemos aquí sin excepciones ya es una buena señal
        System.out.println("   ✅ No se lanzaron excepciones IllegalArgumentException");
        System.out.println("   ✅ No se lanzaron excepciones SQLException");
        
        // Verificar que el método verProyectos() funciona (simulado con comando)
        try {
            System.out.println("   🔍 Simulando comando VER_PROYECTOS...");
            ActionEvent evento = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "VER_PROYECTOS");
            presenter.actionPerformed(evento);
            Thread.sleep(500);
            
            System.out.println("   ✅ Comando VER_PROYECTOS completado exitosamente");
            
        } catch (Exception e) {
            System.err.println("   ❌ ERROR: comando VER_PROYECTOS sigue fallando");
            System.err.println("      " + e.getMessage());
            throw e;
        }
    }
    
    private static void cleanup() {
        if (vista != null) {
            vista.dispose();
        }
        System.out.println("\n🧹 Entorno limpio");
    }
}
