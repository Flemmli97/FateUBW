package io.github.flemmli97.fate.client.render.item;

import com.flemmli97.tenshilib.client.render.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.client.ClientHandler;
import io.github.flemmli97.fate.client.model.ModelEA;
import io.github.flemmli97.fate.client.render.CustomRenderTypes;
import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import io.github.flemmli97.fate.common.capability.ItemStackCap;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Random;

public class RenderEAItem extends ItemStackTileEntityRenderer {

    private static final Random random = new Random(432L);
    private static final float triangleMult = (float) (Math.sqrt(3.0D) / 2.0D);

    private final ModelEA model = new ModelEA();
    private final ResourceLocation base = new ResourceLocation(Fate.MODID, "textures/items/ea/ea_base.png");
    private final ResourceLocation blade = new ResourceLocation(Fate.MODID, "textures/items/ea/ea_blade.png");
    private final RenderUtils.BeamBuilder beam = new RenderUtils.BeamBuilder();

    public RenderEAItem() {
        this.beam.setStartColor(0, 0, 0, 200);
    }

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        this.beam.setRenderType(CustomRenderTypes.TRANSLUCENTCOLOR);
        matrixStack.push();
        matrixStack.scale(1.0F, -1.0F, -1.0F);

        this.model.setBase(true);
        IVertexBuilder builder = ItemRenderer.getEntityGlintVertexBuilder(buffer, this.model.getRenderType(this.base), true, stack.hasEffect());
        this.model.render(matrixStack, builder, combinedLight, combinedOverlay, 1, 1, 1, 1);

        this.model.setBase(false);
        this.model.spinBlade(ClientHandler.clientTick, ClientHandler.getPartialTicks());
        IVertexBuilder builder2 = ItemRenderer.getEntityGlintVertexBuilder(buffer, this.model.getRenderType(this.blade), true, stack.hasEffect());
        this.model.render(matrixStack, builder2, combinedLight, combinedOverlay, 1, 0, 0, 1);
        matrixStack.pop();

        if (stack.getCapability(CapabilityInsts.ItemStackCap).map(ItemStackCap::inUse).orElse(false) && transformType != ItemCameraTransforms.TransformType.GUI) {
            matrixStack.push();
            matrixStack.translate(0, -0.6, 0);
            this.beam.setEndColor(255, 0, 0, 0);
            RenderUtils.renderGradientBeams3d(matrixStack, buffer, 1.5f, 0.5f, ClientHandler.clientTick, ClientHandler.getPartialTicks(), 90 / 200f, 5, this.beam);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(45));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(45));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(45));
            this.beam.setEndColor(0, 0, 0, 50);
            RenderUtils.renderGradientBeams3d(matrixStack, buffer, 1.5f, 0.5f, ClientHandler.clientTick, ClientHandler.getPartialTicks(), 90 / 200f, 9, this.beam);
            matrixStack.pop();
        }
    }
}
