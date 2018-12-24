package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class ItemInvisExcalibur extends ItemSword{
	
    public static ToolMaterial invis_mat = EnumHelper.addToolMaterial("invex_mat", 0, 1500, 0.0F, 2.0F, 10);

    public ItemInvisExcalibur() {
		super(invis_mat);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "invis_excalibur"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
    
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
}
