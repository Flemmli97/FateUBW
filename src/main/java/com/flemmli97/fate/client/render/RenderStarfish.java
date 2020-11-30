package com.flemmli97.fate.client.render;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.ModelStarfishDemon;
import com.flemmli97.fate.common.entity.EntityLesserMonster;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderStarfish<T extends EntityLesserMonster> extends MobRenderer<T, ModelStarfishDemon<T>> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/starfish.png");

    public RenderStarfish(EntityRendererManager manager) {
        super(manager, new ModelStarfishDemon<>(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }
}
