package com.flemmli97.fatemod.common.item;

import java.util.List;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.entity.EntityBabylonWeapon;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGateofBabylon extends Item {

	int fallBack;
	public ItemGateofBabylon() {
		setUnlocalizedName("gate_of_babylon");
		setCreativeTab(Fate.customTab);
		setMaxStackSize(1);
		GameRegistry.register(this, new ResourceLocation(Fate.MODID, "gate_of_babylon"));
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add("Might get removed");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		EntityBabylonWeapon weapon = new EntityBabylonWeapon(world, player);
		if(!world.isRemote)
		{
			weapon.setEntityProperties();
		}
		return super.onItemRightClick(stack, world, player, hand);
	}	
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}