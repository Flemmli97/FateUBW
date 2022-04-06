package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.ArcherArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderArcherArrow extends ArrowRenderer<ArcherArrow> {

    public final ResourceLocation LOCATION = new ResourceLocation(Fate.MODID, "textures/entity/arrows.png");

    public RenderArcherArrow(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(ArcherArrow arrow) {
        return this.LOCATION;
    }
}
