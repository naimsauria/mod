# Compatibilidad de librerías — Minecraft Java 26.2

Fecha de revisión: 18 noviembre 2026. Se conservan las versiones del prototipo sin actualizarlas automáticamente.

| Librería/mod | Uso | Publicación para Fabric 26.2 | Sin ella |
|---|---|---|---|
| Fabric API | Eventos y utilidades Fabric | Obligatoria, fijada 0.155.2+26.2 | No aplica al build existente |
| GeckoLib | Modelos y animaciones | Obligatoria, 5.5.5 | Sistema de modelos vanilla, requiere refactor |
| YACL | GUI de configuración | Sí, 3.9.7+26.2-fabric | JSON de configuración |
| Cloth Config | GUI alternativa | Sí, 26.2.155+fabric | YACL o JSON |
| owo-lib | GUI y utilidades | Sí, 0.13.1+26.2 | Fabric API + utilidades propias |
| Mod Menu | Acceso cómodo a configuración | Sí, 20.0.3 | Archivos de configuración |
| LambDynamicLights | Luz dinámica opcional | Sí, 4.12.2+26.2 verificada | Texturas emisivas sin iluminación de bloques |
| Sodium | Compatibilidad de rendimiento | Sí, mc26.2-0.9.2-fabric | Render vanilla |
| Iris Shaders | Compatibilidad con shaderpacks | Sí, 1.11.4+26.2-fabric | Render vanilla |
| Veil | Shader/postproceso avanzado | ⚠️ No se verificó una build 26.2 en Modrinth | Overlays HUD y efectos nativos |

En esta fase NO se agrega ninguna nueva dependencia. Se recomienda GUI opcional con YACL, pero el mod debe arrancar sin ella. Sodium e Iris no deben ser dependencias: serán objetivos de pruebas de compatibilidad.

Fuentes oficiales de publicación:
- GeckoLib: https://modrinth.com/mod/geckolib/version/5.5.5
- YACL: https://modrinth.com/mod/yacl/version/3.9.7%2B26.2-fabric
- Cloth Config: https://modrinth.com/mod/cloth-config/version/26.2.155%2Bfabric
- owo-lib: https://modrinth.com/mod/owo-lib/version/0.13.1%2B26.2
- Mod Menu: https://modrinth.com/mod/modmenu/version/iRo7AZXf
- LambDynamicLights: https://modrinth.com/mod/lambdynamiclights/version/4.12.2%2B26.2
- Sodium: https://modrinth.com/mod/sodium/version/mc26.2-0.9.2-fabric
- Iris: https://modrinth.com/mod/iris/version/1.11.4%2B26.2-fabric
- Veil: https://modrinth.com/mod/veil/versions

Advertencia: que una build de un mod externo se publique para 26.2 no prueba automáticamente compatibilidad con nuestro renderizado. La compatibilidad final debe probarse.
