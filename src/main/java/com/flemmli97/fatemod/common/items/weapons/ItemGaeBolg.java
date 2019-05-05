package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityGaeBolg;
import com.flemmli97.fatemod.common.handler.capabilities.IPlayer;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.common.TextHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class ItemGaeBolg extends ClassSpear{

    public static ToolMaterial gaebolg_mat = EnumHelper.addToolMaterial("gaebolg_mat", 0, 900, 0.0F, 3.5F, 14);

    public ItemGaeBolg() {
    	super(gaebolg_mat, -1.5F);
    	this.setCreativeTab(Fate.customTab);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "gae_bolg"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
    
    @Override
	public ActionResult <ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    	ItemStack stack = player.getHeldItem(hand);
    	if(!world.isRemote)
		{
			EntityGaeBolg gaeBolg = new EntityGaeBolg(world, player);			     
			gaeBolg.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1.5F, 0);	
			IPlayer mana = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(player.capabilities.isCreativeMode)
			{
				world.spawnEntity(gaeBolg);
			}
			else 
			{
				if (mana.useMana(player, 15))
				{
					world.spawnEntity(gaeBolg);
					stack.shrink(1);;
				}
				else
				{
					player.sendMessage(TextHelper.setColor(new TextComponentTranslation("chat.mana.missing"), TextFormatting.AQUA));
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
}
