@echo off
setlocal
where java >nul 2>nul || (echo Falta Java 25 & exit /b 1)
where gradle >nul 2>nul || (echo Falta Gradle 9.5.1. Ver README.md & exit /b 1)
java -version
call gradle build --stacktrace
if errorlevel 1 (echo ERROR: No se ha generado ningun JAR valido. & exit /b 1)
echo Revisa build\libs para encontrar el .jar
endlocal
