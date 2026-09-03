package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.model.SpearModel;
import io.github.flemmli97.fateubw.common.entity.misc.SpearProjectile;
import io.github.flemmli97.tenshilib.client.render.SimpleModelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderSpearProjectile extends SimpleModelRenderer<SpearProjectile> {

    public static final ResourceLocation TEX = Fate.modRes("textures/entity/spear_projectile.png");

    public RenderSpearProjectile(EntityRendererProvider.Context ctx) {
        super(ctx, new SpearModel());
    }

    @Override
    public void translate(SpearProjectile entity, PoseStack stack, float pitch, float yaw, float partialTick) {
        stack.mulPose(Axis.YP.rotationDegrees(180 + yaw));
        stack.mulPose(Axis.XP.rotationDegrees(pitch));
        stack.translate(0.0, entity.getBbHeight() * 0.5, 26 / 16f);
    }

    @Override
    public ResourceLocation getTextureLocation(SpearProjectile entity) {
        return TEX;
    }
}
