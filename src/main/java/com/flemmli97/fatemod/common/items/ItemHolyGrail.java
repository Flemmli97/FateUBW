package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.GrailReward;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemHolyGrail extends Item{

	public ItemHolyGrail() {
    	this.setCreativeTab(Fate.customTab);
    	this.setMaxStackSize(1);
    	this.setRegistryName(new ResourceLocation(LibReference.MODID, "holy_grail"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(world.isRemote)
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		//GrailReward.giveRewards("astral", player);
		player.getHeldItem(hand).shrink(1);
		//TODO: add some kind of animation?
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	

}
