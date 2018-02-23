package com.flemmli97.fatemod.common.blocks;

import java.util.ArrayList;
import java.util.List;

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

public class BlockServantCharm extends Block {

	public BlockServantCharm() {
		super(Material.IRON);
		this.setCreativeTab(Fate.customTab);
        this.setUnlocalizedName("charm_ore");
        this.blockSoundType = SoundType.STONE;
        this.setResistance(12.0F);
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(3.0F);
    	GameRegistry.register(this, new ResourceLocation(Fate.MODID, "charm_ore"));
    	GameRegistry.register(new ItemBlock(this), this.getRegistryName());
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return true;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
	    drops.add(new ItemStack(ModItems.charm, 1, 0));
	    return drops;
	}

	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
		return 5;
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

}
