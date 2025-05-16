@echo on
setlocal EnableDelayedExpansion

echo Cleaning up old class files...
del /s /q "src\main\java\com\escuela\*.class" 2>nul
del /s /q "src\main\java\com\escuela\modelo\*.class" 2>nul
del /s /q "src\main\java\com\escuela\vista\*.class" 2>nul
del /s /q "src\main\java\com\escuela\controlador\*.class" 2>nul

echo Setting up classpath...
set CP=.;jackson-core.jar;jackson-databind.jar;jackson-annotations.jar;src\main\java
echo Classpath is: %CP%

echo Compiling model classes...
javac -verbose -cp "%CP%" src\main\java\com\escuela\modelo\*.java
if errorlevel 1 goto :error

echo Compiling view classes...
javac -verbose -cp "%CP%" src\main\java\com\escuela\vista\*.java
if errorlevel 1 goto :error

echo Compiling controller classes...
javac -verbose -cp "%CP%" src\main\java\com\escuela\controlador\*.java
if errorlevel 1 goto :error

echo Compiling Main class...
javac -verbose -cp "%CP%" src\main\java\com\escuela\Main.java
if errorlevel 1 goto :error

echo Running application...
java -verbose:class -cp "%CP%" com.escuela.Main
if errorlevel 1 goto :error

goto :end

:error
echo Failed with error #%errorlevel%.
pause
exit /b %errorlevel%

:end
echo Done!
pause
