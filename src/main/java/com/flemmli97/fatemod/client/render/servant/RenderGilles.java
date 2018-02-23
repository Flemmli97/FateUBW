package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.model.servant.ModelGilles;
import com.flemmli97.fatemod.common.entity.servant.EntityGilles;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderGilles extends RenderServant<EntityGilles>
{
    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID + ":textures/entity/servant/gilles.png");

    public RenderGilles(RenderManager renderManager)
    {
        super(renderManager, new ModelGilles(), 0.5F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityGilles entity)
    {
        return textures;
    }
}