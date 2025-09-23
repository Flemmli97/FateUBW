package io.github.flemmli97.fateubw.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.effects.PetrificationEffect;
import io.github.flemmli97.fateubw.common.registry.FateMobEffects;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class PetrificationGeoLayer<T extends Entity & GeoAnimatable> extends GeoRenderLayer<T> {

    public PetrificationGeoLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource buffer, @Nullable VertexConsumer consumer, float partialTick, int packedLight, int packedOverlay) {
        if (!(animatable instanceof LivingEntity entity))
            return;
        MobEffectInstance instance = entity.getEffect(FateMobEffects.PETRIFICATION.asHolder());
        if (instance != null) {
            int amplifier = Mth.clamp(instance.getAmplifier(), 0, PetrificationEffect.MAX_PROGRESS);
            ResourceLocation mask = switch (amplifier) {
                case 0 -> PetrificationLayer.PETRIFICATION_0;
                case 1 -> PetrificationLayer.PETRIFICATION_1;
                case 2 -> PetrificationLayer.PETRIFICATION_2;
                default -> PetrificationLayer.PETRIFICATION_3;
            };
            BakedGeoModel model = this.getDefaultBakedModel(animatable);
            poseStack.pushPose();
            RenderType type = FateRenders.entityCutoutMasked(PetrificationLayer.STONE_TEXTURE, mask);
            VertexConsumer vertexConsumer2 = buffer.getBuffer(type);
            this.getRenderer().reRender(model, poseStack, buffer, animatable, type, vertexConsumer2, partialTick, packedLight, OverlayTexture.NO_OVERLAY, CommonColors.WHITE);
            poseStack.popPose();
        }
    }
}
