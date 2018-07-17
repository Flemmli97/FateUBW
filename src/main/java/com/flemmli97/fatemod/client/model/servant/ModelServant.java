package com.flemmli97.fatemod.client.model.servant;

import org.lwjgl.opengl.GL11;

import com.flemmli97.fatemod.common.entity.servant.EntityServant;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelServant extends ModelBase{
	
    public ModelRenderer servantHead;
    public ModelRenderer servantHeadOverlay;
    public ModelRenderer servantBody;
    public ModelRenderer servantBodyOverlay;
    
    public ModelRenderer servantRightArmUp;
    public ModelRenderer servantRightArmUpOverlay;
    public ModelRenderer servantRightArmJoint;
    public ModelRenderer servantRightArmOverlayJoint;
    public ModelRenderer servantRightArmDown;
    public ModelRenderer servantRightArmDownOverlay;
    
    public ModelRenderer servantLeftArmUp;
    public ModelRenderer servantLeftArmUpOverlay;
    public ModelRenderer servantLeftArmJoint;
    public ModelRenderer servantLeftArmOverlayJoint;
    public ModelRenderer servantLeftArmDown;
    public ModelRenderer servantLeftArmDownOverlay;
    
    public ModelRenderer servantRightLegUp;
    public ModelRenderer servantRightLegUpOverlay;
    public ModelRenderer servantRightLegDown;
    public ModelRenderer servantRightLegDownOverlay;
    
    public ModelRenderer servantLeftLegUp;
    public ModelRenderer servantLeftLegUpOverlay;
    public ModelRenderer servantLeftLegDown;
    public ModelRenderer servantLeftLegDownOverlay;
    
    private float partialTicks;
    public int heldItemMain, heldItemOff;

    //ModelRenderer(Model, TextureOffsetX, TextureOffsetY)
    // .addBox(positionX, positionY, positionZ, sizeX, sizeY, sizeZ, scalefactor)
    // .setRotationPoint(pointX, pointY, pointZ)
	public ModelServant() {
		
		this.textureWidth = 64;
        this.textureHeight = 64;
        
        this.servantHead = new ModelRenderer(this, 0, 0);
        this.servantHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);       
        this.servantHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.servantHeadOverlay = new ModelRenderer(this, 32, 0);
        this.servantHeadOverlay.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
        this.servantHeadOverlay.setRotationPoint(0.0F, 0.0F, 0.0F);
        
        this.servantBody = new ModelRenderer(this, 16, 16);
        this.servantBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.servantBody.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.servantBodyOverlay = new ModelRenderer(this, 16, 32);
		this.servantBodyOverlay.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F);
		this.servantBodyOverlay.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		this.servantLeftArmUp = new ModelRenderer(this, 40, 16);
		this.servantLeftArmUp.mirror=true;
		this.servantLeftArmUp.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, 0.0F);
		this.servantLeftArmUp.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.servantLeftArmUpOverlay = new ModelRenderer(this, 40, 32);
		this.servantLeftArmUpOverlay.mirror=true;
		this.servantLeftArmUpOverlay.addBox(-1.0F, -2.0F, -2.0F, 4, 6, 4, 0.25F);
		this.servantLeftArmUpOverlay.setRotationPoint(5.0F, 2.0F, 0.0F);
		
		this.servantLeftArmJoint = new ModelRenderer (this, 0, 0);
		this.servantLeftArmJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0);
		this.servantLeftArmJoint.setRotationPoint(3.0F, 4.0F, 0.0F);
		this.servantLeftArmUp.addChild(servantLeftArmJoint);
		this.servantLeftArmOverlayJoint = new ModelRenderer (this, 0, 0);
		this.servantLeftArmOverlayJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0);
		this.servantLeftArmOverlayJoint.setRotationPoint(3.0F, 4.0F, 0.0F);
		this.servantLeftArmUpOverlay.addChild(servantLeftArmOverlayJoint);
		
		this.servantLeftArmDown = new ModelRenderer(this, 32, 54);
		this.servantLeftArmDown.mirror=true;
		this.servantLeftArmDown.addBox(-4.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.servantLeftArmDown.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.servantLeftArmJoint.addChild(servantLeftArmDown);
		this.servantLeftArmDownOverlay = new ModelRenderer(this, 48, 54);
		this.servantLeftArmDownOverlay.mirror=true;
		this.servantLeftArmDownOverlay.addBox(-4.0F, 0.0F, -2.0F, 4, 6, 4, 0.25F);
		this.servantLeftArmDownOverlay.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.servantLeftArmOverlayJoint.addChild(servantLeftArmDownOverlay);
		
        this.servantRightArmUp = new ModelRenderer(this, 40, 16);
        this.servantRightArmUp.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4, 0.0F);
        this.servantRightArmUp.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.servantRightArmUpOverlay = new ModelRenderer(this, 40, 32);
        this.servantRightArmUpOverlay.addBox(-3.0F, -2.0F, -2.0F, 4, 6, 4,0.25F);
        this.servantRightArmUpOverlay.setRotationPoint(-5.0F, 2.0F, 0.0F);
        
        this.servantRightArmJoint = new ModelRenderer(this, 0, 0);
        this.servantRightArmJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0);		
        this.servantRightArmJoint.setRotationPoint(-3.0F, 4.0F, 0.0F);
        this.servantRightArmUp.addChild(servantRightArmJoint);
        this.servantRightArmOverlayJoint = new ModelRenderer(this, 0, 0);
        this.servantRightArmOverlayJoint.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0);
        this.servantRightArmOverlayJoint.setRotationPoint(-3.0F, 4.0F, 0.0F);
        this.servantRightArmUpOverlay.addChild(servantRightArmOverlayJoint);
        
        this.servantRightArmDown = new ModelRenderer(this, 32, 54);
        this.servantRightArmDown.addBox(0.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.servantRightArmDown.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.servantRightArmJoint.addChild(servantRightArmDown);
        this.servantRightArmDownOverlay = new ModelRenderer(this, 48, 54);
        this.servantRightArmDownOverlay.addBox(0.0F, 0.0F, -2.0F, 4, 6, 4, 0.25F);
        this.servantRightArmDownOverlay.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.servantRightArmOverlayJoint.addChild(servantRightArmDownOverlay);
        
        this.servantLeftLegUp = new ModelRenderer(this, 0, 16);
        this.servantLeftLegUp.mirror=true;
        this.servantLeftLegUp.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.servantLeftLegUp.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.servantLeftLegUpOverlay = new ModelRenderer(this, 0, 32);
        this.servantLeftLegUpOverlay.mirror=true;
        this.servantLeftLegUpOverlay.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.25F);
        this.servantLeftLegUpOverlay.setRotationPoint(1.9F, 12.0F, 0.0F);
        
        this.servantLeftLegDown = new ModelRenderer(this, 16, 54);
        this.servantLeftLegDown.mirror=true;
        this.servantLeftLegDown.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, 0.0F);
        this.servantLeftLegDown.setRotationPoint(0.0F, 6.0F, -2.0F);
        this.servantLeftLegUp.addChild(servantLeftLegDown);
        this.servantLeftLegDownOverlay = new ModelRenderer(this, 0, 54);
        this.servantLeftLegDownOverlay.mirror=true;
        this.servantLeftLegDownOverlay.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, 0.25F);
        this.servantLeftLegDownOverlay.setRotationPoint(0.0F, 6.0F, -2.0F);
        this.servantLeftLegUpOverlay.addChild(servantLeftLegDownOverlay);

        this.servantRightLegUp = new ModelRenderer(this, 0, 16);
        this.servantRightLegUp.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
        this.servantRightLegUp.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.servantRightLegUpOverlay = new ModelRenderer(this, 0, 32);
        this.servantRightLegUpOverlay.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.25F);
        this.servantRightLegUpOverlay.setRotationPoint(-1.9F, 12.0F, 0.0F);
       
        this.servantRightLegDown = new ModelRenderer(this, 16, 54);
        this.servantRightLegDown.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, 0.0F);
        this.servantRightLegDown.setRotationPoint(0.0F, 6.0F, -2.0F);
        this.servantRightLegUp.addChild(servantRightLegDown);
        this.servantRightLegDownOverlay = new ModelRenderer(this, 0, 54);
        this.servantRightLegDownOverlay.addBox(-2.0F, 0.0F, 0.0F, 4, 6, 4, 0.25F);
        this.servantRightLegDownOverlay.setRotationPoint(0.0F, 6.0F, -2.0F);
        this.servantRightLegUpOverlay.addChild(servantRightLegDownOverlay);
        
        this.servantBody.addChild(servantHead);
        this.servantBody.addChild(servantRightArmUp);
        this.servantBody.addChild(servantLeftArmUp);
        this.servantBody.addChild(servantRightLegUp);
        this.servantBody.addChild(servantLeftLegUp);
		
        this.servantBodyOverlay.addChild(servantHeadOverlay);
        this.servantBodyOverlay.addChild(servantRightArmUpOverlay);
        this.servantBodyOverlay.addChild(servantLeftArmUpOverlay);
        this.servantBodyOverlay.addChild(servantRightLegUpOverlay);
        this.servantBodyOverlay.addChild(servantLeftLegUpOverlay);
	}
	
	public void render(Entity entity, float f0, float f1, float f2, float f3, float f4, float scale)
	{
		this.setRotationAngles(f0, f1, f2, f3, f4, scale, entity);
		servantBody.render(scale);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		servantBodyOverlay.render(scale);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6, Entity entity) {
        this.setRotationAnglesPre(f1, f2, f3, f4, f5, f6, entity);
        	
        syncOverlay();
	}
	
	public void setRotationAnglesPre(float armSwingTime, float armSwingReach, float partialTick, float f4, float f5, float scale, Entity entity) {

        this.servantHead.rotateAngleY = f4 / (180F / (float)Math.PI);
        this.servantHead.rotateAngleX = f5 / (180F / (float)Math.PI);
        this.servantHeadOverlay.rotateAngleY = this.servantHead.rotateAngleY;
        this.servantHeadOverlay.rotateAngleX = this.servantHead.rotateAngleX;
        
        this.servantRightArmUp.rotateAngleX = MathHelper.cos(armSwingTime * 0.6662F + (float)Math.PI) * 2.0F * armSwingReach * 0.5F;
        this.servantLeftArmUp.rotateAngleX = MathHelper.cos(armSwingTime * 0.6662F) * 2.0F * armSwingReach * 0.5F;
        this.servantRightArmUp.rotateAngleZ = 0.0F;
        this.servantLeftArmUp.rotateAngleZ = 0.0F;
        this.servantRightLegUp.rotateAngleX = MathHelper.cos(armSwingTime * 0.6662F) * 1.4F * armSwingReach;
        this.servantLeftLegUp.rotateAngleX = MathHelper.cos(armSwingTime * 0.6662F + (float)Math.PI) * 1.4F * armSwingReach;
        this.servantRightLegUp.rotateAngleY = 0.0F;
        this.servantLeftLegUp.rotateAngleY = 0.0F;
        
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
        
        this.servantRightArmUp.rotateAngleY = 0.0F;
        this.servantLeftArmUp.rotateAngleY = 0.0F;
        float var8;
        float var9;
        
        if (this.swingProgress > -9990.0F)
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
        
        this.servantBody.rotateAngleX = 0.0F;
        this.servantRightLegUp.rotationPointZ = 0.1F;
        this.servantLeftLegUp.rotationPointZ = 0.1F;
        this.servantRightLegUp.rotationPointY = 12.0F;
        this.servantLeftLegUp.rotationPointY = 12.0F;
        this.servantHead.rotationPointY = 0.0F;
        this.servantHeadOverlay.rotationPointY = 0.0F;
        
        this.servantRightArmUp.rotateAngleZ += MathHelper.cos(partialTick * 0.09F) * 0.05F + 0.05F;
        this.servantLeftArmUp.rotateAngleZ -= MathHelper.cos(partialTick * 0.09F) * 0.05F + 0.05F;
        this.servantRightArmUp.rotateAngleX += MathHelper.sin(partialTick * 0.067F) * 0.05F;
        this.servantLeftArmUp.rotateAngleX -= MathHelper.sin(partialTick * 0.067F) * 0.05F;
	}
	
	public void syncOverlay()
	{
		copyModelAngles(servantLeftLegUp, servantLeftLegUpOverlay);
		copyModelAngles(servantRightLegUp, servantRightLegUpOverlay);
		
		copyModelAngles(servantLeftArmUp, servantLeftArmUpOverlay);
		copyModelAngles(servantRightArmUp, servantRightArmUpOverlay);
		
		copyModelAngles(servantRightArmJoint, servantRightArmOverlayJoint);
		copyModelAngles(servantLeftArmJoint, servantLeftArmOverlayJoint);
		
		copyModelAngles(servantRightArmDown, servantRightArmDownOverlay);
		copyModelAngles(servantLeftArmDown, servantLeftArmDownOverlay);
		
		copyModelAngles(servantLeftLegDown, servantLeftLegDownOverlay);
		copyModelAngles(servantRightLegDown, servantRightLegDownOverlay);
		
		copyModelAngles(servantHead, servantHeadOverlay);
		
		copyModelAngles(servantBody, servantBodyOverlay);
	}
	
	public float degToRad(float degree)
	{
		float radiant = degree/ (180F / (float)Math.PI);
		return radiant;
	}
	
	/**endTick for early stop, should be smaller than animationTicker; last par is type of attack: 0=normal, 1=special*/
	protected float animating(EntityServant servant, float animationTicker, float startDegree, float endDegree, float endTick, int type)
	{
		int x=0;
		if(type==0)
		{
			//x=servant.attackTimer();
		}
		else
		{
			//x=servant.specialAnimationValue;
		}
		if(animationTicker >=endTick){
			float m = (startDegree - endDegree)/x;
			float rotationLastTick = degToRad(endDegree + m*(animationTicker+1));
			float rotationNow = degToRad(endDegree + m*(animationTicker));
			float actualRotation = rotationLastTick + (rotationNow - rotationLastTick) * (this.partialTicks);
			return actualRotation;
		}
		else
		{
			float m = (startDegree - endDegree)/x;
			return degToRad(endDegree + m*(endTick));
		}
	}
	
	/** */
	protected float animating2(EntityServant servant, float animationTicker, float startDegree, float midDegree, float endDegree, float changeTick, int type)
	{
		float rotationLastTick;
		float rotationNow;
		float actualRotation;
		int x = 0;
		if(type==0)
		{
			//x=servant.attackTimerValue;
		}
		else
		{
			//x=servant.specialAnimationValue;
		}
		if(animationTicker >= changeTick)
		{
			float m = (startDegree - midDegree)/(x-changeTick);
			float b = midDegree-(m*changeTick);
			rotationLastTick = degToRad(b + m*(animationTicker+1));
			rotationNow = degToRad(b + m*(animationTicker));
			actualRotation = rotationLastTick + (rotationNow - rotationLastTick) * (this.partialTicks);
			return actualRotation;
		}
		else
		{
			float m = (midDegree - endDegree)/changeTick;			
			rotationLastTick = degToRad(endDegree + m*(animationTicker+1));
			rotationNow = degToRad(endDegree + m*(animationTicker));
			actualRotation = rotationLastTick + (rotationNow - rotationLastTick) * (this.partialTicks);
			return actualRotation;
		}
	}
	
	public void resetModel()
	{
		
		servantRightArmJoint.rotateAngleX = degToRad(0.0F);
		servantLeftArmJoint.rotateAngleX = degToRad(0.0F);
		servantRightArmJoint.rotateAngleY = degToRad(0.0F);
		servantLeftArmJoint.rotateAngleY = degToRad(0.0F);
		servantRightArmJoint.rotateAngleZ = degToRad(0.0F);
		servantLeftArmJoint.rotateAngleZ = degToRad(0.0F);
		
		servantRightArmDown.rotateAngleX= degToRad(0);
		servantLeftArmDown.rotateAngleX= degToRad(0);		
		servantRightArmDown.rotateAngleY= degToRad(0);	
		servantLeftArmDown.rotateAngleY= degToRad(0);
		servantRightArmDown.rotateAngleZ = degToRad(0);
		servantLeftArmDown.rotateAngleZ = degToRad(0);
		
		servantRightLegDown.rotateAngleX= degToRad(0);
		servantLeftLegDown.rotateAngleX= degToRad(0);
		servantRightLegDown.rotateAngleY= degToRad(0);
		servantLeftLegDown.rotateAngleY= degToRad(0);
		servantRightLegDown.rotateAngleZ= degToRad(0);
		servantLeftLegDown.rotateAngleZ= degToRad(0);
		
		syncOverlay();
	}
	
	@Override
	public void setLivingAnimations(EntityLivingBase livingBase, float armSwingTime, float armSwingReach, float partialTick) {
		this.partialTicks = partialTick;
		super.setLivingAnimations(livingBase, armSwingTime, armSwingReach, partialTick);
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
