# Fase 0 — Auditoría del prototipo 0.1.0-alpha

Repositorio original leído: https://github.com/naim12mc/Mod (solo como fuente histórica).
Destino de desarrollo: https://github.com/naimsauria/mod .
La fase 0 MIGRA el código existente y documenta el trabajo futuro. No incorpora sistemas nuevos sin validar.

## Inventario verificado
- NaimsaurPrimeEntity: Monster + GeoEntity, vida máxima definida en ModEntities (520), barra vanilla de jefe, introducción, partículas y daños de servidor.
- CombatDirector: habilidades BITE, EARTHQUAKE, ATOMIC_BREATH, METEOR y APOCALYPSE, cuatro fases con umbrales del 75/50/25 por ciento.
- Modelo de 17 huesos, ocho animaciones; la animación death es recurso, no está conectada a una secuencia de muerte.
- PrimordialSigilItem: invocación mediante clic sobre bloque; evita duplicados cerca.
- Núcleo Omega: recompensa básica, sin habilidad especial.
- Registro de una entidad y dos objetos; traducciones español e inglés; receta de sello.
- GitHub Actions antiguo: compilación Java 25 con Fabric 26.2 y prueba manual de CombatDirector.

## Deuda técnica y prioridad
P0 — El código original requiere ejecutar fix_naimsauria_26_2.py en cada build: las seis correcciones deben integrarse en las clases fuente. Evitar dejar un parche que cambia fuentes durante la compilación.
P0 — Los campos introTicks, attackCooldown, attackIndex, telegraphLeft, pending, aimedAt y rewardGiven no se guardan mediante implementación propia. Verificar las firmas de persistencia de Entity en 26.2 y evitar reinicios incoherentes.
P0 — El script gradle build -x test evita la tarea test de Gradle porque CombatDirectorTest es un main y no un test JUnit. Migrar a pruebas reconocidas por Gradle.
P1 — MeleeAttackGoal convive con ataques especiales: el golpe normal no sigue la máquina de estados telegrafiada.
P1 — El código especial recorre server.players() y no filtra explícitamente espectadores/creativo, participantes o dimensión por sesión. En una arena multijugador podrían aplicarse efectos indebidos.
P1 — Falta EncounterSession para arena, desconexiones, reinicio, dificultad por participantes y persistencia.
P1 — No hay límite formal de paquetes/partículas, ni sistema de efectos ajustable.
P1 — La recompensa se entrega desde hurtServer al morir y usa rewardGiven solo en memoria. Revisar comportamiento de muerte, guardado y duplicación de loot.
P2 — Nombre de bossbar fijo en castellano; HUD no personalizado.
P2 — La entidad mezcla estado de combate, IA, partículas y audio en una sola clase.
P2 — Ausentes sonidos .ogg propios, música, cámaras, estructuras, logros, comandos y cuatro jefes restantes.

## Compatibilidad y protección de mundos
Mantener exactamente id de mod naimsauria y la entidad naimsauria:naimsaur_prime.
No renombrar identificadores de items, recetas o modelos sin migración explícita.
Cualquier cambio de guardado se verificará sobre copias de mundos existentes.
Los elementos visuales no determinarán daños ni hitboxes: la autoridad será del servidor.

## Pendiente de prueba real
⚠️ La compilación anterior no verifica que la entidad se dibuje correctamente, la animación siga los impactos, el sello funcione en survival o la bossbar sea estable tras recargar mundo. Se necesita iniciar un cliente de juego real y aportar latest.log ante cualquier fallo.
