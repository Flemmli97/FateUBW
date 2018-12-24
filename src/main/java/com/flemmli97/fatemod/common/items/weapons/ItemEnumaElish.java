package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class ItemEnumaElish extends ItemSword{

    public static ToolMaterial enumaelish_mat = EnumHelper.addToolMaterial("enumaelish_mat", 0, 1500, 0.0F, 4.0F, 10);

    public ItemEnumaElish() {
    	super(enumaelish_mat);
    	this.setCreativeTab(Fate.customTab);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "enuma_elish"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
}
