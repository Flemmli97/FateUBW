package io.github.flemmli97.fateubw.forge.platform;

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
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import java.util.function.Function;

public class ClientPlatformImpl implements ClientPlatform {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> boolean renderLivingEvent(LivingEntity entity, LivingEntityRenderer<T, M> renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, boolean pre) {
        if (pre)
            return MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre<>(entity, renderer, partialTick, poseStack, multiBufferSource, packedLight));
        return MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post<>(entity, renderer, partialTick, poseStack, multiBufferSource, packedLight));
    }

    @Override
    public <T extends Entity> Component nameTagRenderEvent(T entity, Component content, EntityRenderer<?> entityRenderer, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float partialTick, Function<T, Boolean> shouldRender) {
        RenderNameplateEvent renderNameplateEvent = new RenderNameplateEvent(entity, entity.getDisplayName(), entityRenderer, poseStack, multiBufferSource, packedLight, partialTick);
        MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        return renderNameplateEvent.getResult() != Event.Result.DENY && (renderNameplateEvent.getResult() == Event.Result.ALLOW || shouldRender.apply(entity)) ? renderNameplateEvent.getContent() : null;
    }

    @Override
    public void renderModelList(ItemRenderer renderer, BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer) {
        renderer.renderModelLists(model, stack, combinedLight, combinedOverlay, poseStack, buffer);
    }
}
