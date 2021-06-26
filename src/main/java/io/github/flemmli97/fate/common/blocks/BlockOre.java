package io.github.flemmli97.fate.common.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Random;
import java.util.function.Function;

public class BlockOre extends Block {

    private final Function<Random, Integer> xpFunc;

    public BlockOre(Function<Random, Integer> xpFunc, Properties props) {
        super(props);
        this.xpFunc = xpFunc;
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? this.xpFunc.apply(this.RANDOM) : 0;
    }
}
