package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.ModelCaladBolg;
import io.github.flemmli97.fateubw.common.entity.misc.CaladBolg;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderCaladbolg extends RenderProjectileModel<CaladBolg> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/caladbolg.png");

    public RenderCaladbolg(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelCaladBolg(ctx.bakeLayer(ModelCaladBolg.LAYER_LOCATION)));
    }

    @Override
    public ResourceLocation getTextureLocation(CaladBolg entity) {
        return this.tex;
    }

    @Override
    public void translate(CaladBolg entity, PoseStack stack, float pitch, float yaw, float partialTicks) {
        super.translate(entity, stack, pitch, yaw, partialTicks);
        double speed = entity.getDeltaMovement().length();
        if (speed < 1.0)
            speed = 1.0;
        stack.scale(0.35f, 0.35f, (float) (speed * 1.3));
        stack.translate(0, -0.8, 1f);
    }
}
