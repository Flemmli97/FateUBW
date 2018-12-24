package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemGrimoire extends Item{

	public ItemGrimoire() {
		this.setCreativeTab(Fate.customTab);
		this.setMaxStackSize(1);
		this.setMaxDamage(64);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "prelati"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return EnumActionResult.FAIL;
	}
}
