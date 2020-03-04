package com.flemmli97.fatemod.client.model.servant;

import com.flemmli97.fatemod.common.entity.servant.EntityArthur;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.Animation;
import com.flemmli97.tenshilib.client.model.ModelUtils;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelArthur extends ModelServant {
	
    //18, attack at 15
    public Animation swing_1;
    //21, attack at 17
    public Animation excalibur;

    
	public ModelArthur() 
	{
		super();
		this.swing_1 = new Animation(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/swing_1.json"));
	    this.excalibur = new Animation(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/excalibur.json"));
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
    {
		if(!(entity instanceof EntityArthur))
			return;
		super.setRotationAnglesPre(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

		EntityArthur arthur = (EntityArthur)entity;
		
		if(arthur.isStaying())
		{
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
		}
		else
		{
			float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
			AnimatedAction anim = arthur.getAnimation();
	    	if(anim!=null)
	    	{
	    		if(anim.getID().equals("swing_1"))
	    			this.swing_1.animate(anim.getTick(), partialTicks);
	    		if(anim.getID().equals("excalibur"))
	    			this.excalibur.animate(anim.getTick(), partialTicks);
	    	}
		}
		this.syncOverlay();		
	}	
}
