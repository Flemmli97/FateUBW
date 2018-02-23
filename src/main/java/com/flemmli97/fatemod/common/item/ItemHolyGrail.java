package com.flemmli97.fatemod.common.item;

import com.flemmli97.fatemod.Fate;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHolyGrail extends Item{

	
	public ItemHolyGrail() {
    	super();
    	setUnlocalizedName("holy_grail");
    	setCreativeTab(Fate.customTab);
    	setMaxStackSize(1);
    	GameRegistry.register(this, new ResourceLocation(Fate.MODID, "holy_grail"));
    }

	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
