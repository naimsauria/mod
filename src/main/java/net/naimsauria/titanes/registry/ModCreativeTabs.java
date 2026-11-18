package net.naimsauria.titanes.registry;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.naimsauria.titanes.NaimsauriaMod;

public final class ModCreativeTabs {
    private ModCreativeTabs() { }
    private static final ResourceKey<CreativeModeTab> KEY = ResourceKey.create(
        BuiltInRegistries.CREATIVE_MODE_TAB.key(), NaimsauriaMod.id("main"));

    private static final CreativeModeTab NAIMSAURIA = FabricCreativeModeTab.builder()
        .title(Component.translatable("creativeTab.naimsauria"))
        .icon(() -> new ItemStack(ModItems.NAIMSAUR_PRIME_SPAWN_EGG))
        .displayItems((context, output) -> {
            output.accept(ModItems.SELLO_PRIMORDIAL);
            output.accept(ModItems.NUCLEO_SAURIO_OMEGA);
            output.accept(ModItems.REY_TIRANO_SPAWN_EGG);
            output.accept(ModItems.MATRIARCA_SPAWN_EGG);
            output.accept(ModItems.LEVIATAN_SPAWN_EGG);
            output.accept(ModItems.DRAGON_ABISAL_SPAWN_EGG);
            output.accept(ModItems.NAIMSAUR_PRIME_SPAWN_EGG);
        }).build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, KEY, NAIMSAURIA);
    }
}
