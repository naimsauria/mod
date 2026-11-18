# Fase 1 — Núcleo de jefes y resultados visibles

Fijadas las dependencias anteriores: MC 26.2, Java 25, Fabric Loader 0.20.1, API 0.155.2+26.2, GeckoLib 5.5.5, Loom 1.17.17 y Gradle 9.5.1.

## Implementación
- BossKind: atributos de salud, color y umbrales de cuatro y cinco fases.
- BossAttack/BossCombatMachine: wind-up, impacto exacto, recuperación, aturdimiento y transición.
- AbstractBoss: objetivos de IA, ataques dirigidos por servidor, poise, guardado ValueInput/ValueOutput, escalado inicial y reinicio sin participantes.
- NaimsaurPrimeEntity: reutiliza CombatDirector y los cinco ataques anteriores, ahora con cinco fases.
- DinoPrototypeEntity: cuatro prototipos con tamaños, salud y dos ataques iniciales por variante.
- Cinco huevos de generación con texturas originales de píxel y lore; una pestaña creativa NAIMSAURIA.
- BossStatusPayload y BossHud: red de estado compacta y HUD con barra fantasma, fases, poise y color.
- El código Java ya incorpora las seis correcciones de compatibilidad; el workflow deja de mutar Java durante el build.

## Advertencias honestas
Los cuatro modelos de prototipo reutilizan TEMPORALMENTE la geometría, textura y animaciones del Prime (no son los modelos finales). Los iconos de huevo tienen paletas propias. El comportamiento por dispensadores y /summon se controla por duplicados en el primer tick; hay que validar en Minecraft real. El HUD es un primer render de rectángulos, no el acabado artístico final. La cinematografía, modelos de 50-100 huesos, sonidos personalizados, jefes finales completos, templos, drops, armas y armaduras quedan para fases siguientes. No se ha probado dentro del cliente hasta completar el test manual.

## Pruebas en Minecraft
Instala Fabric 26.2, Fabric API 0.155.2+26.2, GeckoLib 5.5.5 y el jar de Actions.
En creativo verifica la pestaña NAIMSAURIA. Prueba los cinco huevos y los comandos:
/summon naimsauria:rey_tirano ~ ~ ~
/summon naimsauria:matriarca ~ ~ ~
/summon naimsauria:leviatan ~ ~ ~
/summon naimsauria:dragon_abisal ~ ~ ~
/summon naimsauria:naimsaur_prime ~ ~ ~
Comprueba el modelo, barra HUD en cada caso, salud, daño, fase y duplicados (incluidos dispensadores).
Para comprobar IA que ignora creativo, prueba supervivencia en un mundo de TEST, no hardcore.

## Assets de las siguientes fases
- Modelos de alta resolución: assets/naimsauria/geckolib/models/entity/*.geo.json
- Animaciones: assets/naimsauria/geckolib/animations/entity/*.animation.json
- Texturas y capas emisivas: assets/naimsauria/textures/entity/*.png
- HUD: assets/naimsauria/textures/gui/*.png
- Audio: assets/naimsauria/sounds/*.ogg + assets/naimsauria/sounds.json
Ningún audio nuevo es necesario para probar la fase 1: usa sonidos vanilla.

## Criterios de aceptación
- [ ] Gradle y dos pruebas de lógica en verde
- [ ] Inicia cliente Fabric 26.2
- [ ] Los cinco huevos y la pestaña creativa tienen texturas
- [ ] Los cinco jefes se renderizan sin errores
- [ ] HUD y fases se sincronizan en multijugador
- [ ] Duplicados por huevo, dispensador y /summon se rechazan
- [ ] Guardar y recargar no rompe estado
