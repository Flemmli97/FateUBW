package io.github.flemmli97.fateubw.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.client.model.EAModel;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderEAItem extends BlockEntityWithoutLevelRenderer {

    private static final ResourceLocation TEXTURE = Fate.modRes("textures/items/enuma_elish.png");

    private final EAModel model;
    private final RenderUtils.BeamBuilder beam = createBeam();

    public RenderEAItem(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.model = new EAModel();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        render(stack, transformType, matrixStack, buffer, combinedLight, combinedOverlay, this.beam, this.model);
    }

    public static RenderUtils.BeamBuilder createBeam() {
        RenderUtils.BeamBuilder beam = new RenderUtils.BeamBuilder();
        beam.setStartColor(0, 0, 0, 200);
        beam.setRenderType(FateRenders.TRANSLUCENTCOLOR);
        return beam;
    }

    public static void render(ItemStack stack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, RenderUtils.BeamBuilder beam, EAModel model) {
        matrixStack.pushPose();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer builder = ItemRenderer.getFoilBufferDirect(buffer, model.renderType(TEXTURE), true, stack.hasFoil());
        model.spinBlade(ClientHandler.clientTick, ClientHandler.getPartialTicks());
        model.renderToBuffer(matrixStack, builder, combinedLight, combinedOverlay, CommonColors.WHITE);
        matrixStack.popPose();

        if (stack.has(FateDataComponents.GLOWING_ITEM.get()) && transformType != ItemDisplayContext.GUI) {
            matrixStack.pushPose();
            matrixStack.translate(0, -0.6, 0);
            beam.setEndColor(255, 0, 0, 0);
            RenderUtils.renderGradientBeams3d(matrixStack, buffer, 1.5f, 0.5f, ClientHandler.clientTick, ClientHandler.getPartialTicks(), 90 / 200f, 5, beam);
            matrixStack.mulPose(Axis.XP.rotationDegrees(45));
            matrixStack.mulPose(Axis.YP.rotationDegrees(45));
            matrixStack.mulPose(Axis.ZP.rotationDegrees(45));
            beam.setEndColor(0, 0, 0, 50);
            RenderUtils.renderGradientBeams3d(matrixStack, buffer, 1.5f, 0.5f, ClientHandler.clientTick, ClientHandler.getPartialTicks(), 90 / 200f, 9, beam);
            matrixStack.popPose();
        }
    }
}
