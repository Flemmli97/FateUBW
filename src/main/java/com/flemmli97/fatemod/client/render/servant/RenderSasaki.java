package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.model.servant.ModelSasaki;
import com.flemmli97.fatemod.common.entity.servant.EntitySasaki;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSasaki extends RenderServant<EntitySasaki>
{
    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID + ":textures/entity/servant/sasaki.png");
    public RenderSasaki(RenderManager renderManager)
    {
        super(renderManager, new ModelSasaki(), 0.5F);
    }

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation servantTexture(EntitySasaki entitySasaki)
    {
        return textures;
    }    

}
