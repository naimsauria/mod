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
import net.naimsauria.titanes.entity.NaimsaurPrimeEntity;

public final class ModEntities {
    private static final ResourceKey<EntityType<?>> PRIME_KEY = ResourceKey.create(
        Registries.ENTITY_TYPE, NaimsauriaMod.id("naimsaur_prime"));

    public static final EntityType<NaimsaurPrimeEntity> NAIMSAUR_PRIME = Registry.register(
        BuiltInRegistries.ENTITY_TYPE, PRIME_KEY,
        EntityType.Builder.of(NaimsaurPrimeEntity::new, MobCategory.MONSTER)
            .sized(3.8f, 6.5f).clientTrackingRange(12).build(PRIME_KEY));

    public static void initialize() {
        FabricDefaultAttributeRegistry.register(NAIMSAUR_PRIME,
            NaimsaurPrimeEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 520.0)
                .add(Attributes.MOVEMENT_SPEED, 0.24)
                .add(Attributes.ATTACK_DAMAGE, 12.0)
                .add(Attributes.FOLLOW_RANGE, 44.0)
                .add(Attributes.ARMOR, 14.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0));
    }
}
