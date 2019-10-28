package com.flemmli97.fatemod.client.render.servant;

import com.flemmli97.fatemod.client.model.servant.ModelArthur;
import com.flemmli97.fatemod.common.entity.servant.EntityArthur;
import com.flemmli97.fatemod.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.Animation;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderArthur extends RenderServant<EntityArthur>
{
    private static final ResourceLocation textures = new ResourceLocation(LibReference.MODID, "textures/entity/servant/arthur.png");
	private static final ModelArthur model = new ModelArthur();
	//18, attack at 15
	public static Animation swing_1 = new Animation(model, new ResourceLocation(LibReference.MODID, "models/entity/animation/swing_1.json"));
    //21, attack at 17
	public static Animation excalibur = new Animation(model, new ResourceLocation(LibReference.MODID, "models/entity/animation/excalibur.json"));

	public RenderArthur(RenderManager renderManager)
    {
        super(renderManager, model, 0.5F);
    }

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation servantTexture(EntityArthur entity)
    {
        return textures;
    }

}
