package io.github.flemmli97.fateubw.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.blocks.SwordDisplayBlock;
import io.github.flemmli97.fateubw.common.blocks.entity.SwordDisplayEntity;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.RotationSegment;

import java.util.HashMap;
import java.util.Map;

public class RenderSwordDisplay implements BlockEntityRenderer<SwordDisplayEntity> {

    @SuppressWarnings("deprecation")
    public static final Material TEXTURE_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, Fate.modRes("block/sword_display_base"));

    public static final ResourceLocation MODEL_LOCATION = Fate.modRes("sword_display_base");

    private final Map<BakedModel, float[]> modelLength = new HashMap<>();
    private final ItemRenderer itemRenderer;

    protected final ReloadableCache<ModelPartsContainer> model;
    public ModelPartsContainer.ModelPartExtended base;

    public RenderSwordDisplay(BlockEntityRendererProvider.Context context) {
        this.model = GeoModelManager.getInstance().getModel(MODEL_LOCATION, m -> this.base = m.getPart("Base"));
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public int getViewDistance() {
        return 128;
    }

    @Override
    public void render(SwordDisplayEntity displayEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stack = displayEntity.item();
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        float rotation = RotationSegment.convertToDegrees(displayEntity.getBlockState().getValue(SwordDisplayBlock.ROTATION));
        if (!stack.isEmpty()) {
            float length = 3.5f;
            MultiBufferSource newSource = bufferSource;
            BakedModel bakedmodel = this.itemRenderer.getModel(stack, displayEntity.getLevel(), null, 0);
            if (this.modelLength.containsKey(bakedmodel)) {
                float[] v = this.modelLength.get(bakedmodel);
                length = Math.abs(v[1] - v[0]);
            } else {
                FloatConsumer consumer = y -> this.modelLength.compute(bakedmodel, (k, v) -> {
                    if (v == null) {
                        return new float[]{y, y};
                    }
                    v[0] = Math.min(v[0], y);
                    v[1] = Math.max(v[1], y);
                    return v;
                });
                newSource = type -> new WrappedConsumer(bufferSource.getBuffer(type), consumer);
            }
            float scale = 2.5f;
            poseStack.translate(0, -2.25, 0);
            poseStack.translate(0, length, 0);
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(Axis.YP.rotationDegrees(-rotation - 90));
            poseStack.mulPose(Axis.ZP.rotationDegrees(135));
            switch (displayEntity.rotation()) {
                case ROTATION_LEFT -> poseStack.mulPose(Axis.ZP.rotationDegrees(-15));
                case ROTATION_RIGHT -> poseStack.mulPose(Axis.ZP.rotationDegrees(15));
            }
            this.itemRenderer.render(stack, ItemDisplayContext.FIXED, false, poseStack, newSource, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
        } else {
            VertexConsumer vertexconsumer = TEXTURE_LOCATION.buffer(bufferSource, RenderType::entitySolid);
            poseStack.scale(-1.0f, -1.0f, 1.0f);
            this.base.yRot = rotation * Mth.DEG_TO_RAD;
            this.model.get().getRoot().render(poseStack, vertexconsumer, packedLight, packedOverlay);
        }
        poseStack.popPose();
    }

    private record WrappedConsumer(VertexConsumer wrapped, FloatConsumer onVertex) implements VertexConsumer {

        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            return this.wrapped.addVertex(x, y, z);
        }

        @Override
        public VertexConsumer setColor(int red, int green, int blue, int alpha) {
            return this.wrapped.setColor(red, green, blue, alpha);
        }

        @Override
        public VertexConsumer setUv(float u, float v) {
            return this.wrapped.setUv(u, v);
        }

        @Override
        public VertexConsumer setUv1(int u, int v) {
            return this.wrapped.setUv1(u, v);
        }

        @Override
        public VertexConsumer setUv2(int u, int v) {
            return this.wrapped.setUv2(u, v);
        }

        @Override
        public VertexConsumer setNormal(float normalX, float normalY, float normalZ) {
            return this.wrapped.setNormal(normalX, normalY, normalZ);
        }

        @Override
        public void addVertex(float x, float y, float z, int color, float u, float v, int packedOverlay, int packedLight, float normalX, float normalY, float normalZ) {
            if (normalY != 0) {
                this.onVertex.accept(y);
            }
            VertexConsumer.super.addVertex(x, y, z, color, u, v, packedOverlay, packedLight, normalX, normalY, normalZ);
        }
    }
}