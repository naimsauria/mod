package net.naimsauria.titanes.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.naimsauria.titanes.entity.NaimsaurPrimeEntity;
import net.naimsauria.titanes.registry.ModEntities;

/** Right-click a block with the relic to begin the boss encounter. */
public final class PrimordialSigilItem extends Item {
    public PrimordialSigilItem(Properties properties) { super(properties); }

    @Override public InteractionResult useOn(UseOnContext ctx) {
        if (!(ctx.getLevel() instanceof ServerLevel level)) return InteractionResult.SUCCESS;
        if (ctx.getPlayer() == null) return InteractionResult.FAIL;
        BlockPos altar = ctx.getClickedPos().above();
        if (!level.getEntitiesOfClass(NaimsaurPrimeEntity.class,
                new AABB(altar).inflate(96)).isEmpty()) {
            return InteractionResult.FAIL;
        }
        NaimsaurPrimeEntity boss = ModEntities.NAIMSAUR_PRIME.create(level, EntitySpawnReason.COMMAND);
        if (boss == null) return InteractionResult.FAIL;
        boss.moveTo(altar.getX() + 0.5, altar.getY(), altar.getZ() + 0.5,
                level.random.nextFloat() * 360, 0);
        if (!level.addFreshEntity(boss)) return InteractionResult.FAIL;
        if (!ctx.getPlayer().getAbilities().instabuild) ctx.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }
}
