package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class ItemArondight extends ItemSword{

    public static ToolMaterial arondight_mat = EnumHelper.addToolMaterial("arondight_mat", 0, 1300, 0.0F, 3.0F, 14);

    public ItemArondight() {
    	super(arondight_mat);
    	this.setCreativeTab(Fate.customTab);
    	this.setRegistryName(new ResourceLocation(LibReference.MODID, "arondight"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
}
