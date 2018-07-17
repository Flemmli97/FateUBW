package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.entity.EntitySpecialProjectile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class RenderProjectileBase<T extends EntitySpecialProjectile> extends Render<T>{

	private boolean useModel, itemRender;
	private ModelBase model;
	public static float vbuffScale = 0.00390625F;
	
    public RenderProjectileBase(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.useModel = false;
    }
    public RenderProjectileBase(RenderManager renderManagerIn, ModelBase model)
    {
        super(renderManagerIn);
        this.useModel = true;
        this.model = model;
    }
    public RenderProjectileBase(RenderManager man, boolean renderItem)
    {
    	super(man);
    	this.itemRender = renderItem;
    }

    @Override
	public void doRender(T projectile, double x, double y, double z, float entityYaw, float partialTick) {
		if(useModel)
		{
			this.doRenderModel(projectile, x, y, z, entityYaw, partialTick);
		}
		else if(itemRender)
		{
			this.doRenderItem(projectile, x, y, z, entityYaw, partialTick);
		}
        super.doRender(projectile, x, y, z, entityYaw, partialTick);
    }
    
	public void doRenderItem(T entity, double x, double y, double z, float entityYaw, float partialTick) {
		GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y+0.2, (float)z); //modification needed
        GlStateManager.enableRescaleNormal();
        float pitchModifier =95;
        if(this.getItemType() == 1)
        {
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        }
        else if(this.getItemType() == 0)
        {
        	GlStateManager.rotate((entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTick)+180, 0.0F, 1.0F, 0.0F);
        	GlStateManager.rotate((entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTick)+pitchModifier, 0.0F, 0.0F, 0.0F);
        	GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
        }
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        
        Minecraft.getMinecraft().getRenderItem().renderItem(renderItemStackFromEntity(entity), TransformType.THIRD_PERSON_RIGHT_HAND);
        
        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        if (!this.renderOutlines)
        {
            this.renderName(entity, x, y, z);
        }
	}
    
	public void doRenderModel(T entity, double x, double y, double z, float entityYaw, float partialTick) {
    		
    	this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTick, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((-entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTick)+180, 0.0F, 0.0F, 0.0F);
        GlStateManager.enableRescaleNormal();

        model.render(entity, 0, 0, 0, 0, 0, 0.0625F);
        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();    	
    }
	
	/**wont matter, if not rendering item*/
	public abstract ItemStack renderItemStackFromEntity(T entity);
	/**wont matter, if not rendering item, 0 = weapon, 1 = normal item*/
	public abstract int getItemType();

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return null;
	}

}
