package com.flemmli97.fatemod.common.item.weapon;

import com.flemmli97.fatemod.Fate;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGaeDearg extends ClassSpear {
	
    public static ToolMaterial gaedearg_mat = EnumHelper.addToolMaterial("gaedearg_mat", 0, 900, 0.0F, 1.0F, 14);

	public ItemGaeDearg() {
    	super(gaedearg_mat, -1.5F);
    	setUnlocalizedName("gae_dearg");
    	setCreativeTab(Fate.customTab);
    	GameRegistry.register(this, new ResourceLocation(Fate.MODID, "gae_dearg"));

	}
	
    
	 @SideOnly(Side.CLIENT)
	    public void initModel() {
	        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	    }

}
