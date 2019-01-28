package com.flemmli97.fatemod.common.items;

import java.util.List;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityBabylonWeapon;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemGateofBabylon extends Item{

	public ItemGateofBabylon() {
		this.setCreativeTab(Fate.customTab);
		this.setMaxStackSize(1);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "gate_of_babylon"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add("Might get removed");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote)
		{
			EntityBabylonWeapon weapon = new EntityBabylonWeapon(world, player);
			weapon.setEntityProperties();
		}
        return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}	
}