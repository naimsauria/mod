# NAIMSAURIA — Biblia visual de los cinco titanes

**Estado:** dirección de arte y producción. Solo el Dragón Abisal cuenta ya con rig de prueba, PNG, glowmask, 26 animaciones y seis sonidos sintetizados. No son diseños finales ni están vinculados todavía al código del mod.

## Diseño común
- Estilo: **Minecraft low-poly de alto detalle**, silueta expresiva, texturas 512–1024 px, contraste en valores y materiales.
- Escala visual: entre 2,8 y 8 bloques de altura de entidad; las alas pueden sobresalir más que la caja de colisión; ajustar bounds/culling de cada entidad.
- Cada criatura: articulaciones agrupadas por segmentos; pivotes centrados en hombros, caderas y cuello; huesos de ataque con nombres estables.
- Paleta propia, zona de emisión específica; **la máscara no brilla sin una capa de render emisiva**.
- Accesibilidad: aviso visual previo al golpe reconocible sin depender exclusivamente del sonido o el color.
- Los ciclos de animación deben permitir una lectura clara de anticipación, contacto y recuperación.

## 1. Rey Tirano — `rey_tirano`

- **Identidad:** T-Rex tanque, torso ancho, cráneo triangular, mandíbula grande, pies pesados, cola baja como contrapeso.
- **Palette:** rojos óxido `#6B2C28`, piedra `#3E3029`, hueso `#C1A477`, ojos ámbar `#F6A42A`.
- **Rig orientativo:** 65–80 huesos, cabeza (4), mandíbula (3), espina (8), cola (10), patas y dedos (24), brazos/garritas (8), placas y cicatrices (8).
- **Huesos de contacto:** `jaw`, `right_foot`, `left_foot`, `tail_tip`, `neck`.
- **Animación clave:** carga con patada al suelo y ruptura de obstáculo; ventana de aturdimiento tras choque.
- **Sonidos:** rugido cavernoso, dos tipos de pasos, mordisco, derrumbe, impacto de cola.
- **HUD:** piedra de badlands tallada, fragmentos de fósil, grietas rojas.

## 2. Matriarca — `matriarca`

- **Identidad:** raptor emplumado, pata en postura digitígrada, garra curva prominente, cuello flexible, velocidad.
- **Paleta:** verde musgo `#375742`, jade `#75B89A`, ocre `#CD9B58`, ojos verdes `#D2FE8B`.
- **Rig orientativo:** 45–60 huesos, manos (6), cuello (4), cola (8), patas (14), plumas de cabeza y brazos (20).
- **Huesos de contacto:** `left_sickle_claw`, `right_sickle_claw`, `jaw`, `tail_mid`.
- **Animación clave:** salto con punto de aterrizaje marcado, esquiva lateral y aullido de coordinación.
- **Sonidos:** chasquidos/llamadas cortas, zarpazo, aleteo de plumas, carrera ligera.
- **HUD:** selva/hojas cortantes, pequeñas marcas de tres garras, esmeralda.

## 3. Leviatán del Pantano — `leviatan`

- **Identidad:** Spinosaurus anfibio con cresta dorsal reconocible y cola ancha tipo remo.
- **Paleta:** azul petróleo `#1C4D53`, verde pantano `#486851`, limo `#809176`, membrana turquesa `#21CCCF`.
- **Rig orientativo:** 75–95 huesos, vela (14), cola remo (12), mandíbula larga (6), patas (18), membranas y dedos (20).
- **Huesos de contacto:** `tail_paddle`, `snout`, `left_foot`, `right_foot`, `sail_1`.
- **Animación clave:** entrada y salida del agua, golpe de cola horizontal, ola telegráfica, elevación de vela durante marea.
- **Sonidos:** rugido húmedo, chapoteos, burbujas y agua a presión, gorgoteo del lago.
- **HUD:** piedra sumergida, marcos de agua y escamas, azul fluorescente.

## 4. Dragón Abisal — `dragon_abisal` (primer prototipo artístico entregado)

- **Identidad:** dragón ciego cazador de vibraciones, sin ojos, alas rotas, corona de cuernos, núcleo bioluminiscente en pecho, tentáculos.
- **Paleta:** obsidiana `#171F2A`, azul pizarra `#304153`, violeta `#54476E`, cian `#29EEE7`.
- **Rig entregado:** 113 huesos y 120 cubos; 26 animaciones; atlas RGBA y máscara 1024×1024.
- **Huesos de contacto:** `heart_core`, `head`, `left_palm`, `right_palm`, `left_wing_root`, `right_wing_root`.
- **Animaciones prioritarias para pulir:** `sonic_beam`, `core_charge`, `wing_blast`, `spawn`, `stagger`, `death`.
- **Sonidos incluidos:** rugido sintetizado, núcleo, ráfaga sónica, pasos, ambiente y tema de prueba.
- **HUD futuro:** marco oscuro con pulso cian del núcleo, indicador de vibración y estabilidad.
- **Falta:** retopología visual manual, pintura artística, capa emisiva y asociación con el render/code.

## 5. NAIMSAUR PRIME — `naimsaur_prime`

- **Identidad:** kaiju ancestral, cuello poderoso, placas dorsales, crecimiento de cristales durante fases y eclipse visual.
- **Paleta:** negro azulado `#171B2C`, ultravioleta `#7C45B8`, azul nuclear `#1DA3FF`, blanco eléctrico `#D4F5FF`.
- **Rig futuro:** evolucionar los 17 huesos originales a 70–100 huesos sin cambiar `naimsauria:naimsaur_prime` ni romper mundos.
- **Huesos de contacto:** `jaw`, `tail_tip`, `plate_1` a `plate_6`, `heart`, `right_foot`.
- **Animación clave:** carga dorsal secuencial, aliento dirigido desde la boca y transformación de eclipse.
- **Sonidos:** rugido grave, pasos masivos, carga ascendente, descarga, respiración, transformación.
- **HUD:** obsidiana y eclipse, fisuras violeta, fases marcadas con fragmentos de cristal.

## Contrato de animaciones para todos

1. `idle`  2. `walk`  3. `run`  4. `turn_left`  5. `turn_right`
6. `spawn`  7. `roar`  8. `bite`  9. `stomp`  10. `tail_sweep`
11. `special_1`  12. `special_2`  13. `special_3`  14. `special_4`
15. `special_5`  16. `special_6`  17. `stagger`  18. `phase_transition`
19. `enrage`  20. `death`  21. `listen`  22. `recover`

**El Dragón Abisal tiene denominaciones más específicas** para sus ataques (`sonic_beam`, `wing_blast`, `grab`, etc.). No se deben renombrar sin actualizar los controladores GeckoLib correspondientes.

## Contrato de entrega para artistas externos

- Modelo editable nativo `.bbmodel` con pivotes y geometría ordenados.
- `*.geo.json`, `*.animation.json`, atlas PNG y máscara `_glowmask.png` final.
- Lista exacta de nombres de huesos y eventos temporales de golpe.
- Prueba de importación en Blockbench y una captura con modelo en pose de reposo.
- Licencia o cesión por escrito que autorice expresamente usar, modificar y distribuir en el mod; sin contenido copiado de Minecraft o franquicias cinematográficas.
- Vista frontal, lateral y tres cuartos para comprobar silueta y proporciones.