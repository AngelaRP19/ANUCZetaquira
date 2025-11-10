package co.edu.uptc.functional;

import co.edu.uptc.view.*;
import co.edu.uptc.presenter.Presenter;
import co.edu.uptc.database.Conexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.Component;
import java.lang.reflect.Method;

/**
 * Test específico para verificar problemas de calendario y archivo
 */
public class TestCalendarioYArchivo {
    
    private static VistaGestor vista;
    private static Presenter presenter;
    
    public static void main(String[] args) {
        try {
            System.out.println("🧪 TEST ESPECÍFICO: CALENDARIO Y ARCHIVO");
            System.out.println("==========================================");
            
            setup();
            
            testProblemaCalendario();
            testProblemaSubirArchivo();
            
            System.out.println("\n✅ TEST COMPLETADO");
            
        } catch (Exception e) {
            System.err.println("❌ Error en test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }
    
    private static void setup() throws Exception {
        System.out.println("\n🔧 Configurando entorno...");
        
        presenter = new Presenter();
        vista = new VistaGestor(presenter);
        
        vista.setSize(800, 600);
        vista.setVisible(true);
        
        System.out.println("✅ Entorno configurado");
    }
    
    private static void testProblemaCalendario() throws Exception {
        System.out.println("\n📅 TEST 1: PROBLEMA DE CALENDARIO");
        System.out.println("================================");
        
        // 1. Crear proyecto con fechas específicas
        System.out.println("1️⃣ Creando proyecto con fechas controladas...");
        
        Date fechaInicioOriginal = new Date(); // Hoy
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 15);
        Date fechaFinOriginal = cal.getTime(); // Dentro de 15 días
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String inicioStr = sdf.format(fechaInicioOriginal);
        String finStr = sdf.format(fechaFinOriginal);
        
        System.out.println("   📅 Fecha inicio original: " + inicioStr);
        System.out.println("   📅 Fecha fin original: " + finStr);
        
        // Navegar a crear proyecto
        vista.crearProyecto();
        Thread.sleep(200);
        
        // Obtener panel CrearProyecto
        CrearProyecto panelCrear = obtenerPanelDentroDePrincipal(CrearProyecto.class);
        
        // VERIFICAR FECHAS ACTUALES (no hay setters, solo getters)
        try {
            System.out.println("   🔍 Verificando fechas actuales del formulario...");
            
            // Verificar que se establecieron
            Method getFechaInicio = panelCrear.getClass().getMethod("getFechaInicio");
            Method getFechaFin = panelCrear.getClass().getMethod("getFechaFin");
            
            Date inicioLeida = (Date) getFechaInicio.invoke(panelCrear);
            Date finLeida = (Date) getFechaFin.invoke(panelCrear);
            
            System.out.println("   ✅ Fechas leídas de vista:");
            System.out.println("      📅 Inicio actual: " + (inicioLeida != null ? sdf.format(inicioLeida) : "NULL"));
            System.out.println("      📅 Fin actual: " + (finLeida != null ? sdf.format(finLeida) : "NULL"));
            
            // PROBAR ESTABLECER FECHAS DIRECTAMENTE EN LOS COMPONENTES
            System.out.println("   🔍 Intentando establecer fechas en componentes...");
            
            Component[] components = panelCrear.getComponents();
            for (Component comp : components) {
                if (comp instanceof IngresarFecha) {
                    IngresarFecha campoFecha = (IngresarFecha) comp;
                    
                    // Establecer fecha usando Calendar
                    Calendar calInicio = Calendar.getInstance();
                    calInicio.setTime(fechaInicioOriginal);
                    campoFecha.setFecha(calInicio);
                    
                    System.out.println("   ✅ Fecha establecida en componente IngresarFecha");
                    break;
                }
            }
            
        } catch (Exception e) {
            System.err.println("   ❌ Error con fechas: " + e.getMessage());
        }
        
        // 2. Llenar campos directamente en componentes y guardar
        System.out.println("2️⃣ Estableciendo datos en componentes y guardando...");
        
        try {
            String nombreTest = "TEST_CALENDARIO_" + System.currentTimeMillis();
            
            // Buscar y establecer datos en los componentes del formulario
            Component[] components = panelCrear.getComponents();
            for (Component comp : components) {
                if (comp instanceof IngresarCampo) {
                    IngresarCampo campo = (IngresarCampo) comp;
                    // Establecer nombre
                    campo.setText(nombreTest);
                    System.out.println("   ✅ Nombre establecido: " + nombreTest);
                    break;
                }
            }
            
            // Buscar área de texto para descripción
            for (Component comp : components) {
                if (comp.getClass().getSimpleName().contains("Area") || comp instanceof javax.swing.JTextArea) {
                    javax.swing.JTextArea area = (javax.swing.JTextArea) comp;
                    area.setText("Proyecto para testear problema de calendario");
                    System.out.println("   ✅ Descripción establecida");
                    break;
                }
            }
            
            // Simular clic en guardar
            for (Component comp : components) {
                if (comp instanceof Boton) {
                    Boton boton = (Boton) comp;
                    if ("GUARDAR_PROYECTO".equals(boton.getActionCommand())) {
                        System.out.println("   🎮 Ejecutando comando GUARDAR_PROYECTO");
                        boton.doClick();
                        Thread.sleep(500);
                        System.out.println("   ✅ Click en guardar ejecutado");
                        break;
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("   ❌ Error guardando proyecto: " + e.getMessage());
        }
        
        // 3. Verificar fechas en base de datos
        System.out.println("3️⃣ Verificando fechas en base de datos...");
        
        try {
            Connection conn = Conexion.getConnection();
            Statement stmt = conn.createStatement();
            
            ResultSet rs = stmt.executeQuery(
                "SELECT nombre, fecha_inicio, fecha_fin FROM proyectos WHERE nombre LIKE 'TEST_CALENDARIO_%' ORDER BY proyecto_id DESC LIMIT 1"
            );
            
            if (rs.next()) {
                String nombreBD = rs.getString("nombre");
                Date inicioBD = rs.getDate("fecha_inicio");
                Date finBD = rs.getDate("fecha_fin");
                
                System.out.println("   ✅ Proyecto encontrado en BD:");
                System.out.println("      📝 Nombre: " + nombreBD);
                System.out.println("      📅 Inicio BD: " + (inicioBD != null ? sdf.format(inicioBD) : "NULL"));
                System.out.println("      📅 Fin BD: " + (finBD != null ? sdf.format(finBD) : "NULL"));
                
                // 4. Comparar fechas originales vs BD
                System.out.println("4️⃣ Comparando fechas originales vs almacenadas...");
                
                if (inicioBD != null && finBD != null) {
                    String inicioBDStr = sdf.format(inicioBD);
                    String finBDStr = sdf.format(finBD);
                    
                    boolean inicioCorrecto = inicioStr.equals(inicioBDStr);
                    boolean finCorrecto = finStr.equals(finBDStr);
                    
                    System.out.println("      📅 Inicio original: " + inicioStr);
                    System.out.println("      📅 Inicio BD: " + inicioBDStr);
                    System.out.println("      ✅ Inicio correcto: " + (inicioCorrecto ? "SÍ" : "NO"));
                    
                    System.out.println("      📅 Fin original: " + finStr);
                    System.out.println("      📅 Fin BD: " + finBDStr);
                    System.out.println("      ✅ Fin correcto: " + (finCorrecto ? "SÍ" : "NO"));
                    
                    if (!inicioCorrecto || !finCorrecto) {
                        System.err.println("   ❌ PROBLEMA DETECTADO: Las fechas no coinciden entre vista y BD");
                    } else {
                        System.out.println("   ✅ Fechas coinciden correctamente");
                    }
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
        
        // 5. Test de edición - Ver si las fechas se cargan correctamente
        System.out.println("5️⃣ Test de edición - Verificar carga de fechas...");
        
        // Obtener el nombre del proyecto creado
        String nombreProyectoCreado = "";
        try {
            Connection conn = Conexion.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT nombre FROM proyectos WHERE nombre LIKE 'TEST_CALENDARIO_%' ORDER BY proyecto_id DESC LIMIT 1"
            );
            if (rs.next()) {
                nombreProyectoCreado = rs.getString("nombre");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println("   ❌ Error obteniendo nombre: " + e.getMessage());
        }
        
        if (!nombreProyectoCreado.isEmpty()) {
            // Navegar a editar proyecto
            List<String> datosProyecto = new ArrayList<>();
            datosProyecto.add(nombreProyectoCreado);
            datosProyecto.add(inicioStr);
            datosProyecto.add(finStr);
            datosProyecto.add("Proyecto para testear problema de calendario");
            datosProyecto.add("ACTIVO");
            
            vista.editarProyecto(datosProyecto);
            Thread.sleep(200);
            
            // Obtener panel EditarProyecto
            EditarProyecto panelEditar = obtenerPanelDentroDePrincipal(EditarProyecto.class);
            
            // Verificar las fechas cargadas
            try {
                Method getFechaInicio = panelEditar.getClass().getMethod("getFechaInicio");
                Method getFechaFin = panelEditar.getClass().getMethod("getFechaFin");
                
                Date inicioEditada = (Date) getFechaInicio.invoke(panelEditar);
                Date finEditada = (Date) getFechaFin.invoke(panelEditar);
                
                System.out.println("   ✅ Fechas cargadas en edición:");
                System.out.println("      📅 Inicio edición: " + (inicioEditada != null ? sdf.format(inicioEditada) : "NULL"));
                System.out.println("      📅 Fin edición: " + (finEditada != null ? sdf.format(finEditada) : "NULL"));
                
                // Comparar con las originales
                if (inicioEditada != null && finEditada != null) {
                    String inicioEditStr = sdf.format(inicioEditada);
                    String finEditStr = sdf.format(finEditada);
                    
                    boolean inicioEditCorrecto = inicioStr.equals(inicioEditStr);
                    boolean finEditCorrecto = finStr.equals(finEditStr);
                    
                    if (!inicioEditCorrecto || !finEditCorrecto) {
                        System.err.println("   ❌ PROBLEMA DETECTADO: Las fechas no cargan correctamente en edición");
                    } else {
                        System.out.println("   ✅ Fechas cargan correctamente en edición");
                    }
                }
                
            } catch (Exception e) {
                System.err.println("   ❌ Error leyendo fechas en edición: " + e.getMessage());
            }
        }
    }
    
    private static void testProblemaSubirArchivo() throws Exception {
        System.out.println("\n📁 TEST 2: PROBLEMA DE SUBIR ARCHIVO");
        System.out.println("===================================");
        
        // 1. Navegar a subir documento
        System.out.println("1️⃣ Navegando a subir documento...");
        
        String[] tiposDocumento = {"ACTA", "PROPUESTA", "INFORMES"};
        String nombreProyecto = "TEST_ARCHIVO_" + System.currentTimeMillis();
        
        vista.subirDocumento(tiposDocumento, nombreProyecto);
        Thread.sleep(200);
        
        // Obtener panel SubirDocumento
        SubirDocumento panelSubir = obtenerPanelDentroDePrincipal(SubirDocumento.class);
        System.out.println("   ✅ Panel SubirDocumento cargado");
        
        // 2. Analizar componentes del panel
        System.out.println("2️⃣ Analizando componentes del panel...");
        
        Component[] components = panelSubir.getComponents();
        int totalComponentes = 0;
        int botonesEncontrados = 0;
        int camposArchivoEncontrados = 0;
        
        for (Component comp : components) {
            totalComponentes++;
            
            if (comp instanceof Boton) {
                Boton boton = (Boton) comp;
                String comando = boton.getActionCommand();
                System.out.println("      🔘 Botón encontrado: " + comando);
                botonesEncontrados++;
                
                // Verificar si hay botón de subir archivo
                if (comando != null && comando.contains("ARCHIVO") || comando.contains("SUBIR") || comando.contains("EXPLORAR")) {
                    System.out.println("         ✅ Botón de archivo encontrado");
                    camposArchivoEncontrados++;
                }
            }
            
            // Buscar componentes de archivo
            if (comp instanceof JTextField || comp.getClass().getSimpleName().contains("File")) {
                System.out.println("      📁 Campo de archivo encontrado: " + comp.getClass().getSimpleName());
                camposArchivoEncontrados++;
            }
        }
        
        System.out.println("   📊 Estadísticas del panel:");
        System.out.println("      📦 Total componentes: " + totalComponentes);
        System.out.println("      🔘 Botones encontrados: " + botonesEncontrados);
        System.out.println("      📁 Campos de archivo: " + camposArchivoEncontrados);
        
        // 3. Testear funcionalidad de archivo
        System.out.println("3️⃣ Testeando funcionalidad de archivo...");
        
        if (camposArchivoEncontrados == 0) {
            System.err.println("   ❌ PROBLEMA: No se encontraron componentes para subir archivo");
        } else {
            System.out.println("   ✅ Componentes de archivo encontrados");
            
            // Intentar simular la selección de archivo
            try {
                // Buscar métodos relacionados con archivo
                Method[] methods = panelSubir.getClass().getMethods();
                boolean tieneMetodoArchivo = false;
                
                for (Method method : methods) {
                    String methodName = method.getName().toLowerCase();
                    if (methodName.contains("archivo") || methodName.contains("file") || 
                        methodName.contains("subir") || methodName.contains("upload")) {
                        System.out.println("      🔍 Método de archivo encontrado: " + method.getName());
                        tieneMetodoArchivo = true;
                    }
                }
                
                if (!tieneMetodoArchivo) {
                    System.err.println("   ❌ PROBLEMA: No se encontraron métodos para manejar archivos");
                } else {
                    System.out.println("   ✅ Métodos de archivo encontrados");
                }
                
            } catch (Exception e) {
                System.err.println("   ❌ Error analizando métodos: " + e.getMessage());
            }
        }
        
        // 4. Verificar integración con backend
        System.out.println("4️⃣ Verificando integración con backend...");
        
        // Verificar métodos en VistaGestor para archivos
        try {
            Method[] metodosVista = vista.getClass().getMethods();
            boolean tieneMetodoArchivo = false;
            
            for (Method method : metodosVista) {
                String methodName = method.getName().toLowerCase();
                if (methodName.contains("archivo") || methodName.contains("documento") || 
                    methodName.contains("file")) {
                    System.out.println("      🔍 Método en VistaGestor: " + method.getName());
                    tieneMetodoArchivo = true;
                }
            }
            
            if (!tieneMetodoArchivo) {
                System.err.println("   ❌ PROBLEMA: VistaGestor no tiene métodos para archivos");
            } else {
                System.out.println("   ✅ VistaGestor tiene métodos para archivos");
            }
            
        } catch (Exception e) {
            System.err.println("   ❌ Error verificando VistaGestor: " + e.getMessage());
        }
        
        // 5. Testear botones del panel
        System.out.println("5️⃣ Testeando botones del panel...");
        
        for (Component comp : components) {
            if (comp instanceof Boton) {
                Boton boton = (Boton) comp;
                String comando = boton.getActionCommand();
                
                if (comando != null && !comando.isEmpty()) {
                    System.out.println("      🎮 Ejecutando comando: " + comando);
                    
                    try {
                        boton.doClick();
                        Thread.sleep(100);
                        System.out.println("         ✅ Comando ejecutado");
                    } catch (Exception e) {
                        System.err.println("         ❌ Error ejecutando comando: " + e.getMessage());
                    }
                }
            }
        }
    }
    
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
        System.out.println("\n🧹 Limpiando entorno...");
        if (vista != null) {
            vista.dispose();
        }
        System.out.println("✅ Entorno limpio");
    }
}
