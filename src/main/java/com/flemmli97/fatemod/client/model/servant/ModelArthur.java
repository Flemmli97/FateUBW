package com.flemmli97.fatemod.client.model.servant;

import com.flemmli97.fatemod.common.entity.servant.EntityArthur;
import com.flemmli97.fatemod.common.entity.servant.EntityServant.State;
import com.flemmli97.tenshilib.client.model.ModelUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class ModelArthur extends ModelServant {
	
	public ModelArthur() {
		super();
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
    {
		if(!(entity instanceof EntityArthur))
			return;
		super.setRotationAnglesPre(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

		EntityArthur arthur = (EntityArthur)entity;
		State state = arthur.entityState();
		
		int attack = arthur.attackTimer();
		float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
		switch(state)
		{
			case ATTACK1:
				//RenderArthur.hit1.animate(attack, partialTicks);
				break;
			case ATTACK2:
				//RenderArthur.hit1.animate(attack, partialTicks);
				break;
			case ATTACK3:
				//RenderArthur.hit1.animate(attack, partialTicks);
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
}
