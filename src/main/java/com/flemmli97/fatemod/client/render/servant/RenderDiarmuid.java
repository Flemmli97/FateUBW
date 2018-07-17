package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.model.servant.ModelDiarmuid;
import com.flemmli97.fatemod.common.entity.servant.EntityDiarmuid;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderDiarmuid extends RenderServant<EntityDiarmuid>
{
    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID + ":textures/entity/servant/diarmuid.png");

    public RenderDiarmuid(RenderManager renderManager)
    {
        super(renderManager, new ModelDiarmuid(), 0.5F);
    }

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation servantTexture(EntityDiarmuid entity)
    {
        return textures;
    }
}