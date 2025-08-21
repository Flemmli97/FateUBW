package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.client.model.ServantModel;
import io.github.flemmli97.fateubw.client.render.layer.ItemTrailLayer;
import io.github.flemmli97.fateubw.client.render.layer.TrailPoseGetter;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.tenshilib.client.render.layer.ItemLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.concurrent.atomic.AtomicInteger;

public class ServantRenderer<T extends BaseServant, M extends ServantModel<T>> extends LivingEntityRenderer<T, ServantModel<T>> implements TrailPoseGetter {

    private static final MultiBufferSource.BufferSource SEP = MultiBufferSource.immediate(new ByteBufferBuilder(1536));

    private final ResourceLocation texture;

    /**
     * This one does not have transformations such as camera rotations etc. applied
     */
    private PoseStack plainPose;

    public ServantRenderer(EntityRendererProvider.Context ctx, M model, ResourceLocation texture, float shadow) {
        super(ctx, model, shadow);
        this.texture = texture;
        this.addLayer(new ItemLayer<>(this, ctx.getItemInHandRenderer()));
        this.addLayer(new CustomHeadLayer<>(this, ctx.getModelSet(), ctx.getItemInHandRenderer()));
        this.addLayer(new ItemTrailLayer<>(this));
    }

    @Override
    public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        this.model.update(entity);
        DoublePoseStack recording = new DoublePoseStack(poseStack);
        this.plainPose = recording.getApplied();
        float summonProgress = (float) entity.getSummonProgress(partialTicks);
        Vector4f clip;
        if (summonProgress >= 0 && summonProgress < 1) {
            Vector3f normal = new Vector3f(0, 0, 1);
            normal.rotate(Axis.XP.rotationDegrees(-90));
            clip = FateRenders.createClippingPlane(normal, entity, -(entity.getBbHeight() + 0.3f) * (1 - summonProgress));
        } else
            clip = null;
        AtomicInteger state = new AtomicInteger();
        MultiBufferSource buf = clip != null ? renderType -> {
            RenderType type = FateRenders.getClippedRendertype(renderType, clip, entity.summonColor(), 0.1f);
            int current = state.get();
            VertexConsumer cons = current == 1 ? SEP.getBuffer(type) : buffer.getBuffer(type);
            if (current == 0 || current == 2)
                state.set(1);
            else
                state.set(2);
            return cons;
        } : buffer;
        super.render(entity, yaw, partialTicks, recording, buf, light);
        if (state.get() != 0) // other buffersource was used
            SEP.endBatch();
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
    public PoseStack getPlainStack() {
        return this.plainPose;
    }
}
