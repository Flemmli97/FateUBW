package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.model.servant.ModelArthur;
import com.flemmli97.fatemod.common.entity.servant.EntityArthur;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderArthur extends RenderServant<EntityArthur>
{
    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID + ":textures/entity/servant/arthur.png");
    public RenderArthur(RenderManager renderManager)
    {
        super(renderManager, new ModelArthur(), 0.5F);
    }

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation servantTexture(EntityArthur entity)
    {
        return textures;
    }

}
