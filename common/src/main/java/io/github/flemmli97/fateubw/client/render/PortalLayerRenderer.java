package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class PortalLayerRenderer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final Predicate<T> shouldRender;
    private final Consumer<PoseStack> transform;
    private final ResourceLocation texture;
    private final float size;

    public PortalLayerRenderer(RenderLayerParent<T, M> renderer, Predicate<T> shouldRender, Consumer<PoseStack> transform, ResourceLocation texture, float size) {
        super(renderer);
        this.shouldRender = shouldRender;
        this.transform = transform;
        this.texture = texture;
        this.size = size;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.shouldRender.test(livingEntity)) {
            poseStack.pushPose();
            this.transform.accept(poseStack);
            Matrix4f mat = poseStack.last().pose();
            VertexConsumer vert = buffer.getBuffer(FateRenderTypes.getPulsingEntityText(this.texture));
            vert.vertex(mat, this.size, this.size, 0).color(255, 255, 255, 255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            vert.vertex(mat, this.size, -this.size, 0).color(255, 255, 255, 255).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            vert.vertex(mat, -this.size, -this.size, 0).color(255, 255, 255, 255).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            vert.vertex(mat, -this.size, this.size, 0).color(255, 255, 255, 255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();

            vert.vertex(mat, -this.size, this.size, 0).color(255, 255, 255, 255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            vert.vertex(mat, -this.size, -this.size, 0).color(255, 255, 255, 255).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            vert.vertex(mat, this.size, -this.size, 0).color(255, 255, 255, 255).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();
            vert.vertex(mat, this.size, this.size, 0).color(255, 255, 255, 255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xff00ff).normal(1, 0, 0).endVertex();

            poseStack.popPose();
        }
    }
}
