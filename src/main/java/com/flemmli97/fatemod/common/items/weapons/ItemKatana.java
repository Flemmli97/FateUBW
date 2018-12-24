package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class ItemKatana extends ItemSword{

    public static ToolMaterial katana_mat = EnumHelper.addToolMaterial("katana_mat", 0, 1000, 0.0F, 4.0F, 10);

    public ItemKatana() {
    	super(katana_mat);
    	this.setCreativeTab(Fate.customTab);
    	this.setRegistryName(new ResourceLocation(LibReference.MODID, "katana"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
}
