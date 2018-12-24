package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class ItemExcalibur extends ItemSword{
	
    public static ToolMaterial excalibur_mat = EnumHelper.addToolMaterial("excalibur_mat", 0, 1000, 0.0F, 5.0F, 10);

    public ItemExcalibur() {
    	super(excalibur_mat);
    	this.setCreativeTab(Fate.customTab);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "excalibur"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		/*if(!world.isRemote)
		{
			EntityExcalibur excalibur = new EntityExcalibur(world, player);	
			
			IPlayer mana = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(player.capabilities.isCreativeMode)
			{
				world.spawnEntityInWorld(excalibur);
			}
			else 
			{
				if (mana.useMana(player, 15))
				{
					world.spawnEntityInWorld(excalibur);
					player.addChatMessage(new TextComponentString(TextFormatting.AQUA + "Used Mana"));
				}
				else
				{
					player.addChatMessage(new TextComponentString(TextFormatting.AQUA + "You don't have enough mana"));
				}
			}
		}*/
		return super.onItemRightClick(world, player, hand);
		
	}		
}
