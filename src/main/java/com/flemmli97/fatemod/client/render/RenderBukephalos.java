package com.flemmli97.fatemod.client.render;

import com.flemmli97.fatemod.common.lib.LibReference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderBukephalos<T extends EntityLiving> extends RenderLiving<T>
{
	private static final ResourceLocation textures = new ResourceLocation(LibReference.MODID, "textures/entity/bukephalos.png");

    protected ModelBase model;
    
    public RenderBukephalos(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelHorse(), 0);
    }

    protected int shouldRenderPass(EntityLiving entity, int par1, float par2)
    {
        return -1;
    }
    
    protected ResourceLocation getEntityTexture(EntityLiving entity)
    {
        return textures;
    }

}
