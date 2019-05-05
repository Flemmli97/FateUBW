package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.client.model.ModelCaladBolg;
import com.flemmli97.fatemod.common.entity.EntityCaladBolg;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.render.RenderProjectileModel;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCaladBolg extends RenderProjectileModel<EntityCaladBolg>{

    private static final ResourceLocation tex = new ResourceLocation(LibReference.MODID, "textures/entity/caladbolg.png");

	public RenderCaladBolg(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelCaladBolg());
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCaladBolg entity) {
		return tex;
	}

	@Override
	public float yawOffset() {
		return 180;
	}
}
