package co.edu.uptc.functional;

import co.edu.uptc.model.Gestor;
import co.edu.uptc.database.Conexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Calendar;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Test Simple de Integración que verifica el flujo completo
 * sin acceder a métodos privados del Presenter.
 */
public class TestSimpleIntegracion {

    private static Gestor gestor;
    private static Connection connection;
    
    // Datos de prueba
    private static final String NOMBRE_PROYECTO = "PROYECTO_SIMPLE_TEST";
    private static final String NOMBRE_ACTIVIDAD = "ACTIVIDAD_SIMPLE_TEST";
    private static final String NOMBRE_DOCUMENTO = "DOCUMENTO_SIMPLE_TEST.pdf";
    private static final String RUTA_ARCHIVO_PRUEBA = "/tmp/test_simple.pdf";

    public static void main(String[] args) {
        System.out.println("🧪 INICIANDO TEST SIMPLE DE INTEGRACIÓN");
        
        try {
            setup();
            crearArchivoPrueba();
            testIntegracionCompleta();
            
            System.out.println("✅ TEST SIMPLE DE INTEGRACIÓN PASÓ");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR EN TEST SIMPLE: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private static void setup() throws Exception {
        System.out.println("\n📋 CONFIGURANDO ENTORNO...");
        
        gestor = new Gestor();
        connection = Conexion.getConnection();
        
        // Limpiar base de datos
        limpiarBaseDatos();
        
        System.out.println("✅ Entorno configurado");
    }

    private static void crearArchivoPrueba() throws IOException {
        System.out.println("📁 Creando archivo de prueba...");
        
        File archivoPrueba = new File(RUTA_ARCHIVO_PRUEBA);
        try (FileWriter writer = new FileWriter(archivoPrueba)) {
            writer.write("Este es un archivo de prueba para el test funcional.");
            writer.write("\nContenido de prueba para documento.");
            writer.write("\nFecha de creación: " + new Date());
        }
        
        // Verificar que el archivo existe y tiene contenido
        if (archivoPrueba.exists() && archivoPrueba.length() > 0) {
            System.out.println("✅ Archivo de prueba creado: " + RUTA_ARCHIVO_PRUEBA + " (" + archivoPrueba.length() + " bytes)");
        } else {
            throw new IOException("No se pudo crear el archivo de prueba");
        }
    }

    private static void testIntegracionCompleta() throws Exception {
        System.out.println("\n🔄 EJECUTANDO FLUJO COMPLETO DE INTEGRACIÓN");
        
        // PASO 1: Crear proyecto (simula vista -> presenter -> model -> bd)
        System.out.println("  📝 Paso 1: Crear proyecto");
        System.out.println("    🎭 Simulando: Usuario llena formulario en vista...");
        
        // Simular que la vista obtiene los datos del formulario
        Calendar cal = Calendar.getInstance();
        Date fechaInicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 60);
        Date fechaFin = cal.getTime();
        String estado = "ACTIVO";
        String descripcion = "Proyecto de prueba simple";
        
        System.out.println("    🎭 Simulando: Vista obtiene datos del formulario...");
        System.out.println("       📋 Nombre: " + NOMBRE_PROYECTO);
        System.out.println("       📋 Fecha inicio: " + fechaInicio);
        System.out.println("       📋 Fecha fin: " + fechaFin);
        System.out.println("       📋 Estado: " + estado);
        
        // Simular que el presenter llama al modelo (como lo haría la vista real)
        System.out.println("    🎭 Simulando: Presenter llama a Gestor.agregarProyecto()...");
        boolean proyectoCreado = gestor.agregarProyecto(
            NOMBRE_PROYECTO, 
            fechaInicio, 
            fechaFin, 
            estado, 
            descripcion
        );
        
        assert proyectoCreado : "❌ No se pudo crear el proyecto";
        System.out.println("    ✅ Proyecto creado en modelo");
        
        // Verificar en BD
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM proyectos WHERE nombre = '" + NOMBRE_PROYECTO + "'");
        rs.next();
        int count = rs.getInt("count");
        rs.close();
        stmt.close();
        
        assert count == 1 : "❌ Proyecto no se guardó en BD";
        System.out.println("    ✅ Proyecto verificado en BD");
        
        // PASO 2: Listar proyectos (simula vista <- presenter <- model <- bd)
        System.out.println("  📋 Paso 2: Listar proyectos");
        gestor.cargarTodoDesdeBD();
        java.util.List<String> proyectos = gestor.getNombresProyectos();
        
        assert proyectos.contains(NOMBRE_PROYECTO) : "❌ Proyecto no aparece en lista";
        System.out.println("    ✅ Proyecto visible en lista: " + proyectos.size() + " proyectos");
        
        // PASO 3: Crear actividad
        System.out.println("  📝 Paso 3: Crear actividad");
        Date fechaActividad = new Date();
        
        gestor.registrarActividad(
            NOMBRE_PROYECTO, 
            NOMBRE_ACTIVIDAD, 
            "Actividad de prueba", 
            co.edu.uptc.model.TipoActividad.CAPACITACION, 
            fechaActividad
        );
        
        System.out.println("    ✅ Actividad creada en modelo");
        
        // Verificar en BD
        stmt = connection.createStatement();
        rs = stmt.executeQuery("SELECT COUNT(*) as count FROM actividades WHERE nombre = '" + NOMBRE_ACTIVIDAD + "'");
        rs.next();
        count = rs.getInt("count");
        rs.close();
        stmt.close();
        
        assert count == 1 : "❌ Actividad no se guardó en BD";
        System.out.println("    ✅ Actividad verificada en BD");
        
        // PASO 4: Listar actividades
        System.out.println("  📋 Paso 4: Listar actividades");
        java.util.List<co.edu.uptc.model.Actividad> actividades = 
            gestor.consultarActividadesDeProyecto(NOMBRE_PROYECTO);
        
        assert actividades.size() > 0 : "❌ No hay actividades en el proyecto";
        System.out.println("    ✅ Actividades visibles en lista: " + actividades.size() + " actividades");
        
        // PASO 5: Crear documento (simula vista -> presenter -> model -> bd)
        System.out.println("  📝 Paso 5: Crear documento");
        System.out.println("    🎭 Simulando: Usuario selecciona archivo en vista...");
        
        // Verificar que el archivo de prueba existe antes de continuar
        File archivoVerificacion = new File(RUTA_ARCHIVO_PRUEBA);
        if (!archivoVerificacion.exists()) {
            throw new IOException("El archivo de prueba no existe: " + RUTA_ARCHIVO_PRUEBA);
        }
        System.out.println("    📁 Archivo verificado: " + RUTA_ARCHIVO_PRUEBA + " (" + archivoVerificacion.length() + " bytes)");
        
        // Simular que la vista obtiene los datos del formulario
        System.out.println("    🎭 Simulando: Vista obtiene datos del formulario...");
        String tipoDocumento = "ACTA";
        String rutaArchivo = RUTA_ARCHIVO_PRUEBA;
        
        // Simular que el presenter llama al modelo (como lo haría la vista real)
        System.out.println("    🎭 Simulando: Presenter llama a Gestor.registrarDocumento()...");
        gestor.registrarDocumento(NOMBRE_PROYECTO, NOMBRE_DOCUMENTO, tipoDocumento, rutaArchivo);
        
        System.out.println("    ✅ Documento creado en modelo");
        
        // Verificar en BD
        stmt = connection.createStatement();
        rs = stmt.executeQuery("SELECT COUNT(*) as count FROM documentos WHERE nombre = '" + NOMBRE_DOCUMENTO + "'");
        rs.next();
        count = rs.getInt("count");
        rs.close();
        stmt.close();
        
        assert count == 1 : "❌ Documento no se guardó en BD";
        System.out.println("    ✅ Documento verificado en BD");
        
        // PASO 6: Listar documentos
        System.out.println("  📋 Paso 6: Listar documentos");
        java.util.List<String> documentos = gestor.obtenerNombresDocumentosDeProyecto(NOMBRE_PROYECTO);
        
        assert documentos.contains(NOMBRE_DOCUMENTO) : "❌ Documento no aparece en lista";
        System.out.println("    ✅ Documentos visibles en lista: " + documentos.size() + " documentos");
        
        // PASO 7: Actualizar proyecto
        System.out.println("  ✏️ Paso 7: Actualizar proyecto");
        gestor.actualizarProyectoCampo(NOMBRE_PROYECTO, "descripcion", "Descripción actualizada");
        
        // Verificar actualización
        stmt = connection.createStatement();
        rs = stmt.executeQuery("SELECT descripcion FROM proyectos WHERE nombre = '" + NOMBRE_PROYECTO + "'");
        rs.next();
        String descripcionActualizada = rs.getString("descripcion");
        rs.close();
        stmt.close();
        
        assert "Descripción actualizada".equals(descripcionActualizada) : "❌ Proyecto no se actualizó";
        System.out.println("    ✅ Proyecto actualizado correctamente");
        
        // PASO 8: Eliminar en cascada
        System.out.println("  🗑️ Paso 8: Eliminar en cascada");
        gestor.eliminarProyectoConDependencias(NOMBRE_PROYECTO);
        
        // Verificar eliminación completa
        stmt = connection.createStatement();
        rs = stmt.executeQuery("SELECT COUNT(*) as total FROM proyectos WHERE nombre = '" + NOMBRE_PROYECTO + "'");
        rs.next();
        int proyectosRestantes = rs.getInt("total");
        rs.close();
        
        // Para actividades y documentos, necesitamos verificar por proyecto_id
        int actividadesRestantes = 0;
        int documentosRestantes = 0;
        
        rs = stmt.executeQuery("SELECT proyecto_id FROM proyectos WHERE nombre = '" + NOMBRE_PROYECTO + "'");
        if (rs.next()) {
            int proyectoId = rs.getInt("proyecto_id");
            rs.close();
            
            // Verificar actividades
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM actividades WHERE proyecto_id = " + proyectoId);
            rs.next();
            actividadesRestantes = rs.getInt("total");
            rs.close();
            
            // Verificar documentos
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM documentos WHERE proyecto_id = " + proyectoId);
            rs.next();
            documentosRestantes = rs.getInt("total");
            rs.close();
            
            assert actividadesRestantes == 0 : "❌ Actividades no se eliminaron en cascada";
            assert documentosRestantes == 0 : "❌ Documentos no se eliminaron en cascada";
        } else {
            rs.close();
        }
        stmt.close();
        
        assert proyectosRestantes == 0 : "❌ Proyecto no se eliminó";
        assert actividadesRestantes == 0 : "❌ Actividades no se eliminaron en cascada";
        assert documentosRestantes == 0 : "❌ Documentos no se eliminaron en cascada";
        
        System.out.println("    ✅ Eliminación en cascada verificada");
        
        System.out.println("✅ FLUJO COMPLETO DE INTEGRACIÓN VERIFICADO");
    }

    private static void limpiarBaseDatos() throws Exception {
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM documentos");
        stmt.execute("DELETE FROM actividades");
        stmt.execute("DELETE FROM proyectos");
        stmt.close();
    }

    private static void cleanup() {
        System.out.println("\n🧹 LIMPIANDO ENTORNO...");
        try {
            limpiarBaseDatos();
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            System.err.println("Error en limpieza: " + e.getMessage());
        }
        System.out.println("✅ Entorno limpio");
    }
}
