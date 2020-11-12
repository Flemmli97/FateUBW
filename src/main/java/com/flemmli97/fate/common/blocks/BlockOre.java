package com.flemmli97.fate.common.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.Random;
import java.util.function.Function;

public class BlockOre extends Block {

    private final Function<Random, Integer> xpFunc;

    public BlockOre(Function<Random, Integer> xpFunc, Properties props) {
        super(props);
        this.xpFunc = xpFunc;
    }

    /*@Override
    public boolean canRenderInLayer(BlockState state, BlockRender layer) {
        return true;
    }*/

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? this.xpFunc.apply(this.RANDOM) : 0;
    }
}
