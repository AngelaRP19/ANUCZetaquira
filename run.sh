#!/bin/bash

# Script para ejecutar la aplicación con todas las dependencias correctas

echo "🚀 Iniciando aplicación ANUC Zetaquira..."

# Directorio base
BASE_DIR="/Users/davidrodriguez/Desktop/AnucProyecto/ANUCZetaquira"

# Construir el classpath con todas las dependencias
CLASSPATH="$BASE_DIR/target/classes"

# Agregar dependencias de Maven (rutas correctas)
CLASSPATH="$CLASSPATH:/Users/davidrodriguez/.m2/repository/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar"
CLASSPATH="$CLASSPATH:/Users/davidrodriguez/.m2/repository/org/slf4j/slf4j-simple/2.1.0-alpha1/slf4j-simple-2.1.0-alpha1.jar"
CLASSPATH="$CLASSPATH:/Users/davidrodriguez/.m2/repository/org/xerial/sqlite-jdbc/3.45.2.0/sqlite-jdbc-3.45.2.0.jar"
CLASSPATH="$CLASSPATH:/Users/davidrodriguez/.m2/repository/com/toedter/jcalendar/1.4/jcalendar-1.4.jar"

# Compilar el proyecto primero
echo "📦 Compilando proyecto..."
cd "$BASE_DIR"
javac -cp "$CLASSPATH" src/main/java/co/edu/uptc/presenter/Main.java -d target/classes

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
    echo "🎮 Ejecutando aplicación..."
    
    # Ejecutar la aplicación
    java -cp "$CLASSPATH" co.edu.uptc.presenter.Main
else
    echo "❌ Error en la compilación"
    exit 1
fi
