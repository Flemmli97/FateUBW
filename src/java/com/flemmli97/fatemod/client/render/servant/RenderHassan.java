package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.model.servant.ModelHassan;
import com.flemmli97.fatemod.common.entity.servant.EntityHassan;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderHassan extends RenderServant<EntityHassan>
{
    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID + ":textures/entity/servant/hassan.png");
    public RenderHassan(RenderManager renderManager)
    {
        super(renderManager, new ModelHassan(), 0.5F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityHassan entity)
    {
        return textures;
    }    

}