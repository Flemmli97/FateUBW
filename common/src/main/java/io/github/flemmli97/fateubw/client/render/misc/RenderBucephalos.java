package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.BucephalosModel;
import io.github.flemmli97.fateubw.common.entity.summons.Bucephalos;
import io.github.flemmli97.tenshilib.client.render.layer.RiderEntityLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderBucephalos<T extends Bucephalos> extends MobRenderer<T, BucephalosModel<T>> {

    public static final ResourceLocation TEX = Fate.modRes("textures/entity/bucephalos.png");

    public RenderBucephalos(EntityRendererProvider.Context ctx) {
        super(ctx, new BucephalosModel<>(), 1);
        this.layers.add(new RiderEntityLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Bucephalos bucephalos) {
        return TEX;
    }
}
