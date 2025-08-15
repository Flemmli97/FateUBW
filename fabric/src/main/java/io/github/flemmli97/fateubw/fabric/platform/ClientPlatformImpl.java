package io.github.flemmli97.fateubw.fabric.platform;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.fateubw.fabric.mixin.ItemRendererAccessor;
import io.github.flemmli97.fateubw.platform.ClientPlatform;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public class ClientPlatformImpl implements ClientPlatform {

    @Override
    public void renderModelList(ItemRenderer renderer, BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer) {
        ((ItemRendererAccessor) renderer).doRenderModelLists(model, stack, combinedLight, combinedOverlay, poseStack, buffer);
    }
}
