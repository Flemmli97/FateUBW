package com.flemmli97.fate.client.render;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.client.model.ModelCaladBolg;
import com.flemmli97.fate.common.entity.EntityCaladBolg;
import com.flemmli97.tenshilib.client.render.RenderProjectileModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class RenderCaladbolg extends RenderProjectileModel<EntityCaladBolg> {

    public final ResourceLocation tex = new ResourceLocation(Fate.MODID, "textures/entity/caladbolg.png");

    public RenderCaladbolg(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelCaladBolg());
    }

    @Override
    public ResourceLocation getEntityTexture(EntityCaladBolg entity) {
        return tex;
    }

    @Override
    public void translate(EntityCaladBolg entity, MatrixStack stack, float pitch, float yaw, float partialTicks) {
        super.translate(entity, stack, pitch, yaw, partialTicks);
        double speed = entity.getMotion().length();
        if(speed <1.0)
            speed=1.0;
        stack.scale(0.35f, 0.35f, (float) (speed*1.3));
        stack.translate(0,-0.8,1f);
    }
}
