package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.init.ModBlocks;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChalk extends Item implements IModelRegister{

	public ItemChalk() {
		this.setCreativeTab(Fate.customTab);
		this.setMaxStackSize(1);
		this.setMaxDamage(64);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "chalk"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		Block blockUp = world.getBlockState(pos.up()).getBlock();
		if(blockUp == Blocks.AIR || blockUp.isReplaceable(world, pos.up()))
		{
			if(world.getBlockState(pos).getBlock() != ModBlocks.chalkLine)
			if(world.setBlockState(pos.up(), ModBlocks.chalkLine.getDefaultState()))
			{
                world.playSound(player, pos.up(), SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.BLOCKS, 1.0F / 2.0F, 1.0F);
				if(!player.capabilities.isCreativeMode)
					stack.damageItem(1, player);
				return EnumActionResult.SUCCESS;

			}
		}
		return EnumActionResult.FAIL;
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
