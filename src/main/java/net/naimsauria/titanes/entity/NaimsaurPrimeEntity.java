package net.naimsauria.titanes.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.naimsauria.titanes.boss.AbstractBoss;
import net.naimsauria.titanes.boss.BossAttack;
import net.naimsauria.titanes.boss.BossKind;
import net.naimsauria.titanes.combat.CombatDirector;
import net.naimsauria.titanes.registry.ModItems;

/** El jefe original: conserva su ID, modelo y cinco ataques de combate. */
public final class NaimsaurPrimeEntity extends AbstractBoss {
    private CombatDirector.AttackPlan selectedPlan;
    private boolean rewardGiven;

    public NaimsaurPrimeEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        xpReward = 100;
    }

    @Override public BossKind bossKind() { return BossKind.NAIMSAUR_PRIME; }

    @Override protected BossAttack chooseAttack(Player target) {
        // Adaptamos el director existente a las cinco fases sin perder habilidades.
        selectedPlan = CombatDirector.nextAttack(Math.min(bossPhase(), 4),
            attackIndex++, distanceTo(target));
        float multiplier = bossPhase() == 5 ? 1.25f : 1.0f;
        return new BossAttack(selectedPlan.skill().name().toLowerCase(),
            selectedPlan.telegraphTicks(), Math.max(18, selectedPlan.cooldownTicks() / 2),
            selectedPlan.damage() * multiplier, selectedPlan.reach());
    }

    @Override protected void onWindup(ServerLevel server, BossAttack attack) {
        String name = attack.id();
        if (name.equals("atomic_breath") || name.equals("meteor")) triggerAnim("actions", "beam");
        else if (name.equals("bite")) triggerAnim("actions", "bite");
        else triggerAnim("actions", "shockwave");
        server.playSound(null, blockPosition(), SoundEvents.ENDER_DRAGON_GROWL,
            SoundSource.HOSTILE, 1.3f, .8f);
    }

    @Override protected void executeBossAttack(ServerLevel server, BossAttack attack) {
        if (selectedPlan == null) return;
        switch (selectedPlan.skill()) {
            case BITE -> hitNearby(server, 5.5, attack.damage(), .5);
            case EARTHQUAKE -> {
                hitNearby(server, 9.0, attack.damage(), 1.2);
                server.sendParticles(ParticleTypes.CLOUD, getX(), getY()+.2, getZ(),
                    65, 4, .3, 4, .08);
            }
            case ATOMIC_BREATH -> atomicBreath(server, attack.damage());
            case METEOR -> {
                server.sendParticles(ParticleTypes.REVERSE_PORTAL,
                    aimedAt.x, aimedAt.y+.4, aimedAt.z, 70, 2, 1, 2, .08);
                for (ServerPlayer player : participants(server)) {
                    if (player.position().distanceToSqr(aimedAt) < 25) {
                        player.hurtServer(server, damageSources().mobAttack(this), attack.damage());
                    }
                }
            }
            case APOCALYPSE -> {
                // Anillo peligroso con zona segura cerca del centro.
                for (ServerPlayer player : participants(server)) {
                    double distance = player.distanceTo(this);
                    if (distance >= 6 && distance < 20)
                        player.hurtServer(server, damageSources().mobAttack(this), attack.damage());
                }
                server.sendParticles(ParticleTypes.END_ROD, getX(), getY()+4, getZ(),
                    85, 8, 3, 8, .12);
            }
        }
        server.playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE.value(),
            SoundSource.HOSTILE, 1.2f, .7f);
        selectedPlan = null;
    }

    private void atomicBreath(ServerLevel server, float damage) {
        Vec3 origin = position().add(0, 4, 0);
        Vec3 direction = aimedAt.add(0, 1, 0).subtract(origin).normalize();
        for (double t = 0; t < 26; t += 1) {
            Vec3 point = origin.add(direction.scale(t));
            server.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                point.x, point.y, point.z, 1, .16, .16, .16, .01);
        }
        for (ServerPlayer player : participants(server)) {
            Vec3 offset = player.position().add(0, 1, 0).subtract(origin);
            double projected = offset.dot(direction);
            if (projected > 0 && projected < 26
                && offset.subtract(direction.scale(projected)).length() < 2.3)
                player.hurtServer(server, damageSources().mobAttack(this), damage);
        }
    }

    @Override public boolean hurtServer(ServerLevel server, DamageSource source, float amount) {
        boolean accepted = super.hurtServer(server, source, amount);
        if (accepted && isDeadOrDying() && !rewardGiven) {
            rewardGiven = true;
            server.addFreshEntity(new ItemEntity(server, getX(), getY()+1.5, getZ(),
                new ItemStack(ModItems.NUCLEO_SAURIO_OMEGA)));
            server.sendParticles(ParticleTypes.END_ROD, getX(), getY()+3, getZ(),
                90, 2, 3, 2, .06);
        }
        return accepted;
    }

    @Override protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("NaimsaurRewardGiven", rewardGiven ? 1 : 0);
    }

    @Override protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        rewardGiven = input.getInt("NaimsaurRewardGiven").orElse(0) != 0;
    }
}
