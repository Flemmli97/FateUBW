package com.flemmli97.fatemod.common.blocks;

import java.util.ArrayList;
import java.util.List;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockMagicOre extends Block{

	public BlockMagicOre() {
		super(Material.IRON);
		this.setCreativeTab(Fate.customTab);
        this.setUnlocalizedName("crystal_ore");
        this.blockSoundType = SoundType.STONE;
        this.setResistance(12.0F);
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(3.0F);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "crystal_ore"));
		this.setUnlocalizedName(this.getRegistryName().toString());   	
	}
			
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return true;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
	    drops.add(new ItemStack(ModItems.crystals[Block.RANDOM.nextInt(ModItems.crystals.length)], 1+Block.RANDOM.nextInt(2)+Block.RANDOM.nextInt(fortune+1)));
	    return drops;
	}

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
		return Block.RANDOM.nextInt(10)+2;
	}
}
