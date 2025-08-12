package io.github.flemmli97.fateubw.neoforge.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.function.Predicate;

public class ClientPlatformImpl implements ClientPlatform {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> boolean renderLivingEvent(LivingEntity entity, LivingEntityRenderer<T, M> renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, boolean pre) {
        if (pre)
            return NeoForge.EVENT_BUS.post(new RenderLivingEvent.Pre<>(entity, renderer, partialTick, poseStack, multiBufferSource, packedLight)).isCanceled();
        NeoForge.EVENT_BUS.post(new RenderLivingEvent.Post<>(entity, renderer, partialTick, poseStack, multiBufferSource, packedLight));
        return false;
    }

    @Override
    public <T extends Entity> Component nameTagRenderEvent(T entity, Component content, EntityRenderer<?> entityRenderer, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick, Predicate<T> shouldRender) {
        RenderNameTagEvent event = new RenderNameTagEvent(entity, entity.getDisplayName(), entityRenderer, poseStack, bufferSource, packedLight, partialTick);
        NeoForge.EVENT_BUS.post(event);
        return event.canRender().isTrue() || event.canRender().isDefault() && shouldRender.test(entity) ? event.getContent() : null;
    }

    @Override
    public void renderModelList(ItemRenderer renderer, BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer) {
        renderer.renderModelLists(model, stack, combinedLight, combinedOverlay, poseStack, buffer);
    }
}
