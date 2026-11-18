package net.naimsauria.titanes.entity;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.naimsauria.titanes.combat.CombatDirector;
import net.naimsauria.titanes.registry.ModItems;

/** Server-authoritative four-phase boss. Client only animates and renders. */
public final class NaimsaurPrimeEntity extends Monster implements GeoEntity {
    private static final EntityDataAccessor<Integer> PHASE =
        SynchedEntityData.defineId(NaimsaurPrimeEntity.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private final ServerBossEvent bossBar = new ServerBossEvent(
        Component.literal("NAIMSAUR PRIME — TITÁN DEL ECLIPSE"),
        BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);
    private int introTicks = 140;
    private int attackCooldown = 50;
    private int attackIndex = 0;
    private CombatDirector.AttackPlan pending;
    private int telegraphLeft;
    private Vec3 aimedAt;
    private boolean rewardGiven;

    public NaimsaurPrimeEntity(EntityType<? extends Monster> type, Level world) {
        super(type, world);
        this.setPersistenceRequired();
        this.xpReward = 100;
    }
    @Override protected void defineSynchedData(SynchedEntityData.Builder data) {
        super.defineSynchedData(data);
        data.define(PHASE, 1);
    }
    @Override protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.9, false));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.65));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 24));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    @Override public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player); bossBar.addPlayer(player);
    }
    @Override public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player); bossBar.removePlayer(player);
    }
    @Override public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, state -> {
            return state.setAndContinue(RawAnimation.begin().thenLoop(
                state.isMoving() ? "animation.naimsaur_prime.walk" : "animation.naimsaur_prime.idle"));
        }));
        controllers.add(new AnimationController<>(this, "actions", 0, state -> PlayState.STOP)
            .triggerableAnim("roar", RawAnimation.begin().thenPlay("animation.naimsaur_prime.roar"))
            .triggerableAnim("bite", RawAnimation.begin().thenPlay("animation.naimsaur_prime.bite"))
            .triggerableAnim("shockwave", RawAnimation.begin().thenPlay("animation.naimsaur_prime.stomp"))
            .triggerableAnim("beam", RawAnimation.begin().thenPlay("animation.naimsaur_prime.beam"))
            .triggerableAnim("transform", RawAnimation.begin().thenPlay("animation.naimsaur_prime.transform")));
    }
    @Override public AnimatableInstanceCache getAnimatableInstanceCache() { return geoCache; }

    @Override public void aiStep() {
        super.aiStep();
        if (!(level() instanceof ServerLevel server) || isDeadOrDying()) return;
        bossBar.setProgress(Math.max(0, getHealth() / getMaxHealth()));
        int newPhase = CombatDirector.phase(Math.max(0, Math.min(1, getHealth() / getMaxHealth())));
        int oldPhase = entityData.get(PHASE);
        if (newPhase != oldPhase) {
            entityData.set(PHASE, newPhase);
            triggerAnim("actions", "transform");
            roar(server);
            attackCooldown = 85;
            pending = null;
            server.sendParticles(ParticleTypes.REVERSE_PORTAL, getX(), getY()+3, getZ(), 95, 2, 3, 2, 0.18);
        }
        if (introTicks > 0) {
            introTicks--;
            getNavigation().stop();
            if (introTicks == 100 || introTicks == 40) roar(server);
            if (introTicks % 7 == 0) server.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                getX(), getY()+3, getZ(), 15, 2.6, 2.5, 2.6, 0.03);
            return;
        }
        Player target = getTarget() instanceof Player player ? player : null;
        if (pending != null) {
            getNavigation().stop();
            if (telegraphLeft % 5 == 0) warningParticles(server, pending.skill());
            if (--telegraphLeft <= 0) {
                execute(server, pending);
                attackCooldown = pending.cooldownTicks();
                pending = null;
            }
            return;
        }
        if (attackCooldown-- > 0 || target == null || !target.isAlive()) return;
        pending = CombatDirector.nextAttack(newPhase, attackIndex++, distanceTo(target));
        telegraphLeft = pending.telegraphTicks();
        aimedAt = target.position(); // snapshot: attack does not snap-track during warning
        switch (pending.skill()) {
            case BITE -> triggerAnim("actions", "bite");
            case EARTHQUAKE, APOCALYPSE -> triggerAnim("actions", "shockwave");
            case ATOMIC_BREATH, METEOR -> triggerAnim("actions", "beam");
        }
    }
    private void roar(ServerLevel server) {
        triggerAnim("actions", "roar");
        server.playSound(null, blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 2, 0.65f);
    }
    private void warningParticles(ServerLevel level, CombatDirector.Skill skill) {
        double radius = switch (skill) {
            case BITE -> 5.0; case EARTHQUAKE -> 9.0; case ATOMIC_BREATH -> 2.0;
            case METEOR -> 14.0; case APOCALYPSE -> 18.0;
        };
        // bounded count (24 particles maximum per telegraph step).
        for (int i=0; i<24; i++) {
            double a=i*Math.PI/12;
            double x=getX()+Math.cos(a)*radius, z=getZ()+Math.sin(a)*radius;
            level.sendParticles(ParticleTypes.END_ROD, x, getY()+0.2, z, 1, 0, 0.1, 0, 0);
        }
        if (skill == CombatDirector.Skill.ATOMIC_BREATH && aimedAt != null) {
            Vec3 dir=aimedAt.subtract(position()).normalize();
            for (int i=2;i<=24;i+=2) {
                Vec3 p=position().add(dir.scale(i));
                level.sendParticles(ParticleTypes.ELECTRIC_SPARK, p.x, p.y+2, p.z, 2, .2,.2,.2,0.02);
            }
        }
    }
    private void execute(ServerLevel server, CombatDirector.AttackPlan plan) {
        switch (plan.skill()) {
            case BITE -> damageAround(server, 5.5, plan.damage(), 0.6);
            case EARTHQUAKE -> {
                damageAround(server, plan.reach(), plan.damage(), 1.5);
                server.sendParticles(ParticleTypes.CLOUD, getX(), getY()+.4, getZ(), 100, 5, .3, 5, .12);
            }
            case ATOMIC_BREATH -> atomicBreath(server, plan.damage());
            case METEOR -> {
                if (aimedAt != null) {
                    server.sendParticles(ParticleTypes.DRAGON_BREATH, aimedAt.x, aimedAt.y+.6, aimedAt.z, 85, 3, 1, 3, .08);
                    for (ServerPlayer p : server.players())
                        if (p.position().distanceToSqr(aimedAt) < 25)
                            p.hurtServer(server, damageSources().mobAttack(this), plan.damage());
                }
            }
            case APOCALYPSE -> {
                // Safe center <= 4 blocks, dangerous ring between 6 and 20 blocks.
                for (ServerPlayer p : server.players()) {
                    double d=p.distanceTo(this);
                    if (d >= 6 && d < 20)
                        p.hurtServer(server, damageSources().mobAttack(this), plan.damage());
                }
                server.sendParticles(ParticleTypes.END_ROD, getX(), getY()+7, getZ(), 190, 9, 4, 9, .11);
            }
        }
        server.playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 1.4f, .8f);
    }
    private void damageAround(ServerLevel server, double radius, float damage, double knock) {
        for (ServerPlayer p : server.players()) {
            double d=p.distanceTo(this);
            if (d <= radius && p.isAlive()) {
                p.hurtServer(server, damageSources().mobAttack(this), damage);
                Vec3 n=p.position().subtract(position()).multiply(1, 0, 1).normalize();
                p.push(n.x*knock, Math.min(0.5, knock*0.3), n.z*knock);
            }
        }
    }
    private void atomicBreath(ServerLevel server, float damage) {
        if (aimedAt == null) return;
        Vec3 origin=position().add(0, 4.0, 0);
        Vec3 dir=aimedAt.add(0,1.0,0).subtract(origin).normalize();
        for (double i=1; i<27; i+=0.7) {
            Vec3 p=origin.add(dir.scale(i));
            server.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, p.x,p.y,p.z, 2,.22,.22,.22,.015);
        }
        for (ServerPlayer p : server.players()) {
            Vec3 relative=p.position().add(0,1.0,0).subtract(origin);
            double projection=relative.dot(dir);
            if (projection>0 && projection<26 && relative.subtract(dir.scale(projection)).length()<2.3)
                p.hurtServer(server, damageSources().mobAttack(this), damage);
        }
    }
    @Override public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (introTicks > 0) return false;
        boolean accepted = super.hurtServer(level, source, amount);
        if (accepted && isDeadOrDying() && !rewardGiven) {
            rewardGiven = true;
            level.addFreshEntity(new ItemEntity(level, getX(), getY()+1.5, getZ(),
                new ItemStack(ModItems.NUCLEO_SAURIO_OMEGA)));
            level.sendParticles(ParticleTypes.END_ROD, getX(), getY()+3, getZ(), 180, 2,3,2,.11);
        }
        return accepted;
    }
}
