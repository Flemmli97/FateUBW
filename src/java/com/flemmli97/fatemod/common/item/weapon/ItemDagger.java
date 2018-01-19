package com.flemmli97.fatemod.common.item.weapon;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.common.item.IExtendedReach;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDagger extends ItemSword implements IExtendedReach{

    public static ToolMaterial dagger_mat = EnumHelper.addToolMaterial("dagger_mat", 0, 1000, 0.0F, 5.0F, 10);

	public ItemDagger() {
		super(dagger_mat);
    	setCreativeTab(Fate.customTab);
    	setUnlocalizedName("dagger");
    	GameRegistry.register(this, new ResourceLocation(Fate.MODID, "dagger"));
	}

	@SideOnly(Side.CLIENT)
	    public void initModel() {
	        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	    }

	@Override
	public int getRange() {
		return 5;
	}
}
