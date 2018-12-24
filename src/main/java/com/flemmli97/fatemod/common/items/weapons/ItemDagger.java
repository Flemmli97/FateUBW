package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.api.item.IExtendedWeapon;

import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class ItemDagger extends ItemSword implements IExtendedWeapon{

    public static ToolMaterial dagger_mat = EnumHelper.addToolMaterial("dagger_mat", 0, 1000, 0.0F, 5.0F, 10);

	public ItemDagger() {
		super(dagger_mat);
    	this.setCreativeTab(Fate.customTab);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "dagger"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}

	@Override
	public float getRange() {
		return 5;
	}
}
