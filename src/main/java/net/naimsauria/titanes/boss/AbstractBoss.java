package net.naimsauria.titanes.boss;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.naimsauria.titanes.network.BossNetworking;

import java.util.List;

/** Núcleo autoritativo y reutilizable. El render nunca decide daños. */
public abstract class AbstractBoss extends Monster implements GeoEntity {
    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(AbstractBoss.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ACTION = SynchedEntityData.defineId(AbstractBoss.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> POISE = SynchedEntityData.defineId(AbstractBoss.class, EntityDataSerializers.FLOAT);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    protected final BossCombatMachine machine = new BossCombatMachine();
    protected Vec3 aimedAt = Vec3.ZERO;
    protected int introTicks = 110;
    protected int cooldownTicks = 30;
    protected int attackIndex;
    private Vec3 arenaOrigin;
    private boolean scaled;
    private int withoutParticipants;

    protected AbstractBoss(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        setPersistenceRequired();
        xpReward = 70;
    }

    public abstract BossKind bossKind();
    protected abstract BossAttack chooseAttack(Player target);
    protected abstract void executeBossAttack(ServerLevel level, BossAttack attack);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PHASE, 1);
        builder.define(ACTION, 0);
        builder.define(POISE, 0.0f);
    }

    public int bossPhase() { return entityData.get(PHASE); }
    public float poiseRatio() { return entityData.get(POISE) / 100.0f; }
    public BossCombatMachine.State bossState() { return machine.state(); }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 28));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>("movement", 6, state ->
            state.setAndContinue(RawAnimation.begin().thenLoop(
                state.isMoving() ? "animation.naimsaur_prime.walk" : "animation.naimsaur_prime.idle"))));
        registrar.add(new AnimationController<>("actions", 0, state -> PlayState.STOP)
            .triggerableAnim("roar", RawAnimation.begin().thenPlay("animation.naimsaur_prime.roar"))
            .triggerableAnim("bite", RawAnimation.begin().thenPlay("animation.naimsaur_prime.bite"))
            .triggerableAnim("shockwave", RawAnimation.begin().thenPlay("animation.naimsaur_prime.stomp"))
            .triggerableAnim("beam", RawAnimation.begin().thenPlay("animation.naimsaur_prime.beam"))
            .triggerableAnim("transform", RawAnimation.begin().thenPlay("animation.naimsaur_prime.transform")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return geoCache; }

    protected List<ServerPlayer> participants(ServerLevel server) {
        return server.players().stream()
            .filter(player -> player.isAlive() && !player.isSpectator() && !player.isCreative())
            .filter(player -> player.distanceToSqr(this) < 80.0 * 80.0)
            .toList();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!(level() instanceof ServerLevel server) || isDeadOrDying()) return;
        if (arenaOrigin == null) arenaOrigin = position();

        // Regla común a huevos, dispensadores y /summon: solo uno del mismo tipo por zona.
        if (tickCount == 1 && !server.getEntitiesOfClass(AbstractBoss.class,
                getBoundingBox().inflate(96), other -> other != this && other.getType() == getType()
                && other.getId() < getId()).isEmpty()) {
            discard();
            return;
        }

        List<ServerPlayer> players = participants(server);
        if (!scaled && !players.isEmpty()) scaleFor(players.size());
        if (scaled && players.isEmpty()) {
            if (++withoutParticipants >= 600) resetEncounter();
        } else {
            withoutParticipants = 0;
        }

        float ratio = getHealth() / getMaxHealth();
        int targetPhase = bossKind().phase(ratio);
        if (targetPhase > bossPhase()) {
            entityData.set(PHASE, targetPhase);
            machine.transition(45);
            triggerAnim("actions", "transform");
            server.playSound(null, blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 2f, .7f);
            server.sendParticles(ParticleTypes.REVERSE_PORTAL, getX(), getY() + 2, getZ(), 50, 2, 2, 2, .08);
        }

        if (tickCount % 10 == 0) BossNetworking.sendStatus(server, this);
        if (introTicks > 0) {
            introTicks--;
            getNavigation().stop();
            if (introTicks == 50) {
                triggerAnim("actions", "roar");
                server.playSound(null, blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 1.6f, .75f);
            }
            return;
        }

        if (machine.isBusy()) {
            getNavigation().stop();
            if (machine.state() == BossCombatMachine.State.WINDUP && tickCount % 6 == 0)
                telegraph(server, machine.current());
            if (machine.tick() && machine.current() != null) {
                executeBossAttack(server, machine.current());
            }
        } else {
            Player target = getTarget() instanceof Player candidate && candidate.isAlive()
                && !candidate.isCreative() && !candidate.isSpectator() ? candidate : null;
            if (target != null && !players.isEmpty()) {
                if (cooldownTicks-- <= 0) {
                    BossAttack selected = chooseAttack(target);
                    aimedAt = target.position();
                    machine.begin(selected);
                    onWindup(server, selected);
                    cooldownTicks = 20;
                } else if (distanceToSqr(target) > 7 * 7 && tickCount % 8 == 0) {
                    getNavigation().moveTo(target, 0.9);
                }
            }
        }
        entityData.set(ACTION, machine.state().ordinal());
    }

    protected void onWindup(ServerLevel server, BossAttack attack) {
        triggerAnim("actions", attack.id().contains("beam") ? "beam" : "shockwave");
        server.playSound(null, blockPosition(), SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.HOSTILE, 1.1f, .65f);
    }

    private void telegraph(ServerLevel server, BossAttack attack) {
        if (attack == null) return;
        for (int i = 0; i < 18; i++) {
            double a = i * Math.PI / 9.0;
            server.sendParticles(ParticleTypes.END_ROD,
                getX() + Math.cos(a) * attack.radius(), getY() + .2,
                getZ() + Math.sin(a) * attack.radius(), 1, 0, 0, 0, 0);
        }
    }

    protected void hitNearby(ServerLevel server, double radius, float damage, double knockback) {
        for (ServerPlayer player : participants(server)) {
            if (player.distanceToSqr(this) <= radius * radius) {
                player.hurtServer(server, damageSources().mobAttack(this), damage);
                Vec3 direction = player.position().subtract(position()).multiply(1, 0, 1).normalize();
                player.push(direction.x * knockback, .25, direction.z * knockback);
            }
        }
    }

    private void scaleFor(int players) {
        scaled = true;
        int n = Math.min(4, Math.max(1, players));
        double base = bossKind().health();
        double hp = base * (1.0 + .40 * (n - 1));
        if (getAttribute(Attributes.MAX_HEALTH) != null) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(hp);
            setHealth((float) hp);
        }
    }

    private void resetEncounter() {
        machine.reset();
        scaled = false;
        withoutParticipants = 0;
        cooldownTicks = 40;
        introTicks = 0;
        entityData.set(PHASE, 1);
        entityData.set(POISE, 0.0f);
        getNavigation().stop();
        setTarget(null);
        if (getAttribute(Attributes.MAX_HEALTH) != null)
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(bossKind().health());
        setHealth(getMaxHealth());
        if (arenaOrigin != null) setPos(arenaOrigin.x, arenaOrigin.y, arenaOrigin.z);
    }

    @Override
    public boolean hurtServer(ServerLevel server, DamageSource source, float damage) {
        if (introTicks > 0 || machine.state() == BossCombatMachine.State.TRANSITION)
            return false;
        float capped = Math.min(damage, 48.0f);
        boolean accepted = super.hurtServer(server, source, capped);
        if (accepted && !isDeadOrDying()) {
            float next = entityData.get(POISE) + Math.min(30, capped * 2.8f);
            if (next >= 100.0f) {
                entityData.set(POISE, 0.0f);
                machine.stun(65);
                triggerAnim("actions", "roar");
                server.sendParticles(ParticleTypes.CRIT, getX(), getY() + 3, getZ(), 30, 1, 2, 1, .03);
            } else {
                entityData.set(POISE, next);
            }
        }
        return accepted;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("NaimsaurPhase", bossPhase());
        output.putInt("NaimsaurIntro", introTicks);
        output.putInt("NaimsaurAttackIndex", attackIndex);
        output.putFloat("NaimsaurPoise", entityData.get(POISE));
        output.putBoolean("NaimsaurScaled", scaled);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        entityData.set(PHASE, Math.max(1, Math.min(bossKind().phases(), input.getInt("NaimsaurPhase").orElse(1))));
        introTicks = Math.max(0, input.getInt("NaimsaurIntro").orElse(0));
        attackIndex = Math.max(0, input.getInt("NaimsaurAttackIndex").orElse(0));
        entityData.set(POISE, Math.max(0, Math.min(100, input.getFloat("NaimsaurPoise").orElse(0f))));
        scaled = input.getBoolean("NaimsaurScaled").orElse(false);
        machine.reset();
    }
}
