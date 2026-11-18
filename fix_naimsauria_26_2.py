#!/usr/bin/env python3
"""Parche idempotente para los 6 fallos Java ya observados en la V0.1.
Elimina este parche en la fase 2 cuando los dos .java ya esten corregidos
permanentemente en el repositorio.
"""
from pathlib import Path

BOSS = Path('src/main/java/net/naimsauria/titanes/entity/NaimsaurPrimeEntity.java')
SIGIL = Path('src/main/java/net/naimsauria/titanes/item/PrimordialSigilItem.java')

def replace_once(content, old, new):
    if old in content:
        if content.count(old) != 1:
            raise RuntimeError('Ambigüedad en origen: ' + old[:80])
        return content.replace(old, new)
    if new in content:
        return content
    raise RuntimeError('No se localizo la API esperada: ' + old[:80])

text = BOSS.read_text(encoding='utf-8')
text = replace_once(text, 'import net.minecraft.core.particles.ParticleTypes;',
                    'import java.util.UUID;\nimport net.minecraft.core.particles.ParticleTypes;')
text = replace_once(text, 'new ServerBossEvent(\n        Component.literal',
                    'new ServerBossEvent(\n        UUID.randomUUID(), Component.literal')
text = replace_once(text, 'new AnimationController<>(this, "movement", 5, state ->',
                    'new AnimationController<>("movement", 5, state ->')
text = replace_once(text, 'new AnimationController<>(this, "actions", 0, state ->',
                    'new AnimationController<>("actions", 0, state ->')
text = replace_once(text, 'server.sendParticles(ParticleTypes.DRAGON_BREATH, aimedAt.x',
                    'server.sendParticles(ParticleTypes.REVERSE_PORTAL, aimedAt.x')
BOSS.write_text(text, encoding='utf-8')

text = SIGIL.read_text(encoding='utf-8')
text = replace_once(text,
                    '        boss.moveTo(altar.getX() + 0.5, altar.getY(), altar.getZ() + 0.5,\n'
                    '                level.random.nextFloat() * 360, 0);',
                    '        boss.setPos(altar.getX() + 0.5, altar.getY(), altar.getZ() + 0.5);\n'
                    '        boss.setYRot(level.getRandom().nextFloat() * 360.0f);\n'
                    '        boss.setXRot(0.0f);')
SIGIL.write_text(text, encoding='utf-8')
print('Compatibilidad Minecraft 26.2 aplicada al prototipo.')
