package com.flemmli97.fate.common.items;

import com.flemmli97.fate.common.registry.ModBlocks;
import com.flemmli97.fate.common.utils.EnumPositionChalk;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;

public class ItemChalk extends Item {

    public ItemChalk(Properties props) {
        super(props);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        World world = ctx.getWorld();
        if (world.isRemote)
            return ActionResultType.SUCCESS;
        ItemStack stack = ctx.getItem();
        BlockState blockUp = world.getBlockState(ctx.getPos().up());
        if (blockUp.isReplaceable(new BlockItemUseContext(ctx))) {
            BlockState state = ModBlocks.chalks.get(EnumPositionChalk.MIDDLE).get().getDefaultState();
            if (this.canPlace(new BlockItemUseContext(ctx), state) && world.setBlockState(ctx.getPos().up(), state)) {
                world.playSound(null, ctx.getPos().up(), SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1.0F / 2.0F, 1.0F);
                if (ctx.getPlayer() != null && !ctx.getPlayer().isCreative())
                    stack.damageItem(1, ctx.getPlayer(), player -> player.sendBreakAnimation(ctx.getHand()));
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    protected boolean canPlace(BlockItemUseContext ctx, BlockState state) {
        PlayerEntity playerentity = ctx.getPlayer();
        ISelectionContext iselectioncontext = playerentity == null ? ISelectionContext.dummy() : ISelectionContext.forEntity(playerentity);
        return state.isValidPosition(ctx.getWorld(), ctx.getPos()) && ctx.getWorld().canPlace(state, ctx.getPos(), iselectioncontext);
    }
}
