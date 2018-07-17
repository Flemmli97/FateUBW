package com.flemmli97.fatemod.common.items;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHolyGrail extends Item implements IModelRegister{

	public ItemHolyGrail() {
    	this.setCreativeTab(Fate.customTab);
    	this.setMaxStackSize(1);
    	this.setRegistryName(new ResourceLocation(LibReference.MODID, "holy_grail"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
