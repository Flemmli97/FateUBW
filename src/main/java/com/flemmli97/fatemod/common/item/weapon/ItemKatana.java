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

public class ItemKatana extends ItemSword{

    public static ToolMaterial katana_mat = EnumHelper.addToolMaterial("katana_mat", 0, 1000, 0.0F, 4.0F, 10);

    public ItemKatana() {
    	super(katana_mat);
    	setCreativeTab(Fate.customTab);
    	setUnlocalizedName("katana");
    	GameRegistry.register(this, new ResourceLocation(Fate.MODID, "katana"));
    }

	@SideOnly(Side.CLIENT)
	    public void initModel() {
	        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	    }
}
