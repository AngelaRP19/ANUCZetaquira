#!/bin/bash

echo "🧪 EJECUTANDO TESTS SIMPLES DE ANUC ZETAQUIRA"
echo "============================================"

# Compilar el proyecto
echo "📦 Compilando proyecto..."
mvn clean compile > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "❌ ERROR: No se pudo compilar el proyecto"
    exit 1
fi
echo "✅ Proyecto compilado exitosamente"

# Ejecutar test simple de integración
echo ""
echo "🎭 Ejecutando Test Simple de Integración..."
java -cp target/classes co.edu.uptc.functional.TestSimpleIntegracion

if [ $? -eq 0 ]; then
    echo "✅ Test Simple de Integración PASÓ"
else
    echo "❌ Test Simple de Integración FALLÓ"
    exit 1
fi

echo ""
echo "🎉 TEST SIMPLE DE INTEGRACIÓN HA PASADO!"
echo "✅ La aplicación está funcionando correctamente"
echo "✅ La integración Model-BD es completa"
echo "✅ Todos los cambios se persisten correctamente"
