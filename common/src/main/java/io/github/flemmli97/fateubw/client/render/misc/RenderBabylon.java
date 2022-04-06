package io.github.flemmli97.fateubw.client.render.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.BabylonWeapon;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileItem;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class RenderBabylon extends RenderProjectileItem<BabylonWeapon> {

    public final ResourceLocation babylonIddle = new ResourceLocation(Fate.MODID, "textures/entity/babylon.png");
    private final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    public RenderBabylon(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(BabylonWeapon projectile, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        if (projectile.iddle) {
            stack.pushPose();
            RenderUtils.applyYawPitch(stack, Mth.lerp(partialTicks, projectile.yRotO, projectile.getYRot()),
                    Mth.lerp(partialTicks, projectile.xRotO, projectile.getXRot()));
            float ripple = Mth.sin((projectile.tickCount + projectile.renderRand) / 2f) * 0.025f + 1;
            float size = (float) (1.45 * ripple);
            this.textureBuilder.setLight(packedLight);
            RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(this.babylonIddle)), size, size, this.textureBuilder);
            stack.popPose();
        }
        super.render(projectile, rotation, partialTicks, stack, buffer, packedLight);
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