package com.flemmli97.fatemod.common.item.weapon;

import com.flemmli97.fatemod.Fate;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemInvisExcalibur extends ItemSword {
	
    public static ToolMaterial invis_mat = EnumHelper.addToolMaterial("invex_mat", 0, 1500, 0.0F, 2.0F, 10);

    public ItemInvisExcalibur() {
    		super(invis_mat);
    		setUnlocalizedName("invis_excalibur");
    		GameRegistry.register(this, new ResourceLocation(Fate.MODID, "invis_excalibur"));
    }
    
	 @Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	    public void initModel() {
	        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	    }

}
