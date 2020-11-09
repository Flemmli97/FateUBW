package com.flemmli97.fate.client.render;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.ModelCaladBolg;
import com.flemmli97.fate.common.entity.EntityCaladBolg;
import com.flemmli97.tenshilib.client.render.RenderProjectileModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class RenderCaladbolg extends RenderProjectileModel<EntityCaladBolg> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/caladbolg.png");

    public RenderCaladbolg(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelCaladBolg());
    }

    @Override
    public ResourceLocation getEntityTexture(EntityCaladBolg p_110775_1_) {
        return tex;
    }

    @Override
    public void translate(EntityCaladBolg entity, MatrixStack stack, float pitch, float yaw, float partialTicks) {
        super.translate(entity, stack, pitch, yaw, partialTicks);
        double speed = entity.getMotion().length();
        if(speed <1.0)
            speed=1.0;
        stack.scale(0.4f, 0.4f, (float) (speed*1.5));
    }
}
