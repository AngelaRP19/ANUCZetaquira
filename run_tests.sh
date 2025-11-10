#!/bin/bash

echo "🧪 EJECUTANDO TESTS COMPLETOS DE ANUC ZETAQUIRA"
echo "=============================================="

# Compilar el proyecto
echo "📦 Compilando proyecto..."
mvn clean compile > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "❌ ERROR: No se pudo compilar el proyecto"
    exit 1
fi
echo "✅ Proyecto compilado exitosamente"

# Ejecutar test de integración
echo ""
echo "🔧 Ejecutando Test de Integración..."
java -cp "target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)" co.edu.uptc.integration.TestIntegracionCompleto

if [ $? -eq 0 ]; then
    echo "✅ Test de Integración PASÓ"
else
    echo "❌ Test de Integración FALLÓ"
    exit 1
fi

# Ejecutar test simple de integración
echo ""
echo "🎭 Ejecutando Test Simple de Integración..."
java -cp "target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)" co.edu.uptc.functional.TestSimpleIntegracion

if [ $? -eq 0 ]; then
    echo "✅ Test Simple de Integración PASÓ"
else
    echo "❌ Test Simple de Integración FALLÓ"
    exit 1
fi

echo ""
echo "🎉 TODOS LOS TESTS HAN PASADO EXITOSAMENTE!"
echo "✅ La aplicación está funcionando correctamente"
echo "✅ La integración Vista-Presenter-Model-BD es completa"
echo "✅ Todos los cambios se persisten correctamente"
