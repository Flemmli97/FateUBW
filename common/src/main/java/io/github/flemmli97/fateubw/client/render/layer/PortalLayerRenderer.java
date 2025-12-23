package io.github.flemmli97.fateubw.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class PortalLayerRenderer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final Predicate<T> shouldRender;
    private final Consumer<PoseStack> transform;
    private final ResourceLocation texture;
    private final float size;
    private int r = 255, g = 255, b = 255, a = 255;

    public PortalLayerRenderer(RenderLayerParent<T, M> renderer, Predicate<T> shouldRender, Consumer<PoseStack> transform, ResourceLocation texture, float size) {
        super(renderer);
        this.shouldRender = shouldRender;
        this.transform = transform;
        this.texture = texture;
        this.size = size;
    }

    public PortalLayerRenderer<T, M> color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.shouldRender.test(livingEntity)) {
            poseStack.pushPose();
            this.transform.accept(poseStack);
            Matrix4f mat = poseStack.last().pose();
            VertexConsumer vert = buffer.getBuffer(FateRenders.getFullBrightText(this.texture));
            vert.addVertex(mat, this.size, this.size, 0).setColor(this.r, this.g, this.b, this.a).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xff00ff).setNormal(1, 0, 0);
            vert.addVertex(mat, this.size, -this.size, 0).setColor(this.r, this.g, this.b, this.a).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xff00ff).setNormal(1, 0, 0);
            vert.addVertex(mat, -this.size, -this.size, 0).setColor(this.r, this.g, this.b, this.a).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xff00ff).setNormal(1, 0, 0);
            vert.addVertex(mat, -this.size, this.size, 0).setColor(this.r, this.g, this.b, this.a).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xff00ff).setNormal(1, 0, 0);

            vert.addVertex(mat, -this.size, this.size, 0).setColor(this.r, this.g, this.b, this.a).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xff00ff).setNormal(1, 0, 0);
            vert.addVertex(mat, -this.size, -this.size, 0).setColor(this.r, this.g, this.b, this.a).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xff00ff).setNormal(1, 0, 0);
            vert.addVertex(mat, this.size, -this.size, 0).setColor(this.r, this.g, this.b, this.a).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xff00ff).setNormal(1, 0, 0);
            vert.addVertex(mat, this.size, this.size, 0).setColor(this.r, this.g, this.b, this.a).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xff00ff).setNormal(1, 0, 0);

            poseStack.popPose();
        }
    }
}
