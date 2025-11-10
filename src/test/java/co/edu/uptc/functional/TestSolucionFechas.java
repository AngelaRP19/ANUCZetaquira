package co.edu.uptc.functional;

import co.edu.uptc.view.*;
import co.edu.uptc.presenter.Presenter;

import javax.swing.*;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Test para solucionar el problema de fechas
 */
public class TestSolucionFechas {
    
    private static VistaGestor vista;
    private static Presenter presenter;
    
    public static void main(String[] args) {
        try {
            System.out.println("🔧 TEST: SOLUCIÓN PROBLEMA DE FECHAS");
            System.out.println("====================================");
            
            setup();
            
            testEstablecerFechasCorrectamente();
            testGuardarProyectoConFechas();
            testVerificarFechasEnBD();
            
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
    
    private static void testEstablecerFechasCorrectamente() throws Exception {
        System.out.println("\n📅 TEST 1: Establecer fechas correctamente");
        System.out.println("==========================================");
        
        // Navegar a crear proyecto
        vista.crearProyecto();
        Thread.sleep(200);
        
        // Obtener panel
        CrearProyecto panel = obtenerPanelDentroDePrincipal(CrearProyecto.class);
        
        // Encontrar todos los componentes
        List<Component> todosComponentes = findAllComponents(panel);
        
        // Fechas de prueba
        Date fechaInicio = new Date(); // Hoy
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date fechaFin = cal.getTime(); // Dentro de 30 días
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("   📅 Fecha inicio a establecer: " + sdf.format(fechaInicio));
        System.out.println("   📅 Fecha fin a establecer: " + sdf.format(fechaFin));
        
        int camposFechaEncontrados = 0;
        
        // Buscar y establecer fechas en los campos IngresarFecha
        for (Component comp : todosComponentes) {
            if (comp instanceof IngresarFecha) {
                camposFechaEncontrados++;
                IngresarFecha campoFecha = (IngresarFecha) comp;
                
                System.out.println("   🔍 Encontrado campo de fecha #" + camposFechaEncontrados);
                
                // Verificar fecha actual
                Date fechaActual = campoFecha.getFechaCalendar();
                System.out.println("      📅 Fecha actual: " + (fechaActual != null ? sdf.format(fechaActual) : "NULL"));
                
                // Establecer fecha usando Calendar
                Calendar calEstablecer = Calendar.getInstance();
                
                if (camposFechaEncontrados == 1) {
                    // Primera fecha: fecha de inicio
                    calEstablecer.setTime(fechaInicio);
                    campoFecha.setFecha(calEstablecer);
                    System.out.println("      ✅ Estableciendo fecha de inicio");
                } else if (camposFechaEncontrados == 2) {
                    // Segunda fecha: fecha de fin
                    calEstablecer.setTime(fechaFin);
                    campoFecha.setFecha(calEstablecer);
                    System.out.println("      ✅ Estableciendo fecha de fin");
                }
                
                // Verificar que se estableció
                Date fechaVerificada = campoFecha.getFechaCalendar();
                System.out.println("      📅 Fecha después de establecer: " + (fechaVerificada != null ? sdf.format(fechaVerificada) : "NULL"));
                
                if (fechaVerificada != null) {
                    System.out.println("      ✅ Fecha establecida correctamente");
                } else {
                    System.err.println("      ❌ Error: Fecha sigue siendo NULL");
                }
            }
        }
        
        System.out.println("   📊 Total campos de fecha encontrados: " + camposFechaEncontrados);
        
        // Verificar con los métodos del panel
        try {
            java.lang.reflect.Method getFechaInicio = panel.getClass().getMethod("getFechaInicio");
            java.lang.reflect.Method getFechaFin = panel.getClass().getMethod("getFechaFin");
            
            Date inicioPanel = (Date) getFechaInicio.invoke(panel);
            Date finPanel = (Date) getFechaFin.invoke(panel);
            
            System.out.println("   📅 Fechas según panel:");
            System.out.println("      📅 Inicio: " + (inicioPanel != null ? sdf.format(inicioPanel) : "NULL"));
            System.out.println("      📅 Fin: " + (finPanel != null ? sdf.format(finPanel) : "NULL"));
            
        } catch (Exception e) {
            System.err.println("   ❌ Error obteniendo fechas del panel: " + e.getMessage());
        }
    }
    
    private static void testGuardarProyectoConFechas() throws Exception {
        System.out.println("\n💾 TEST 2: Guardar proyecto con fechas");
        System.out.println("=====================================");
        
        // Obtener panel actual
        CrearProyecto panel = obtenerPanelDentroDePrincipal(CrearProyecto.class);
        
        // Establecer nombre del proyecto
        String nombreProyecto = "PROYECTO_FECHAS_" + System.currentTimeMillis();
        
        // Buscar campo de nombre y establecerlo
        List<Component> componentes = findAllComponents(panel);
        for (Component comp : componentes) {
            if (comp instanceof IngresarCampo) {
                IngresarCampo campo = (IngresarCampo) comp;
                campo.setText(nombreProyecto);
                System.out.println("   ✅ Nombre establecido: " + nombreProyecto);
                break;
            }
        }
        
        // Buscar botón guardar y hacer clic
        for (Component comp : componentes) {
            if (comp instanceof Boton) {
                Boton boton = (Boton) comp;
                if ("GUARDAR_PROYECTO".equals(boton.getActionCommand())) {
                    System.out.println("   🎮 Ejecutando comando GUARDAR_PROYECTO");
                    boton.doClick();
                    Thread.sleep(1000); // Esperar más tiempo para guardar
                    System.out.println("   ✅ Click en guardar ejecutado");
                    break;
                }
            }
        }
        
        // Verificar si hay mensajes de éxito
        System.out.println("   📝 Proyecto guardado, verificando en BD...");
    }
    
    private static void testVerificarFechasEnBD() throws Exception {
        System.out.println("\n🗄️ TEST 3: Verificar fechas en base de datos");
        System.out.println("==========================================");
        
        try {
            java.sql.Connection conn = co.edu.uptc.database.Conexion.getConnection();
            java.sql.Statement stmt = conn.createStatement();
            
            // Buscar el proyecto recién guardado
            java.sql.ResultSet rs = stmt.executeQuery(
                "SELECT nombre, fecha_inicio, fecha_fin FROM proyectos WHERE nombre LIKE 'PROYECTO_FECHAS_%' ORDER BY proyecto_id DESC LIMIT 1"
            );
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                java.sql.Date inicioBD = rs.getDate("fecha_inicio");
                java.sql.Date finBD = rs.getDate("fecha_fin");
                
                System.out.println("   ✅ Proyecto encontrado en BD:");
                System.out.println("      📝 Nombre: " + nombre);
                System.out.println("      📅 Fecha inicio BD: " + (inicioBD != null ? sdf.format(inicioBD) : "NULL"));
                System.out.println("      📅 Fecha fin BD: " + (finBD != null ? sdf.format(finBD) : "NULL"));
                
                if (inicioBD != null && finBD != null) {
                    System.out.println("   ✅ FECHAS GUARDADAS CORRECTAMENTE EN BD");
                    
                    // Test de edición para ver si las fechas se cargan bien
                    testEdicionConFechas(nombre);
                    
                } else {
                    System.err.println("   ❌ ERROR: Fechas nulas en BD");
                }
                
            } else {
                System.err.println("   ❌ ERROR: Proyecto no encontrado en BD");
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            System.err.println("   ❌ Error verificando BD: " + e.getMessage());
        }
    }
    
    private static void testEdicionConFechas(String nombreProyecto) throws Exception {
        System.out.println("\n✏️ TEST 4: Edición con fechas");
        System.out.println("============================");
        
        // Crear datos para edición
        List<String> datos = new ArrayList<>();
        datos.add(nombreProyecto);
        datos.add("2024-01-01"); // fecha inicio
        datos.add("2024-12-31"); // fecha fin
        datos.add("Proyecto de prueba con fechas");
        datos.add("ACTIVO");
        
        // Navegar a edición
        vista.editarProyecto(datos);
        Thread.sleep(300);
        
        // Obtener panel de edición
        EditarProyecto panelEdit = obtenerPanelDentroDePrincipal(EditarProyecto.class);
        
        // Verificar fechas cargadas
        try {
            java.lang.reflect.Method getFechaInicio = panelEdit.getClass().getMethod("getFechaInicio");
            java.lang.reflect.Method getFechaFin = panelEdit.getClass().getMethod("getFechaFin");
            
            Date inicioEdit = (Date) getFechaInicio.invoke(panelEdit);
            Date finEdit = (Date) getFechaFin.invoke(panelEdit);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            System.out.println("   📅 Fechas cargadas en edición:");
            System.out.println("      📅 Inicio: " + (inicioEdit != null ? sdf.format(inicioEdit) : "NULL"));
            System.out.println("      📅 Fin: " + (finEdit != null ? sdf.format(finEdit) : "NULL"));
            
            // Comparar con las esperadas
            if (inicioEdit != null && finEdit != null) {
                String inicioStr = sdf.format(inicioEdit);
                String finStr = sdf.format(finEdit);
                
                boolean inicioCorrecto = inicioStr.equals("2024-01-01");
                boolean finCorrecto = finStr.equals("2024-12-31");
                
                if (inicioCorrecto && finCorrecto) {
                    System.out.println("   ✅ FECHAS CARGADAS CORRECTAMENTE EN EDICIÓN");
                } else {
                    System.err.println("   ❌ ERROR: Fechas no coinciden en edición");
                    System.err.println("      Esperado inicio: 2024-01-01, obtenido: " + inicioStr);
                    System.err.println("      Esperado fin: 2024-12-31, obtenido: " + finStr);
                }
            } else {
                System.err.println("   ❌ ERROR: Fechas nulas en edición");
            }
            
        } catch (Exception e) {
            System.err.println("   ❌ Error verificando fechas en edición: " + e.getMessage());
        }
    }
    
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
