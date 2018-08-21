package com.flemmli97.fatemod.client.model.servant;

import com.flemmli97.fatemod.client.model.ModelUtils;
import com.flemmli97.fatemod.common.entity.servant.EntityArthur;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.State;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class ModelArthur extends ModelServant {
	
	public ModelArthur() {
		super();
	}
	
	@Override
	public void render(Entity entity, float f0, float f1, float f2, float f3, float f4, float scale) {
		EntityArthur arthur = (EntityArthur)entity;
		State state = arthur.entityState();
		//int attack = arthur.attackTimer();
		switch(state)
		{
		case ATTACK1:
			break;
		case ATTACK2:
			break;
		case ATTACK3:
			break;
		case NP:
			break;
		default:
			break;
		}

		super.render(entity, f0, f1, f2, f3, f4, scale);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
    {
		super.setRotationAnglesPre(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

		EntityArthur arthur = (EntityArthur)entity;
		State state = arthur.entityState();
		//Reverse the ticker so it counts up
		int attack = arthur.attackTickerFromState(state).getLeft()-arthur.attackTimer();
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		switch(state)
		{
			case ATTACK1:
				this.attack1(attack, partialTicks);
				break;
			case ATTACK2:
				this.attack1(attack, partialTicks);
				break;
			case ATTACK3:
				this.attack1(attack, partialTicks);
				break;
			case NP:
				break;
			case STAY:
				servantRightArmUp.rotateAngleX = ModelUtils.degToRad(-28.70F);
				servantLeftArmUp.rotateAngleX = ModelUtils.degToRad(-65.0F);
				
				servantRightArmUp.rotateAngleY = ModelUtils.degToRad(-15.0F);
				servantLeftArmUp.rotateAngleY = ModelUtils.degToRad(35.0F);
				
				servantRightArmJoint.rotateAngleX = ModelUtils.degToRad(28.7F);
				servantLeftArmJoint.rotateAngleX = ModelUtils.degToRad(65.0F);
				
				servantRightArmDown.rotateAngleX= ModelUtils.degToRad(-90.0F);
				servantLeftArmDown.rotateAngleX= ModelUtils.degToRad(-90.0F);
				
				servantRightArmDown.rotateAngleY= ModelUtils.degToRad(-18.26F);	
				servantLeftArmDown.rotateAngleY= ModelUtils.degToRad(26.09F);
				
				servantLeftArmDown.rotateAngleZ = ModelUtils.degToRad(-10.43F);
				
				servantLeftLegUp.rotateAngleX = ModelUtils.degToRad(6.0F);
				servantLeftLegUp.rotateAngleY = ModelUtils.degToRad(-5.0F);
				
				servantRightLegUp.rotateAngleX = ModelUtils.degToRad(-14);
				servantRightLegDown.rotateAngleX= ModelUtils.degToRad(14);
				break;
		}
		syncOverlay();		
	}	
	
	private void attack1(int attack, float partialTicks)
	{
		int dur1=5;
		int point1 = 5;
		int dur2=7;
		int point2 = 12;
		int dur3=3;
		
		this.servantRightArmUp.rotateAngleX = ModelUtils.animAngle(this.servantRightArmUp.rotateAngleX, 
				attack, this.servantRightArmUp.rotateAngleX, -55, 0, dur1, partialTicks, true);
		this.servantRightArmUp.rotateAngleX = ModelUtils.stayAtAngle(this.servantRightArmUp.rotateAngleX,
				attack, -55, point1, dur2, true);
		this.servantRightArmUp.rotateAngleX = ModelUtils.animAngle(this.servantRightArmUp.rotateAngleX, 
				attack, -55, this.servantRightArmUp.rotateAngleX, point2, dur3, partialTicks, true);
		this.servantRightArmUp.rotateAngleY = ModelUtils.animAngle(this.servantRightArmUp.rotateAngleY, 
				attack, this.servantRightArmUp.rotateAngleY, -40, 0, dur1, partialTicks, true);
		this.servantRightArmUp.rotateAngleY = ModelUtils.animAngle(this.servantRightArmUp.rotateAngleY, 
				attack, -40, 70, point1, dur2, partialTicks, true);
		this.servantRightArmUp.rotateAngleY = ModelUtils.animAngle(this.servantRightArmUp.rotateAngleY, 
				attack, 70, this.servantRightArmUp.rotateAngleY, point2, dur3, partialTicks, true);

		this.servantRightArmDown.rotateAngleX = ModelUtils.animAngle(this.servantRightArmDown.rotateAngleX, 
				attack, this.servantRightArmDown.rotateAngleX, 15, 0, dur1, partialTicks, true);
		this.servantRightArmDown.rotateAngleX = ModelUtils.animAngle(this.servantRightArmDown.rotateAngleX, 
				attack, 15, 0, point1, dur2, partialTicks, true);
		this.servantRightArmDown.rotateAngleZ = ModelUtils.animAngle(this.servantRightArmDown.rotateAngleZ, 
				attack, this.servantRightArmDown.rotateAngleZ, -40, 0, dur1, partialTicks, true);
		this.servantRightArmDown.rotateAngleZ = ModelUtils.animAngle(this.servantRightArmDown.rotateAngleZ, 
				attack, -40, 0, point1, dur2, partialTicks, true);
		
		this.servantBody.rotateAngleX = ModelUtils.animAngle(this.servantBody.rotateAngleX, 
				attack, this.servantBody.rotateAngleX, 10, 0, dur1, partialTicks, true);
		this.servantBody.rotateAngleX = ModelUtils.stayAtAngle(this.servantBody.rotateAngleX, 
				attack, 10, point1, dur2, true);
		this.servantBody.rotateAngleX = ModelUtils.animAngle(this.servantBody.rotateAngleX, 
				attack, 10, this.servantBody.rotateAngleX, point2, dur3, partialTicks, true);
		this.servantBody.rotateAngleY = ModelUtils.animAngle(this.servantBody.rotateAngleY, 
				attack, this.servantBody.rotateAngleZ, -5, 0, dur1, partialTicks, true);
		this.servantBody.rotateAngleY = ModelUtils.animAngle(this.servantBody.rotateAngleY, 
				attack, -5, 15, point1, dur2, partialTicks, true);
		this.servantBody.rotateAngleY = ModelUtils.animAngle(this.servantBody.rotateAngleY, 
				attack, 15, this.servantBody.rotateAngleZ, point2, dur3, partialTicks, true);
		
		this.servantRightLegUp.rotateAngleX = ModelUtils.animAngle(this.servantRightLegUp.rotateAngleX, 
				attack, this.servantRightLegUp.rotateAngleX, -30, 0, dur1, partialTicks, true);
		this.servantRightLegUp.rotateAngleX = ModelUtils.stayAtAngle(this.servantRightLegUp.rotateAngleX, 
				attack, -30, point1, dur2, true);
		this.servantRightLegUp.rotateAngleX = ModelUtils.animAngle(this.servantRightLegUp.rotateAngleX, 
				attack, -30, this.servantRightLegUp.rotateAngleX, point2, dur3, partialTicks, true);
		
		this.servantRightLegDown.rotateAngleX = ModelUtils.animAngle(this.servantRightLegDown.rotateAngleX, 
				attack, this.servantRightLegDown.rotateAngleX, 20, 0, dur1, partialTicks, true);
		this.servantRightLegDown.rotateAngleX = ModelUtils.stayAtAngle(this.servantRightLegDown.rotateAngleX, 
				attack, 20, point1, dur2, true);
		this.servantRightLegDown.rotateAngleX = ModelUtils.animAngle(this.servantRightLegDown.rotateAngleX, 
				attack, 20, this.servantRightLegDown.rotateAngleX, point2, dur3, partialTicks, true);
		
		this.servantLeftLegUp.rotateAngleX = ModelUtils.animAngle(this.servantLeftLegUp.rotateAngleX, 
				attack, this.servantLeftLegUp.rotateAngleX, 5, 0, dur1, partialTicks, true);
		this.servantLeftLegUp.rotateAngleX = ModelUtils.stayAtAngle(this.servantLeftLegUp.rotateAngleX, 
				attack, 5, point1, dur2, true);
		this.servantLeftLegUp.rotateAngleX = ModelUtils.animAngle(this.servantLeftLegUp.rotateAngleX, 
				attack, 5, this.servantLeftLegUp.rotateAngleX, point2, dur3, partialTicks, true);
		
		this.servantLeftLegDown.rotateAngleX = ModelUtils.animAngle(this.servantLeftLegDown.rotateAngleX, 
				attack, this.servantLeftLegDown.rotateAngleX, 15, 0, dur1, partialTicks, true);
		this.servantLeftLegDown.rotateAngleX = ModelUtils.stayAtAngle(this.servantLeftLegDown.rotateAngleX, 
				attack, 15, point1, dur2, true);
		this.servantLeftLegDown.rotateAngleX = ModelUtils.animAngle(this.servantLeftLegDown.rotateAngleX, 
				attack, 15, this.servantLeftLegUp.rotateAngleX, point2, dur3, partialTicks, true);
	}
}
