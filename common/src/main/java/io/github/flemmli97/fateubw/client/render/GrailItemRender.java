package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.item.ItemEntity;

public class GrailItemRender {

    private static final RenderUtils.BeamBuilder BUILDER = new RenderUtils.BeamBuilder();

    public static void renderBeams(ItemEntity entity, PoseStack poseStack, MultiBufferSource buffer, float partialTicks) {
        if (entity.getItem().isEmpty() || entity.getItem().getItem() != ModItems.GRAIL.get())
            return;
        poseStack.pushPose();
        poseStack.translate(0, 0.15, 0);
        BUILDER.setEndColor(247, 200, 35, 20);
        RenderUtils.renderGradientBeams3d(poseStack, buffer, 1, 0.25f, entity.tickCount, partialTicks, 0.5f, 10, BUILDER);
        poseStack.popPose();
    }
}
