package com.flemmli97.fatemod.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMagicOre extends Block{

	public BlockMagicOre() {
		super(Material.IRON);
		this.setCreativeTab(Fate.customTab);
        this.setUnlocalizedName("crystal_ore");
        this.blockSoundType = SoundType.STONE;
        this.setResistance(12.0F);
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(3.0F);
    	GameRegistry.register(this, new ResourceLocation(Fate.MODID, "crystal_ore"));
    	GameRegistry.register(new ItemBlock(this), this.getRegistryName());   	
	}
			
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return true;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
	    drops.add(new ItemStack(ModItems.crystal, 1+Block.RANDOM.nextInt(2)+Block.RANDOM.nextInt(fortune+1), Block.RANDOM.nextInt(5)));
	    return drops;
	}
	
	

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		// TODO Auto-generated method stub
		return super.getItemDropped(state, rand, fortune);
	}

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
		return Block.RANDOM.nextInt(10)+2;
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}
