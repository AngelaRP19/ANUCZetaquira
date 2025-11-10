#!/bin/bash

# Script para ejecutar la aplicación usando Maven (asegura dependencias correctas)

echo "🚀 Iniciando aplicación ANUC Zetaquira con Maven..."

# Directorio base
cd "/Users/davidrodriguez/Desktop/AnucProyecto/ANUCZetaquira"

# Compilar con Maven para asegurar todas las dependencias
echo "📦 Compilando con Maven..."
mvn compile

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa con Maven"
    echo "🎮 Ejecutando aplicación con Maven..."
    
    # Ejecutar usando Maven exec plugin
    mvn exec:java -Dexec.mainClass="co.edu.uptc.presenter.Main"
else
    echo "❌ Error en la compilación con Maven"
    exit 1
fi
