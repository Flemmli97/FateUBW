package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.client.model.servant.ModelServant;
import com.flemmli97.fatemod.client.render.layer.LayerHand;
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
    
    public RenderServant(RenderManager renderManagerIn, ModelServant modelServant, float shadowSize)
    {
        super(renderManagerIn, modelServant, shadowSize);
        this.modelServantMain = modelServant;
        this.addLayer(new LayerCustomHead(modelServant.servantHead));
        this.addLayer(new LayerHand(this));
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
    	if(!showIdentity(servant))
    		this.mainModel=this.defaultModel;
    	else
    		this.mainModel=this.modelServantMain;
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity)
	{
		return showIdentity(entity)?this.servantTexture(entity):DEFAULT_RES_LOC;
    }
	
	public static boolean showIdentity(EntityServant servant)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		boolean owner = player!=null?player.getCapability(PlayerCapProvider.PlayerCap, null).getServant()==servant:false;
		return (servant.showServant() || owner || servant.getDeathTick()>0);
	}
	
	protected abstract ResourceLocation servantTexture(T entity);
}
