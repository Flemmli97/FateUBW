package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class ItemRuleBreaker extends ItemSword{

    public static ToolMaterial rulebreaker_mat = EnumHelper.addToolMaterial("rulebreaker_mat", 0, 300, 0.0F, 0.5F, 10);

    public ItemRuleBreaker() {
    	super(rulebreaker_mat);
    	this.setCreativeTab(Fate.customTab);
    	this.setRegistryName(new ResourceLocation(LibReference.MODID, "rule_breaker"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    }
}