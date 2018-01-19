package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.model.servant.ModelIskander;
import com.flemmli97.fatemod.common.entity.servant.EntityIskander;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderIskander extends RenderServant<EntityIskander>
{
    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID + ":textures/entity/servant/iskander.png");
    public RenderIskander(RenderManager renderManager)
    {
        super(renderManager, new ModelIskander(), 0.5F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityIskander entityIskander)
    {
        return textures;
    }    

}