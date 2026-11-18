package net.naimsauria.titanes.client;

import com.geckolib.renderer.GeoEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.naimsauria.titanes.registry.ModEntities;

public final class NaimsauriaClient implements ClientModInitializer {
    @Override public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.NAIMSAUR_PRIME,
            context -> new GeoEntityRenderer<>(context, ModEntities.NAIMSAUR_PRIME));
    }
}
