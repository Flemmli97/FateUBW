package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.client.model.servant.ModelGilles;
import com.flemmli97.fatemod.common.entity.servant.EntityGilles;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.Animation;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderGilles extends RenderServant<EntityGilles>
{
    private static final ResourceLocation textures = new ResourceLocation(LibReference.MODID, "textures/entity/servant/gilles.png");
    private final static ModelGilles model = new ModelGilles();
    //38, attack at 25
	public static Animation casting = new Animation(model, new ResourceLocation(LibReference.MODID, "models/entity/animation/gilles_cast.json"));

    public RenderGilles(RenderManager renderManager)
    {
        super(renderManager, model, 0.5F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation servantTexture(EntityGilles entity)
    {
        return textures;
    }
}