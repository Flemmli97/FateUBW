package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemDebug extends Item{
			
	public ItemDebug()
    {
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "debug_item"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {		
		//IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
		/*if(!world.isRemote)
		{
			GrailWarPlayerTracker track = GrailWarPlayerTracker.get(player.world);
			if(player.isSneaking())
			{
				track.reset();
			}
			else
				track.addPlayer(player);
		}*/
		/*if(player.isSneaking())
		{
			capSync.useCommandSeal(player);
		}
		else
		{
			capSync.setCommandSeals(player, 3);
		}*/
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}