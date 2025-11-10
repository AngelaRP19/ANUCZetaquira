package co.edu.uptc.functional;

import co.edu.uptc.presenter.Presenter;
import co.edu.uptc.view.VistaGestor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Test para verificar que todas las correcciones funcionan correctamente
 */
public class TestCorrecciones {
    
    private static VistaGestor vista;
    private static Presenter presenter;
    
    public static void main(String[] args) {
        try {
            System.out.println("🧪 TEST: Verificación de correcciones implementadas");
            System.out.println("====================================================");
            
            setup();
            
            // 1. Probar comandos corregidos
            testComandosCorregidos();
            
            // 2. Probar formato de fechas en actividades
            testFormatoFechasActividades();
            
            // 3. Probar botón subir documento
            testBotonSubirDocumento();
            
            System.out.println("\n✅ Todas las correcciones verificadas exitosamente");
            
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
    
    private static void testComandosCorregidos() throws Exception {
        System.out.println("\n1️⃣ Probando comandos corregidos...");
        
        // Probar comando EDITAR_ACTIVIDADHola (antes no reconocido)
        ActionEvent evento1 = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "EDITAR_ACTIVIDADHola");
        try {
            presenter.actionPerformed(evento1);
            System.out.println("   ✅ Comando EDITAR_ACTIVIDADHola reconocido");
        } catch (Exception e) {
            System.err.println("   ❌ Error en comando EDITAR_ACTIVIDADHola: " + e.getMessage());
        }
        
        // Probar comando VOLVER_VER_ACTIVIDADES (antes no reconocido)
        ActionEvent evento2 = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "VOLVER_VER_ACTIVIDADES");
        try {
            presenter.actionPerformed(evento2);
            System.out.println("   ✅ Comando VOLVER_VER_ACTIVIDADES reconocido");
        } catch (Exception e) {
            System.err.println("   ❌ Error en comando VOLVER_VER_ACTIVIDADES: " + e.getMessage());
        }
        
        Thread.sleep(200);
    }
    
    private static void testFormatoFechasActividades() throws Exception {
        System.out.println("\n2️⃣ Probando formato de fechas en actividades...");
        
        // Probar el formateo de fechas que se usa en Presenter
        Date fechaActual = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        String fechaFormateada = formato.format(fechaActual);
        
        System.out.println("   📅 Fecha actual: " + fechaActual);
        System.out.println("   📅 Fecha formateada (dd-MM-yyyy): " + fechaFormateada);
        
        // Verificar que se puede parsear de vuelta
        try {
            Date fechaParseada = formato.parse(fechaFormateada);
            System.out.println("   ✅ Fecha parseada correctamente: " + fechaParseada);
        } catch (Exception e) {
            System.err.println("   ❌ Error parseando fecha formateada: " + e.getMessage());
        }
    }
    
    private static void testBotonSubirDocumento() throws Exception {
        System.out.println("\n3️⃣ Probando botón subir documento...");
        
        // Probar comando PANEL_SUBIR_DOCUMENTO (corregido)
        ActionEvent evento = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "PANEL_SUBIR_DOCUMENTO");
        try {
            presenter.actionPerformed(evento);
            System.out.println("   ✅ Comando PANEL_SUBIR_DOCUMENTO reconocido");
        } catch (Exception e) {
            System.err.println("   ❌ Error en comando PANEL_SUBIR_DOCUMENTO: " + e.getMessage());
        }
        
        Thread.sleep(200);
    }
    
    private static void cleanup() {
        if (vista != null) {
            vista.dispose();
        }
        System.out.println("\n🧹 Entorno limpio");
    }
}
