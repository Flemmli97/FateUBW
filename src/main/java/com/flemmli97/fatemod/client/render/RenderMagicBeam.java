package com.flemmli97.fatemod.client.render;

import org.apache.commons.lang3.tuple.Pair;

import com.flemmli97.fatemod.common.entity.EntityMagicBeam;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderBeam;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMagicBeam extends RenderBeam<EntityMagicBeam>{

    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/magic_beam.png");

	public RenderMagicBeam(RenderManager renderManagerIn) {
		super(renderManagerIn, 0.3f);
	}
	
	@Override
    public void doRender(EntityMagicBeam projectile, double x, double y, double z, float entityYaw, float partialTick) {
        if(projectile.iddle)
        {
            /*GlStateManager.pushMatrix();
            double ripple = Math.sin((projectile.ticksExisted+projectile.renderRand)/2f)*0.025+1;
            float size = (float) (1.6*ripple);
            RenderUtils.renderTexture(this.renderManager, babylonIddle, x, y, z, size, size, RenderUtils.defaultColor,
                    projectile.prevRotationYaw + (projectile.rotationYaw - projectile.prevRotationYaw) * partialTick+180, 
                    projectile.prevRotationPitch + (projectile.rotationPitch - projectile.prevRotationPitch) * partialTick+5);
            GlStateManager.popMatrix();*/
        }
        else
            super.doRender(projectile, x, y, z, entityYaw, partialTick);
    }
	
	@Override
	protected ResourceLocation getEntityTexture(EntityMagicBeam entity) {
		return tex;
	}

	@Override
	public Pair<ResourceLocation, Float> startTexture(EntityMagicBeam entity) {
		return null;
	}

	@Override
	public Pair<ResourceLocation, Float> endTexture(EntityMagicBeam entity) {
		return null;
	}
}
