package net.naimsauria.titanes.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.type.ItemLore;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.entity.EntityType;
import net.naimsauria.titanes.NaimsauriaMod;
import net.naimsauria.titanes.item.PrimordialSigilItem;
import java.util.List;

public final class ModItems {
    private ModItems() { }

    private static ResourceKey<Item> key(String id) {
        return ResourceKey.create(Registries.ITEM, NaimsauriaMod.id(id));
    }

    public static final Item SELLO_PRIMORDIAL = Registry.register(
        BuiltInRegistries.ITEM, key("sello_primordial"),
        new PrimordialSigilItem(new Item.Properties()
            .setId(key("sello_primordial")).stacksTo(1).rarity(Rarity.EPIC)));

    public static final Item NUCLEO_SAURIO_OMEGA = Registry.register(
        BuiltInRegistries.ITEM, key("nucleo_saurio_omega"),
        new Item(new Item.Properties()
            .setId(key("nucleo_saurio_omega")).stacksTo(16).rarity(Rarity.EPIC)));

    public static final Item REY_TIRANO_SPAWN_EGG = egg("rey_tirano_spawn_egg", ModEntities.REY_TIRANO);
    public static final Item MATRIARCA_SPAWN_EGG = egg("matriarca_spawn_egg", ModEntities.MATRIARCA);
    public static final Item LEVIATAN_SPAWN_EGG = egg("leviatan_spawn_egg", ModEntities.LEVIATAN);
    public static final Item DRAGON_ABISAL_SPAWN_EGG = egg("dragon_abisal_spawn_egg", ModEntities.DRAGON_ABISAL);
    public static final Item NAIMSAUR_PRIME_SPAWN_EGG = egg("naimsaur_prime_spawn_egg", ModEntities.NAIMSAUR_PRIME);

    private static Item egg(String id, EntityType<?> type) {
        ResourceKey<Item> itemKey = key(id);
        return Registry.register(BuiltInRegistries.ITEM, itemKey,
            new SpawnEggItem(new Item.Properties()
                .setId(itemKey)
                .stacksTo(16)
                .spawnEgg(type)
                .component(DataComponents.LORE, new ItemLore(List.of(
                    Component.translatable("lore.naimsauria." + id))))));
    }

    public static void initialize() { }
}
