package io.github.flemmli97.fateubw.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.Random;

public class RenderUBWSky {

    private static final ReloadableCache<ModelPartsContainer> UBW_GEAR = GeoModelManager.getInstance().getModel(Fate.modRes("gear"));
    private static final ResourceLocation UBW_GEAR_TEXTURE = Fate.modRes("textures/environment/gear.png");
    private static final Random RANDOM = new Random();
    private static GearData[] gearData;

    public static void renderUBWSky(ClientLevel level, VertexBuffer skyBuffer, VertexBuffer darkBuffer, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Runnable skyFogSetup) {
        skyFogSetup.run();
        PoseStack stack = new PoseStack();
        stack.mulPose(frustumMatrix);

        Vec3 pos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        Vec3 color = Vec3.fromRGB24(level.getBiomeManager().getNoiseBiomeAtPosition(pos.x(), pos.y(), pos.z()).value().getSkyColor());
        FogRenderer.levelFogColor();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor((float) color.x, (float) color.y, (float) color.z, 1);
        ShaderInstance shaderinstance = RenderSystem.getShader();
        skyBuffer.bind();
        skyBuffer.drawWithShader(stack.last().pose(), projectionMatrix, shaderinstance);
        VertexBuffer.unbind();
        renderGears(stack, partialTick);
        RenderSystem.setShaderColor(0, 0, 0, 1);
        double d0 = Minecraft.getInstance().player.getEyePosition(partialTick).y - level.getLevelData().getHorizonHeight(level);
        if (d0 < 0) {
            stack.pushPose();
            stack.translate(0, 12, 0);
            darkBuffer.bind();
            darkBuffer.drawWithShader(stack.last().pose(), projectionMatrix, shaderinstance);
            VertexBuffer.unbind();
            stack.popPose();
        }
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.depthMask(true);
    }

    private static void renderGears(PoseStack stack, float partialTick) {
        Player player = Minecraft.getInstance().player;
        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        float tick = player.tickCount + partialTick;
        calculateGearData();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        VertexConsumer consumer = buffers.getBuffer(RenderType.entityTranslucent(UBW_GEAR_TEXTURE));
        for (GearData data : gearData) {
            stack.pushPose();
            stack.mulPose(Axis.YP.rotationDegrees(data.yRot()));
            stack.mulPose(Axis.XP.rotationDegrees(-data.xRot()));
            stack.scale(-1, -1, 1);
            stack.translate(0, 20 + data.offset(), 0);
            stack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            stack.mulPose(Axis.YP.rotationDegrees(data.yModelRot()));
            stack.scale(4, 4, 4);
            ModelPartsContainer model = UBW_GEAR.get();
            if (model != null) {
                model.resetPoses();
                model.getPart("root").zRot = tick * data.rotation() * Mth.DEG_TO_RAD;
                model.getRoot().render(stack, consumer, 0xffffff, OverlayTexture.NO_OVERLAY);
            }
            stack.popPose();
        }
        buffers.endLastBatch();
    }

    private static void calculateGearData() {
        if (gearData != null)
            return;
        RANDOM.setSeed(Minecraft.getInstance().player.getUUID().getLeastSignificantBits());
        gearData = new GearData[16];
        for (int i = 0; i < gearData.length; i++) {
            float yRot = RANDOM.nextFloat() * 360;
            float xRot = 85 + RANDOM.nextFloat() * 40;
            int tries = 0;
            while (isTooClose(xRot, yRot) && tries < 64) {
                yRot = RANDOM.nextFloat() * 360;
                xRot = 85 + RANDOM.nextFloat() * 40;
                tries++;
            }
            float rot = 0.5f + RANDOM.nextFloat() + 0.5f;
            gearData[i] = new GearData(yRot, xRot, RANDOM.nextInt(10), RANDOM.nextInt(20) - 10, RANDOM.nextBoolean() ? rot : -rot);
        }
    }

    private static boolean isTooClose(float xRot, float yRot) {
        for (GearData data : gearData) {
            if (data != null) {
                float dx = data.xRot() - xRot;
                float dy = data.yRot() - yRot;
                if (dx * dx + dy * dy < 25 * 25) {
                    return true;
                }
            }
        }
        return false;
    }

    private record GearData(float yRot, float xRot, float offset, float yModelRot, float rotation) {
    }
}
