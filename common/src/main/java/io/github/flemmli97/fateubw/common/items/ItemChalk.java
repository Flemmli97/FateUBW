package io.github.flemmli97.fateubw.common.items;

import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class ItemChalk extends Item {

    public ItemChalk(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext ictx) {
        Level world = ictx.getLevel();
        if (world.isClientSide)
            return InteractionResult.SUCCESS;
        BlockPlaceContext ctx = new BlockPlaceContext(ictx);
        ItemStack stack = ctx.getItemInHand();
        if (ctx.canPlace()) {
            BlockState state = ModBlocks.chalk.get().defaultBlockState();
            if (this.canPlace(ctx, state) && world.setBlockAndUpdate(ctx.getClickedPos(), state)) {
                world.playSound(null, ctx.getClickedPos(), SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F / 2.0F, 1.0F);
                if (ctx.getPlayer() != null && !ctx.getPlayer().isCreative())
                    stack.hurtAndBreak(1, ctx.getPlayer(), player -> player.broadcastBreakEvent(ctx.getHand()));
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
