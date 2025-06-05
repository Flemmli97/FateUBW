package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RenderEmpty<T extends Entity> extends EntityRenderer<T> {

    public RenderEmpty(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public boolean shouldRender(T entity, Frustum helper, double camX, double camY, double camZ) {
        return false;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return null;
    }
}
