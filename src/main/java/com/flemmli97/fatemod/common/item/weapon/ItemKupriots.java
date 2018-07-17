package com.flemmli97.fatemod.common.item.weapon;

import com.flemmli97.fatemod.Fate;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKupriots extends ItemSword{
	
    public static ToolMaterial isk_mat = EnumHelper.addToolMaterial("isk_mat", 0, 700, 0.0F, 2.0F, 10);

    public ItemKupriots() {
    	super(isk_mat);
    	setUnlocalizedName("isk_sword");
    	setCreativeTab(Fate.customTab);
    	GameRegistry.register(this, new ResourceLocation(Fate.MODID, "isk_sword"));

    }
    
	 @SideOnly(Side.CLIENT)
	    public void initModel() {
	        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	    }

}
