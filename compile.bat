@echo off
setlocal

rem Delete existing class files
del /s /q "src\main\java\com\escuela\*.class"
del /s /q "src\main\java\com\escuela\modelo\*.class"
del /s /q "src\main\java\com\escuela\vista\*.class"
del /s /q "src\main\java\com\escuela\controlador\*.class"

set CLASSPATH=.;jackson-core.jar;jackson-databind.jar;jackson-annotations.jar;src\main\java

echo Compilando clases del modelo...
javac -cp "%CLASSPATH%" src\main\java\com\escuela\modelo\*.java

echo Compilando clases de la vista...
javac -cp "%CLASSPATH%" src\main\java\com\escuela\vista\*.java

echo Compilando clases del controlador...
javac -cp "%CLASSPATH%" src\main\java\com\escuela\controlador\*.java

echo Compilando Main...
javac -cp "%CLASSPATH%" src\main\java\com\escuela\Main.java

echo Ejecutando la aplicacion...
java -cp "%CLASSPATH%" com.escuela.Main

pause
