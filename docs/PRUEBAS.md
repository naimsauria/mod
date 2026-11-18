# Pruebas y compilación del prototipo

## Compilación (GitHub Actions)
Workflow: .github/workflows/compilar.yml
Entorno: ubuntu-latest, JDK 25 Temurin, Gradle 9.5.1.

El proceso de origen era:
1. python3 fix_naimsauria_26_2.py
2. gradle testClasses --stacktrace
3. java -cp build/classes/java/main:build/classes/java/test net.naimsauria.titanes.combat.CombatDirectorTest
4. gradle clean build -x test --stacktrace
5. Validación build/libs/*.jar y subida del artefacto ZIP.

Ese flujo comprueba compilar y la lógica pura de CombatDirector; no simula el servidor, los renders ni las estructuras.
El JAR principal es naimsauria-titanes-0.1.0-alpha.jar. No instalar el -sources.jar.
⚠️ Gradle Wrapper todavía no existe: para uso local hace falta Gradle 9.5.1 instalado.

## Prueba en Minecraft
- Instalar Fabric Loader 0.20.1, Fabric API 0.155.2+26.2, GeckoLib Fabric 5.5.5, Java 25.
- Iniciar un mundo creativo nuevo y verificar que el mod cargue.
- Ejecutar /give @s naimsauria:sello_primordial
- Ejecutar /summon naimsauria:naimsaur_prime ~ ~ ~ en una zona amplia.
- Comprobar apariencia 3D, textura, barra de jefe, animaciones, daño y recompensa.
- Guardar y reiniciar para detectar fallos de persistencia.
- Probar con dos jugadores y verificar espectadores/creativo, reinicio y abandono de arena. Las últimas pruebas pueden fallar: son criterios pendientes, no promesas.

Ante fallos adjuntar logs/latest.log y, si existe, crash-reports/*.txt. Nunca compartir tokens ni contraseñas.
