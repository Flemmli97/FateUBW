package com.flemmli97.fatemod.common.item;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.handler.GrailWarPlayerTracker;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemDebug extends Item{
			
	public ItemDebug()
    {
		super();
		setUnlocalizedName("debug_item");
		GameRegistry.register(this, new ResourceLocation(Fate.MODID, "debug_item"));
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {		
		//IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
		if(!world.isRemote)
		{
			GrailWarPlayerTracker track = GrailWarPlayerTracker.get(player.worldObj);
    			track.addPlayer(player);
    			if(player.isSneaking())
    			{
    				track.reset();
    			}
    			else
    				track.addPlayer(player);
		}
		/*if(player.isSneaking())
		{
			capSync.useCommandSeal(player);
		}
		else
		{
			capSync.setCommandSeals(player, 3);
		}*/
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
}