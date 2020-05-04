package com.flemmli97.fatemod.client.model.servant;

import com.flemmli97.fatemod.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.TabulaAnimation;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelCuchulainn extends ModelServant{
	
    public TabulaAnimation gae_bolg;

	public ModelCuchulainn() {
		super();
		this.gae_bolg = new TabulaAnimation(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/gaebolg.json"));
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
    {
		if(!(entity instanceof EntityCuchulainn))
			return;
		super.setRotationAnglesPre(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

		EntityCuchulainn cuchulainn = (EntityCuchulainn)entity;
		
		if(cuchulainn.isStaying())
		{
			
		}
		else
		{
			float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
			AnimatedAction anim = cuchulainn.getAnimation();
	    	if(anim!=null)
	    	{
	    		if(anim.getID().equals("gae_bolg"))
	    			this.gae_bolg.animate(anim.getTick(), partialTicks);
	    	}
		}
		this.syncOverlay();	
	}
}
