package io.github.flemmli97.fateubw.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.mixinhelper.ClientMixinUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderItemPre(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        ClientMixinUtils.currentItemRenderContext = itemStack;
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void onRenderItemPost(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        ClientMixinUtils.currentItemRenderContext = null;
    }

    @Inject(method = "getCompassFoilBuffer", at = @At("HEAD"), cancellable = true)
    private static void compassFoil(MultiBufferSource buffer, RenderType renderType, PoseStack.Pose matrixEntry, CallbackInfoReturnable<VertexConsumer> info) {
        VertexConsumer cons = ClientMixinUtils.getConsumerOnContext(buffer, renderType);
        if (cons != null) {
            info.setReturnValue(cons);
        }
    }

    @Inject(method = "getFoilBuffer", at = @At("HEAD"), cancellable = true)
    private static void foil(MultiBufferSource buffer, RenderType renderType, boolean isItem, boolean glint, CallbackInfoReturnable<VertexConsumer> info) {
        VertexConsumer cons = ClientMixinUtils.getConsumerOnContext(buffer, renderType);
        if (cons != null) {
            info.setReturnValue(cons);
        }
    }

    @Inject(method = "getFoilBufferDirect", at = @At("HEAD"), cancellable = true)
    private static void foilDirect(MultiBufferSource buffer, RenderType renderType, boolean noEntity, boolean withGlint, CallbackInfoReturnable<VertexConsumer> info) {
        VertexConsumer cons = ClientMixinUtils.getConsumerOnContext(buffer, renderType);
        if (cons != null) {
            info.setReturnValue(cons);
        }
    }
}
