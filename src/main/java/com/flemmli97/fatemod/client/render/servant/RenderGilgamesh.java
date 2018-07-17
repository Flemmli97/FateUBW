package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.model.servant.ModelGilgamesh;
import com.flemmli97.fatemod.common.entity.servant.EntityGilgamesh;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderGilgamesh extends RenderServant<EntityGilgamesh>
{
    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID + ":textures/entity/servant/gilgamesh.png");

    public RenderGilgamesh(RenderManager manager)
    {
        super(manager, new ModelGilgamesh(), 0.5F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation servantTexture(EntityGilgamesh entity)
    {
        return textures;
    }

}
