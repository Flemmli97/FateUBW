package io.github.flemmli97.fateubw.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.entity.servant.EntityLancelot;
import io.github.flemmli97.fateubw.mixinhelper.ClientMixinUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderItem(ItemStack itemStack, ItemTransforms.TransformType transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo info) {
        ClientMixinUtils.renderCorruptedItem = itemStack.hasTag() && itemStack.getTag().getBoolean(EntityLancelot.CORRUPTED_ITEM);
    }

    @Inject(method = "getCompassFoilBuffer", at = @At("HEAD"), cancellable = true)
    private static void compassFoil(MultiBufferSource buffer, RenderType renderType, PoseStack.Pose matrixEntry, CallbackInfoReturnable<VertexConsumer> info) {
        if (ClientMixinUtils.renderCorruptedItem) {
            info.setReturnValue(VertexMultiConsumer.create(buffer.getBuffer(FateRenders.CORRUPTED_OVERLAY), buffer.getBuffer(renderType)));
            ClientMixinUtils.renderCorruptedItem = false;
        }
    }

    @Inject(method = "getCompassFoilBufferDirect", at = @At("HEAD"), cancellable = true)
    private static void compassDirect(MultiBufferSource buffer, RenderType renderType, PoseStack.Pose matrixEntry, CallbackInfoReturnable<VertexConsumer> info) {
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

    @Inject(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getModel(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)Lnet/minecraft/client/resources/model/BakedModel;"))
    private void onStaticRender(@Nullable LivingEntity livingEntity, ItemStack itemStack, ItemTransforms.TransformType transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, @Nullable Level level, int combinedLight, int combinedOverlay, int i, CallbackInfo info) {
        ClientMixinUtils.adjustForHeldModel(itemStack, transformType);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void adjustModel(ItemStack itemStack, ItemTransforms.TransformType transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo info) {
        ClientMixinUtils.resetHeldModel();
    }
}
