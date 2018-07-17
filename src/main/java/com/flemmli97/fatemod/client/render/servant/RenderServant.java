package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.client.model.servant.ModelServant;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
        //this.addLayer(layer)
        //this.addLayer(new LayerHand(this));
        //this.addLayer(new LayerHeldItem(this));

    }
    
    @Override
	protected void preRenderCallback(T servant, float partialTickTime) {
    	ItemStack stackMain = servant.getHeldItemMainhand();
    	ItemStack stackOff = servant.getHeldItemOffhand();
		modelServantMain.heldItemMain = 0;
		modelServantMain.heldItemOff = 0;
    	if(!stackMain.isEmpty())
    	{
    		modelServantMain.heldItemMain = 1;
    	}
    	if(!stackOff.isEmpty())
    	{
    		modelServantMain.heldItemOff = 1;
    	}
    	if(!servant.showServant())
    		this.mainModel=this.defaultModel;
		super.preRenderCallback(servant, partialTickTime);
	}

	/*@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}*/
	@Override
	protected ResourceLocation getEntityTexture(T entity)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		boolean owner = player.hasCapability(PlayerCapProvider.PlayerCap, null)?player.getCapability(PlayerCapProvider.PlayerCap, null).getServant()==entity:false;
        return (entity.showServant() || owner || entity.getDeathTick()>0)?this.servantTexture(entity):DEFAULT_RES_LOC;
    }
	
	protected abstract ResourceLocation servantTexture(T entity);
}
