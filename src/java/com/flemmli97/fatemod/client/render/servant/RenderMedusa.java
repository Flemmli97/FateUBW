package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.model.servant.ModelMedusa;
import com.flemmli97.fatemod.common.entity.servant.EntityMedusa;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMedusa extends RenderServant<EntityMedusa>
{
    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID + ":textures/entity/servant/medusa.png");
    public RenderMedusa(RenderManager renderManager)
    {
        super(renderManager, new ModelMedusa(), 0.5F);
    }

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityMedusa entity)
    {
        return textures;
    }
    
}
