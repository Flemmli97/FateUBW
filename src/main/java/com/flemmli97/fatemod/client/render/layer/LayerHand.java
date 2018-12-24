package com.flemmli97.fatemod.client.render.layer;

import com.flemmli97.fatemod.client.model.servant.ModelServant;
import com.flemmli97.fatemod.client.render.servant.RenderServant;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerHand extends LayerHeldItem
{
    private static final ItemStack genericWeapon = new ItemStack(Items.IRON_SWORD);

    public LayerHand(RenderLivingBase<?> renderLiving)
    {
    	super(renderLiving);
    }

    @Override
    public void doRenderLayer(EntityLivingBase servant, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	if(servant instanceof EntityServant)
    	{
	    	if(RenderServant.showIdentity((EntityServant) servant))
	    	{
	    		super.doRenderLayer(servant, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
	    	}
	    	else
	    	{
	    		GlStateManager.pushMatrix();
	    		EnumHandSide side = servant.getPrimaryHand();
	            boolean leftHanded = side == EnumHandSide.LEFT;
	    		ItemCameraTransforms.TransformType trans = leftHanded?TransformType.THIRD_PERSON_LEFT_HAND:TransformType.THIRD_PERSON_RIGHT_HAND;
	
	            if (servant.isSneaking())
	            {
	                GlStateManager.translate(0.0F, 0.2F, 0.0F);
	            }
	            // Forge: moved this call down, fixes incorrect offset while sneaking.
	            this.translateToHand(side);
	            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
	            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
	            GlStateManager.translate((leftHanded ? -1 : 1) / 16.0F, 0.125F, -0.625F);
	            Minecraft.getMinecraft().getItemRenderer().renderItemSide(servant, genericWeapon, trans, leftHanded);
	            GlStateManager.popMatrix();
	    	}
    	}
    }
    
    protected void translateToHand(EnumHandSide handSide)
    {
        ((ModelServant) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, handSide);
    }

}