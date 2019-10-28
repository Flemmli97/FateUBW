package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.client.model.servant.ModelHeracles;
import com.flemmli97.fatemod.common.entity.servant.EntityHeracles;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.Animation;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderHeracles extends RenderServant<EntityHeracles>
{
    private static final ResourceLocation textures = new ResourceLocation(LibReference.MODID, "textures/entity/servant/heracles.png");
    
    private static final ModelHeracles model = new ModelHeracles();
	public static Animation swing_1 = new Animation(model, new ResourceLocation(LibReference.MODID, "models/entity/animation/heracles_swing_1.json"));

    public RenderHeracles(RenderManager renderManager)
    {
        super(renderManager, model, 0.5F);
    }

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation servantTexture(EntityHeracles entity)
    {
        return textures;
    }   

}
