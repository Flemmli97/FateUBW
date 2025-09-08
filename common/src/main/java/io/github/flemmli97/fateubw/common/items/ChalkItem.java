package io.github.flemmli97.fateubw.common.items;

import io.github.flemmli97.fateubw.common.registry.FateBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class ChalkItem extends Item {

    public ChalkItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide)
            return InteractionResult.SUCCESS;
        BlockPlaceContext ctx = new BlockPlaceContext(context);
        ItemStack stack = ctx.getItemInHand();
        if (ctx.canPlace()) {
            BlockState state = FateBlocks.CHALK.get().defaultBlockState();
            if (this.canPlace(ctx, state) && level.setBlockAndUpdate(ctx.getClickedPos(), state)) {
                level.playSound(null, ctx.getClickedPos(), SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F / 2.0F, 1.0F);
                if (ctx.getPlayer() != null && !ctx.getPlayer().isCreative())
                    stack.hurtAndBreak(1, ctx.getPlayer(), LivingEntity.getSlotForHand(ctx.getHand()));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    protected boolean canPlace(BlockPlaceContext ctx, BlockState state) {
        Player playerentity = ctx.getPlayer();
        CollisionContext iselectioncontext = playerentity == null ? CollisionContext.empty() : CollisionContext.of(playerentity);
        return state.canSurvive(ctx.getLevel(), ctx.getClickedPos()) && ctx.getLevel().isUnobstructed(state, ctx.getClickedPos(), iselectioncontext);
    }
}
