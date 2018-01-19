package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.client.model.servant.ModelServant;
import com.flemmli97.fatemod.client.render.layer.LayerHand;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

public class RenderServant<T extends EntityServant> extends RenderLiving<T>{
	
	
    private static final ResourceLocation DEFAULT_RES_LOC = new ResourceLocation("textures/entity/steve.png");
    protected ModelServant modelServantMain;
    protected float scale;

    public RenderServant(RenderManager renderManagerIn, ModelServant modelServant, float shadowSize)
    {
        super(renderManagerIn, modelServant, shadowSize);
        this.modelServantMain = modelServant;
        this.addLayer(new LayerCustomHead(modelServant.servantHead));
        this.addLayer(new LayerHand(this));
    }
    
    @Override
	protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
    	ItemStack stackMain = entitylivingbaseIn.stackFromHand(EnumHand.MAIN_HAND);
    	ItemStack stackOff = entitylivingbaseIn.stackFromHand(EnumHand.OFF_HAND);
		modelServantMain.heldItemMain = 0;
		modelServantMain.heldItemOff = 0;
    	if(stackMain!=null)
    	{
    		modelServantMain.heldItemMain = 1;
    	}
    	if(stackOff!=null)
    	{
    		modelServantMain.heldItemOff = 1;
    	}
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	protected ResourceLocation getEntityTexture(T entity)
    {
        return DEFAULT_RES_LOC;
    }

    public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    protected int shouldRenderPass(EntityLiving entity, int par1, float par2)
    {
        return -1;
    }
}
