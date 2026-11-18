package net.naimsauria.titanes.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.naimsauria.titanes.NaimsauriaMod;
import net.naimsauria.titanes.boss.BossKind;
import net.naimsauria.titanes.entity.DinoPrototypeEntity;
import net.naimsauria.titanes.entity.NaimsaurPrimeEntity;

public final class ModEntities {
    private ModEntities() { }

    private static final ResourceKey<EntityType<?>> PRIME_KEY =
        ResourceKey.create(Registries.ENTITY_TYPE, NaimsauriaMod.id("naimsaur_prime"));

    public static final EntityType<NaimsaurPrimeEntity> NAIMSAUR_PRIME =
        Registry.register(BuiltInRegistries.ENTITY_TYPE, PRIME_KEY,
            EntityType.Builder.of(NaimsaurPrimeEntity::new, MobCategory.MONSTER)
                .sized(3.8f, 6.5f).clientTrackingRange(12).build(PRIME_KEY));

    public static final EntityType<DinoPrototypeEntity> REY_TIRANO =
        prototype(BossKind.REY_TIRANO, 3.4f, 5.4f);
    public static final EntityType<DinoPrototypeEntity> MATRIARCA =
        prototype(BossKind.MATRIARCA, 1.9f, 2.8f);
    public static final EntityType<DinoPrototypeEntity> LEVIATAN =
        prototype(BossKind.LEVIATAN, 3.5f, 5.3f);
    public static final EntityType<DinoPrototypeEntity> DRAGON_ABISAL =
        prototype(BossKind.DRAGON_ABISAL, 4.4f, 6.8f);

    private static EntityType<DinoPrototypeEntity> prototype(BossKind kind, float width, float height) {
        ResourceKey<EntityType<?>> key =
            ResourceKey.create(Registries.ENTITY_TYPE, NaimsauriaMod.id(kind.id()));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key,
            EntityType.Builder.of(DinoPrototypeEntity::new, MobCategory.MONSTER)
                .sized(width, height).clientTrackingRange(12).build(key));
    }

    public static void initialize() {
        attributes(NAIMSAUR_PRIME, BossKind.NAIMSAUR_PRIME, .24);
        attributes(REY_TIRANO, BossKind.REY_TIRANO, .22);
        attributes(MATRIARCA, BossKind.MATRIARCA, .37);
        attributes(LEVIATAN, BossKind.LEVIATAN, .26);
        attributes(DRAGON_ABISAL, BossKind.DRAGON_ABISAL, .27);
    }

    private static void attributes(EntityType<? extends net.minecraft.world.entity.monster.Monster> type,
                                   BossKind kind, double speed) {
        FabricDefaultAttributeRegistry.register(type,
            DinoPrototypeEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, kind.health())
                .add(Attributes.MOVEMENT_SPEED, speed)
                .add(Attributes.ATTACK_DAMAGE, kind == BossKind.MATRIARCA ? 8.0 : 12.0)
                .add(Attributes.FOLLOW_RANGE, 54.0)
                .add(Attributes.ARMOR, kind == BossKind.MATRIARCA ? 6.0 : 14.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, .9));
    }
}
