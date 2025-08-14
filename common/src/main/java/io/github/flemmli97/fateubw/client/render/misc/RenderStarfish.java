package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.StarfishModel;
import io.github.flemmli97.fateubw.common.entity.summons.LesserMonster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderStarfish<T extends LesserMonster> extends MobRenderer<T, StarfishModel<T>> {

    public static final ResourceLocation TEX = Fate.modRes("textures/entity/starfish.png");

    public RenderStarfish(EntityRendererProvider.Context ctx) {
        super(ctx, new StarfishModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEX;
    }
}
