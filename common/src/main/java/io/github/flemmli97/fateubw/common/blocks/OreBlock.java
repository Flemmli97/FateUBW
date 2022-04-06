package io.github.flemmli97.fateubw.common.blocks;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;
import java.util.function.Function;

public class OreBlock extends Block {

    private final Function<Random, Integer> xpFunc;

    public OreBlock(Function<Random, Integer> xpFunc, Properties props) {
        super(props);
        this.xpFunc = xpFunc;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack) {
        int i;
        super.spawnAfterBreak(state, level, pos, stack);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0 && (i = this.xpFunc.apply(level.random)) > 0) {
            this.popExperience(level, pos, i);
        }
    }
}
