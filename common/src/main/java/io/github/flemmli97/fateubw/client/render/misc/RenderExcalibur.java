package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.Excalibur;
import io.github.flemmli97.tenshilib.client.render.RenderBeam;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderExcalibur extends RenderBeam<Excalibur> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/excalibur.png");

    public RenderExcalibur(EntityRendererProvider.Context ctx) {
        super(ctx, Excalibur.radius - 0.2f, 6);
    }

    @Override
    public ResourceLocation getTextureLocation(Excalibur entity) {
        return this.tex;
    }

    @Override
    public ResourcePair startTexture(Excalibur entity) {
        return null;
    }

    @Override
    public ResourcePair endTexture(Excalibur entity) {
        return null;
    }
}
