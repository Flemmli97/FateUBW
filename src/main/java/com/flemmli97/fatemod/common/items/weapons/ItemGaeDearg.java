package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class ItemGaeDearg extends ClassSpear{
	
    public static ToolMaterial gaedearg_mat = EnumHelper.addToolMaterial("gaedearg_mat", 0, 900, 0.0F, 1.0F, 14);

	public ItemGaeDearg() {
    	super(gaedearg_mat, -1.5F);
    	this.setCreativeTab(Fate.customTab);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "gae_dearg"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
}
