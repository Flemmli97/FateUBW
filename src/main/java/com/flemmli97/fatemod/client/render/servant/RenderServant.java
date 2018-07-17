package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.client.model.servant.ModelServant;
import com.flemmli97.fatemod.client.render.layer.LayerHand;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

public abstract class RenderServant<T extends EntityServant> extends RenderLiving<T>{
	
	
    private static final ResourceLocation DEFAULT_RES_LOC = new ResourceLocation("textures/entity/steve.png");
    protected ModelServant modelServantMain;
    protected ModelServant defaultModel = new ModelServant();
    protected float scale;

    public RenderServant(RenderManager renderManagerIn, ModelServant modelServant, float shadowSize)
    {
        super(renderManagerIn, modelServant, shadowSize);
        this.modelServantMain = modelServant;
        this.addLayer(new LayerCustomHead(modelServant.servantHead));
        this.addLayer(new LayerHand(this));
    }
    
    @Override
	protected void preRenderCallback(T servant, float partialTickTime) {
    	ItemStack stackMain = servant.stackFromHand(EnumHand.MAIN_HAND);
    	ItemStack stackOff = servant.stackFromHand(EnumHand.OFF_HAND);
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
    	if(!servant.showServant())
    		this.mainModel=this.defaultModel;
		super.preRenderCallback(servant, partialTickTime);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	@Override
	protected ResourceLocation getEntityTexture(T entity)
    {
        return entity.showServant()?this.servantTexture(entity):DEFAULT_RES_LOC;
    }
	
	protected abstract ResourceLocation servantTexture(T entity);
	
	@Override
    public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }
}
