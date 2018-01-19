package com.flemmli97.fatemod.client.model.servant;

import org.lwjgl.opengl.GL11;

import com.flemmli97.fatemod.common.entity.servant.EntityArthur;

import net.minecraft.entity.Entity;

public class ModelArthur extends ModelServant {
	
	public ModelArthur() {
		super();
	}
	
	

	@Override
	public void render(Entity entity, float f0, float f1, float f2, float f3, float f4, float scale) {
		/*EntityArthur arthur = (EntityArthur)entity;
		int attack = arthur.getAttackTime();
		if(attack!=0)
		{
			GL11.glTranslated(0, 0.3, 0);
		}*/
		super.render(entity, f0, f1, f2, f3, f4, scale);
	}



	/*@Override
	public void setRotationAngles(float armSwingTime, float armSwingReach, float ageInPartialTick, float f4, float f5, float scaleorpartialTick, Entity entity) {		
		EntityArthur arthur = (EntityArthur)entity;
		int attack = arthur.getAttackTime();
		int special = arthur.specialAnimation;
		super.setRotationAnglesPre(armSwingTime, armSwingReach, ageInPartialTick, f4, f5, scaleorpartialTick, entity);
		if(special > 0)
		{
			this.resetModel();
			servantBody.rotateAngleX = animating2(arthur, special, 0, 50, -30, 2, 1);
			servantLeftLegUp.rotateAngleX = animating2(arthur, special, 0, -50, 30, 2, 1);
			servantRightLegUp.rotateAngleX = animating2(arthur, special, 0, -50, 30, 2, 1);
			
			servantLeftArmDown.rotateAngleZ = degToRad(23);
			servantRightArmDown.rotateAngleZ = degToRad(-45);
			servantRightArmUp.rotateAngleX = animating2(arthur, special, 0, 57, -115, 2, 1);
			servantLeftArmUp.rotateAngleX = animating2(arthur, special, 0, 33, -93, 2, 1);
			servantLeftArmUp.rotateAngleY = degToRad(23);
			servantLeftArmUp.rotateAngleZ = degToRad(-7);
		}
		else if (attack > 0)
        {
			this.resetModel();
			servantLeftLegDown.rotateAngleX = animating(arthur, attack, 0, 15, 10, 0);
			
			servantRightLegDown.rotateAngleX = animating(arthur, attack, 0, 20, 10, 0);
			
			servantRightLegUp.rotateAngleX = animating(arthur, attack, 0, -60, 10, 0);
			servantRightLegUp.rotateAngleY = animating(arthur, attack, 0, 5, 10, 0);
					
            servantRightArmUp.rotateAngleX = animating2(arthur, attack, 0, -45, -60, 10, 0);
            servantRightArmUp.rotateAngleY = animating2(arthur, attack, 0, -31.3F, 105.65F, 10, 0);
            servantRightArmUp.rotateAngleZ = animating2(arthur, attack, 0, -10.43F, 49.57F, 10, 0);
            
            servantRightArmDown.rotateAngleZ = animating2(arthur, attack, 0, -40, 0, 10, 0);
			
			servantBody.rotateAngleX = animating2(arthur, attack, 0, 30, 40, 10, 0);
			servantBody.rotateAngleY = animating2(arthur, attack, 0, -15, 5, 10, 0);
            
            syncOverlay();
        }
		else if(arthur.stay)
		{
			this.resetModel();
			servantRightArmUp.rotateAngleX = degToRad(-28.70F);
			servantLeftArmUp.rotateAngleX = degToRad(-65.0F);
			
			servantRightArmUp.rotateAngleY = degToRad(-15.0F);
			servantLeftArmUp.rotateAngleY = degToRad(35.0F);
			
			servantRightArmJoint.rotateAngleX = degToRad(28.7F);
			servantLeftArmJoint.rotateAngleX = degToRad(65.0F);
			
			servantRightArmDown.rotateAngleX= degToRad(-90.0F);
			servantLeftArmDown.rotateAngleX= degToRad(-90.0F);
			
			servantRightArmDown.rotateAngleY= degToRad(-18.26F);	
			servantLeftArmDown.rotateAngleY= degToRad(26.09F);
			
			servantLeftArmDown.rotateAngleZ = degToRad(-10.43F);
			
			servantLeftLegUp.rotateAngleX = degToRad(6.0F);
			servantLeftLegUp.rotateAngleY = degToRad(-5.0F);
			
			servantRightLegUp.rotateAngleX = degToRad(-14);
			servantRightLegDown.rotateAngleX= degToRad(14);
		}
		else if(arthur.nearbyEnemy)
		{
			this.resetModel();
			servantRightArmUp.rotateAngleX = degToRad(-28.70F);
			servantLeftArmUp.rotateAngleX = degToRad(-65.0F);
			
			servantRightArmUp.rotateAngleY = degToRad(-15.0F);
			servantLeftArmUp.rotateAngleY = degToRad(35.0F);
			
			servantRightArmJoint.rotateAngleX = degToRad(28.7F);
			servantLeftArmJoint.rotateAngleX = degToRad(65.0F);
			
			servantRightArmDown.rotateAngleX= degToRad(-90.0F);
			servantLeftArmDown.rotateAngleX= degToRad(-90.0F);
			
			servantRightArmDown.rotateAngleY= degToRad(-18.26F);	
			servantLeftArmDown.rotateAngleY= degToRad(26.09F);
			
			servantLeftArmDown.rotateAngleZ = degToRad(-10.43F);
		}
		else
		{
			resetModel();
		}
		syncOverlay();		
	}	*/
}
