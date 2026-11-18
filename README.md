# NAIMSAURIA: Reyes del Jurásico

**Minecraft Java 26.2 | Fabric Loader 0.20.1 | Fabric API 0.155.2+26.2 | GeckoLib 5.5.5 | Java 25 | Gradle 9.5.1 | Fabric Loom 1.17.17**

Proyecto original NAIMSAURIA migrado a este repositorio. La base técnica es el prototipo 0.1.0-alpha, que contiene UN jefe implementado: NAIMSAUR PRIME. El objetivo posterior es un mod 1.0 con cinco jefes y cinco templos, pero estas funciones no se anuncian como terminadas.

## Contenido importado
- Código fuente de la entidad naimsauria:naimsaur_prime, registro, items y lógica de combate.
- Modelo original GeckoLib, animaciones, textura 256x256 y proyecto editable de Blockbench.
- Sello Primordial, Núcleo del Saurio Omega, receta, idiomas es_es/en_us.
- Configuración de Fabric/Gradle y flujo de compilación que ya funcionaron en el proyecto de origen.
- Prueba de lógica de combate ejecutada mediante su método main.

## Ejecutar la compilación
En GitHub, abre Actions -> Compilar NAIMSAURIA 26.2. Un push a main la inicia automáticamente; también admite workflow_dispatch.
El artefacto generado se llama NAIMSAURIA-Minecraft-26.2. Dentro del ZIP usa únicamente naimsauria-titanes-0.1.0-alpha.jar: el JAR que termina en -sources.jar no se instala.

En local instala JDK 25 y Gradle 9.5.1. Este repositorio todavía no tiene Gradle Wrapper.
Ejecuta: python3 fix_naimsauria_26_2.py && gradle testClasses && gradle clean build -x test

## Probar
En un mundo nuevo con Fabric 26.2, Fabric API compatible y GeckoLib 5.5.5:
- /give @s naimsauria:sello_primordial
- /summon naimsauria:naimsaur_prime ~ ~ ~

No lo pruebes en un mundo Hardcore importante sin copia de seguridad.

## Documentación
- docs/FASE_0_AUDITORIA.md: inventario y deuda técnica real
- docs/DEPENDENCIAS_26_2.md: librerías verificadas y alternativas
- docs/ARQUITECTURA.md: módulos futuros sin código ficticio
- docs/ROADMAP.md: fases y criterios de aceptación
- docs/PRUEBAS.md: comprobaciones de build y juego

El código importado conserva deliberadamente sus APIs originales; el script de correcciones se mantiene hasta integrarlas directamente en Java en la siguiente fase. Compilar no equivale a haber probado renderizado, IA o rendimiento dentro del juego.
