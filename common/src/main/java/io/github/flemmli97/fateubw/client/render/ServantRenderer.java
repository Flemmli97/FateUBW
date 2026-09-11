package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.client.model.ServantModel;
import io.github.flemmli97.fateubw.client.render.layer.ItemTrailLayer;
import io.github.flemmli97.fateubw.client.render.layer.ServantItemRender;
import io.github.flemmli97.fateubw.client.render.layer.TrailPoseGetter;
import io.github.flemmli97.fateubw.client.render.vertex.ClippingVertexConsumerWrapper;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.tenshilib.client.render.WrappedBufferSource;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ServantRenderer<T extends BaseServant, M extends ServantModel<T>> extends LivingEntityRenderer<T, ServantModel<T>> implements TrailPoseGetter {

    private final ResourceLocation texture;

    /**
     * This one does not have transformations such as camera rotations etc. applied
     */
    private PoseStack modelStack;

    public ServantRenderer(EntityRendererProvider.Context ctx, M model, ResourceLocation texture, float shadow) {
        super(ctx, model, shadow);
        this.texture = texture;
        this.addLayer(new ServantItemRender<>(this, ctx.getItemInHandRenderer()));
        this.addLayer(new CustomHeadLayer<>(this, ctx.getModelSet(), ctx.getItemInHandRenderer()));
        this.addLayer(new ItemTrailLayer<>(this));
    }

    @Override
    public void render(T entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light) {
        this.model.update(entity);
        DoublePoseStack recording = new DoublePoseStack(poseStack);
        this.modelStack = recording.getApplied();
        float summonProgress = (float) entity.getSummonProgress(partialTick);
        Vector4f clip;
        if (summonProgress >= 0 && summonProgress < 1) {
            Vector3f normal = new Vector3f(0, 0, 1);
            normal.rotate(Axis.XP.rotationDegrees(-90));
            clip = FateRenders.createClippingPlane(normal, entity, -(entity.getBbHeight() + 0.3f) * (1 - summonProgress));
        } else
            clip = null;
        MultiBufferSource buf = clip == null ? buffer : new WrappedBufferSource(buffer, FateRenders::getClippedRendertype,
                c -> ClippingVertexConsumerWrapper.wrap(c, clip, entity.summonColor(), 0.1f));
        super.render(entity, yaw, partialTick, recording, buf, light);
    }

    @Override
    protected boolean shouldShowName(T entity) {
        return super.shouldShowName(entity) && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    protected float getFlipDegrees(T livingEntity) {
        return 0;
    }

    @Override
    public ResourceLocation getTextureLocation(T servant) {
        return this.texture;
    }

    @Override
    public PoseStack getModelStack() {
        return this.modelStack;
    }
}
