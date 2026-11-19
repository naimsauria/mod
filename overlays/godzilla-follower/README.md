# NAIMSAURIA — Godzilla Follower (StreamElements)

Assets públicos para permitir un widget con JavaScript reducido. Modelo GLB/GZIP dividido en cinco partes de 180 kB aproximadamente (descarga paralela y reunión de bytes en el navegador sin pérdida); cinco audios MP3.

Base HTTPS: `https://cdn.jsdelivr.net/gh/naimsauria/mod@<SHA_DEL_COMMIT>/overlays/godzilla-follower/assets`

Archivos: `godzilla.glb.gz.part1` hasta `part5`, `pisada.mp3`, `rugido.mp3`, `carga.mp3`, `rayo.mp3`, `impacto.mp3`.

La alerta carga `part1…part5` en orden, las concatena y descomprime con `DecompressionStream('gzip')`. Los recursos son públicos al estar en este repositorio; confirma que tienes los derechos correspondientes para distribuirlos.
