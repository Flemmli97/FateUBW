package io.github.flemmli97.fateubw.client.render.misc;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.GordiusWheelModel;
import io.github.flemmli97.fateubw.common.entity.summons.GordiusWheel;
import io.github.flemmli97.tenshilib.client.render.layer.RiderEntityLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderGordius extends MobRenderer<GordiusWheel, GordiusWheelModel> {

    public static final ResourceLocation TEX = Fate.modRes("textures/entity/gordius_wheel.png");

    public RenderGordius(EntityRendererProvider.Context ctx) {
        super(ctx, new GordiusWheelModel(), 0.5f);
        this.layers.add(new RiderEntityLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(GordiusWheel gordiusWheel) {
        return TEX;
    }
}
