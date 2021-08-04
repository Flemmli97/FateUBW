package io.github.flemmli97.fate.client.render.misc;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelStarfishDemon;
import io.github.flemmli97.fate.common.entity.minions.EntityLesserMonster;
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
