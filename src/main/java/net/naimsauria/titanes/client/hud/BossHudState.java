package net.naimsauria.titanes.client.hud;

import net.naimsauria.titanes.network.BossStatusPayload;

/** Caché visual del último jefe cercano; sin autoridad sobre el combate. */
public final class BossHudState {
    private static BossStatusPayload current;
    private static long lastSeen;

    private BossHudState() { }

    public static void update(BossStatusPayload packet) {
        if (packet.kind() < 0 || packet.kind() >= 5 || !Float.isFinite(packet.health())
            || !Float.isFinite(packet.poise())) return;
        current = packet;
        lastSeen = System.currentTimeMillis();
    }

    public static BossStatusPayload visible() {
        if (current == null || System.currentTimeMillis() - lastSeen > 1600) return null;
        return current;
    }

    public static void clear() { current = null; }
}
