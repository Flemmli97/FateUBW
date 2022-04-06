package io.github.flemmli97.fateubw.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ClientHandler;
import io.github.flemmli97.fateubw.client.model.ModelEA;
import io.github.flemmli97.fateubw.client.render.CustomRenderTypes;
import io.github.flemmli97.fateubw.common.attachment.ItemStackData;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class RenderEAItem extends BlockEntityWithoutLevelRenderer {

    private static final Random random = new Random(432L);
    private static final float triangleMult = (float) (Math.sqrt(3.0D) / 2.0D);

    private final ModelEA model;
    private static final ResourceLocation base = new ResourceLocation(Fate.MODID, "textures/items/ea/ea_base.png");
    private static final ResourceLocation blade = new ResourceLocation(Fate.MODID, "textures/items/ea/ea_blade.png");
    private final RenderUtils.BeamBuilder beam = createBeam();

    public RenderEAItem(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.model = new ModelEA(entityModelSet.bakeLayer(ModelEA.LAYER_LOCATION));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        render(stack, transformType, matrixStack, buffer, combinedLight, combinedOverlay, beam, model);
    }

    public static RenderUtils.BeamBuilder createBeam() {
        RenderUtils.BeamBuilder beam = new RenderUtils.BeamBuilder();
        beam.setStartColor(0, 0, 0, 200);
        beam.setRenderType(CustomRenderTypes.TRANSLUCENTCOLOR);
        return beam;
    }

    public static void render(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, RenderUtils.BeamBuilder beam, ModelEA model) {
        matrixStack.pushPose();
        matrixStack.scale(1.0F, -1.0F, -1.0F);

        model.setBase(true);
        VertexConsumer builder = ItemRenderer.getFoilBufferDirect(buffer, model.renderType(base), true, stack.hasFoil());
        model.renderToBuffer(matrixStack, builder, combinedLight, combinedOverlay, 1, 1, 1, 1);

        model.setBase(false);
        model.spinBlade(ClientHandler.clientTick, ClientHandler.getPartialTicks());
        VertexConsumer builder2 = ItemRenderer.getFoilBufferDirect(buffer, model.renderType(blade), true, stack.hasFoil());
        model.renderToBuffer(matrixStack, builder2, combinedLight, combinedOverlay, 1, 0, 0, 1);
        matrixStack.popPose();

        if (Platform.INSTANCE.getItemStackData(stack).map(ItemStackData::inUse).orElse(false) && transformType != ItemTransforms.TransformType.GUI) {
            matrixStack.pushPose();
            matrixStack.translate(0, -0.6, 0);
            beam.setEndColor(255, 0, 0, 0);
            RenderUtils.renderGradientBeams3d(matrixStack, buffer, 1.5f, 0.5f, ClientHandler.clientTick, ClientHandler.getPartialTicks(), 90 / 200f, 5, beam);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(45));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(45));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(45));
            beam.setEndColor(0, 0, 0, 50);
            RenderUtils.renderGradientBeams3d(matrixStack, buffer, 1.5f, 0.5f, ClientHandler.clientTick, ClientHandler.getPartialTicks(), 90 / 200f, 9, beam);
            matrixStack.popPose();
        }
    }
}
