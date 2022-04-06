package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelStarfishDemon;
import io.github.flemmli97.fateubw.common.entity.minions.LesserMonster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderStarfish<T extends LesserMonster> extends MobRenderer<T, ModelStarfishDemon<T>> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/starfish.png");

    public RenderStarfish(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelStarfishDemon<>(ctx.bakeLayer(ModelStarfishDemon.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.tex;
    }
}
