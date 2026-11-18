package net.naimsauria.titanes.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.naimsauria.titanes.NaimsauriaMod;
import net.naimsauria.titanes.item.PrimordialSigilItem;

public final class ModItems {
    private static final ResourceKey<Item> SIGIL_KEY = ResourceKey.create(Registries.ITEM,
        NaimsauriaMod.id("sello_primordial"));
    private static final ResourceKey<Item> CORE_KEY = ResourceKey.create(Registries.ITEM,
        NaimsauriaMod.id("nucleo_saurio_omega"));

    public static final Item SELLO_PRIMORDIAL = Registry.register(BuiltInRegistries.ITEM, SIGIL_KEY,
        new PrimordialSigilItem(new Item.Properties().setId(SIGIL_KEY).stacksTo(1).rarity(Rarity.EPIC)));
    public static final Item NUCLEO_SAURIO_OMEGA = Registry.register(BuiltInRegistries.ITEM, CORE_KEY,
        new Item(new Item.Properties().setId(CORE_KEY).stacksTo(16).rarity(Rarity.EPIC)));

    public static void initialize() { }
}
