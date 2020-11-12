package com.flemmli97.fate.common.items;

import com.flemmli97.fate.common.blocks.BlockChalkLine;
import com.flemmli97.fate.common.registry.ModBlocks;
import com.flemmli97.fate.common.utils.EnumPositionChalk;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
            if (!(world.getBlockState(ctx.getPos()).getBlock() instanceof BlockChalkLine))
                if (world.setBlockState(ctx.getPos().up(), ModBlocks.chalks.get(EnumPositionChalk.MIDDLE).get().getDefaultState())) {
                    world.playSound(ctx.getPlayer(), ctx.getPos().up(), SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1.0F / 2.0F, 1.0F);
                    if (ctx.getPlayer() != null && !ctx.getPlayer().isCreative())
                        stack.damageItem(1, ctx.getPlayer(), player -> player.sendBreakAnimation(ctx.getHand()));
                    return ActionResultType.SUCCESS;

                }
        }
        return ActionResultType.PASS;
    }
}
