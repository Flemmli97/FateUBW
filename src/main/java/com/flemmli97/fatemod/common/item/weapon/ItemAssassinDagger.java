package com.flemmli97.fatemod.common.item.weapon;

import com.flemmli97.fatemod.Fate;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAssassinDagger extends ItemSword{

    public static ToolMaterial dagger_mat = EnumHelper.addToolMaterial("dagger_mat", 0, 550, 0.0F, 2.5F, 24);

	public ItemAssassinDagger() {
    	super(dagger_mat);
    	setUnlocalizedName("assassinDagger");
    	setCreativeTab(Fate.customTab);
    	GameRegistry.register(this, new ResourceLocation(Fate.MODID, "assassinDagger"));
    }

	@SideOnly(Side.CLIENT)
	    public void initModel() {
	        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	    }
}
