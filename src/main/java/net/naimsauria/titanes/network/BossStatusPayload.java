package net.naimsauria.titanes.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.naimsauria.titanes.NaimsauriaMod;

/** Un único paquete compacto de estado, enviado como máximo diez veces por segundo. */
public record BossStatusPayload(
    int entityId, int kind, int phase, int action, float health, float poise
) implements CustomPacketPayload {
    public static final Type<BossStatusPayload> TYPE =
        new Type<>(NaimsauriaMod.id("boss_status"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BossStatusPayload> CODEC =
        StreamCodec.of(
            (buf, data) -> {
                buf.writeVarInt(data.entityId);
                buf.writeVarInt(data.kind);
                buf.writeVarInt(data.phase);
                buf.writeVarInt(data.action);
                buf.writeFloat(data.health);
                buf.writeFloat(data.poise);
            },
            buf -> new BossStatusPayload(buf.readVarInt(), buf.readVarInt(),
                buf.readVarInt(), buf.readVarInt(), buf.readFloat(), buf.readFloat())
        );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
