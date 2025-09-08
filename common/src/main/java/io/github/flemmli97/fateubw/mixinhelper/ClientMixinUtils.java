package io.github.flemmli97.fateubw.mixinhelper;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;

public class ClientMixinUtils {

    public static ItemStack currentItemRenderContext;

    public static VertexConsumer getConsumerOnContext(MultiBufferSource buffer, RenderType renderType) {
        if (currentItemRenderContext != null && currentItemRenderContext.has(FateDataComponents.CORRUPTED_ITEM.get())) {
            return VertexMultiConsumer.create(buffer.getBuffer(FateRenders.CORRUPTED_OVERLAY), buffer.getBuffer(renderType));
        }
        return null;
    }
}
