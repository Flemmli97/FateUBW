package com.flemmli97.fatemod.client.render.servant;

import org.lwjgl.opengl.GL11;

import com.flemmli97.fatemod.client.model.servant.ModelServant;
import com.flemmli97.fatemod.client.render.layer.LayerHand;
import com.flemmli97.fatemod.common.entity.servant.EntityServant;
import com.flemmli97.fatemod.common.handler.capabilities.PlayerCapProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
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
    protected void renderModel(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        boolean flag = this.isVisible(entity);
        boolean flag1 = !flag && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().player);
        boolean flag2 = !flag1 && entity.isDead();
        if (flag || flag1)
        {
            if (!this.bindEntityTexture(entity))
            {
                return;
            }

            if (flag1)
            {
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            else if(flag2)
            {
            	GlStateManager.pushMatrix();
                GlStateManager.enableBlend();

                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, 
                		GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.alphaFunc(GL11.GL_GEQUAL, 1/255f);               
            	GlStateManager.color(1.0F, 1.0F, 1.0F, Math.max(0.1f, 1-(entity.getDeathTick()/(float)entity.maxDeathTick())));
            }

            this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

            if (flag1)
            {
                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            else if(flag2)
            {
            	GlStateManager.disableBlend();
                GlStateManager.alphaFunc(GL11.GL_GEQUAL, 0.1F);
                GlStateManager.popMatrix();
            }
        }
    }

	@Override
	protected void preRenderCallback(T servant, float partialTickTime) {
    	ItemStack stackMain = servant.getHeldItemMainhand();
    	ItemStack stackOff = servant.getHeldItemOffhand();
		modelServantMain.heldItemMain = 0;
		modelServantMain.heldItemOff = 0;
		this.defaultModel.heldItemMain=1;
		this.defaultModel.heldItemOff=0;
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
		//Else tabula throws so many errors...
		try
		{
			EntityServant playerServant = Minecraft.getMinecraft().player.getCapability(PlayerCapProvider.PlayerCap, null).getServant(Minecraft.getMinecraft().player);
			boolean owner = playerServant!=null && playerServant.equals(servant);
			return (servant.showServant() || owner || servant.getDeathTick()>0);
		}
		catch(Exception e)
		{
			return true;
		}
	}
	
	protected abstract ResourceLocation servantTexture(T entity);
}
