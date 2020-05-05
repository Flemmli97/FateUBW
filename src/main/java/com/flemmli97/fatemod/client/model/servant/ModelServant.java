package com.flemmli97.fatemod.client.model.servant;

import org.lwjgl.opengl.GL11;

import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelServant extends ModelBase implements IResetModel{
	
    public ModelRendererPlus servantHead;
    public ModelRendererPlus servantHeadOverlay;
    public ModelRendererPlus servantBody;
    public ModelRendererPlus servantBodyOverlay;
    
    public ModelRendererPlus servantRightArmUp;
    public ModelRendererPlus servantRightArmUpOverlay;
    public ModelRendererPlus servantRightArmJoint;
    public ModelRendererPlus servantRightArmOverlayJoint;
    public ModelRendererPlus servantRightArmDown;
    public ModelRendererPlus servantRightArmDownOverlay;
    
    public ModelRendererPlus servantLeftArmUp;
    public ModelRendererPlus servantLeftArmUpOverlay;
    public ModelRendererPlus servantLeftArmJoint;
    public ModelRendererPlus servantLeftArmOverlayJoint;
    public ModelRendererPlus servantLeftArmDown;
    public ModelRendererPlus servantLeftArmDownOverlay;
    
    public ModelRendererPlus servantRightLegUp;
    public ModelRendererPlus servantRightLegUpOverlay;
    public ModelRendererPlus servantRightLegDown;
    public ModelRendererPlus servantRightLegDownOverlay;
    
    public ModelRendererPlus servantLeftLegUp;
    public ModelRendererPlus servantLeftLegUpOverlay;
    public ModelRendererPlus servantLeftLegDown;
    public ModelRendererPlus servantLeftLegDownOverlay;
    
    public int heldItemMain, heldItemOff;

	public ModelServant() {
		
		this.textureWidth = 64;
        this.textureHeight = 64;
        
        this.servantHead = new ModelRendererPlus(this, 0, 0);
        this.servantHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0);       
        this.servantHead.setDefaultRotPoint(0, 0, 0);
        this.servantHeadOverlay = new ModelRendererPlus(this, 32, 0);
        this.servantHeadOverlay.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.servantHeadOverlay.setDefaultRotPoint(0, 0, 0);
        
        this.servantBody = new ModelRendererPlus(this, 16, 16);
        this.servantBody.addBox(-4.0F, 0, -2.0F, 8, 12, 4, 0);
        this.servantBody.setDefaultRotPoint(0, 0, 0);
        this.servantBodyOverlay = new ModelRendererPlus(this, 16, 32);
		this.servantBodyOverlay.addBox(-4.0F, 0, -2.0F, 8, 12, 4, 0.25F);
		this.servantBodyOverlay.setDefaultRotPoint(0, 0, 0);
		
		this.servantLeftArmUp = new ModelRendererPlus(this, 40, 16);
		this.servantLeftArmUp.mirror=true;
		this.servantLeftArmUp.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, 0);
		this.servantLeftArmUp.setDefaultRotPoint(5.0F, 2.0F, 0);
		this.servantLeftArmUpOverlay = new ModelRendererPlus(this, 40, 32);
		this.servantLeftArmUpOverlay.mirror=true;
		this.servantLeftArmUpOverlay.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, 0.25F);
		this.servantLeftArmUpOverlay.setDefaultRotPoint(5.0F, 2.0F, 0);
		
		this.servantLeftArmJoint = new ModelRendererPlus (this, 0, 0);
		this.servantLeftArmJoint.addBox(0, 0, 0, 0, 0, 0);
		this.servantLeftArmJoint.setDefaultRotPoint(3.0F, 4.0F, 0);
		this.servantLeftArmUp.addChild(this.servantLeftArmJoint);
		this.servantLeftArmOverlayJoint = new ModelRendererPlus (this, 0, 0);
		this.servantLeftArmOverlayJoint.addBox(0, 0, 0, 0, 0, 0);
		this.servantLeftArmOverlayJoint.setDefaultRotPoint(3.0F, 4.0F, 0);
		this.servantLeftArmUpOverlay.addChild(this.servantLeftArmOverlayJoint);
		
		this.servantLeftArmDown = new ModelRendererPlus(this, 32, 54);
		this.servantLeftArmDown.mirror=true;
		this.servantLeftArmDown.addBox(-4.0F, 0, -2.0F, 4, 6, 4, 0);
		this.servantLeftArmDown.setDefaultRotPoint(0, 0, 0);
		this.servantLeftArmJoint.addChild(this.servantLeftArmDown);
		this.servantLeftArmDownOverlay = new ModelRendererPlus(this, 48, 54);
		this.servantLeftArmDownOverlay.mirror=true;
		this.servantLeftArmDownOverlay.addBox(-4.0F, 0, -2.0F, 4, 6, 4, 0.25F);
		this.servantLeftArmDownOverlay.setDefaultRotPoint(0, 0, 0);
		this.servantLeftArmOverlayJoint.addChild(this.servantLeftArmDownOverlay);
		
        this.servantRightArmUp = new ModelRendererPlus(this, 40, 16);
        this.servantRightArmUp.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, 0);
        this.servantRightArmUp.setDefaultRotPoint(-5.0F, 2.0F, 0);
        this.servantRightArmUpOverlay = new ModelRendererPlus(this, 40, 32);
        this.servantRightArmUpOverlay.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4,0.25F);
        this.servantRightArmUpOverlay.setDefaultRotPoint(-5.0F, 2.0F, 0);
        
        this.servantRightArmJoint = new ModelRendererPlus(this, 0, 0);
        this.servantRightArmJoint.addBox(0, 0, 0, 0, 0, 0);		
        this.servantRightArmJoint.setDefaultRotPoint(-3.0F, 4.0F, 0);
        this.servantRightArmUp.addChild(this.servantRightArmJoint);
        this.servantRightArmOverlayJoint = new ModelRendererPlus(this, 0, 0);
        this.servantRightArmOverlayJoint.addBox(0, 0, 0, 0, 0, 0);
        this.servantRightArmOverlayJoint.setDefaultRotPoint(-3.0F, 4.0F, 0);
        this.servantRightArmUpOverlay.addChild(this.servantRightArmOverlayJoint);
        
        this.servantRightArmDown = new ModelRendererPlus(this, 32, 54);
        this.servantRightArmDown.addBox(0, 0, -2.0F, 4, 6, 4, 0);
        this.servantRightArmDown.setDefaultRotPoint(0, 0, 0);
        this.servantRightArmJoint.addChild(this.servantRightArmDown);
        this.servantRightArmDownOverlay = new ModelRendererPlus(this, 48, 54);
        this.servantRightArmDownOverlay.addBox(0, 0, -2.0F, 4, 6, 4, 0.25F);
        this.servantRightArmDownOverlay.setDefaultRotPoint(0, 0, 0);
        this.servantRightArmOverlayJoint.addChild(this.servantRightArmDownOverlay);
        
        this.servantLeftLegUp = new ModelRendererPlus(this, 0, 16);
        this.servantLeftLegUp.mirror=true;
        this.servantLeftLegUp.addBox(-2.0F, 0, -2.0F, 4, 6, 4, 0);
        this.servantLeftLegUp.setDefaultRotPoint(1.9F, 12.0F, 0);
        this.servantLeftLegUpOverlay = new ModelRendererPlus(this, 0, 32);
        this.servantLeftLegUpOverlay.mirror=true;
        this.servantLeftLegUpOverlay.addBox(-2.0F, 0, -2.0F, 4, 6, 4, 0.25F);
        this.servantLeftLegUpOverlay.setDefaultRotPoint(1.9F, 12.0F, 0);
        
        this.servantLeftLegDown = new ModelRendererPlus(this, 16, 54);
        this.servantLeftLegDown.mirror=true;
        this.servantLeftLegDown.addBox(-2.0F, 0, 0, 4, 6, 4, 0);
        this.servantLeftLegDown.setDefaultRotPoint(0, 6.0F, -2.0F);
        this.servantLeftLegUp.addChild(this.servantLeftLegDown);
        this.servantLeftLegDownOverlay = new ModelRendererPlus(this, 0, 54);
        this.servantLeftLegDownOverlay.mirror=true;
        this.servantLeftLegDownOverlay.addBox(-2.0F, 0, 0, 4, 6, 4, 0.25F);
        this.servantLeftLegDownOverlay.setDefaultRotPoint(0, 6.0F, -2.0F);
        this.servantLeftLegUpOverlay.addChild(this.servantLeftLegDownOverlay);

        this.servantRightLegUp = new ModelRendererPlus(this, 0, 16);
        this.servantRightLegUp.addBox(-2.0F, 0, -2.0F, 4, 6, 4, 0);
        this.servantRightLegUp.setDefaultRotPoint(-1.9F, 12.0F, 0);
        this.servantRightLegUpOverlay = new ModelRendererPlus(this, 0, 32);
        this.servantRightLegUpOverlay.addBox(-2.0F, 0, -2.0F, 4, 6, 4, 0.25F);
        this.servantRightLegUpOverlay.setRotationPoint(-1.9F, 12.0F, 0);
       
        this.servantRightLegDown = new ModelRendererPlus(this, 16, 54);
        this.servantRightLegDown.addBox(-2.0F, 0, 0, 4, 6, 4, 0);
        this.servantRightLegDown.setDefaultRotPoint(0, 6.0F, -2.0F);
        this.servantRightLegUp.addChild(this.servantRightLegDown);
        this.servantRightLegDownOverlay = new ModelRendererPlus(this, 0, 54);
        this.servantRightLegDownOverlay.addBox(-2.0F, 0, 0, 4, 6, 4, 0.25F);
        this.servantRightLegDownOverlay.setDefaultRotPoint(0, 6.0F, -2.0F);
        this.servantRightLegUpOverlay.addChild(this.servantRightLegDownOverlay);
        
        this.servantBody.addChild(this.servantHead);
        this.servantBody.addChild(this.servantRightArmUp);
        this.servantBody.addChild(this.servantLeftArmUp);
        this.servantBody.addChild(this.servantRightLegUp);
        this.servantBody.addChild(this.servantLeftLegUp);
		
        this.servantBodyOverlay.addChild(this.servantHeadOverlay);
        this.servantBodyOverlay.addChild(this.servantRightArmUpOverlay);
        this.servantBodyOverlay.addChild(this.servantLeftArmUpOverlay);
        this.servantBodyOverlay.addChild(this.servantRightLegUpOverlay);
        this.servantBodyOverlay.addChild(this.servantLeftLegUpOverlay);
	}
	
	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		this.servantBody.render(scale);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.servantBodyOverlay.render(scale);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
    {
		this.setRotationAnglesPre(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.syncOverlay();
	}
	
	public void setRotationAnglesPre(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity) {
		this.resetModel();
        this.servantHead.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
        this.servantHead.rotateAngleX = headPitch / (180F / (float)Math.PI);
        this.servantHeadOverlay.rotateAngleY = this.servantHead.rotateAngleY;
        this.servantHeadOverlay.rotateAngleX = this.servantHead.rotateAngleX;
        
        this.servantRightArmUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.servantLeftArmUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.servantRightArmUp.rotateAngleZ = 0;
        this.servantLeftArmUp.rotateAngleZ = 0;
        this.servantRightLegUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.servantLeftLegUp.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.servantRightLegUp.rotateAngleY = 0;
        this.servantLeftLegUp.rotateAngleY = 0;
        
        if (this.isRiding)
        {
            this.servantRightArmUp.rotateAngleX += -((float)Math.PI / 5F);
            this.servantLeftArmUp.rotateAngleX += -((float)Math.PI / 5F);
            this.servantRightLegUp.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.servantLeftLegUp.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.servantRightLegUp.rotateAngleY = ((float)Math.PI / 10F);
            this.servantLeftLegUp.rotateAngleY = -((float)Math.PI / 10F);
        }
        
        if (this.heldItemOff == 1)
        {
            this.servantLeftArmUp.rotateAngleX = this.servantLeftArmUp.rotateAngleX * 0.5F - ((float)Math.PI / 10F) ;
        }
        if (this.heldItemMain == 1)
        {
            this.servantRightArmUp.rotateAngleX = this.servantRightArmUp.rotateAngleX * 0.5F - ((float)Math.PI / 10F) ;
        }
        
        this.servantRightArmUp.rotateAngleY = 0;
        this.servantLeftArmUp.rotateAngleY = 0;
        float var8;
        float var9;
        
        if (this.swingProgress > -9990)
        {
            var8 = this.swingProgress;
            this.servantBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(var8) * (float)Math.PI * 2.0F) * 0.2F;
            this.servantRightArmUp.rotationPointZ = MathHelper.sin(this.servantBody.rotateAngleY) * 5.0F;
            this.servantRightArmUp.rotationPointX = -MathHelper.cos(this.servantBody.rotateAngleY) * 5.0F;
            this.servantLeftArmUp.rotationPointZ = -MathHelper.sin(this.servantBody.rotateAngleY) * 5.0F;
            this.servantLeftArmUp.rotationPointX = MathHelper.cos(this.servantBody.rotateAngleY) * 5.0F;
            this.servantRightArmUp.rotateAngleY += this.servantBody.rotateAngleY;
            this.servantLeftArmUp.rotateAngleY += this.servantBody.rotateAngleY;
            this.servantLeftArmUp.rotateAngleX += this.servantBody.rotateAngleY;
            var8 = 1.0F - this.swingProgress;
            var8 *= var8;
            var8 *= var8;
            var8 = 1.0F - var8;
            var9 = MathHelper.sin(var8 * (float)Math.PI);
            float var10 = MathHelper.sin(this.swingProgress * (float)Math.PI) * -(this.servantHead.rotateAngleX - 0.7F) * 0.75F;
            this.servantRightArmUp.rotateAngleX = (float)((double)this.servantRightArmUp.rotateAngleX - ((double)var9 * 1.2D + (double)var10));
            this.servantRightArmUp.rotateAngleY += this.servantBody.rotateAngleY * 2.0F;
            this.servantRightArmUp.rotateAngleZ = MathHelper.sin(this.swingProgress * (float)Math.PI) * -0.4F;
        }
        
        this.servantBody.rotateAngleX = 0;
        this.servantRightLegUp.rotationPointZ = 0.1F;
        this.servantLeftLegUp.rotationPointZ = 0.1F;
        this.servantRightLegUp.rotationPointY = 12.0F;
        this.servantLeftLegUp.rotationPointY = 12.0F;
        this.servantHead.rotationPointY = 0;
        this.servantHeadOverlay.rotationPointY = 0;
        
        this.servantRightArmUp.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.servantLeftArmUp.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.servantRightArmUp.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.servantLeftArmUp.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
	}
	
	public void syncOverlay()
	{
		copyModelAngles(this.servantLeftLegUp, this.servantLeftLegUpOverlay);
		copyModelAngles(this.servantRightLegUp, this.servantRightLegUpOverlay);
		
		copyModelAngles(this.servantLeftArmUp, this.servantLeftArmUpOverlay);
		copyModelAngles(this.servantRightArmUp, this.servantRightArmUpOverlay);
		
		copyModelAngles(this.servantRightArmJoint, this.servantRightArmOverlayJoint);
		copyModelAngles(this.servantLeftArmJoint, this.servantLeftArmOverlayJoint);
		
		copyModelAngles(this.servantRightArmDown, this.servantRightArmDownOverlay);
		copyModelAngles(this.servantLeftArmDown, this.servantLeftArmDownOverlay);
		
		copyModelAngles(this.servantLeftLegDown, this.servantLeftLegDownOverlay);
		copyModelAngles(this.servantRightLegDown, this.servantRightLegDownOverlay);
		
		copyModelAngles(this.servantHead, this.servantHeadOverlay);
		
		copyModelAngles(this.servantBody, this.servantBodyOverlay);
	}
	
	@Override
	public void resetModel()
	{
		//Rotation point reset
        this.servantHead.reset();
        this.servantBody.reset();
        
        this.servantLeftArmUp.reset();
		this.servantLeftArmJoint.reset();
		this.servantLeftArmDown.reset();

        this.servantRightArmUp.reset();	
        this.servantRightArmJoint.reset();
        this.servantRightArmDown.reset();

        this.servantLeftLegUp.reset();
        this.servantLeftLegDown.reset();
        this.servantRightLegUp.reset();
        this.servantRightLegDown.reset();
        this.syncOverlay();
	}
	
	public void postRenderArm(float scale, EnumHandSide side)
    {
        if(side == EnumHandSide.LEFT)
        {
        	this.servantLeftArmUp.postRender(scale);
            this.servantLeftArmJoint.postRender(0);
            this.servantLeftArmDown.postRender(scale);      	
        }
        else if(side == EnumHandSide.RIGHT)
        {
        	this.servantRightArmUp.postRender(scale);
            this.servantRightArmJoint.postRender(0);
            this.servantRightArmDown.postRender(scale);
        }
    }
}
