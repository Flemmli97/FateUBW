package com.flemmli97.fatemod.client.render.servant;


import com.flemmli97.fatemod.client.model.servant.ModelMedea;
import com.flemmli97.fatemod.common.entity.servant.EntityMedea;
import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMedea extends RenderBiped<EntityMedea>
{
    private static final ResourceLocation textures = new ResourceLocation(LibReference.MODID, "textures/entity/servant/medea.png");
    public RenderMedea(RenderManager renderManager)
    {
        super(renderManager, new ModelMedea(), 0.5F);
    }

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityMedea entity)
    {
        return textures;
    }
    
}
