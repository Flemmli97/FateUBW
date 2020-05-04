package com.flemmli97.fatemod.client.model.servant;

import com.flemmli97.fatemod.common.entity.servant.EntityGilles;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.TabulaAnimation;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelGilles extends ModelServant{
	
    //38, attack at 25
    public TabulaAnimation casting;

	public ModelGilles()
	{
		super();
		this.casting = new TabulaAnimation(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/gilles_cast.json"));
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Entity entity)
    {
		if(!(entity instanceof EntityGilles))
			return;
		super.setRotationAnglesPre(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		EntityGilles gilles = (EntityGilles)entity;
		if(gilles.isStaying())
		{

		}
		else
		{
			float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
			AnimatedAction anim = gilles.getAnimation();
	    	if(anim!=null)
	    	{
	    		if(anim.getID().equals("ranged"))
	    			this.casting.animate(anim.getTick(), partialTicks);
	    	}
		}
		this.syncOverlay();
    }
}
