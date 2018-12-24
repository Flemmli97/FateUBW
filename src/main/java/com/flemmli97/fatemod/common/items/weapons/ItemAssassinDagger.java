package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class ItemAssassinDagger extends ItemSword{

    public static ToolMaterial dagger_mat = EnumHelper.addToolMaterial("dagger_mat", 0, 550, 0.0F, 2.5F, 24);

	public ItemAssassinDagger() {
    	super(dagger_mat);
    	this.setCreativeTab(Fate.customTab);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "assassinDagger"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
}
