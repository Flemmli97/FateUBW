package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.model.servant.ModelEmiya;
import com.flemmli97.fatemod.common.entity.servant.EntityEmiya;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEmiya extends RenderServant<EntityEmiya> {

	private static final ResourceLocation textures = new ResourceLocation(Fate.MODID + ":textures/entity/servant/emiya.png");
    public RenderEmiya(RenderManager renderManager)
    {
        super(renderManager, new ModelEmiya(), 0.5F);
    }

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityEmiya entity)
    {
        return textures;
    }

}
