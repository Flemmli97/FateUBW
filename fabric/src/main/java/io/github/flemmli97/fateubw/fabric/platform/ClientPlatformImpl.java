package io.github.flemmli97.fateubw.fabric.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.fabric.mixin.ItemRendererAccessor;
import io.github.flemmli97.fateubw.platform.ClientPlatform;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class ClientPlatformImpl implements ClientPlatform {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> boolean renderLivingEvent(LivingEntity entity, LivingEntityRenderer<T, M> renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, boolean pre) {
        return false;
    }

    @Override
    public <T extends Entity> Component nameTagRenderEvent(T entity, Component content, EntityRenderer<?> entityRenderer, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float partialTick, Function<T, Boolean> shouldRender) {
        return shouldRender.apply(entity) ? entity.getDisplayName() : null;
    }

    @Override
    public void renderModelList(ItemRenderer renderer, BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer) {
        ((ItemRendererAccessor) renderer).doRenderModelLists(model, stack, combinedLight, combinedOverlay, poseStack, buffer);
    }
}
