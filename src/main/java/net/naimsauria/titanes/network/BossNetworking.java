package net.naimsauria.titanes.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.naimsauria.titanes.boss.AbstractBoss;

public final class BossNetworking {
    private BossNetworking() { }

    public static void initialize() {
        PayloadTypeRegistry.clientboundPlay().register(BossStatusPayload.TYPE,
            BossStatusPayload.CODEC);
    }

    public static void sendStatus(ServerLevel world, AbstractBoss boss) {
        float health = Math.max(0, Math.min(1, boss.getHealth() / boss.getMaxHealth()));
        BossStatusPayload packet = new BossStatusPayload(
            boss.getId(), boss.bossKind().ordinal(), boss.bossPhase(),
            boss.bossState().ordinal(), health, boss.poiseRatio()
        );
        for (ServerPlayer player : world.players()) {
            if (player.distanceToSqr(boss) < 96.0 * 96.0
                && ServerPlayNetworking.canSend(player, BossStatusPayload.TYPE)) {
                ServerPlayNetworking.send(player, packet);
            }
        }
    }
}
