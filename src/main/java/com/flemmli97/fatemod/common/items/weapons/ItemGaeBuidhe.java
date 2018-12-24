package com.flemmli97.fatemod.common.items.weapons;


import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class ItemGaeBuidhe extends ClassSpear{

    public static ToolMaterial gaebuidhe_mat = EnumHelper.addToolMaterial("gaebuidhe_mat", 0, 900, 0.0F, 4.0F, 14);

	public ItemGaeBuidhe() {
    	super(gaebuidhe_mat, -1.5F);
    	this.setCreativeTab(Fate.customTab);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "gae_buidhe"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
}
