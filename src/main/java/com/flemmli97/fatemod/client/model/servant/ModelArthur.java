package com.flemmli97.fatemod.client.model.servant;

import com.flemmli97.fatemod.common.entity.servant.EntityArthur;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.TabulaAnimation;
import com.flemmli97.tenshilib.client.model.ModelUtils;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelArthur extends ModelServant {
	
    //18, attack at 15
    public TabulaAnimation swing_1;
    //21, attack at 17
    public TabulaAnimation excalibur;

    
	public ModelArthur() 
	{
		super();
		this.swing_1 = new TabulaAnimation(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/swing_1.json"));
	    this.excalibur = new TabulaAnimation(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/excalibur.json"));
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
            this.servantRightArmUp.rotateAngleX = ModelUtils.degToRad(-28.70F);
            this.servantLeftArmUp.rotateAngleX = ModelUtils.degToRad(-65.0F);

            this.servantRightArmUp.rotateAngleY = ModelUtils.degToRad(-15.0F);
            this.servantLeftArmUp.rotateAngleY = ModelUtils.degToRad(35.0F);

            this.servantRightArmJoint.rotateAngleX = ModelUtils.degToRad(28.7F);
            this.servantLeftArmJoint.rotateAngleX = ModelUtils.degToRad(65.0F);

            this.servantRightArmDown.rotateAngleX= ModelUtils.degToRad(-90.0F);
            this.servantLeftArmDown.rotateAngleX= ModelUtils.degToRad(-90.0F);

            this.servantRightArmDown.rotateAngleY= ModelUtils.degToRad(-18.26F);
            this.servantLeftArmDown.rotateAngleY= ModelUtils.degToRad(26.09F);

            this.servantLeftArmDown.rotateAngleZ = ModelUtils.degToRad(-10.43F);

            this.servantLeftLegUp.rotateAngleX = ModelUtils.degToRad(6.0F);
            this.servantLeftLegUp.rotateAngleY = ModelUtils.degToRad(-5.0F);

            this.servantRightLegUp.rotateAngleX = ModelUtils.degToRad(-14);
            this.servantRightLegDown.rotateAngleX= ModelUtils.degToRad(14);
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
