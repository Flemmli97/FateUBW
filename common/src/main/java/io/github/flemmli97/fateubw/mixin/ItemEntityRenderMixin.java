package io.github.flemmli97.fateubw.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.client.render.GrailItemRender;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRenderMixin {

    @Inject(method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemEntityRenderer;renderMultipleFromCount(Lnet/minecraft/client/renderer/entity/ItemRenderer;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/resources/model/BakedModel;ZLnet/minecraft/util/RandomSource;)V"))
    private void onRender(ItemEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo info) {
        GrailItemRender.renderBeams(entity, poseStack, buffer, partialTicks);
    }

}
