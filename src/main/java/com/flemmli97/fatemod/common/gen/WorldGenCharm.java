package com.flemmli97.fatemod.common.gen;

import java.util.Random;

import com.flemmli97.fatemod.common.init.ModBlocks;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenCharm extends WorldGenerator{

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos pos) {

        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
        if (state.getBlock().isReplaceableOreGen(state, worldIn, pos, BlockMatcher.forBlock(Blocks.STONE)))
        {
            worldIn.setBlockState(pos, ModBlocks.servantCharmBlock.getDefaultState(), 2);       
        }
        return true;
	}
}
	