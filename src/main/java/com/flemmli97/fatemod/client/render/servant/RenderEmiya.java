package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.client.model.servant.ModelEmiya;
import com.flemmli97.fatemod.common.entity.servant.EntityEmiya;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEmiya extends RenderServant<EntityEmiya> {

	private static final ResourceLocation textures = new ResourceLocation(LibReference.MODID, "textures/entity/servant/emiya.png");
    public RenderEmiya(RenderManager renderManager)
    {
        super(renderManager, new ModelEmiya(), 0.5F);
    }

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation servantTexture(EntityEmiya entity)
    {
        return textures;
    }

}
