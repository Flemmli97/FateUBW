package com.flemmli97.fatemod.common.items.weapons;

import com.flemmli97.fatemod.common.items.IModelRegister;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemInvisExcalibur extends ItemSword implements IModelRegister{
	
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

	@Override
	@SideOnly(Side.CLIENT)
    public void initModel() {
	        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	    }

}
