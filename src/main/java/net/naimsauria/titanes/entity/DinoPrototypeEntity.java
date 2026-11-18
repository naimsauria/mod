package net.naimsauria.titanes.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.naimsauria.titanes.boss.AbstractBoss;
import net.naimsauria.titanes.boss.BossAttack;
import net.naimsauria.titanes.boss.BossKind;
import net.naimsauria.titanes.registry.ModEntities;

/** Cuatro prototipos jugables: IA propia básica y recursos temporales de Prime. */
public final class DinoPrototypeEntity extends AbstractBoss {
    public DinoPrototypeEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override public BossKind bossKind() {
        if (getType() == ModEntities.REY_TIRANO) return BossKind.REY_TIRANO;
        if (getType() == ModEntities.MATRIARCA) return BossKind.MATRIARCA;
        if (getType() == ModEntities.LEVIATAN) return BossKind.LEVIATAN;
        return BossKind.DRAGON_ABISAL;
    }

    @Override protected BossAttack chooseAttack(Player target) {
        int next = attackIndex++;
        return switch (bossKind()) {
            case REY_TIRANO -> next % 3 == 0
                ? new BossAttack("seismic_stomp", 42, 32, 13, 9)
                : new BossAttack("tyrant_bite", 23, 25, 10, 5);
            case MATRIARCA -> next % 2 == 0
                ? new BossAttack("raptor_pounce", 18, 22, 8, 6)
                : new BossAttack("pack_roar", 35, 25, 6, 8);
            case LEVIATAN -> next % 2 == 0
                ? new BossAttack("tidal_sweep", 38, 28, 11, 11)
                : new BossAttack("swamp_bite", 25, 26, 9, 5);
            case DRAGON_ABISAL -> next % 2 == 0
                ? new BossAttack("sonic_beam", 48, 35, 15, 13)
                : new BossAttack("abyss_stomp", 30, 28, 12, 8);
            case NAIMSAUR_PRIME -> throw new IllegalStateException("Prime has its own combat");
        };
    }

    @Override protected void executeBossAttack(ServerLevel level, BossAttack attack) {
        hitNearby(level, attack.radius(), attack.damage(),
            bossKind() == BossKind.MATRIARCA ? .45 : .9);
        switch (bossKind()) {
            case REY_TIRANO -> level.sendParticles(ParticleTypes.CLOUD,
                getX(), getY()+.4, getZ(), 42, 4, .4, 4, .09);
            case MATRIARCA -> level.sendParticles(ParticleTypes.CRIT,
                getX(), getY()+2, getZ(), 32, 3, 1, 3, .07);
            case LEVIATAN -> level.sendParticles(ParticleTypes.SPLASH,
                getX(), getY()+1, getZ(), 54, 4, 1, 4, .07);
            case DRAGON_ABISAL -> level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                getX(), getY()+2.5, getZ(), 58, 4, 2, 4, .05);
            case NAIMSAUR_PRIME -> { }
        }
        level.playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE.value(),
            SoundSource.HOSTILE, 1f, bossKind() == BossKind.MATRIARCA ? 1.45f : .65f);
    }
}
