package net.naimsauria.titanes.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.AABB;
import net.naimsauria.titanes.entity.NaimsaurPrimeEntity;
import net.naimsauria.titanes.registry.ModEntities;

/** El sello histórico sigue invocando la misma entidad para proteger mundos existentes. */
public final class PrimordialSigilItem extends Item {
    public PrimordialSigilItem(Properties properties) { super(properties); }

    @Override public InteractionResult useOn(UseOnContext ctx) {
        if (!(ctx.getLevel() instanceof ServerLevel level)) return InteractionResult.SUCCESS;
        if (ctx.getPlayer() == null) return InteractionResult.FAIL;
        BlockPos pos = ctx.getClickedPos().above();
        if (!level.getEntitiesOfClass(NaimsaurPrimeEntity.class,
                new AABB(pos).inflate(96)).isEmpty()) return InteractionResult.FAIL;
        NaimsaurPrimeEntity boss = ModEntities.NAIMSAUR_PRIME.create(level, EntitySpawnReason.COMMAND);
        if (boss == null) return InteractionResult.FAIL;
        boss.setPos(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
        boss.setYRot(level.getRandom().nextFloat() * 360);
        boss.setXRot(0);
        if (!level.addFreshEntity(boss)) return InteractionResult.FAIL;
        if (!ctx.getPlayer().getAbilities().instabuild) ctx.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }
}
