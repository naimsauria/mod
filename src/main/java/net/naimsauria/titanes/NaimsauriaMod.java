package net.naimsauria.titanes;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import net.naimsauria.titanes.registry.ModEntities;
import net.naimsauria.titanes.registry.ModItems;

public final class NaimsauriaMod implements ModInitializer {
    public static final String MOD_ID = "naimsauria";
    public static Identifier id(String path) { return Identifier.fromNamespaceAndPath(MOD_ID, path); }
    @Override public void onInitialize() {
        ModEntities.initialize();
        ModItems.initialize();
    }
}
