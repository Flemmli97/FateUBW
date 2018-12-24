package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.EnumServantType;
import com.flemmli97.fatemod.common.init.ModItems;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemServantCharm extends Item{
	
	private EnumServantType type;
	public ItemServantCharm(EnumServantType type)
    {
		this.type=type;
        this.setCreativeTab(Fate.customTab);
        this.setMaxStackSize(16);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "servant_medallion_"+this.type.getLowercase()));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
	
	public EnumServantType type()
	{
		return this.type;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote)
		{
			if(this.type==EnumServantType.NOTASSIGNED)
			{
				if(!player.capabilities.isCreativeMode)
					stack.shrink(1);
				ItemStack charm = new ItemStack(ModItems.charms[world.rand.nextInt(ModItems.charms.length)]);
				EntityItem item = new EntityItem(world, player.posX, player.posY, player.posZ, charm);
				item.setPickupDelay(0);
				world.spawnEntity(item);	
		        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	}
}
