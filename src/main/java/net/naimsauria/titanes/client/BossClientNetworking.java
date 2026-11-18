package net.naimsauria.titanes.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.naimsauria.titanes.client.hud.BossHudState;
import net.naimsauria.titanes.network.BossStatusPayload;

/** El cliente jamás ejecuta ataques al recibir un estado visual. */
public final class BossClientNetworking {
    private BossClientNetworking() { }

    public static void initialize() {
        ClientPlayNetworking.registerGlobalReceiver(BossStatusPayload.TYPE,
            (data, context) -> context.client().execute(() -> BossHudState.update(data)));
    }
}
