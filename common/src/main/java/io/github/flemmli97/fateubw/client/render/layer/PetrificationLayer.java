package io.github.flemmli97.fateubw.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.effects.PetrificationEffect;
import io.github.flemmli97.fateubw.common.registry.FateMobEffects;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class PetrificationLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public static final ResourceLocation STONE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/block/stone.png");
    public static final ResourceLocation PETRIFICATION_0 = Fate.modRes("textures/misc/petrification_0.png");
    public static final ResourceLocation PETRIFICATION_1 = Fate.modRes("textures/misc/petrification_1.png");
    public static final ResourceLocation PETRIFICATION_2 = Fate.modRes("textures/misc/petrification_2.png");
    public static final ResourceLocation PETRIFICATION_3 = Fate.modRes("textures/misc/petrification_3.png");

    public PetrificationLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        MobEffectInstance instance = entity.getEffect(FateMobEffects.PETRIFICATION.asHolder());
        if (instance != null) {
            int amplifier = Mth.clamp(instance.getAmplifier(), 0, PetrificationEffect.MAX_PROGRESS);
            ResourceLocation mask = switch (amplifier) {
                case 0 -> PETRIFICATION_0;
                case 1 -> PETRIFICATION_1;
                case 2 -> PETRIFICATION_2;
                default -> PETRIFICATION_3;
            };
            M model = this.getParentModel();
            poseStack.pushPose();
            VertexConsumer vertexConsumer2 = buffer.getBuffer(FateRenders.entityCutoutMasked(STONE_TEXTURE, mask));
            model.renderToBuffer(poseStack, vertexConsumer2, packedLight, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }
}
