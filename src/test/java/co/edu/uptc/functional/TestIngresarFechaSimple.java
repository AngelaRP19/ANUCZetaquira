package co.edu.uptc.functional;

import co.edu.uptc.view.IngresarFecha;
import co.edu.uptc.view.VistaGestor;
import co.edu.uptc.presenter.Presenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Test simple para aislar el problema de IngresarFecha
 */
public class TestIngresarFechaSimple {
    
    public static void main(String[] args) {
        try {
            System.out.println("🔍 TEST SIMPLE: IngresarFecha");
            System.out.println("===============================");
            
            // Crear un componente IngresarFecha directamente
            IngresarFecha campoFecha = new IngresarFecha(300);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            // 1. Verificar estado inicial
            System.out.println("1️⃣ Estado inicial:");
            Date fechaInicial = campoFecha.getFechaCalendar();
            System.out.println("   📅 Fecha inicial: " + (fechaInicial != null ? sdf.format(fechaInicial) : "NULL"));
            
            // 2. Establecer una fecha
            System.out.println("\n2️⃣ Estableciendo fecha...");
            Calendar cal = Calendar.getInstance();
            cal.set(2024, Calendar.JANUARY, 15); // 15 de enero de 2024
            Date fechaAEstablecer = cal.getTime();
            
            System.out.println("   📅 Fecha a establecer: " + sdf.format(fechaAEstablecer));
            
            campoFecha.setFecha(cal);
            System.out.println("   ✅ Fecha establecida con setFecha()");
            
            // 3. Verificar después de establecer
            System.out.println("\n3️⃣ Verificación después de establecer:");
            Date fechaDespues = campoFecha.getFechaCalendar();
            System.out.println("   📅 Fecha después: " + (fechaDespues != null ? sdf.format(fechaDespues) : "NULL"));
            
            // 4. Comparar
            if (fechaDespues != null) {
                String fechaEstablecidaStr = sdf.format(fechaAEstablecer);
                String fechaObtenidaStr = sdf.format(fechaDespues);
                
                if (fechaEstablecidaStr.equals(fechaObtenidaStr)) {
                    System.out.println("   ✅ ÉXITO: La fecha se estableció correctamente");
                } else {
                    System.err.println("   ❌ ERROR: Fechas no coinciden");
                    System.err.println("      Esperada: " + fechaEstablecidaStr);
                    System.err.println("      Obtenida: " + fechaObtenidaStr);
                }
            } else {
                System.err.println("   ❌ ERROR: La fecha sigue siendo NULL");
            }
            
            // 5. Test con el componente en un frame real
            System.out.println("\n4️⃣ Test en entorno real:");
            testEnEntornoReal();
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testEnEntornoReal() throws Exception {
        // Crear vista real
        Presenter presenter = new Presenter();
        VistaGestor vista = new VistaGestor(presenter);
        vista.setSize(800, 600);
        vista.setVisible(true);
        
        // Navegar a crear proyecto
        vista.crearProyecto();
        Thread.sleep(200);
        
        // Obtener panel CrearProyecto
        co.edu.uptc.view.CrearProyecto panel = obtenerPanelDentroDePrincipal(vista, co.edu.uptc.view.CrearProyecto.class);
        
        // Obtener fechas del panel
        Date fechaInicio = panel.getFechaInicio();
        Date fechaFin = panel.getFechaFin();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("   📅 Fecha inicio del panel: " + (fechaInicio != null ? sdf.format(fechaInicio) : "NULL"));
        System.out.println("   📅 Fecha fin del panel: " + (fechaFin != null ? sdf.format(fechaFin) : "NULL"));
        
        // Intentar establecer fechas directamente en el panel
        System.out.println("   🔍 Estableciendo fechas en panel...");
        
        Calendar calInicio = Calendar.getInstance();
        calInicio.set(2024, Calendar.MARCH, 1);
        Calendar calFin = Calendar.getInstance();
        calFin.set(2024, Calendar.DECEMBER, 31);
        
        // Buscar componentes IngresarFecha dentro del panel
        java.util.List<java.awt.Component> componentes = findAllComponents(panel);
        int camposFecha = 0;
        
        for (java.awt.Component comp : componentes) {
            if (comp instanceof IngresarFecha) {
                camposFecha++;
                IngresarFecha campo = (IngresarFecha) comp;
                
                if (camposFecha == 1) {
                    campo.setFecha(calInicio);
                    System.out.println("   ✅ Fecha inicio establecida en componente");
                } else if (camposFecha == 2) {
                    campo.setFecha(calFin);
                    System.out.println("   ✅ Fecha fin establecida en componente");
                }
            }
        }
        
        // Verificar de nuevo
        Thread.sleep(100);
        Date nuevoInicio = panel.getFechaInicio();
        Date nuevoFin = panel.getFechaFin();
        
        System.out.println("   📅 Nueva fecha inicio: " + (nuevoInicio != null ? sdf.format(nuevoInicio) : "NULL"));
        System.out.println("   📅 Nueva fecha fin: " + (nuevoFin != null ? sdf.format(nuevoFin) : "NULL"));
        
        if (nuevoInicio != null && nuevoFin != null) {
            System.out.println("   ✅ ÉXITO: Fechas establecidas en panel real");
        } else {
            System.err.println("   ❌ ERROR: No se pudieron establecer fechas en panel real");
        }
        
        vista.dispose();
    }
    
    private static java.util.List<java.awt.Component> findAllComponents(java.awt.Component container) {
        java.util.List<java.awt.Component> components = new java.util.ArrayList<>();
        addComponents(container, components);
        return components;
    }
    
    private static void addComponents(java.awt.Component component, java.util.List<java.awt.Component> components) {
        if (component == null) return;
        
        components.add(component);
        
        if (component instanceof java.awt.Container) {
            java.awt.Container container = (java.awt.Container) component;
            for (java.awt.Component child : container.getComponents()) {
                addComponents(child, components);
            }
        }
    }
    
    private static <T> T obtenerPanelDentroDePrincipal(VistaGestor vista, Class<T> panelClass) throws Exception {
        java.awt.Component currentComponent = vista.getContentPane().getComponent(0);
        if (!(currentComponent instanceof co.edu.uptc.view.Principal)) {
            throw new Exception("Panel actual no es Principal");
        }
        
        co.edu.uptc.view.Principal panelPrincipal = (co.edu.uptc.view.Principal) currentComponent;
        T panel = null;
        for (java.awt.Component comp : panelPrincipal.getComponents()) {
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
}
