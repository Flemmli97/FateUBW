package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.Fate;
import com.flemmli97.fatemod.client.model.servant.ModelLancelot;
import com.flemmli97.fatemod.common.entity.servant.EntityLancelot;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderLancelot extends RenderServant<EntityLancelot>
{
    private static final ResourceLocation textures = new ResourceLocation(Fate.MODID + ":textures/entity/servant/lancelot.png");
    public RenderLancelot(RenderManager renderManager)
    {
        super(renderManager, new ModelLancelot(), 0.5F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityLancelot entityLancelot)
    {
        return textures;
    }

}