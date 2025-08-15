package io.github.flemmli97.fateubw.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.mixinhelper.ClientMixinUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderItem(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        ClientMixinUtils.renderCorruptedItem = itemStack.has(FateDataComponents.CORRUPTED_ITEM.get());
    }

    @Inject(method = "getCompassFoilBuffer", at = @At("HEAD"), cancellable = true)
    private static void compassFoil(MultiBufferSource buffer, RenderType renderType, PoseStack.Pose matrixEntry, CallbackInfoReturnable<VertexConsumer> info) {
        if (ClientMixinUtils.renderCorruptedItem) {
            info.setReturnValue(VertexMultiConsumer.create(buffer.getBuffer(FateRenders.CORRUPTED_OVERLAY), buffer.getBuffer(renderType)));
            ClientMixinUtils.renderCorruptedItem = false;
        }
    }

    @Inject(method = "getFoilBuffer", at = @At("HEAD"), cancellable = true)
    private static void foil(MultiBufferSource buffer, RenderType renderType, boolean isItem, boolean glint, CallbackInfoReturnable<VertexConsumer> info) {
        if (ClientMixinUtils.renderCorruptedItem) {
            info.setReturnValue(VertexMultiConsumer.create(buffer.getBuffer(FateRenders.CORRUPTED_OVERLAY), buffer.getBuffer(renderType)));
            ClientMixinUtils.renderCorruptedItem = false;
        }
    }

    @Inject(method = "getFoilBufferDirect", at = @At("HEAD"), cancellable = true)
    private static void foilDirect(MultiBufferSource buffer, RenderType renderType, boolean noEntity, boolean withGlint, CallbackInfoReturnable<VertexConsumer> info) {
        if (ClientMixinUtils.renderCorruptedItem) {
            info.setReturnValue(VertexMultiConsumer.create(buffer.getBuffer(FateRenders.CORRUPTED_OVERLAY), buffer.getBuffer(renderType)));
            ClientMixinUtils.renderCorruptedItem = false;
        }
    }

    @Inject(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getModel(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)Lnet/minecraft/client/resources/model/BakedModel;"))
    private void onStaticRender(LivingEntity entity, ItemStack itemStack, ItemDisplayContext diplayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, Level level, int combinedLight, int combinedOverlay, int seed, CallbackInfo info) {
        ClientMixinUtils.adjustForHeldModel(itemStack, diplayContext);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void adjustModel(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo info) {
        ClientMixinUtils.resetHeldModel();
    }
}
