package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.render.FateRenders;
import io.github.flemmli97.fateubw.common.entity.misc.BabylonWeapon;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileItem;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

public class RenderBabylon extends RenderProjectileItem<BabylonWeapon> {

    public static final ResourceLocation BABYLON_IDLE = new ResourceLocation(Fate.MODID, "textures/entity/babylon.png");

    private static final MultiBufferSource.BufferSource SEP = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

    private final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    public RenderBabylon(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(BabylonWeapon projectile, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        if (projectile.idle) {
            stack.pushPose();
            RenderUtils.applyYawPitch(stack, Mth.lerp(partialTicks, projectile.yRotO, projectile.getYRot()),
                    Mth.lerp(partialTicks, projectile.xRotO, projectile.getXRot()));
            float ripple = Mth.sin((projectile.tickCount + projectile.renderRand) / 2f) * 0.025f + 1;
            float size = (float) (1.45 * ripple);
            this.textureBuilder.setLight(packedLight);
            RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(BABYLON_IDLE)), size, size, this.textureBuilder);
            stack.popPose();
        }
        stack.pushPose();
        if (projectile.idle) {
            float yRot = Mth.lerp(partialTicks, projectile.yRotO, projectile.getYRot());
            float xRot = Mth.lerp(partialTicks, projectile.xRotO, projectile.getXRot());
            stack.mulPose(Vector3f.YP.rotationDegrees(yRot));
            stack.mulPose(Vector3f.ZP.rotationDegrees(xRot));
            stack.translate(0, 0, -2 * (0.8 - projectile.preparationState(partialTicks)));
            stack.mulPose(Vector3f.ZP.rotationDegrees(-xRot));
            stack.mulPose(Vector3f.YP.rotationDegrees(-yRot));
        }
        // Item rendering sometimes use double vertexconsumer but clipped rendertype will always return default and thus crash
        // Use separate buffersource for that instead
        AtomicInteger state = new AtomicInteger();
        MultiBufferSource buf = projectile.idle ? renderType -> {
            Vector3f normal = new Vector3f(0, 0, 1);
            normal.transform(Vector3f.YN.rotationDegrees(-Mth.rotLerp(partialTicks, projectile.yRotO, projectile.getYRot())));
            RenderType rendertype = FateRenders.getClippedRendertype(renderType, FateRenders.createClippingPlane(normal, projectile, 0));
            int current = state.get();
            VertexConsumer cons = current == 1 ? SEP.getBuffer(rendertype) : buffer.getBuffer(rendertype);
            if (current == 0 || current == 2)
                state.set(1);
            else
                state.set(2);
            return cons;
        } : buffer;
        super.render(projectile, rotation, partialTicks, stack, buf, packedLight);
        if (state.get() != 0) // other buffersource was used
            SEP.endBatch();
        stack.popPose();
    }

    @Override
    public ItemStack getRenderItemStack(BabylonWeapon entity) {
        return entity.getWeapon();
    }

    @Override
    public Type getRenderType(BabylonWeapon entity) {
        return Type.WEAPON;
    }
}