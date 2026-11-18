package net.naimsauria.titanes.client;

import com.geckolib.renderer.GeoEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.naimsauria.titanes.client.hud.BossHud;
import net.naimsauria.titanes.registry.ModEntities;

public final class NaimsauriaClient implements ClientModInitializer {
    @Override public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.NAIMSAUR_PRIME,
            context -> new GeoEntityRenderer<>(context, ModEntities.NAIMSAUR_PRIME));
        EntityRendererRegistry.register(ModEntities.REY_TIRANO,
            context -> new GeoEntityRenderer<>(context, ModEntities.REY_TIRANO));
        EntityRendererRegistry.register(ModEntities.MATRIARCA,
            context -> new GeoEntityRenderer<>(context, ModEntities.MATRIARCA));
        EntityRendererRegistry.register(ModEntities.LEVIATAN,
            context -> new GeoEntityRenderer<>(context, ModEntities.LEVIATAN));
        EntityRendererRegistry.register(ModEntities.DRAGON_ABISAL,
            context -> new GeoEntityRenderer<>(context, ModEntities.DRAGON_ABISAL));
        BossClientNetworking.initialize();
        BossHud.initialize();
    }
}
