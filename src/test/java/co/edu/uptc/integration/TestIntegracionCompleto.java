package co.edu.uptc.integration;

import co.edu.uptc.model.Gestor;
import co.edu.uptc.model.Proyecto;
import co.edu.uptc.model.Actividad;
import co.edu.uptc.model.Documento;
import co.edu.uptc.database.Conexion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

/**
 * Test de Integración Completo para verificar que toda la aplicación funciona
 * desde la vista hasta la base de datos.
 */
public class TestIntegracionCompleto {

    private static Gestor gestor;
    private static Connection connection;

    public static void main(String[] args) {
        System.out.println("🧪 INICIANDO TEST DE INTEGRACIÓN COMPLETO");
        
        try {
            setup();
            testProyectos();
            testActividades();
            testDocumentos();
            testBaseDeDatos();
            
            System.out.println("✅ TODOS LOS TESTS PASARON EXITOSAMENTE");
        } catch (Exception e) {
            System.err.println("❌ ERROR EN LOS TESTS: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private static void setup() throws Exception {
        System.out.println("\n📋 CONFIGURANDO ENTORNO DE PRUEBA...");
        
        gestor = new Gestor();
        connection = Conexion.getConnection();
        
        // Limpiar base de datos de prueba
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM documentos");
        stmt.execute("DELETE FROM actividades");
        stmt.execute("DELETE FROM proyectos");
        stmt.close();
        
        System.out.println("✅ Entorno configurado");
    }

    private static void testProyectos() throws Exception {
        System.out.println("\n🏗️ TESTEANDO PROYECTOS...");
        
        // Test 1: Crear proyecto
        System.out.println("  📝 Test 1: Crear proyecto");
        String nombreProyecto = "PROYECTO_TEST_INTEGRACION";
        Date fechaInicio = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date fechaFin = cal.getTime();
        String estado = "ACTIVO";
        String descripcion = "Proyecto de prueba para integración";
        
        boolean creado = gestor.agregarProyecto(nombreProyecto, fechaInicio, fechaFin, estado, descripcion);
        assert creado : "❌ No se pudo crear el proyecto";
        System.out.println("    ✅ Proyecto creado en memoria");
        
        // Test 2: Verificar en base de datos
        System.out.println("  🔍 Test 2: Verificar proyecto en BD");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM proyectos WHERE nombre = '" + nombreProyecto + "'");
        
        assert rs.next() : "❌ Proyecto no encontrado en BD";
        assert nombreProyecto.equals(rs.getString("nombre")) : "❌ Nombre incorrecto en BD";
        assert descripcion.equals(rs.getString("descripcion")) : "❌ Descripción incorrecta en BD";
        rs.close();
        stmt.close();
        System.out.println("    ✅ Proyecto verificado en BD");
        
        // Test 3: Listar proyectos
        System.out.println("  📋 Test 3: Listar proyectos");
        List<String> proyectos = gestor.getNombresProyectos();
        assert proyectos.contains(nombreProyecto) : "❌ Proyecto no aparece en la lista";
        System.out.println("    ✅ Proyecto aparece en lista: " + proyectos.size() + " proyectos");
        
        // Test 4: Buscar proyecto
        System.out.println("  🔎 Test 4: Buscar proyecto");
        Proyecto proyecto = gestor.buscarProyectoPorNombre(nombreProyecto);
        assert proyecto != null : "❌ Proyecto no encontrado";
        assert nombreProyecto.equals(proyecto.getNombre()) : "❌ Proyecto encontrado incorrecto";
        System.out.println("    ✅ Proyecto encontrado correctamente");
        
        // Test 5: Actualizar proyecto
        System.out.println("  ✏️ Test 5: Actualizar proyecto");
        String nuevaDescripcion = "Descripción actualizada";
        gestor.actualizarProyectoCampo(nombreProyecto, "descripcion", nuevaDescripcion);
        
        Proyecto proyectoActualizado = gestor.buscarProyectoPorNombre(nombreProyecto);
        assert nuevaDescripcion.equals(proyectoActualizado.getDescripcion()) : "❌ Proyecto no se actualizó";
        System.out.println("    ✅ Proyecto actualizado correctamente");
        
        System.out.println("✅ TESTS DE PROYECTOS COMPLETADOS");
    }

    private static void testActividades() throws Exception {
        System.out.println("\n📋 TESTEANDO ACTIVIDADES...");
        
        String nombreProyecto = "PROYECTO_TEST_INTEGRACION";
        
        // Test 1: Crear actividad
        System.out.println("  📝 Test 1: Crear actividad");
        String nombreActividad = "ACTIVIDAD_TEST";
        Date fechaActividad = new Date();
        String tipo = "CAPACITACION";
        String descripcionActividad = "Actividad de prueba";
        
        gestor.registrarActividad(nombreProyecto, nombreActividad, descripcionActividad, 
                                 co.edu.uptc.model.TipoActividad.CAPACITACION, fechaActividad);
        System.out.println("    ✅ Actividad creada en memoria");
        
        // Test 2: Verificar en base de datos
        System.out.println("  🔍 Test 2: Verificar actividad en BD");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM actividades WHERE nombre = '" + nombreActividad + "'");
        
        assert rs.next() : "❌ Actividad no encontrada en BD";
        assert nombreActividad.equals(rs.getString("nombre")) : "❌ Nombre incorrecto en BD";
        assert tipo.equals(rs.getString("tipo")) : "❌ Tipo incorrecto en BD";
        rs.close();
        stmt.close();
        System.out.println("    ✅ Actividad verificada en BD");
        
        // Test 3: Listar actividades del proyecto
        System.out.println("  📋 Test 3: Listar actividades del proyecto");
        List<Actividad> actividades = gestor.consultarActividadesDeProyecto(nombreProyecto);
        assert actividades.size() > 0 : "❌ No hay actividades en el proyecto";
        
        boolean encontrada = false;
        for (Actividad act : actividades) {
            if (nombreActividad.equals(act.getNombre())) {
                encontrada = true;
                break;
            }
        }
        assert encontrada : "❌ Actividad no aparece en la lista del proyecto";
        System.out.println("    ✅ Actividad aparece en lista: " + actividades.size() + " actividades");
        
        // Test 4: Consultar actividad específica
        System.out.println("  🔎 Test 4: Consultar actividad específica");
        Actividad actividad = gestor.consultarActividad(nombreProyecto, nombreActividad);
        assert actividad != null : "❌ Actividad no encontrada";
        assert nombreActividad.equals(actividad.getNombre()) : "❌ Actividad encontrada incorrecta";
        System.out.println("    ✅ Actividad encontrada correctamente");
        
        System.out.println("✅ TESTS DE ACTIVIDADES COMPLETADOS");
    }

    private static void testDocumentos() throws Exception {
        System.out.println("\n📁 TESTEANDO DOCUMENTOS...");
        
        String nombreProyecto = "PROYECTO_TEST_INTEGRACION";
        
        // Test 1: Crear documento
        System.out.println("  📝 Test 1: Crear documento");
        String nombreDocumento = "DOCUMENTO_TEST.pdf";
        String tipo = "ACTA";
        String rutaArchivo = "/test/ruta/documento.pdf";
        
        gestor.registrarDocumento(nombreProyecto, nombreDocumento, tipo, rutaArchivo);
        System.out.println("    ✅ Documento creado en memoria");
        
        // Test 2: Verificar en base de datos
        System.out.println("  🔍 Test 2: Verificar documento en BD");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM documentos WHERE nombre = '" + nombreDocumento + "'");
        
        assert rs.next() : "❌ Documento no encontrado en BD";
        assert nombreDocumento.equals(rs.getString("nombre")) : "❌ Nombre incorrecto en BD";
        assert tipo.equals(rs.getString("tipo")) : "❌ Tipo incorrecto en BD";
        rs.close();
        stmt.close();
        System.out.println("    ✅ Documento verificado en BD");
        
        // Test 3: Listar documentos del proyecto
        System.out.println("  📋 Test 3: Listar documentos del proyecto");
        List<String> documentos = gestor.obtenerNombresDocumentosDeProyecto(nombreProyecto);
        assert documentos.size() > 0 : "❌ No hay documentos en el proyecto";
        assert documentos.contains(nombreDocumento) : "❌ Documento no aparece en la lista";
        System.out.println("    ✅ Documento aparece en lista: " + documentos.size() + " documentos");
        
        System.out.println("✅ TESTS DE DOCUMENTOS COMPLETADOS");
    }

    private static void testBaseDeDatos() throws Exception {
        System.out.println("\n🗄️ TESTEANDO INTEGRIDAD DE BASE DE DATOS...");
        
        // Test 1: Verificar relaciones
        System.out.println("  🔗 Test 1: Verificar relaciones entre tablas");
        Statement stmt = connection.createStatement();
        
        ResultSet rs = stmt.executeQuery(
            "SELECT p.nombre as proyecto, a.nombre as actividad, d.nombre as documento " +
            "FROM proyectos p " +
            "LEFT JOIN actividades a ON p.nombre = a.nombre_proyecto " +
            "LEFT JOIN documentos d ON p.nombre = d.nombre_proyecto " +
            "WHERE p.nombre = 'PROYECTO_TEST_INTEGRACION'"
        );
        
        assert rs.next() : "❌ No se encontraron relaciones en BD";
        String proyecto = rs.getString("proyecto");
        String actividad = rs.getString("actividad");
        String documento = rs.getString("documento");
        
        assert "PROYECTO_TEST_INTEGRACION".equals(proyecto) : "❌ Relación de proyecto incorrecta";
        assert actividad != null : "❌ Relación de actividad incorrecta";
        assert documento != null : "❌ Relación de documento incorrecta";
        
        rs.close();
        stmt.close();
        System.out.println("    ✅ Relaciones verificadas correctamente");
        
        // Test 2: Verificar eliminación en cascada
        System.out.println("  🗑️ Test 2: Verificar eliminación en cascada");
        stmt = connection.createStatement();
        
        int actividadesAntes = 0;
        rs = stmt.executeQuery("SELECT COUNT(*) as count FROM actividades WHERE nombre_proyecto = 'PROYECTO_TEST_INTEGRACION'");
        if (rs.next()) actividadesAntes = rs.getInt("count");
        rs.close();
        
        int documentosAntes = 0;
        rs = stmt.executeQuery("SELECT COUNT(*) as count FROM documentos WHERE nombre_proyecto = 'PROYECTO_TEST_INTEGRACION'");
        if (rs.next()) documentosAntes = rs.getInt("count");
        rs.close();
        
        // Eliminar proyecto
        gestor.eliminarProyectoConDependencias("PROYECTO_TEST_INTEGRACION");
        
        // Verificar que se eliminó todo
        rs = stmt.executeQuery("SELECT COUNT(*) as count FROM proyectos WHERE nombre = 'PROYECTO_TEST_INTEGRACION'");
        assert rs.next() && rs.getInt("count") == 0 : "❌ Proyecto no se eliminó";
        rs.close();
        
        rs = stmt.executeQuery("SELECT COUNT(*) as count FROM actividades WHERE nombre_proyecto = 'PROYECTO_TEST_INTEGRACION'");
        assert rs.next() && rs.getInt("count") == 0 : "❌ Actividades no se eliminaron en cascada";
        rs.close();
        
        rs = stmt.executeQuery("SELECT COUNT(*) as count FROM documentos WHERE nombre_proyecto = 'PROYECTO_TEST_INTEGRACION'");
        assert rs.next() && rs.getInt("count") == 0 : "❌ Documentos no se eliminaron en cascada";
        rs.close();
        
        stmt.close();
        System.out.println("    ✅ Eliminación en cascada verificada");
        
        System.out.println("✅ TESTS DE BASE DE DATOS COMPLETADOS");
    }

    private static void cleanup() {
        System.out.println("\n🧹 LIMPIANDO ENTORNO DE PRUEBA...");
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            System.err.println("Error cerrando conexión: " + e.getMessage());
        }
        System.out.println("✅ Entorno limpio");
    }
}
