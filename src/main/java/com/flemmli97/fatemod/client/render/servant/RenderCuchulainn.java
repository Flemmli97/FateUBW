package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.client.model.servant.ModelCuchulainn;
import com.flemmli97.fatemod.common.entity.servant.EntityCuchulainn;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.Animation;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCuchulainn extends RenderServant<EntityCuchulainn>
{
    private static final ResourceLocation textures = new ResourceLocation(LibReference.MODID, "textures/entity/servant/cuchulainn.png");
   
    private static final ModelCuchulainn model = new ModelCuchulainn();
	public static Animation gae_bolg = new Animation(model, new ResourceLocation(LibReference.MODID, "models/entity/animation/gaebolg.json"));

    public RenderCuchulainn(RenderManager renderManager)
    {
        super(renderManager, model, 0.5F);
    }

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation servantTexture(EntityCuchulainn entity)
    {
        return textures;
    }
    
}
