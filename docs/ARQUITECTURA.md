# Arquitectura objetivo (propuesta de fase 0)

El código del prototipo permanece por ahora bajo net.naimsauria.titanes.entity y combat.
NO se han creado ni se anuncian como implementadas las clases de esta propuesta.

## Paquetes previstos
- boss: AbstractDinoBoss, estado de combate, fases y reglas de daño.
- boss.attack: BossAttack, máquina de estados WINDUP -> EXECUTE -> RECOVERY -> IDLE, selección ponderada y cooldown.
- boss.dino: entidades Rey Tirano, Matriarca, Leviatán, Señor de las Tormentas y Prime.
- network: eventos cliente/servidor y estado compacto; usar CustomPacketPayload y StreamCodec SOLO tras validar firmas 26.2.
- client.hud: bossbar personalizada, fases, estabilidad y anticipación visual.
- client.camera: vibración, FOV y secuencias optativas con posibilidad de omitir.
- client.fx: partículas, overlays y compatibilidad gráfica.
- client.audio: audio por jefe, sonido ambiental y mezclas por fase.
- world: sesión de arena, estructuras, jigsaw, restauración reversible de cambios.
- registry: registro único de bloques, entidades, items, partículas y sonidos.
- config: ajustes de combate y visuales; servidor y cliente separados.
- command: comandos admin y herramientas de diagnóstico.
- datagen: recetas, traducciones, tags, loot y avances.

## Autoridad y sincronización
El servidor decide vida, fases, agresión, temporizadores, daños, participación y reinicios.
El cliente solo muestra HUD, efectos, sonidos y cámara. Cada paquete se valida y se limita.
Los efectos se agrupan, se priorizan por proximidad y respetan límites de calidad.
El estado persistente se guardará con las APIs oficiales verificadas para 26.2.

## Compatibilidad de mundo
Retener mod ID naimsauria y entity ID naimsauria:naimsaur_prime.
Integrar las correcciones al código fuente antes de crear AbstractDinoBoss.
Crear pruebas de compatibilidad de guardado y restauración con mundos de copia.
En multiplayer excluir explícitamente creativo, espectadores y otras dimensiones.

## Diseño de fases
Jefes 1 a 4: cuatro fases en umbrales 100 / 70 / 40 / 15 por ciento.
NAIMSAUR PRIME: cinco fases con habilidades heredadas; balance y umbrales se decidirán con pruebas.
Señales de ataque legibles para cada mecánica; daño solo en ventanas efectivas, recuperación real, poise y límites anti-cheese proporcionados.

## Audio y recursos
Los recursos originales de Prime quedan en src/main/resources/assets/naimsauria.
Sonidos futuros: assets/naimsauria/sounds/*.ogg con sounds.json; no hay música propia incluida.
HUD futuro: assets/naimsauria/textures/gui/.
Worldgen futuro: data/naimsauria/worldgen/ y data/naimsauria/structure/.
⚠️ La estructura exacta de data-driven worldgen y APIs de cámara, render, NBT y HUD depende de las firmas reales de 26.2: no se fijará código sin verificación.
