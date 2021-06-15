package io.github.flemmli97.fate.client.render;

import com.flemmli97.tenshilib.client.render.RenderProjectileModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.model.ModelCaladBolg;
import io.github.flemmli97.fate.common.entity.EntityCaladBolg;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderCaladbolg extends RenderProjectileModel<EntityCaladBolg> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/caladbolg.png");

    public RenderCaladbolg(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelCaladBolg());
    }

    @Override
    public ResourceLocation getEntityTexture(EntityCaladBolg entity) {
        return this.tex;
    }

    @Override
    public void translate(EntityCaladBolg entity, MatrixStack stack, float pitch, float yaw, float partialTicks) {
        super.translate(entity, stack, pitch, yaw, partialTicks);
        double speed = entity.getMotion().length();
        if (speed < 1.0)
            speed = 1.0;
        stack.scale(0.35f, 0.35f, (float) (speed * 1.3));
        stack.translate(0, -0.8, 1f);
    }
}
