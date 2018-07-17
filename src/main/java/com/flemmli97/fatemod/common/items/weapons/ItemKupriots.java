package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.items.IModelRegister;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKupriots extends ItemSword implements IModelRegister{
	
    public static ToolMaterial isk_mat = EnumHelper.addToolMaterial("isk_mat", 0, 700, 0.0F, 2.0F, 10);

    public ItemKupriots() {
    	super(isk_mat);
    	this.setCreativeTab(Fate.customTab);
    	this.setRegistryName(new ResourceLocation(LibReference.MODID, "isk_sword"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    public void initModel() {
	        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	    }

}
